package com.bogdanova;

import com.bogdanova.data.Locale;
import com.codeborne.selenide.CollectionCondition;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$$;

public class TestJunitHomeWork {
    @ValueSource(strings = {"Платье", "Тапочки"}) //test_data
    @ParameterizedTest(name = "Проверка количества карточек при поиске по ключевому слову {0}")
    void wildberriesSearchCommonTest(String testData) {
        open("https://www.wildberries.ru/");
        $("#searchInput").click();
        $("#searchInput").setValue(testData).pressEnter();
        $$("div.product-card__wrapper").filter(visible)
                .shouldHave(CollectionCondition.size(105))
                .first()
                .shouldHave(text(testData));
    }

    @CsvSource({
            "Платье, По запросу «платье» найдено",
            "Юбка, По запросу «юбка» найдено"
    }) //test_data
    @ParameterizedTest(name = "Наименование результата поиска и наличие в нем ключевого слова {0}")
    void wildberriesSearchCommonTestDifferentExpectedText(String searchQuery, String expectedText) {
        open("https://www.wildberries.ru/");
        $("#searchInput").click();
        $("#searchInput").setValue(searchQuery).pressEnter();
        $$("div.searching-results__inner")
                .shouldHave(CollectionCondition.texts(expectedText))
                .shouldHave(CollectionCondition.texts(searchQuery));
    }

    static Stream<Arguments> yandexMusicSiteButtonsText() {
        return Stream.of(
                Arguments.of(Locale.UZ, List.of("Mualliflik huquqi egasi", "Foydalanuvchi kelishuvi", "Yordam")),
                Arguments.of(Locale.RU, List.of("Правообладателям", "Пользовательское соглашение", "Справка")),
                Arguments.of(Locale.UK, List.of("Правовласникам", "Угода користувача", "Довідка"))
        );
    }
    @MethodSource
    @ParameterizedTest(name = "Проверка отображения названия кнопок на Yandex.Music для локали {0}")
    void yandexMusicSiteButtonsText(Locale locale, List<String> buttonsTexts) {
        open("https://music.yandex.ru");
        executeJavaScript("$('.pay-promo-close-btn').click()");
        $(".footer").scrollTo();
        $(".d-lang-switcher").click();
        $$(".d-select__item a").find(text(locale.name())).click();
        $$(".footer__left a").filter(visible).shouldHave(CollectionCondition.texts(buttonsTexts));
    }
}