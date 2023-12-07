package com.example.hellotoast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageUsersActivity extends AppCompatActivity {

    private EditText edtUserName, edtPassword;
    private CheckBox checkBoxAdmin;
    private RecyclerView recyclerView;
    private List<User> userList;
    private UserAdapter userAdapter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        int id = 0;

        Button btnAdd = findViewById(R.id.button_add);
        Button back_button = findViewById(R.id.button_back);
        user = (User)getIntent().getSerializableExtra("user");


        userList = new ArrayList<>();

        userAdapter = new UserAdapter(userList, new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(User userInfo) {
                // Show user details
                Intent intent = new Intent(ManageUsersActivity.this, UserDetailActivity.class);
                intent.putExtra("userInfo", userInfo);
                intent.putExtra("user", user);
                Toast.makeText(ManageUsersActivity.this, "Clicked: " + userInfo.getName(), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        //Request Users
        requestUsers();
        userList = userAdapter.getUserList();

        // Set OnClickListener for the "Add" button
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserToDatabase();
            }
        });


        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setAdapter(userAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(userAdapter);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start CMSActivity
                Intent intent = new Intent(ManageUsersActivity.this, CMSActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }

    private void requestUsers() {
        Call<List<User>> call = RetrofitClient.getUserApi().getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> users = response.body();
                    // Do something with the list of users...
                    if (users != null) {
                        // Update the adapter with the new list of users
                        userAdapter.updateData(users);
                    }
                } else {
                       userAdapter.updateData(null);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                // Handle failure...
            }
        });
    }

    private void addUserToDatabase() {
        //Go to the AddUsersActivity
        Intent intent = new Intent(ManageUsersActivity.this, AddUsersActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        //Toast.makeText(this, "User added to the database", Toast.LENGTH_SHORT).show();
    }

    private void removeUserFromDatabase() {
        // Get user input
        String userName = edtUserName.getText().toString();

        //Remover o user

        Toast.makeText(this, "User removed from the database", Toast.LENGTH_SHORT).show();
    }
}


