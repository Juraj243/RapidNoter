package com.example.juraj.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;
import com.example.juraj.note.adapters.SectionsPagerAdapter;
import com.example.juraj.note.data.Cart;
import com.example.juraj.note.data.CartItem;
import com.example.juraj.note.data.Constants;
import com.example.juraj.note.data.DaoMaster;
import com.example.juraj.note.data.DaoSession;
import com.example.juraj.note.data.Note;
import com.example.juraj.note.data.SessionManager;



import org.greenrobot.greendao.database.Database;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Juraj on 18.11.2017.
 */

public class MainActivity extends AppCompatActivity {

    private static final int CREATE_NOTE_REQUEST_CODE = 1;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        daoSession =  SessionManager.getInstance(this).getDaoSession();
        final Database db = SessionManager.getInstance().getDb();

       DaoMaster.dropAllTables(db, true);
        DaoMaster.createAllTables(db, true);
        //daoSession.getNoteDao().deleteAll();
        //daoSession.getNoteDao().insert(new Note(1l,"Nazov","poznamka, poznamka", new Date(), null, ""));

        Cart c = new Cart();
        c.setName("cart1");
        daoSession.getCartDao().insert(c);

        CartItem ci = new CartItem();
        ci.setDate(new Date());
        ci.setCart(c);
        ci.setName("item1");
        ci.setCartId(c.getId());
        c.getCartItems().add(ci);
        daoSession.getCartItemDao().insert(ci);
        daoSession.getCartDao().update(c);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
                startActivityForResult(intent, CREATE_NOTE_REQUEST_CODE);*/
                presentActivity(view);
            }
        });

        findViewById(R.id.fab2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DaoMaster.dropAllTables(db, true);
                DaoMaster.createAllTables(db, true);
                ((GridView) findViewById(R.id.notes_container)).invalidateViews();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CREATE_NOTE_REQUEST_CODE) {
            Note noteToSave = new Note();
            noteToSave.setTitle(data.getStringExtra(Constants.NOTE_TITLE));
            noteToSave.setText(data.getStringExtra(Constants.NOTE_TEXT));

            noteToSave.setLatitude(data.getDoubleExtra(Constants.NOTE_LATITUDE, 0));
            noteToSave.setLongitude(data.getDoubleExtra(Constants.NOTE_LONGITUDE, 0));
            DateFormat df = SimpleDateFormat.getDateTimeInstance();
            setDateForNote(noteToSave, data, Constants.NOTE_DATE_FROM);
            setDateForNote(noteToSave, data, Constants.NOTE_DATE_TO);
            try {
                Date date = df.parse(data.getStringExtra(Constants.NOTE_CREATED));
                Toast.makeText(this, data.getStringExtra(Constants.NOTE_CREATED), Toast.LENGTH_LONG).show();
                noteToSave.setCreated(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            daoSession.getNoteDao().insert(noteToSave);
            mViewPager.getAdapter().notifyDataSetChanged();

        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    private void setDateForNote(Note noteToSave, Intent data, String key) {
        String date = data.getStringExtra(key);
        if (date == null) {
            // log?
            return;
        }
        DateFormat df = SimpleDateFormat.getDateTimeInstance();
        try {
            Date parsedDate = df.parse(date);
            switch (key) {
                case Constants.NOTE_DATE_FROM:
                    noteToSave.setDate_from(parsedDate);
                    break;
                case Constants.NOTE_DATE_TO:
                    noteToSave.setDate_to(parsedDate);
                    break;
                default:
                    throw new UnsupportedOperationException(String.valueOf(key));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void presentActivity(View view) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "transition");
        int revX = (int) (view.getX() + view.getWidth() / 2);
        int revY = (int) (view.getY() + view.getHeight() / 2);

        Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
        intent.putExtra(CreateNoteActivity.EXTRA_CIRC_REV_X, revX);
        intent.putExtra(CreateNoteActivity.EXTRA_CIRC_REV_Y, revY);
        startActivityForResult(intent, CREATE_NOTE_REQUEST_CODE);
    }
}
