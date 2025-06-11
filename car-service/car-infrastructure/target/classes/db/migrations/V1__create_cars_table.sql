CREATE TABLE cars (
  id             UUID         PRIMARY KEY,
  created_at     TIMESTAMP    NOT NULL,
  updated_at     TIMESTAMP,
  created_by     VARCHAR(100) NOT NULL,
  make           VARCHAR(100) NOT NULL,
  model          VARCHAR(100) NOT NULL,
  plate_number   VARCHAR(50)  NOT NULL UNIQUE,
  status         VARCHAR(50)  NOT NULL
);
