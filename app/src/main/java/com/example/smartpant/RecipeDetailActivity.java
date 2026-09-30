package com.example.smartpant;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpant.database.DatabaseHelper;
import com.example.smartpant.model.Recipe;
import com.example.smartpant.model.RecipeIngredient;
import com.google.android.material.appbar.MaterialToolbar;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        TextView tvRecipeName = findViewById(R.id.tvRecipeName);
        TextView tvIngredients = findViewById(R.id.tvIngredients);
        TextView tvSteps = findViewById(R.id.tvSteps);

        toolbar.setNavigationOnClickListener(v -> finish());

        long recipeId = getIntent().getLongExtra("recipe_id", -1);
        if (recipeId == -1) {
            finish();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Recipe recipe = dbHelper.getRecipe(recipeId);

        if (recipe != null) {
            tvRecipeName.setText(recipe.getName());
            tvSteps.setText(recipe.getSteps());

            // Build ingredients list
            StringBuilder ingredientsText = new StringBuilder();
            for (RecipeIngredient ing : recipe.getIngredients()) {
                ingredientsText.append("• ")
                        .append(ing.getQuantity())
                        .append(" ")
                        .append(ing.getUnit() != null ? ing.getUnit() : "")
                        .append(" ")
                        .append(ing.getName())
                        .append("\n");
            }
            tvIngredients.setText(ingredientsText.toString().trim());
        }
    }
}