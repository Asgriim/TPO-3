package org.omega.tpo3;

import org.junit.jupiter.api.*;

import org.omega.tpo3.pages.MainPage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.assertj.core.api.Assertions.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MainPageTest extends AbstractWebTest{
    private final List<MainPage> mainPageList = new ArrayList<>();

    @BeforeAll
    public void init() {
        drivers.forEach( d -> {
                    d.get(Constants.URL_MAIN);
                    mainPageList.add(new MainPage(d));
                }
        );
    }

    @AfterEach
    public void tearDown() {
        drivers.forEach( d -> {
                    d.get(Constants.URL_MAIN);
                }
        );
    }

    @Test
    public void checkBasketURL() {
        for (var page : mainPageList) {
            page.basketElement.click();

            assertThat(page.getDriver().getCurrentUrl())
                    .isEqualTo(Constants.URL_BACKET);
        }

    }

    @Test
    public void checkSearch() throws InterruptedException {
        for (var page : mainPageList) {
            page.getWait().until(visibilityOf(page.inputSearchText));
            page.inputSearchText.click();
            Thread.sleep(500);
            page.inputSearchText.sendKeys("RTX4090");
//            Thread.sleep(1000);
            page.inputSearchText.sendKeys(Keys.RETURN);

            page.getWait().until(visibilityOf(page.linkToItemCard));
            page.linkToItemCard.click();
            page.getWait().until(visibilityOf(page.nameOfItem));

            assertThat(page.nameOfItem.getText().contains("4090"))
                    .isTrue();
        }
    }



    @Test
    public void goslingCostSelect() throws InterruptedException {
        for (var page : mainPageList) {
            page.getWait().until(visibilityOf(page.inputSearchText));

            page.inputSearchText.click();
            Thread.sleep(500);
            page.inputSearchText.sendKeys(Constants.SEARCH_REQ);
            page.inputSearchText.sendKeys(Keys.RETURN);

            page.getWait().until(visibilityOf(page.costFilterButton));
            page.costFilterButton.click();

            page.getWait().until(visibilityOf(page.startCostInput));
            page.startCostInput.clear();
            page.startCostInput.sendKeys(Constants.FILTER_START_COST);
            page.endCostInput.clear();
            page.endCostInput.sendKeys(Constants.FILTER_END_COST);
            page.submitCostFilterButton.click();
            page.getWait().until(visibilityOf(page.filterChoiceList));

            page.getWait().until(invisibilityOf(page.linkToItemCard));
//            Thread.sleep(1000);

            List<WebElement> costsOnPage = page.getCostsOnPage();
            for (var element : costsOnPage) {
                String str = element.getText();
//                System.out.println(str);
                str = str.replace("₽","").strip();
                if (str.isEmpty()) {
                    continue;
                }
                int cost = Integer.parseInt(str);
                assertThat(cost)
                        .isBetween(
                                Integer.parseInt(Constants.FILTER_START_COST),
                                Integer.parseInt(Constants.FILTER_END_COST)
                        );
            }
        }
    }


}
