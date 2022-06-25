package com.velasco.recipeapp.CRUD_Recipe.Sub_CRUD_Recipe;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.velasco.recipeapp.Bean.Ingredient;
import com.velasco.recipeapp.Bean.Instruction;
import com.velasco.recipeapp.Constants;
import com.velasco.recipeapp.R;
import com.velasco.recipeapp.RecyclerViewAdapter.IngredientAdapter;
import com.velasco.recipeapp.Singleton.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IngredientsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IngredientsFragment extends Fragment {


    private static final String RECIPE_ID = "recipe_id";
    private int mRecipeId;


    public IngredientsFragment() {
        // Required empty public constructor
    }

    public static IngredientsFragment newInstance(int recipeID) {
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle args = new Bundle();
        args.putInt(RECIPE_ID, recipeID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipeId = getArguments().getInt(RECIPE_ID);
        }
    }

    /********************** START OF CODE *********************/
    View view;

    private ArrayList<Ingredient> mIngredientList;
    private RecyclerView mRecyclerView;
    private IngredientAdapter mIngredientAdapter;

    private ActivityResultLauncher<Intent> mActivityResultLauncher;

    // add button
    private FloatingActionButton floatingActionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ingredients, container, false);

        mRecyclerView = view.findViewById(R.id.rv_ingredients);
        floatingActionButton = view.findViewById(R.id.fab);

        // Layout Manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());  // Κάθε item είναι μια γραμμή
        mRecyclerView.setLayoutManager(layoutManager);

        // Διαχωριστικά των items μεταξύ τους
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        // animation: fade in , fade out
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        /*************************/


        mIngredientList = new ArrayList<>();

        mIngredientAdapter = new IngredientAdapter(getContext(), mIngredientList, new IngredientAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Ingredient item) {

            }
        }, new IngredientAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Ingredient item) {
                //display the dialog to confirm deletion
                showDialog(item);
            }

        });

        mRecyclerView.setAdapter(mIngredientAdapter);

        refreshList();
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
                            refreshList();
                        }
                    }
                });

        // add instruction
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = mRecipeId;
                Bundle bundle = new Bundle();
                bundle.putInt("recipeID", id);

                AddIngredientFragment addIngredientFragment = new AddIngredientFragment();
                addIngredientFragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.ingredientsFrag, addIngredientFragment).addToBackStack(null).commit();
            }
        });

        return view;
    }

    private void showDialog(Ingredient ingredient) {
        //create and initialise an alert dialog
        final androidx.appcompat.app.AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Delete entry");
        alertDialog.setMessage("Are you sure you want to delete the selected contact?");

        //set the dialog OK action
        alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Snackbar.make(view, "YES WAS CLICKED" + ingredient.getId(), Snackbar.LENGTH_LONG).show();

                // Add delete functionality
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_DELETE_INGREDIENT, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {
                                Snackbar.make(view, "" + jsonObject.getString("message"), Snackbar.LENGTH_LONG).show();
                                refreshList();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("TEST", "onErrorResponse: " + error.toString());
                    }
                }) {
                    protected HashMap<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", Integer.toString(ingredient.getId()));
                        return (HashMap<String, String>) params;
                    }
                };
                RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
                dialog.dismiss();
            }
        });

        //set the dialog CANCEL action
        alertDialog.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        //show the dialog
        alertDialog.show();
    }

    private void refreshList() {
        mIngredientList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SELECT_RECIPE_INGREDIENTS, new Response.Listener<String>() {
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


                    Type type = new TypeToken<ArrayList<Ingredient>>() {
                    }.getType();
                    ArrayList<Ingredient> ingredientArrayList = gson.fromJson(jsonArray.toString(), type);
                    mIngredientList.addAll(ingredientArrayList);

                    mRecyclerView.setAdapter(mIngredientAdapter);
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
                params.put("id", Integer.toString(mRecipeId));
                return params;
            }
        };
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}