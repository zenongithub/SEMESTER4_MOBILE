package com.shedenk.app.ui.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.shedenk.app.LoginActivity;
import com.shedenk.app.R;
import com.shedenk.app.SessionManager;
import com.shedenk.app.databinding.FragmentProfileBinding;
import com.shedenk.app.produk.DetailProdukKeranjang;

import java.util.HashMap;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    SessionManager sessionManager;
    TextView id, nama, email, password;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        sessionManager = new SessionManager(getActivity());
        sessionManager.checkLogin();

        id = view.findViewById(R.id.id_akunprofile);
        nama = view.findViewById(R.id.nama_akunprofile);
        email = view.findViewById(R.id.email_akunprofile);
        password = view.findViewById(R.id.password_akunprofile);


        HashMap<String,String> user = sessionManager.getUserDetail();
        String sid = user.get(sessionManager.ID);
        String snama = user.get(sessionManager.NAMA);
        String semail = user.get(sessionManager.EMAIL);
        String spassword = user.get(sessionManager.PASSWORD);

        id.setText(sid);
        nama.setText(snama);
        email.setText(semail);
        password.setText(spassword);

        Button btnKeluar = view.findViewById(R.id.btn_keluar);
        btnKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logout();
                Intent LoginIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(LoginIntent);
            }
        });
        Button btneditprofile = view.findViewById(R.id.btn_EditProfile);
        btneditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ProfileEdit.class);

                intent.putExtra("idakun", id.getText());
                intent.putExtra("namaakun", nama.getText());
                intent.putExtra("emailakun", email.getText());
                intent.putExtra("passwordakun", password.getText());

                startActivityForResult(intent, 1);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}