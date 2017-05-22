package com.example.justakiss.stoberriibeautymusic.custom;

/**
 * Created by justakiss on 30/09/2016.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.justakiss.stoberriibeautymusic.R;
import com.example.justakiss.stoberriibeautymusic.activity.MainScreen;
import com.example.justakiss.stoberriibeautymusic.activity.PlayScreen;

/**
 * The Class CircularSeekBar.
 */
public class CircularSeekBar extends View {

    /** The context */
    private Context mContext;
    /** The listener to listen for changes */
    private OnSeekChangeListener mListener;
    /** The color of the progress ring */
    private Paint circleColor;
    private Paint circleColor2;
    /** the color of the inside circle. Acts as background color */
    private Paint innerColor;
    /** The progress circle ring background */
    private Paint circleRing;
    /** The angle of progress */
    private int angle = 0;
    /** The start angle (12 O'clock */
    private int startAngle = -90;
    /** The width of the progress ring */
    private int barWidth = 15;
    /** The width of the view */
    private int width;
    /** The height of the view */
    private int height;
    /** The maximum progress amount */
    private int maxProgress = 300;
    /** The current progress */
    private int progress;
    /** The progress percent */
    private int progressPercent;
    /** The radius of the inner circle */
    private float innerRadius;
    /** The radius of the outer circle */
    private float outerRadius;
    /** The circle's center X coordinate */
    private float cx;
    /** The circle's center Y coordinate */
    private float cy;
    /** The left bound for the circle RectF */
    private float left;
    /** The right bound for the circle RectF */
    private float right;
    /** The top bound for the circle RectF */
    private float top;
    /** The bottom bound for the circle RectF */
    private float bottom;
    /** The X coordinate for the top left corner of the marking drawable */
    private float dx;
    /** The Y coordinate for the top left corner of the marking drawable */
    private float dy;
    /** The X coordinate for 12 O'Clock */
    private float startPointX;
    /** The Y coordinate for 12 O'Clock */
    private float startPointY;

    /**
     * The X coordinate for the current position of the marker, pre adjustment
     * to center
     */
    private float markPointX;

    /**
     * The Y coordinate for the current position of the marker, pre adjustment
     * to center
     */
    private float markPointY;

    /**
     * The adjustment factor. This adds an adjustment of the specified size to
     * both sides of the progress bar, allowing touch events to be processed
     * more user-friendly (yes, I know that's not a word)
     */
    private float adjustmentFactor = 100;

    /** The progress mark when the view isn't being progress modified */
    private Bitmap progressMark;

    /** The progress mark when the view is being progress modified. */
    private Bitmap progressMarkPressed;

    /** The flag to see if view is pressed */
    private boolean IS_PRESSED = false;

    /**
     * The flag to see if the setProgress() method was called from our own
     * View's setAngle() method, or externally by a user.
     */
    private boolean CALLED_FROM_ANGLE = false;

    private boolean SHOW_SEEKBAR = true;

    /** The rectangle containing our circles and arcs. */
    private RectF rect = new RectF(), rectx = new RectF();
    BitmapShader shader = null;
    {
        mListener = new OnSeekChangeListener() {
            @Override
            public void onProgressChange(CircularSeekBar view, int newProgress) {

            }
        };

        circleColor = new Paint();
        circleColor2 = new Paint();
        innerColor = new Paint();
        circleRing = new Paint();

        circleColor.setColor(Color.parseColor("#cf3afce8")); // Set default
        circleColor2.setColor(Color.parseColor("#cf3afce8")); // Set default
        // progress
        // color to holo
        // blue.
        innerColor.setColor(Color.parseColor("#0096f22d")); // Set default background color to
        // black
        circleRing.setColor(Color.parseColor("#0096f22d"));// Set default background color to Gray

        circleColor.setAntiAlias(true);
        circleColor2.setAntiAlias(true);
        innerColor.setAntiAlias(true);
        circleRing.setAntiAlias(true);

        circleColor.setStrokeWidth(7);
        circleColor2.setStrokeWidth(5);
        innerColor.setStrokeWidth(5);
        circleRing.setStrokeWidth(5);

        circleColor.setStyle(Paint.Style.STROKE);
        circleColor2.setStyle(Paint.Style.STROKE);
    }

    /**
     * Instantiates a new circular seek bar.
     *
     * @param context
     *            the context
     * @param attrs
     *            the attrs
     * @param defStyle
     *            the def style
     */
    public CircularSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
//        initDrawable();
    }

    /**
     * Instantiates a new circular seek bar.
     *
     * @param context
     *            the context
     * @param attrs
     *            the attrs
     */
    public CircularSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
