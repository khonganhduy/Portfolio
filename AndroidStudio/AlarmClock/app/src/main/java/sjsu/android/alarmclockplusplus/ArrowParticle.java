package sjsu.android.alarmclockplusplus;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.Display;

public class ArrowParticle {
    private float mPosX;
    private float mPosY;
    private float mVelX;
    private float mVelY;
    private int originX;
    private int originY;
    private Bitmap upArrow;
    private Bitmap downArrow;
    private Bitmap leftArrow;
    private Bitmap rightArrow;
    private int width;
    private int height;

    public ArrowParticle(Resources resources, Display display){
        Bitmap temp = BitmapFactory.decodeResource(resources, R.drawable.up_arrow);
        upArrow = Bitmap.createScaledBitmap(temp, display.getWidth()/2, display.getHeight(), true);
        temp = BitmapFactory.decodeResource(resources, R.drawable.down_arrow);
        downArrow = Bitmap.createScaledBitmap(temp, display.getWidth()/2, display.getHeight(), true);
        temp = BitmapFactory.decodeResource(resources, R.drawable.left_arrow);
        leftArrow = Bitmap.createScaledBitmap(temp, display.getWidth(), display.getHeight()/2, true);
        temp = BitmapFactory.decodeResource(resources, R.drawable.right_arrow);
        rightArrow = Bitmap.createScaledBitmap(temp, display.getWidth(), display.getHeight()/2, true);
        originX = display.getWidth()/2;
        originY = display.getHeight()/2;

    }

    public void updatePosition(float sx, float sy, float sz, long timestamp){
        float dt = (System.nanoTime() - timestamp) /1000000000.0f;
        mVelX += -sx;
        mVelY += -sy;
        mPosX += mVelX * dt;
        mPosY += mVelY * dt;
    }

    public boolean checkCollision(float horizontalBound, float verticalBound){
        if(mPosX > horizontalBound || mPosX < -horizontalBound || mPosY > verticalBound || mPosY < -verticalBound){
            mPosX = 0;
            mPosY = 0;
            mVelX = 0;
            mVelY = 0;
            return true;
        }
        return false;
    }

    public void drawParticle(Canvas canvas, Direction direction, Display display){
        Bitmap temp = rightArrow;
        width = display.getWidth();
        height = display.getHeight()/2;
        if (direction == Direction.LEFT){
            temp = leftArrow;
            width = display.getWidth();
            height = display.getHeight()/2;
        }
        else if (direction == Direction.DOWN){
            temp = downArrow;
            width = display.getWidth()/2;
            height = display.getHeight();
        }
        else if (direction == Direction.UP){
            temp = upArrow;
            width = display.getWidth()/2;
            height = display.getHeight();
        }
        canvas.drawBitmap(temp, (originX - width/2) + mPosX, (originY - height/2) - mPosY, null);
    }

    public int getOriginX() {
        return originX;
    }

    public int getOriginY() {
        return originY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
