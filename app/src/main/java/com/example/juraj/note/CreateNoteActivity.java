package com.example.juraj.note;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.juraj.note.data.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CreateNoteActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    private EditText focusedEditText;
    public static final String EXTRA_CIRC_REV_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRC_REV_Y = "EXTRA_CIRCULAR_REVEAL_Y";

    View root;
    private int revealX;
    private int revealY;
    private GoogleMap gMap;
    private MapView mMap;

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
                ArrayList<String> data = new ArrayList<>();
                EditText et = (EditText) findViewById(R.id.et_new_note_title);
                data.add(Constants.NOTE_TITLE, et.getText().toString());
                et = (EditText) findViewById(R.id.et_new_note_text);
                data.add(Constants.NOTE_TEXT, et.getText().toString());

                DateFormat sdf = SimpleDateFormat.getDateTimeInstance();
                Date date = new Date();
                data.add(Constants.NOTE_CREATED, sdf.format(date));

                Intent intent = new Intent();
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
            MapsInitializer.initialize(getApplicationContext());
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
        focusedEditText.getText().append(" ").append(view.getTag().toString());
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
    public void onMapReady(GoogleMap googleMap) {

    }
}
