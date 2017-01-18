package com.example.bribri.myapplication.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bribri.myapplication.R;
import com.example.bribri.myapplication.models.User;
import com.example.bribri.myapplication.tasks.LoginAsyncTask;
import com.example.bribri.myapplication.tasks.SignUpAsyncTask;
import com.example.bribri.myapplication.tools.InternalSaver;
import com.example.bribri.myapplication.tools.InternalSearcher;

public class LoginActivity extends AppCompatActivity {
    Button bLogin,bCancel,bRegister,bSignIn;
    EditText eUserName,ePassword,eEepassword;
    User user;
    int counter = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupUI(findViewById(R.id.main_login_layout));
        initValues();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InternalSearcher.getUrl()==null){
                    Toast.makeText(getApplicationContext(),
                            "Invalid url", Toast.LENGTH_SHORT).show();
                    return;
                }
                user = new User(eUserName.getText().toString(),ePassword.getText().toString());
                LoginAsyncTask LoginThread = new LoginAsyncTask();
                LoginAsyncTask.LoginListener LoginListener = new LoginAsyncTask.LoginListener(){
                    @Override public void onLogin(String result){
                        try{
                            Long.parseLong(result);
                            Intent intent = new Intent(LoginActivity.this,ProfileActivity.class);
                            user.id = result;
                            InternalSaver.saveCredentials(user);
                            startActivity(intent);
                        }catch(Exception e){
                            Toast.makeText(getApplicationContext(), "Wrong Credentials : "+counter + " tries left",Toast.LENGTH_SHORT).show();
                            counter--;
                            if (counter == 0) {
                                bLogin.setEnabled(false);
                            }
                        }
                    }
                };
                LoginThread.setLoginListener(LoginListener);
                LoginThread.execute(user);
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eEepassword.setVisibility(View.VISIBLE);
                bLogin.setVisibility(View.GONE);
                bRegister.setVisibility(View.GONE);
                bSignIn.setVisibility(View.VISIBLE);
            }
        });

        bSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!eEepassword.getText().toString().equals(ePassword.getText().toString())) {
                    Toast.makeText(getApplicationContext(),
                            "Passwords don't match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(eUserName.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),
                            "Invalid UserName", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(InternalSearcher.getUrl()==null){
                    Toast.makeText(getApplicationContext(),
                            "Invalid url", Toast.LENGTH_SHORT).show();
                    return;
                }

                user = new User(eUserName.getText().toString(),ePassword.getText().toString());
                SignUpAsyncTask SignUpThread = new SignUpAsyncTask();
                SignUpAsyncTask.SignUpListener SignUpListener = new SignUpAsyncTask.SignUpListener(){
                    @Override public void onSignUp(Boolean result){
                        if(result){
                            eEepassword.setVisibility(View.GONE);
                            bLogin.setVisibility(View.VISIBLE);
                            bRegister.setVisibility(View.VISIBLE);
                            bSignIn.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "User created",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                SignUpThread.setSignUpListener(SignUpListener);
                SignUpThread.execute(user);
            }
        });



    }

    private void initValues(){
        bLogin = (Button)findViewById(R.id.button_login);
        bLogin.setVisibility(View.VISIBLE);
        bCancel = (Button)findViewById(R.id.button_cancel);
        bCancel.setVisibility(View.VISIBLE);
        bRegister = (Button)findViewById(R.id.button_Register);
        bRegister.setVisibility(View.VISIBLE);
        bSignIn = (Button)findViewById(R.id.button_SignIn);
        bSignIn.setVisibility(View.GONE);
        eUserName = (EditText)findViewById(R.id.editText_username);
        ePassword = (EditText)findViewById(R.id.editText_password);
        eEepassword = (EditText)findViewById(R.id.editText_repassword);
        eEepassword.setVisibility(View.GONE);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(LoginActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
