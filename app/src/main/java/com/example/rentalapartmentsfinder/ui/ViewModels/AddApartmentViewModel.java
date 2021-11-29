package com.example.rentalapartmentsfinder.ui.ViewModels;

import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddApartmentViewModel extends ViewModel {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private void addProperty() {
        // write to firebase firestore here

    }
}
