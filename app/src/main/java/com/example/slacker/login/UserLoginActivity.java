package com.example.slacker.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.slacker.MainActivity;
import com.example.slacker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserLoginActivity extends AppCompatActivity {
    private Button signup_btn;
    private Button login_btn;
    private String email;
    private String password;
    private EditText email_et;
    private EditText password_et;
    private FirebaseAuth mAuth;

    private static String TAG = "FirebaseAuth CreateUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        mAuth = FirebaseAuth.getInstance();

        signup_btn = (Button) findViewById(R.id.goto_signup_btn);
        login_btn = (Button) findViewById(R.id.login_btn);
        email_et = (EditText) findViewById(R.id.email_login_et);
        password_et = (EditText) findViewById(R.id.password_login_et);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = email_et.getText().toString();
                password = password_et.getText().toString();

                login(email, password);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //TODO
        }
    }

    public void login(String email, String password) {
        if(!email.equals("")) {
            if(!password.equals("")) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(UserLoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(UserLoginActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(UserLoginActivity.this, "이메일을 입력하세요.", Toast.LENGTH_LONG).show();
        }
    }
}
