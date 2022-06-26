package com.velasco.recipeapp.WebServices;

public class Constants {

    // server and database
    public static final String URL = "http://10.0.2.2/";
    public static final String ROOT_URL = URL + "Recipes/";

    // login and register , update picture user
    public static final String URL_REGISTER = ROOT_URL + "register.php";
    public static final String URL_LOGIN = ROOT_URL + "login.php";
    public static final String URL_UPLOAD = ROOT_URL + "update_picture.php";

    // view by category recipe
    public static final String URL_SELECT_CATEGORIES = ROOT_URL + "select_categories.php";
    public static final String URL_SELECT_CATEGORY = ROOT_URL + "select_category.php";

    // view recipe instructions and ingredients
    public static final String URL_SELECT_RECIPE_STEPS = ROOT_URL + "select_recipe_steps.php";
    public static final String URL_SELECT_RECIPE_INGREDIENTS = ROOT_URL + "select_recipe_ingredients.php";

    // add, delete, update recipe details
    public static final String URL_ADD_RECIPE = ROOT_URL + "add_recipe.php";
    public static final String URL_DELETE_RECIPE = ROOT_URL + "delete_recipe.php";
    public static final String URL_UPDATE_RECIPE = ROOT_URL + "update_recipe.php";

    // add, delete, update recipe instruction
    public static final String URL_ADD_INSTRUCTION = ROOT_URL + "add_instruction.php";
    public static final String URL_DELETE_INSTRUCTION = ROOT_URL + "delete_instruction.php";
    public static final String URL_UPDATE_INSTRUCTION = ROOT_URL + "update_instruction.php";

    // add, delete, update recipe ingredient
    public static final String URL_ADD_INGREDIENT = ROOT_URL + "add_ingredient.php";
    public static final String URL_DELETE_INGREDIENT = ROOT_URL + "delete_ingredient.php";
    public static final String URL_UPDATE_INGREDIENT = ROOT_URL + "update_ingredient.php";

}
