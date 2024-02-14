package helpers;

import com.codeborne.selenide.SelenideElement;
import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.Rectangle;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.codeborne.selenide.Screenshots.takeScreenShotAsImage;
import static com.codeborne.selenide.Selenide.$x;
import static com.github.romankh3.image.comparison.ImageComparisonUtil.saveImage;
import static java.util.Objects.requireNonNullElseGet;

public class Methods {

    static final String S = File.separator;
    static final String PROJ_PATH = new File("").getAbsolutePath();
    static final String BUILD_PATH = "build" + S + "resources" + S + "test" + S;

    public static void fluentWait(Runnable run, int steps, int pause) {
        var success = false;
        for(var i = 0; i < steps - 1; i++) {
            try {
                run.run();
                success = true;
                break;
            } catch (Exception | AssertionError ae) {
                LockSupport.parkNanos(pause);
            }
        }
        if(!success) run.run();
    }

    @Step("Save screenshot to file")
    static void saveScreen(String filePath, SelenideElement elem) {
        // If SelenideElement is null - save image of the whole page, else of the element
        saveImage(new File(filePath),takeScreenShotAsImage(requireNonNullElseGet(elem, () -> $x("/html"))));
    }

    static ImageComparisonResult getResult(BufferedImage expected, BufferedImage actual, List<Rectangle> ignores, long failPixels) {
        return new ImageComparison(expected, actual)
                // Enable and set opacity for ignored areas (will be displayed as green on result image)
                .setDrawExcludedRectangles(true).setExcludedRectangleFilling(true, 50).setExcludedAreas(ignores)
                // Allowed percentage of difference for failure. If failPixels != 0 calculate percentage
                .setAllowingPercentOfDifferentPixels(failPixels == 0 ? 0 : (failPixels * 100.0) / getTotalPixels(actual))
                // Set opacity for difference areas (will be displayed as red on result image)
                .setDifferenceRectangleFilling(true, 50)
                .compareImages();
    }

    static void attachFailResults(String className, String methodName, String resolution, ImageComparisonResult result) {
        // getDifferencePercent is calculated incorrectly, for future use
        // TODO https://github.com/romankh3/image-comparison/issues/233
        Allure.addAttachment("Failed Percent " + resolution, result.getDifferencePercent() + " %");
        Allure.addAttachment("Failed Pixels " + resolution, String.valueOf(
                (long) (getTotalPixels(result.getActual()) * (result.getDifferencePercent() / 100.0))));
        savePng("Expected " + resolution, result.getExpected(), PROJ_PATH + S +
                "src" + S + "test" + S + "resources" + S + "screens" + S + className + S + methodName + resolution + ".png");
        savePng("Actual " + resolution, result.getActual(),
                BUILD_PATH + "actual" + S + className + S + methodName + resolution + ".png");
        saveGif("GIF " + resolution, className, methodName, resolution, result);
    }

    static long getTotalPixels(BufferedImage img) {
        return (long) img.getWidth() * img.getHeight();
    }

    @Attachment(value = "{0}", type = "image/png")
    static byte[] savePng(String fileName, BufferedImage img, String filePath) {
        var stream = new ByteArrayOutputStream();
        var byteImage = new byte[0];
        try {
            ImageIO.write(img, "png", stream);
            stream.flush();
            byteImage = stream.toByteArray();
            stream.close();
            // Print image link to console
            System.out.println(fileName + ":");
            if(fileName.contains("Screenshot") || fileName.contains("Expected")) {
                System.out.println(new URI(filePath));
            } else {
                var imageFile = new File(filePath);
                saveImage(imageFile, img);
                System.out.println(new URI(imageFile.getAbsolutePath()));
            }
        } catch (IOException | URISyntaxException e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage());
        }
        return byteImage;
    }

    @Attachment(value = "{0}", type = "image/gif")
    static byte[] saveGif(String fileName, String className, String methodName, String resolution, ImageComparisonResult res) {
        var gif = new File(BUILD_PATH + "gifs" + S + className + S + methodName + resolution + ".gif");
        gif.getParentFile().mkdirs();
        var byteGif = new byte[0];
        try {
            var output = new FileImageOutputStream(gif);
            var writer = new GifSequenceWriter(output, BufferedImage.TYPE_INT_ARGB, 1000, true);
            writer.writeToSequence(res.getExpected());
            writer.writeToSequence(res.getActual());
            writer.writeToSequence(res.getResult());
            writer.close();
            output.close();
            byteGif = Files.readAllBytes(Paths.get(gif.toURI()));
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage());
        }
        // Print link to GIF in console
        System.out.println("Differences " + fileName + ":");
        System.out.println(gif.getAbsolutePath());
        return byteGif;
    }

}
