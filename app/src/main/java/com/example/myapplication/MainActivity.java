package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText email, password;
    private Button signinbutton, signupbutton;
    private FirebaseAuth mAuth;
    private ArrayList<User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signinbutton = findViewById(R.id.signinbutton);
        signinbutton.setOnClickListener(this);
        signupbutton = findViewById(R.id.signupbutton);
        signupbutton.setOnClickListener(this);
        Intent intent=getIntent();
        users = (ArrayList<User>) intent.getSerializableExtra("users");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signinbutton) {
            String emailText = email.getText().toString().trim();
            String passwordText = password.getText().toString().trim();
            if (emailValidate(emailText) && passwordValidate(passwordText)) {
                SignInUser(emailText, passwordText);
            }
        } else if (view.getId() == R.id.signupbutton) {
            Intent intent = new Intent(MainActivity.this, SignUpPage.class);
            intent.putExtra("users",users);
            startActivity(intent);
        }
    }

    public void SignInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(MainActivity.this, HomePage.class);
                            intent.putExtra("users",users);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

        public boolean emailValidate(String email) {
        if (email.isEmpty()) {
            this.email.setError("empty mail is invalid");
            return false;
        } else if (1 < email.split("@", -1).length - 1) {
            this.email.setError("Your mail have to contain only one @");
            return false;
        } else if (!(email.contains("@"))) {
            this.email.setError("your mail must contain @");
            return false;
        } else if (1 < email.split(".com", -1).length - 1) {
            this.email.setError("Your mail have to contain only one .com");
            return false;
        } else if (!email.endsWith(".com")) {
            this.email.setError("Your mail must end with :   .com");
            return false;
        }
        return true;
    }

    public boolean passwordValidate(String password) {
        if (password.length() < 6) {
            this.password.setError("The password should be at least 6 letters");
            return false;
        }
        return true;
    }
}
