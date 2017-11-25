package com.example.juraj.note.adapters;

import android.content.Context;
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
import com.example.juraj.note.data.Cart;
import com.example.juraj.note.data.CartItem;

import java.util.ArrayList;
import java.util.List;

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
            text.setText(setDataText(data.get(position).getCartItems()));
            text = item.findViewById(R.id.tv_cart_title_small);
            text.setText(data.get(position).getName());
            final int finalPosition = position;

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) getContext()).showCartItemsFragment((ArrayList<CartItem>) data.get(finalPosition).getCartItems());
                }
            });
        } else {

        }
        return item;
    }

    private String setDataText(List<CartItem> items){
        StringBuilder sb = new StringBuilder();
        if(items.size() < 3){
            for (CartItem item : items){
                sb.append(item.getName() + "\n");
            }
        }
        else {
            for (int j = 0; j < 3; j++) {
                sb.append(items.get(j).getName() + "\n");
            }
            sb.append("...");
        }
        return sb.toString();
    }

    @Override
    public int getCount() {
         return data.size();
    }
}
