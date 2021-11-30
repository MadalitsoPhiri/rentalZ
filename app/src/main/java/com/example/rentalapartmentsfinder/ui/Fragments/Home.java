package com.example.rentalapartmentsfinder.ui.Fragments;

import android.os.Bundle;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.PopupMenu;

import com.example.rentalapartmentsfinder.Adapters.PropertyAdapter;
import com.example.rentalapartmentsfinder.R;
import com.example.rentalapartmentsfinder.databinding.FragmentHomeBinding;
import com.example.rentalapartmentsfinder.models.property;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;


public class Home extends Fragment {

  private FragmentHomeBinding binding;
  private ArrayList<property> data = new ArrayList<>();
    private ArrayList<property> backup = new ArrayList<>();

    private final String TAG = "Home";
  private PropertyAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Home() {
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
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.apartmentsRecyclerView.setLayoutManager(layoutManager);
        adapter = new PropertyAdapter(data,getParentFragment(),getContext());
        binding.loadingProgress.setVisibility(View.VISIBLE);
        binding.loadingText.setVisibility(View.VISIBLE);
        binding.apartmentsRecyclerView.setVisibility(View.GONE);
        binding.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view, R.menu.home_menu);
            }
        });
        binding.searchTextField.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.searchTextField.getEditText().setText("");
            }
        });

        binding.searchTextField.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                    String newText = editable.toString().trim().toLowerCase();
                    if(newText.isEmpty()){
                        data.clear();
                        for(property current: backup){
                            data.add(current);
                        }

                    }else{
                        data.clear();
                        Log.d(TAG, "afterTextChanged: "+ newText);

                        ArrayList<property> filtered = new ArrayList<>();
                        for(property current : backup){
                            if(current.getDate().toLowerCase().contains(newText) || current.getReporterName().toLowerCase().contains(newText) || current.getMonthlyRent().toLowerCase().contains(newText) || current.getFurnitureType().toLowerCase().contains(newText) || current.getNotes().toLowerCase().contains(newText) || current.getBedrooms().toLowerCase().contains(newText) || current.getPropertyType().toLowerCase().contains(newText)){
                                data.add(current);
                            }
                        }

                    }
                adapter.notifyDataSetChanged();


            }
        });
        db.collection("property")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            data.clear();
                            backup.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                               property current = new property(document.getId(),document.get("reporterName").toString(),document.get("bedrooms").toString(),document.get("date").toString(),document.get("notes").toString(),document.get("propertyType").toString(),document.get("rental").toString(),document.get("furniture").toString());
                               data.add(current);
                                backup.add(current);
                            }
                            binding.loadingProgress.setVisibility(View.GONE);
                            binding.loadingText.setVisibility(View.GONE);
                            binding.apartmentsRecyclerView.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        binding.apartmentsRecyclerView.setAdapter(adapter);
        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.addApartment);
            }
        });
        return binding.getRoot();
    }

    private void showMenu( View v, @MenuRes int menu) {

        PopupMenu popup = new PopupMenu(getContext(),v);
        popup.getMenuInflater().inflate(menu, popup.getMenu());


       popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem menuItem) {
               switch (menuItem.getItemId()){
                   case R.id.delete_all:
                       deleteAllProperties();
                       break;
                   case R.id.log_out:
                       logout();
                       break;
                   default:
                       return false;
               }
               return false;
           }
       });

       popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
           @Override
           public void onDismiss(PopupMenu popupMenu) {

           }
       });

        // Show the popup menu.
        popup.show();
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        NavHostFragment.findNavController(getParentFragment()).popBackStack(R.id.home2, true);
        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.authScreen);

    }

    private void deleteAllProperties(){
        binding.loadingProgress.setVisibility(View.VISIBLE);
        binding.loadingText.setText("Deleting All Properties....");
        binding.loadingText.setVisibility(View.VISIBLE);
        binding.apartmentsRecyclerView.setVisibility(View.GONE);
        db.collection("property")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            data.clear();
                            backup.clear();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().delete();
                            }
                            binding.loadingProgress.setVisibility(View.GONE);
                            binding.loadingText.setVisibility(View.GONE);
                            binding.apartmentsRecyclerView.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}