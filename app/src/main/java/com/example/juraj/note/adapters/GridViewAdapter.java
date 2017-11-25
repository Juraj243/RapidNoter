package com.example.juraj.note.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.juraj.note.data.DaoSession;
import com.example.juraj.note.data.Note;
import com.example.juraj.note.data.SessionManager;

import java.util.ArrayList;

/**
 * Created by Juraj on 19.11.2017.
 */

public class GridViewAdapter extends ArrayAdapter<Note> {
    private ArrayList<Note> data;
    private LayoutInflater inflater;
    private TextView text;

    public void setData(ArrayList<Note> data) {
        this.data = data;
    }

    public GridViewAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<Note> data) {
        super(context, resource, data);
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = convertView;
        if (item == null) {
            inflater = ((MainActivity) getContext()).getLayoutInflater();
            item = inflater.inflate(R.layout.template_note, parent, false);
            text = item.findViewById(R.id.tv_note_text_small);
            text.setText(data.get(position).getText());
            text = item.findViewById(R.id.tv_note_title_small);
            text.setText(data.get(position).getTitle());
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Note note = data.get(position);
                    showDetailFragmentDialog(view, note);
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

    private void showDetailFragmentDialog(View view, Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View v = inflater.inflate(R.layout.note_detail_fragment, null);
        ((TextView) v.findViewById(R.id.tv_detail_text)).setText(note.getText());
        if (note.getDate_from() != null) {

        }
        builder.setTitle(note.getTitle())
                .setView(v)
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNeutralButton("Cancel", null);
        builder.show();
    }
}
