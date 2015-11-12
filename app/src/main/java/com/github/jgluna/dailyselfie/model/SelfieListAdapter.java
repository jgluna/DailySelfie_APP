package com.github.jgluna.dailyselfie.model;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jgluna.dailyselfie.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class SelfieListAdapter extends ArrayAdapter<Selfie> {

    private final Context context;
    private SparseBooleanArray selectedSelfies;

    public SelfieListAdapter(Context context, List<Selfie> objects) {
        super(context, 0, objects);
        this.context = context;
        selectedSelfies = new SparseBooleanArray();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Selfie selfie = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.selfie_item, parent, false);
            holder.dateView = (TextView) convertView.findViewById(R.id.list_selfie_date);
            int measures = convertView.getWidth();
            holder.imageView = (ImageView) convertView.findViewById(R.id.list_selfie_image);
            holder.imageView.getLayoutParams().height = measures;
            holder.imageView.getLayoutParams().width = measures;
            holder.imageView.requestLayout();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(context).load(new File(selfie.getImagePath())).into(holder.imageView);
        holder.dateView.setText(selfie.getSelfieDate().toString());
        return convertView;
    }

    public void toggleSelection(int position) {
        selectView(position, !selectedSelfies.get(position));
    }

    public void removeSelection() {
        selectedSelfies = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            selectedSelfies.put(position, value);
        else
            selectedSelfies.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return selectedSelfies.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return selectedSelfies;
    }

    private static class ViewHolder {
        TextView dateView;
        ImageView imageView;
    }
}
