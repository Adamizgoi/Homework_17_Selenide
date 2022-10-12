package ru.netology.services.carddelivery;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.time.LocalDate;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static java.lang.String.valueOf;
import static java.time.Duration.ofSeconds;
import static java.time.format.DateTimeFormatter.ofPattern;

public class AppCardDeliveryTest {

    @BeforeEach
    void setUp() {
        Configuration.headless = true;
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

    /** Задача №1
     */

    /**
     * Проверяет, что форма отправляется и показывает правильный текст после отправки, если все поля заполнены правильно (согласно требованиям)
     * <p>
     * Требования:
     * Город — один из административных центров субъектов РФ.
     * Дата — не ранее трёх дней с текущей даты.
     * В поле фамилии и имени разрешены только русские буквы, дефисы и пробелы.
     * В поле телефона — только 11 цифр, символ + на первом месте.
     * Флажок согласия должен быть выставлен.
     */

    @Test
    void shouldBookSuccessfullyIfAllFieldsAreOk() {
        $("[data-test-id = 'city'] input").setValue("Майкоп");
        $("[data-test-id = 'date'] input").setValue(generateDate(3));
        $("[data-test-id = 'name'] input").setValue("Федорова Светлана");
        $("[data-test-id = 'phone'] input").setValue("+79060483535");
        $("[data-test-id = 'agreement'] span").click();
        $("[type='button'].button").click();
        $("[data-test-id = 'notification'] .notification__content").shouldHave(text("Встреча успешно забронирована на " + generateDate(3)), ofSeconds(15));
    }

    /** Задача №2 Условие: "Ввод двух букв в поле «Город», после чего выбор нужного города из выпадающего списка"
     *  Условие: "Выбор даты на неделю вперёд, начиная от текущей даты, через инструмент календаря:"
     */

    /**
     * При вводе двух первых букв города в списке рекомендаций на любом месте содержится нужный город
     */

    // коллекция для хранения всех городов из выпадающего списка городов
    ElementsCollection cities;

    // дает месяц из нужной даты
    String monthOfDate(int days) {
        return valueOf(LocalDate.now().plusDays(days).getMonth());
    }

    // дает день из нужной даты
    String dayOfDate(int days) {
        return valueOf(LocalDate.now().plusDays(days).getDayOfMonth());
    }

    @Test
    void shouldBeOpportunityToChooseCityFromListOfSuggestions() {
        $("[data-test-id = 'city'] input").setValue("Ма");
        cities = $$(".popup_visible .menu-item span");
        cities.findBy(text("Майкоп")).click();
        $("[data-test-id='date'] button").click();
        if (!monthOfDate(7).equals(monthOfDate(0))) {
            $(".calendar__arrow[data-step='1']").click();
        }
        $(byText(dayOfDate(7))).click();
        $("[data-test-id = 'name'] input").setValue("Федорова Светлана");
        $("[data-test-id = 'phone'] input").setValue("+79060483535");
        $("[data-test-id = 'agreement'] span").click();
        $("[type='button'].button").click();
        $("[data-test-id = 'notification'] .notification__content").shouldHave(text("Встреча " +
                "успешно забронирована на " + generateDate(7)), ofSeconds(15));
    }
}
