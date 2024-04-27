package org.omega.tpo3;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.omega.tpo3.pages.SearchPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.html5.WebStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

public class FilterTests extends AbstractWebTest {
    private final List<SearchPage> searchPageList = new ArrayList<>();

    @BeforeAll
    public void init() {
        drivers.forEach( d -> {
                    d.get(Constants.URL_SEARCH);
                    searchPageList.add(new SearchPage(d));
                }
        );
    }

    @BeforeEach
    void clearBefore() {
        drivers.forEach( d -> {
                    d.manage().deleteAllCookies();
                    WebStorage webStorage = (WebStorage)d;
                    webStorage.getSessionStorage().clear();
                    webStorage.getLocalStorage().clear();
                    d.navigate().refresh();
                }
        );
    }

    @AfterEach
    public void tearDown() {
        drivers.forEach( d -> {
                    d.get(Constants.URL_SEARCH);
                    d.manage().deleteAllCookies();
                    WebStorage webStorage = (WebStorage)d;
                    webStorage.getSessionStorage().clear();
                    webStorage.getLocalStorage().clear();
                }
        );
    }

    @Test
    public void checkCostFilterList() throws InterruptedException {
        for (var page : searchPageList) {
            page.getWait().until(visibilityOf(page.costFilterButton));
            page.costFilterButton.click();

            page.getWait().until(visibilityOf(page.startCostInput));
            page.startCostInput.clear();
            page.startCostInput.sendKeys(Constants.FILTER_START_COST);
            page.endCostInput.clear();
            page.endCostInput.sendKeys(Constants.FILTER_END_COST);
            page.submitCostFilterButton.click();

            page.getWait().until(visibilityOf(page.filterChoiceList));
            Thread.sleep(1000);

            List<WebElement> elementList = page.getChilds(page.filterChoiceList);
            String firstChoice = elementList.get(0).getText();
            String regex = "(\\d+)\\s*до\\s*(\\d+)";

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(firstChoice);

            if (matcher.find()) {
                String firstNumber = matcher.group(1);
                String secondNumber = matcher.group(2);
                assertThat(firstNumber)
                        .isEqualTo(Constants.FILTER_START_COST);
                assertThat(secondNumber)
                        .isEqualTo(Constants.FILTER_END_COST);
            }
        }
    }


    @Test
    public void checkCostFilterListFail() throws InterruptedException {
        for (var page : searchPageList) {
            page.getWait().until(visibilityOf(page.costFilterButton));
            page.costFilterButton.click();

            page.getWait().until(visibilityOf(page.startCostInput));
            page.startCostInput.clear();
            page.startCostInput.sendKeys(Constants.FILTER_START_COST_FAIL);
            page.endCostInput.clear();
            page.endCostInput.sendKeys(Constants.FILTER_END_COST_FAIL);
            page.submitCostFilterButton.click();

            page.getWait().until(visibilityOf(page.filterChoiceList));
            Thread.sleep(1000);

            List<WebElement> elementList = page.getChilds(page.filterChoiceList);
            String firstChoice = elementList.get(0).getText();
            String regex = "(\\d+)\\s*до\\s*(\\d+)";

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(firstChoice);

            if (matcher.find()) {
                String firstNumber = matcher.group(1);
                String secondNumber = matcher.group(2);
                assertThat(firstNumber)
                        .as("expected 76 in start")
                        .isNotEqualTo(Constants.FILTER_START_COST_FAIL);
                assertThat(secondNumber)
                        .as("expected 276 in start")
                        .isNotEqualTo
                                (Constants.FILTER_END_COST_FAIL);
            }
        }
    }

    @Test
    public void checkCategoryUnselect() throws InterruptedException {
        for (var page : searchPageList) {
            page.getWait().until(visibilityOf(page.filterCategoryButton));
            page.filterCategoryButton.click();
            page.getWait().until(visibilityOf(page.filterFrame));

            page.clickOnFilterCheckbox("Блокнот");
            Thread.sleep(500);
            page.clickOnFilterCheckboxSelected("Блокнот");
            page.getWait().until(invisibilityOf(page.filterCategoryNotebookButtonSelected));

            List<WebElement> elementList = page.getChilds(page.filterChoiceList);
            assertThat(elementList.size())
                    .isEqualTo(1);
        }

    }


    @Test
    public void checkMultipleCategorySelect() throws InterruptedException {
        for (var page : searchPageList) {
            page.getWait().until(visibilityOf(page.filterCategoryButton));
            page.filterCategoryButton.click();
            page.getWait().until(visibilityOf(page.filterFrame));

            page.clickOnFilterCheckbox("Блокнот");
            page.clickOnFilterCheckbox("Брелок");

            page.getWait().until(visibilityOf(page.filterChoiceList));
            List<WebElement> elementList = page.getChilds(page.filterChoiceList);


            assertThat(elementList.size())
                    .isEqualTo(3);
        }

    }


