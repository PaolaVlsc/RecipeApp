package com.velasco.recipeapp.CRUD_Recipe;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.velasco.recipeapp.Bean.Recipe;
import com.velasco.recipeapp.Constants;
import com.velasco.recipeapp.R;
import com.velasco.recipeapp.RecyclerViewAdapter.RecipeAdapter;
import com.velasco.recipeapp.SharedPrefManager;
import com.velasco.recipeapp.Singleton.RequestHandler;
import com.velasco.recipeapp.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StartersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeFragment newInstance(String param1, String param2) {
        RecipeFragment fragment = new RecipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    /********************** START OF CODE *********************/
    View view;


    private ArrayList<Recipe> mRecipeList;
    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;

    private ActivityResultLauncher<Intent> mActivityResultLauncher;
    //getting the current user
    User user = SharedPrefManager.getInstance(getContext()).getUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recipe, container, false);

        mRecyclerView = view.findViewById(R.id.rv_starters);

        // Layout Manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());  // Κάθε item είναι μια γραμμή
        mRecyclerView.setLayoutManager(layoutManager);

        // Διαχωριστικά των items μεταξύ τους
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        // animation: fade in , fade out
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Bundle bundle = this.getArguments();
        int category_id = bundle.getInt("category_id");




        mRecipeList = new ArrayList<>();


        mRecipeAdapter = new RecipeAdapter(getContext(), mRecipeList, new RecipeAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(Recipe item) {


                int id = item.getId();
                Bundle bundle = new Bundle();
                bundle.putInt("recipeID", id);

                DetailsFragment detailsFragment = new DetailsFragment();
                detailsFragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.categoriesFrag, detailsFragment).addToBackStack(null).commit();

            }
        }, new RecipeAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Recipe item) {
                //display the dialog to confirm deletion
                showDialog(item, category_id);
            }
        });

        mRecyclerView.setAdapter(mRecipeAdapter);


        refreshList(category_id);
        mActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Log.i("TEST", "onActivityResult: ");
                            Intent data = result.getData();
                            Log.i("TEST", "onActivityResult: DATA: " + data.getData().toString());
                            refreshList(category_id);
                        }
                    }
                });

        return view;
    }


    private void showDialog(Recipe recipe, int category_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final CharSequence[] dialogItems = {"Edit Data", "Delete Data"};
        builder.setTitle(recipe.getName());
        builder.setItems(dialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                       // Snackbar.make(view, "Edit", Snackbar.LENGTH_LONG).show();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("recipe", recipe);
                        EditRecipeFragment editRecipeFragment = new EditRecipeFragment();

                        editRecipeFragment.setArguments(bundle);
                        getParentFragmentManager().beginTransaction().replace(R.id.startersFrag, editRecipeFragment).commit();

                        break;
                    case 1:
                        AlertDialog.Builder builderDel = new AlertDialog.Builder(getContext());
                        builderDel.setTitle(recipe.getName());
                        builderDel.setMessage("Are you sure you want to delete this entry?" + Integer.toString(recipe.getId()));
                        builderDel.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_DELETE_RECIPE, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Snackbar.make(view, "deleted" + recipe.getId(), Snackbar.LENGTH_LONG).show();
                                        refreshList(category_id);
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.i("TEST", "onErrorResponse: " + error.toString());
                                    }
                                }) {
                                    protected HashMap<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<>();
                                        params.put("id", Integer.toString(recipe.getId()));
                                        return (HashMap<String, String>) params;
                                    }
                                };
                                RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
                                dialogInterface.dismiss();
                            }
                        });

                        builderDel.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builderDel.create().show();
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void refreshList(int category_id) {

        mRecipeList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SELECT_CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Gson gson = new Gson();

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    //Toast.makeText(MainActivity.this,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    Log.i("TEST", "onResponse: SUCCESS=" + jsonObject.getString("success"));
                    Log.i("TEST", "onResponse: MESSAGE=" + jsonObject.getString("message"));
                    Log.i("TEST", "onResponse: TOTAL ROWS=" + jsonObject.getString("total"));
                    Log.i("TEST", "onResponse: RESPONSE=" + response);


                    Type type = new TypeToken<ArrayList<Recipe>>() {
                    }.getType();
                    ArrayList<Recipe> recipeArrayList = gson.fromJson(jsonArray.toString(), type);
                    mRecipeList.addAll(recipeArrayList);


                    mRecyclerView.setAdapter(mRecipeAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(MainActivity.this, "Failed " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("category", Integer.toString(category_id));
                params.put("userid", Integer.toString(user.getId()));
                return params;
            }
        };
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}
