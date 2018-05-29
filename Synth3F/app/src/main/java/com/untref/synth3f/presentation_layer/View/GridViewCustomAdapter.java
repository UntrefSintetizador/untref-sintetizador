package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.untref.synth3f.presentation_layer.fragment.FragmentMatriz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

/**
 * Created by ddavi on 25/2/2017.
 */

public class GridViewCustomAdapter extends BaseAdapter {

    //ArrayList<String> items;
    ArrayList<Button> itemsPressed;
    Map<String,Button> items;

    static FragmentMatriz context;

    private static LayoutInflater inflater = null;

    public GridViewCustomAdapter(FragmentMatriz activity, Map<String,Button> tempTitle) {
        context = activity;
        items = tempTitle;
        itemsPressed = new ArrayList<>();

        inflater = (LayoutInflater) context.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<Button> getItemsPressed(){
        return itemsPressed;
    }

    public void setItems(Map<String,Button> map){
        items = map;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ArrayList<Button> buttons = new ArrayList<Button>(items.values() );

        Collections.sort(buttons, new Comparator<Button>() {
            @Override
            public int compare(Button b2, Button b1) {
                return (b2.getText().toString().compareTo(b1.getText().toString()));
            }
        });

        Button tv = buttons.get(position);

        if (itemsPressed.contains(tv)){
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.parseColor("#FF9800"));
        }else {
            tv.setTextColor(Color.BLACK);
            tv.setBackgroundColor(Color.parseColor("#607D8B"));
        }
        return tv;
    }
}