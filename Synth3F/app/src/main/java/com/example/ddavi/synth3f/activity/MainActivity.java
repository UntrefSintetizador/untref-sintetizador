package com.example.ddavi.synth3f.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.media.midi.MidiManager;
import android.media.midi.MidiReceiver;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ddavi.synth3f.R;
import com.example.ddavi.synth3f.fragment.FragmentMatriz;
import com.example.ddavi.synth3f.fragment.FragmentOrgano;
import com.example.ddavi.synth3f.midi.LoggingReceiver;
import com.example.ddavi.synth3f.midi.MidiPrinter;
import com.example.ddavi.synth3f.midi.MidiScope;
import com.example.ddavi.synth3f.midi.ScopeLogger;
import com.example.ddavi.synth3f.presenters.MainActivityPresenter;
import com.mobileer.miditools.MidiFramer;
import com.mobileer.miditools.MidiOutputPortSelector;
import com.mobileer.miditools.MidiPortWrapper;

import java.io.IOException;
import java.util.LinkedList;

import org.puredata.core.PdBase;

public class MainActivity extends AppCompatActivity implements ActionBar.OnNavigationListener, SharedPreferences.OnSharedPreferenceChangeListener, ScopeLogger {

    private MainActivityPresenter presenter;

    // MIDI :: START
    private TextView mLog;
    private ScrollView mScroller;
    private LinkedList<String> logLines = new LinkedList<String>();
    private static final int MAX_LINES = 100;
    private MidiOutputPortSelector mLogSenderSelector;
    private MidiManager mMidiManager;
    private MidiReceiver mLoggingReceiver;
    private MidiFramer mConnectFramer;
    private MyDirectReceiver mDirectReceiver;
    private boolean mShowRaw;

    class MyDirectReceiver extends MidiReceiver {
        @Override
        public void onSend(byte[] data, int offset, int count,
                           long timestamp) throws IOException {
            if (mShowRaw) {
                String prefix = String.format("0x%08X, ", timestamp);
                logByteArray(prefix, data, offset, count);
            }
            // Send raw data to be parsed into discrete messages.
            mConnectFramer.send(data, offset, count, timestamp);
        }
    }

    private void logByteArray(String prefix, byte[] value, int offset, int count) {
        StringBuilder builder = new StringBuilder(prefix);
        for (int i = 0; i < count; i++) {
            builder.append(String.format("0x%02X", value[offset + i]));
            if (i != count - 1) {
                builder.append(", ");
            }
        }
        log(builder.toString());
    }

