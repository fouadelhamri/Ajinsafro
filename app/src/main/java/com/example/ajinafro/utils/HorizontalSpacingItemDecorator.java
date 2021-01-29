package com.example.ajinafro.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class HorizontalSpacingItemDecorator extends RecyclerView.ItemDecoration {
    private final int horizontalSpaceHeight;

    public HorizontalSpacingItemDecorator(int verticalSpaceHeight) {
        this.horizontalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {

        outRect.right= horizontalSpaceHeight;
    }
}
