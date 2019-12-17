import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


class CardDeliveryOrderTest {
    LocalDate localDate = LocalDate.now();
    static SelenideElement form = $("form");

    @Test
    void checkVerificationFormWithCorrectInput() {
        open("http://localhost:9999");
        form.$("[data-test-id=city] input").setValue("Санкт-Петербург");
        form.$("[placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        form.$("[placeholder='Дата встречи']").setValue(localDate.plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.YYYY")));
        form.$("[name='name']").setValue("Иванов Иван");
        form.$("[name='phone']").setValue("+79012345678");
        form.$("[data-test-id=agreement]").click();
        form.$("[class='button__content']").click();
        $(withText("Встреча успешно забронирована на")).waitUntil(Condition.visible, 25000);
    }
}