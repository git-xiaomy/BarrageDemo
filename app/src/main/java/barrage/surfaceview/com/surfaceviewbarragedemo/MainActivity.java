package barrage.surfaceview.com.surfaceviewbarragedemo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.Random;

public class MainActivity extends Activity {

    private mSurfaceView msurfaceView;
    private VideoView videoView;
    private Random random=new Random();

    private String[] strings={"6666","厉害了我的国","加油！！！","欢迎收看晨间新闻","程序猿很苦逼","我能怎么办，我也很无奈"};

    private int[] colors={Color.WHITE,Color.MAGENTA,Color.CYAN,Color.RED,Color.BLUE,Color.GREEN};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        msurfaceView = findViewById(R.id.msv);
        videoView = findViewById(R.id.mvv);

        //申请播放网络视频权限
        per();

        startVideo();

        //动态生成弹幕
        startBarrage();
    }

    //生成弹幕
    private void startBarrage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    mText mText=new mText();
                    mText.setText(strings[random.nextInt(strings.length)]);
                    mText.setSpeed(3);
                    mText.setColor(colors[random.nextInt(colors.length)]);
                    mText.setSize(40);
                    msurfaceView.add(mText);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    //申请视频播放权限
    private void per() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {//检查是否有了权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                //没有权限即动态申请
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }

        }

        //开始播放视频
    private void startVideo() {
            String url1 = "http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4";

            Uri uri=Uri.parse(url1);

            //设置视频控制器
            videoView.setMediaController(new MediaController(this));


            //设置视频路径
            videoView.setVideoURI(uri);

            //开始播放视频
            videoView.start();

        }
}
