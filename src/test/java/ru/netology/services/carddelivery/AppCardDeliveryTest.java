package ru.netology.services.carddelivery;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.time.LocalDate;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static java.time.Duration.ofSeconds;
import static java.time.format.DateTimeFormatter.ofPattern;

public class AppCardDeliveryTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
        $(By.cssSelector("[data-test-id = 'date'] input")).sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
        $(By.cssSelector("[data-test-id = 'date'] input")).sendKeys(Keys.DELETE);
    }

    @AfterEach
    void teardown() {
        closeWebDriver();
    }

    String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(ofPattern("dd.MM.yyyy"));
    }

    @Test
    void shouldBookSuccessfullyIfAllFieldsAreOk() {
        $("[data-test-id = 'city'] input").setValue("Майкоп");
        $("[data-test-id = 'date'] input").setValue(generateDate(3));
        $("[data-test-id = 'name'] input").setValue("Федорова Светлана");
        $("[data-test-id = 'phone'] input").setValue("+79060483535");
        $("[data-test-id = 'agreement'] span").click();
        $("[type='button'].button").click();
        $("[data-test-id = 'notification'] .notification__content").shouldHave(text("Встреча успешно забронирована на"), ofSeconds(15));
    }
}
