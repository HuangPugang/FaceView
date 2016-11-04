package com.hp.faceview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


public class FaceView extends View {
    private int status = 0;//0 普通   1成功  2失败
    public static final int NORMAL = 0;
    public static final int SUCCESS = 1;
    public static final int FAILED = 2;
    /**
     * 画笔对象的引用
     */
    private Paint paintArc;


    private Paint paintRing;//圆环


    private Paint paintEye; //眼睛

    private Paint paintMouth;//嘴巴

    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private int progress = 0;

    private boolean finish = false;


    private int eyeRadius = 1;


    public FaceView(Context context) {
        this(context, null);
    }

    public FaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paintRing = new Paint();
        paintArc = new Paint();
        paintEye = new Paint();
        paintMouth = new Paint();

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.FaceView);

        //获取自定义属性和默认值
        roundColor = mTypedArray.getColor(R.styleable.FaceView_roundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(R.styleable.FaceView_roundProgressColor, Color.GREEN);
        roundWidth = mTypedArray.getDimension(R.styleable.FaceView_roundWidth, 5);
        max = mTypedArray.getInteger(R.styleable.FaceView_max, 100);

        mTypedArray.recycle();
    }


    private void initPaint() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 画最外层的大圆环
         */
        int centre = getWidth() / 2; //获取圆心的x坐标
        int quarter = getWidth() / 4;
        int third = getWidth() / 3;
        int radius = (int) (centre - roundWidth / 2); //圆环的半径


        paintRing.setColor(roundColor); //设置圆环的颜色
        paintRing.setStyle(Paint.Style.STROKE); //设置空心
        paintRing.setStrokeWidth(roundWidth); //设置圆环的宽度
        paintRing.setAntiAlias(true);  //消除锯齿

        paintArc.setColor(roundColor); //设置圆环的颜色
        paintArc.setStyle(Paint.Style.STROKE); //设置空心
        paintArc.setStrokeWidth(roundWidth); //设置圆环的宽度
        paintArc.setAntiAlias(true);  //消除锯齿
        paintArc.setStrokeWidth(roundWidth); //设置圆环的宽度

        if (status == FAILED) {
            paintArc.setColor(roundColor);  //设置进度的颜色
        } else {
            paintArc.setColor(roundProgressColor);  //设置进度的颜色
        }

        if (status == FAILED) {
            paintEye.setColor(roundColor);
        } else {
            paintEye.setColor(roundProgressColor);
        }
        paintEye.setStyle(Paint.Style.FILL);
        paintEye.setAntiAlias(true);


        if (status == FAILED) {
            paintMouth.setColor(roundColor);
        } else {
            paintMouth.setColor(roundProgressColor);
        }
        paintMouth.setStyle(Paint.Style.STROKE); //设置空心
        paintMouth.setStrokeWidth(roundWidth); //设置圆环的宽度
        paintMouth.setAntiAlias(true);

        canvas.drawCircle(centre, centre, radius, paintRing); //画出圆环

        //设置进度是实心还是空心
        RectF oval = new RectF(centre - radius, centre - radius, centre
                + radius, centre + radius);  //用于定义的圆弧的形状和大小的界限


        RectF ovalMouthSuccess = new RectF((float) (centre - radius * 0.70), (float) (centre - radius * 0.70),
                (float) (centre + radius * 0.70), (float) (centre + radius * 0.70));


        RectF ovalMouthFailed = new RectF(centre - radius / 2, centre,
                centre + radius / 2, getWidth());

        int current = 360 * progress / max;

        if (status == NORMAL) {
            if (current >= 270) {
                canvas.drawArc(oval, current - 270, 270, false, paintArc);
            } else {
                canvas.drawArc(oval, 0, current, false, paintArc);  //根据进度画圆弧
            }

            postDelayed(new Runnable() {
                @Override
                public void run() {
                    progress++;
                    postInvalidate();

                }
            }, 1);
        } else if (status == SUCCESS) {
            canvas.drawArc(oval, 0, 360, false, paintArc);

            canvas.drawCircle(quarter, third, eyeRadius, paintEye);
            canvas.drawCircle(getWidth() - quarter, third, eyeRadius, paintEye);

            canvas.drawArc(ovalMouthSuccess, 0, 10 * eyeRadius, false, paintMouth);
            if (!finish) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        eyeRadius++;
                        if (eyeRadius == 18) {
                            finish = true;
                        }
                        postInvalidate();

                    }
                }, 1);

            }

        } else if (status == FAILED) {
            canvas.drawArc(oval, 0, 360, false, paintArc);

            canvas.drawCircle(quarter, third, eyeRadius, paintEye);
            canvas.drawCircle(getWidth() - quarter, third, eyeRadius, paintEye);

            canvas.drawArc(ovalMouthFailed, -150, (float) (eyeRadius * 6.666666), false, paintMouth);
            if (!finish) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        eyeRadius++;
                        if (eyeRadius == 18) {
                            finish = true;
                        }
                        postInvalidate();

                    }
                }, 1);

            }
        }


    }


    public void setStatus(int status1) {
        this.status = status1;
        if (status1 == SUCCESS) {
            postInvalidate();
        } else if (status1 == FAILED) {
            paintMouth.setColor(roundColor);
            paintArc.setColor(roundColor);
            paintEye.setColor(roundColor);
        }

    }


    public void reset() {
        status = NORMAL;
        progress = 0;
        eyeRadius = 1;
        postInvalidate();
        finish = false;
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }
}
