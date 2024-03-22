package com.example.myapplication;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private ArrayAdapter<User> arrayAdapter;
    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        mAuth = FirebaseAuth.getInstance();
        ListView listView = findViewById(R.id.listView);

        Intent intent = getIntent();
        users = (ArrayList<User>) intent.getSerializableExtra("users");
        if (users == null) {
            users = new ArrayList<>();
        }

        arrayAdapter = new UsersArrayAdapter(this, R.layout.listviewrow, users);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomePage.this, namesPage.class);
                intent.putExtra("users",users);
                startActivity(intent);
            }
        });


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.signoutfile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.signOut) {
            signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, LENGTH_SHORT).show();
        startActivity(new Intent(HomePage.this, namesPage.class));
    }

    private void signOut() {
        mAuth.signOut();
        Intent intent = new Intent(HomePage.this, MainActivity.class);
        intent.putExtra("users", users);
        startActivity(intent);
        finish();
    }
}
