package com.example.ddavi.prueba;

/**
 * Created by Hernan Cortes on 04/06/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import org.puredata.core.PdBase;

import java.util.ArrayList;

public class GridViewCustomAdapter extends BaseAdapter {

    ArrayList<String> items;
    ArrayList<Integer> itemsPressed;

    static Activity mActivity;

    private static LayoutInflater inflater = null;

    public GridViewCustomAdapter(Activity activity, ArrayList<String> tempTitle) {
        mActivity = activity;
        items = tempTitle;
        itemsPressed = new ArrayList<>();

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public final int getCount() {

        return items.size();

    }

    @Override
    public final Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public final long getItemId(int position) {

        return position;
    }

    /*
    ASI ESTABA FUNCIONANDO ORIGINALMENTE
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = null;

            v = inflater.inflate(R.layout.item, null);

            Button tv = (Button) v.findViewById(R.id.button);
            tv.setText(items.get(position));

            return v;
        }
    */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = null;

        v = inflater.inflate(R.layout.item, null);

        final Button tv = (Button) v.findViewById(R.id.button);
        tv.setText(items.get(position));

        if (itemsPressed.contains(position)){
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.parseColor("#FF9800"));
        }else {
            tv.setTextColor(Color.BLACK);
            tv.setBackgroundColor(Color.parseColor("#607D8B"));
        }

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = "connect-" + tv.getText();
                if (tv.getCurrentTextColor() == Color.BLACK) {
                    Float value = 1.0f;
                    PdBase.sendFloat(msg, value);
                    tv.setBackgroundColor(Color.parseColor("#FF9800"));
                    Log.i("MSJ A PD ", msg);
                    tv.setTextColor(Color.WHITE);
                    tv.setWidth(80);
                    itemsPressed.add(position);
                    //SE NECESITA MANTENER APRETADO (ES LO MISMO QUE TOGGLE?)
                    //tv.setPressed(true);
                } else {
                    Float value = 0.0f;
                    PdBase.sendFloat(msg, value);
                    tv.setBackgroundColor(Color.parseColor("#607D8B"));
                    Log.i("MSJ A PD ", msg);
                    tv.setTextColor(Color.BLACK);
                    tv.setWidth(81);
                    itemsPressed.remove(itemsPressed.indexOf(position));
                    //SE NECESITA MANTENER APRETADO (ES LO MISMO QUE TOGGLE?)
                    //tv.setPressed(false);
                }

            }
        });
        return v;
    }
}