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
                if(!edtUserName.getText().toString().trim().equals("") && !edtPassword.getText().toString().trim().equals("")) requestAddUser();
                else Toast.makeText(AddUsersActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start CMSActivity
                Intent intent = new Intent(AddUsersActivity.this, ManageUsersActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }

    private void requestAddUser() {
        Call<addUser_Request> call = RetrofitClient.getUserApi().add_user(edtUserName.getText().toString(),edtPassword.getText().toString(),checkBoxAdmin.isChecked());

        call.enqueue(new Callback<addUser_Request>() {
            @Override
            public void onResponse(Call<addUser_Request> call, Response<addUser_Request> response) {
                if (response.isSuccessful()) {
                    addUser_Request result = response.body();
                    // Do something with the list of users...
                    if (result != null) {
                        getResponse(result);
                        Log.w("RequestAddUser", "Result received " + String.valueOf(result.isConfirm()));
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

    private void getResponse(addUser_Request result) {
        if (result.isConfirm()) Toast.makeText(this, "User with id = " + result.getId() + " was added", Toast.LENGTH_SHORT).show();
        else Toast.makeText(this, "User couldn't be added", Toast.LENGTH_SHORT).show();
    }

}
