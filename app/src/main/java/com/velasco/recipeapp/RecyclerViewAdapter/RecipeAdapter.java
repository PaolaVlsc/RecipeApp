package com.velasco.recipeapp.RecyclerViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.velasco.recipeapp.Pojo.Recipe;
import com.velasco.recipeapp.R;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    // implemet OnItemClickListener
    public interface OnItemClickListener {
        void onItemClick(Recipe item);
    }


    public interface OnItemLongClickListener {
        void onItemLongClick(Recipe item);
    }

    // χαρακτηριστικά μιας Adapter κλάσης
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Recipe> mList;
    private final RecipeAdapter.OnItemClickListener listener;
    private final OnItemLongClickListener longListener;


    // θα ζητηθεί constructor
    public RecipeAdapter(Context context, ArrayList<Recipe> mList, RecipeAdapter.OnItemClickListener listener, OnItemLongClickListener longListener) {
        inflater = LayoutInflater.from(context); // φτιάχνει οπτικά αντικείμενα.
        this.context = context;
        this.mList = mList;
        this.listener = listener;
        this.longListener = longListener;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_recipe, parent, false); // sundesh me thn xml item
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
        private TextView recipeNameTv;
        private ImageView imageViewRecipeTv;
        private TextView recipeDetailTv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Σύνδεση με το xml item
            recipeNameTv = itemView.findViewById(R.id.tv_recipeName);
            recipeDetailTv = itemView.findViewById(R.id.tv_detail);

        }

        // Αναλαμβάνει να τοποθετεί την πληροφοία στα πεδία που πρέπει
        // Ο adapter καλεί τον bind
        public void bind(Recipe recipe, OnItemClickListener listener) {
            recipeNameTv.setText(recipe.getName());
            recipeDetailTv.setText(recipe.getDescription());
            //imageViewTv.setImageResource(); // logo

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(recipe);
                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longListener.onItemLongClick(recipe);
                    return true;
                }
            });

        }
    }
}
