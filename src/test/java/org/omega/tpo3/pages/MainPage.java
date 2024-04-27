package org.omega.tpo3.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class MainPage extends Page{

    @FindBy(xpath = "//a[.//script[@type='jsv#91^']]")
    public WebElement linkCardMain;

    @FindBy(xpath = "//span[contains(@class, 'navbar-pc__icon--basket')]")
    public WebElement basketElement;

    @FindBy(xpath = "//*[@id='searchInput']")
    public WebElement inputSearchText;

    @FindBy(xpath = "//a[contains(@aria-label, 'RTL')]")
    public WebElement linkToItemCard;

    @FindBy(xpath = "//h1[@class=\"product-page__title\"]")
    public WebElement nameOfItem;

    @FindBy(xpath = "//ul[@class='your-choice__list']")
    public WebElement filterChoiceList;

    @FindBy(xpath = "//button[@class=\"dropdown-filter__btn\"][contains(text(), 'Цена')]")
    public WebElement costFilterButton;

    @FindBy(xpath = "//input[@name='startN'][@class='j-price']")
    public WebElement startCostInput;

    @FindBy(xpath = "//input[@name='endN'][@class='j-price']")
    public WebElement endCostInput;

    @FindBy(xpath = "//button[contains(@class, 'filter-btn__main')]")
    public WebElement submitCostFilterButton;


    public List<WebElement> getCostsOnPage(){
        return driver.findElements(By.xpath("//ins[@class='price__lower-price wallet-price']"));
    }


    public MainPage(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(driver,this);
    }


}