    @Test
    public void goslingCharmSelect() {
        for (var page : searchPageList) {
            page.getWait().until(visibilityOf(page.filterCategoryButton));

            page.filterCategoryButton.click();
            page.getWait().until(visibilityOf(page.filterFrame));

            page.getWait().until(visibilityOf(page.filterChoiceCharm));
            page.clickOnFilterCheckbox(Constants.FILTER_CATEGORY_CHOICE_CHARM);
            page.getWait().until(visibilityOf(page.filterChoiceList));
            List<WebElement> childs = page.getChilds(page.filterChoiceList);
            assertThat(childs.size())
                    .isEqualTo(2);
        }
    }

    @Test
    void resetChoices()  {
        for (var page : searchPageList) {
            page.getWait().until(visibilityOf(page.filterCategoryButton));

            page.filterCategoryButton.click();
            page.getWait().until(visibilityOf(page.filterFrame));

            page.getWait().until(visibilityOf(page.filterChoiceCharm));
            page.clickOnFilterCheckbox(Constants.FILTER_CATEGORY_CHOICE_CHARM);
            page.getWait().until(visibilityOf(page.filterChoiceList));
            page.resetChoicesButton.click();

            page.getWait().until(invisibilityOf(page.filterCategoryNotebookButtonSelected));

            List<WebElement> childs = page.getChilds(page.filterChoiceList);
            assertThat(childs.size())
                    .isEqualTo(1);
        }
    }


    @Test
    void checkTShirtCard() throws InterruptedException {
        for (var page : searchPageList) {
            page.getWait().until(visibilityOf(page.filterCategoryButton));

            page.filterCategoryButton.click();
            page.getWait().until(visibilityOf(page.filterFrame));

            page.filterSearchInput.sendKeys(Constants.FILTER_CATEGORY_CHOICE_TSHIRT);
            WebElement element = page.getFilterCheckboxElement(Constants.FILTER_CATEGORY_CHOICE_TSHIRT);
            page.getWait().until(visibilityOf(element));

            page.clickOnFilterCheckbox(Constants.FILTER_CATEGORY_CHOICE_TSHIRT);

            Thread.sleep(500);
            List<WebElement> productCards = page.getProductCards();
            page.clickOnElement(productCards.get(0));
            page.getWait().until(visibilityOf(page.nameOfItem));
            String text = page.nameOfItem.getText();

            assertThat(text.toLowerCase()).contains(Constants.FILTER_CATEGORY_CHOICE_TSHIRT.toLowerCase());
        }
    }

    @Test
    void checkCategorySearch() {
        for (var page : searchPageList) {
            page.getWait().until(visibilityOf(page.filterCategoryButton));

            page.filterCategoryButton.click();
            page.getWait().until(visibilityOf(page.filterFrame));

            page.filterSearchInput.sendKeys(Constants.FILTER_CATEGORY_CHOICE_TSHIRT);
            WebElement element = page.getFilterCheckboxElement(Constants.FILTER_CATEGORY_CHOICE_TSHIRT);
            page.getWait().until(visibilityOf(element));

            page.clickOnFilterCheckbox(Constants.FILTER_CATEGORY_CHOICE_TSHIRT);

            page.getWait().until(visibilityOf(page.filterChoiceList));

            List<WebElement> elementList = page.getChilds(page.filterChoiceList);

            assertThat(elementList.get(0).getText().toLowerCase()).contains(Constants.FILTER_CATEGORY_CHOICE_TSHIRT.toLowerCase());

        }
    }


    @Test
    void checkBacketItems() throws InterruptedException {
        for (var page : searchPageList) {

            page.getWait().until(visibilityOf(page.filterCategoryButton));

            page.filterCategoryButton.click();
            page.getWait().until(visibilityOf(page.filterFrame));

            page.getWait().until(visibilityOf(page.filterChoiceCharm));
            page.clickOnFilterCheckbox(Constants.FILTER_CATEGORY_CHOICE_CHARM);
            page.getWait().until(visibilityOf(page.filterChoiceList));

            List<WebElement> productCards = page.getDriver().findElements(By.xpath("//a[@class='product-card__add-basket j-add-to-basket btn-main']"));
            page.getWait().until(visibilityOf(productCards.get(0)));

            for (int i = 0; i < Constants.BACKET_ITEMS_SELECTED; i++) {
                WebElement card = productCards.get(i);
                page.clickOnElement(card);
                Thread.sleep(100);
            }

            page.getDriver().get(Constants.URL_BACKET);
            page.getWait().until(visibilityOf(page.backetGoodsNumber));
            String regex = "\\d+";

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(page.backetGoodsNumber.getText());
            if (matcher.find()) {
                String numberStr = matcher.group();
                int number = Integer.parseInt(numberStr);
                assertThat(number).isEqualTo(Constants.BACKET_ITEMS_SELECTED);
            }
        }
    }

