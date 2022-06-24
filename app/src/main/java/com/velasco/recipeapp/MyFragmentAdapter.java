package com.velasco.recipeapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyFragmentAdapter extends FragmentStateAdapter {

    private int recipe_id;

    public MyFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, int recipe_id) {
        super(fragmentManager, lifecycle);
        this.recipe_id = recipe_id;
    }

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
