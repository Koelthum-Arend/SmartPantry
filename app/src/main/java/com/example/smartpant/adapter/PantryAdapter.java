package com.example.smartpant.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpant.AddEditIngredientActivity;
import com.example.smartpant.R;
import com.example.smartpant.database.DatabaseHelper;
import com.example.smartpant.model.PantryItem;

import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    private Context context;
    private List<PantryItem> pantryList;
    private DatabaseHelper dbHelper;
    private OnPantryChangeListener listener;

    public interface OnPantryChangeListener {
        void onPantryChanged();
    }

    public PantryAdapter(Context context, List<PantryItem> pantryList, OnPantryChangeListener listener) {
        this.context = context;
        this.pantryList = pantryList;
        this.dbHelper = new DatabaseHelper(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pantry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PantryItem item = pantryList.get(position);

        holder.tvName.setText(item.getName());
        holder.tvQuantity.setText(item.getQuantity() + " " + (item.getUnit() != null ? item.getUnit() : ""));

        if (item.getExpiryDate() != null && !item.getExpiryDate().isEmpty()) {
            holder.tvExpiry.setVisibility(View.VISIBLE);
            holder.tvExpiry.setText("Expires: " + item.getExpiryDate());
        } else {
            holder.tvExpiry.setVisibility(View.GONE);
        }

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditIngredientActivity.class);
            intent.putExtra("item_id", item.getId());
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            dbHelper.deletePantryItem(item.getId());
            pantryList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, pantryList.size());
            Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
            if (listener != null) listener.onPantryChanged();
        });
    }

    @Override
    public int getItemCount() {
        return pantryList.size();
    }

    public void updateList(List<PantryItem> newList) {
        this.pantryList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity, tvExpiry;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvQuantity = itemView.findViewById(R.id.tvItemQuantity);
            tvExpiry = itemView.findViewById(R.id.tvItemExpiry);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}