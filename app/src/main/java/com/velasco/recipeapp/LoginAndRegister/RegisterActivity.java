package com.velasco.recipeapp.LoginAndRegister;

import static com.velasco.recipeapp.WebServices.Constants.URL_REGISTER;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    // views on register xml
    private TextInputEditText nameTiet, emailTiet, passwordTiet, passwordConfirmTiet;
    private Button registerBtn;
    private ProgressBar loadingPb;
    private TextView loginTv;

    // hold strings of text fields
    private String nameTxt, emailTxt, passwordTxt, passwordConfirmTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // connect with xml
        loadingPb = findViewById(R.id.pb_loading);

        //if the user is already logged in we will directly start the main activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }


        // connect with xml
        nameTiet = findViewById(R.id.et_name);
        emailTiet = findViewById(R.id.et_email);
        passwordTiet = findViewById(R.id.et_password);
        passwordConfirmTiet = findViewById(R.id.et_passwordConfirm);
        registerBtn = findViewById(R.id.btn_register);
        loginTv = findViewById(R.id.tv_login);
        passwordConfirmTiet = findViewById(R.id.et_passwordConfirm);

        // login listener button
        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if user pressed on login
                //we will open the login screen
                finish();
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });

        // register listener
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if user pressed on button register
                //here we will register the user to server
                registerUser();
            }
        });

    }

    // method to register a user
    private void registerUser() {

        // get text fields data
        nameTxt = nameTiet.getText().toString().trim();
        passwordTxt = passwordTiet.getText().toString().trim();
        emailTxt = emailTiet.getText().toString().trim();

        // validate text fields
        if (validate()) {
            // send StringRequest with POST and to URL_REGISTER
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // when it receives a response make the loadingPb invisible
                            loadingPb.setVisibility(View.GONE);

                            try {
                                //converting response to json object
                                JSONObject jsonObject = new JSONObject(response);
                                Gson gson = new Gson();
                                //if response success field equals to 1
                                if (jsonObject.getString("success").equals("1")) {
                                    // Toast.makeText(RegisterActivity.this, "Success login: ", Toast.LENGTH_SHORT).show();
                                    // Log.i("TEST", "onResponse: TOTAL ROWS=" + jsonObject.getString("data"));

                                    // get response "data" and convert it to a user GSON
                                    User user = gson.fromJson(jsonObject.getString("data"), User.class);
                                    // Log.i("TEST", "user" + user.getId());

                                    //storing the user in shared preferences
                                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                    //starting the profile activity
                                    finish();
                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
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
                    // set the parameters (id, value)
                    Map<String, String> params = new HashMap<>();
                    params.put("name", nameTxt);
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

    // method to check validation of text fields
    private boolean validate() {

        // Name field should not be empty
        if (TextUtils.isEmpty(nameTxt)) {
            nameTiet.setError("Please enter your name");
            nameTiet.requestFocus();
            return false;
        }

        // password should not be empty
        if (TextUtils.isEmpty(passwordTxt)) {
            passwordTiet.setError("Enter a password");
            passwordTiet.requestFocus();
            return false;
        }

        // email should not be empty
        if (TextUtils.isEmpty(emailTxt)) {
            emailTiet.setError("Please enter your email");
            emailTiet.requestFocus();
            return false;
        }

        // email should be an email format (use a ready java method)
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailTxt).matches()) {
            emailTiet.setError("Enter a valid email");
            emailTiet.requestFocus();
            return false;
        }

        // compare if confirm pwd is the same as pwd inserted
        if (!passwordTiet.getText().toString().equals(passwordConfirmTiet.getText().toString())) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}

