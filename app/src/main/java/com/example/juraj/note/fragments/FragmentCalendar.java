package com.example.juraj.note.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.juraj.note.MainActivity;
import com.example.juraj.note.R;
import com.example.juraj.note.adapters.GridViewAdapter;
import com.example.juraj.note.data.DaoSession;
import com.example.juraj.note.data.Note;
import com.example.juraj.note.data.NoteDao;
import com.prolificinteractive.materialcalendarview.*;
import org.greenrobot.greendao.query.WhereCondition;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentCalendar extends AbstractFragent {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private String title = "Calendar";

    private MaterialCalendarView calendar;
    private ListView dayView;

    private List<Note> notes;
    private WhereCondition dateFromNotNull = NoteDao.Properties.Date_from.isNotNull();
    private DaoSession daoSession;

    public FragmentCalendar() {
        // Required empty public constructor
    }

    public static FragmentCalendar newInstance(String param1, String param2) {
        FragmentCalendar fragment = new FragmentCalendar();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        daoSession = ((MainActivity) getActivity()).getDaoSession();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        initCalendar(view);
        showDetail(calendar.getCurrentDate());
        return view;
    }

    private void showDetail(CalendarDay day) {
        if (day == null) {
            dayView.setAdapter(null);
            return;
        }
        ArrayList<Note> currentDayNotes = new ArrayList<>();
        Date currentDay = day.getDate();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        for (Note note : notes) {
            if (sdf.format(currentDay).equals(sdf.format(note.getDate_from()))) {
                currentDayNotes.add(note);
            }
        }

        if (notes.isEmpty()) {
            return;
        }

        GridViewAdapter adapter = new GridViewAdapter(getContext(), R.layout.template_note, currentDayNotes);

        dayView.setAdapter(adapter);
    }

    private void initCalendar(View mainView) {
        notes = daoSession.getNoteDao().queryBuilder().where(dateFromNotNull).list();
        calendar = mainView.findViewById(R.id.calendarView);
        calendar.setTileSizeDp(45);

        final ArrayList<CalendarDay> calendarDays = new ArrayList<>();
        for (Note note : notes) {
            calendarDays.add(CalendarDay.from(note.getDate_from()));
        }
        calendar.addDecorator(new NotedDates(calendarDays));

        calendar.setOnDateChangedListener(new CalendarListener());

        dayView = mainView.findViewById(R.id.calendarDayView);
    }

    public String getTitle() {
        return title;
    }

    @Override
    public void refreshGridView() {
        //refresh
    }

    private class NotedDates implements DayViewDecorator {

        private final ArrayList<CalendarDay> dates;

        NotedDates(ArrayList<CalendarDay> dates) {
            super();
            this.dates = dates;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.date_with_note));
        }
    }

    private class CalendarListener implements OnDateSelectedListener {
        @Override
        public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
            if (selected) {
                showDetail(date);
            } else {
                showDetail(null);
            }
        }
    }
}
