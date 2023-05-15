package com.shedenk.app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shedenk.app.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

//    ActivityLoginBinding binding;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityLoginBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        binding.txtregister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
//                startActivity(intent);
//            }
//        });
//        binding.btnlogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (binding.TextEmail.getText().toString().isEmpty()){
//                    binding.TextEmail.setError("Masukan Email");
//                } else if (binding.TextPassword.getText().toString().isEmpty()) {
//                    binding.TextPassword.setError("Masukan Password");
//                }else {
//                    LoginUser(binding);
//                }
//            }
//        });
//    }
//
//    private void LoginUser(ActivityLoginBinding binding) {
//
//        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.86.194:8000/api/login", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                startActivity(new Intent(LoginActivity.this , HomeActivity.class));
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String , String> map = new HashMap<>();
//
//                map.put("email" , binding.TextEmail.getText().toString());
//                map.put("password" , binding.TextPassword.getText().toString());
//
//                return map;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(request);
//    }


    EditText email, password;
    Button login;
    TextView register;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.TextEmail);
        password = (EditText) findViewById(R.id.TextPassword);
        login = (Button) findViewById(R.id.btnlogin);
        register = (TextView) findViewById(R.id.txtregister);
        progressDialog = new ProgressDialog(LoginActivity.this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sEmail = email.getText().toString();
                String sPassword = password.getText().toString();

                CheckLogin(sEmail, sPassword);
            }
        });
    }

    public void CheckLogin(final String email, final String password) {
        if (checkNetworkConnection()) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.86.194:8000/api/login",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("message");
                                if (resp.equals("Sukses")) {

                                    Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };

            VolleyConnection.getInstance(LoginActivity.this).addToRequestQue(stringRequest);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.cancel();
                }
            }, 2000);
        } else {
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}

