package guru.qa.lesson_3;

import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class AlfabankTest extends BaseTest {
    @Test
    public void testArchiveDeposits() {
        open("https://alfabank.ru/");

        $$(byText("Вклады")).find(visible).click();
        $(".sub-navigation__h1").$("a").shouldHave(text("Вклады и инвестиции"));

        $$(byText("Депозиты")).find(visible).click();
        $(".product-cell__cell-header").shouldHave(text("Самый высокий доход"));

        $(byText("Архивные депозиты")).scrollIntoView(false).click();
        $$(".product-cell__cell").shouldHave(size(3));
    }

    @Test
    public void testDepositInsurance() {
        open("https://alfabank.ru/");

        $$(byText("Вклады")).find(visible).click();
        $(".sub-navigation__h1").$("a").shouldHave(text("Вклады и инвестиции"));

        $(".navigation li").sibling(4).$("a").click();
        $("h1").shouldHave(text("Страхование вкладов"));
    }
}
