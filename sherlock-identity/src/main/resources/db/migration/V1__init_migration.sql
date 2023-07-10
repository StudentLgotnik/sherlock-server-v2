CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE SCHEMA IF NOT EXISTS sherlock_schema;

CREATE TABLE IF NOT EXISTS sherlock_schema.users (
    uuid UUID NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(255),
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    oidc_id VARCHAR(255)
    );
