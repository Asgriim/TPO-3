package org.omega.tpo3;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractWebTest {
    protected List<WebDriver> drivers = new ArrayList<>();
    protected Properties prop;

    public AbstractWebTest() {
        try {
            setUpDrivers();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        // Fix the issue https://github.com/SeleniumHQ/selenium/issues/11750
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        WebDriver driver = new ChromeDriver(options);

        drivers.add(driver);
    }

    private void addFurry() {
        WebDriver furryFox = new FirefoxDriver();

        drivers.add(furryFox);
    }

    public List<WebDriver> getDrivers() {
        return drivers;
    }

    public void setUpDrivers() throws IOException {
        prop = new Properties();
        prop.load(AbstractWebTest.class.getClassLoader().getResourceAsStream("conf.properties"));
        String property = prop.getProperty("drivers", "all");
        switch (property)  {
            case "chrome" -> {
                addChromeDriver();
            }
            case "fox" -> {
                addFurry();
            }
            case "all" -> {
                addFurry();
                addChromeDriver();
            }
        }


        for (var drive : drivers) {
            drive.manage().window().setSize(new Dimension(1280,720));
            drive.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }
    }

    @AfterAll
    public void closeDrivers(){
        drivers.forEach(WebDriver::quit);
    }
}
