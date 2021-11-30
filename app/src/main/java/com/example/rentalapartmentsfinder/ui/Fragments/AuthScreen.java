package com.example.rentalapartmentsfinder.ui.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rentalapartmentsfinder.R;
import com.example.rentalapartmentsfinder.databinding.FragmentAuthScreenBinding;
import com.google.firebase.auth.FirebaseAuth;


public class AuthScreen extends Fragment {

    private FragmentAuthScreenBinding binding;
    FirebaseAuth mAuth;

    public AuthScreen() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAuthScreenBinding.inflate(getLayoutInflater());
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.login);
            }
        });

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.signUp);
            }
        });
        return binding.getRoot();
    }
}