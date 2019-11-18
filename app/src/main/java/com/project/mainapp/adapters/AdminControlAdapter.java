package com.project.mainapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mainapp.R;
import com.project.mainapp.interfaces.SimpleCallback;

import java.util.ArrayList;

public class AdminControlAdapter extends RecyclerView.Adapter<AdminControlAdapter.AdminMainViewHolder> {

    private SimpleCallback listener;
    private ArrayList<String> data;

    public AdminControlAdapter(ArrayList<String> data, SimpleCallback listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminMainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdminMainViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.main_view_adapter,
                        parent, false) //inflating the buttons here
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AdminMainViewHolder holder, final int position) {
        holder.btn.setText("Edit " + data.get(position));
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(data.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class AdminMainViewHolder extends RecyclerView.ViewHolder {
        Button btn;

        AdminMainViewHolder(@NonNull View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.mainViewAdapterBtn);
        }
    }

}

