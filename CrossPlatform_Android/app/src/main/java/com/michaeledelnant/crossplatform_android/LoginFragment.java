package com.michaeledelnant.crossplatform_android;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    protected View mRootView;
    private Callbacks mContainmentActivity;

    //UI Elements
    protected EditText mUserNameField;
    protected EditText mPasswordField;
    protected TextView mForgotPasswordBtn;
    protected Button mLoginBtn;
    protected Button mCreateAccountBtn;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_login, container, false);

        //Fetch UI Elements and bind
        mUserNameField = (EditText) mRootView.findViewById(R.id.userNameField);
        mPasswordField = (EditText) mRootView.findViewById(R.id.passwordField);
        mForgotPasswordBtn = (TextView) mRootView.findViewById(R.id.btnForgotPassword);
        mLoginBtn = (Button) mRootView.findViewById(R.id.btnLogin);
        mCreateAccountBtn = (Button) mRootView.findViewById(R.id.btnCreateAccount);

        //ForgotPassword Action
        mForgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Instantiate customDialogFragment class
                ForgotPasswordDialogFragment dialogFragment = new ForgotPasswordDialogFragment();

                //Build bundle
                Bundle b = new Bundle();
                b.putString("title", "Forgot Password");
                b.putString("message", "Please provide your email address for us to send an email reset link.");
                //Pass unique type to handle desired actions for this unique instance
                b.putInt("dialogType", 0);

                //Attach bundle as arguments to fragment
                dialogFragment.setArguments(b);

                //Trigger display of dialogFragment
                dialogFragment.show(getFragmentManager(), "forgotPassword");

            }
        });

        //Login Action
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Fetch strings from form fields for login
                String userName = mUserNameField.getText().toString();
                String password = mPasswordField.getText().toString();

                //Create New empty parseUser object
                ParseUser user = new ParseUser();

                if(!userName.equals("") || !password.equals("")) {

                    //Login through parse passing username and password - expecting callback
                    user.logInInBackground(userName, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {

                            if(e == null) {
                                //On Success
                                mContainmentActivity.onLoginSuccess();
                            } else {
                                //Error
                                Log.e(TAG, e.toString());
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(getActivity(), "Please enter your username and password.", Toast.LENGTH_LONG).show();
                }

            }
        });

        //Create Account Action
        mCreateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Forward back to regPathContainer and load createAccountFragment
                mContainmentActivity.sendToCreateAccount();
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
        public void onLoginSuccess();
        public void sendToCreateAccount();

    }

}
