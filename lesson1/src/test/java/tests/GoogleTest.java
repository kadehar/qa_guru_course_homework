package tests;

import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class GoogleTest {
    @Test
    public void selenideSearchAtGoogleTest() {
        // Открыть google
        open("https://google.com");

        // Ввести Selenide в поиск
        $(byName("q")).setValue("Selenide").pressEnter();

        // Проверить, что Selenide появился в результатах поиска
        $("html").shouldHave(text("ru.selenide.org"));
    }

    @Test
    public void seleniumSearchAtGoogleTest() {
        open("https://google.com");

        // Ввести Selenium в поиск
        $(byName("q")).setValue("Selenium").pressEnter();

        // Проверить, что Selenium появился в результатах поиска
        $("html").shouldHave(text("www.selenium.dev"));
    }

    @Test
    public void appiumSearchAtGoogleTest() {
        open("https://google.com");

        // Ввести Appium в поиск
        $(byName("q")).setValue("Appium").pressEnter();

        // Проверить, что Appium появился в результатах поиска
        $("html").shouldHave(text("appium.io"));
    }
}
