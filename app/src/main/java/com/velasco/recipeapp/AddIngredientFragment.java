package com.velasco.recipeapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddIngredientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddIngredientFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddIngredientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddIngredientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddIngredientFragment newInstance(String param1, String param2) {
        AddIngredientFragment fragment = new AddIngredientFragment();
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


    View view;
    private Button cancelBtn;
    private Button doneBtn;

    private EditText quantityEt, measurementEt, ingredientNameEt;
    private String quantityTxt, measurementTxt, ingredientNameTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_ingredient, container, false);

        cancelBtn = view.findViewById(R.id.btn_cancelIngredient);
        doneBtn = view.findViewById(R.id.btn_doneIngredient);

        quantityEt = view.findViewById(R.id.et_quantity);
        measurementEt = view.findViewById(R.id.et_measurement);
        ingredientNameEt = view.findViewById(R.id.et_ingredientName);


        Bundle bundle = this.getArguments();
        int id = bundle.getInt("recipeID");


        // cancel
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().add(R.id.ingredientsFrag, new IngredientsFragment().newInstance(id)).commit();
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get texts
                quantityTxt = quantityEt.getText().toString();
                measurementTxt = measurementEt.getText().toString();
                ingredientNameTxt = ingredientNameEt.getText().toString();

                // check if it is not empty

                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_ADD_INGREDIENT, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            //Toast.makeText(AddActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            if (jsonObject.getString("success").equals("true")) {
                                // success go back somewhere idk
                                Snackbar.make(view, "inserted", Snackbar.LENGTH_LONG).show();
                                ingredientNameEt.setText("");
                                quantityEt.setText("");
                                measurementEt.setText("");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(AddActivity.this, "Failed to Add Data",Toast.LENGTH_SHORT).show();
                    }
                }) {
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("name", ingredientNameTxt);
                        params.put("quantity", quantityTxt);
                        params.put("measurement", measurementTxt);
                        params.put("recipe", Integer.toString(id));
                        return params;
                    }
                };
                RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);

            }
        });


        return view;
    }
}