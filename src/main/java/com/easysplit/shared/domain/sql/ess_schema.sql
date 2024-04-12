-- The docker compose file creates the database.
-- This file creates the schema and all the needed tables

-- DROP SCHEMA IF EXISTS ess_schema;
CREATE SCHEMA IF NOT EXISTS ess_schema
    AUTHORIZATION christiandleon;

CREATE TABLE ESS_SCHEMA.USERS (
    USERGUID VARCHAR(150) UNIQUE NOT NULL,
    NAME VARCHAR(100) NOT NULL,
    LASTNAME VARCHAR(100),
    USERNAME VARCHAR(50) UNIQUE,
    PASSWORD VARCHAR(60) UNIQUE NOT NULL,
    EMAIL VARCHAR(100) UNIQUE NOT NULL,
    PHONE VARCHAR(10) UNIQUE NOT NULL,
    CREATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO ESS_SCHEMA.USERS (USERGUID, NAME, LASTNAME, USERNAME, PASSWORD, EMAIL, PHONE) VALUES (
	'fd48e99b-abd0-4295-96db-41b2d38f76b3',
	'Christian',
	'Ramirez de Leon',
	'christiandleonr',
	'$2a$10$iHmldp2BfQaIkI4UsJ7fw.mEAb6e4KiskbsTpFJv.WJyfiTGqgczO',
	'christiandleonr@gmail.com',
	'6677848479'
);

CREATE TABLE ESS_SCHEMA.ROLES (
    ROLEGUID VARCHAR(150) UNIQUE NOT NULL,
    NAME VARCHAR(100) UNIQUE NOT NULL
);

INSERT INTO ESS_SCHEMA.ROLES (ROLEGUID, NAME) VALUES ('93605fa8-0fd6-4d1a-a0a5-80f4d892b091', 'ES_USER');
INSERT INTO ESS_SCHEMA.ROLES (ROLEGUID, NAME) VALUES ('9dd2559a-2c91-46bb-800c-8ac0727f6d83', 'ADMIN');

CREATE TABLE ESS_SCHEMA.USER_ROLES (
    USERGUID VARCHAR(150) NOT NULL,
    ROLEGUID VARCHAR(150) NOT NULL,
    FOREIGN KEY (USERGUID) REFERENCES ESS_SCHEMA.USERS(USERGUID),
    FOREIGN KEY (ROLEGUID) REFERENCES ESS_SCHEMA.ROLES(ROLEGUID)
);

INSERT INTO ESS_SCHEMA.USER_ROLES (USERGUID, ROLEGUID) VALUES (
	'fd48e99b-abd0-4295-96db-41b2d38f76b3',
	'9dd2559a-2c91-46bb-800c-8ac0727f6d83'
);
INSERT INTO ESS_SCHEMA.USER_ROLES (USERGUID, ROLEGUID) VALUES (
	'fd48e99b-abd0-4295-96db-41b2d38f76b3',
	'93605fa8-0fd6-4d1a-a0a5-80f4d892b091'
);

CREATE TABLE ESS_SCHEMA.FRIENDSHIPS (
    FRIENDSHIPGUID VARCHAR(150) UNIQUE NOT NULL,
    FRIEND VARCHAR(150) NOT NULL,
    STATUS VARCHAR(50) NOT NULL,
    CREATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADDED_BY VARCHAR(150) NOT NULL,
    FOREIGN KEY (FRIEND) REFERENCES ESS_SCHEMA.USERS(USERGUID),
    FOREIGN KEY (ADDED_BY) REFERENCES ESS_SCHEMA.USERS(USERGUID)
);

CREATE TABLE ESS_SCHEMA.GROUPS (
    GROUPGUID VARCHAR(150) UNIQUE NOT NULL,
    NAME VARCHAR(100) NOT NULL,
    DESCRIPTION VARCHAR(200),
    CREATED_BY VARCHAR(150) NOT NULL,
    CREATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_BY VARCHAR(150) NOT NULL,
    UPDATED_DATE VARCHAR(150) NOT NULL
);

CREATE TABLE ESS_SCHEMA.GROUP_MEMBERS (
    GROUPGUID VARCHAR(150) NOT NULL,
    MEMBERGUID VARCHAR(150) NOT NULL,
    FOREIGN KEY (GROUPGUID) REFERENCES ESS_SCHEMA.GROUPS(GROUPGUID),
    FOREIGN KEY (MEMBERGUID) REFERENCES ESS_SCHEMA.USERS(USERGUID)
);

CREATE TABLE ESS_SCHEMA.TRANSACTIONS (
    TRANSACTIONGUID VARCHAR(150) UNIQUE NOT NULL,
    NAME VARCHAR(150) NOT NULL,
    CURRENCY VARCHAR(3) NOT NULL,
    GROUPGUID VARCHAR(150),
    CREDITOR VARCHAR(150) NOT NULL,
    DEBTOR VARCHAR(150) NOT NULL,
    CREATED_BY VARCHAR(150) NOT NULL,
    CREATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_BY VARCHAR(150) NOT NULL,
    UPDATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (GROUPGUID) REFERENCES ESS_SCHEMA.GROUPS(GROUPGUID),
    FOREIGN KEY (CREDITOR) REFERENCES ESS_SCHEMA.USERS(USERGUID),
    FOREIGN KEY (DEBTOR) REFERENCES ESS_SCHEMA.USERS(USERGUID),
    FOREIGN KEY (CREATED_BY) REFERENCES ESS_SCHEMA.USERS(USERGUID),
    FOREIGN KEY (UPDATED_BY) REFERENCES ESS_SCHEMA.USERS(USERGUID)
);

CREATE TABLE ESS_SCHEMA.DEBTS (
    DEBTGUID VARCHAR(150) UNIQUE NOT NULL,
    TRANSACTIONGUID VARCHAR(150) NOT NULL,
    TOTALAMOUNT MONEY NOT NULL,
    DEBT MONEY NOT NULL,
    DEBTSETTLED BOOLEAN NOT NULL,
    REVISION INT NOT NULL,
    CREATED_BY VARCHAR(150) NOT NULL,
    CREATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (CREATED_BY) REFERENCES ESS_SCHEMA.USERS(USERGUID),
    FOREIGN KEY (TRANSACTIONGUID) REFERENCES ESS_SCHEMA.TRANSACTIONS(TRANSACTIONGUID)
);

CREATE TABLE ESS_SCHEMA.REFRESH_TOKENS (
    ID INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    TOKEN VARCHAR(500) NOT NULL,
    USERGUID VARCHAR(150) NOT NULL,
    FOREIGN KEY (USERGUID) REFERENCES ESS_SCHEMA.USERS(USERGUID)
)