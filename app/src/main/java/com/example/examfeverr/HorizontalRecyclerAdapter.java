package com.example.examfeverr;


import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
/*
public class HorizontalRecyclerAdapter extends FirestoreRecyclerAdapter<model, HorizontalRecyclerAdapter.ViewHolder> {



    public HorizontalRecyclerAdapter(@NonNull FirestoreRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull HorizontalRecyclerAdapter.ViewHolder holder, int position, @NonNull model model) {

    }

    @NonNull
    @Override
    public HorizontalRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new HorizontalRecyclerAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView hrvTv;
        model usermodel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), exam_page_layout.class);
                    intent.putExtra("model", usermodel);
                    itemView.getContext().startActivity(intent);

                }
            });
        }


    }
}


 */