    @Test
    void pickTShirtSize() throws InterruptedException {
        for (var page : searchPageList) {
            page.getWait().until(visibilityOf(page.filterCategoryButton));

            page.filterCategoryButton.click();
            page.getWait().until(visibilityOf(page.filterFrame));

            page.filterSearchInput.sendKeys(Constants.FILTER_CATEGORY_CHOICE_TSHIRT);
            WebElement element = page.getFilterCheckboxElement(Constants.FILTER_CATEGORY_CHOICE_TSHIRT);
            page.getWait().until(visibilityOf(element));

            page.clickOnFilterCheckbox(Constants.FILTER_CATEGORY_CHOICE_TSHIRT);
            Thread.sleep(500);

            List<WebElement> productCards = page.getDriver().findElements(By.xpath("//a[@class='product-card__add-basket j-add-to-basket btn-main']"));
            page.getWait().until(visibilityOf(productCards.get(0)));

            page.clickOnElement(productCards.get(0));

            page.getWait().until(visibilityOf(page.choiceTShirtSizeLabel));
            page.clickOnElement(page.choiceTShirtSizeLabel);
            page.getDriver().get(Constants.URL_BACKET);

            WebElement itemProps = page.getDriver().findElement(By.xpath("//div[@class='good-info__properties ']"));
            page.getWait().until(visibilityOf(itemProps));

            String itemPropsText = itemProps.getText();
            assertThat(itemPropsText).contains(Constants.TSHIRT_SIZE);
//            page.clickOnElement(page.deleteBucketButton);
        }
    }

    @Test
    void checkBucketDeclination() throws InterruptedException {
        for (var page : searchPageList) {
            page.getWait().until(visibilityOf(page.filterCategoryButton));

            page.filterCategoryButton.click();
            page.getWait().until(visibilityOf(page.filterFrame));

            page.filterSearchInput.sendKeys(Constants.FILTER_CATEGORY_CHOICE_CHARM);
            WebElement element = page.getFilterCheckboxElement(Constants.FILTER_CATEGORY_CHOICE_CHARM);
            page.getWait().until(visibilityOf(element));

            page.clickOnFilterCheckbox(Constants.FILTER_CATEGORY_CHOICE_CHARM);
            Thread.sleep(500);

            List<WebElement> productCards = page.getDriver().findElements(By.xpath("//a[@class='product-card__add-basket j-add-to-basket btn-main']"));
            page.getWait().until(visibilityOf(productCards.get(0)));

            page.clickOnElement(productCards.get(0));
            Thread.sleep(500);

            page.getDriver().get(Constants.URL_BACKET);
            page.getWait().until(visibilityOf(page.backetGoodsNumber));

            assertThat(page.backetGoodsNumber.getText()).contains("товар");

            page.clickOnElement(page.bucketPlusButton);

            assertThat(page.backetGoodsNumber.getText()).contains("товара");

            page.clickOnElement(page.bucketPlusButton);
            page.clickOnElement(page.bucketPlusButton);
            page.clickOnElement(page.bucketPlusButton);

            assertThat(page.backetGoodsNumber.getText()).contains("товаров");

        }
    }

    @Test
    void checkBucketCostCalc() throws InterruptedException {
        for (var page : searchPageList) {
            page.getWait().until(visibilityOf(page.filterCategoryButton));

            page.filterCategoryButton.click();
            page.getWait().until(visibilityOf(page.filterFrame));

            page.filterSearchInput.sendKeys(Constants.FILTER_CATEGORY_CHOICE_CHARM);
            WebElement element = page.getFilterCheckboxElement(Constants.FILTER_CATEGORY_CHOICE_CHARM);
            page.getWait().until(visibilityOf(element));

            page.clickOnFilterCheckbox(Constants.FILTER_CATEGORY_CHOICE_CHARM);
            Thread.sleep(500);

            List<WebElement> productCards = page.getDriver().findElements(By.xpath("//a[@class='product-card__add-basket j-add-to-basket btn-main']"));
            page.getWait().until(visibilityOf(productCards.get(0)));

            page.clickOnElement(productCards.get(0));
            Thread.sleep(500);

            page.getDriver().get(Constants.URL_BACKET);
            page.getWait().until(visibilityOf(page.backetGoodsNumber));


            Thread.sleep(1000);

            String costOfBucketText = page.costOfBucket.getText();
            System.out.println(costOfBucketText);
            costOfBucketText = costOfBucketText.replace("₽", "").strip();
            int costOfBucket = Integer.parseInt(costOfBucketText);

            page.clickOnElement(page.bucketPlusButton);
            Thread.sleep(2000);

            costOfBucketText = page.costOfBucket.getText();
            costOfBucketText = costOfBucketText.replace("₽", "").strip();

            assertThat(Integer.parseInt(costOfBucketText))
                    .isEqualTo(costOfBucket * 2);
        }
    }

