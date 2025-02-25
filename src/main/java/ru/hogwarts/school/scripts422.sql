CREATE TABLE cars (
    id BIGINT PRIMARY KEY,
    brand VARCHAR(30) NOT NULL,
    model VARCHAR(30),
    price NUMERIC(9, 2)
);

CREATE TABLE humans (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    age INTEGER CHECK (age > 0),
    drivers_license BOOLEAN,
    car_id BIGINT REFERENCES cars(id)
);




