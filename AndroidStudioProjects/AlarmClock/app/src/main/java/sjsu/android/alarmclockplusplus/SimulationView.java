package sjsu.android.alarmclockplusplus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.core.view.GestureDetectorCompat;

public class SimulationView extends View {

    private Display mDisplay;
    private ArrowParticle arrow;
    private int scoreTracker;
    private GestureDetectorCompat mDetector;
    private Direction direction;
    private long timestamp;
    private float mVelocityX;
    private float mVelocityY;
    private float velocityScale;
    private boolean readingInput;
    private Context context;
    private Bundle myInput;
    private static int GOAL = 10;

    public SimulationView(Context context, Bundle myInput) {
        super(context);
        this.context = context;
        this.setLongClickable(true);
        this.myInput = myInput;
        mDetector = new GestureDetectorCompat(context, new MyGestureListener());
        direction = Direction.getRandomDirection();
        WindowManager windowManger = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = windowManger.getDefaultDisplay();
        arrow = new ArrowParticle(getResources(), mDisplay);
        scoreTracker = 0;
        mVelocityX = 0;
        mVelocityY = 0;
        scoreTracker = 0;
        readingInput = true;
        velocityScale = mDisplay.getHeight()/3000000.0f;
        timestamp = System.currentTimeMillis();
    }

    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        arrow.updatePosition(mVelocityX, mVelocityY, 0, timestamp);
        if (arrow.checkCollision(arrow.getOriginX() + arrow.getWidth(), arrow.getOriginY() + arrow.getHeight())){
            direction = Direction.getRandomDirection();
            readingInput = true;
            mVelocityX = 0;
            mVelocityY = 0;
        }
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);
        arrow.drawParticle(canvas, direction, mDisplay);
        paint.setColor(Color.WHITE);
        paint.setTextSize(40);
        canvas.drawText("Goal: " + String.valueOf(GOAL), 10, 45, paint);
        canvas.drawText("Score: " + String.valueOf(scoreTracker), 10, 85, paint);
        invalidate();
    }
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            if (readingInput) {
                boolean pass = false;
                if (velocityX > 0 && velocityX > Math.abs(velocityY)) {
                    if (direction == Direction.RIGHT) {
                        pass = true;
                        mVelocityX = -velocityScale;
                        mVelocityY = 0;
                    }
                } else if (velocityX < 0 && Math.abs(velocityX) > Math.abs(velocityY)) {
                    if (direction == Direction.LEFT) {
                        pass = true;
                        mVelocityX = velocityScale;
                        mVelocityY = 0;
                    }
                } else if (velocityY > 0 && velocityY > Math.abs(velocityX)) {
                    if (direction == Direction.DOWN) {
                        pass = true;
                        mVelocityX = 0;
                        mVelocityY = velocityScale;
                    }
                } else if (velocityY < 0 && Math.abs(velocityY) > Math.abs(velocityX)) {
                    if (direction == Direction.UP) {
                        pass = true;
                        mVelocityX = 0;
                        mVelocityY = -velocityScale;
                    }
                }
                if (pass) {
                    timestamp = System.currentTimeMillis() - timestamp;
                    scoreTracker++;
                    readingInput = false;
                    if (scoreTracker >= GOAL){
                        Intent myIntent = new Intent(context, SnoozeActivity.class);
                        myIntent.putExtra(AlarmListDisplayActivity.ALARM_SNOOZE_TIME,  myInput.getInt(AlarmListDisplayActivity.ALARM_SNOOZE_TIME));
                        myIntent.putExtra(AlarmListDisplayActivity.ALARM_RING_PATH,  myInput.getString(AlarmListDisplayActivity.ALARM_RING_PATH));
                        myIntent.putExtra(AlarmListDisplayActivity.ALARM_ID, myInput.getInt(AlarmListDisplayActivity.ALARM_ID));
                        myIntent.putExtra(AlarmListDisplayActivity.ALARM_VIBRATION, myInput.getBoolean(AlarmListDisplayActivity.ALARM_VIBRATION));
                        myIntent.putExtra(AlarmListDisplayActivity.ALARM_MINIGAME, myInput.getBoolean(AlarmListDisplayActivity.ALARM_MINIGAME));
                        myIntent.putExtra(AlarmListDisplayActivity.ALARM_REPEAT_DAYS, myInput.getString(AlarmListDisplayActivity.ALARM_REPEAT_DAYS));
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(myIntent);
                    }
                }
            }
            return true;
        }
    }
}
