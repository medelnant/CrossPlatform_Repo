package com.michaeledelnant.crossplatform_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseUser;


public class RegPathContainer extends Activity
     implements LoginFragment.Callbacks,
                CreateAccountFragment.Callbacks,
                ForgotPasswordDialogFragment.Callbacks{

    private static final String TAG = "RegPathContainerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regpathcontainer);

        ParseUser user = ParseUser.getCurrentUser();
        //If user obj exists due to not logging out previously (user obj persists through crashes or killing the app)
        if(user != null) {
            //Direct them into the app
            Intent intent = new Intent(this, MainActivityContainer.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);

        } else {
            //Load the login fragment
            //Grab Login Fragment and add to regPathContainer
            LoginFragment loginFragment = new LoginFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.regPathContainer, loginFragment)
                    .commit();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void sendToCreateAccount() {

        CreateAccountFragment createAccountFragment = new CreateAccountFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.regPathContainer, createAccountFragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onCreateAccountSuccess() {

        Intent intent = new Intent(this, MainActivityContainer.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);

    }

    @Override
    public void onLoginSuccess() {

        Intent intent = new Intent(this, MainActivityContainer.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);

    }
}
