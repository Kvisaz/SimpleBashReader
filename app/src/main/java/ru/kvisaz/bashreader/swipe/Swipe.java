package ru.kvisaz.bashreader.swipe;

/**
 *  Simple Swipe model
 *   use start on touch
 *   use finish on release
 *   use block before long action after swipe
 *   use unblock after long action
 */
public class Swipe {
    public boolean isLeft;
    public boolean isRight;
    public boolean isBlocked;

    private float x, y;
    private final float kWork = 1.5f;
    private final int minDistance = 20;

    public float dx;
    public float dy;

    public Swipe(){

     }

    public void block(){
        isBlocked = true;
    }

    public void let(){
        isBlocked = false;
    }

    public void start(float x, float y){
        reset();

        this.x = x;
        this.y = y;
    }

    public void finish(float x, float y){
        dx = x - this.x;
        dy = y - this.y;

        float absDx = Math.abs(dx);
        float absDy = Math.abs(dy);

        if(absDx < minDistance && absDy < minDistance)
        {
            reset();
            return;
        }

        float kXY = absDx/absDy;
        if(kXY < kWork)  // vertical, no used
        {
            reset();
            return;
        }

        // horizontal swipe
        if(dx > 0) isRight = true;
        else isLeft = true;

    }

    private void reset(){
        isLeft = false;
        isRight = false;
    }

}
