package wangzq.cn;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import java.io.IOException;

public class FloatWindowService extends Service {

    public FloatWindowService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        createFloatView();

    }


    private void createFloatView() {

        final WindowManager wm;

        final SurfaceView surfaceView = new SurfaceView(getApplicationContext());

        wm = (WindowManager) getApplicationContext().getSystemService(
            Context.WINDOW_SERVICE);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        // 设置window type
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        /*
         * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
         * 即拉下通知栏不可见
         */

        params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

        // 设置Window flag
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        /*
         * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
         * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
         * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
         */

        // 设置悬浮窗的长宽
        params.width = 300;
        params.height = 200;

        // 设置悬浮窗的Touch监听
        surfaceView.setOnTouchListener(new OnTouchListener() {
            int lastX, lastY;
            int paramX, paramY;

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        paramX = params.x;
                        paramY = params.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        params.x = paramX + dx;
                        params.y = paramY + dy;
                        // 更新悬浮窗位置
                        wm.updateViewLayout(surfaceView, params);
                        break;
                }
                return true;
            }
        });


        SurfaceHolder mSurfaceHolder = surfaceView.getHolder();
        // 设置 Surface 格式
        // 参数： PixelFormat中定义的 int 值 ,详细参见 PixelFormat.java
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        // 添加 Surface 的 callback 接口
        mSurfaceHolder.addCallback(new Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {


                Camera camera = Camera.open();//打开硬件摄像头，这里导包得时候一定要注意是android.hardware.Camera
                //设置角度
//                setCameraDisplayOrientation(MainActivity.this,0,camera);
                try {
                    camera.setPreviewDisplay(holder);//通过SurfaceView显示取景画面
                } catch (IOException e) {
                    e.printStackTrace();
                }
                camera.startPreview();//开始预览

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });



        wm.addView(surfaceView, params);
//        isAdded = true;
    }


}
