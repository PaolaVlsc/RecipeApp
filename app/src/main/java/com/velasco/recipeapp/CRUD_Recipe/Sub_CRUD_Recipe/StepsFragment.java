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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.velasco.recipeapp.Bean.Instruction;
import com.velasco.recipeapp.Constants;
import com.velasco.recipeapp.R;
import com.velasco.recipeapp.RecyclerViewAdapter.InstructionAdapter;
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
 * Use the {@link StepsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepsFragment extends Fragment {

    private static final String RECIPE_ID = "recipe_id";
    private int mRecipeId;

    public StepsFragment() {
        // Required empty public constructor
    }

    public static StepsFragment newInstance(int recipeID) {
        StepsFragment fragment = new StepsFragment();
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
    private TextView recipeId;
    private FloatingActionButton floatingActionButton;


    private ArrayList<Instruction> mInstructionList;
    private RecyclerView mRecyclerView;
    private InstructionAdapter mInstructionAdapter;

    private ActivityResultLauncher<Intent> mActivityResultLauncher;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_steps, container, false);
        floatingActionButton = view.findViewById(R.id.fab);

        // recipeId = view.findViewById(R.id.recipeId);
        // recipeId.setText(Integer.toString(mRecipeId));


        mRecyclerView = view.findViewById(R.id.rv_instructions);

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

        mInstructionList = new ArrayList<>();

        mInstructionAdapter = new InstructionAdapter(getContext(), mInstructionList, new InstructionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Instruction item) {

            }
        }, new InstructionAdapter.OnItemLongClickListener() {

            @Override
            public void onItemLongClick(Instruction item) {
                //display the dialog to confirm deletion
                showDialog(item);
            }
        });


        mRecyclerView.setAdapter(mInstructionAdapter);

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

                AddInstructionFragment addInstructionFragment = new AddInstructionFragment();
                addInstructionFragment.setArguments(bundle);

                getParentFragmentManager().beginTransaction().replace(R.id.stepsFrag, addInstructionFragment).addToBackStack(null).commit();
            }
        });


        return view;
    }

    private void showDialog(Instruction instruction) {
        //create and initialise an alert dialog
        final androidx.appcompat.app.AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Delete entry");
        alertDialog.setMessage("Are you sure you want to delete the selected contact?");

        //set the dialog OK action
        alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Snackbar.make(view, "YES WAS CLICKED" + instruction.getId(), Snackbar.LENGTH_LONG).show();

                // Add delete functionality
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_DELETE_INSTRUCTION, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MainActivity.this, "Successfully Deleted " + person.getName(), Toast.LENGTH_LONG).show();
                        refreshList();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("TEST", "onErrorResponse: " + error.toString());
                    }
                }) {
                    protected HashMap<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("id",Integer.toString(instruction.getId()));
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

        mInstructionList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SELECT_RECIPE_STEPS, new Response.Listener<String>() {
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


                    Type type = new TypeToken<ArrayList<Instruction>>() {
                    }.getType();
                    ArrayList<Instruction> instructionArrayList = gson.fromJson(jsonArray.toString(), type);
                    mInstructionList.addAll(instructionArrayList);

                    mRecyclerView.setAdapter(mInstructionAdapter);
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