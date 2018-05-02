CREATE TABLE IF NOT EXISTS users (
  id       SERIAL       NOT NULL PRIMARY KEY,
  login    VARCHAR(50)  NOT NULL UNIQUE,
  email    VARCHAR(50)  NOT NULL UNIQUE,
  password VARCHAR(200) NOT NULL,
  avatar   VARCHAR,
  scoreS    INTEGER      DEFAULT 0,
  scoreM    INTEGER      DEFAULT 0
);

