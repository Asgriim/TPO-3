package org.omega.tpo3;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.omega.tpo3.pages.SearchPage;
import org.openqa.selenium.html5.WebStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOf;

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
            page.setCostRangeFilter(Constants.FILTER_START_COST, Constants.FILTER_END_COST);

            page.waitDeleteBucketButtonInvisible();

            String firstChoice = page.getFilterChoiceListElement(0);
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

            page.setCostRangeFilter(Constants.FILTER_START_COST_FAIL, Constants.FILTER_END_COST_FAIL);

            page.waitDeleteBucketButtonInvisible();


            String firstChoice = page.getFilterChoiceListElement(0);
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
            page.clickFilterCategoryButton();

            page.clickOnFilterCheckbox("Блокнот");
            Thread.sleep(500);
            page.clickOnFilterCheckboxSelected("Блокнот");

            page.waitForFilterCategoryNotebookButtonSelectedInVisibility();
            assertThat(page.getFilterChoiceListSize())
                    .isEqualTo(1);
        }

    }


    @Test
    public void checkMultipleCategorySelect() throws InterruptedException {
        for (var page : searchPageList) {
            page.clickFilterCategoryButton();
            page.clickOnFilterCheckbox("Блокнот");
            page.clickOnFilterCheckbox("Брелок");

            page.waitFilterChoiceListVisible();


            assertThat(page.getFilterChoiceListSize())
                    .isEqualTo(3);
        }

    }


    @Test
    public void goslingCharmSelect() {
        for (var page : searchPageList) {
            page.clickFilterCategoryButton();

            page.clickOnFilterCheckbox(Constants.FILTER_CATEGORY_CHOICE_CHARM);

            page.waitFilterChoiceListVisible();
            assertThat(page.getFilterChoiceListElement(0)).contains(Constants.FILTER_CATEGORY_CHOICE_CHARM);
        }
    }

    @Test
    void resetChoices()  {
        for (var page : searchPageList) {
            page.clickFilterCategoryButton();

            page.clickOnFilterCheckbox(Constants.FILTER_CATEGORY_CHOICE_CHARM);

            page.waitFilterChoiceListVisible();

            page.clickResetFilterChoicesButton();

            page.waitForFilterCategoryNotebookButtonSelectedInVisibility();


            assertThat(page.getFilterChoiceListSize())
                    .isEqualTo(1);
        }
    }


    @Test
    void checkTShirtCard() throws InterruptedException {
        for (var page : searchPageList) {
            page.clickFilterCategoryButton();

            page.inputToFilterSearch(Constants.FILTER_CATEGORY_CHOICE_TSHIRT);
            page.clickOnFilterCheckBox(Constants.FILTER_CATEGORY_CHOICE_TSHIRT);

            page.waitForFilterCategoryNotebookButtonSelectedInVisibility();

            page.clickOnProductCard(0);
            String text = page.getNameOfCurrentItem();

            assertThat(text.toLowerCase()).contains(Constants.FILTER_CATEGORY_CHOICE_TSHIRT.toLowerCase());
        }
    }

    @Test
    void checkCategorySearch() {
        for (var page : searchPageList) {
            page.clickFilterCategoryButton();

            page.inputToFilterSearch(Constants.FILTER_CATEGORY_CHOICE_TSHIRT);
            page.clickOnFilterCheckBox(Constants.FILTER_CATEGORY_CHOICE_TSHIRT);

            page.waitFilterChoiceListVisible();



            assertThat(page.getFilterChoiceListElement(0).toLowerCase()).contains(Constants.FILTER_CATEGORY_CHOICE_TSHIRT.toLowerCase());

        }
    }


    @Test
    void checkBacketItems() throws InterruptedException {
        for (var page : searchPageList) {

            page.clickFilterCategoryButton();

            page.clickOnFilterCheckbox(Constants.FILTER_CATEGORY_CHOICE_CHARM);
            page.waitFilterChoiceListVisible();

            for (int i = 0; i < Constants.BACKET_ITEMS_SELECTED; i++) {
                page.addItemToBacket(i);
            }

            page.getDriver().get(Constants.URL_BACKET);
            page.waitForBacketGoodsNumberVisible();

            assertThat(page.getBacketGoodsNumbers()).isEqualTo(Constants.BACKET_ITEMS_SELECTED);
        }
    }

    @Test
    void pickTShirtSize() throws InterruptedException {
        for (var page : searchPageList) {
            page.clickFilterCategoryButton();

            page.inputToFilterSearch(Constants.FILTER_CATEGORY_CHOICE_TSHIRT);
            page.clickOnFilterCheckBox(Constants.FILTER_CATEGORY_CHOICE_TSHIRT);
            Thread.sleep(500);

            page.addItemToBacket(0);

            page.pickTShirtSizeLabel(Constants.TSHIRT_SIZE);

            page.getDriver().get(Constants.URL_BACKET);
            String itemPropsText = page.getItemsProps();
            assertThat(itemPropsText).contains(Constants.TSHIRT_SIZE);
        }
    }

    @Test
    void checkBucketDeclination() throws InterruptedException {
        for (var page : searchPageList) {
            page.clickFilterCategoryButton();

            page.inputToFilterSearch(Constants.FILTER_CATEGORY_CHOICE_CHARM);

            page.clickOnFilterCheckbox(Constants.FILTER_CATEGORY_CHOICE_CHARM);
            page.waitDeleteBucketButtonInvisible();
//            Thread.sleep(500);

            page.addItemToBacket(0);

            page.getDriver().get(Constants.URL_BACKET);
            page.waitForBacketGoodsNumberVisible();

            assertThat(page.backetGoodsNumber.getText()).contains("товар");

            page.clickOnBacketPlusButton();

            assertThat(page.backetGoodsNumber.getText()).contains("товара");

            page.clickOnBacketPlusButton();
            page.clickOnBacketPlusButton();
            page.clickOnBacketPlusButton();

            assertThat(page.backetGoodsNumber.getText()).contains("товаров");

        }
    }

    @Test
    void checkBucketCostCalc() throws InterruptedException {
        for (var page : searchPageList) {
            page.clickFilterCategoryButton();

            page.inputToFilterSearch(Constants.FILTER_CATEGORY_CHOICE_CHARM);

            page.clickOnFilterCheckbox(Constants.FILTER_CATEGORY_CHOICE_CHARM);
            page.waitDeleteBucketButtonInvisible();

            page.addItemToBacket(0);
            page.waitDeleteBucketButtonInvisible();
//            Thread.sleep(500);

            page.getDriver().get(Constants.URL_BACKET);
            page.waitForBacketGoodsNumberVisible();
            page.waitForEndCostInputInvisibility();

            String costOfBucketText = page.costOfBucket.getText();
            System.out.println(costOfBucketText);
            costOfBucketText = costOfBucketText.replace("₽", "").strip();
            int costOfBucket = Integer.parseInt(costOfBucketText);

            page.clickOnElement(page.bucketPlusButton);
            page.waitForEndCostInputInvisibility();
//            Thread.sleep(2000);

            costOfBucketText = page.costOfBucket.getText();
            costOfBucketText = costOfBucketText.replace("₽", "").strip();

            assertThat(Integer.parseInt(costOfBucketText))
                    .isEqualTo(costOfBucket * 2);
        }
    }

    @Test
    void checkItemsIncrease() throws InterruptedException {
        for (var page : searchPageList) {
            page.clickFilterCategoryButton();

            page.inputToFilterSearch(Constants.FILTER_CATEGORY_CHOICE_CHARM);

            page.clickOnFilterCheckbox(Constants.FILTER_CATEGORY_CHOICE_CHARM);

            page.waitDeleteBucketButtonInvisible();

            page.addItemToBacket(0);

            page.waitDeleteBucketButtonInvisible();

            page.getDriver().get(Constants.URL_BACKET);

            page.waitForPageToLoad();
            page.waitForBacketGoodsNumberVisible();

            assertThat(page.getBacketGoodsNumbers()).isEqualTo(1);

            page.clickOnBacketPlusButton();

            assertThat(page.getBacketGoodsNumbers()).isEqualTo(2);
        }
    }

    @Test
    void checkDeleteAllInBucket() throws InterruptedException {
        for (var page : searchPageList) {
            page.clickFilterCategoryButton();

            page.inputToFilterSearch(Constants.FILTER_CATEGORY_CHOICE_CHARM);

            page.clickOnFilterCheckbox(Constants.FILTER_CATEGORY_CHOICE_CHARM);
            page.waitDeleteBucketButtonInvisible();

            page.addItemToBacket(0);

            page.getDriver().get(Constants.URL_BACKET);

            page.waitDeleteBucketButtonInvisible();

            page.waitForPageToLoad();

            page.clickOnDeleteBucketButton();

            assertThat(page.getBucketHeader()).contains("пусто");

        }
    }


    @Test
    void checkNotebookClass() throws InterruptedException {
        for (var page : searchPageList) {
            page.clickFilterCategoryButton();
            page.clickOnFilterCheckbox(Constants.FILTER_CATEGORY_CHOICE_NOTEBOOK);
            page.waitDeleteBucketButtonInvisible();

            page.clickOnAllFiltersButton();

            page.waitDeleteBucketButtonInvisible();

            page.clickOnProductCard(0);
            page.waitForPageToLoad();
            page.getWait().until(invisibilityOf(page.deleteBucketButton));

            assertThat(page.getItemsParams()).contains("в точку");

        }
    }
}
