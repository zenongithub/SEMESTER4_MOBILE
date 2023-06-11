package com.shedenk.app.ui.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shedenk.app.Env;
import com.shedenk.app.HomeActivity;
import com.shedenk.app.LoginActivity;
import com.shedenk.app.R;
import com.shedenk.app.RegisterActivity;
import com.shedenk.app.VolleyConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProfileEdit extends AppCompatActivity {
    EditText nama, password;
    CheckBox show;
    TextView email, id;
    Button simpan;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        nama = findViewById(R.id.editprofile_nama);
        password = findViewById(R.id.editprofile_password);
        show = findViewById(R.id.showpassword_profile);
        email = findViewById(R.id.editprofile_email);
        id = findViewById(R.id.editprofile_idakun);
        simpan = findViewById(R.id.btn_simpaneditprofile);

        nama.setText(getIntent().getExtras().getString("namaakun"));
        password.setText(getIntent().getExtras().getString("passwordakun"));
        email.setText(getIntent().getExtras().getString("emailakun"));
        id.setText(getIntent().getExtras().getString("idakun"));

        show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b){
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(ProfileEdit.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Apakah Mau Update Akun?")
                        .setConfirmText("Ya, Update!")
                        .setCancelText("Batal")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                String sid = id.getText().toString();
                                String snama = nama.getText().toString();
                                String spassword = password.getText().toString();

                                updateAkun(sid, snama, spassword);
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        }).show();
            }
        });

    }

    private void updateAkun(String sid, String snama, String spassword) {
        if (checkNetworkConnection()) {
//            progressDialog.show();
            String url = Env.BASE_URL + "update";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("success");
                                if (resp.equals("Berhasil Update")) {
//                                    Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
                                    new SweetAlertDialog(ProfileEdit.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Berhasil Update")
                                            .setContentText("Silahkan Login Kembaliq ")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    Intent intent = new Intent(ProfileEdit.this, LoginActivity.class);
                                                    startActivity(intent);
                                                }
                                            })
                                            .show();
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
                    Toast.makeText(getApplicationContext(), "Gagal Mengirim Data", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id_akun", sid);
                    params.put("nama", snama);
                    params.put("password", spassword);
                    return params;
                }
            };

            VolleyConnection.getInstance(ProfileEdit.this).addToRequestQue(stringRequest);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    progressDialog.cancel();
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