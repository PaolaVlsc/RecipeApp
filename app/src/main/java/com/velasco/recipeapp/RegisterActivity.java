package com.velasco.recipeapp;

import static com.velasco.recipeapp.Constants.URL_REGISTER;

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
import com.velasco.recipeapp.Singleton.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText nameTiet, emailTiet, passwordTiet, passwordConfirmTiet;
    private Button registerBtn;
    private ProgressBar loadingPb;
    private TextView loginTv;
    private String nameTxt, emailTxt, passwordTxt, passwordConfirmTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // sundesh me xml
        loadingPb = findViewById(R.id.pb_loading);

        //if the user is already logged in we will directly start the profile activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }


        // sundesh me xml
        nameTiet = findViewById(R.id.et_name);
        emailTiet = findViewById(R.id.et_email);
        passwordTiet = findViewById(R.id.et_password);
        passwordConfirmTiet = findViewById(R.id.et_passwordConfirm);
        registerBtn = findViewById(R.id.btn_register);
        loginTv = findViewById(R.id.tv_login);
        passwordConfirmTiet = findViewById(R.id.et_passwordConfirm);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if user pressed on button register
                //here we will register the user to server
                registerUser();
            }
        });

        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if user pressed on login
                //we will open the login screen
                finish();
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });

    }

    private void registerUser() {
        nameTxt = nameTiet.getText().toString().trim();
        passwordTxt = passwordTiet.getText().toString().trim();
        emailTxt = emailTiet.getText().toString().trim();


        // validate
        if (validate()) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loadingPb.setVisibility(View.GONE);

                            try {
                                //converting response to json object
                                JSONObject jsonObject = new JSONObject(response);
                                Gson gson = new Gson();
                                //if no error in response
                                if (jsonObject.getString("success").equals("1")) {
                                    Toast.makeText(RegisterActivity.this, "Success login: ", Toast.LENGTH_SHORT).show();
                                    Log.i("TEST", "onResponse: TOTAL ROWS=" + jsonObject.getString("data"));

                                    User user = gson.fromJson(jsonObject.getString("data"), User.class);
                                    Log.i("TEST", "user" + user.getId());

//                                    //getting the user from the response
//                                    JSONObject userJson = jsonObject.getJSONObject("data");
//
//                                    //creating a new user object
//                                    User user = new User(
//                                            userJson.getInt("id"),
//                                            userJson.getString("name"),
//                                            userJson.getString("email")
//                                    );
//
//                                    //storing the user in shared preferences
                                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
//
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
                    Map<String, String> params = new HashMap<>();
                    params.put("name", nameTxt);
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

    private boolean validate() {
        if (TextUtils.isEmpty(nameTxt)) {
            nameTiet.setError("Please enter username");
            nameTiet.requestFocus();
            return false;
        }


        if (TextUtils.isEmpty(passwordTxt)) {
            passwordTiet.setError("Enter a password");
            passwordTiet.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(emailTxt)) {
            emailTiet.setError("Please enter your email");
            emailTiet.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailTxt).matches()) {
            emailTiet.setError("Enter a valid email");
            emailTiet.requestFocus();
            return false;

        }

        return true;
    }
}

