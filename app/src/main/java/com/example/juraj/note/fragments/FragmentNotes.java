package com.example.juraj.note.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.juraj.note.MainActivity;
import com.example.juraj.note.R;
import com.example.juraj.note.adapters.GridViewAdapter;
import com.example.juraj.note.data.DaoSession;
import com.example.juraj.note.data.Note;

import java.util.ArrayList;

public class FragmentNotes extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private GridViewAdapter adapter;

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
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        DaoSession daoSession = ((MainActivity)getActivity()).getDaoSession();
        ArrayList<Note> notes = (ArrayList<Note>) daoSession.getNoteDao().loadAll();

        adapter = new GridViewAdapter(getContext(), R.layout.template_note, notes);

        ((GridView)view.findViewById(R.id.notes_container)).setAdapter(adapter);

        return view;
    }


}
