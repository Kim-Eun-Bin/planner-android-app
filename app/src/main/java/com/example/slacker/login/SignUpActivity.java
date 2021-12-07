package com.example.slacker.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.slacker.R;
import com.example.slacker.utils.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
    private Button login_btn;
    private Button signup_btn;
    private FirebaseAuth mAuth;
    private String name;
    private String email;
    private String password;
    private String password_check;
    private EditText name_et;
    private EditText email_et;
    private EditText password_et;
    private EditText password_check_et;
    private TextView name_tv;
    private TextView email_tv;
    private TextView password_tv;
    private TextView password_check_tv;
    private DatabaseReference mDatabase;
    private int userNum;
    private static String TAG = "FirebaseAuth CreateUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        login_btn = (Button) findViewById(R.id.goto_login_btn);
        signup_btn = (Button) findViewById(R.id.signup_btn);
        name_et = (EditText) findViewById(R.id.name_sinup_et);
        email_et = (EditText) findViewById(R.id.email_signup_et);
        password_et = (EditText) findViewById(R.id.password_signup_et);
        password_check_et = (EditText) findViewById(R.id.passwordcheck_signup_et);
        name_tv = (TextView) findViewById(R.id.name_tv);
        email_tv = (TextView) findViewById(R.id.email_tv);
        password_tv = (TextView) findViewById(R.id.password_tv);
        password_check_tv = (TextView) findViewById(R.id.passwordcheck_tv);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, UserLoginActivity.class);
                startActivity(intent);
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = name_et.getText().toString();
                email = email_et.getText().toString();
                password = password_et.getText().toString();
                password_check = password_check_et.getText().toString();
                createUser(email, password);
            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                    for (DataSnapshot dataSnapshot : snapshot.child("user").getChildren()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            userNum = user.getId();
                        } else {
                            userNum = 1;
                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void createUser(String email, String password) {
        boolean flag = false;

        if(name.equals("")) {
            name_tv.setText("이름을 입력하세요");
            flag = true;
        } else {
            name_tv.setText("");
        }
        if(email.equals("")) {
            email_tv.setText("이메일을 입력하세요");
            flag = true;
        } else {
            name_tv.setText("");
        }
        if(password.equals("")) {
            password_tv.setText("비밀번호를 입력하세요");
            flag = true;
        } else {
            name_tv.setText("");
        }
        if(password_check.equals("")) {
            password_check_tv.setText("비밀번호 확인을 입력하세요");
            flag = true;
        } else {
            name_tv.setText("");
        }

        if(flag) {
            return;
        }

        if(password.length() >= 6) {
            if (password.equals(password_check)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if(user != null) {
                                        User new_user = new User(userNum + 1, email, name);
                                        mDatabase.child("user").child(user.getUid()).setValue(new_user);
                                    }
                                    Toast.makeText(SignUpActivity.this, "회원가입 되었습니다.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignUpActivity.this, UserLoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                password_check_tv.setText("비밀번호를 확인해주세요.");
            }
        } else {
            Toast.makeText(SignUpActivity.this, "비밀번호는 6자리 이상입니다.", Toast.LENGTH_LONG).show();
        }
    }
}
