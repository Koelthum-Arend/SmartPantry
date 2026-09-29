package com.example.smartpant;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpant.database.DatabaseHelper;
import com.example.smartpant.model.PantryItem;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddEditIngredientActivity extends AppCompatActivity {

    private TextInputLayout tilName, tilQuantity, tilUnit, tilExpiry;
    private TextInputEditText etName, etQuantity, etUnit, etExpiry;
    private MaterialButton btnSave;
    private DatabaseHelper dbHelper;

    private long itemId = -1; // -1 means new item, otherwise we are editing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        dbHelper = new DatabaseHelper(this);

        // Bind views
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        tilName = findViewById(R.id.tilName);
        tilQuantity = findViewById(R.id.tilQuantity);
        tilUnit = findViewById(R.id.tilUnit);
        tilExpiry = findViewById(R.id.tilExpiry);

        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        etUnit = findViewById(R.id.etUnit);
        etExpiry = findViewById(R.id.etExpiry);
        btnSave = findViewById(R.id.btnSave);

        // Back button
        toolbar.setNavigationOnClickListener(v -> finish());

        // Check if we are editing an existing item
        if (getIntent().hasExtra("item_id")) {
            itemId = getIntent().getLongExtra("item_id", -1);
            toolbar.setTitle("Edit Ingredient");
            loadExistingItem();
        } else {
            toolbar.setTitle("Add Ingredient");
        }

        btnSave.setOnClickListener(v -> saveItem());
    }

    private void loadExistingItem() {
        PantryItem item = dbHelper.getPantryItem(itemId);
        if (item != null) {
            etName.setText(item.getName());
            etQuantity.setText(String.valueOf(item.getQuantity()));
            etUnit.setText(item.getUnit() != null ? item.getUnit() : "");
            etExpiry.setText(item.getExpiryDate() != null ? item.getExpiryDate() : "");
        }
    }

    private void saveItem() {
        // Clear previous errors
        tilName.setError(null);
        tilQuantity.setError(null);
        tilUnit.setError(null);
        tilExpiry.setError(null);

        String name = etName.getText() != null ? etName.getText().toString().trim() : "";
        String quantityStr = etQuantity.getText() != null ? etQuantity.getText().toString().trim() : "";
        String unit = etUnit.getText() != null ? etUnit.getText().toString().trim() : "";
        String expiry = etExpiry.getText() != null ? etExpiry.getText().toString().trim() : "";

        boolean isValid = true;

        // Validation
        if (TextUtils.isEmpty(name)) {
            tilName.setError("Ingredient name is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(quantityStr)) {
            tilQuantity.setError("Quantity is required");
            isValid = false;
        } else {
            try {
                double qty = Double.parseDouble(quantityStr);
                if (qty <= 0) {
                    tilQuantity.setError("Quantity must be greater than 0");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                tilQuantity.setError("Enter a valid number");
                isValid = false;
            }
        }

        if (TextUtils.isEmpty(unit)) {
            tilUnit.setError("Unit is required (e.g. g, ml, pcs)");
            isValid = false;
        }

        // Optional simple date format check
        if (!TextUtils.isEmpty(expiry) && !expiry.matches("\\d{4}-\\d{2}-\\d{2}")) {
            tilExpiry.setError("Use format yyyy-MM-dd (e.g. 2026-10-15)");
            isValid = false;
        }

        if (!isValid) {
            return;
        }

        double quantity = Double.parseDouble(quantityStr);

        PantryItem item = new PantryItem();
        item.setName(name);
        item.setQuantity(quantity);
        item.setUnit(unit);
        item.setExpiryDate(TextUtils.isEmpty(expiry) ? null : expiry);

        if (itemId == -1) {
            // Create new
            long id = dbHelper.addPantryItem(item);
            if (id > 0) {
                Toast.makeText(this, "Ingredient added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add ingredient", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Update existing
            item.setId(itemId);
            int rows = dbHelper.updatePantryItem(item);
            if (rows > 0) {
                Toast.makeText(this, "Ingredient updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update ingredient", Toast.LENGTH_SHORT).show();
            }
        }
    }
}