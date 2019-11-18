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

public class MainViewAdapter extends RecyclerView.Adapter<MainViewAdapter.MainViewHolder> {

    private SimpleCallback listener;
    private ArrayList<String> data;

    public MainViewAdapter(ArrayList<String> data, SimpleCallback listener) {
        this.data = data;  //getAllTypes
        this.listener = listener; //listens for onClick
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.main_view_adapter,
                        parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, final int position) {
        holder.btn.setText(data.get(position));
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

    class MainViewHolder extends RecyclerView.ViewHolder {
        Button btn;

        MainViewHolder(@NonNull View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.mainViewAdapterBtn);
        }
    }

}
