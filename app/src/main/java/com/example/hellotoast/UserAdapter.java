package com.example.hellotoast;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hellotoast.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private OnItemClickListener onItemClickListener;

    public UserAdapter(List<User> userList, OnItemClickListener onItemClickListener) {
        this.userList = userList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void updateData(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    public List<User> getUserList() {
        return userList;
    }



    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        User clickedUser = userList.get(position);
                        onItemClickListener.onItemClick(clickedUser);
                    }
                }
            });
        }


        @SuppressLint("ResourceAsColor")
        public void bind(User user) {
            nameTextView.setText(user.getName());

            // Change the background color here
            if (user.isAdmin()) {
                // Set a different color for admin users
               // nameTextView.setBackgroundColor(R.color.black);
            } else {
                // Set the default color for non-admin users
               //nameTextView.setBackgroundColor(R.color.black);
                //nameTextView.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.black));
            }
        }
    }
}
