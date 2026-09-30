# SmartPant – Smart Pantry Manager

A Java Android application that helps reduce food waste by tracking ingredients in your pantry and suggesting recipes you can make using **strictly** the ingredients you already have.

## Features
- Full CRUD for pantry items (Add, Edit, Delete, List)
- 18 pre-loaded recipes
- Strict ingredient matching (only shows recipes you can fully make)
- Recipe detail view with ingredients and method
- Settings screen (expiry alerts toggle + unit preference)
- Data persists using SQLite

## Database Choice
**SQLite** (via `SQLiteOpenHelper`) was chosen because:
- It is fully offline and requires no external services
- Aligns with the persistent data storage covered in the module
- Simple to implement and explain
- Sufficient for local pantry and recipe data

## How to Run
1. Open the project in Android Studio
2. Sync Gradle
3. Connect a device or start an emulator
4. Click Run

## Author
[Koelthum Arend]  
Student Number: [402412638]  
Module: Mobile App Development 700