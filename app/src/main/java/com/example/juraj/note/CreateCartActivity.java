package com.example.juraj.note;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.juraj.note.R;
import com.example.juraj.note.data.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateCartActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_CIRC_REV_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRC_REV_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    View root;
    private EditText focusedEditText;
    private int revealX;
    private int revealY;
    private GoogleMap gMap;
    private MapView mMap;
    private LatLng location;

    // typed views
    private TextView dateTo;
    private TextView timeTo;
    private TextView dateFrom;
    private TextView timeFrom;
    private boolean saveDates = false;

    // dates
    private Calendar calendarFrom = Calendar.getInstance();
    private Calendar calendarTo = Calendar.getInstance();
    private CheckBox checkBoxAlarmtime;
    private Spinner alarmTimesSpinner;
    private ArrayAdapter<CharSequence> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cart);

        final Intent intent = getIntent();
        root = findViewById(R.id.create_note_root);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Button b;
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_item_temp_container);
        LinearLayoutCompat.LayoutParams lp = new LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //int count = prefs.getAll().size()/2;
        for (int i = 0; i < 5; i++) {
            prefs.edit().putString("text" + String.valueOf(i + 1), "pivo " + i).putString("title" + String.valueOf(i + 1), "Pivo " + i).apply();
            b = new Button(this, null, R.style.Widget_AppCompat_Button_Borderless, R.style.Widget_AppCompat_Button_Borderless);
            b.setText(prefs.getString("title" + String.valueOf(i + 1), null));
            b.setTag(prefs.getString("text" + String.valueOf(i + 1), null));
            b.setLayoutParams(lp);
            b.setOnClickListener(this);
            System.out.println("creating button " + i);
            linearLayout.addView(b);
        }

        findViewById(R.id.fb_save_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK, buildResult(intent));
                unRevealActivity();
            }
        });

        EditText titleEditText = (EditText) findViewById(R.id.et_new_cart_title);
        //setFocusedET(titleEditText);
        titleEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) setFocusedET(view);
            }
        });

        EditText textEditText = (EditText) findViewById(R.id.et_new_cart_items);
        textEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) setFocusedET(view);
            }
        });


    }



    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        setResult(RESULT_CANCELED);
        unRevealActivity();
    }

    private void setFocusedET(View focusedET) {
        focusedEditText = (EditText) focusedET;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.cb_alarm_time:
                toggleAlarmTimeListener();
                break;
            default:
                focusedEditText.getText().append("\n").append(view.getTag().toString());
                if(focusedEditText.getId() == R.id.et_new_cart_title){
                    View v = findViewById(R.id.et_new_cart_items);
                    if(v.requestFocus()) {
                        setFocusedET(v);
                    }
                }
                break;
        }
    }

    private void toggleAlarmTimeListener() {
        if (((CheckBox) findViewById(R.id.cb_alarm_time)).isChecked()) {
            alarmTimesSpinner.setEnabled(true);
        } else {
            alarmTimesSpinner.setEnabled(false);
        }
    }

    private Intent buildResult(Intent result){
        EditText et = (EditText) findViewById(R.id.et_new_note_title);
        result.putExtra(Constants.NOTE_TITLE, et.getText().toString());
        et = (EditText) findViewById(R.id.et_new_note_text);
        result.putExtra(Constants.NOTE_TEXT, et.getText().toString());

        DateFormat sdf = SimpleDateFormat.getDateTimeInstance();
        Date date = new Date();

        if(location != null) {
            result.putExtra(Constants.NOTE_LATITUDE, location.latitude);
            result.putExtra(Constants.NOTE_LONGITUDE, location.longitude);
        } else {
            result.putExtra(Constants.NOTE_LATITUDE, 0.0);
            result.putExtra(Constants.NOTE_LONGITUDE, 0.0);
        }

        result.putExtra(Constants.NOTE_CREATED, sdf.format(date));

        if(saveDates) {
            System.out.println(sdf.format(calendarFrom.getTime()));
            result.putExtra(Constants.NOTE_DATE_FROM, sdf.format(calendarFrom.getTime()));
            result.putExtra(Constants.NOTE_DATE_TO, sdf.format(calendarTo.getTime()));
        }

        return result;
    }

    protected void revealActivity(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float radius = (float) ((Math.max(root.getWidth(), root.getHeight())) * 1.1);
            Animator circReveal = ViewAnimationUtils.createCircularReveal(root, x, y, 0, radius);
            circReveal.setDuration(400);
            circReveal.setInterpolator(new AccelerateInterpolator());
            root.setVisibility(View.VISIBLE);
            circReveal.start();
        } else {
            finish();
        }
    }

    protected void unRevealActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float radius = (float) ((Math.max(root.getWidth(), root.getHeight())) * 1.1);
            Animator circReveal = ViewAnimationUtils.createCircularReveal(root, revealX, revealY, radius, 0);
            circReveal.setDuration(400);
            circReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    root.setVisibility(View.INVISIBLE);
                    finish();
                }
            });

            circReveal.start();
        } else {
            finish();
        }
    }


}
