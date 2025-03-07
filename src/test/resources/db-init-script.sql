CREATE TABLE Roles
(
    id           SERIAL PRIMARY KEY,
    capacity     INT,
    name         VARCHAR(30),
    description  TEXT,
    access_level INT
);

CREATE TABLE Files
(
    id       SERIAL PRIMARY KEY,
    capacity INT,
    name     VARCHAR(30),
    owner    VARCHAR(30),
    tag      VARCHAR(20)
);