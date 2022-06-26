package com.velasco.recipeapp;

import static com.velasco.recipeapp.Constants.URL_LOGIN;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.velasco.recipeapp.Singleton.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    private TextInputEditText emailEt, passwordEt;
    private TextView linkRegisterTv;
    private Button loginBtn;
    private ProgressBar loadingPb;


    String emailTxt, passwordTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // check if user is logged in
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        // sundesh
        loadingPb = findViewById(R.id.pb_loading);
        emailEt = findViewById(R.id.et_email);
        passwordEt = findViewById(R.id.et_password);
        loginBtn = findViewById(R.id.btn_login);
        linkRegisterTv = findViewById(R.id.tv_linkRegister);

        //if user presses on login
        //calling the method login
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();

            }
        });


        // go to register screen
        linkRegisterTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }

    private void userLogin() {
        emailTxt = emailEt.getText().toString().trim();
        passwordTxt = passwordEt.getText().toString().trim();

        // validate inputs
        if (validate()) {
            //if everything is fine
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loadingPb.setVisibility(View.GONE);

                            try {
                                //converting response to json object
                                JSONObject jsonObject = new JSONObject(response);
                                Gson gson = new Gson();

                                //if no error in response
                                if (jsonObject.getString("success").equals("true")) {
                                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    Log.i("TEST", "onResponse: TOTAL ROWS=" + jsonObject.getString("data"));
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                                    JSONObject userJson = jsonArray.getJSONObject(0);
                                    User user = gson.fromJson(userJson.toString(), User.class);
                                    Log.i("TEST", user.getName() + " " + user.getEmail());
//                                    for (int i = 0; i < jsonArray.length(); i++) {
//                                        JSONObject userJson = jsonArray.getJSONObject(i);
//                                        user = gson.fromJson(userJson.toString(), User.class);
//                                        Log.i("TEST", user.getName() + " " + user.getEmail());
//                                    }


                                    //getting the user from the response
                                    //   JSONObject userJson = jsonObject.getJSONObject("user");

                                    //creating a new user object
//                                    User user = gson.fromJson(jsonObject.getString("data"), User.class);
//                                    Log.i("TEST", "user" + user.getId());

//
                                    //storing the user in shared preferences
                                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                    //starting the profile activity
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    Log.i("TEST", "user" + user.getName());

                                    // finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", emailTxt);
                    params.put("password", passwordTxt);
                    return params;
                }
            };

            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        } else {
            Toast.makeText(getApplicationContext(), "A field is empty", Toast.LENGTH_SHORT).show();
        }

    }


    boolean validate() {
        if (emailTxt.isEmpty()) {
            emailEt.setError("Please enter your username");
            emailEt.requestFocus();
            return false;
        }

        if (passwordTxt.isEmpty()) {
            passwordEt.setError("Please enter your password");
            passwordEt.requestFocus();
            return false;
        }


        return true;


    }
}