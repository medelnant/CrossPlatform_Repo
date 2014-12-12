package com.michaeledelnant.crossplatform_android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
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

import com.michaeledelnant.utilities.Validation;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class ForgotPasswordDialogFragment extends DialogFragment {

    private static final String TAG = "CustomDialogFragment";
    protected View mInflatedView;
    protected int mDialogType;
    protected Validation mValidationLib;

    private Callbacks mContainmentActivity;


    public ForgotPasswordDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mValidationLib = new Validation();

        Bundle b = getArguments();

        //Set title and message via dialogBuilder with passed arguments
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle(b.getString("title"));
        dialogBuilder.setMessage(b.getString("message"));

        //Inflate Layout into dialog for forgotPassword
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mInflatedView = inflater.inflate(R.layout.dialog_forgotpassword, null);
        dialogBuilder.setView(mInflatedView);

        //Send Action
        dialogBuilder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do nothing - refer to onStart method where we override default btn functionality
            }
        });

        //Cancel Action
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getDialog().dismiss();
            }
        });

        return dialogBuilder.create();

    }

    @Override
    public void onStart() {
        super.onStart();

        AlertDialog dialog = (AlertDialog)getDialog();
        if(dialog != null) {
            Button pButton = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);
            pButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText emailAddressField = (EditText)mInflatedView.findViewById(R.id.emailField);
                    String emailAddress = emailAddressField.getText().toString();

                    if(!emailAddress.equals("") && Validation.isValidEmail(emailAddress)) {

                        if(mValidationLib.isNetworkAvailable(getActivity())) {
                            ParseUser.requestPasswordResetInBackground(emailAddress, new RequestPasswordResetCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e == null) {
                                        dismiss();
                                        Toast.makeText(getActivity(), "An email has been sent to reset your password.", Toast.LENGTH_LONG).show();
                                    } else {
                                        Log.e(TAG, e.toString());
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "No Internet Connection Present. Please try again later.", Toast.LENGTH_LONG).show();
                            //dismiss();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Please enter a valid email address", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
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
        // TODO: Update argument type and name
    }

}
