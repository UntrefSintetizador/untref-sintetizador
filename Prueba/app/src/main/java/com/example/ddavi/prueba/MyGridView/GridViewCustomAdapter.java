package com.example.ddavi.prueba.MyGridView;

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

import com.example.ddavi.prueba.Listeners.ModuleListener;
import com.example.ddavi.prueba.MainActivity;
import com.example.ddavi.prueba.ModulesPopupWindow.VCOPopupWindow;
import com.example.ddavi.prueba.R;

import org.puredata.core.PdBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GridViewCustomAdapter extends BaseAdapter {

    //ArrayList<String> items;
    ArrayList<Button> itemsPressed;
    Map<String,Button> items;

    static Activity mActivity;

    private static LayoutInflater inflater = null;

    /**
    public GridViewCustomAdapter(Activity activity, ArrayList<String> tempTitle) {
        mActivity = activity;
        items = tempTitle;
        itemsPressed = new ArrayList<>();

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }**/

    public GridViewCustomAdapter(Activity activity, Map<String,Button> tempTitle) {
        mActivity = activity;
        items = tempTitle;
        itemsPressed = new ArrayList<>();

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<Button> getItemsPressed(){
        return itemsPressed;
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
        List<Button> buttons = new ArrayList<Button>(items.values() );

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