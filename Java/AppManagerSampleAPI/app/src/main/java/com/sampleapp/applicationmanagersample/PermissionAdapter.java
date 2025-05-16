package com.sampleapp.applicationmanagersample;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class PermissionAdapter extends ArrayAdapter<String> {
    private final List<String> originalList;
    private final List<String> filteredList;
    private final List<String> selectedItems;
    public PermissionAdapter(Context context, List<String> items) {
        super(context, 0, items);
        this.originalList = new ArrayList<>(items);
        this.selectedItems = new ArrayList<>();
        filteredList = new ArrayList<>();
    }
    public List<String> getSelectedItems() {
        return selectedItems;
    }
    public void clearSelectedItems() {
        selectedItems.clear();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.permission_item, parent, false);
        }

        String item = filteredList.isEmpty() ? originalList.get(position) : filteredList.get(position);
        CheckBox checkBox = convertView.findViewById(R.id.cb_permission);

        checkBox.setText(item);
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(selectedItems.contains(item));

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedItems.add(item);
            } else {
                selectedItems.remove(item);
            }
        });

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                filteredList.clear();
                if (constraint != null) {
                    String query = constraint.toString().toLowerCase().trim();
                    Log.d("PermissionAdapter", "query: " + query);
                    for (String item : originalList) {
                        if (item.toLowerCase().startsWith(query)) {
                            filteredList.add(item);
                        }
                    }
                    results.values = filteredList;
                    results.count = filteredList.size();
                } else {
                    Log.d("PermissionAdapter", "originalList in else: " + originalList.size());
                    results.values = originalList;
                    results.count = originalList.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                addAll((List<String>) results.values);
                notifyDataSetChanged();
            }
        };
    }

}
