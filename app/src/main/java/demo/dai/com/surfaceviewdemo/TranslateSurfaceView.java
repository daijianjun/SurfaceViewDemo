package demo.dai.com.surfaceviewdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by dai on 2017/7/18.
 */

public class TranslateSurfaceView extends SurfaceView implements DrawInterface {
    private DrawHandler drawHandler;
    private int width;
    private int height;
    private Bitmap bitmap;
    private int bitmapWidth;
    private int bitmapHeight;
    private Toast toast;
    private int count = 0;

    private ArrayList<MoveModel> moveList = new ArrayList<>();

    public TranslateSurfaceView(Context context) {
        this(context, null);
    }

    public TranslateSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TranslateSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        SurfaceHolder holder = getHolder();
        setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    public void prepare() {
        DrawThread drawThread = new DrawThread();
        drawThread.start();
        drawHandler = new DrawHandler(drawThread.getLooper(), this);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_redenvelope2);
        bitmapWidth = bitmap.getWidth();
        bitmapHeight = bitmap.getHeight();

    }

    public void addMoveModel(MoveModel moveModel) {
        moveList.add(moveModel);
    }

    public void start() {
        count = 0;
        moveList.clear();
        for (int i = 0; i < 10; i++) {
            generateModel();
        }
        drawHandler.sendEmptyMessage(DrawHandler.START_DRAW_KEY);
    }

    public void resume() {
        drawHandler.sendEmptyMessage(DrawHandler.START_DRAW_KEY);
    }

    public void pause() {
        drawHandler.sendEmptyMessage(DrawHandler.STOP_DRAW_KEY);
    }

    public void quit() {
        if (null != drawHandler) {
            drawHandler.removeCallbacksAndMessages(null);
            drawHandler.getLooper().quit();
        }
    }

    @Override
    public void startDraw() {
        SurfaceHolder holder = getHolder();
        Canvas canvas = holder.lockCanvas();
        if (null == canvas) {
            return;
        }

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        for (MoveModel moveModel : moveList) {
            canvas.drawBitmap(bitmap, moveModel.x, moveModel.y, paint);
            if (moveModel.x > width || moveModel.y > height) {
                resetMoveModel(moveModel);
            } else {
                moveModel.y += moveModel.randomY;
            }
        }
        holder.unlockCanvasAndPost(canvas);
        drawHandler.sendEmptyMessage(DrawHandler.START_DRAW_KEY);
    }

    @Override
    public void stopDraw() {
        drawHandler.removeMessages(DrawHandler.START_DRAW_KEY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                checkInRect((int) event.getX(), (int) event.getY());
                break;
        }
        return true;
    }

    /**
     * 是否点击在红包区域
     * @param x
     * @param y
     */
    private void checkInRect(int x, int y) {
        int length = moveList.size();
        for (int i = 0; i < length; i++) {
            MoveModel moveModel = moveList.get(i);
            Rect rect = new Rect((int) moveModel.x, (int) moveModel.y, (int) moveModel.x + bitmapWidth, (int) moveModel.y + bitmapHeight);
            if (rect.contains(x, y)) {
                count++;
                resetMoveModel(moveModel);
                if (toast == null) {
                    toast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
                }
                toast.setText("抢到红包" + count);
                toast.show();
                break;
            }
        }
    }

    private void resetMoveModel(MoveModel moveModel) {
        Random random = new Random();
        moveModel.x = random.nextInt(11) * (width / 10);
        moveModel.y = 0;
        moveModel.randomY = (random.nextInt(5) + 2) * getResources().getDisplayMetrics().density * 1.4f;
    }

    private void generateModel() {
        Random random = new Random();
        MoveModel moveModel = new MoveModel();
        moveModel.moveId = moveList.size() + 1;
        moveModel.x = random.nextInt(11) * (width / 10);
        moveModel.y = 0;
        moveModel.randomY = (random.nextInt(5) + 2) * getResources().getDisplayMetrics().density * 1.4f;
        moveList.add(moveModel);
    }
}
