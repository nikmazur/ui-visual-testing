package helpers;

import com.codeborne.selenide.SelenideElement;
import com.github.romankh3.image.comparison.model.ImageComparisonState;
import com.github.romankh3.image.comparison.model.Rectangle;
import io.qameta.allure.Step;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.util.List;
import java.util.Objects;

import static com.codeborne.selenide.Screenshots.takeScreenShotAsImage;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.github.romankh3.image.comparison.ImageComparisonUtil.readImageFromResources;
import static helpers.Methods.*;
import static java.util.Collections.emptyList;
import static org.testng.Assert.assertEquals;

public class PageAsserts {

    @Step("Compare current page screenshot with saved file")
    public static void assertPage(String className, String methodName) {
        assertScreens(className, methodName, null, emptyList(), 0, List.of(""));
    }

    @Step("Compare area of the current page screenshot with saved file")
    public static void assertPageArea(String className, String methodName, SelenideElement elem) {
        assertScreens(className, methodName, elem, emptyList(), 0, List.of(""));
    }

    @Step("Compare current page screenshot with saved files in custom resolutions")
    public static void assertPageWCustomResolutions(String className, String methodName, List<String> resolutions) {
        assertScreens(className, methodName, null, emptyList(), 0, resolutions);
    }

    @Step("Compare current page screenshot (excluding ignored areas) with saved file")
    public static void assertPageWIgnore(String className, String methodName, List<Rectangle> ignores) {
        assertScreens(className, methodName, null, ignores, 0, List.of(""));
    }

    @Step("Compare current page screenshot with {2} pixels ignored")
    public static void assertPageWFailPixels(String className, String methodName, long failPixels) {
        assertScreens(className, methodName, null, emptyList(), failPixels, List.of(""));
    }

    private static void assertScreens(String className, String methodName, SelenideElement elem,
                               List<Rectangle> ignores, long failPixels, List<String> resolutions) {
        // Soft assert for tests with multiple resolutions
        var sAssert = new SoftAssert();
        // Hide scrollbars in browser (they affect screenshot resolution)
        executeJavaScript("$(\"body\").css(\"overflow\", \"hidden\");");

        resolutions.forEach(res -> {
            // Change browser resolution if a custom one is provided
            if(!res.isEmpty()) ScreensBrowser.changeBrowserResolution(res);

            final var FILE_PATH = PROJ_PATH + S + "src" + S + "test" + S + "resources" + S + "screens" + S +
                    className + S + methodName + res + ".png";

            // Fluent wait for page to fully load using JS readyState
            Methods.fluentWait(()->
                            assertEquals(executeJavaScript("return document.readyState").toString(), "complete"),
                    10, 200);

            // Checks if screen file exists. If not - create a new one
            if(!new File(FILE_PATH).exists()) Methods.saveScreen(FILE_PATH, elem);

            var expected = readImageFromResources(FILE_PATH);
            // If SelenideElement is null - take screen of the whole page, else of the element
            var actual = takeScreenShotAsImage(Objects.requireNonNullElseGet(elem, () -> $x("/html")));

            var result = Methods.getResult(expected, actual, ignores, failPixels);

            if(result.getImageComparisonState() == ImageComparisonState.MATCH)
                savePng("Screenshot " + res, result.getResult(), FILE_PATH);
            else
                Methods.attachFailResults(className, methodName, res, result);

            sAssert.assertEquals(result.getImageComparisonState(), ImageComparisonState.MATCH, "Page" + res);
        });

        sAssert.assertAll();
    }

}
