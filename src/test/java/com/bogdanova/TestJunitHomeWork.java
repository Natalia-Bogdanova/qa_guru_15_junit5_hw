package com.bogdanova;

import com.codeborne.selenide.CollectionCondition;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$$;

public class TestJunitHomeWork {
    @ValueSource(strings = {"Платье", "Юбка"}) //test_data
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

    static Stream<Arguments> wildberriesSiteButtonsTextDataProvider() {
        return Stream.of(
                Arguments.of(List.of("Москва", "Бесплатная доставка", "Продавайте на Wildberries", "Работа в Wildberries"), Locale.Ru),
                Arguments.of(List.of("Ташкент", "Бесплатная доставка", "Продавайте на Wildberries"), Locale.Uz),
                Arguments.of(List.of("Минск", "Бесплатная доставка", "Продавайте на Wildberries"), Locale.By)
        );
    }
    @MethodSource("wildberriesSiteButtonsTextDataProvider")
    @ParameterizedTest(name = "Проверка отображения названия кнопок для локали: {1}")
    void selenideSiteButtonsText(List<String> buttonsTexts, Locale locale) {
        open("https://www.wildberries.ru/");
        $$("li.simple-menu__item j-b-header-country").find(text(locale.name())).click();
        $$(".header__top").filter(visible)
                .shouldHave(CollectionCondition.texts(buttonsTexts));
    }

//    @EnumSource(Locale.class)
//    @ParameterizedTest
//    void checkLocaleTest(Locale locale) {
//        open("https://selenide.org/");
//        $$("#languages a").find(text(locale.name())).shouldBe(visible);
//    }
}