# Recipe Android Application

A full stack android application for the final project on a uni subject "Technology and Programming of Mobile Devices"

# Table of Contents

1. [Technologies Used](#technologies-used)
2. [Database MySQL](#database-mysql)
   - [Create User](#create-user)
   - [Import Backup](#import-backup)
3. [The Application](#the-application)
   - [Login](#login)
   - [Register](#register)
   - [Main](#main) - [Categories](#categories) - [Add Recipe](#add-recipe) - [Add, Delete, Edit Ingredients](#add-delete-edit-ingredients) - [Delete a Recipe](#delete-a-recipe) - [Edit Recipe](#edit-recipe) - [Profile](#profile) 4.[Author](#author) 5.[Extras](#extras)

## Technologies used

During the development of the application, the following technologies were used:

- RecyclerView
- TabLayout
- Fragments
- Menu Navigation Bar
- Volley
- MySQL
- Spinner
- Singleton
- SharedPreferences
- Gson
- Image Select

Specifically, the application consists of 3 activities:

1. LoginActivity
2. MainActivity
3. RegisterActivity

For the database, Volley technology was used in conjunction with PHP and MySQL. The PHP files are located in the "Recipes" zip file. You should place this folder in the "xampp/htdocs" directory.

The application includes a bottom menu, and all other screens are created using the Fragments technology.

## Database mysql

### Create user

```sql
-- Drop the database if it exists
DROP DATABASE IF EXISTS recipes_db;

-- Create the database
CREATE DATABASE recipes_db;

-- Drop the user if it exists
DROP USER IF EXISTS 'cs161020'@'localhost';

-- Create the user
CREATE USER 'cs161020'@'localhost';

-- Alter the user's password
ALTER USER 'cs161020'@'localhost' IDENTIFIED BY '1998';

-- Grant all privileges on the recipes_db database to the user 'cs161020'@'localhost' with grant option
GRANT ALL PRIVILEGES ON `recipes_db`.* TO 'cs161020'@'localhost' WITH GRANT OPTION;
```

### Import Backup

In phpMyAdmin, with the "recipes_db" database selected, import the file "recipes_db_backup.sql."

## The Application

### Login

<div align="center">
  <img src="https://github.com/PaolaVlsc/RecipeApp/assets/87998374/23cad322-e4e2-4505-b13e-f199e0e5e8a8" width="30%">
</div>

### Register

<div align="center">
  <img src="https://github.com/PaolaVlsc/RecipeApp/assets/87998374/db27c4ac-ab69-4d9e-ae60-00f9a6d8da7b" width="50%">
</div>

### Main

#### Categories

<div align="center">
  <img src="https://github.com/PaolaVlsc/RecipeApp/assets/87998374/d9f691b6-3873-4e44-a423-1d6ebe51aef1" width="50%">
</div>

#### Add Recipe

<div align="center">
  <img src="https://github.com/PaolaVlsc/RecipeApp/assets/87998374/5151e122-5c81-41e2-8cc8-1df6b6cb5838" width="50%">
</div>

#### Add, delete, edit ingredients

<div align="center">
  <img src="https://github.com/PaolaVlsc/RecipeApp/assets/87998374/f1b250db-0a26-44be-a2a8-7a8317a72e78" width="50%">
</div>
<div align="center">
  <img src="https://github.com/PaolaVlsc/RecipeApp/assets/87998374/94e5fe6a-bed9-4a51-bdc2-f604f559df70" width="50%">
</div>

#### Delete a recipe

<div align="center">
  <img src="https://github.com/PaolaVlsc/RecipeApp/assets/87998374/5ae5c214-de08-4ead-83d9-90e2115fc053" width="50%">
</div>

#### Edit recipe

<div align="center">
  <img src="https://github.com/PaolaVlsc/RecipeApp/assets/87998374/32b303fb-401e-446c-8a1f-d9b62935a1ac" width="50%">
</div>

#### Profile

<div align="center">
  <img src="https://github.com/PaolaVlsc/RecipeApp/assets/87998374/b6545ae1-f5b3-4a28-a871-aa02951e1dd0" width="30%">
</div>

## Author

This project was written by Velasco Paola

## Extras

[Report paper in greek](https://github.com/PaolaVlsc/RecipeApp/blob/master/extras/cs161020_PROJECT.pdf)

[Εκφώνηση](<https://github.com/PaolaVlsc/RecipeApp/blob/master/extras/%CE%95%CE%9A%CE%A6%CE%A9%CE%9D%CE%97%CE%A3%CE%97%20%CE%A4%CE%95%CE%9B%CE%99%CE%9A%CE%97%CE%A3%20%CE%91%CE%A3%CE%9A%CE%97%CE%A3%CE%97%CE%A3%20(%CE%A3%CE%95%CE%A0%CE%A4).pdf>)