//        initDrawable();
    }

    /**
     * Instantiates a new circular seek bar.
     *
     * @param context
     *            the context
     */
    public CircularSeekBar(Context context) {
        super(context);
        mContext = context;
//        initDrawable();
    }

    /**
     * Inits the drawable.
     */
    public void initDrawable() {
        progressMark = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.scrubber_control_normal_holo);
        progressMarkPressed = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.scrubber_control_pressed_holo);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View#onMeasure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(700,400);
        width = getWidth(); // Get View Width
        height = getHeight();// Get View Height

        int size = (width > height) ? height : width; // Choose the smaller
        // between width and
        // height to make a
        // square

        cx = width / 2; // Center X for circle
        cy = height / 2; // Center Y for circle
        outerRadius = size / 2; // Radius of the outer circle

        innerRadius = outerRadius - barWidth; // Radius of the inner circle

        left = cx - outerRadius; // Calculate left bound of our rect
        right = cx + outerRadius;// Calculate right bound of our rect
        top = cy - outerRadius;// Calculate top bound of our rect
        bottom = cy + outerRadius;// Calculate bottom bound of our rect

        startPointX = cx; // 12 O'clock X coordinate
        startPointY = cy - outerRadius;// 12 O'clock Y coordinate
        markPointX = startPointX;// Initial locatino of the marker X coordinate
        markPointY = startPointY;// Initial locatino of the marker Y coordinate

        rect.set(left+10, top+10, right-10, bottom-10);
        rectx.set(left+1, top+1, right-1, bottom-1);// assign size to rect
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawCircle(cx, cy, outerRadius, circleRing);
        if(width>0) {
            if (PlayScreen.sStart == 0) {
                shader = new BitmapShader(setCircleBG(),
                        Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                PlayScreen.sStart = 1;
            }
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(shader);

            RectF rect1 = new RectF((width - height) / 2 + 5, 5,
                    (width + height) / 2 - 5, height - 5);
//            RectF rect1 = new RectF(0, 5, (width+height)/2-5, height-5);
            canvas.drawRoundRect(rect1, outerRadius, outerRadius, paint);
        }
        canvas.drawArc(rect, startAngle, angle, false, circleColor);
//        canvas.drawArc(rectx, 90, 90, false, circleColor2);
        if(SHOW_SEEKBAR){
//            dx = getXFromAngle();
//            dy = getYFromAngle();
//            drawMarkerAtProgress(canvas);
        }
        super.onDraw(canvas);
    }

    /**
     * Draw marker at the current progress point onto the given canvas.
     *
     * @param canvas
     *            the canvas
     */
    public void drawMarkerAtProgress(Canvas canvas) {
        if (IS_PRESSED) {
            canvas.drawBitmap(progressMarkPressed, dx, dy, null);
        } else {
            canvas.drawBitmap(progressMark, dx, dy, null);
        }
    }

    /**
     * Gets the X coordinate of the arc's end arm's point of intersection with
     * the circle
     *
     * @return the X coordinate
     */
    public float getXFromAngle() {
        int size1 = progressMark.getWidth();
        int size2 = progressMarkPressed.getWidth();
        int adjust = (size1 > size2) ? size1 : size2;
        float x = markPointX - (adjust / 2);
        return x;
    }

    /**
     * Gets the Y coordinate of the arc's end arm's point of intersection with
     * the circle
     *
     * @return the Y coordinate
     */
    public float getYFromAngle() {
        int size1 = progressMark.getHeight();
        int size2 = progressMarkPressed.getHeight();
        int adjust = (size1 > size2) ? size1 : size2;
        float y = markPointY - (adjust / 2);
        return y;
    }

    /**
     * Get the angle.
     *
     * @return the angle
     */
    public int getAngle() {
        return angle;
    }

    /**
     * Set the angle.
     *
     * @param angle
     *            the new angle
     */
    public void setAngle(int angle) {
        this.angle = angle;
        float donePercent = (((float) this.angle) / 360) * 300;
        float progress = (donePercent / 300) * getMaxProgress();
        setProgressPercent(Math.round(donePercent));
        CALLED_FROM_ANGLE = true;
        setProgress(Math.round(progress));
    }

    /**
     * Sets the seek bar change listener.
     *
     * @param listener
     *            the new seek bar change listener
     */
    public void setSeekBarChangeListener(OnSeekChangeListener listener) {
        mListener = listener;
    }

    /**
     * Gets the seek bar change listener.
     *
     * @return the seek bar change listener
     */
    public OnSeekChangeListener getSeekBarChangeListener() {
        return mListener;
    }

    /**
     * Gets the bar width.
     *
     * @return the bar width
     */
    public int getBarWidth() {
        return barWidth;
    }

    /**
     * Sets the bar width.
     *
     * @param barWidth
     *            the new bar width
     */
    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
    }

    /**
     * The listener interface for receiving onSeekChange events. The class that
     * is interested in processing a onSeekChange event implements this
     * interface, and the object created with that class is registered with a
     * component using the component's
     * <code>setSeekBarChangeListener(OnSeekChangeListener)<code> method. When
     * the onSeekChange event occurs, that object's appropriate
     * method is invoked.
     *
     * @see //OnSeekChangeEvent
     */
    public interface OnSeekChangeListener {

        /**
         * On progress change.
         *
         * @param view
         *            the view
         * @param newProgress
         *            the new progress
         */
        public void onProgressChange(CircularSeekBar view, int newProgress);
    }

    /**
     * Gets the max progress.
     *
     * @return the max progress
     */
    public int getMaxProgress() {
        return maxProgress;
    }

    /**
     * Sets the max progress.
     *
     * @param maxProgress
     *            the new max progress
     */
    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    /**
     * Gets the progress.
     *
     * @return the progress
     */
    public int getProgress() {
        return progress;
    }

    /**
     * Sets the progress.
     *
     * @param progress
     *            the new progress
     */
    public void setProgress(int progress) {
        if (this.progress != progress) {
            this.progress = progress;
            if (!CALLED_FROM_ANGLE) {
                int newPercent = (this.progress * 300) / this.maxProgress;
                int newAngle = (newPercent * 360) / 300 ;
                this.setAngle(newAngle);
                this.setProgressPercent(newPercent);
            }
            mListener.onProgressChange(this, this.getProgress());
            CALLED_FROM_ANGLE = false;
            invalidate();
        }
    }

    /**
     * Gets the progress percent.
     *
     * @return the progress percent
     */
    public int getProgressPercent() {
        return progressPercent;
    }

    /**
     * Sets the progress percent.
     *
     * @param progressPercent
     *            the new progress percent
     */
    public void setProgressPercent(int progressPercent) {
        this.progressPercent = progressPercent;
    }

    /**
     * Sets the ring background color.
     *
     * @param color
     *            the new ring background color
     */
    public void setRingBackgroundColor(int color) {
        circleRing.setColor(color);
    }

    /**
     * Sets the back ground color.
     *
     * @param color
     *            the new back ground color
     */
    public void setBackGroundColor(int color) {
        innerColor.setColor(color);
    }

    /**
     * Sets the progress color.
     *
     * @param color
     *            the new progress color
     */
    public void setProgressColor(int color) {
        circleColor.setColor(color);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        boolean up = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moved(x, y, up);
                break;
            case MotionEvent.ACTION_MOVE:
                moved(x, y, up);
                break;
            case MotionEvent.ACTION_UP:
                up = true;
                moved(x, y, up);
                break;
        }
        return true;
    }

    /**
     * Moved.
     *
     * @param x
     *            the x
     * @param y
     *            the y
     * @param up
     *            the up
     */
    private void moved(float x, float y, boolean up) {
        float distance = (float) Math.sqrt(Math.pow((x - cx), 2) + Math.pow((y - cy), 2));
        if (distance < outerRadius + adjustmentFactor && distance > innerRadius - adjustmentFactor && !up) {
            IS_PRESSED = true;

            markPointX = (float) (cx + outerRadius * Math.cos(Math.atan2(x - cx, cy - y) - (Math.PI /2)));
            markPointY = (float) (cy + outerRadius * Math.sin(Math.atan2(x - cx, cy - y) - (Math.PI /2)));

            float degrees = (float) ((float) ((Math.toDegrees(Math.atan2(x - cx, cy - y)) + 360.0)) % 360.0);
            // and to make it count 0-360
            if (degrees < 0) {
                degrees += 2 * Math.PI;
            }

            setAngle(Math.round(degrees));
            ProgressBar play = new ProgressBar(getProgressPercent());
            invalidate();

        } else {
            IS_PRESSED = false;
            ProgressBar play = new ProgressBar(getProgressPercent());
            invalidate();
        }

    }

    /**
     * Gets the adjustment factor.
     *
     * @return the adjustment factor
     */
    public float getAdjustmentFactor() {
        return adjustmentFactor;
    }

    /**
     * Sets the adjustment factor.
     *
     * @param adjustmentFactor
     *            the new adjustment factor
     */
    public void setAdjustmentFactor(float adjustmentFactor) {
        this.adjustmentFactor = adjustmentFactor;
    }

    /**
     * To display seekbar
     */
    public void ShowSeekBar() {
        SHOW_SEEKBAR = true;
    }

    /**
     * To hide seekbar
     */
    public void hideSeekBar() {
        SHOW_SEEKBAR = false;
    }

    public Bitmap setCircleBG() {
        Bitmap bitmap;
        if(MainScreen.sImage[MainScreen.sIndex]!=null) {
            bitmap = ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(MainScreen.sImage[MainScreen.sIndex]), height,height);
        } else {
            Drawable drawable = PlayScreen.sContextPlay.getResources().getDrawable(
                    R.drawable.ic_music2);
            bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
            Canvas canvas1 = new Canvas(bitmap);
            drawable.setBounds(0, 0, 400, 400);
            drawable.draw(canvas1);
        }

        Bitmap output = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xFFFFFFFF;
        final Paint paint = new Paint();
        final Rect rect = new Rect((width-height)/2-50, -50, (width+height)/2+50, height+50);
//        final Rect rect2 = new Rect(0, 0, width, height);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawRect(0, 0, width, height,paint);
        Log.e("WIDTH_HEIGHT","width"+width+"-----height:"+height);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

}

