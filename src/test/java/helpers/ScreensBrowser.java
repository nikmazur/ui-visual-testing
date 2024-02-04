package helpers;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;
import com.github.romankh3.image.comparison.model.Rectangle;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.codeborne.selenide.Screenshots.takeScreenShotAsImage;
import static com.codeborne.selenide.Selenide.$x;
import static com.github.romankh3.image.comparison.ImageComparisonUtil.readImageFromResources;
import static com.github.romankh3.image.comparison.ImageComparisonUtil.saveImage;
import static org.testng.Assert.assertEquals;

public class ScreensBrowser {

    static final String S = File.separator;
    static final String SCREENS_PATH = "src" + S + "test" + S + "resources" + S + "screens" + S;
    static final String PROJ_PATH = new File("").getAbsolutePath();

    @BeforeMethod(alwaysRun = true, description = "Browser Setup")
    public void setup() {
        WebDriverManager.firefoxdriver().setup();
        Configuration.browser = "firefox";
        // Browser needs to be headless to avoid screenshotting viewport instead of the full page
        Configuration.headless = true;
    }

    @Step("Open site home page")
    public static void openMainPage() {
        Selenide.open("file://" + PROJ_PATH + S + "src" + S + "test" + S + "resources" + S +
                "site" + S + "demo.seleniumeasy.com" + S + "index.html");
    }

    @Step("Save screenshot to file")
    public void saveScreen(String className, String methodName, SelenideElement elem) {
        // If SelenideElement is null - save image of the whole page, else of the element
        saveImage(new File(SCREENS_PATH + className + S + methodName + ".png"),
                takeScreenShotAsImage(Objects.requireNonNullElseGet(elem, () -> $x("/html"))));
    }

    @Step("Calculate {0} coordinates for adding to ignore list")
    public Rectangle calcElemLocation(String elemName, SelenideElement elem) {
        int minX = elem.getRect().getX();
        int minY = elem.getRect().getY();
        int maxX = minX + elem.getRect().getWidth();
        int maxY = minY + elem.getRect().getHeight();
        return new Rectangle(minX, minY, maxX, maxY);
    }

    @Step("Compare current page screenshot with saved file")
    public void assertPage(String className, String methodName) {
        assertScreens(className, methodName, null, Collections.emptyList(), 0);
    }

    @Step("Compare area of the current page screenshot with saved file")
    public void assertPageArea(String className, String methodName, SelenideElement elem) {
        assertScreens(className, methodName, elem, Collections.emptyList(), 0);
    }

    @Step("Compare current page screenshot (excluding ignored areas) with saved file")
    public void assertPageWIgnore(String className, String methodName, List<Rectangle> ignores) {
        assertScreens(className, methodName, null, ignores, 0);
    }

    @Step("Compare current page screenshot with {2} pixels ignored")
    public void assertPageWFailPixels(String className, String methodName, long failPixels) {
        assertScreens(className, methodName, null, Collections.emptyList(), failPixels);
    }

    private void assertScreens(String className, String methodName, SelenideElement elem, List<Rectangle> ignores, long failPixels) {
        // Fluent wait for page to fully load using JS readyState
        Methods.waitForSuccess(()->
                        assertEquals(Selenide.executeJavaScript("return document.readyState").toString(), "complete"),
                10, 200);

        // Checks if screen file exists. If not - create a new one
        if(!new File(SCREENS_PATH + className + S + methodName + ".png").exists())
            saveScreen(className, methodName, elem);

        var expected = readImageFromResources(SCREENS_PATH + className + S + methodName + ".png");
        // If SelenideElement is null - take screen of the whole page, else of the element
        var actual = takeScreenShotAsImage(Objects.requireNonNullElseGet(elem, () -> $x("/html")));

        var result = getResult(expected, actual, ignores, failPixels);

        if(result.getImageComparisonState() == ImageComparisonState.MATCH)
            attachPng("Result", result.getResult());
        else
            attachFailResults(className, methodName, result);

        assertEquals(result.getImageComparisonState(), ImageComparisonState.MATCH);
    }

    private ImageComparisonResult getResult(BufferedImage expected, BufferedImage actual, List<Rectangle> ignores, long failPixels) {
        return new ImageComparison(expected, actual)
                // Enable and set opacity for ignored areas (will be displayed as green on result image)
                .setDrawExcludedRectangles(true).setExcludedRectangleFilling(true, 50).setExcludedAreas(ignores)
                // Allowed percentage of difference for failure. If failPixels != 0 calculate percentage
                .setAllowingPercentOfDifferentPixels(failPixels == 0 ? 0 : (failPixels * 100.0) / getTotalPixels(actual))
                // Set opacity for difference areas (will be displayed as red on result image)
                .setDifferenceRectangleFilling(true, 50)
                .compareImages();
    }

    private void attachFailResults(String className, String methodName, ImageComparisonResult result) {
        // getDifferencePercent is calculated incorrectly, for future use
        // TODO https://github.com/romankh3/image-comparison/issues/233
        Allure.addAttachment("Failed Percent", result.getDifferencePercent() + " %");
        Allure.addAttachment("Failed Pixels", String.valueOf(
                (long) (getTotalPixels(result.getActual()) * (result.getDifferencePercent() / 100.0))));
        attachPng("Expected", result.getExpected());
        attachPng("Actual", result.getActual());
        attachGif(className, methodName, result);
    }

    private long getTotalPixels(BufferedImage img) {
        return (long) img.getWidth() * img.getHeight();
    }

    @Attachment(value = "{0}", type = "image/png")
    private static byte[] attachPng(String fileName, BufferedImage img) {
        var stream = new ByteArrayOutputStream();
        var byteImage = new byte[0];
        try {
            ImageIO.write(img, "png", stream);
            stream.flush();
            byteImage = stream.toByteArray();
            stream.close();
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage());
        }
        return byteImage;
    }

    @Attachment(value = "GIF", type = "image/gif")
    private static byte[] attachGif(String className, String methodName, ImageComparisonResult res) {
        var gif = new File("build" + S + "resources" + S + "test" + S + className + S + methodName + ".gif");
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
        return byteGif;
    }
}
