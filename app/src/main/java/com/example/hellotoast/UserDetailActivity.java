package com.example.hellotoast;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailActivity extends AppCompatActivity {

    private User user, userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        // Get the User object from the Intent
        user = (User) getIntent().getSerializableExtra("user");
        userInfo = (User) getIntent().getSerializableExtra("userInfo");

        // Find TextViews and Button by their IDs
        TextView textViewTitle = findViewById(R.id.textUserName);
        TextView textViewId = findViewById(R.id.textViewId);
        TextView textViewName = findViewById(R.id.textViewName);
        TextView textViewAdmin = findViewById(R.id.textViewAdmin);
        TextView textViewSeeder = findViewById(R.id.textViewSeeder);
        Button btnDeleteUser = findViewById(R.id.idBtnDeleteUser);
        Button button_back = findViewById(R.id.button_back);

        // Set values based on the User object
        if (userInfo != null) {
            textViewTitle.setText(userInfo.getName());
            textViewId.setText(String.format("Id: %s", userInfo.getId()));
            textViewName.setText(String.format("Name: %s", userInfo.getName()));
            textViewAdmin.setText(String.format("Admin: %s", userInfo.isAdmin() ? "Yes" : "No"));
            int i =  userInfo.getSeeder();
            if (i > 0) textViewSeeder.setText(String.format("Seeding movie with id %s", i));
            else textViewSeeder.setText(String.format("%s is not seeding a movie", userInfo.getName()));
        }

        //Delete User
        btnDeleteUser.setOnClickListener(view -> {
            // Create a confirmation dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to delete this user?");

            // Add buttons for confirmation
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                     deleteUserRequest();

                    button_back.performClick();
                }
            });

            // Add button for cancellation
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // User clicked No, do nothing or handle accordingly
                }
            });

            // Show the dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        button_back.setOnClickListener(view -> {
            Intent intent = new Intent(UserDetailActivity.this, UsersManageActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        });
    }

    private void deleteUserRequest() {
            Call<Boolean> call = RetrofitClient.getUserApi().del_user(userInfo.getId());
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful()) {
                        Boolean result = response.body();
                        // Do something with the list of users...
                        if (result == true) {
                            getResponse(true);
                            //delete user from the app
                        }
                    } else {
                        getResponse(false);
                        Log.w("RequestDeleteUser", "Response is false");
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    t.toString();
                    Log.e("RequestDeleteUser", t.toString());
                }
            });

    }

    private void getResponse(boolean check) {
        if(check) Toast.makeText(this, "User with id = " + userInfo.getId() + " was removed", Toast.LENGTH_SHORT).show();
        else Toast.makeText(this, "User with id = " + userInfo.getId() + " couldn't be removed", Toast.LENGTH_SHORT).show();
    }
}
