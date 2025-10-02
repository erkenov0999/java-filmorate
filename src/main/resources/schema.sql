CREATE TABLE users (
    id          INT             GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email       VARCHAR(50)     NOT NULL UNIQUE,
    login       VARCHAR(50)     NOT NULL UNIQUE,
    name        VARCHAR(50),
    birthday    DATE
);


CREATE TABLE films (
    id              INT                 GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name            VARCHAR(100)        NOT NULL,
    description     VARCHAR(200)        NOT NULL,
    release_date     DATE                NOT NULL,
    duration        INT                 NOT NULL,
);


CREATE TABLE genres (
    id      INT                 GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name    VARCHAR(50)         NOT NULL
);


CREATE TABLE mpa (
    id      INT                 GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name    VARCHAR(50)         NOT NULL
);