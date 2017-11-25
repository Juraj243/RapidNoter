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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.*;
import com.example.juraj.note.data.Constants;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreateNoteActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    public static final String EXTRA_CIRC_REV_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRC_REV_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    private final ArrayList<String> data = new ArrayList<>();
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

    // dates
    private Calendar calendarFrom = Calendar.getInstance();
    private Calendar calendarTo = Calendar.getInstance();

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

                EditText et = (EditText) findViewById(R.id.et_new_note_title);
                intent.putExtra(Constants.NOTE_TITLE, et.getText().toString());
                et = (EditText) findViewById(R.id.et_new_note_text);
                intent.putExtra(Constants.NOTE_TEXT, et.getText().toString());

                DateFormat sdf = SimpleDateFormat.getDateTimeInstance();
                Date date = new Date();
                /*data.add(Constants.NOTE_CREATED, sdf.format(date));
                data.add(Constants.NOTE_NOTIFICATION, "");

                if(location != null) {
                    data.add(Constants.NOTE_LATITUDE, String.valueOf(location.latitude));
                    data.add(Constants.NOTE_LONGITUDE, String.valueOf(location.longitude));
                } else {
                    data.add(Constants.NOTE_LATITUDE, "");
                    data.add(Constants.NOTE_LONGITUDE, "");
                }*/
                intent.putExtra(Constants.NOTE_CREATED, sdf.format(date));

                intent.putExtra(Constants.NOTE_DATE_FROM, sdf.format(calendarFrom.getTime()));
                intent.putExtra(Constants.NOTE_DATE_TO, sdf.format(calendarTo.getTime()));

                intent.putStringArrayListExtra("note_data", data);
                setResult(RESULT_OK, intent);
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

        dateTo = (TextView) findViewById(R.id.tv_date_To);
        dateFrom = (TextView) findViewById(R.id.tv_date_From);
        timeTo = (TextView) findViewById(R.id.tv_time_To);
        timeFrom = (TextView) findViewById(R.id.tv_time_From);


        Date date = new Date();
        setDateField(dateFrom, date);
        setTimeField(timeFrom, date);
        date.setTime(date.getTime() + 1000 * 60 * 60);
        setDateField(dateTo, date);
        setTimeField(timeTo, date);
        dateFrom.setOnClickListener(this);
        dateTo.setOnClickListener(this);
        timeFrom.setOnClickListener(this);
        timeTo.setOnClickListener(this);
        findViewById(R.id.cb_end_time).setOnClickListener(this);
        toggleEndTimeListener();
    }

    private void setTimeField(TextView tw, Date date) {
        DateFormat format = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);
        tw.setText(format.format(date));
    }

    private void setDateField(TextView tw, Date date) {
        DateFormat time = SimpleDateFormat.getDateInstance();
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
                    setDateField(dateFrom, c.getTime());
//                    if (calendarFrom == null) {
//                        calendarFrom = c;
//                    } else {
                    calendarFrom.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
//                    }
                } else {
                    setDateField(dateTo, c.getTime());
//                    if (calendarTo == null) {
//                        calendarTo = c;
//                    } else {
                    calendarTo.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
//                    }
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
                    setTimeField(timeFrom, c.getTime());
//                    if (calendarFrom == null) {
//                        calendarFrom = c;
//                    } else {
                    calendarFrom.set(Calendar.HOUR, c.get(Calendar.HOUR));
                    calendarFrom.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
//                    }
                } else {
                    setTimeField(timeTo, c.getTime());
//                    if (calendarTo == null) {
//                        calendarTo = c;
//                    } else {
                    calendarTo.set(Calendar.HOUR, c.get(Calendar.HOUR));
                    calendarTo.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
//                    }
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
            case R.id.cb_end_time:
                toggleEndTimeListener();
                break;
            default:
                focusedEditText.getText().append(" ").append(view.getTag().toString());
                if (focusedEditText.getId() == R.id.et_new_note_title) {
                    View v = findViewById(R.id.et_new_note_text);
                    if (v.requestFocus()) {
                        setFocusedET(v);
                    }
                }
                break;
        }
    }

    private void toggleEndTimeListener() {
        if (((CheckBox) findViewById(R.id.cb_end_time)).isChecked()) {
            dateTo.setVisibility(View.VISIBLE);
            timeTo.setVisibility(View.VISIBLE);
        } else {
            dateTo.setVisibility(View.INVISIBLE);
            timeTo.setVisibility(View.INVISIBLE);
        }
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
