package com.michaeledelnant.crossplatform_android;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.michaeledelnant.adapters.CustomDataListAdapter;
import com.michaeledelnant.crossplatform_android.dummy.DummyContent;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataListFragment extends ListFragment {

    private static final String TAG = "DataListFragment";
    private Callbacks mContainmentActivity;

    protected View mRootView;
    protected ListView mListView;
    protected ArrayList<ParseObject> mDataSource;
    protected CustomDataListAdapter mListAdapter;

    public DataListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Specify that this fragment has its own actionbar
        setHasOptionsMenu(true);
        setMenuVisibility(true);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_datalist, container, false);
        return mRootView;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getDataItems();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.datalist, menu);
        Log.i("DetailFragment", "onCreateOptionsMenu has been called from within fragment");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.addItem) {
            Log.i(TAG, "addItemClicked");
            Toast.makeText(getActivity(), "add item clicked in action bar", Toast.LENGTH_LONG).show();

            //Instantiate customDialogFragment class
            NewDataDialogFragment dialogFragment = new NewDataDialogFragment();

            //Build bundle
            Bundle b = new Bundle();
            b.putString("title", "Add New Data Item");
            //b.putString("message", "Please provide your email address for us to send an email reset link.");
            //Pass unique type to handle desired actions for this unique instance


            //Attach bundle as arguments to fragment
            dialogFragment.setArguments(b);

            //Trigger display of dialogFragment
            dialogFragment.show(getActivity().getFragmentManager(), "newDataItem");

            //return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void getDataItems() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("DataItem");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e==null) {

                    //Define ArrayList struct
                    mDataSource = new ArrayList<ParseObject>();

                    //Loop through all results
                    for(ParseObject dataItem : parseObjects) {
                        mDataSource.add(dataItem);
                    }

                    bindListViewData(mDataSource);

                } else {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    public void refreshDataItems() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("DataItem");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e==null) {

                    //Define ArrayList struct
                    mDataSource = new ArrayList<ParseObject>();

                    //Loop through all results
                    for(ParseObject dataItem : parseObjects) {
                        mDataSource.add(dataItem);
                    }

                    mListAdapter.notifyDataSetChanged();

                } else {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    public void bindListViewData(ArrayList<ParseObject> dataSource) {
        if(dataSource != null) {
            mListAdapter = new CustomDataListAdapter(getActivity(), dataSource, getFragmentManager());
            setListAdapter(mListAdapter);
            getListView().setDivider(null);
            getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ParseObject dataItemObject = mDataSource.get(position);
                    Log.i(TAG, "Clicked " + dataItemObject.getString("title") + " | " + dataItemObject.getNumber("quantity"));
                }
            });
        }
    }

    public interface Callbacks {
        // TODO: Update argument type and name

    }

}
