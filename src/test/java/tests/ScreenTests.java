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

@Epic("Web Testing")
@Feature("Testing using screenshots")
@Test(groups = "Screens")
public class ScreenTests extends ScreensBrowser {
    private final String CLASSNAME = this.getClass().getSimpleName();

    @Test(description = "Selenium Easy home page")
    public void selEasyHomePage() {
        openMainPage();
        assertPage(CLASSNAME, new Object(){}.getClass().getEnclosingMethod().getName());
    }

    @Test(description = "Specific area on a page (banner)")
    @Description("Compare screens of a specific page area")
    public void siteBanner() {
        openMainPage();
        assertPageArea(CLASSNAME, new Object(){}.getClass().getEnclosingMethod().getName(),
                $x("//div[@role='listbox']"));
    }

    @Test(description = "Failing test with edited screen")
    @Description("Failing test where the expected screen has been manually edited in paint")
    public void editedImage() {
        openMainPage();
        assertPage(CLASSNAME, new Object(){}.getClass().getEnclosingMethod().getName());
    }

    @Test(description = "Page assert with ignored area")
    @Description("The expected screen has been edited in paint by adding 2 lines - red and blue. " +
            "However the red line is over the banner area, which is ignored, while blue is not.")
    public void pageWIgnoredArea() {
        openMainPage();
        List<Rectangle> ignore = Collections.singletonList(
                calcElemLocation("Banner", $x("//div[@role='listbox']")));
        assertPageWIgnore(CLASSNAME, new Object(){}.getClass().getEnclosingMethod().getName(), ignore);
    }

    @Test(description = "Test with allowed percentage of differences")
    @Description("This test uses same screenshot as editedImage test, but passes because of allowed amount of different pixels.")
    public void failPercentage() {
        openMainPage();
        assertPageWFailPixels(CLASSNAME, new Object(){}.getClass().getEnclosingMethod().getName(), 300_000);
    }
}
