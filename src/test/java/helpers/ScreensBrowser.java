package helpers;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.github.romankh3.image.comparison.model.Rectangle;
import io.qameta.allure.Step;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import static helpers.Methods.PROJ_PATH;
import static helpers.Methods.S;

public class ScreensBrowser {

    @BeforeMethod(alwaysRun = true, description = "Browser Setup")
    public void setup() {
        WebDriverRunner.setWebDriver(new FirefoxDriver(
                new FirefoxOptions().addArguments("-headless", "-width=1280", "-height=720")));
    }

    @AfterMethod(alwaysRun = true, description = "Close Browser")
    public void closeBrowser() {
        WebDriverRunner.getWebDriver().close();
    }

    @Step("Open site home page")
    public static void openMainPage() {
        Selenide.open("file://" + PROJ_PATH + S + "src" + S + "test" + S + "resources" + S +
                "site" + S + "demo.seleniumeasy.com" + S + "index.html");
    }

    @Step("Calculate {0} coordinates for adding to ignore list")
    public Rectangle calcElemLocation(String elemName, SelenideElement elem) {
        int minX = elem.getRect().getX();
        int minY = elem.getRect().getY();
        int maxX = minX + elem.getRect().getWidth();
        int maxY = minY + elem.getRect().getHeight();
        return new Rectangle(minX, minY, maxX, maxY);
    }

    @Step("Change browser window resolution to {0} ")
    static void changeBrowserResolution(String resolution) {
        try{
            var dimensions = resolution.toLowerCase().split("x");
            WebDriverRunner.getWebDriver().manage().window().setSize(
                    new Dimension(Integer.parseInt(dimensions[0].trim()), Integer.parseInt(dimensions[1].trim())));
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException | InvalidArgumentException e) {
            throw new AssertionError(resolution + " is invalid. Resolution format should be: 0x0");
        }
    }
}
