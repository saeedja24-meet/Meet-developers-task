package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class namesPage extends AppCompatActivity implements View.OnClickListener {
    private Button button;
    private TextView textView;
    private ArrayList<User> users;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.namepage);
        Intent intent = getIntent();
        users = (ArrayList<User>) intent.getSerializableExtra("users");
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        button.setOnClickListener(this);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userEmail = mAuth.getCurrentUser().getEmail();
        textView.setText("Signed in as: " + userEmail);
        }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(namesPage.this, HomePage.class);
        intent.putExtra("users", users); // Pass the users ArrayList back to HomePage
        startActivity(intent);
    }
}
