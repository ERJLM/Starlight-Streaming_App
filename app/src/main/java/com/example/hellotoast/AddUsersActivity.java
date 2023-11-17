package com.example.hellotoast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUsersActivity extends AppCompatActivity {

    private EditText edtUserName, edtPassword;
    private CheckBox checkBoxAdmin;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_add_users);

        // Initialize views
        edtUserName = findViewById(R.id.idEdtUserName);
        edtPassword = findViewById(R.id.idEdtPassword);
        checkBoxAdmin = findViewById(R.id.checkbox_admin);
        user = (User)getIntent().getSerializableExtra("user");

        Button btnAdd = findViewById(R.id.button_add);
        Button back_button = findViewById(R.id.button_back);

        // Set OnClickListener for the "Add" button
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestAddUser();
            }
        });


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start CMSActivity
                Intent intent = new Intent(AddUsersActivity.this, CMSActivity.class);
                startActivity(intent);
            }
        });
    }

    private void requestAddUser() {
        Call<addUser_Request> call = RetrofitClient.getUserApi().add_user(edtUserName.toString(),edtPassword.toString(),checkBoxAdmin.isChecked());
        call.enqueue(new Callback<addUser_Request>() {
            @Override
            public void onResponse(Call<addUser_Request> call, Response<addUser_Request> response) {
                if (response.isSuccessful()) {
                    addUser_Request result = response.body();
                    // Do something with the list of users...
                    if (result != null) {
                        //Add user to app
                    }
                } else {
                    Log.w("RequestAddUser", "Response is null");
                }
            }

            @Override
            public void onFailure(Call<addUser_Request> call, Throwable t) {
                t.toString();
                Log.e("RequestAdUser", t.toString());
            }
        });
    }

    private void addUserToDatabase() {
        // Get user input
        String userName = edtUserName.getText().toString();
        String password = edtPassword.getText().toString();
        boolean isAdmin = checkBoxAdmin.isChecked();

        //Adicionar o user


        Toast.makeText(this, "User added to the database", Toast.LENGTH_SHORT).show();
    }

}
