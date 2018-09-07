package com.example.shivam97.buku;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static com.example.shivam97.buku.Buku.mAuth;
import static com.example.shivam97.buku.Buku.mDatabase;

public class SignUpActivity extends AppCompatActivity  {

    EditText edit_name,edit_email,edit_pass,edit_pass2,edit_age;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edit_age=findViewById(R.id.enter_age);
        edit_email=findViewById(R.id.enter_email);
        edit_pass=findViewById(R.id.enter_password);
        edit_pass2=findViewById(R.id.enter_password2);
        edit_name=findViewById(R.id.enter_name);
    }
    private boolean isEmailValid(String email) {
        return (email.contains("@") && email.contains("."));
    }
    private boolean isPassValid(String pass) {
        return (pass.length()>=8);
    }

    public void checkDetails(View view) {
        final int[] userIndex = new int[1];
        final String name,email,pass,pass2,age;
        boolean proceed=true;
        name=edit_name.getText().toString().trim();
        email=edit_email.getText().toString().trim();
        pass=edit_pass.getText().toString().trim();
        pass2=edit_pass2.getText().toString().trim();
        age=edit_age.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            edit_email.setError(getString(R.string.error_field_required));
            proceed=false;
        } else if (!isEmailValid(email)) {
            edit_email.setError(getString(R.string.error_invalid_email));
            proceed = false;
        }
        if(TextUtils.isEmpty(name)) {
            edit_name.setError(getString(R.string.error_field_required));
            proceed = false;
        }
        if(TextUtils.isEmpty(age)) {
            edit_age.setError(getString(R.string.error_field_required));
            proceed = false;
        }
        if(TextUtils.isEmpty(pass)) {
            edit_pass.setError(getString(R.string.error_field_required));
            proceed = false;
        }
        if(TextUtils.isEmpty(pass2)) {
            edit_pass2.setError(getString(R.string.error_field_required));
            proceed = false;
        }
        if(!isPassValid(pass)){
            edit_pass.setError("Password must be 8 character long");
            proceed=false;
        }
        if(!pass.equals(pass2))
        {
            edit_pass2.setError("password doesn't match");
            proceed=false;
        }
        if(proceed){

            progressBar=findViewById(R.id.login_progress);
            progressBar.setVisibility(View.VISIBLE);
            (findViewById(R.id.creating_tv)).setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        mDatabase.child("userIndex").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int i;
                                i=Integer.parseInt(dataSnapshot.getValue().toString())+1;
                                DatabaseReference newUser= mDatabase.child("users").child( String.valueOf(i) );
                                newUser.child("name").setValue(name);
                                newUser.child("age").setValue(age);
                                mDatabase.child("userIndex").setValue(String.valueOf(i));
                                userIndex[0] =i;
                                Toast.makeText(SignUpActivity.this,"Account successfully created",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                (findViewById(R.id.creating_tv)).setVisibility(View.GONE);
                                finish();
                                Intent i1=new Intent(SignUpActivity.this,AlreadyReaded.class);
                                i1.putExtra("userIndex", userIndex[0]);
                                startActivity(i1);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(SignUpActivity.this,"User creation failed",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                (findViewById(R.id.creating_tv)).setVisibility(View.GONE);

                            }
                        });

                    }
                    else {
                        String failed=task.getResult().toString();
                        Toast.makeText(SignUpActivity.this,"User creation failed\n"+failed,Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        (findViewById(R.id.creating_tv)).setVisibility(View.GONE);

                    }
                }
            });

        }
    }



}
