package com.example.lap4_android_networking;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProfileFragment extends Fragment {
    private TextView tv_name, tv_email, tv_message;
    private SharedPreferences pref;
    private AppCompatButton btn_change_password, btn_logout;
    private EditText edt_old_password, edt_new_password;
    private AlertDialog dialog;
    private ProgressBar progressBar;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pref = getActivity().getPreferences(0);
        tv_name.setText("Welcome : " + pref.getString(Constants.NAME, ""));
        tv_email.setText(pref.getString(Constants.EMAIL, ""));
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(getContext(),Gravity.CENTER);
            }
        });
    }

    private void initViews(View view) {
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_email = (TextView) view.findViewById(R.id.tv_email);
        btn_change_password = (AppCompatButton) view.findViewById(R.id.btn_chg_password);
        btn_logout = (AppCompatButton) view.findViewById(R.id.btn_logout);
    }

    private void showDialog(Context context,int gravity) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_password);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if (Gravity.BOTTOM == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        edt_new_password = dialog.findViewById(R.id.ed_change_new_password);
        edt_old_password = dialog.findViewById(R.id.ed_old_password);
        Button btnClose = dialog.findViewById(R.id.btnClose);
        Button btnSucces = dialog.findViewById(R.id.btnSuccess);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnSucces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = edt_new_password.getText().toString();
                String oldPassword = edt_old_password.getText().toString();
                changePassword(newPassword,oldPassword,dialog);

            }
        });
        dialog.show();
    }
        private void changePassword(String newPass,String oldPasss,Dialog dialog){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.0.104:3000/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RequestInterface requestInterface = retrofit.create(RequestInterface.class);
            User user = new User();
            user.setEmail((pref.getString(Constants.EMAIL, "")));
            user.setPassWord(newPass);
            user.setOldPassword(oldPasss);
            Call<User> response = requestInterface.changePassword(pref.getString(Constants.UNIQUE_ID, "") ,user);
            response.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Snackbar.make(getView(), "Đổi thành công", Snackbar.LENGTH_LONG).show();
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });


        }
    private void logout() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(Constants.IS_LOGGED_IN, false);
        editor.putString(Constants.NAME, "");
        editor.putString(Constants.EMAIL, "");
        editor.putString(Constants.UNIQUE_ID, "");
        editor.apply();
        goToLogin();
    }

    private void goToLogin() {
        LoginFragment loginFragment = new LoginFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame, loginFragment);
        fragmentTransaction.addToBackStack("null");
        fragmentTransaction.commit();
        ;

    }
}