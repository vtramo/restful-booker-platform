package e2e;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Optional;

public class WebDrivers {
    public static String TEST_PILOT_WEB_DRIVER = Optional.ofNullable(System.getenv("TEST_PILOT_WEB_DRIVERR")).orElse("chrome");

    public static WebDriver buildWebDriver() {
        switch (TEST_PILOT_WEB_DRIVER) {
            case "firefox": {
                System.setProperty("webdriver.gecko.driver","/snap/bin/geckodriver");
                FirefoxDriver firefoxDriver = new FirefoxDriver(new FirefoxOptions().addArguments("--headless", "--disable-dev-tools", "--no-sandbox"));
                firefoxDriver.manage().window().maximize();
                return firefoxDriver;
            }
            case "edge":
                return new EdgeDriver();
            default:
                return new ChromeDriver(new ChromeOptions().addArguments("--headless", "--disable-dev-tools", "--no-sandbox"));
        }
    }
}