    @Test
    void checkItemsIncrease() throws InterruptedException {
        for (var page : searchPageList) {
            page.getWait().until(visibilityOf(page.filterCategoryButton));

            page.filterCategoryButton.click();
            page.getWait().until(visibilityOf(page.filterFrame));

            page.filterSearchInput.sendKeys(Constants.FILTER_CATEGORY_CHOICE_CHARM);
            WebElement element = page.getFilterCheckboxElement(Constants.FILTER_CATEGORY_CHOICE_CHARM);
            page.getWait().until(visibilityOf(element));

            page.clickOnFilterCheckbox(Constants.FILTER_CATEGORY_CHOICE_CHARM);
            Thread.sleep(500);

            List<WebElement> productCards = page.getDriver().findElements(By.xpath("//a[@class='product-card__add-basket j-add-to-basket btn-main']"));
            page.getWait().until(visibilityOf(productCards.get(0)));

            page.clickOnElement(productCards.get(0));
            Thread.sleep(500);

            page.getDriver().get(Constants.URL_BACKET);

            page.waitForPageToLoad();
            page.getWait().until(visibilityOf(page.backetGoodsNumber));
            String regex = "\\d+";

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(page.backetGoodsNumber.getText());
            if (matcher.find()) {
                String numberStr = matcher.group();
                int number = Integer.parseInt(numberStr);
                assertThat(number).isEqualTo(1);
            }

            page.clickOnElement(page.bucketPlusButton);

            matcher = pattern.matcher(page.backetGoodsNumber.getText());
            if (matcher.find()) {
                String numberStr = matcher.group();
                int number = Integer.parseInt(numberStr);
                assertThat(number).isEqualTo(2);
            }
        }
    }

    @Test
    void checkDeleteAllInBucket() throws InterruptedException {
        for (var page : searchPageList) {
            page.getWait().until(visibilityOf(page.filterCategoryButton));

            page.filterCategoryButton.click();
            page.getWait().until(visibilityOf(page.filterFrame));

            page.filterSearchInput.sendKeys(Constants.FILTER_CATEGORY_CHOICE_CHARM);
            WebElement element = page.getFilterCheckboxElement(Constants.FILTER_CATEGORY_CHOICE_CHARM);
            page.getWait().until(visibilityOf(element));

            page.clickOnFilterCheckbox(Constants.FILTER_CATEGORY_CHOICE_CHARM);
            Thread.sleep(500);

            List<WebElement> productCards = page.getDriver().findElements(By.xpath("//a[@class='product-card__add-basket j-add-to-basket btn-main']"));
            page.getWait().until(visibilityOf(productCards.get(0)));

            page.clickOnElement(productCards.get(0));
            Thread.sleep(500);

            page.getDriver().get(Constants.URL_BACKET);

            page.waitForPageToLoad();

            page.clickOnElement(page.deleteBucketButton);

            WebElement header = page.getDriver().findElement(By.xpath("//h1[@class='section-header basket-empty__title']"));

            page.getWait().until(visibilityOf(header));
            assertThat(header.getText()).contains("пусто");

        }
    }


    @Test
    void checkNotebookClass() throws InterruptedException {
        for (var page : searchPageList) {
            page.getWait().until(visibilityOf(page.filterCategoryButton));

            page.filterCategoryButton.click();
            page.getWait().until(visibilityOf(page.filterFrame));

            page.filterSearchInput.sendKeys(Constants.FILTER_CATEGORY_CHOICE_NOTEBOOK);
            WebElement element = page.getFilterCheckboxElement(Constants.FILTER_CATEGORY_CHOICE_NOTEBOOK);
            page.getWait().until(visibilityOf(element));

            page.clickOnFilterCheckbox(Constants.FILTER_CATEGORY_CHOICE_NOTEBOOK);
            Thread.sleep(500);

            page.clickOnElement(page.allFiltersButton);

            WebElement checkbox = page.getFilterCheckboxElement("в точку");
            page.getWait().until(visibilityOf(checkbox));
            page.clickOnElement(checkbox);
            Thread.sleep(1000);

            List<WebElement> productCards = page.getProductCards();

            page.clickOnElement(productCards.get(0));
            page.waitForPageToLoad();

            List<WebElement> params = page.getDriver().findElements(By.xpath("//td[@class='product-params__cell']"));
            page.getWait().until(visibilityOf(params.get(0)));

            String collect = params.stream().map(WebElement::getText).collect(Collectors.joining());

            assertThat(collect).contains("в точку");

        }
    }
}
