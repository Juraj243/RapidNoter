package com.example.juraj.note.fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.juraj.note.MainActivity;
import com.example.juraj.note.R;
import com.example.juraj.note.adapters.GridViewAdapterCartItems;
import com.example.juraj.note.data.CartItem;
import com.example.juraj.note.data.DaoSession;
import com.example.juraj.note.data.SessionManager;

import java.util.ArrayList;

public class FragmentCartItems extends DialogFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String title = "CartItems";
    private GridViewAdapterCartItems adapter;
    private View view;
    private  GridView gridView;
    private static ArrayList<CartItem> notes;


    public FragmentCartItems() {
        // Required empty public constructor
    }

    public static FragmentCartItems newInstance(ArrayList<CartItem> data) {
        FragmentCartItems fragment = new FragmentCartItems();
        notes = data;
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
        view = inflater.inflate(R.layout.fragment_cart_items, container, false);
        adapter = new GridViewAdapterCartItems(getContext(), R.layout.template_cart_items,notes);
        gridView = view.findViewById(R.id.cart_items_container);
        gridView.setAdapter(adapter);
        refreshGridView();
        return view;
    }

    public String getTitle() {
        return title;
    }

    public void refreshGridView() {

        adapter = new GridViewAdapterCartItems(getContext(), R.layout.template_cart_items,notes);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity)getActivity()).tabLayout.setVisibility(View.INVISIBLE);
        ((MainActivity)getActivity()).mViewPager.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity)getActivity()).mViewPager.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).tabLayout.setVisibility(View.VISIBLE);
    }

}