    @Override
    public void log(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logFromUiThread(string);
            }
        });
    }

    // Log a message to our TextView.
    // Must run on UI thread.
    private void logFromUiThread(String s) {
        logLines.add(s);
        if (logLines.size() > MAX_LINES) {
            logLines.removeFirst();
        }
        // Render line buffer to one String.
        StringBuilder sb = new StringBuilder();
        for (String line : logLines) {
            sb.append(line).append('\n');
        }
        mLog.setText(sb.toString());
        mScroller.fullScroll(View.FOCUS_DOWN);
        if (s.contains("NoteOn")) {
            String[] split = s.split(",");
            if (split.length > 1) {
                String velocityString = split[2].trim();
                velocityString = velocityString.substring(0,velocityString.length() - 1);
                int velocity = Integer.parseInt(velocityString);
                int midi = Integer.parseInt(split[1].trim());
//                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
                if (velocity != 0) {
                    PdBase.sendFloat("X_KB_midi_note", midi);
                    PdBase.sendFloat("X_KB_gate", 1);
                } else {
                    PdBase.sendFloat("X_KB_gate", 0);
                }
            }
        }
//            PdBase.sendFloat("X_KB_midi_note", (keyboard.getPressedKey().midiCode - 24) + (INITIAL_OCTIVE * 12));
    }

    private void setupMIDI() {
        mLog = (TextView) findViewById(R.id.log);
        mScroller = (ScrollView) findViewById(R.id.scroll);

        // Setup MIDI
        mMidiManager = (MidiManager) getSystemService(MIDI_SERVICE);

        // Receiver that prints the messages.
        mLoggingReceiver = new LoggingReceiver(this);

        // Receivers that parses raw data into complete messages.
        mConnectFramer = new MidiFramer(mLoggingReceiver);

        // Setup a menu to select an input source.
        mLogSenderSelector = new MidiOutputPortSelector(mMidiManager, this,
                R.id.spinner_senders) {

            @Override
            public void onPortSelected(final MidiPortWrapper wrapper) {
                super.onPortSelected(wrapper);
                if (wrapper != null) {
                    log(MidiPrinter.formatDeviceInfo(wrapper.getDeviceInfo()));
                }
            }
        };

        mDirectReceiver = new MyDirectReceiver();
        mLogSenderSelector.getSender().connect(mDirectReceiver);

        // Tell the virtual device to log its messages here..
        MidiScope.setScopeLogger(this);
    }
    // MIDI :: END

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initializeActionBar();

        presenter = new MainActivityPresenter(this);

        FragmentManager fmanager = getFragmentManager();
        FragmentTransaction transaction = fmanager.beginTransaction();
        transaction.add(R.id.container_matriz,new FragmentMatriz());
        transaction.add(R.id.container_piano, new FragmentOrgano());
        transaction.commit();

        this.presenter.initializeMasterConfig();
        this.initializeNavegationDrawer();

        this.setupMIDI();
    }

    private void initializeNavegationDrawer(){
        final DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        final NavigationView navView = (NavigationView)findViewById(R.id.navview);

        Menu menu = navView.getMenu();
        MenuItem modulos= menu.findItem(R.id.menu_seccion_sliders);
        SpannableString s = new SpannableString(modulos.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.MenuTextTheme), 0, s.length(), 0);
        modulos.setTitle(s);

        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        presenter.onClickSelected(navView,menuItem.getTitle().toString());
                        drawerLayout.closeDrawers();

                        return true;
                    }
                });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        presenter.startService();
        /*
        if (pdConfig.getService().isRunning()) {
            pdConfig.startAudio();
        }*/
    }

    private void initializeActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        //actionBar.setIcon(R.drawable.icon);

        //crear el spinner
        SpinnerAdapter adapter = ArrayAdapter.createFromResource(this, R.array.Presets, R.layout.item_spinner);
        actionBar.setListNavigationCallbacks(adapter, this);

        //mostrar el spinner
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

    }

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        presenter.resetPresets();
        switch (i) {
            case 0:
                presenter.setPreset("sq_bass1");
                Log.i("Preset Elegido", "Preset 9 (Sq Bass1)");
                break;

            case 1:
                presenter.setPreset("filter_tone");
                Log.i("Preset Elegido", "Preset 2 (Filter Tone)");
                break;

            case 2:
                presenter.setPreset("herbie");
                Log.i("Preset Elegido", "Preset 3 (Herbie)");
                break;

            case 3:
                presenter.setPreset("extasis");
                Log.i("Preset Elegido", "Preset 4 (Extasis)");
                break;

            case 4:
                presenter.setPreset("fm");
                Log.i("Preset Elegido", "Preset 9 (Sq Bass1)");
                break;

            case 5:
                presenter.setPreset("saw_seq");
                Log.i("Preset Elegido", "Preset 6 (Saw Seq)");
                break;

            case 6:
                presenter.setPreset("bell");
                Log.i("Preset Elegido", "Preset 7 (Bell)");
                break;

            case 7:
                presenter.setPreset("sq_bass");
                Log.i("Preset Elegido", "Preset 8 (Sq Bass)");
                break;

            case 8:
                presenter.setPreset("chord_pad");
                Log.i("Preset Elegido", "Preset 1 (Chord Pad)");
                break;

            default:
                presenter.setPreset("sq_bass1");
                Log.i("Preset Elegido", "Preset 1 (Chord Pad)");
                break;

        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_exit:
                presenter.stopService();
                presenter.unBindService();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unBindService();
    }

}
