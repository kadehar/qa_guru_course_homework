package guru.qa.github.listener;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static guru.qa.github.NamedBy.css;
import static guru.qa.github.NamedBy.named;
import static guru.qa.github.config.Config.config;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.openqa.selenium.By.linkText;

@Feature("Создание новой задачи")
@Story("Использование Listeners")
@Owner("kadehar")
public class IssueTestWithListeners {
    private static final String REPOSITORY = config().getRepository();

    @BeforeEach
    public void initListener() {
        SelenideLogger.addListener("allure", new AllureSelenide()
                .savePageSource(true)
                .screenshots(true));
    }

    @Test
    @DisplayName("Пользователь должен иметь возможность создать новую задачу")
    public void createNewIssue() {
        open(config().getLoginFormUrl());

        $(css("#login_field").as("Login field")).setValue(config().getLogin());
        $(css("#password").as("Password field")).setValue(config().getPassword());
        $(named(byName("commit")).as("Login button")).click();

        $(css(".header-search-input").as("Поисковая строка в заголовке")).setValue(REPOSITORY);
        $(css(".header-search-input").as("Поисковая строка в заголовке")).submit();

        $(named(linkText(REPOSITORY)).as("Ссылка на репозиторий")).click();

        $(css("a[href='/kadehar/qa_guru_course_homework/issues']").as("Issues page")).click();

        $(css(".btn-primary > span").as("New issue button")).click();

        $(css("#labels-select-menu").as("Labels select menu")).click();
        $(named(withText("bug")).as("Bug label")).click();
        $(named(withText("duplicate")).as("Duplicate label")).click();
        $(css("body").as("Body")).click();

        $(css("#assignees-select-menu").as("Assignees select menu")).click();
        $(css(".js-username").as("Assignee")).click();
        $(css("body").as("Body")).click();

        $(css("#issue_title").as("Issue title")).setValue("Hello from QA.Guru course!");
        $(named(withText("Submit new issue")).as("Submit new issue")).click();


        int issue = Integer.parseInt(($x("//span[contains(text(),'#')]").getText())
                .replace("#", ""));

        // @formatter:off
        given()
                .baseUri("https://api.github.com")
                .header("Authorization", "token " + config().getToken())
                .filter(new AllureRestAssured())
                .log().uri()
        .when()
                .get("/repos/kadehar/qa_guru_course_homework/issues/{issue}", issue)
        .then()
                .log().body()
                .statusCode(200)
                .body("labels.name.flatten()", hasItems("bug", "duplicate"))
                .body("assignee.login", is(config().getAssignee()))
                .body("title", is(config().getTitle()));
        // @formatter:on
    }

    @AfterEach
    public void closeDriver() {
        closeWebDriver();
    }
}
