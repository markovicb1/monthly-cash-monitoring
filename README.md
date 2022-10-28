# monthly-cash-monitoring // `ENG`Monthly cash flow checker / `SRB`Notes potrosnje
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

The Design of the app is behind FlatLaf open-source LookAndFeel. For download and more info check [here](https://www.formdev.com/flatlaf/)

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

## Prepare the app as new project in IDE

In order to prepare app for personal use or further development, follow this steps:

1. Install PostgreSQL 15 from this [site](https://www.postgresql.org/download/)



[^1]: Version 1.0 is in Serbian Cyrilic only.
