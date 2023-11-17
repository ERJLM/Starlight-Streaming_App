package com.example.hellotoast;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    // Defining variables for edit texts and buttons
    private EditText userNameEdt;
    private EditText passwordEdt;
    private Button loginBtn;

    private AndroidWebServer server;

    private Login_Request userLogin;
    private User user;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSION_STORAGE = {
            android.Manifest.permission.READ_MEDIA_VIDEO,
            android.Manifest.permission.READ_MEDIA_IMAGES,
            android.Manifest.permission.READ_MEDIA_AUDIO,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE,

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        startWebServer();
        verifyStoragePermission(this);

        // Initialize edit texts and buttons
        userNameEdt = findViewById(R.id.idEdtUserName);
        passwordEdt = findViewById(R.id.idEdtPassword);
        loginBtn = findViewById(R.id.idBtnLogin);

        // Set click listener for login button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
            @Override
            public void onClick(View v) {
                // Get user inputs from edit texts
                String userName = userNameEdt.getText().toString();
                String password = passwordEdt.getText().toString();

                requestLogin(userName, password);

                // Validate user inputs
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution if inputs are empty
                }

                // Simulate login process (without using Parse SDK)
                if (userName.equals("admin") && password.equals("admin")) {
                    // Admin Login successful, switch to MainActivity
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, CMSActivity.class);
                    intent.putExtra("username", userName);
                    intent.putExtra("user", user);
                    startActivity(intent);
                } else if (userName.equals("user") && password.equals("user")) {
                    // UserLogin successful, switch to MainActivity
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, MovieSelectorActivityUser.class);
                    intent.putExtra("username", userName);
                    startActivity(intent);
                } else {
                    // Login failed, display error message
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestLogin(String userName, String password) {
        Call<Login_Request> call = RetrofitClient.getUserApi().login(userName, password);
        call.enqueue(new Callback<Login_Request>() {
            @Override
            public void onResponse(Call<Login_Request> call, Response<Login_Request> response) {
                if (response.isSuccessful()) {
                    Login_Request login = response.body();
                    // Do something with the list of users...
                    if (login != null) {
                        // Update the adapter with the new list of users
                        userLogin = login;
                    }
                } else {
                    userLogin = null;
                }
            }

            @Override
            public void onFailure(Call<Login_Request> call, Throwable t) {
                t.toString();
                Log.e("RequestLogin", t.toString());
            }
        });
    }

    private void startWebServer() {
        server = new AndroidWebServer(8080);
        try {
            server.start();
            Log.w("Httpd", "Web server initialized*.");

        } catch (IOException ioe) {
            Log.w("Httpd", "The server could not start.*");
        }
    }

    private void verifyStoragePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSION_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        } else {

            //TODO
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //todo
            } else {
                // Handle the case where permission is denied
            }
        }
    }
}

