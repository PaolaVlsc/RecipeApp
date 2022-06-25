package com.velasco.recipeapp.RecyclerViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.velasco.recipeapp.Bean.Category;
import com.velasco.recipeapp.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    // implemet OnItemClickListener
    public interface OnItemClickListener {
        void onItemClick(Category item);
    }

    // χαρακτηριστικά μιας Adapter κλάσης
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Category> mList;
    private final OnItemClickListener listener;


    // θα ζητηθεί constructor
    public CategoryAdapter(Context context, ArrayList<Category> mList, OnItemClickListener listener) {
        inflater = LayoutInflater.from(context); // φτιάχνει οπτικά αντικείμενα.
        this.context = context;
        this.mList = mList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_category, parent, false); // sundesh me thn xml item
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        holder.bind(mList.get(position), listener); // mporw na ta kanw edw h se mia function
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Σύνδεση με το xml item
        private TextView categoryNameTv;
        private ImageView imageViewTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Σύνδεση με το xml item
            categoryNameTv = itemView.findViewById(R.id.tv_categoryName);
            imageViewTv = itemView.findViewById(R.id.imageview_categoryImage);

        }

        // Αναλαμβάνει να τοποθετεί την πληροφοία στα πεδία που πρέπει
        // Ο adapter καλεί τον bind
        public void bind(Category category, OnItemClickListener listener) {

            categoryNameTv.setText(category.getName());
            //imageViewTv.setImageResource(); // logo

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(category);
                }
            });


        }
    }
}
