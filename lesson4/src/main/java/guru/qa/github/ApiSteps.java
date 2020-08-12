package guru.qa.github;

import guru.qa.github.model.Issue;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;

import static guru.qa.github.config.Config.config;
import static io.qameta.allure.Allure.parameter;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

public class ApiSteps {
    private static final String TOKEN = config().getToken();

    @Step("Get issue by number")
    public Issue getIssueByNumber(final int issue) {
        parameter("Issue #", issue);
        // @formatter:off
        return
                given()
                    .baseUri("https://api.github.com")
                    .header("Authorization", String.format("token %s", TOKEN))
                    .filter(new AllureRestAssured())
                    .log().uri()
                .when()
                    .get("/repos/kadehar/qa_guru_course_homework/issues/{issue}", issue)
                .then()
                    .log().body()
                .extract()
                    .as(Issue.class);
        // @formatter:on
    }

    @Step("Check that issue is created")
    public void checkThatIssueIsCreated(final int issue,
                                        final String assignee,
                                        final String title,
                                        final String... labels) {
        parameter("Issue #", issue);
        // @formatter:off
        given()
                .baseUri("https://api.github.com")
                .header("Authorization", String.format("token %s", TOKEN))
                .filter(new AllureRestAssured())
                .log().uri()
        .when()
                .get("/repos/kadehar/qa_guru_course_homework/issues/{issue}", issue)
        .then()
                .log().body()
                .body("labels.name.flatten()", hasItems(labels))
                .body("assignee.login", is(assignee))
                .body("title", is(title));
        // @formatter:on
    }
}
