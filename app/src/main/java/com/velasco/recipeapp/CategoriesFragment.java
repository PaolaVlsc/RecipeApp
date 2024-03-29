package com.velasco.recipeapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.velasco.recipeapp.Pojo.Category;
import com.velasco.recipeapp.CRUD_Recipe.RecipeFragment;
import com.velasco.recipeapp.RecyclerViewAdapter.CategoryAdapter;
import com.velasco.recipeapp.WebServices.Singleton.RequestHandler;
import com.velasco.recipeapp.WebServices.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoriesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoriesFragment newInstance(String param1, String param2) {
        CategoriesFragment fragment = new CategoriesFragment();
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

    // recycler view
    private ArrayList<Category> mCategoryList;
    private RecyclerView mRecyclerView;
    private CategoryAdapter mCategoryAdapter;

    private ActivityResultLauncher<Intent> mActivityResultLauncher;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_categories, container, false);

        /********** start of recycler view settings ***********/
        mRecyclerView = view.findViewById(R.id.rv_categories);

        // Layout Manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());  // Κάθε item είναι μια γραμμή
        mRecyclerView.setLayoutManager(layoutManager);

        // Διαχωριστικά των items μεταξύ τους
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        // animation: fade in , fade out
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        /********** end of recycler view settings ***********/

        // set recycler view data
        mCategoryList = new ArrayList<>();
        mCategoryAdapter = new CategoryAdapter(getContext(), mCategoryList, new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Category item) {

                // send bundle to fragment ( connection between fragments )
                Bundle bundle = new Bundle();
                bundle.putInt("category_id", item.getId());

                // change current fragment
                RecipeFragment recipeFragment = new RecipeFragment();
                recipeFragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().addToBackStack(null).add(R.id.categoriesFrag, recipeFragment).commit(); // main activity is responsible
            }
        });
        mRecyclerView.setAdapter(mCategoryAdapter);

        // refresh database ( instead of liveview )
        refreshList();
        mActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            //Log.i("TEST", "onActivityResult: ");
                            Intent data = result.getData();
                            // Log.i("TEST", "onActivityResult: DATA: " + data.getData().toString());
                            refreshList();
                        }
                    }
                });

        return view;
    }

    // fetch (select) data from database
    private void refreshList() {
        mCategoryList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SELECT_CATEGORIES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // get json response
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    // Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    // Log.i("TEST", "onResponse: SUCCESS=" + jsonObject.getString("success"));
                    // Log.i("TEST", "onResponse: MESSAGE=" + jsonObject.getString("message"));
                    // Log.i("TEST", "onResponse: TOTAL ROWS=" + jsonObject.getString("total"));
                    // Log.i("TEST", "onResponse: RESPONSE=" + response);

                    // one line code
                    // ArrayList<Category> categoryArrayList = gson.fromJson(jsonArray.toString(), Category.class);
                    Type type = new TypeToken<ArrayList<Category>>() {}.getType();
                    ArrayList<Category> categoryArrayList = gson.fromJson(jsonArray.toString(), type);
                    mCategoryList.addAll(categoryArrayList);

                    mRecyclerView.setAdapter(mCategoryAdapter);
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
                params.put("name", "kl");
                return params;
            }
        };
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}