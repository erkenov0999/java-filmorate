package ru.yandex.practicum.filmorate;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class FilmTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("Правильно созданный фильм должен проходить валидацию")
    void  validMovie_MustBeValidated(){
        //Arrange
        Film film = new Film("Test movie", "Testing movie",
                LocalDate.of(2025, 6, 1), 120);

        //Act
        var violations = validator.validate(film);

        //Assert
        assertTrue(violations.isEmpty(), "Для валидного фильма не должно быть ошибок");
    }

    @Test
    @DisplayName("При пустом названии фильма, валидация не проходит")
    void  ifNameIsEmpty_ValidationFails(){
        //Arrange
        Film film = new Film("", "Testing movie",
                LocalDate.of(2025, 6, 1), 120);

        //Act
        var violations = validator.validate(film);

        //Assert
        assertEquals(1, violations.size(), "Если переданное название фильма пустое, валидация не пройдет");
    }

    @Test
    @DisplayName("Если описание фильма пустое, валидация не проходит")
    void  ifDeskriptionIsEmpty_ValidationFails(){
        //Arrange
        Film film = new Film("Test movie", " ",
                LocalDate.of(2025, 6, 1), 120);

        //Act
        var violations = validator.validate(film);

        //Assert
        assertEquals(1, violations.size(), "Если переданное описание фильма пустое, валидация не пройдет");
    }

    @Test
    @DisplayName("Если в описании более 200 символов, валидация не пройдет")
    void  description_contains_more_than_200_characters(){
        //Arrange
        Film film = new Film("Test movie", "Бывший киллер Джон Уик выходит из отставки, когда бандиты " +
                "убивают его собаку — последний подарок умершей жены. Месть превращается в кровавую войну с мафией. " +
                "Культовый неонуар-экшен с идеально поставленными драками и стильной эстетикой. Киану Ривз в роли " +
                "безжалостного, но харизматичного мстителя.",
                LocalDate.of(2025, 6, 1), 120);

        //Act
        var violations = validator.validate(film);

        //Assert
        assertEquals(1, violations.size(), "Если описание более 200 символов, валидация не пройдет");
    }

    @Test
    @DisplayName("Если описание содержит менее 200 символов, валидация пройдет")
    void description_contains_less_than_200_characters(){
        //Arrange
        Film film = new Film("Test movie", "После гибели единственного друга — собаки — Джон Уик " +
                "объявляет войну мафии. Культовый экшен с безупречными драками.",
                LocalDate.of(2025, 6, 1), 120);

        //Act
        var violations = validator.validate(film);

        //Assert
        assertTrue(violations.isEmpty(), "Валидация проходит, так как в описании менее 200 символов");
    }

    @Test
    @DisplayName("Валидация не пройдет, если дата релиза пустая")
    void release_date_is_blank(){
        //Arrange
        Film film = new Film("Test movie", "Testing movie", null, 120);

        //Act
        var violations = validator.validate(film);

        //Assert
        assertEquals(1, violations.size(), "Если дата релиза пустая, валидация не пройдет");
    }

    @Test
    @DisplayName("Если продолжительность фильма указана меньше нуля, валидация не пройдет")
    void duration_film_is_negative(){
        //Arrange
        Film film = new Film("Test movie", "Testing movie",
                LocalDate.of(2025, 6, 1), -10);

        //Act
        var violations = validator.validate(film);

        //Assert
        assertEquals(1, violations.size(), "Продолжительность фильма меньше нуля, валидация не пройдет");
    }

    @Test
    @DisplayName("Если продолжительность фильма равна 0, валидация не пройдет")
    void duration_film_is_zero(){
        //Arrange
        Film film = new Film("Test movie", "Testing movie",
                LocalDate.of(2025, 6, 1), 0);

        //Act
        var violations = validator.validate(film);

        //Assert
        assertEquals(1, violations.size(), "Продолжительность фильма равна нулю, валидация не пройдет");
    }
}
