CREATE TABLE bookings (
  id          UUID        PRIMARY KEY,
  car_id      UUID        NOT NULL,
  user_id     UUID        NOT NULL,
  status      VARCHAR(50) NOT NULL,
  rent_from   TIMESTAMP   NOT NULL,
  rent_to     TIMESTAMP   NOT NULL,
  amount      BIGINT      NOT NULL,       
  payment_id  UUID,
  created_at  TIMESTAMP   NOT NULL,
  updated_at  TIMESTAMP,
  created_by  VARCHAR(100) NOT NULL
);
