import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;


import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;


class CardDeliveryOrderTest {
    static LocalDate localDate = LocalDate.now();


    static Date datePicker() {
        $("[placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[placeholder='Дата встречи']")
                .setValue(localDate.plusDays(5).format(DateTimeFormatter.ofPattern("dd.MM.YYYY")));
        return null;
    }

    static String defaultName() {
        $("[name='name']").setValue("Иванов Иван");
        return null;
    }

    static String defaultPhone() {
        $("[name='phone']").setValue("+79012345678");
        return null;
    }

    @Test
    @DisplayName(value = "Check verification form with correct input")
    void checkVerificationFormWithCorrectInput() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        datePicker();
        defaultName();
        defaultPhone();
        $("[data-test-id=agreement]").click();
        $("[class='button__content']").click();
        $("[class='notification__content']").waitUntil(Condition.visible, 25000)
                .shouldHave(text("Встреча успешно забронирована на "));
    }

    @Test
    @DisplayName(value = "Wrong date selection test")
    void wrongDateSelectionTest() {
        open("http://localhost:9999");
        $("[placeholder='Город']").setValue("Санкт-Петербург");
        $("[placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[placeholder='Дата встречи']").setValue(localDate.format(DateTimeFormatter.ofPattern("dd.MM.YYYY")));
        defaultName();
        defaultPhone();
        $("[data-test-id=agreement]").click();
        $("[class='button__content']").click();
        $(".input_invalid .input__sub").shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @Test
    @DisplayName(value = "Wrong city selection test")
    void wrongCitySelectionTest() {
        open("http://localhost:9999");
        $("[placeholder='Город']").setValue("Норильск");
        datePicker();
        defaultName();
        defaultPhone();
        $("[data-test-id=agreement]").click();
        $("[class='button__content']").click();
        $(".input_invalid .input__sub").shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    @DisplayName(value = "Wrong name selection test")
    void wrongNameSelectionTest() {
        open("http://localhost:9999");
        $("[placeholder='Город']").setValue("Псков");
        datePicker();
        $("[name='name']").setValue("Ivanov Ivan");
        defaultPhone();
        $("[data-test-id=agreement]").click();
        $("[class='button__content']").click();
        $(".input_invalid .input__sub").shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только " +
                "русские буквы, пробелы и дефисы."));
    }

    @Test
    @DisplayName(value = "Wrong phone selection test")
    void wrongPhoneSelectionTest() {
        open("http://localhost:9999");
        $("[placeholder='Город']").setValue("Псков");
        datePicker();
        defaultName();
        $("[name='phone']").setValue("+790123456");
        $("[data-test-id=agreement]").click();
        $("[class='button__content']").click();
        $(".input_invalid .input__sub").shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, " +
                "например, +79012345678."));
    }

    @Test
    @DisplayName(value = "Check for empty field")
    void checkForEmptyField() {
        open("http://localhost:9999");
        datePicker();
        defaultName();
        defaultPhone();
        $("[data-test-id=agreement]").click();
        $("[class='button__content']").click();
        $(".input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    @DisplayName(value = "Verification not agreement")
    void verificationNotAgreement() {
        open("http://localhost:9999");
        $("[placeholder='Город']").setValue("Псков");
        datePicker();
        defaultName();
        defaultPhone();
        $("[class='button__content']").click();
        $("[class='checkbox__text']").shouldHave(text("Я соглашаюсь с условиями обработки и " +
                "использования моих персональных данных"));
    }

    @Test
    @DisplayName(value = "Check city selection by two letters")
    void checkCitySelectionByTwoLetters() {
        open("http://localhost:9999");
        $("[placeholder='Город']").setValue("Пс");
        $$("[class=menu-item__control]").find(exactText("Псков")).click();
        $("[placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[placeholder='Дата встречи']")
                .setValue(localDate.plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.YYYY")));
        defaultName();
        defaultPhone();
        $("[data-test-id=agreement]").click();
        $("[class='button__content']").click();
        $("[class='notification__content']").waitUntil(Condition.visible, 25000)
                .shouldHave(text("Встреча успешно забронирована на "));
    }

    @Test
    @DisplayName(value = "On 7 days order")
    void on7DaysOrder() {
        LocalDate dateToBe = localDate.plusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dateToUse = dateToBe.format(formatter);
        open("http://localhost:9999");
        $("[placeholder='Город']").setValue("Псков");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[data-test-id=date] input").click();
        $("[data-test-id=date] input").setValue(dateToUse);
        defaultName();
        defaultPhone();
        $("[data-test-id=agreement]").click();
        $("[class='button__content']").click();
        $("[class='notification__content']").waitUntil(Condition.visible, 25000)
                .shouldHave(text("Встреча успешно забронирована на "));
    }
}