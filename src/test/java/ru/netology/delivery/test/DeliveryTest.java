package ru.netology.delivery.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class DeliveryTest {
    @BeforeAll
    static void setUpAll() {SelenideLogger.addListener("allure", new AllureSelenide()); }

    @AfterAll
    static void tearDownAll() {SelenideLogger.removeListener("allure");}
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

        @Test
        @DisplayName("Should successful plan and replan meeting")
        void shouldSuccessfulPlanAndReplanMeeting() {
            DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
            int daysToAddForFirstMeeting = 4;
            String firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
            int daysToAddForSecondMeeting = 7;
            String secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
            $("[data-test-id=city] input").setValue((validUser.getCity()));
            $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
            $("[data-test-id='date'] input").setValue(firstMeetingDate);
            $("[data-test-id='name'] input").setValue(String.valueOf(validUser.getName()));
            $("[data-test-id='phone'] input").setValue(String.valueOf(validUser.getPhone()));
            $("[data-test-id='agreement']").click();
            $(byText("Запланировать")).click();
            $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
            $("[data-test-id='success-notification'] .notification__content")
                    .shouldBe(visible, Duration.ofSeconds(15))
                    .shouldHave(text(" Встреча успешно запланирована на  " + firstMeetingDate));
            $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
            $("[data-test-id='date'] input").setValue(secondMeetingDate);
            $(byText("Запланировать")).click();
            $("[data-test-id='replan-notification']").shouldBe(visible);
            $(withText("Перепланировать")).click();
            $("[data-test-id='replan-notification'] .notification__content")
                    .shouldBe(visible)
                    .shouldBe(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
            $("[data-test-id='replan-notification'] button").click();
            $("[data-test-id='success-notification'] .notification__content")
                    .shouldBe(visible)
                    .shouldHave(text("Встреча успешно запланирована на  " + secondMeetingDate));
        }

}
