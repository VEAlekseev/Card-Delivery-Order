import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


class CardDeliveryOrderTest {
    LocalDate localDate = LocalDate.now();
    SelenideElement form = $("form");

    @Test
    @DisplayName(value = "Check verification form with correct input")
    void checkVerificationFormWithCorrectInput() {
        open("http://localhost:9999");
        form.$("[data-test-id=city] input").setValue("Санкт-Петербург");
        form.$("[placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[placeholder='Дата встречи']")
                .setValue(localDate.plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.YYYY")));
        form.$("[name='name']").setValue("Иванов Иван");
        form.$("[name='phone']").setValue("+79012345678");
        form.$("[data-test-id=agreement]").click();
        form.$("[class='button__content']").click();
        $(withText("Встреча успешно забронирована на")).waitUntil(Condition.visible, 25000);
    }

    @Test
    @DisplayName(value = "Wrong date selection test")
    void WrongDateSelectionTest() {
        open("http://localhost:9999");
        form.$("[placeholder='Город']").setValue("Санкт-Петербург");
        form.$("[placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[placeholder='Дата встречи']").setValue(localDate.format(DateTimeFormatter.ofPattern("dd.MM.YYYY")));
        form.$("[name='name']").setValue("Иванов Иван");
        form.$("[name='phone']").setValue("+79012345678");
        form.$("[data-test-id=agreement]").click();
        form.$("[class='button__content']").click();
        form.$(withText("Заказ на выбранную дату невозможен")).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName(value = "Wrong city selection test")
    void WrongCitySelectionTest() {
        open("http://localhost:9999");
        form.$("[placeholder='Город']").setValue("Норильск");
        form.$("[placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[placeholder='Дата встречи']")
                .setValue(localDate.plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.YYYY")));
        form.$("[name='name']").setValue("Иванов Иван");
        form.$("[name='phone']").setValue("+79012345678");
        form.$("[data-test-id=agreement]").click();
        form.$("[class='button__content']").click();
        form.$(withText("Доставка в выбранный город недоступна")).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName(value = "Wrong name selection test")
    void WrongNameSelectionTest() {
        open("http://localhost:9999");
        form.$("[placeholder='Город']").setValue("Псков");
        form.$("[placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[placeholder='Дата встречи']")
                .setValue(localDate.plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.YYYY")));
        form.$("[name='name']").setValue("Ivanov Ivan");
        form.$("[name='phone']").setValue("+79012345678");
        form.$("[data-test-id=agreement]").click();
        form.$("[class='button__content']").click();
        form.$(withText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName(value = "Wrong phone selection test")
    void WrongPhoneSelectionTest() {
        open("http://localhost:9999");
        form.$("[placeholder='Город']").setValue("Псков");
        form.$("[placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[placeholder='Дата встречи']")
                .setValue(localDate.plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.YYYY")));
        form.$("[name='name']").setValue("Иванов Иван");
        form.$("[name='phone']").setValue("+790123456");
        form.$("[data-test-id=agreement]").click();
        form.$("[class='button__content']").click();
        form.$(withText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName(value = "Check for empty field")
    void checkForEmptyField() {
        open("http://localhost:9999");
        form.$("[placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[placeholder='Дата встречи']")
                .setValue(localDate.plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.YYYY")));
        form.$("[name='name']").setValue("Иванов Иван");
        form.$("[name='phone']").setValue("+790123456");
        form.$("[data-test-id=agreement]").click();
        form.$("[class='button__content']").click();
        form.$(withText("Поле обязательно для заполнения")).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName(value = "Verification not agreement")
    void verificationNotAgreement() {
        open("http://localhost:9999");
        form.$("[placeholder='Город']").setValue("Псков");
        form.$("[placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[placeholder='Дата встречи']")
                .setValue(localDate.plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.YYYY")));
        form.$("[name='name']").setValue("Иванов Иван");
        form.$("[name='phone']").setValue("+790123456");
        form.$("[class='button__content']").click();
        form.$(withText("Я соглашаюсь с условиями обработки и использования моих персональных данных"))
                .shouldBe(Condition.visible);
    }
}