package com.example.hiral.myapplication;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import android.widget.EditText;
/**
 * Created by Hiral on 7/28/2016.
 * Edited by Richa and Surendra on 11/24/2016.
 */
/**
 * A login screen that offers login via email/password.
 * Provides Functionality to Signup for new account
 */
public class LoginActivity extends AppCompatActivity {

    // UI references
    private EditText mEmail;
    private EditText mPasswordView;
    Button mSignInButton;
    Button mSignUpButton;
    LoginDataBaseAdapter loginDataBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Creates object for LoginDataBaseAdapter to gain access to database
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        mEmail = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);


        //OnClick Listener for SignInButton
        mSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                // Code below gets the User name and Password
                String userName = mEmail.getText().toString();
                String password = mPasswordView.getText().toString();

                /**TODO: Code here to fetch the Password from database for respective user name
                 */
                String database_password = loginDataBaseAdapter.getSingleEntry(userName);


                /**TODO: Code here to check if the password entered matches with the database entry
                 * TODO: if password matches navigate to LocationUpdate Activity using intent
                 * TODO: if password does not match show a toast "username or password does not match"
                 */
                //checking if the password entered matches with the database entry
                if(password.equals(database_password)){
                    Toast.makeText(getApplicationContext(), "Successfully Logged In ", Toast.LENGTH_LONG).show();
                    Intent i_Locationupdate = new android.content.Intent(getApplicationContext(), LocationUpdate.class);
                    i_Locationupdate.putExtra(LocationUpdate.EXTRA_USER_NAME,userName);
                    startActivity(i_Locationupdate);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Invalid password or username" + database_password, Toast.LENGTH_LONG).show();
                }

            }
        });


        /** TODO: Code here to create onclick listener for SignUp Button
         *  TODO: OnClick event for SignUp Button Navigates to SignUpActivity
         */

        mSignUpButton = (Button)findViewById(R.id.email_sign_up_button);
        mSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            //on clicking sign up button, the signup activity is called up
            public void onClick(android.view.View v) {
                Intent i_signup = new Intent(getApplicationContext(),SignUPActivity.class);
                startActivity(i_signup);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close The Database
        loginDataBaseAdapter.close();
    }
}