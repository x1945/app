-- SCHEMA
CREATE SCHEMA IF NOT EXISTS TEST;

CREATE TABLE IF NOT EXISTS persistent_logins (
   username varchar(64) not null,
   series varchar(64) primary key,
   token varchar(64) not null,
   last_used timestamp not null
);

CREATE TABLE TEST.APP0USR (
	ID            VARCHAR(10) NOT NULL,
	NAME          VARCHAR(100) NOT NULL,
	PWD           VARCHAR(50) NOT NULL,
	EMAIL         VARCHAR(100),
	UPDATER       VARCHAR(10),
	UPDTIME       TIMESTAMP,
	PRIMARY KEY(ID)
);

CREATE TABLE TEST.APP0PAM (
	SYS                 VARCHAR(1)	NOT NULL,
	TYPE                VARCHAR(30)	NOT NULL,
	PAM1                VARCHAR(60)	NOT NULL,
	PAM2                VARCHAR(600),
	PAM3                VARCHAR(300),
	ISUSE               CHAR(1),
	SORT                DECIMAL(5),
	MAINTAIN            VARCHAR(6),
	UPDATER             VARCHAR(10),
	UPDTIME             TIMESTAMP,
	PRIMARY KEY(SYS, TYPE, PAM1)
);