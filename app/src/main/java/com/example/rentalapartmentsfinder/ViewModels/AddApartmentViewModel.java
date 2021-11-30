package com.example.rentalapartmentsfinder.ViewModels;

import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

public class AddApartmentViewModel extends ViewModel {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private void addProperty() {
        // write to firebase firestore here

    }
}
