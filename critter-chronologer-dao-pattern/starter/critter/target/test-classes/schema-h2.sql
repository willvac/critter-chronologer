CREATE TYPE PETTYPE AS ENUM ('CAT', 'DOG', 'LIZARD', 'BIRD', 'FISH', 'SNAKE', 'OTHER');
CREATE TYPE PETACTIVITY as ENUM ('PETTING', 'WALKING', 'FEEDING', 'MEDICATING', 'SHAVING');
CREATE TYPE DAYOFWEEK as ENUM ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY');

CREATE TABLE IF NOT EXISTS customers (
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    phonenumber VARCHAR,
    notes VARCHAR
);

CREATE TABLE IF NOT EXISTS pets (
    id SERIAL PRIMARY KEY,
    type PETTYPE,
    name VARCHAR,
    ownerid INTEGER REFERENCES customers(id) ON DELETE CASCADE,
    birthdate DATE,
    notes VARCHAR
);

CREATE TABLE IF NOT EXISTS employees (
    id SERIAL PRIMARY KEY,
    name VARCHAR
);

CREATE TABLE IF NOT EXISTS appointments (
    id SERIAL PRIMARY KEY,
    petid INTEGER REFERENCES pets(id) ON DELETE CASCADE,
    employeeid INTEGER REFERENCES employees(id) ON DELETE SET NULL,
    activity PETACTIVITY,
    serviceday DAYOFWEEK,
    servicetime TIME WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS employee_skills (
    id SERIAL PRIMARY KEY,
    employeeid INTEGER REFERENCES employees(id) ON DELETE CASCADE,
    skill PETACTIVITY,
    UNIQUE(employeeid, skill)
);

CREATE TABLE IF NOT EXISTS employee_availabilities (
    id SERIAl PRIMARY KEY,
    employeeid INTEGER REFERENCES employees(id) ON DELETE CASCADE,
    dayavailable DAYOFWEEK,
    UNIQUE(employeeid, dayavailable)
);