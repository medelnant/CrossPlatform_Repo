package com.michaeledelnant.crossplatform_android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.michaeledelnant.utilities.Validation;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;

import java.util.List;

public class NewDataDialogFragment extends DialogFragment {

    private static final String TAG = "CustomDialogFragment";
    protected View mInflatedView;
    protected ParseObject mDataObject;


    private Callbacks mContainmentActivity;



    public NewDataDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle b = getArguments();

        //Set title and message via dialogBuilder with passed arguments
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle(b.getString("title"));
        dialogBuilder.setMessage(b.getString("message"));

        //Inflate Layout into dialog for forgotPassword
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mInflatedView = inflater.inflate(R.layout.dialog_newdata, null);
        dialogBuilder.setView(mInflatedView);


        //If in Edit Mode query obect based on objectID passed in bundle
        if(b.getString("title").equals("Edit Data Item")) {
            // Assume ParseObject myPost was previously created.
            ParseQuery<ParseObject> dataItemQuery = ParseQuery.getQuery("DataItem");
            dataItemQuery.whereEqualTo("objectId", b.getString("objectID"));

            dataItemQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    if(e == null) {
                        for (ParseObject dataItem : parseObjects) {
                            Log.i(TAG, dataItem.getString("title") + " | " + dataItem.getNumber("quantity"));

                            //Set dataObject to be saved
                            mDataObject = dataItem;

                            EditText dataTitle = (EditText) mInflatedView.findViewById(R.id.dataTitle);
                            EditText dataQuantity = (EditText) mInflatedView.findViewById(R.id.dataQuantity);

                            dataTitle.setText(dataItem.getString("title"));
                            dataQuantity.setText(String.valueOf(dataItem.getNumber("quantity")));
                        }
                    } else {
                        Log.e(TAG, e.getMessage());
                    }
                }
            });
        }

        //Send Action
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
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

            Bundle b = getArguments();
            final String dialogTitle = b.getString("title");


            Button pButton = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);
            pButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EditText dataTitle = (EditText) mInflatedView.findViewById(R.id.dataTitle);
                    EditText dataQuantity = (EditText) mInflatedView.findViewById(R.id.dataQuantity);
                    int quantityInt= 0;

                    String titleString = dataTitle.getText().toString();
                    if(dataQuantity.getText().length() > 0) {
                      quantityInt = Integer.parseInt(dataQuantity.getText().toString());
                    }

                    if(!dialogTitle.equals("Edit Data Item")) {

                        //Set as new dataObject if creating
                        mDataObject= new ParseObject("DataItem");

                    }

                    if(titleString != null && quantityInt > 0) {

                        mDataObject.put("title", titleString);
                        mDataObject.put("quantity", quantityInt);
                        mDataObject.put("user", ParseUser.getCurrentUser());
                        mDataObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null) {
                                    Toast.makeText(getActivity(), "Data has been saved", Toast.LENGTH_LONG).show();
                                    mContainmentActivity.notifyNewDataItemSaved();
                                    getDialog().dismiss();
                                } else {
                                    Log.e(TAG, e.getMessage());
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "Please provide a title and quantity for your item.", Toast.LENGTH_LONG).show();
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
        public void notifyNewDataItemSaved();
    }

}
