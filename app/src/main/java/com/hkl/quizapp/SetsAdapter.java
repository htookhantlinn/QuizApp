package com.hkl.quizapp;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Random;

public class SetsAdapter extends BaseAdapter {
    private int numberOfSets;


    public SetsAdapter(int numberOfSets) {
        this.numberOfSets = numberOfSets;
    }

    public SetsAdapter() {
    }

    @Override
    public int getCount() {
        return numberOfSets;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_item_layout, parent, false);

        } else {
            view = convertView;
        }
        ((TextView) view.findViewById(R.id.setItemLayout_txtView)).setText(String.valueOf(position + 1));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(parent.getContext(),QuestionActivity.class);
                intent.putExtra("SetNo",position+1);
                parent.getContext().startActivity(intent);            }
        });

        return view;
    }
}
