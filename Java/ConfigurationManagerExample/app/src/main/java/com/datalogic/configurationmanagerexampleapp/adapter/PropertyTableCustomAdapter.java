package com.datalogic.configurationmanagerexampleapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.datalogic.configurationmanagerexampleapp.R;

import org.jetbrains.annotations.NotNull;


public class PropertyTableCustomAdapter extends RecyclerView.Adapter<PropertyTableCustomAdapter.ViewHolder> {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView[] row;

        public ViewHolder(@NonNull @NotNull View view) {
            super(view);
            /* A row has 4 elements (columns) */
            row = new TextView[]{
                    view.findViewById(R.id.row_col_0),
                    view.findViewById(R.id.row_col_1),
                    view.findViewById(R.id.row_col_2),
                    view.findViewById(R.id.row_col_3),
                    view.findViewById(R.id.row_col_4)};
        }

        public TextView getRowElement(int index){
            return row[index];
        }

        public TextView[] getRow(){
            return row;
        }
    }

    /**
     * Provide a reference
     */

    private String[][] localDataSet;
    public PropertyTableCustomAdapter(String[][] dataSet){
        localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.property_table_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder viewHolder, int row_index) {
        for (int col_index = 0; col_index<localDataSet[row_index].length; col_index++){
            TextView table_element = viewHolder.getRow()[col_index];
            table_element.setText(localDataSet[row_index][col_index]);
        }
    }

    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}
