package com.velasco.recipeapp.CRUD_Recipe;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.velasco.recipeapp.CRUD_Recipe.Sub_CRUD_Recipe.IngredientsFragment;
import com.velasco.recipeapp.CRUD_Recipe.Sub_CRUD_Recipe.StepsFragment;

// src: https://stackoverflow.com/questions/63998971/pass-data-from-activity-to-fragment-via-fragmentstateadapter
public class MyFragmentAdapter extends FragmentStateAdapter {

    private int recipe_id;

    public MyFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, int recipe_id) {
        super(fragmentManager, lifecycle);
        this.recipe_id = recipe_id;  // get parameter
    }

    // set Fragment
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new StepsFragment().newInstance(recipe_id);
        }
        return new IngredientsFragment().newInstance(recipe_id);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
