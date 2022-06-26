package com.velasco.recipeapp.CRUD_Recipe.Sub_CRUD_Recipe;

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
import com.velasco.recipeapp.Pojo.Instruction;
import com.velasco.recipeapp.WebServices.Constants;
import com.velasco.recipeapp.R;
import com.velasco.recipeapp.WebServices.Singleton.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditInstructionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditInstructionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditInstructionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditInstructionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditInstructionFragment newInstance(String param1, String param2) {
        EditInstructionFragment fragment = new EditInstructionFragment();
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
    private Button updateBtn;
    private EditText instructionEditText;
    private String instructionTxt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_instruction, container, false);

        // views
        cancelBtn = view.findViewById(R.id.btn_cancel);
        updateBtn = view.findViewById(R.id.btn_updateInstruction);
        instructionEditText = view.findViewById(R.id.et_instruction);

        // get data from previous fragment
        Bundle bundle = this.getArguments();
        Instruction instruction = bundle.getParcelable("instructionID");

        instructionTxt = instruction.getDescription();
        instructionEditText.setText(instructionTxt);


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get text
                instructionTxt = instructionEditText.getText().toString();
                // check if it's empty or not

                // send query
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_UPDATE_INSTRUCTION, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            //Toast.makeText(AddActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            if (jsonObject.getString("success").equals("true")) {

                                //Snackbar.make(view, "updated" + instruction.getId(), Snackbar.LENGTH_LONG).show();
                                getParentFragmentManager().beginTransaction().add(R.id.stepsFrag, new StepsFragment().newInstance(instruction.getRecipe())).commit();
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
                        params.put("id", Integer.toString(instruction.getId()));
                        params.put("description", instructionEditText.getText().toString());
                        return params;
                    }
                };
                RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);


            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().add(R.id.stepsFrag, new StepsFragment().newInstance(instruction.getRecipe())).commit();
            }
        });
        return view;
    }
}