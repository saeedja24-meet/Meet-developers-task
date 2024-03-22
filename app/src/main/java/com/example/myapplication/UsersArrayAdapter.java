package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class UsersArrayAdapter extends ArrayAdapter<User> {
    private Context context;
    private int resource;

    public UsersArrayAdapter(@NonNull Context context, int resource, ArrayList<User> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(this.resource, parent, false);
        }

        User user = getItem(position);
        TextView nameTextView = convertView.findViewById(R.id.rowName);
        TextView emailTextView = convertView.findViewById(R.id.rowEmail);

        // Set the name and email in the TextViews
        if (user != null) {
            nameTextView.setText(user.getName());
            emailTextView.setText(user.getEmail());
        }

        return convertView;
    }
}
