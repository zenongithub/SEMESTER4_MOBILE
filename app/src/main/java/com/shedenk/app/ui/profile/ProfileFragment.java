package com.shedenk.app.ui.profile;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.shedenk.app.LoginActivity;
import com.shedenk.app.R;
import com.shedenk.app.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    TextView email;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        email = view.findViewById(R.id.profile_email);

        sharedPreferences = view.getContext().getSharedPreferences("LoginFile", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Button btnKeluar = view.findViewById(R.id.btn_keluar);
        btnKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear().apply();
                Intent LoginIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(LoginIntent);
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