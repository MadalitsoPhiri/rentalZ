package com.example.rentalapartmentsfinder.Adapters;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalapartmentsfinder.R;
import com.example.rentalapartmentsfinder.databinding.PropertyListItemBinding;
import com.example.rentalapartmentsfinder.models.property;
import com.example.rentalapartmentsfinder.ui.Fragments.Home;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileReader;
import java.util.ArrayList;


public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.ViewHolder> {

    private ArrayList<property> localDataSet;
    private androidx.fragment.app.Fragment frag;
    private Context context;



    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private PropertyListItemBinding binding;
        private final String TAG = "Home";
        private PropertyAdapter adapter;
        public ViewHolder(PropertyListItemBinding binding , androidx.fragment.app.Fragment frag, ArrayList<property> data, Context context, PropertyAdapter adapter) {
            super(binding.getRoot());
            // Define click listener for the ViewHolder's View
            this.binding = binding;
            this.adapter = adapter;
            this.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("property", data.get(getAdapterPosition()));
                    NavHostFragment.findNavController(frag).navigate(R.id.propertyDetails,bundle);
                }
            });
            this.binding.propertyItemMenuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showMenu(view, R.menu.property_item_menu,context,data.get(getAdapterPosition()), frag,data);
                }
            });

        }



        private void showMenu( View v, @MenuRes int menu, Context context, property property,  androidx.fragment.app.Fragment frag, ArrayList<property> data) {

            PopupMenu popup = new PopupMenu(context,v);
            popup.getMenuInflater().inflate(menu, popup.getMenu());
            FirebaseFirestore db = FirebaseFirestore.getInstance();


            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("property", property);

                    switch (menuItem.getItemId()){
                        case R.id.update:
                            NavHostFragment.findNavController(frag).navigate(R.id.addApartment,bundle);
                            Log.d(TAG, "onMenuItemClick: Update");
                            break;


                        case R.id.delete:
                            Log.d(TAG, "onMenuItemClick: Delete");
                            binding.loadingProgress.setVisibility(View.VISIBLE);
                            String id = property.getId();

                                db.collection("property").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        binding.loadingProgress.setVisibility(View.GONE);
                                        data.remove(getAdapterPosition());
                                        adapter.notifyDataSetChanged();


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        binding.loadingProgress.setVisibility(View.GONE);
                                    }
                                });


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
    }


    public PropertyAdapter(ArrayList<property> dataSet, androidx.fragment.app.Fragment frag, Context context) {
        this.localDataSet = dataSet;
        this.frag = frag;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item


        return new ViewHolder(PropertyListItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false), frag, localDataSet, context, this);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.binding.date.setText(localDataSet.get(position).getDate());
        viewHolder.binding.reporterName.setText(localDataSet.get(position).getReporterName());
        viewHolder.binding.propertyType.setText(localDataSet.get(position).getPropertyType());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
