--Создание таблицы с ВОЗРАСТНЫМ РЕЙТИНГОМ
CREATE TABLE IF NOT EXISTS mpa (
    id      INT                 GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name    VARCHAR(50)         NOT NULL
);

--Создание таблицы с ПОЛЬЗОВАТЕЛЯМИ
CREATE TABLE IF NOT EXISTS users (
    id          INT             GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email       VARCHAR(50)     NOT NULL UNIQUE,
    login       VARCHAR(50)     NOT NULL UNIQUE,
    name        VARCHAR(50),
    birthday    DATE
);

--Создание таблицы с ФИЛЬМАМИ
CREATE TABLE IF NOT EXISTS films (
    id              INT                 GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name            VARCHAR(100)        NOT NULL,
    description     VARCHAR(200)        NOT NULL,
    release_date    DATE                NOT NULL,
    duration        INT                 NOT NULL,
    mpa_id          INT                 REFERENCES mpa(id)
);

--Создание таблицы с ЖАНРАМИ
CREATE TABLE IF NOT EXISTS genres (
    id      INT                 GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name    VARCHAR(50)         NOT NULL
);

--Связующая таблица между фильмами и жанрами (связь: многие-ко-многим)
CREATE TABLE IF NOT EXISTS film_genres (
    film_id     INT     NOT NULL,
    genre_id    INT     NOT NULL,

    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE
);

--Связующая таблица между фильмами и лайками пользователей
CREATE TABLE IF NOT EXISTS film_likes (
    film_id INT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

--Связующая таблица между пользователями и друзьями (связь: многие-ко-многим)
CREATE TABLE IF NOT EXISTS user_friends (
    user_id INT NOT NULL,
    friend_id INT NOT NULL,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users(id) ON DELETE CASCADE
);