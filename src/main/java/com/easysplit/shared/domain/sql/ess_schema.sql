-- Database: ess

-- DROP SCHEMA IF EXISTS ess_schema ;

CREATE SCHEMA IF NOT EXISTS ess_schema
    AUTHORIZATION christiandleon;

-- DROP DATABASE IF EXISTS ess;

CREATE DATABASE ess
    WITH
    OWNER = christiandleon
    ENCODING = 'UTF8'
    LC_COLLATE = 'C'
    LC_CTYPE = 'C'
    TABLESPACE = pg_default
    CONNECTION LIMIT = s-1
    IS_TEMPLATE = False;

CREATE TABLE "ESS_SCHEMA".USERS (
    USERGUID VARCHAR(150) UNIQUE NOT NULL,
    NAME VARCHAR(100) NOT NULL,
    LASTNAME VARCHAR(100) NOT NULL,
    USERNAME VARCHAR(50) UNIQUE NOT NULL,
    CREATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "ESS_SCHEMA".FRIENDSHIPS (
    FRIENDSHIPGUID VARCHAR(150) UNIQUE NOT NULL,
    FRIEND VARCHAR(150) NOT NULL,
    STATUS VARCHAR(50) NOT NULL,
    CREATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADDED_BY VARCHAR(150) NOT NULL,
    FOREIGN KEY (FRIEND) REFERENCES "ESS_SCHEMA".USERS(USERGUID),
    FOREIGN KEY (ADDED_BY) REFERENCES "ESS_SCHEMA".USERS(USERGUID)
);

CREATE TABLE "ESS_SCHEMA".GROUPS (
    GROUPGUID VARCHAR(150) UNIQUE NOT NULL,
    NAME VARCHAR(100) NOT NULL,
    DESCRIPTION VARCHAR(200),
    CREATED_BY VARCHAR(150) NOT NULL,
    CREATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_BY VARCHAR(150) NOT NULL,
    UPDATED_DATE VARCHAR(150) NOT NULL
);

CREATE TABLE "ESS_SCHEMA".GROUP_MEMBERS (
    GROUPGUID VARCHAR(150) NOT NULL,
    MEMBERGUID VARCHAR(150) NOT NULL,
    FOREIGN KEY (GROUPGUID) REFERENCES "ESS_SCHEMA".GROUPS(GROUPGUID),
    FOREIGN KEY (USERGUID) REFERENCES "ESS_SCHEMA".USERS(USERGUID)
);

CREATE TABLE "ESS_SCHEMA".TRANSACTIONS (
    TRANSACTIONGUID VARCHAR(150) UNIQUE NOT NULL,
    NAME VARCHAR(150) NOT NULL,
    GROUPGUID VARCHAR(150) NOT NULL,
    CREDITOR VARCHAR(150) NOT NULL,
    DEBTOR VARCHAR(150) NOT NULL,
    TOTALAMOUNT MONEY NOT NULL,
    DEBT MONEY NOT NULL,
    DEBTSETTLED VARCHAR(50) NOT NULL,
    CREATED_BY VARCHAR(150) NOT NULL,
    CREATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_BY VARCHAR(150) NOT NULL,
    UPDATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (GROUPGUID) REFERENCES "ESS_SCHEMA".GROUPS(GROUPGUID),
    FOREIGN KEY (CREDITOR) REFERENCES "ESS_SCHEMA".USERS(USERGUID),
    FOREIGN KEY (DEBTOR) REFERENCES "ESS_SCHEMA".USERS(USERGUID),
    FOREIGN KEY (CREATED_BY) REFERENCES "ESS_SCHEMA".USERS(USERGUID),
    FOREIGN KEY (UPDATED_BY) REFERENCES "ESS_SCHEMA".USERS(USERGUID)
);