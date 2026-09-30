package com.example.smartpant;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpant.adapter.RecipeAdapter;
import com.example.smartpant.database.DatabaseHelper;
import com.example.smartpant.model.PantryItem;
import com.example.smartpant.model.Recipe;
import com.example.smartpant.model.RecipeIngredient;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerSuggested;
    private TextView tvNoRecipes;
    private RecipeAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        dbHelper = new DatabaseHelper(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        recyclerSuggested = findViewById(R.id.recyclerSuggested);
        tvNoRecipes = findViewById(R.id.tvNoRecipes);

        toolbar.setNavigationOnClickListener(v -> finish());
        recyclerSuggested.setLayoutManager(new LinearLayoutManager(this));

        loadSuggestedRecipes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSuggestedRecipes(); // refresh if pantry changed
    }

    private void loadSuggestedRecipes() {
        List<Recipe> allRecipes = dbHelper.getAllRecipes();
        List<PantryItem> pantry = dbHelper.getAllPantryItems();

        // Build a map of normalized pantry ingredient → available quantity
        Map<String, Double> pantryMap = new HashMap<>();
        for (PantryItem item : pantry) {
            String key = normalize(item.getName());
            double current = pantryMap.getOrDefault(key, 0.0);
            pantryMap.put(key, current + item.getQuantity());
        }

        // Strict matching
        List<Recipe> matched = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            if (canMakeRecipe(recipe, pantryMap)) {
                matched.add(recipe);
            }
        }

        if (adapter == null) {
            adapter = new RecipeAdapter(this, matched);
            recyclerSuggested.setAdapter(adapter);
        } else {
            adapter.updateList(matched);
        }

        if (matched.isEmpty()) {
            tvNoRecipes.setVisibility(View.VISIBLE);
            recyclerSuggested.setVisibility(View.GONE);
        } else {
            tvNoRecipes.setVisibility(View.GONE);
            recyclerSuggested.setVisibility(View.VISIBLE);
        }
    }

    /**
     * STRICT MATCHING RULE
     * A recipe is only suggested if EVERY required ingredient
     * exists in the pantry in at least the required quantity.
     */
    private boolean canMakeRecipe(Recipe recipe, Map<String, Double> pantryMap) {
        for (RecipeIngredient needed : recipe.getIngredients()) {
            String key = normalize(needed.getName());
            double available = pantryMap.getOrDefault(key, 0.0);

            if (available < needed.getQuantity()) {
                return false; // missing or not enough
            }
        }
        return true; // all ingredients present in sufficient quantity
    }

    /**
     * Simple normalization to handle common real-world differences
     * e.g. "Tomatoes" vs "tomato", "Eggs" vs "egg"
     */
    private String normalize(String name) {
        if (name == null) return "";
        String n = name.toLowerCase(Locale.ROOT).trim();

        // Remove common plural endings
        if (n.endsWith("oes") && n.length() > 3) {          // tomatoes → tomato
            n = n.substring(0, n.length() - 2);
        } else if (n.endsWith("es") && n.length() > 3) {    // potatoes → potato, etc.
            n = n.substring(0, n.length() - 2);
        } else if (n.endsWith("s") && n.length() > 2) {     // eggs → egg
            n = n.substring(0, n.length() - 1);
        }

        return n;
    }
}