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
import com.example.juraj.note.data.Note;

import java.util.ArrayList;

/**
 * Created by Juraj on 19.11.2017.
 */

public class GridViewAdapter extends ArrayAdapter<Note> {
    private ArrayList<Note> data = new ArrayList<>();

    public GridViewAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Note> objects) {
        super(context, resource, objects);
        data = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = convertView;
        if (item == null) {
            LayoutInflater inflater = ((MainActivity) getContext()).getLayoutInflater();
            item = inflater.inflate(R.layout.template_note, parent, false);
            TextView text = item.findViewById(R.id.tv_note_text_small);
            text.setText(data.get(position).getText());
        } else {

        }
        return item;
    }
}
