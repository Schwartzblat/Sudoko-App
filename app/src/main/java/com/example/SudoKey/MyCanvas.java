package com.example.SudoKey;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MyCanvas extends View {
    private int [][] board;
    public MyCanvas(Context context, int [][] board) {
        super(context);
        this.board =board;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = 900;
        int height = 900;
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(30);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, width, height, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, width+3, height+3, paint);

        // draw board:
        for (int i = 1; i < 10; i++) {
            if (i % 3 == 0) {
                paint.setStrokeWidth(8);
                canvas.drawRect(100 * i, 0, 10, 900, paint);
                canvas.drawRect(0, 100 * i, 900, 10, paint);
            } else {
                paint.setStrokeWidth(3);
                canvas.drawRect(100 * i, 0, 3, 900, paint);
                canvas.drawRect(0, 100 * i, 900, 3, paint);
            }
        }
        paint.setStyle(Paint.Style.FILL);
        //draw numbers:
        /*Typeface tf = Typeface.createFromAsset(context.getAssets(), "Sketch.ttf");
        paint.setTypeface(tf);*/
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(board[i][j]!=0) {
                    paint.setTextSize(40);
                    canvas.drawText(String.valueOf(board[i][j]), 45 + 100 * j, 68 + 100 * i, paint);
                }
            }
        }
    }
}