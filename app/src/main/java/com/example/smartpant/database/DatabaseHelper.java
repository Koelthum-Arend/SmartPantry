package com.example.smartpant.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.smartpant.model.PantryItem;
import com.example.smartpant.model.Recipe;
import com.example.smartpant.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smartpant.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_PANTRY = "pantry_items";
    public static final String TABLE_RECIPES = "recipes";
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    // Common columns
    public static final String COL_ID = "id";

    // Pantry columns
    public static final String COL_NAME = "name";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_UNIT = "unit";
    public static final String COL_EXPIRY = "expiry_date";

    // Recipe columns
    public static final String COL_STEPS = "steps";

    // Recipe ingredient columns
    public static final String COL_RECIPE_ID = "recipe_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Pantry table
        String createPantry = "CREATE TABLE " + TABLE_PANTRY + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_QUANTITY + " REAL NOT NULL, " +
                COL_UNIT + " TEXT, " +
                COL_EXPIRY + " TEXT)";
        db.execSQL(createPantry);

        // Recipes table
        String createRecipes = "CREATE TABLE " + TABLE_RECIPES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_STEPS + " TEXT NOT NULL)";
        db.execSQL(createRecipes);

        // Recipe ingredients table
        String createRecipeIngredients = "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RECIPE_ID + " INTEGER NOT NULL, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_QUANTITY + " REAL NOT NULL, " +
                COL_UNIT + " TEXT, " +
                "FOREIGN KEY(" + COL_RECIPE_ID + ") REFERENCES " + TABLE_RECIPES + "(" + COL_ID + "))";
        db.execSQL(createRecipeIngredients);

        // Seed recipes
        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        onCreate(db);
    }

    // ========== SEED DATA (18 recipes) ==========
    private void seedRecipes(SQLiteDatabase db) {
        // Helper to insert a recipe + its ingredients
        insertRecipe(db, "Scrambled Eggs",
                "1. Crack eggs into a bowl and whisk with a splash of milk.\n2. Melt butter in a pan over medium heat.\n3. Pour in eggs and stir gently until just set.\n4. Season with salt and pepper. Serve immediately.",
                new String[]{"egg", "2", "pcs", "milk", "30", "ml", "butter", "10", "g"});

        insertRecipe(db, "Cheese Toast",
                "1. Butter one side of the bread slices.\n2. Place cheese between the unbuttered sides.\n3. Toast in a pan or toaster until golden and cheese melts.",
                new String[]{"bread", "2", "slices", "cheese", "50", "g", "butter", "10", "g"});

        insertRecipe(db, "Tomato Pasta",
                "1. Boil pasta according to packet instructions.\n2. In a pan, heat oil and cook chopped tomato and garlic until soft.\n3. Drain pasta and toss with the tomato sauce. Season well.",
                new String[]{"pasta", "100", "g", "tomato", "2", "pcs", "garlic", "2", "cloves", "olive oil", "15", "ml"});

        insertRecipe(db, "Fried Rice",
                "1. Heat oil in a wok or large pan.\n2. Add beaten egg and scramble, then set aside.\n3. Stir-fry cooked rice with soy sauce, then mix the egg back in.",
                new String[]{"rice", "200", "g", "egg", "2", "pcs", "soy sauce", "15", "ml", "oil", "15", "ml"});

        insertRecipe(db, "Omelette",
                "1. Whisk eggs with a pinch of salt.\n2. Melt butter in a non-stick pan.\n3. Pour eggs in, tilt pan to spread, and cook until set. Fold and serve.",
                new String[]{"egg", "3", "pcs", "butter", "10", "g", "salt", "1", "pinch"});

        insertRecipe(db, "Garlic Bread",
                "1. Mix softened butter with crushed garlic.\n2. Spread on bread slices.\n3. Toast under grill or in pan until golden.",
                new String[]{"bread", "4", "slices", "butter", "30", "g", "garlic", "3", "cloves"});

        insertRecipe(db, "Simple Salad",
                "1. Chop tomato and cucumber.\n2. Slice onion thinly.\n3. Toss everything with olive oil, salt and pepper.",
                new String[]{"tomato", "2", "pcs", "cucumber", "1", "pcs", "onion", "0.5", "pcs", "olive oil", "15", "ml"});

        insertRecipe(db, "Boiled Eggs",
                "1. Place eggs in a pot and cover with cold water.\n2. Bring to a boil, then simmer for 7-9 minutes.\n3. Cool under cold water and peel.",
                new String[]{"egg", "4", "pcs"});

        insertRecipe(db, "Cheese Omelette",
                "1. Whisk eggs.\n2. Cook in buttered pan, add grated cheese when almost set.\n3. Fold and serve hot.",
                new String[]{"egg", "3", "pcs", "cheese", "40", "g", "butter", "10", "g"});

        insertRecipe(db, "Pasta Aglio e Olio",
                "1. Cook pasta.\n2. Gently heat olive oil with sliced garlic until fragrant (do not brown).\n3. Toss drained pasta with the garlic oil. Season.",
                new String[]{"pasta", "100", "g", "garlic", "4", "cloves", "olive oil", "30", "ml"});

        insertRecipe(db, "Tomato Sandwich",
                "1. Slice tomato.\n2. Butter the bread.\n3. Layer tomato between slices and season with salt and pepper.",
                new String[]{"bread", "2", "slices", "tomato", "1", "pcs", "butter", "10", "g"});

        insertRecipe(db, "Egg Fried Rice",
                "1. Scramble egg in a hot pan with oil.\n2. Add cooked rice and break up any clumps.\n3. Season with soy sauce and mix well.",
                new String[]{"rice", "200", "g", "egg", "2", "pcs", "soy sauce", "10", "ml", "oil", "15", "ml"});

        insertRecipe(db, "Butter Pasta",
                "1. Cook pasta until al dente.\n2. Drain, reserving a little pasta water.\n3. Toss with butter and a splash of pasta water until glossy.",
                new String[]{"pasta", "100", "g", "butter", "30", "g"});

        insertRecipe(db, "Milk Toast",
                "1. Dip bread lightly in milk.\n2. Fry in butter until golden on both sides.\n3. Optional: sprinkle a little sugar.",
                new String[]{"bread", "2", "slices", "milk", "50", "ml", "butter", "15", "g"});

        insertRecipe(db, "Onion Omelette",
                "1. Finely slice onion and soften in butter.\n2. Pour over whisked eggs.\n3. Cook until set and fold.",
                new String[]{"egg", "3", "pcs", "onion", "1", "pcs", "butter", "10", "g"});

        insertRecipe(db, "Garlic Rice",
                "1. Heat oil and cook minced garlic until fragrant.\n2. Add cooked rice and stir-fry until heated through.\n3. Season with salt.",
                new String[]{"rice", "200", "g", "garlic", "3", "cloves", "oil", "15", "ml"});

        insertRecipe(db, "Cheese and Tomato Toast",
                "1. Layer tomato slices and cheese on bread.\n2. Grill or pan-fry until cheese melts and bread is toasted.",
                new String[]{"bread", "2", "slices", "cheese", "40", "g", "tomato", "1", "pcs"});

        insertRecipe(db, "Simple Scramble with Onion",
                "1. Soften chopped onion in butter.\n2. Add whisked eggs and scramble gently.\n3. Season and serve.",
                new String[]{"egg", "3", "pcs", "onion", "0.5", "pcs", "butter", "10", "g"});
    }

    private void insertRecipe(SQLiteDatabase db, String name, String steps, String[] ingredientData) {
        ContentValues recipeValues = new ContentValues();
        recipeValues.put(COL_NAME, name);
        recipeValues.put(COL_STEPS, steps);
        long recipeId = db.insert(TABLE_RECIPES, null, recipeValues);

        // ingredientData format: name, qty, unit, name, qty, unit...
        for (int i = 0; i < ingredientData.length; i += 3) {
            ContentValues ingValues = new ContentValues();
            ingValues.put(COL_RECIPE_ID, recipeId);
            ingValues.put(COL_NAME, ingredientData[i]);
            ingValues.put(COL_QUANTITY, Double.parseDouble(ingredientData[i + 1]));
            ingValues.put(COL_UNIT, ingredientData[i + 2]);
            db.insert(TABLE_RECIPE_INGREDIENTS, null, ingValues);
        }
    }

    // ========== PANTRY CRUD ==========

    public long addPantryItem(PantryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, item.getName());
        values.put(COL_QUANTITY, item.getQuantity());
        values.put(COL_UNIT, item.getUnit());
        values.put(COL_EXPIRY, item.getExpiryDate());
        long id = db.insert(TABLE_PANTRY, null, values);
        db.close();
        return id;
    }

    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PANTRY, null, null, null, null, null, COL_NAME + " ASC");

        if (cursor.moveToFirst()) {
            do {
                PantryItem item = new PantryItem();
                item.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)));
                item.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)));
                item.setQuantity(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_QUANTITY)));
                item.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(COL_UNIT)));
                item.setExpiryDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_EXPIRY)));
                list.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public int updatePantryItem(PantryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, item.getName());
        values.put(COL_QUANTITY, item.getQuantity());
        values.put(COL_UNIT, item.getUnit());
        values.put(COL_EXPIRY, item.getExpiryDate());
        int rows = db.update(TABLE_PANTRY, values, COL_ID + " = ?", new String[]{String.valueOf(item.getId())});
        db.close();
        return rows;
    }

    public void deletePantryItem(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PANTRY, COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public PantryItem getPantryItem(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PANTRY, null, COL_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        PantryItem item = null;
        if (cursor.moveToFirst()) {
            item = new PantryItem();
            item.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)));
            item.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)));
            item.setQuantity(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_QUANTITY)));
            item.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(COL_UNIT)));
            item.setExpiryDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_EXPIRY)));
        }
        cursor.close();
        db.close();
        return item;
    }

    // ========== RECIPE METHODS ==========

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null, null, null, null, null, COL_NAME + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)));
                recipe.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)));
                recipe.setSteps(cursor.getString(cursor.getColumnIndexOrThrow(COL_STEPS)));
                recipe.setIngredients(getIngredientsForRecipe(recipe.getId()));
                recipes.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recipes;
    }

    public Recipe getRecipe(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null, COL_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        Recipe recipe = null;
        if (cursor.moveToFirst()) {
            recipe = new Recipe();
            recipe.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)));
            recipe.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)));
            recipe.setSteps(cursor.getString(cursor.getColumnIndexOrThrow(COL_STEPS)));
            recipe.setIngredients(getIngredientsForRecipe(id));
        }
        cursor.close();
        db.close();
        return recipe;
    }

    private List<RecipeIngredient> getIngredientsForRecipe(long recipeId) {
        List<RecipeIngredient> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPE_INGREDIENTS, null,
                COL_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                RecipeIngredient ing = new RecipeIngredient();
                ing.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)));
                ing.setRecipeId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_RECIPE_ID)));
                ing.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)));
                ing.setQuantity(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_QUANTITY)));
                ing.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(COL_UNIT)));
                list.add(ing);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Helper for strict matching later
    public List<PantryItem> getPantryItemsByNormalizedName(String normalizedName) {
        return getAllPantryItems(); // temporary
    }
}