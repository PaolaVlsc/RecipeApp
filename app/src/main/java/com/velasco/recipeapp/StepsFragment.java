package com.velasco.recipeapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StepsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepsFragment extends Fragment {

    private static final String ARG_GAME_ID = "recipe_id";
    private int mRecipeId;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StepsFragment() {
        // Required empty public constructor
    }


    public static StepsFragment newInstance(int recipeID) {
        StepsFragment fragment = new StepsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_GAME_ID,recipeID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipeId = getArguments().getInt(ARG_GAME_ID);
        }
    }


    View view;
    private TextView recipeId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_steps, container, false);

        recipeId = view.findViewById(R.id.recipeId);

        recipeId.setText(Integer.toString(mRecipeId));
        return view;

    }
}