# monthly-cash-monitoring // `ENG` Monthly cash flow checker / `SRB` Нотес потрошње
## ***Java Swing app for monthly monitoring of your personal cash flow - srb edition***

## Overview

This personal **dynamic GUI** app helps you track all your incomes and expenses on monthly basis. It is **easy to use, light and user friendly**. See your cash flow in simple tabular view.

## Development

This Java Swing app works dynamically using PostgreSQL Database Management System. The core functionality is behind 3 connected tables inside the DB:
- User table
  - Defines different Users, since app tends to be used by many users on one PC
- Activities table
  - Defines different Activities, divided in 2 groups: revenues and expenses (Types of Activities)
- Transactions table
  - Defines different Transactions. Every User can make revenue or expense in some certain amount, on a specific date. This table is the main functionality of the DB

Users and Activities are unique by Name.

**The Design of the app is behind FlatLaf open-source LookAndFeel.** For download and more info check [here](https://www.formdev.com/flatlaf/)

## How to use `v1.0`[^1]

The app is divided in 2 segments: Side bar and central panel.

The Side bar is used as context menu:
- Add new Revenue
- Add new Expense
- Add new Activity
- View Cash flow history

The Central panel shows content of particular menu. Cash flow history shows tabular view of Transactions made in user-specified year and month, as well as the Balance at the end of that month.

## Download Executable JAR

***As it is in the early phase of development (Local DB, no eng translation etc), the app is not yet available to be downloaded. However, you can make app operational in your IDE following the steps from the section bellow.***

## Prepare the app as a new project in IDE

### Installation of PostgreSQL (and Java?) and creation of the DB

In order to prepare app for personal use or further development, follow this steps:

1. Install PostgreSQL 15 from this [site](https://www.postgresql.org/download/)
  - For password set `postgresbogdan` as it is password in Source code
  - PostgreSQL Server and Command Line Tools are the only neccesery things for the app but you can choose all available options in the installation process
2. Open Terminal/Command prompt and type `java --version`. If it's unrecognized command, or Java version is bellow 17, follow this [link](https://www.oracle.com/java/technologies/downloads/#jdk17-windows) and download Java 17[^2]
3. Find the app called `SQL Shell (psql)` in your apps, and open it. It should look like modified Terminal/Command prompt. Press enter until the shell asks you to enter password.
  - Enter password you put in Installation wizard of the PostgreSQL. It should be `postgresbogdan`.
  - Now you have access to the default Database `postgres`.
4. Create Database `app_db` with this SQL command (paste in shell):
```sql
CREATE DATABASE app_db
WITH OWNER = postgres
ENCODING = 'UTF8'
TABLESPACE = pg_default
CONNECTION LIMIT = -1
IS_TEMPLATE = False;
  ```
5. Reopen `SQL Shell (psql)`. Press enter until the shell asks you to choose DB
  - Type `app_db`
6. Press enter until the shell asks you to enter password. Enter `postgresbogdan` (or the password you specified in Installation Wizard).
  - Now we are inside the app_db and ready to make all the tables
7. Create Table `Activities`. Paste the code bellow in `SQL Shell`, and press enter.
```sql
CREATE TABLE IF NOT EXISTS public."Activities"
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    name text COLLATE pg_catalog."default" NOT NULL,
    type "char" DEFAULT '+'::"char",
    CONSTRAINT "Activities_pkey" PRIMARY KEY (id),
    CONSTRAINT activity_name_unique UNIQUE (name)
)

TABLESPACE pg_default;
```
  - Enter additional code:
```sql
ALTER TABLE IF EXISTS public."Activities"
OWNER to postgres;
```
8. Create Table 'User'. Paste the code bellow in `SQL Shell`, and press enter.
```sql
CREATE TABLE IF NOT EXISTS public."User"
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    name text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "User_pkey" PRIMARY KEY (id),
    CONSTRAINT user_name_unique UNIQUE (name)
)

TABLESPACE pg_default;
```
  - Enter additional code and press enter:
```sql
ALTER TABLE IF EXISTS public."User"
OWNER to postgres;
```
9. Create the final Table 'Transactions'. Paste the code bellow in `SQL Shell`, and press enter.
```sql
CREATE TABLE IF NOT EXISTS public."Transactions"
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    id_activity integer NOT NULL,
    amount numeric NOT NULL,
    date date,
    id_user integer NOT NULL,
    CONSTRAINT transaction_primary_key PRIMARY KEY (id),
    CONSTRAINT activity_foreign_key FOREIGN KEY (id_activity)
        REFERENCES public."Activities" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT user_foreign_key FOREIGN KEY (id_user)
        REFERENCES public."User" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

TABLESPACE pg_default;
```
  - Enter additional code and press enter:
```sql
ALTER TABLE IF EXISTS public."Transactions"
OWNER to postgres;
```
10. Insert demo data in 'User' table:
```sql
INSERT INTO "User" (name)
VALUES ('Admin');
```
11. Finally, the DB is ready to use

### Adding important Libraries

Unfortunately, PostgreSQL doesn't have built-in/default DriverManager for Java lang so it's necessary to download it separately from [here](https://jdbc.postgresql.org/download/)[^3].

Additionally, you can download Third party LookAndFeel to make Java Swing more eye catching. I recommend using [FlatLaf](https://www.formdev.com/flatlaf/)

Downloaded files are Executable JARs, which should be added in Projects directory. After adding them, they should also be added as libraries in project.

If you are not fammiliar with adding JARs as external libraries in Java projects, I recommend reading these articles:
- IntelliJ IDEA: [GeeksForGeeks](https://www.geeksforgeeks.org/how-to-add-external-jar-file-to-an-intellij-idea-project/)
- Eclipse Java IDE: [WikiHow](https://www.wikihow.com/Add-JARs-to-Project-Build-Paths-in-Eclipse-(Java))

## Photo (SRB edition)
![CASHGUI](https://user-images.githubusercontent.com/115867204/199236213-53f256af-265a-4058-b555-4260dfa24967.jpg)

# Future changes

Here's the list of upcoming changes:
- English interface
- Edit Activities directly through the app
- Make history graph
- PDF view of history with additional statistics

[^1]: Version 1.0 is in Serbian Cyrilic only.
[^2]: Even though the newest version of Java is 19, it is recommended to use the LTS(Long time support) version, which is version 17
[^3]: PostgreSQL JDBC Driver, commonly called pgJDBC is an open souce JDBC driver/API for Java. More about this project can be found [here](https://jdbc.postgresql.org/documentation/)
