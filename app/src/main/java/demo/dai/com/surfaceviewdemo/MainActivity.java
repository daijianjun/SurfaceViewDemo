package demo.dai.com.surfaceviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    TranslateSurfaceView translateSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        translateSurfaceView = (TranslateSurfaceView) findViewById(R.id.translate_view);

        translateSurfaceView.prepare();

        findViewById(R.id.start_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                translateSurfaceView.start();
            }
        });

        findViewById(R.id.resume_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                translateSurfaceView.resume();
            }
        });

        findViewById(R.id.pause_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                translateSurfaceView.pause();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        translateSurfaceView.pause();
    }

    @Override
    protected void onDestroy() {
        translateSurfaceView.quit();
        super.onDestroy();
    }
}
