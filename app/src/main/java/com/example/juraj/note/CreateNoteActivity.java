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
import java.util.List;

public class CreateNoteActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
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
        setContentView(R.layout.activity_create_note);

        final Intent intent = getIntent();
        root = findViewById(R.id.create_note_root);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Button b;
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_note_temp_container);
        LinearLayoutCompat.LayoutParams lp = new LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //int count = prefs.getAll().size()/2;
        for (int i = 0; i < 5; i++) {
            prefs.edit().putString("text" + String.valueOf(i + 1), "text " + i).putString("title" + String.valueOf(i + 1), "Poznamka " + i).apply();
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

        EditText titleEditText = (EditText) findViewById(R.id.et_new_note_title);
        setFocusedET(titleEditText);
        titleEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) setFocusedET(view);
            }
        });

        EditText textEditText = (EditText) findViewById(R.id.et_new_note_text);
        textEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) setFocusedET(view);
            }
        });

        findViewById(R.id.note_temp1).setOnClickListener(this);
        mMap = (MapView) findViewById(R.id.map_new_note);
        mMap.onCreate(savedInstanceState);
        mMap.onResume();
        try {
            MapsInitializer.initialize(getBaseContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMap.getMapAsync(this);

        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(EXTRA_CIRC_REV_X) &&
                intent.hasExtra(EXTRA_CIRC_REV_Y)) {
            root.setVisibility(View.INVISIBLE);

            revealX = intent.getIntExtra(EXTRA_CIRC_REV_X, 0);
            revealY = intent.getIntExtra(EXTRA_CIRC_REV_Y, 0);

            ViewTreeObserver viewTreeObserver = root.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        revealActivity(revealX, revealY);
                        root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        } else {
            root.setVisibility(View.VISIBLE);
        }

        dateTo = (Button) findViewById(R.id.tv_date_To);
        dateFrom = (Button) findViewById(R.id.tv_date_From);
        timeTo = (Button) findViewById(R.id.tv_time_To);
        timeFrom = (Button) findViewById(R.id.tv_time_From);
        checkBoxAlarmtime = (CheckBox) findViewById(R.id.cb_alarm_time);

        Date date = new Date();
        setDateField(dateFrom, date, false);
        setTimeField(timeFrom, date, false);
        date.setTime(date.getTime() + 1000 * 60 * 60);
        setDateField(dateTo, date, false);
        setTimeField(timeTo, date, false);
        dateFrom.setOnClickListener(this);
        dateTo.setOnClickListener(this);
        timeFrom.setOnClickListener(this);
        timeTo.setOnClickListener(this);
        checkBoxAlarmtime.setOnClickListener(this);
        alarmTimesSpinner = (Spinner) findViewById(R.id.sp_alarm_time);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.alarm_times, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        alarmTimesSpinner.setAdapter(spinnerAdapter);

        //findViewById(R.id.cb_end_time).setOnClickListener(this);
        //toggleAlarmTimeListener();
    }

    private void setTimeField(TextView tw, Date date, boolean picked) {
        DateFormat format = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);
        if(picked)
            tw.setText("✓ "+format.format(date));
        else
            tw.setText(format.format(date));

    }

    private void setDateField(TextView tw, Date date, boolean picked) {
        DateFormat time = SimpleDateFormat.getDateInstance();
        if(picked)
            tw.setText("✓ "+time.format(date));
        else
            tw.setText(time.format(date));

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

    private void onDateTextClick(final boolean from) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                c.set(year, month, day);
                if (from) {
                    setDateField(dateFrom, c.getTime(), true);
//                    if (calendarFrom == null) {
//                        calendarFrom = c;
//                    } else {
                    calendarFrom.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
//                    }
                    dateFrom.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark));
                    saveDates = true;
                } else {
                    setDateField(dateTo, c.getTime(), true);
//                    if (calendarTo == null) {
//                        calendarTo = c;
//                    } else {
                    calendarTo.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
//                    }
                    dateFrom.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark));
                    dateTo.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark));
                    saveDates = true;
                }

            }
        }, year, month, day).show();
    }

    private void onTimeTextClick(final boolean from) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                c.set(0, 0, 0, hour, minute);
                if (from) {
                    setTimeField(timeFrom, c.getTime(), true);
//                    if (calendarFrom == null) {
//                        calendarFrom = c;
//                    } else {
                    calendarFrom.set(Calendar.HOUR, c.get(Calendar.HOUR));
                    calendarFrom.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
//                    }
                    timeFrom.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark));
                    saveDates = true;
                } else {
                    setTimeField(timeTo, c.getTime(), true);
//                    if (calendarTo == null) {
//                        calendarTo = c;
//                    } else {
                    calendarTo.set(Calendar.HOUR, c.get(Calendar.HOUR));
                    calendarTo.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
//                    }
                    timeFrom.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark));
                    timeTo.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark));
                    saveDates = true;
                }
            }
        }, hour, minute, true).show();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_date_From:
                onDateTextClick(true);
                break;
            case R.id.tv_date_To:
                onDateTextClick(false);
                break;
            case R.id.tv_time_From:
                onTimeTextClick(true);
                break;
            case R.id.tv_time_To:
                onTimeTextClick(false);
                break;
            case R.id.cb_alarm_time:
                toggleAlarmTimeListener();
                break;
            default:
                focusedEditText.getText().append(" ").append(view.getTag().toString());
                if(focusedEditText.getId() == R.id.et_new_note_title){
                    View v = findViewById(R.id.et_new_note_text);
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

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        gMap = googleMap;
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                gMap.clear();
                gMap.addMarker(new MarkerOptions().position(latLng));
                gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                location = latLng;
            }
        });

    }
}
