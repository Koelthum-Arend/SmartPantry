package com.example.smartpant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpant.adapter.PantryAdapter;
import com.example.smartpant.database.DatabaseHelper;
import com.example.smartpant.model.PantryItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PantryAdapter.OnPantryChangeListener {

    private RecyclerView recyclerPantry;
    private TextView tvEmptyPantry;
    private PantryAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<PantryItem> pantryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        recyclerPantry = findViewById(R.id.recyclerPantry);
        tvEmptyPantry = findViewById(R.id.tvEmptyPantry);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        recyclerPantry.setLayoutManager(new LinearLayoutManager(this));

        loadPantryItems();

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditIngredientActivity.class);
            startActivity(intent);
        });

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_pantry) {
                return true;
            } else if (id == R.id.nav_suggested) {
                startActivity(new Intent(this, SuggestedRecipesActivity.class));
                return true;
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            }
            return false;
        });

        bottomNav.setSelectedItemId(R.id.nav_pantry);
    }

    private void loadPantryItems() {
        pantryList = dbHelper.getAllPantryItems();

        if (adapter == null) {
            adapter = new PantryAdapter(this, pantryList, this);
            recyclerPantry.setAdapter(adapter);
        } else {
            adapter.updateList(pantryList);
        }

        if (pantryList.isEmpty()) {
            tvEmptyPantry.setVisibility(View.VISIBLE);
            recyclerPantry.setVisibility(View.GONE);
        } else {
            tvEmptyPantry.setVisibility(View.GONE);
            recyclerPantry.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantryItems(); // Refresh list when coming back from Add/Edit
    }

    @Override
    public void onPantryChanged() {
        loadPantryItems();
    }
}