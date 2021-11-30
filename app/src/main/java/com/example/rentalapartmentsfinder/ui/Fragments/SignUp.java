package com.example.rentalapartmentsfinder.ui.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rentalapartmentsfinder.R;
import com.example.rentalapartmentsfinder.databinding.FragmentSignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;


public class SignUp extends Fragment {

    private FragmentSignUpBinding binding;
    FirebaseAuth mAuth;
    public SignUp() {
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
        binding = FragmentSignUpBinding.inflate(getLayoutInflater());
        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Signup();
            }
        });
        return binding.getRoot();
    }

    private void Signup (){
        binding.emailField.setError(null);
        binding.passwordField.setError(null);
        String EmailLogin = binding.emailField.getEditText().getText().toString().trim();
        String PasswordLogin = binding.passwordField.getEditText().getText().toString().trim();
        boolean isError = false;

        if(EmailLogin.isEmpty()){
            isError = true;
            binding.emailField.setError("Email is Empty");
            binding.emailField.requestFocus();

        }
        if(PasswordLogin.isEmpty()){
            isError = true;
            binding.passwordField.setError("Enter password");
            binding.passwordField.requestFocus();

        }
        if(!Patterns.EMAIL_ADDRESS.matcher(EmailLogin).matches()){
            isError = true;
            binding.emailField.setError("Enter valid email Address");
            binding.emailField.requestFocus();

        }

        if(PasswordLogin.length()<5)
        {
            isError = true;
            binding.passwordField.setError("Length of password should be more than 5 characters");
            binding.passwordField.requestFocus();

        }

        if(!isError){
            binding.loadingProgress.setVisibility(View.VISIBLE);
            binding.loadingText.setText("Signing in...");
            binding.loadingText.setVisibility(View.VISIBLE);
            binding.title.setVisibility(View.GONE);
            binding.register.setVisibility(View.GONE);
            binding.linearLayout.setVisibility(View.GONE);
            mAuth.createUserWithEmailAndPassword(EmailLogin,PasswordLogin).addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    NavHostFragment.findNavController(getParentFragment()).navigate(R.id.home2);
                }else{
                    binding.loadingProgress.setVisibility(View.GONE);
                    binding.loadingText.setText("Signing in...");
                    binding.loadingText.setVisibility(View.GONE);
                    binding.title.setVisibility(View.VISIBLE);
                    binding.register.setVisibility(View.VISIBLE);
                    binding.linearLayout.setVisibility(View.VISIBLE);
                    if(task.getException() instanceof FirebaseAuthException){
                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                        switch (errorCode) {

                            case "ERROR_INVALID_CUSTOM_TOKEN":
                                Toast.makeText(getContext(), "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                                Toast.makeText(getContext(), "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_INVALID_CREDENTIAL":
                                Toast.makeText(getContext(), "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_INVALID_EMAIL":
                                Toast.makeText(getContext(), "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                                binding.emailField.setError("The email address is badly formatted.");
                                binding.emailField.requestFocus();



                                break;

                            case "ERROR_WRONG_PASSWORD":
                                Toast.makeText(getContext(), "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                                binding.passwordField.setError("password is incorrect ");
                                binding.passwordField.requestFocus();
                                binding.passwordField.getEditText().setText("");
                                break;

                            case "ERROR_USER_MISMATCH":
                                Toast.makeText(getContext(), "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_REQUIRES_RECENT_LOGIN":
                                Toast.makeText(getContext(), "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                Toast.makeText(getContext(), "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_EMAIL_ALREADY_IN_USE":
                                Toast.makeText(getContext(), "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                                binding.emailField.setError("The email address is already in use by another account.");
                                binding.emailField.requestFocus();
                                break;

                            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                                Toast.makeText(getContext(), "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_USER_DISABLED":
                                Toast.makeText(getContext(), "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_USER_TOKEN_EXPIRED":
                                Toast.makeText(getContext(), "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_USER_NOT_FOUND":
                                Toast.makeText(getContext(), "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_INVALID_USER_TOKEN":
                                Toast.makeText(getContext(), "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_OPERATION_NOT_ALLOWED":
                                Toast.makeText(getContext(), "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_WEAK_PASSWORD":
                                Toast.makeText(getContext(), "The given password is invalid.", Toast.LENGTH_LONG).show();
                                binding.passwordField.setError("The password is invalid it must 6 characters at least");
                                binding.passwordField.requestFocus();
                                break;

                        }
                    }else{
                        Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                }




            });
        }

    }
}