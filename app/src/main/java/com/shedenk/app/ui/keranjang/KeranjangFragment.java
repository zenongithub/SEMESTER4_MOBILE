package com.shedenk.app.ui.keranjang;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shedenk.app.R;
import com.shedenk.app.databinding.FragmentKeranjangBinding;

public class KeranjangFragment extends Fragment {

    private FragmentKeranjangBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_keranjang,container,false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}