package org.omega.tpo3.pages;

import org.omega.tpo3.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

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

    private WebElement getFilterCheckboxElement(String choice) {
        String path = "//div[@class='checkbox-with-text j-list-item'][span[@class='checkbox-with-text__text'][contains(text(),'"+ choice+"')]]";
        return driver.findElement(By.xpath(path));
    }

    public void clickOnFilterCheckBox(String choice) {
        WebElement element = getFilterCheckboxElement(Constants.FILTER_CATEGORY_CHOICE_TSHIRT);
        getWait().until(visibilityOf(element));

        clickOnFilterCheckbox(Constants.FILTER_CATEGORY_CHOICE_TSHIRT);
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

    public void setCostRangeFilter(String start, String end) {
        getWait().until(visibilityOf(costFilterButton));
        costFilterButton.click();

        getWait().until(visibilityOf(startCostInput));
        startCostInput.clear();
        startCostInput.sendKeys(start);
        endCostInput.clear();
        endCostInput.sendKeys(end);
        submitCostFilterButton.click();
    }

    public void waitFilterChoiceListVisible() {
        getWait().until(visibilityOf(filterChoiceList));
    }

    public void waitDeleteBucketButtonInvisible() {
        getWait().until(invisibilityOf(deleteBucketButton));
    }

    public String getFilterChoiceListElement(int index) {
        waitFilterChoiceListVisible();
        List<WebElement> elementList = getChilds(filterChoiceList);
        return elementList.get(index).getText();
    }

    public void clickFilterCategoryButton() {
        getWait().until(visibilityOf(filterCategoryButton));
        filterCategoryButton.click();
        getWait().until(visibilityOf(filterFrame));
    }

    public int getFilterChoiceListSize() {
        return getChilds(filterChoiceList).size();
    }

    public void waitForFilterCategoryNotebookButtonSelectedInVisibility() {
        getWait().until(invisibilityOf(filterCategoryNotebookButtonSelected));
    }

    public void clickResetFilterChoicesButton() {
        clickOnElement(resetChoicesButton);
    }

    public void inputToFilterSearch(String search) {
        filterSearchInput.sendKeys(search);
    }

    public void clickOnProductCard(int ind) {
        List<WebElement> productCards = getProductCards();
        clickOnElement(productCards.get(0));
        getWait().until(visibilityOf(nameOfItem));
    }

    public String getNameOfCurrentItem() {
        return nameOfItem.getText();
    }

    public void addItemToBacket(int ind) throws InterruptedException {
        List<WebElement> productCards = getDriver().findElements(By.xpath("//a[@class='product-card__add-basket j-add-to-basket btn-main']"));
        getWait().until(visibilityOf(productCards.get(ind)));
        WebElement card = productCards.get(ind);
        clickOnElement(card);
        Thread.sleep(100);
    }

    public void waitForBacketGoodsNumberVisible() {
        getWait().until(visibilityOf(backetGoodsNumber));
    }

    public int getBacketGoodsNumbers() {
        String regex = "\\d+";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(backetGoodsNumber.getText());
        if (matcher.find()) {
            String numberStr = matcher.group();
            int number = Integer.parseInt(numberStr);
            return number;
        }
        return 0;
    }

    public void pickTShirtSizeLabel(final String size) {
        getWait().until(visibilityOf(choiceTShirtSizeLabel));
        clickOnElement(choiceTShirtSizeLabel);
    }

    public String getItemsProps() {
        WebElement itemProps = getDriver().findElement(By.xpath("//div[@class='good-info__properties ']"));
        getWait().until(visibilityOf(itemProps));
        return itemProps.getText();
    }

    public void clickOnBacketPlusButton() {
        clickOnElement(bucketPlusButton);
    }

    public void waitForEndCostInputInvisibility(){
        getWait().until(invisibilityOf(endCostInput));
    }

    public void clickOnDeleteBucketButton() {
        clickOnElement(deleteBucketButton);
    }

    public String getBucketHeader() {
        WebElement header = getDriver().findElement(By.xpath("//h1[@class='section-header basket-empty__title']"));

        getWait().until(visibilityOf(header));
        return header.getText();
    }

    public void clickOnAllFiltersButton() {
        clickOnElement(allFiltersButton);
    }

    public String getItemsParams() {
        List<WebElement> params =getDriver().findElements(By.xpath("//td[@class='product-params__cell']"));
        getWait().until(visibilityOf(params.get(0)));

        String collect = params.stream().map(WebElement::getText).collect(Collectors.joining());
        return collect;
    }
}
