package com.example.rentalapartmentsfinder.ui.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rentalapartmentsfinder.R;
import com.example.rentalapartmentsfinder.databinding.FragmentAppLoadingBinding;
import com.google.firebase.auth.FirebaseAuth;


public class AppLoading extends Fragment {

    private FragmentAppLoadingBinding binding;
    FirebaseAuth mAuth;

    public AppLoading() {
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
        binding = FragmentAppLoadingBinding.inflate(getLayoutInflater());
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            // user logged in
            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.home2);
        }else{
            // user not logged in
            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.authScreen);
        }
        return binding.getRoot();
    }
}