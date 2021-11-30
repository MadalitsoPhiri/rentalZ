package com.example.rentalapartmentsfinder.ui.Fragments;

import static android.view.View.VISIBLE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.rentalapartmentsfinder.R;
import com.example.rentalapartmentsfinder.databinding.FragmentAddApartmentBinding;
import com.example.rentalapartmentsfinder.databinding.FragmentPropertyDetailsBinding;
import com.example.rentalapartmentsfinder.models.property;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AddApartment extends Fragment {

    private FragmentAddApartmentBinding binding;
    private ArrayList<String> propertyType;
    private ArrayList<String> bedrooms;
    private ArrayList<String> furnitureTypes;
    private ArrayAdapter propertyTypeAdapter;
    private ArrayAdapter bedroomsAdapter;
    private ArrayAdapter furnitureTypesAdapter;
    private final String TAG = "AddApartment";
    private boolean isUpdate;
    private String documentId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public AddApartment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        binding = FragmentAddApartmentBinding.inflate(getLayoutInflater());
        if(bundle == null){
            isUpdate = false;
        }else {
            Serializable args = bundle.getSerializable("property");
            property current = ((property) args);
            isUpdate = true;
            documentId = ((property) args).getId();
            binding.propertyType.getEditText().setText(((property) args).getPropertyType());
            binding.Bedrooms.getEditText().setText(((property) args).getBedrooms());
            binding.datepickerButton.setText(((property) args).getDate());
            binding.Furniture.getEditText().setText(((property) args).getFurnitureType());
            binding.notesField.getEditText().setText(((property) args).getNotes());
            binding.reporterField.getEditText().setText(((property) args).getReporterName());
            binding.monthlyRentalField.getEditText().setText(((property) args).getMonthlyRent());

        }
        if(isUpdate){
            binding.title.setText("Update Property");
            binding.addButton.setText("Update Property");
        }

        // Inflate the layout for this fragment

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(getParentFragment()).popBackStack();
            }
        });
        propertyType = new ArrayList<String>(Arrays.asList("home","bungalow","flat"));
        bedrooms = new ArrayList<String>(Arrays.asList("studio","one","two","three","four","five","six","seven","eight","nine","ten"));
        furnitureTypes = new ArrayList<String>(Arrays.asList("furnished","unfurnished","part furnished"));
        propertyTypeAdapter = new ArrayAdapter(requireContext(),R.layout.list_item,propertyType);
        bedroomsAdapter = new ArrayAdapter(requireContext(),R.layout.list_item,bedrooms);
        furnitureTypesAdapter = new ArrayAdapter(requireContext(),R.layout.list_item,furnitureTypes);
        MaterialDatePicker picker = MaterialDatePicker.Builder.datePicker().setTitleText("Pick Property Listing Date") .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT).build();
        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                binding.datepickerButton.setText(picker.getHeaderText());
                Log.d(TAG, "onPositiveButtonClick: "+ picker.getHeaderText()) ;
                binding.datepickerButton.setError(null);
            }
        });
        picker.addOnNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.datepickerButton.setError(null);
            }
        });


        binding.propertyTypeInner.setAdapter(propertyTypeAdapter);
        binding.BedroomsInner.setAdapter(bedroomsAdapter);
        binding.FurnitureInner.setAdapter(furnitureTypesAdapter);

        binding.datepickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                picker.show(getChildFragmentManager(),"picker");
            }
        });
        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add button clicked here
                boolean isError = false;
                binding.propertyType.setError(null);
                binding.Bedrooms.setError(null);
                binding.datepickerButton.setError(null);
                binding.monthlyRentalField.setError(null);
                binding.reporterField.setError(null);



                String propertyTypeText = binding.propertyType.getEditText().getText().toString();
                if(propertyTypeText.isEmpty()){
                    isError = true;
                    binding.propertyType.setError("property type is required");
                }

                String bedroomsTypeText = binding.Bedrooms.getEditText().getText().toString();
                if(bedroomsTypeText.isEmpty()){
                      isError = true;
                    binding.Bedrooms.setError("bedrooms is required");
                }
                String dateText = binding.datepickerButton.getText().toString();
                if(dateText.isEmpty() || dateText.equals("Select Date") ){
                    isError = true;
                    binding.datepickerButton.setError("date is required");


                }
                String furnitureText = binding.Furniture.getEditText().getText().toString();
                String monthlyRentalText = binding.monthlyRentalField.getEditText().getText().toString();
                if(monthlyRentalText.isEmpty()){
                    isError = true;
                    binding.monthlyRentalField.setError("Rental is required");
                }
                String notesText = binding.notesField.getEditText().getText().toString();
                String reporterNameText = binding.reporterField.getEditText().getText().toString();
                if(reporterNameText.isEmpty()){
                    isError = true;
                    binding.reporterField.setError("reporter is required");
                }

                Log.d(TAG, "onClick: "+"propertyTypeText: "+ propertyTypeText+" bedroomsTypeText: "+bedroomsTypeText+" dateText: "+dateText+" furnitureText: "+furnitureText+" monthlyRentalText: "+monthlyRentalText+" notesText: "+notesText+" reporterNameText: "+reporterNameText+" ");
                if(!isError) {


                    Map<String, Object> property = new HashMap<>();
                    property.put("propertyType", propertyTypeText);
                    property.put("bedrooms", bedroomsTypeText);
                    property.put("date", dateText);
                    property.put("furniture", furnitureText);
                    property.put("rental", monthlyRentalText);
                    property.put("notes", notesText);
                    property.put("reporterName", reporterNameText);

                    if(!isUpdate){
                        binding.loadingProgress.setVisibility(VISIBLE);
                        binding.loadingText.setVisibility(VISIBLE);
                        binding.addPropertyLayout.setVisibility(View.GONE);
                    db.collection("property")
                            .add(property)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                    NavHostFragment.findNavController(getParentFragment()).popBackStack();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                    binding.loadingProgress.setVisibility(View.GONE);
                                    binding.loadingText.setVisibility(View.GONE);
                                    binding.addPropertyLayout.setVisibility(VISIBLE);
                                }
                            });
                }else{
                        binding.loadingText.setText("Updating property...");
                        binding.loadingProgress.setVisibility(VISIBLE);
                        binding.loadingText.setVisibility(VISIBLE);
                        binding.addPropertyLayout.setVisibility(View.GONE);

                        db.collection("property").document(documentId)
                                .update(property)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                        NavHostFragment.findNavController(getParentFragment()).popBackStack();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error updating document", e);
                                        binding.loadingProgress.setVisibility(View.GONE);
                                        binding.loadingText.setVisibility(View.GONE);
                                        binding.addPropertyLayout.setVisibility(VISIBLE);
                                    }
                                });


                }

                }

            }
        });
        return binding.getRoot();
    }
}