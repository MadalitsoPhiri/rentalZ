package com.example.rentalapartmentsfinder.ui.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rentalapartmentsfinder.R;
import com.example.rentalapartmentsfinder.databinding.FragmentPropertyDetailsBinding;
import com.example.rentalapartmentsfinder.models.property;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;


public class PropertyDetails extends Fragment {

    private FragmentPropertyDetailsBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "PropertyDetails";


    public PropertyDetails() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPropertyDetailsBinding.inflate(getLayoutInflater());
        Serializable args = getArguments().getSerializable("property");
         property current = ((property) args);

        Log.d(TAG, "rent: " +current.getMonthlyRent());
        binding.reporter.setText("Reporter Name: "+current.getReporterName());
        binding.propertyType.setText("Property Type: "+current.getPropertyType());
        binding.bedrooms.setText("Bedrooms: "+current.getBedrooms());
        binding.date.setText("Date: "+current.getDate());
        binding.notes.setText("Notes: "+current.getNotes());
        binding.furniture.setText("Furniture Type: "+current.getFurnitureType());
        binding.rental.setText("Monthly Rental: "+current.getMonthlyRent());
//        db.collection("property").document(propertyId)
//        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                    } else {
//                        Log.d(TAG, "No such document");
//                    }
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                }
//            }
//        });
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(getParentFragment()).popBackStack();
            }
        });
        return binding.getRoot();
    }
}