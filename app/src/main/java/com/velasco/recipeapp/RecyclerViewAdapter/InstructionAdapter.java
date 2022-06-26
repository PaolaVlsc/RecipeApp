package com.velasco.recipeapp.RecyclerViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.velasco.recipeapp.Pojo.Instruction;
import com.velasco.recipeapp.R;

import java.util.ArrayList;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.ViewHolder> {

    // implemet OnItemClickListener
    public interface OnItemClickListener {
        void onItemClick(Instruction item);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Instruction item);
    }

    // χαρακτηριστικά μιας Adapter κλάσης
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Instruction> mList;
    private final OnItemClickListener listener;
    private final OnItemLongClickListener longListener;

    // θα ζητηθεί constructor
    public InstructionAdapter(Context context, ArrayList<Instruction> mList, OnItemClickListener listener, OnItemLongClickListener longListener) {
        inflater = LayoutInflater.from(context); // φτιάχνει οπτικά αντικείμενα.
        this.context = context;
        this.mList = mList;
        this.listener = listener;
        this.longListener = longListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_instruction, parent, false);
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
        private TextView descriptionTv;
        private ImageView logoItemIv, deleteItemIv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Σύνδεση με το xml item
            descriptionTv = itemView.findViewById(R.id.tv_instruction);

        }

        public void bind(Instruction instruction, OnItemClickListener listener) {
            descriptionTv.setText(instruction.getDescription());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(instruction);
                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longListener.onItemLongClick(instruction);
                    return true;
                }
            });


        }
    }
}
