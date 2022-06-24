package com.velasco.recipeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {


    // implemet OnItemClickListener
    public interface OnItemClickListener {
        void onItemClick(Ingredient item);
    }


    // χαρακτηριστικά μιας Adapter κλάσης
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Ingredient> mList;
    private final IngredientAdapter.OnItemClickListener listener;


    public IngredientAdapter(Context context, ArrayList<Ingredient> mList, OnItemClickListener listener) {
        inflater = LayoutInflater.from(context); // φτιάχνει οπτικά αντικείμενα.
        this.context = context;
        this.mList = mList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_ingredient, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mList.get(position), listener); // mporw na ta kanw edw h se mia function
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Σύνδεση με το xml item
        private TextView instructionTv;
        private ImageView logoItemIv, deleteItemIv;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Σύνδεση με το xml item
            instructionTv = itemView.findViewById(R.id.tv_ingredient);
        }

        public void bind(Ingredient ingredient, OnItemClickListener listener) {

            instructionTv.setText(ingredient.getName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(ingredient);
                }
            });


        }
    }
}
