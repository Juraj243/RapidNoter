package com.example.juraj.note.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.juraj.note.MainActivity;
import com.example.juraj.note.R;
import com.example.juraj.note.adapters.GridViewAdapter;
import com.example.juraj.note.data.DaoSession;
import com.example.juraj.note.data.Note;
import com.example.juraj.note.data.SessionManager;

import java.util.ArrayList;

public class FragmentNotes extends AbstractFragent {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String title = "Notes";
    private GridViewAdapter adapter;
    private View view;
    private  GridView gridView;
    private ArrayList<Note> notes = new ArrayList<>();

    public FragmentNotes() {
        // Required empty public constructor
    }

    public static FragmentNotes newInstance(String param1, String param2) {
        FragmentNotes fragment = new FragmentNotes();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notes, container, false);
        loadNotes();
        adapter = new GridViewAdapter(getContext(), R.layout.template_note,notes);
        gridView = view.findViewById(R.id.notes_container);
        gridView.setAdapter(adapter);
        return view;
    }

    public String getTitle() {
        return title;
    }

    public void refreshGridView() {
        loadNotes();
        adapter = new GridViewAdapter(getContext(), R.layout.template_note,notes);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void loadNotes(){
        DaoSession daoSession = SessionManager.getInstance().getDaoSession();
        notes.removeAll(notes);
        notes.addAll(daoSession.getNoteDao().loadAll());
    }

}
