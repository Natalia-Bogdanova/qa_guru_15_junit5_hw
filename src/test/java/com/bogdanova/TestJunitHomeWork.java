package com.bogdanova;

import com.bogdanova.data.Locale;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.files.DownloadActions.click;

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

    static Stream<Arguments> yandexSiteButtonsTextDataProvider() {
        return Stream.of(
                Arguments.of(List.of("Главное", "Подкасты и книги", "Детям", "Потоки"), Locale.RU),
                Arguments.of(List.of("Home", "Podcasts and books", "For kids", "Stations"), Locale.EN),
                Arguments.of(List.of("Asosiy", "Podcastlar va kitoblar", "Bolalar uchun", "Oqimlar"), Locale.UZ)
        );
    }

    @MethodSource("yandexSiteButtonsTextDataProvider")
    @ParameterizedTest(name = "Проверка отображения названия кнопок для локали: {1}")
    void selenideSiteButtonsText(List<String> buttonsTexts, Locale locale) {
        open("https://music.yandex.ru/");
        $(".d-select__button deco-button-simple deco-button").click();
        $("li.deco-popup-menu__item d-select__item").find(text(Locale.name())).click();
        $$(".nav-kids a").filter(visible)
                .shouldHave(CollectionCondition.texts(buttonsTexts));
    }
}