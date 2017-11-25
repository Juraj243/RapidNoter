package com.example.juraj.note.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.juraj.note.MainActivity;
import com.example.juraj.note.R;
import com.example.juraj.note.data.Cart;
import com.example.juraj.note.data.Note;

import java.util.ArrayList;

/**
 * Created by Juraj on 19.11.2017.
 */

public class GridViewAdapterCarts extends ArrayAdapter<Cart> {
    private ArrayList<Cart> data;
    private LayoutInflater inflater;
    private TextView text;

    public void setData(ArrayList<Cart> data) {
        this.data = data;
    }

    public GridViewAdapterCarts(@NonNull Context context, @LayoutRes int resource, ArrayList<Cart> data) {
        super(context, resource,data);
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = convertView;
        if (item == null) {
            inflater = ((MainActivity) getContext()).getLayoutInflater();
            item = inflater.inflate(R.layout.template_cart, parent, false);
            text = item.findViewById(R.id.tv_cart_text_small);
            Log.d("TAG",data.get(position).getCartItems().toString() );
            text.setText(data.get(position).getCartItems().get(0).getName());
            text = item.findViewById(R.id.tv_cart_title_small);
            text.setText(data.get(position).getName());
        } else {

        }
        return item;
    }

    @Override
    public int getCount() {
         return data.size();
    }
}
