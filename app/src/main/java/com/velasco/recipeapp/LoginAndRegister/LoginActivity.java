package com.velasco.recipeapp.LoginAndRegister;

import static com.velasco.recipeapp.WebServices.Constants.URL_LOGIN;

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
import com.velasco.recipeapp.Pojo.User;
import com.velasco.recipeapp.MainActivity;
import com.velasco.recipeapp.R;
import com.velasco.recipeapp.WebServices.Singleton.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {


    // views on login xml
    private TextInputEditText emailEt, passwordEt;
    private TextView linkRegisterTv;
    private Button loginBtn;
    private ProgressBar loadingPb;

    // hold strings of text fields
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

        // connect with xml
        loadingPb = findViewById(R.id.pb_loading);
        emailEt = findViewById(R.id.et_email);
        passwordEt = findViewById(R.id.et_password);
        loginBtn = findViewById(R.id.btn_login);
        linkRegisterTv = findViewById(R.id.tv_linkRegister);

        // login listener
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();

            }
        });


        // register listener: go to register screen / activity
        linkRegisterTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }

    // method to login
    private void userLogin() {

        // get user's text
        emailTxt = emailEt.getText().toString().trim();
        passwordTxt = passwordEt.getText().toString().trim();

        // validate inputs
        if (validate()) {
            //if everything is fine
            // send StringRequest with POST and to URL_LOGIN
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            // when it reaches a response make the loadingPb invisible
                            loadingPb.setVisibility(View.GONE);

                            try {
                                //converting response to json object
                                JSONObject jsonObject = new JSONObject(response);
                                Gson gson = new Gson();

                                //if response success field equals to true
                                if (jsonObject.getString("success").equals("true")) {
                                    // Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    // Log.i("TEST", "onResponse: TOTAL ROWS=" + jsonObject.getString("data"));

                                    // convert data field to object User (GSON)
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    JSONObject userJson = jsonArray.getJSONObject(0); // only one entry
                                    User user = gson.fromJson(userJson.toString(), User.class);
                                    // Log.i("TEST", user.getName() + " " + user.getEmail());

                                    // storing the user in shared preferences
                                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                    // starting the main activity
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    // Log.i("TEST", "user" + user.getName());

                                    finish(); // end activity
                                } else {
                                    Toast.makeText(getApplicationContext(), "Wrong credentials", Toast.LENGTH_SHORT).show();
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

                    // set the parameters (id, value)
                    Map<String, String> params = new HashMap<>();
                    params.put("email", emailTxt);
                    params.put("password", passwordTxt);

                    return params;
                }
            };
            // add request to queue - singleton
            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        } else {
            Toast.makeText(getApplicationContext(), "A field is empty", Toast.LENGTH_SHORT).show();
        }

    }

    // method to check validation
    boolean validate() {

        // email should not be empty
        if (emailTxt.isEmpty()) {
            emailEt.setError("Please enter your email");
            emailEt.requestFocus();
            return false;
        }

        //  password should not be empty
        if (passwordTxt.isEmpty()) {
            passwordEt.setError("Please enter your password");
            passwordEt.requestFocus();
            return false;
        }

        return true;
    }
}