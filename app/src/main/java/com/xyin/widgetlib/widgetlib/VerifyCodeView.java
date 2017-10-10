package com.xyin.widgetlib.widgetlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xyin.widgetlib.R;

/**
 * Created by Administrator on 2017/10/9 0009.
 */

public class VerifyCodeView extends View {

    private final Paint mPaint2;
    private int num;    //box长度
    private int mBorderColor;    //描边的颜色
    private int mBorderWidth;    //描边的宽度
    private int mBoxSize;  //每个格子的大小
    private int mBoxSpace;  //格子间的间距
    private int mTextColor;
    private int mTextSize;
    private int mTextL; //单个数字所确定的正方形边长
    private float mBaseLineH;

    private Paint mPaint;

    private StringBuilder codeBuilder;


    public VerifyCodeView(Context context) {
        this(context, null);
    }

    public VerifyCodeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerifyCodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        codeBuilder = new StringBuilder(6);
        codeBuilder.append(0);
        codeBuilder.append(3);
        codeBuilder.append(2);

        //获取属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerifyCodeView, defStyleAttr, 0);
        mTextSize = a.getDimensionPixelOffset(R.styleable.VerifyCodeView_text_size, 14);
        mBoxSpace = a.getDimensionPixelOffset(R.styleable.VerifyCodeView_box_space, 2);
        int mBoxPadding = a.getDimensionPixelOffset(R.styleable.VerifyCodeView_box_padding, 0); //格子的内间距
        num = a.getInteger(R.styleable.VerifyCodeView_box_num, 1);
        mBorderColor = a.getColor(R.styleable.VerifyCodeView_border_color, 0xFF000000);
        mBorderWidth = a.getDimensionPixelOffset(R.styleable.VerifyCodeView_border_width, 2);
        mTextColor = a.getColor(R.styleable.VerifyCodeView_text_color, 0xFF000000);
        a.recycle();

        //计算大小box大小
        Paint paint = new Paint();
        paint.setTextSize(mTextSize);
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float height = fontMetrics.bottom - fontMetrics.top;
        float width = paint.measureText("0");
        mTextL = (int) Math.max(width, height);
        mBaseLineH = Math.abs(fontMetrics.top);
        mBoxSize = mTextL + mBoxPadding + mBorderWidth;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint2.setColor(Color.BLUE);

        Log.e("TAG", "height " + height + "width" + width);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制矩形框
        mPaint.setColor(mBorderColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderWidth);

        float right;
        float offset = mBorderWidth / 2;    //因为存在外边框所以矩形的起始点应该以边框的中心线为准,即绘制边框的中心线围成的矩形
        for (int i = 0; i < num; i++) {
            right = i * (mBoxSize + mBoxSpace);
            canvas.drawRect(right + offset, offset, right + mBoxSize - offset, mBoxSize - offset, mPaint);
        }

        //绘制验证码
        if (codeBuilder.length() > 0) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mTextColor);
            mPaint.setTextSize(mTextSize);
            offset = (mBoxSize - mTextL) / 2;
            float y = offset + mBaseLineH;
            char[] codeStr = codeBuilder.toString().toCharArray();
            for (int i = 0; i < codeStr.length; i++) {
                right = i * (mBoxSize + mBoxSpace);
                canvas.drawRect(right + offset,offset,right + mTextL,offset+mTextL,mPaint2);
                canvas.drawText(codeStr, i, 1, right + offset, y, mPaint);
            }
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //根据box的个数来确定大小
        setMeasuredDimension(mBoxSize * num + num * mBoxSpace - mBoxSpace, mBoxSize);
    }

    public String getVerifyCodeStr() {
        return codeBuilder.toString();
    }


}
