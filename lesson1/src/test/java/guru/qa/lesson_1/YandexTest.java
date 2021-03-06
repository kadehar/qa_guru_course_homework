package guru.qa.lesson_1;

import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class YandexTest {
    @Test
    public void selenideSearchAtYandexTest() {
        // Открыть google
        open("https://yandex.ru");

        // Ввести Selenide в поиск
        $(byId("text")).setValue("Selenide").pressEnter();

        // Проверить, что Selenide появился в результатах поиска
        $("html").shouldHave(text("ru.selenide.org"));
    }
}
