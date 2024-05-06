package org.omega.tpo3.pages;

import org.omega.tpo3.AbstractWebTest;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;


public abstract class Page {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Properties prop;

    public Page(WebDriver driver) {
        this.driver = driver;
        prop = new Properties();
        try {
            prop.load(Page.class.getClassLoader().getResourceAsStream("conf.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public WebDriverWait getWait() {
        return wait;
    }

    public List<WebElement> getChilds(WebElement element) {
        return element.findElements(By.xpath("./child::*"));
    }

    public List<WebElement> getProductCards() {
        return driver.findElements(By.xpath("//a[@class='product-card__link j-card-link j-open-full-product-card']"));
    }

    public void clickOnElement(WebElement element) {
        wait.
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }
}
