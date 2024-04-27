package org.omega.tpo3.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class SearchPage extends Page{

    @FindBy(xpath = "//*[@id='searchInput']")
    public WebElement inputSearchText;

    @FindBy(xpath = "//button[@class=\"dropdown-filter__btn\"][contains(text(), 'Категория')]")
    public WebElement filterCategoryButton;

    @FindBy(xpath = "//div[@class='dropdown-filter open']/div[@class='dropdown-filter__content']")
    public WebElement filterFrame;

    @FindBy(xpath = "//div[@class='checkbox-with-text j-list-item selected'][span[@class='checkbox-with-text__text'][contains(text(), 'Блокнот')]]")
    public WebElement filterCategoryNotebookButtonSelected;

    @FindBy(xpath = "//ul[@class='your-choice__list']")
    public WebElement filterChoiceList;

    //    @FindBy(xpath = "//div[@class='checkbox-with-text j-list-item'][span[@class='checkbox-with-text__text'][contains(text(),'Брелок')]]/span[@class='checkbox-with-text__decor']")
    @FindBy(xpath = "//div[@class='checkbox-with-text j-list-item'][span[@class='checkbox-with-text__text'][contains(text(),'Брелок')]]")
    public WebElement filterChoiceCharm;

    @FindBy(xpath = "//button[@class=\"dropdown-filter__btn\"][contains(text(), 'Цена')]")
    public WebElement costFilterButton;

    @FindBy(xpath = "//input[@name='startN'][@class='j-price']")
    public WebElement startCostInput;

    @FindBy(xpath = "//input[@name='endN'][@class='j-price']")
    public WebElement endCostInput;

    @FindBy(xpath = "//button[contains(@class, 'filter-btn__main')]")
    public WebElement submitCostFilterButton;

    @FindBy(xpath = "//ul[@class='your-choice__list']//button[@class='your-choice__btn']")
    public WebElement resetChoicesButton;

    @FindBy(xpath = "//a[.//script[@type='jsv#91^']]")
    public WebElement linkCardMain;

    @FindBy(xpath = "//h1[@class=\"product-page__title\"]")
    public WebElement nameOfItem;

    @FindBy(xpath = "//div[@class='dropdown-filter open']/div[@class='dropdown-filter__content']//input[@class='j-search-filter']")
    public WebElement filterSearchInput;

    @FindBy(xpath = "//div[@class='accordion__goods-count']")
    public WebElement backetGoodsNumber;

    @FindBy(xpath = "//label[@class='j-quick-order-size-fake sizes-list__button'][span[@class='sizes-list__size'][contains(text(),'M')]]")
    public WebElement choiceTShirtSizeLabel;

    @FindBy(xpath = "//button[@class='count__plus plus']")
    public WebElement bucketPlusButton;

    @FindBy(xpath = "//div[@class='list-item__price-new wallet']")
    public WebElement costOfBucket;

    @FindBy(xpath = "//button[@class = 'btn__del j-basket-item-del']")
    public WebElement deleteBucketButton;

    @FindBy(xpath = "//button[@class='dropdown-filter__btn dropdown-filter__btn--all']")
    public WebElement allFiltersButton;

    public SearchPage(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(driver,this);
    }

    public void waitForPageToLoad() {
        ExpectedCondition<Boolean> pageLoad = driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
        try {
            wait.until(pageLoad);
        } catch (Throwable ignored) {

        }
    }

    public WebElement getFilterCheckboxElement(String choice) {
        String path = "//div[@class='checkbox-with-text j-list-item'][span[@class='checkbox-with-text__text'][contains(text(),'"+ choice+"')]]";
        return driver.findElement(By.xpath(path));
    }

    public void clickOnFilterCheckbox(String choice) {
        WebElement element = getFilterCheckboxElement(choice);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }

    public void clickOnFilterCheckboxSelected(String choice) {
        String path = "//div[@class='checkbox-with-text j-list-item selected'][span[@class='checkbox-with-text__text'][contains(text(),'"+ choice+"')]]";
        WebElement element = driver.findElement(By.xpath(path));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }
}
