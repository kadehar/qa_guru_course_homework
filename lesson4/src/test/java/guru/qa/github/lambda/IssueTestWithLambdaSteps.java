package guru.qa.github.lambda;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static guru.qa.github.config.Config.config;
import static io.qameta.allure.Allure.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.openqa.selenium.By.linkText;

@Feature("Создание новой задачи")
@Story("Использование Lambda steps")
@Owner("kadehar")
public class IssueTestWithLambdaSteps {
    private static final String REPOSITORY = config().getRepository();
    private static final String LOGIN_FORM_URI = config().getLoginFormUrl();
    private static final String TOKEN = config().getToken();
    private static final String USER = config().getLogin();
    private static final String PWD = config().getPassword();
    private static final String BUG_LABEL = "bug";
    private static final String DUPLICATE_LABEL = "duplicate";
    private static final String TITLE = config().getTitle();
    private int issue;

    @Test
    @DisplayName("Пользователь должен иметь возможность создать новую задачу")
    public void createNewIssue() {
        link("GitHub", String.format("https://github.com/%s", REPOSITORY));
        parameter("Login Form url", LOGIN_FORM_URI);
        parameter("Repository", REPOSITORY);

        step("Open Login Form page", () -> {
            open(LOGIN_FORM_URI);
        });

        step("Login to GitHub", () -> {
            $("#login_field").setValue(USER);
            $("#password").setValue(PWD);
            $(byName("commit")).click();
        });

        step("Search for repository " + REPOSITORY, () -> {
            $(".header-search-input").setValue(REPOSITORY);
            $(".header-search-input").submit();
        });

        step("Go to repository", () -> {
            $(linkText(REPOSITORY)).click();
        });

        step("Go to issues page", () -> {
            $("a[href='/kadehar/qa_guru_course_homework/issues']").click();
        });

        step("Create new issue", () -> {
            step("Click 'New issue' button", () -> {
                $(".btn-primary > span").click();
            });

            step(String.format("Add labels %s and %s", BUG_LABEL, DUPLICATE_LABEL), () -> {
                $("#labels-select-menu").click();
                $(withText(BUG_LABEL)).click();
                $(withText(DUPLICATE_LABEL)).click();
                $("body").click();
            });

            step("Select assignee", () -> {
                $("#assignees-select-menu").click();
                $(".js-username").click();
                $("body").click();
            });

            step("Set title", () -> {
                $("#issue_title").setValue(TITLE);
            });

            step("Click 'Submit new issue' button", () -> {
                $(withText("Submit new issue")).click();
            });
        });

        step("Get issue number", () -> {
            issue = Integer.parseInt(($x("//span[contains(text(),'#')]").getText())
                    .replace("#", ""));
        });

        step(String.format("Check that issue #%d is created", issue), () -> {
            // @formatter:off
            given()
                    .baseUri("https://api.github.com")
                    .header("Authorization", "token " + TOKEN)
                    .filter(new AllureRestAssured())
                    .log().uri()
            .when()
                    .get("/repos/kadehar/qa_guru_course_homework/issues/{issue}", issue)
            .then()
                    .log().body()
                    .statusCode(200)
                    .body("labels.name.flatten()", hasItems(BUG_LABEL, DUPLICATE_LABEL))
                    .body("assignee.login", is(config().getAssignee()))
                    .body("title", is(TITLE));
            // @formatter:on
        });
    }

    @AfterEach
    public void closeDriver() {
        closeWebDriver();
    }
}
