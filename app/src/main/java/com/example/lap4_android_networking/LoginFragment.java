package com.example.lap4_android_networking;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.lap4_android_networking.RegisterFragment;
import com.google.android.material.snackbar.Snackbar;

public class LoginFragment extends Fragment {

    private AppCompatButton btn_login;
    private EditText edt_email, edt_password;
    private TextView tv_register;
    private ProgressBar progressBar;
    private SharedPreferences pref;

    public LoginFragment() {

    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container,
                false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        pref = getActivity().getPreferences(0);
        btn_login = (AppCompatButton) view.findViewById(R.id.btn_login);
        edt_email = (EditText) view.findViewById(R.id.et_email);
        edt_password = (EditText) view.findViewById(R.id.et_password);
        tv_register = (TextView) view.findViewById(R.id.tv_register);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(getView(), "Hello", Snackbar.LENGTH_LONG).show();
                goToRegister();

            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edt_email.getText().toString();
                String password = edt_password.getText().toString();
                if (!email.isEmpty() && !password.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    loginProcess(email, password);
                } else {
                    Snackbar.make(getView(), "Fields are empty !", Snackbar.LENGTH_LONG).show();
                }

            }
        });
    }

    private void loginProcess(String email, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.104:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        User user = new User();
        user.setEmail(email);
        user.setPassWord(password);
        Call<User> response = requestInterface.login(user);
        response.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {

                User userRespnse = response.body();
                Log.d("idUserLogin", "onResponse: " + userRespnse._id);
                Snackbar.make(getView(), "Đăng nhập thành công", Snackbar.LENGTH_LONG).show();
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean(Constants.IS_LOGGED_IN, true);
                editor.putString(Constants.EMAIL, userRespnse.getEmail());
                editor.putString(Constants.NAME, userRespnse.getUserName());
                editor.putString(Constants.UNIQUE_ID, userRespnse.getId());
                editor.apply();
                goToProfile();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(Constants.TAG, "failed" + t);
                Snackbar.make(getView(), t.getLocalizedMessage(),
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void goToRegister() {
        RegisterFragment registerFragment = new RegisterFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame, registerFragment);
        fragmentTransaction.addToBackStack("null");
        fragmentTransaction.commit();
    }

    private void goToProfile() {
        ProfileFragment profileFragment = new ProfileFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame, profileFragment);
        fragmentTransaction.addToBackStack("null");
        fragmentTransaction.commit();

    }
}