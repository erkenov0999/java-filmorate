package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MpaTest {

    @Test
    void constructor_WithValidParameters_ShouldCreateMpa() {
        // Arrange
        int id = 1;
        String name = "G";

        // Act
        Mpa mpa = new Mpa(id, name);

        // Assert
        assertEquals(id, mpa.getId());
        assertEquals(name, mpa.getName());
    }

    @Test
    void constructor_WithDifferentParameters_ShouldCreateMpa() {
        // Arrange
        int id = 2;
        String name = "PG";

        // Act
        Mpa mpa = new Mpa(id, name);

        // Assert
        assertEquals(id, mpa.getId());
        assertEquals(name, mpa.getName());
    }

    @Test
    void setId_WithValidId_ShouldSetId() {
        // Arrange
        Mpa mpa = new Mpa(1, "G");
        int newId = 3;

        // Act
        mpa.setId(newId);

        // Assert
        assertEquals(newId, mpa.getId());
    }

    @Test
    void setName_WithValidName_ShouldSetName() {
        // Arrange
        Mpa mpa = new Mpa(1, "G");
        String newName = "PG-13";

        // Act
        mpa.setName(newName);

        // Assert
        assertEquals(newName, mpa.getName());
    }

    @Test
    void equals_WithSameIdAndName_ShouldReturnTrue() {
        // Arrange
        Mpa mpa1 = new Mpa(1, "G");
        Mpa mpa2 = new Mpa(1, "G");

        // Act & Assert
        assertEquals(mpa1, mpa2);
    }

    @Test
    void equals_WithDifferentId_ShouldReturnFalse() {
        // Arrange
        Mpa mpa1 = new Mpa(1, "G");
        Mpa mpa2 = new Mpa(2, "G");

        // Act & Assert
        assertNotEquals(mpa1, mpa2);
    }

    @Test
    void equals_WithDifferentName_ShouldReturnFalse() {
        // Arrange
        Mpa mpa1 = new Mpa(1, "G");
        Mpa mpa2 = new Mpa(1, "PG");

        // Act & Assert
        assertNotEquals(mpa1, mpa2);
    }
}


