package com.xyin.widgetlib.widgetlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import com.xyin.widgetlib.R;

/**
 * Created by Administrator on 2017/10/9 0009.
 */

public class VerifyCodeView extends View {

    private int boxNum;    //box长度
    private int borderColor;    //描边的颜色
    private float borderWidth;    //描边的宽度
    private float boxSize;  //每个格子的大小
    private float boxSpace;  //格子间的间距
    private int textColor;
    private float textSize;
    private float baseLineH;
    private float textWidth;
    private float textHeight;
    private int boxBackground;
    private float boxRadius;

    private RectF mRectF;   //圆角矩形框
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
        setFocusableInTouchMode(true);  //能获取焦点才能弹出软键盘

        //获取属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerifyCodeView, defStyleAttr, 0);
        textSize = a.getDimension(R.styleable.VerifyCodeView_text_size, 14);
        boxSpace = a.getDimension(R.styleable.VerifyCodeView_box_space, 2);
        float boxPadding = a.getDimension(R.styleable.VerifyCodeView_box_padding, 0);
        boxNum = a.getInteger(R.styleable.VerifyCodeView_box_num, 1);
        borderColor = a.getColor(R.styleable.VerifyCodeView_border_color, 0xFF000000);
        borderWidth = a.getDimension(R.styleable.VerifyCodeView_border_width, 2);
        textColor = a.getColor(R.styleable.VerifyCodeView_text_color, 0xFF000000);
        boxBackground = a.getColor(R.styleable.VerifyCodeView_box_background, 0xFFFFFFFF);
        boxRadius = a.getDimension(R.styleable.VerifyCodeView_border_radius, 0);
        a.recycle();

        //计算大小box大小
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        textHeight = fontMetrics.bottom - fontMetrics.top;
        textWidth = paint.measureText("0");
        baseLineH = Math.abs(fontMetrics.top);
        boxSize = Math.max(textHeight, textWidth) + boxPadding + borderWidth;

        codeBuilder = new StringBuilder(boxNum);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectF = new RectF();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制矩形框
        mPaint.setColor(borderColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(borderWidth);

        float right;
        //因为存在外边框所以矩形的起始点应该以边框的中心线为准,即绘制边框的中心线围成的矩形
        float offset = borderWidth / 2;
        for (int i = 0; i < boxNum; i++) {
            right = i * (boxSize + boxSpace);

            //底色
            mPaint.setColor(boxBackground);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(right + borderWidth, borderWidth, right + boxSize - borderWidth, boxSize - borderWidth, mPaint);

            //圆角边框
            mPaint.setColor(borderColor);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(borderWidth);
            mRectF.set(right + offset, offset, right + boxSize - offset, boxSize - offset);
            canvas.drawRoundRect(mRectF, boxRadius, boxRadius, mPaint);

        }

        //绘制验证码
        if (codeBuilder.length() > 0) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(textColor);
            mPaint.setTextSize(textSize);
            float y = (boxSize - textHeight) / 2 + baseLineH;
            offset = (boxSize - textWidth) / 2;
            char[] codeStr = codeBuilder.toString().toCharArray();
            for (int i = 0; i < codeStr.length; i++) {
                right = i * (boxSize + boxSpace);
                canvas.drawText(codeStr, i, 1, right + offset, y, mPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //根据box的个数来确定大小
        setMeasuredDimension((int) (boxSize * boxNum + boxNum * boxSpace - boxSpace), (int) boxSize);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //接收按键事件，67是删除键(backspace),7-16就是0-9
        if (keyCode == 67 && codeBuilder.length() > 0) {
            codeBuilder.deleteCharAt(codeBuilder.length() - 1);
            invalidate();
        } else if (keyCode >= 7 && keyCode <= 16 && codeBuilder.length() < boxNum) {
            codeBuilder.append(keyCode - 7);
            invalidate();
        }
        if (codeBuilder.length() >= boxNum) {
            //达到位数自动隐藏键盘
            InputMethodManager imm = (InputMethodManager) getContext().
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //在View上点击时弹出软键盘
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.viewClicked(this);
        imm.showSoftInput(this, 0);
        return super.onTouchEvent(event);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.inputType = InputType.TYPE_CLASS_NUMBER;   //定义软键盘样式为数字键盘
        return super.onCreateInputConnection(outAttrs);
    }

    public String getVerifyCodeStr() {
        return codeBuilder.toString();
    }


}
