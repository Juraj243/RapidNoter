package com.example.juraj.note.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.juraj.note.R;
import com.example.juraj.note.adapters.GridViewAdapterCarts;
import com.example.juraj.note.data.Cart;
import com.example.juraj.note.data.DaoSession;
import com.example.juraj.note.data.SessionManager;

import java.util.ArrayList;

public class FragmentCarts extends AbstractFragent {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String title = "Carts";
    private GridViewAdapterCarts adapter;
    private View view;
    private  GridView gridView;
    private ArrayList<Cart> notes = new ArrayList<>();

    public FragmentCarts() {
        // Required empty public constructor
    }

    public static FragmentCarts newInstance(String param1, String param2) {
        FragmentCarts fragment = new FragmentCarts();
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
        view = inflater.inflate(R.layout.fragment_carts, container, false);
        loadNotes();
        adapter = new GridViewAdapterCarts(getContext(), R.layout.template_cart,notes);
        gridView = view.findViewById(R.id.carts_container);
        gridView.setAdapter(adapter);
        return view;
    }

    public String getTitle() {
        return title;
    }

    public void refreshGridView() {
        loadNotes();
        adapter = new GridViewAdapterCarts(getContext(), R.layout.template_cart,notes);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void loadNotes(){
        DaoSession daoSession = SessionManager.getInstance().getDaoSession();
        notes.removeAll(notes);
        notes.addAll(daoSession.getCartDao().loadAll());
    }

}
