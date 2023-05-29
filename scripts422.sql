CREATE TABLE car (
    id SERIAL PRIMARY KEY,
    brand TEXT,
    model TEXT,
    price NUMERIC
);

CREATE TABLE human (
    id SERIAL PRIMARY KEY,
    name TEXT,
    age INTEGER,
    driver_license BOOLEAN,
    car_id INTEGER REFERENCES car (id)
);