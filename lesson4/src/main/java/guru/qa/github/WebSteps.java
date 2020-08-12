package guru.qa.github;

import io.qameta.allure.Step;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static guru.qa.github.config.Config.config;
import static io.qameta.allure.Allure.parameter;
import static org.openqa.selenium.By.linkText;

public class WebSteps {
    private static final String LOGIN_FORM_URL = config().getLoginFormUrl();

    @Step("Open Login form")
    public void openLoginForm() {
        parameter("Login Form", LOGIN_FORM_URL);
        open(LOGIN_FORM_URL);
    }

    @Step("Login to GitHub account")
    public void loginAs(final String login, final String password) {
        $("#login_field").setValue(login);
        $("#password").setValue(password);
        $(byName("commit")).click();
    }

    @Step("Search for repository")
    public void searchForRepository(final String repository) {
        parameter("Repository", repository);
        $(".header-search-input").setValue(repository);
        $(".header-search-input").submit();
    }

    @Step("Open repository by link")
    public void openRepositoryByLink(final String repository) {
        $(linkText(repository)).click();
    }

    @Step("Open Issues page")
    public void openIssuesPage() {
        $("a[href='/kadehar/qa_guru_course_homework/issues']").click();
    }

    @Step("Click 'New issue' button")
    public void clickNewIssueButton() {
        $(".btn-primary > span").click();
    }

    @Step("Add labels to issue")
    public void addLabelsToIssue(final String... labels) {
        parameter("Labels", labels);
        List<String> labelsList = Arrays.asList(labels);
        $("#labels-select-menu").click();
        labelsList.forEach(
                label -> {
                    $(withText(label)).click();
                }
        );
        $("body").click();
    }

    @Step("Select assignee")
    public void selectAssignee() {
        $("#assignees-select-menu").click();
        $(".js-username").click();
        $("body").click();
    }

    @Step("Set issue title")
    public void setIssueTitle(final String title) {
        parameter("Title", title);
        $("#issue_title").setValue(title);
    }

    @Step("Submit new issue")
    public void submitNewIssue() {
        $(withText("Submit new issue")).click();
    }

    @Step("Get number of created issue")
    public int getCreatedIssueNumber() {
        return Integer.parseInt(($x("//span[contains(text(),'#')]").getText())
                .replace("#", ""));
    }
}
