package com.example.juraj.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;
import com.example.juraj.note.adapters.SectionsPagerAdapter;
import com.example.juraj.note.data.CartItem;
import com.example.juraj.note.data.Constants;
import com.example.juraj.note.data.DaoMaster;
import com.example.juraj.note.data.DaoSession;
import com.example.juraj.note.data.Note;
import com.example.juraj.note.data.SessionManager;
import com.example.juraj.note.fragments.FragmentCartItems;
import org.greenrobot.greendao.database.Database;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Juraj on 18.11.2017.
 */

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final int CREATE_NOTE_REQUEST_CODE = 1;
    public ViewPager mViewPager;
    public TabLayout tabLayout;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("RapidNoter");
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);
        mViewPager.addOnPageChangeListener(this);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        daoSession = SessionManager.getInstance(this).getDaoSession();
        final Database db = SessionManager.getInstance().getDb();
//        DaoMaster.dropAllTables(db, true);
//        DaoMaster.createAllTables(db, true);
        //daoSession.getNoteDao().deleteAll();
        //daoSession.getNoteDao().insert(new Note(1l,"Nazov","poznamka, poznamka", new Date(), null, ""));
        /*Cart c = new Cart();
        c.setName("cart1");
        daoSession.getCartDao().insert(c);

        CartItem ci = new CartItem();
        ci.setDate(new Date());
        ci.setCart(c);
        ci.setName("Chleba");
        ci.setCartId(c.getId());
        c.getCartItems().add(ci);
        daoSession.getCartItemDao().insert(ci);

        CartItem ci1 = new CartItem();
        ci1.setDate(new Date());
        ci1.setCart(c);
        ci1.setName("Rozky");
        ci1.setCartId(c.getId());
        c.getCartItems().add(ci1);
        daoSession.getCartItemDao().insert(ci1);

        CartItem ci2 = new CartItem();
        ci2.setDate(new Date());
        ci2.setCart(c);
        ci2.setName("Mlieko");
        ci2.setCartId(c.getId());
        c.getCartItems().add(ci2);
        daoSession.getCartItemDao().insert(ci2);

        CartItem ci3 = new CartItem();
        ci3.setDate(new Date());
        ci3.setCart(c);
        ci3.setName("Vajca");
        ci3.setCartId(c.getId());
        c.getCartItems().add(ci3);
        daoSession.getCartItemDao().insert(ci3);
        daoSession.getCartDao().update(c);


        Cart c2 = new Cart();
        c2.setName("cart2");
        daoSession.getCartDao().insert(c2);

        CartItem ci4 = new CartItem();
        ci4.setDate(new Date());
        ci4.setCart(c);
        ci4.setName("Vajca");
        ci4.setCartId(c2.getId());
        c.getCartItems().add(ci4);
        daoSession.getCartItemDao().insert(ci4);

        CartItem ci5 = new CartItem();
        ci5.setDate(new Date());
        ci5.setCart(c);
        ci5.setName("Pivo");
        ci5.setCartId(c2.getId());
        c.getCartItems().add(ci5);
        daoSession.getCartItemDao().insert(ci5);

        daoSession.getCartDao().update(c2);
*/
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
                startActivityForResult(intent, CREATE_NOTE_REQUEST_CODE);*/
                presentActivity(view, CreateNoteActivity.class);
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

    public void presentActivity(View view, Class clazz) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "transition");
        int revX = (int) (view.getX() + view.getWidth() / 2);
        int revY = (int) (view.getY() + view.getHeight() / 2);

        Intent intent = new Intent(getApplicationContext(), clazz);
        intent.putExtra(CreateNoteActivity.EXTRA_CIRC_REV_X, revX);
        intent.putExtra(CreateNoteActivity.EXTRA_CIRC_REV_Y, revY);
        startActivityForResult(intent, CREATE_NOTE_REQUEST_CODE);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        FloatingActionButton fab = findViewById(R.id.fab);
        if (position == 3) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presentActivity(view, CreateCartActivity.class);
                }
            });
        } else {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presentActivity(view, CreateNoteActivity.class);
                }
            });
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void showCartItemsFragment(ArrayList<CartItem> items) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FragmentCartItems fragmentCartItems = FragmentCartItems.newInstance(items);
        transaction.replace(R.id.frameLayout, fragmentCartItems).addToBackStack(null).commit();

    }
}
