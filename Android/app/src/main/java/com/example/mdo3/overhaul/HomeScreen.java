package com.example.mdo3.overhaul;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.Date;


/**
 * A login screen that offers login via email/password.
 */
public class HomeScreen extends AppCompatActivity
{
    private UserLoginTask mAuthTask = null;
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Switch loginSwitch;
    private Customer customerDets = null;
    private Driver driverDets = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        mEmailView = (EditText) findViewById(R.id.login_email);
        mPasswordView = (EditText) findViewById(R.id.login_password);
        loginSwitch = (Switch) findViewById(R.id.login_switch);

        mLoginFormView = findViewById(R.id.login_main_layout);
        mProgressView = findViewById(R.id.login_progress);

        loginSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    loginSwitch.setText(R.string.user_driver);
                else
                    loginSwitch.setText(R.string.user_customer);
            }
        });

        //Sign Up Button
        OnClickListener signUpListen = new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (loginSwitch.isChecked()) {
                    // If the switch is checked, that means the text is set to Driver. Go to the Driver main screen.
                    finish();
                    Intent myIntent = new Intent(HomeScreen.this, DriverRegistration.class);
                    HomeScreen.this.startActivity(myIntent);
                } else {
                    // Otherwise, that means the switch is still showing Customer, so go to the Customer main screen.
                    finish();
                    Intent myIntent = new Intent(HomeScreen.this, UserRegistration.class);
                    HomeScreen.this.startActivity(myIntent);
                }
            }
        };
        Button signUpBtn = (Button) findViewById(R.id.button_sign_up);
        signUpBtn.setOnClickListener(signUpListen);

        //Login Button
        OnClickListener loginListen = new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        };
        Button loginBtn = (Button) findViewById(R.id.button_log_in);
        loginBtn.setOnClickListener(loginListen);


        System.out.println("Register new account");
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin()
    {
        customerDets = null;
        driverDets = null;
        DataAccess DA = new DataAccess();

        if (mAuthTask != null)
        {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password))
        {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
            Toast.makeText(this, "Password Invalid, Please Try Again", Toast.LENGTH_SHORT).show();
        }

        System.out.println("Password valid");

        // Check for a valid email address.
        if (TextUtils.isEmpty(email))
        {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
            Toast.makeText(this, "Email Invalid, Please Try again", Toast.LENGTH_SHORT).show();
        } else if (!isEmailValid(email))
        {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
            Toast.makeText(this, "Email Invalid, Please Try again", Toast.LENGTH_SHORT).show();
        }

        if (cancel)
        {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else
        {
            System.out.println("Password valid");
            System.out.print("Logging now......");
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            if(loginSwitch.isChecked())
            {
                System.out.println("Logging in as Driver");
                driverDets = DA.checkDriverLogin(email, password);
                if (driverDets != null)
                {
                    Intent myIntent = new Intent(HomeScreen.this, DriverMainScreen.class);
                    myIntent.putExtra("Driver", driverDets);
                    HomeScreen.this.startActivity(myIntent);
                }
                else
                {
                    Toast.makeText(this, "Driver Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                System.out.println("Logging in as Customer");
                customerDets = DA.checkCustomerLogin(email, password);

                    if(customerDets != null)
                    {
                        Intent myIntent = new Intent(HomeScreen.this, ClientMainScreen.class);
                        myIntent.putExtra("Customer", customerDets);
                        HomeScreen.this.startActivity(myIntent);

                    }
                    else
                    {
                        Toast.makeText(this, "Customer Login Failed", Toast.LENGTH_SHORT).show();
                    }
            }

            showProgress(false);
        }
    }

    // Checks if the entered items are valid to even attempt to sign in
    private boolean isEmailValid(String email)
    {
        Boolean result = false;
        String[] end = {".com", ".edu"};

        for(String str : end)
            if(email.contains(str))
                result = true;

        if(email.contains("@"))
            result = true;

        return result;
    }

    private boolean isPasswordValid(String password)
    {
        Boolean result = false;
        Boolean hasCap = false;
        Boolean hasSpecChar = false;
        Boolean hasNumber = false;

        //check for length of password
        if(password.length() < 2 || password.length() > 15)
            result = false;
        else
            result = true;

        //checks for....
        for(int i = 0; i < password.length(); i++)
        {
            int x = (int) password.charAt(i);
            //has capital letter
            if ( x >= 65 && x <= 90)
                hasCap = true;

            //has special character
            if ((x >= 33 && x <= 47) || (x >= 58 && x <= 64) || (x >= 91 && x <= 96) || (x >= 123 && x <= 126))
                hasSpecChar = true;

            //has number
            if(x >= 48 && x <= 57)
                hasNumber = true;
        }

        // Makes sure the password is strong
        if (hasNumber && hasCap && hasSpecChar)
            result = true;

        return result;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else
        {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<String, Void, Boolean>
    {

        private final String mEmail;
        private final String mPassword;
        private final Boolean mFlag;

        UserLoginTask(String email, String password, Boolean flag)
        {
            mEmail = email;
            mPassword = password;
            mFlag = flag;
        }

        @Override
        protected Boolean doInBackground(String... params)
        {
            System.out.println("Creating Data Access Object");
            customerDets = null;
            driverDets = null;
            DataAccess DA = new DataAccess();
            try
            {
                //is checked = 1 : driver, else : customer
                if(mFlag)
                {
                    System.out.println("Logging in as Driver");
                    driverDets = DA.checkDriverLogin(mEmail, mPassword);
                }
                else
                {
                    System.out.println("Logging in as Customer");
                    customerDets = DA.checkCustomerLogin(mEmail, mPassword);
                }
                Thread.sleep(2000);

            } catch (InterruptedException e)
            {
                System.out.println("Login Failed");
                return false;
            }

            return (customerDets != null || driverDets != null) ? true : false;
        }

        @Override
        protected void onCancelled()
        {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

