// SignUpPage.java
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SignUpPage extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText name, email, password;
    private Button signupbutton;
    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        users = (ArrayList<User>) intent.getSerializableExtra("users");
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signupbutton = findViewById(R.id.signupbutton);
        signupbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signupbutton) {
            String nameText = name.getText().toString().trim();
            String emailText = email.getText().toString().trim();
            String passwordText = password.getText().toString().trim();

            if (emailValidate(emailText) && nameValidate(nameText) && passwordValidate(passwordText)){
                SignUpUser(emailText, passwordText, nameText);
            }
        }
    }

    public void SignUpUser(String email, String password, String name) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                            userRef.child("name").setValue(name);
                            userRef.child("email").setValue(email);

                            // Create a new user object and add it to the users list
                            User user = new User(name, email);
                            if (users == null) {
                                users = new ArrayList<>();
                            }
                            users.add(user);

                            Toast.makeText(SignUpPage.this, "Account created successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SignUpPage.this, HomePage.class);
                            intent.putExtra("users", users);
                            startActivity(intent);
                            finish();
                        } else if(task.getException() instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(SignUpPage.this, "There is an existing account with the same email, try signing in", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUpPage.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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

    public boolean nameValidate(String name) {
        if (name.length() < 1) {
            this.name.setError("Your name shouldn't be empty");
            return false;
        }
        return true;
    }
}
