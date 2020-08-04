package guru.qa.lesson_2;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class BaseTest {
    @BeforeEach
    public void setUp() {
        Configuration.fastSetValue = true;
        Configuration.startMaximized = true;
        Configuration.timeout = 5000;
    }

    @AfterEach
    public void tearDown() {
        closeWebDriver();
    }
}
