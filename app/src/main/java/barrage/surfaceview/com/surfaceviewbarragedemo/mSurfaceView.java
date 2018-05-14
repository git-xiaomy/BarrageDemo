package barrage.surfaceview.com.surfaceviewbarragedemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class mSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    //用于标注线程是否继续
    private boolean Flag=true;

    //SurfaceHolder
    SurfaceHolder surfaceHolder;

    //弹幕的集合
    public List<mText> Barrages=new ArrayList<>();

    //用于随机生成弹幕的Y轴坐标
    Random random=new Random();

    public mSurfaceView(Context context) {
        super(context);
    }

    public mSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        surfaceHolder=getHolder();
        surfaceHolder.addCallback(this);

        //设置背景透明
        this.setZOrderOnTop(true);
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
    }


    public mSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }




    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Flag=true;

        //启动线程绘制雨滴
        new Thread(this).start();

    }

    //添加弹幕
    public void add(mText mText) {
        if (mText.getX()==0){
            mText.setX(getWidth());
        }
        if (mText.getY()==0){
            int i=getHeight();
            if (i>0){
                mText.setY(random.nextInt(i));
            }

        }
        Barrages.add(mText);
    }


    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Flag=false;
    }


    @Override
    public void run() {
        Canvas canvas=null;
        mText mtext = null;
        Paint paint=null;
        while (Flag){
            //如果集合为0，则跳过本次循环
            if (Barrages.size()==0){
                continue;
            }
            try {
                //获取画布
                canvas=surfaceHolder.lockCanvas();
                //清空画布
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            }catch (Exception e){
                e.printStackTrace();
                break;
            }


            //遍历弹幕集合
            for (int i = 0; i < Barrages.size(); i++) {
                mtext=Barrages.get(i);
                //如果弹幕类中的画笔为空,则在此处定义画笔，根据弹幕类中的颜色大小等进行绘制
                if (mtext.getPaint()==null){
                    if (paint==null){
                        paint=new Paint();
                    }
                    paint.setColor(mtext.getColor());
                    paint.setTextSize(mtext.getSize());
                    paint.setStrokeWidth(3f);
                }else {
                    //如果弹幕类中的画笔不为空，则直接用弹幕类中的画笔绘制
                    paint=mtext.getPaint();
                }
                //绘制文本
                canvas.drawText(mtext.getText(),mtext.getX(),mtext.getY(),paint);
                //如果弹幕超出屏幕左侧，则从集合中删除，否则进行移动
                if (mtext.getX()<-getWidth()){
                    Barrages.remove(mtext);
                }else {
                    mtext.setX((mtext.getX()-mtext.getSpeed()));
                }
            }

            //解锁画布
            surfaceHolder.unlockCanvasAndPost(canvas);

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
