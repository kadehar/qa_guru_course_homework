package guru.qa.github.steps;

import com.codeborne.selenide.logevents.SelenideLogger;
import guru.qa.github.ApiSteps;
import guru.qa.github.WebSteps;
import guru.qa.github.model.Issue;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static guru.qa.github.config.Config.config;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Feature("Создание новой задачи")
@Story("Использование Steps")
@Owner("kadehar")
public class IssueTestWithSteps {
    private static final String REPOSITORY = config().getRepository();
    private static final String USER = config().getLogin();
    private static final String PWD = config().getPassword();
    private static final String BUG_LABEL = "bug";
    private static final String DUPLICATE_LABEL = "duplicate";
    private static final String TITLE = config().getTitle();

    private final WebSteps webSteps = new WebSteps();
    private final ApiSteps apiSteps = new ApiSteps();

    @BeforeEach
    public void initLogger() {
        SelenideLogger.addListener("allure", new AllureSelenide()
                .savePageSource(true)
                .screenshots(true));
    }

    @Test
    @DisplayName("Пользователь должен иметь возможность создать новую задачу")
    public void createNewIssue() {
        webSteps.openLoginForm();
        webSteps.loginAs(USER, PWD);
        webSteps.searchForRepository(REPOSITORY);
        webSteps.openRepositoryByLink(REPOSITORY);
        webSteps.openIssuesPage();
        webSteps.clickNewIssueButton();
        webSteps.addLabelsToIssue(BUG_LABEL, DUPLICATE_LABEL);
        webSteps.selectAssignee();
        webSteps.setIssueTitle(config().getTitle());
        webSteps.submitNewIssue();
        int issueNum = webSteps.getCreatedIssueNumber();
        Issue created = apiSteps.getIssueByNumber(issueNum);

        assertThat(created.getTitle(),is(TITLE));
        assertThat(created.getAssignee().getLogin(), is(config().getAssignee()));
        assertThat(created.getLabels().get(0).getName(), is(BUG_LABEL));
        assertThat(created.getLabels().get(1).getName(), is(DUPLICATE_LABEL));
    }

    @Test
    @DisplayName("Пользователь должен иметь возможность создать новую задачу")
    public void createNewIssueAndCheckWithAPI() {
        webSteps.openLoginForm();
        webSteps.loginAs(USER, PWD);
        webSteps.searchForRepository(REPOSITORY);
        webSteps.openRepositoryByLink(REPOSITORY);
        webSteps.openIssuesPage();
        webSteps.clickNewIssueButton();
        webSteps.addLabelsToIssue(BUG_LABEL, DUPLICATE_LABEL);
        webSteps.selectAssignee();
        webSteps.setIssueTitle(TITLE);
        webSteps.submitNewIssue();
        int issueNum = webSteps.getCreatedIssueNumber();

        apiSteps.checkThatIssueIsCreated(issueNum, config().getAssignee(),
                TITLE, BUG_LABEL, DUPLICATE_LABEL);
    }

    @AfterEach
    public void closeDriver() {
        closeWebDriver();
    }
}
