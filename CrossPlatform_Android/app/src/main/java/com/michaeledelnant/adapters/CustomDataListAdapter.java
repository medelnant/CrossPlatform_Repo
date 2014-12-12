package com.michaeledelnant.adapters;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.michaeledelnant.crossplatform_android.DataListFragment;
import com.michaeledelnant.crossplatform_android.MainActivityContainer;
import com.michaeledelnant.crossplatform_android.R;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;

public class CustomDataListAdapter extends BaseAdapter {

    private static final String TAG = "CustomDataListAdapter";

    LayoutInflater inflater;
    ArrayList<ParseObject> mDataList;
    Context context;
    android.support.v4.app.FragmentManager mFragMgr;

    public CustomDataListAdapter(Context context, ArrayList<ParseObject> dataList, android.support.v4.app.FragmentManager fragmentManager) {

        super();

        this.mDataList = dataList;
        this.context = context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mFragMgr = fragmentManager;

    }

    @Override
    public int getCount() {
        return this.mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ParseObject dataItem = this.mDataList.get(position);

        View listRowView = convertView;
        if(convertView == null) {
            listRowView = inflater.inflate(R.layout.datalist_itemrow, null);
        }

        TextView dataItemTitle = (TextView) listRowView.findViewById(R.id.dataTitle);
        TextView dataItemQuantity = (TextView) listRowView.findViewById(R.id.dataQuantity);
/*        ImageButton dataDelete = (ImageButton) listRowView.findViewById(R.id.deleteItem);
        dataDelete.setColorFilter(Color.rgb(209, 35, 50));

        dataDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataItem.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e ==  null) {

                            //Call loaded dataListFragment and re-get data from parse
                            DataListFragment dataListFragment = (DataListFragment) mFragMgr.findFragmentByTag("dataListFragment");
                            if(dataListFragment != null) {
                                dataListFragment.getDataItems();
                            }
                        } else {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                });

            }
        });*/


        String dataTitleString = dataItem.getString("title");
        String dataQuantityString = String.valueOf(dataItem.getNumber("quantity"));

        if(dataItem != null) {
            dataItemTitle.setText(dataTitleString);
            dataItemQuantity.setText("Quantity: " + dataQuantityString);
        }

        return listRowView;
    }
}
