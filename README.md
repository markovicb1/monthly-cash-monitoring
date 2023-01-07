# monthly-cash-monitoring // `ENG` Cash Flow Watcher / `SRB` Нотес потрошње
## ***Java Swing app for monthly and yearly monitoring of your personal cash flow***

## Overview

This personal **dynamic GUI** app helps you track all your incomes and expenses on monthly basis. It is **easy to use, light and user friendly**. See your cash flow in simple tabular view. Generate PDF to view your yearly statistics!

## Development

This Java Swing app works dynamically using PostgreSQL Database Management System. The core functionality is behind 3(4) connected tables inside the DB:
- User table
  - Defines different Users, since app tends to be used by many users on one PC
- Activities table
  - Defines different Activities, divided in 2 groups: revenues and expenses (Types of Activities)
- Transactions table
  - Defines different Transactions. Every User can make revenue or expense in some certain amount, on a specific date. This table is the main functionality of the DB
- Language table
  - Saves previous language mode

Users and Activities are unique by Name.

**The Design of the app is behind FlatLaf open-source LookAndFeel.** For download and more info check [here](https://www.formdev.com/flatlaf/)

## How to use `v1.0`[^1]

***This applies to all new versions***

The app is divided in 2 segments: Side bar and central panel.

The Side bar is used as context menu:
- Add new Revenue
- Add new Expense
- Add new Activity
- View Monthly Cash flow history

The top bar, represented as menu, has 2 sections:
- Data submenu
  - Change Actions names
  - Change Transactions money amount
  - Generate yearly statistics in PDF form
- Help/About submenu
  - Change interface language (Serbian - English)
  - How to use the app. Redirects exactly here
  - The shortest possible info about the app and the author

The Central panel shows content of particular menu. Cash flow history shows tabular view of Transactions made in user-specified year and month, as well as the Balance at the end of that month.

## Download Executable JAR

***If you are interested in using this simple app, you can find the most recent version in the Releases section of this repo.***

***Be aware that this app works with local database, which means you have to install PostgreSQL DBMS and follow instructions dedicated to database configuration in order to use the app.***

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
9. Create Table 'Transactions'. Paste the code bellow in `SQL Shell`, and press enter.
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
11. Create Table 'Language'. Paste the code bellow in `SQL Shell`, and press enter.
```sql
CREATE TABLE IF NOT EXISTS public."Language"
(
    id integer NOT NULL,
    type integer,
    CONSTRAINT "Language_pkey" PRIMARY KEY (id)
)

TABLESPACE pg_default;
```
  - Enter additional code and press enter:
 ```sql
 ALTER TABLE IF EXISTS public."Language"
    OWNER to postgres;
 ```
 
### Adding important external Libraries

If you are familiar with the Apache Maven, you don't have to download anything but add dependencies to pom file:

For Postgres DriverManager add:
```
<dependency>
  <groupid>org.postgresql</groupid>
  <artifactid>postgresql</artifactid>
  <version>42.5.1</version>
</dependency>
```
For FlatLaf (third party LookAndFeel) add this dependecy:
```
<dependency>
  <groupId>com.formdev</groupId>
  <artifactId>flatlaf</artifactId>
  <version>2.6</version>
</dependency>
```
For Apache PDFBox
```
<dependency>
  <groupId>org.apache.pdfbox</groupId>
  <artifactId>pdfbox</artifactId>
  <version>2.0.27</version>
</dependency>
```
For JFree JFreeChart
```
<dependency>
  <groupId>org.jfree</groupId>
  <artifactId>jfreechart</artifactId>
  <version>1.5.3</version>
</dependency>
```

***The section bellow is for those who don't use Apache Maven:***

Unfortunately, PostgreSQL doesn't have built-in/default DriverManager for Java lang so it's necessary to download it separately from [here](https://jdbc.postgresql.org/download/)[^3].

In order to get PDF function working, download Executable JARs:
- [Apache PDFBox](https://pdfbox.apache.org/download.html)
- [JFreeChart](https://github.com/jfree/jfreechart/releases/tag/v1.5.2)

Additionally, you can download Third party LookAndFeel to make Java Swing more eye catching. I recommend using [FlatLaf](https://www.formdev.com/flatlaf/)

***If you don't want to use LookAndFeel, remove the LookAndFeel method from the Main class***

Downloaded files are Executable JARs, which should be added in Projects directory. After adding them, they should also be added as libraries in project.

If you are not fammiliar with adding JARs as external libraries in Java projects, I recommend reading these articles:
- IntelliJ IDEA: [GeeksForGeeks](https://www.geeksforgeeks.org/how-to-add-external-jar-file-to-an-intellij-idea-project/)
- Eclipse Java IDE: [WikiHow](https://www.wikihow.com/Add-JARs-to-Project-Build-Paths-in-Eclipse-(Java))

## Photos (SRB edition)
![Notes slika](https://user-images.githubusercontent.com/115867204/201484618-63c2df1d-17a4-4302-84fd-883be99cea49.png)

*Picture 1: Add new revenue*

![Notes slika 2](https://user-images.githubusercontent.com/115867204/203429056-c2ae94f5-301f-4d27-bb5d-6614a751dc63.png)

*Picture 2: View transaction history for particular month*

![Notes slika 3](https://user-images.githubusercontent.com/115867204/203429592-3702176e-997f-44ab-bc07-7df09b30f271.png)

*Picture 3: Change money amount for particular transaction*

## Legal Notes

This app is open-source and should be treated that way. It is licensed under GNU General Public License v3.

All Third-Party software used within this project is also open-source and is and will be respected acording to their licences available on websites previously provided through this README.md file. In order to understand everything, check their licences.

For more informations related to the legal notes, check GPL license in this repository.
# Future changes

Here's the list of upcoming changes:
- Refactor code
- Add logo to PDF
- Insert threads for better performance

[^1]: Version 1.0, 1.1 and 1.2 is in Serbian Cyrilic only.
[^2]: Even though the newest version of Java is 19, it is recommended to use the LTS(Long time support) version, which is version 17
[^3]: PostgreSQL JDBC Driver, commonly called pgJDBC is an open souce JDBC driver/API for Java. More about this project can be found [here](https://jdbc.postgresql.org/documentation/)
