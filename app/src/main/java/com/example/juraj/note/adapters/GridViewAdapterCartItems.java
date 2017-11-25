package com.example.juraj.note.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.juraj.note.MainActivity;
import com.example.juraj.note.R;
import com.example.juraj.note.data.CartItem;

import java.util.ArrayList;

/**
 * Created by Juraj on 19.11.2017.
 */

public class GridViewAdapterCartItems extends ArrayAdapter<CartItem> {
    private ArrayList<CartItem> data;
    private LayoutInflater inflater;
    private TextView text;

    public void setData(ArrayList<CartItem> data) {
        this.data = data;
    }

    public GridViewAdapterCartItems(@NonNull Context context, @LayoutRes int resource, ArrayList<CartItem> data) {
        super(context, resource,data);
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = convertView;
        if (item == null) {
            inflater = ((MainActivity) getContext()).getLayoutInflater();
            item = inflater.inflate(R.layout.template_cart_items, parent, false);
            final View finalitem = item;
            text = item.findViewById(R.id.tv_cartItems_text_small);
            text.setText("");
            text = item.findViewById(R.id.tv_cartItems_title_small);
            text.setText(data.get(position).getName());
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalitem.setBackgroundColor(Color.GRAY);
                    finalitem.setEnabled(false);
                }
            });
        } else {

        }
        return item;
    }

    @Override
    public int getCount() {
         return data.size();
    }
}
