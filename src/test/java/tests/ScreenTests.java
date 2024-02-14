package tests;

import com.github.romankh3.image.comparison.model.Rectangle;
import helpers.ScreensBrowser;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static com.codeborne.selenide.Selenide.$x;
import static helpers.PageAsserts.*;

@Epic("Web Testing")
@Feature("Testing using screenshots")
@Test(groups = "Screens")
public class ScreenTests extends ScreensBrowser {
    private final String CLASS_NAME = this.getClass().getSimpleName();

    @Test(description = "Selenium Easy home page")
    public void selEasyHomePage() {
        openMainPage();
        assertPage(CLASS_NAME, new Object(){}.getClass().getEnclosingMethod().getName());
    }

    @Test(description = "Specific area on a page (banner)")
    @Description("Compare screens of a specific page area")
    public void siteBanner() {
        openMainPage();
        assertPageArea(CLASS_NAME, new Object(){}.getClass().getEnclosingMethod().getName(),
                $x("//div[@role='listbox']"));
    }

    @Test(description = "Home page with multiple resolutions")
    @Description("Compare screens with multiple custom resolutions (browser is resized for each resolution)")
    public void multipleResolutions() {
        openMainPage();
        assertPageWCustomResolutions(CLASS_NAME, new Object(){}.getClass().getEnclosingMethod().getName(),
                List.of("360x800", "1024x576", "1920x1080", "3840x2160"));
    }

    @Test(description = "Failing test with edited screen")
    @Description("Failing test where the expected screen has been manually edited in Paint")
    public void editedImage() {
        openMainPage();
        assertPage(CLASS_NAME, new Object(){}.getClass().getEnclosingMethod().getName());
    }

    @Test(description = "Page assert with ignored area")
    @Description("""
            The expected screen has been edited in paint by adding a blue line.
            However part of the line is over the ignored banner area, so it is not marked as difference.
            The remaining part of the line is marked as different, causing the test to fail.""")
    public void pageWIgnoredArea() {
        openMainPage();
        List<Rectangle> ignore = Collections.singletonList(
                calcElemLocation("Banner", $x("//div[@role='listbox']")));
        assertPageWIgnore(CLASS_NAME, new Object(){}.getClass().getEnclosingMethod().getName(), ignore);
    }

    @Test(description = "Test with allowed percentage of differences")
    @Description("This test uses same screenshot as editedImage test, but passes because of allowed amount of different pixels.")
    public void failPercentage() {
        openMainPage();
        assertPageWFailPixels(CLASS_NAME, new Object(){}.getClass().getEnclosingMethod().getName(), 300_000);
    }
}
