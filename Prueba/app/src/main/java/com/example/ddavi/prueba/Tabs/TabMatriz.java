package com.example.ddavi.prueba.Tabs;

import android.content.ClipData;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.ddavi.prueba.Listeners.ModuleListener;
import com.example.ddavi.prueba.MainActivity;
import com.example.ddavi.prueba.ModulesPopupWindow.EGPopupWindow;
import com.example.ddavi.prueba.ModulesPopupWindow.MIXPopupWindow;
import com.example.ddavi.prueba.ModulesPopupWindow.ModulePopupWindow;
import com.example.ddavi.prueba.ModulesPopupWindow.SHPopupWindow;
import com.example.ddavi.prueba.ModulesPopupWindow.VCAPopupWindow;
import com.example.ddavi.prueba.ModulesPopupWindow.VCFPopupWindow;
import com.example.ddavi.prueba.ModulesPopupWindow.VCOPopupWindow;
import com.example.ddavi.prueba.MyGridView.MyGridView;
import com.example.ddavi.prueba.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by ddavi on 13/9/2016.
 */
public class TabMatriz extends Fragment {

    private MyGridView matriz;
    boolean addModules;

    public TabMatriz(){
        addModules = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final MainActivity activity = (MainActivity)getActivity();
        View view;

        if (activity.getButtonsModulesMatriz().isEmpty() || addModules)
            view = inflater.inflate(R.layout.menu_modulos, container, false);
        else {
            view = inflater.inflate(R.layout.tab_matriz, container, false);
            matriz = (MyGridView) view.findViewById(R.id.grid_view);
            matriz.setAdapter(activity.getGridViewAdapter());

            initializeSpinnerSilders(view,activity);

            int cant_elements = activity.getButtonsModulesMatriz().size();
            int cant_columns = (cant_elements/4 > 15)? 15: (int)cant_elements/4;
            matriz.setNumColumns(cant_columns);

            initializeAddModules(activity,view);
        }

        return view;
    }

    private ArrayList<String> getNameSliders(MainActivity activity){
        ArrayList<String> sliders = new ArrayList<String>(activity.getSliders().keySet());

        Collections.sort(sliders, new Comparator<String>() {
            @Override
            public int compare(String b2, String b1) {
                return (b2.compareTo(b1));
            }
        });
        ArrayList<String> sliders2 = new ArrayList<>();
        sliders2.add("Seleccionar Modulo");
        sliders2.addAll(sliders);

        return sliders2;
    }

    private void initializeSpinnerSilders(View view,final MainActivity activity){
        final Spinner spSliders = (Spinner) view.findViewById(R.id.spinner_sliders);
        ArrayList<String> sliders = getNameSliders(activity);
        spSliders.setSelection(1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,R.layout.item_spinner, sliders);
        adapter.setDropDownViewResource(R.layout.item_spinner);
        spSliders.setAdapter(adapter);
        spSliders.post(new Runnable() {
            @Override
            public void run() {
                spSliders.setSelection(0);
            }
        });
        spSliders.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {

               if (position > 0 && v != null) {
                   // On selecting a spinner item
                   String name_modulo = adapter.getItemAtPosition(position).toString();
                   // Showing selected spinner item
                   ModulePopupWindow popup = activity.getSliders().get(name_modulo);//createPopUpWindow(name_modulo, name_modulo, activity);
                   popup.showAsDropDown(v, 150, -200);
                   popup.setButton(v);
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void initializeAddModules(final MainActivity activity, View view){

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.addModules);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TabMatriz tab = ((TabMatriz)activity.getSupportFragmentManager().findFragmentByTag("tab2"));
                tab.setAddModules(true);

                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                ft.detach(tab);
                ft.attach(tab);
                ft.commit();
            }
        });
    }

    public void setAddModules(boolean add){
        addModules = add;
    }

}