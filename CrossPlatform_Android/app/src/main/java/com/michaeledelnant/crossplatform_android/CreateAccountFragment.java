package com.michaeledelnant.crossplatform_android;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.michaeledelnant.connection.CheckDataConnection;
import com.michaeledelnant.utilities.Validation;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class CreateAccountFragment extends Fragment {

    private static final String TAG = "CreateAccountFragment";
    protected View mRootView;
    private Callbacks mContainmentActivity;

    //UI Elements
    protected EditText mUsernameField;
    protected EditText mEmailField;
    protected EditText mFirstnameField;
    protected EditText mLastnameField;
    protected EditText mPasswordField;
    protected Button mCreateAccountBtn;
    protected Validation mValidationLib;


    public CreateAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mValidationLib = new Validation();

        // Inflate the layout for this fragment
        mRootView =inflater.inflate(R.layout.fragment_create_account, container, false);

        //Fetch UI Elements
        mUsernameField = (EditText)mRootView.findViewById(R.id.userNameField);
        mEmailField = (EditText)mRootView.findViewById(R.id.emailField);
        mFirstnameField = (EditText)mRootView.findViewById(R.id.firstNameField);
        mLastnameField = (EditText)mRootView.findViewById(R.id.lastNameField);
        mPasswordField = (EditText)mRootView.findViewById(R.id.passwordField);
        mCreateAccountBtn = (Button)mRootView.findViewById(R.id.btnCreateAccount);


        //Create Account Action
        mCreateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Fetch text from form fields
                String userName = mUsernameField.getText().toString();
                String emailAddress = mEmailField.getText().toString();
                String firstName = mFirstnameField.getText().toString();
                String lastName = mLastnameField.getText().toString();
                String password = mPasswordField.getText().toString();

                //Create new empty parseUser object
                ParseUser newUser = new ParseUser();

                //Check all field string to make sure data was entered into each
                if( !userName.equals("") &&
                    !emailAddress.equals("") &&
                    !firstName.equals("") &&
                    !lastName.equals("") &&
                    !password.equals("")) {

                    //Check email address to verify it is correctly formatted
                    if(Validation.isValidEmail(emailAddress)) {
                        newUser.setUsername(userName);
                        newUser.setPassword(password);
                        newUser.put("firstname", firstName);
                        newUser.put("lastname", lastName);
                        newUser.put("email", emailAddress);

                        if(mValidationLib.isNetworkAvailable(getActivity())) {

                            //Send newUser to parse for account creation
                            newUser.signUpInBackground(new SignUpCallback() {
                                @Override
                                public void done(ParseException e) {
                                    //Handle Response from callback
                                    if(e == null) {
                                        //Success
                                        Toast.makeText(getActivity(), "Your Account has been created", Toast.LENGTH_LONG).show();
                                        mContainmentActivity.onCreateAccountSuccess();
                                    } else {
                                        //Error
                                        Log.e(TAG, e.toString());
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(getActivity(), "No Internet Connection Present. Please try again later.", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        //Alert user to enter properly formatted email
                        Toast.makeText(getActivity(), "Please enter a valid email address", Toast.LENGTH_LONG).show();
                    }

                } else {
                    //Alert user to fill out all fields from the form
                    Toast.makeText(getActivity(), "Please fill out all fields of this form", Toast.LENGTH_LONG).show();
                }
            }
        });

        return mRootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        //Attach interface to this fragment
        mContainmentActivity = (Callbacks) activity;

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public interface Callbacks {
        public void onCreateAccountSuccess();
    }

}
