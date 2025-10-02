CREATE TABLE IF NOT EXISTS users (
    id          INT             GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email       VARCHAR(50)     NOT NULL UNIQUE,
    login       VARCHAR(50)     NOT NULL UNIQUE,
    name        VARCHAR(50),
    birthday    DATE
);


CREATE TABLE IF NOT EXISTS films (
    id              INT                 GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name            VARCHAR(100)        NOT NULL,
    description     VARCHAR(200)        NOT NULL,
    release_date    DATE                NOT NULL,
    duration        INT                 NOT NULL,
    mpa_id          INT                 REFERENCES mpa(id)
);


CREATE TABLE IF NOT EXISTS genres (
    id      INT                 GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name    VARCHAR(50)         NOT NULL
);


CREATE TABLE IF NOT EXISTS mpa (
    id      INT                 GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name    VARCHAR(50)         NOT NULL
);