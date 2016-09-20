package com.congnt.androidbasecomponent.view.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import com.congnt.androidbasecomponent.utility.DimensionUtil;

import java.util.ArrayList;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.HONEYCOMB;

/**
 * Just test, not recommend for use
 */
public class DrawingView extends View implements View.OnTouchListener {

    public static final int NORMAL_STROKE_WIDTH = 5; //dp unit
    public static final int MAX_STROKE_WIDTH = 20; //dp unit
    public static final int MIN_STROKE_WIDTH = 1; //dp unit

    private static final float TOUCH_TOLERANCE = 4;
    //    private static final int ERASER_WIDTH = 80;
    private static final float MAX_DISTANCE_ALLOW = 2000;
    private static final float MAX_DISTANCE_CRAYON_ALLOW = 1500;
//    private static final int SHADOW_COLOR = 0xFF000000;

    public static boolean isEraserActive = false;

    private static int P_NUM = 20;
    private static int RANGE = 15;

    ArrayList<Pair<Path, Paint>> mPaths = new ArrayList<>();

    RectF mBoundPath = new RectF();

    long[] rp = new long[P_NUM];
    ArrayList<Pair<RectF, ArrayList<Pair<Path, Paint>>>> mHistoricalDrawing = new ArrayList<>();
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;
    private float mX, mY;
    private int mColor = Color.BLACK;
    private int mShadowColor = Color.BLACK;
    private int mStroke;
    private int mBoundPadding;
    private int mAlpha = 100; // alpha (range from 0 to 100)
    private Drawable mBackground;
    private Paint mBitmapPaint;
    private int mHandWritingType = HANDWRITING_TYPE.PEN; //default type is pen
    private float mDistanceX = 0;
    private float mDistanceY = 0;
    private UndoRedoListener mUndoRedoListener;
    private int mCurrentStep = -1;

    public DrawingView(Context context, AttributeSet attr) {
        super(context, attr);

        init(context);

        setFocusable(true);
        setFocusableInTouchMode(true);
        setBackgroundColor(Color.TRANSPARENT);
        setDrawingCacheEnabled(true);

        if (SDK_INT >= HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        this.setOnTouchListener(this);

        onCanvasInitialization();
    }

    public void setUndoRedoListener(UndoRedoListener listener) {
        mUndoRedoListener = listener;
    }

    private void init(Context context) {
        mStroke = (int) DimensionUtil.dpToPx(context, NORMAL_STROKE_WIDTH);
    }

    public void onCanvasInitialization() {
        mPaint = createNormalPaint();
        mShadowColor = mColor;

        mCanvas = new Canvas();
        mPath = new Path();
    }

    public void setLineWidth(int stroke) {
        mStroke = stroke;
    }

    public void setColor(int color) {
        mColor = color;
        mShadowColor = color;
    }

    public void setAlpha(int alpha) {
        mAlpha = alpha;
//        mColor = AndroidUtil.getColorWithAlpha(mAlpha, mColor);
    }

    public void setHandWritingType(int handWritingType) {
        mHandWritingType = handWritingType;
        isEraserActive = false;
    }

    public void activeEraser(boolean isEraserActive) {
        this.isEraserActive = isEraserActive;
        if (isEraserActive) {
            mHandWritingType = HANDWRITING_TYPE.PEN;
        }
    }

    @Override
    public void setBackground(Drawable background) {
        mBackground = background;
        super.setBackground(background);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public boolean onTouch(View arg0, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onInitHandwriting();
                touch_start(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                //add drawing to historical
                addToHistorical();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        updateBoundPath();

        if (!mPaths.isEmpty()) {
            for (int i = 0; i <= mPaths.size() - 1; i++) {
                canvas.drawPath(mPaths.get(i).first, mPaths.get(i).second);
            }
        }

    }

    private void touch_start(float x, float y) {
        //setup Paint
        setupPaint();
        //reset path
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
        addToListPath();
    }

    private void addToListPath() {
        Paint newPaint = new Paint(mPaint); // Clones the mPaint object
        mPaths.add(new Pair<>(mPath, newPaint));
    }

    private void updateListPath() {
        if (mPaths == null || mPaths.isEmpty()) {
            return;
        }
        Paint newPaint = new Paint(mPaint); // Clones the mPaint object
        Path newPath = new Path(mPath);
        mPaths.remove(mPaths.size() - 1);
        mPaths.add(new Pair<>(newPath, newPaint));
    }

    private void setupPaint() {
        //check eraser active is on or off
        if (isEraserActive) {
            mPaint = createEraserPaint();
        } else {
            mPaint = createNormalPaint();
        }
        switch (mHandWritingType) {
            case HANDWRITING_TYPE.PEN:
                mPaint.setShadowLayer(0, 0, 0, mShadowColor);
                break;
            case HANDWRITING_TYPE.BRUSH:
                mPaint.setShadowLayer(mStroke, 0, 0, mShadowColor);
                break;
            case HANDWRITING_TYPE.CRAYON:
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setShadowLayer(0, 0, 0, mShadowColor);
                break;
            case HANDWRITING_TYPE.MARKER:
                mPaint.setShadowLayer(0, 0, 0, mShadowColor);
                break;
            default:
                break;
        }
    }

    private Paint createEraserPaint() {
        Paint eraserPaint = new Paint();
        eraserPaint.setAlpha(0);
        eraserPaint.setColor(Color.TRANSPARENT);
        eraserPaint.setStrokeWidth(mStroke * 2);
        eraserPaint.setStyle(Paint.Style.STROKE);
        eraserPaint.setMaskFilter(null);
        eraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        eraserPaint.setAntiAlias(true);
        return eraserPaint;
    }

    private Paint createNormalPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAlpha(mAlpha);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        paint.setStrokeWidth(mStroke);
        paint.setColor(mColor);
        return paint;
    }

    private void touch_move(float x, float y) {
        if (isOutOfAreaView(x, y)) {
            mX = x;
            mY = y;
            return;
        }
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            switch (mHandWritingType) {
                case HANDWRITING_TYPE.PEN:
                    drawPen(x, y);
                    break;
                case HANDWRITING_TYPE.BRUSH:
                    drawBrush(x, y);
                    break;
                case HANDWRITING_TYPE.CRAYON:
                    drawCrayon(x, y);
                    break;
                case HANDWRITING_TYPE.MARKER:
                    drawMarker(x, y);
                    break;
                default:
                    break;
            }
        }
    }

    private boolean isOutOfAreaView(float x, float y) {
        Log.d("AAA", "X and Y: " + x + "|" + y);
        return x < 0 || x > getMeasuredWidth() || y < 0 || y > getMeasuredHeight();
    }

    private void touch_up() {
        updateListPath();
        resetDrawing();
    }

    private void drawLine(float x, float y) {
        mPath.moveTo(mX, mY);
        mPath.lineTo(x, y);
        invalidate();
        mX = x;
        mY = y;
    }

    private void drawBrush(float x, float y) {
        drawLine(x, y);
    }

    private void drawPen(float x, float y) {
        drawLine(x, y);
    }

    private void drawCrayon(float x, float y) {
        if (checkDistance(x, y)) {
            addToListPath();
        }

        int inkAmount = mStroke;

        double px = x - mX;
        double py = y - mY;

        double length = Math.sqrt(px * px + py * py);

        int sep = 5;
        double dotSize = sep * this.clamp(inkAmount / length * sep, 1, 0.5);
        double dotNum = Math.ceil(mStroke);
//        double dotNum = 20;
        double range = mStroke / 2;
        double i, r, cc, cx, cy, cw, ch;

        for (i = 0; i < dotNum; i++) {
            r = random(range, 0);
            cc = random(Math.PI * 2, 0);
            cw = random(dotSize, dotSize / 2);
            ch = random(dotSize, dotSize / 2);
            cx = x + r * Math.sin(cc) - cw / 2;
            cy = y + r * Math.cos(cc) - ch / 2;
            mPath.addRect((float) cx, (float) cy, (float) (cx + cw), (float) (cy + ch), Path.Direction.CW);
        }
        invalidate();
        mX = x;
        mY = y;
    }


    private void drawMarker(float x, float y) {
        checkDistance(x, y);

        mPaint.setStrokeWidth(2.0f);
        mPaint.setShadowLayer(mStroke, 0, 0, mShadowColor);
        mPaint.setColor(mColor);

        Paint newPaint = new Paint(mPaint); // Clones the mPaint object
        mPaths.add(new Pair<>(mPath, newPaint));

        for (int i = 0; i < P_NUM; i++) {
            //this.c.globalAlpha = 0.5;
            mPath.moveTo(mX + this.rp[i], mY + this.rp[i]);
            mPath.lineTo(x + this.rp[i], y + this.rp[i]);
        }
        invalidate();

        mPaint.setStrokeWidth(4.0f);
        mPaint.setShadowLayer(0, 0, 0, mShadowColor);

        Paint newPaint2 = new Paint(mPaint); // Clones the mPaint object
        mPaths.add(new Pair<>(mPath, newPaint2));

        mPath.moveTo(mX, mY);
        mPath.lineTo(x, y);
        invalidate();

        mX = x;
        mY = y;

    }

    private void onInitHandwriting() {
        int padding = mStroke / 2 + 5;
        mBoundPadding = (mBoundPadding >= padding ? mBoundPadding : padding);

        if (mHandWritingType != HANDWRITING_TYPE.MARKER) {
            return;
        }
        for (int i = 0; i < P_NUM; i++) {
            this.rp[i] = Math.round(Math.random() * (mStroke - RANGE) + 5);
        }

    }

    private boolean checkDistance(float x, float y) {
        float max_distance = MAX_DISTANCE_ALLOW;
        if (mHandWritingType == HANDWRITING_TYPE.CRAYON) {
            max_distance = MAX_DISTANCE_CRAYON_ALLOW;
        }
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        mDistanceX += dx;
        mDistanceY += dy;
        Log.d("Tag", "Distance: " + mDistanceX + "|" + mDistanceY);
        if (mDistanceX >= max_distance || mDistanceY >= max_distance) {
            //update list path
            updateListPath();
            //reset drawing
            resetDrawing();
            return true;
        }
        return false;
    }

    private void resetDrawing() {
        mPath.reset();
        destroyDrawingCache();
        mDistanceX = 0;
        mDistanceY = 0;
    }


    private void updateBoundPath() {
        RectF boundPath = new RectF();
        mPath.computeBounds(boundPath, true);
        Log.d("AAA", "BoundPath New: " + boundPath.left + "|" + boundPath.top + "|" + boundPath.right + "|" + boundPath.bottom);
//        if (isEraserActive) {
//            updateBoundDrawing(boundPath);
//        } else {
//            mBoundPath.union(boundPath);
//        }
        if (!isEraserActive) {
            mBoundPath.union(boundPath);
        }

        Log.d("AAA", "BoundPath: " + mBoundPath.left + "|" + mBoundPath.top + "|" + mBoundPath.right + "|" + mBoundPath.bottom);
    }


    private void updateBoundDrawing(RectF diffRectF) {
        RectF boundEraser = new RectF(mBoundPath.left, mBoundPath.top, mBoundPath.right, mBoundPath.bottom);
        boolean intersect = boundEraser.intersect(diffRectF);
        if (!intersect) {
            return;
        }

        PointF corner_1 = new PointF(mBoundPath.left, mBoundPath.top);
        PointF corner_2 = new PointF(mBoundPath.right, mBoundPath.top);
        PointF corner_3 = new PointF(mBoundPath.left, mBoundPath.bottom);
        PointF corner_4 = new PointF(mBoundPath.right, mBoundPath.bottom);

        if (boundEraser.contains(corner_1.x, corner_1.y) && boundEraser.contains(corner_4.x, corner_4.y)) {
            mBoundPath.setEmpty();
        }

        if (boundEraser.contains(corner_1.x, corner_1.y) && boundEraser.contains(corner_2.x, corner_2.y)) {
            mBoundPath.set(mBoundPath.left, boundEraser.bottom, mBoundPath.right, mBoundPath.bottom);
        }

        if (boundEraser.contains(corner_3.x, corner_3.y) && boundEraser.contains(corner_4.x, corner_4.y)) {
            mBoundPath.set(mBoundPath.left, mBoundPath.top, mBoundPath.right, boundEraser.top);
        }

        if (boundEraser.contains(corner_1.x, corner_1.y) && boundEraser.contains(corner_3.x, corner_3.y)) {
            mBoundPath.set(boundEraser.right, mBoundPath.top, mBoundPath.right, mBoundPath.bottom);
        }

        if (boundEraser.contains(corner_2.x, corner_2.y) && boundEraser.contains(corner_4.x, corner_4.y)) {
            mBoundPath.set(mBoundPath.left, mBoundPath.top, boundEraser.left, mBoundPath.bottom);
        }

    }

    public Bitmap getDrawingBitmap() {
        Bitmap bitmap = this.getDrawingCache();
        RectF rectF = updateBoundaryRect(mBoundPath);
        if (rectF.width() == 0 || rectF.height() == 0) {
            return null;
        }
        return Bitmap.createBitmap(bitmap, (int) rectF.left, (int) rectF.top, (int) rectF.width(), (int) rectF.height());

    }

    public Bitmap getFullDrawingBitmap() {
        Bitmap bitmap = this.getDrawingCache();
        return Bitmap.createBitmap(bitmap, 0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    public RectF getRectBoundary() {
        return updateBoundaryRect(mBoundPath);
    }

    private RectF updateBoundaryRect(RectF original) {
        int left = (int) original.left - mBoundPadding;
        left = left >= 0 ? left : 0;

        int top = (int) original.top - mBoundPadding;
        top = top >= 0 ? top : 0;

        int maxWidth = getMeasuredWidth() - left;
        int maxHeight = getMeasuredHeight() - top;

        int width = (int) original.width() + 2 * mBoundPadding;
        width = width <= maxWidth ? width : maxWidth;

        int height = (int) original.height() + 2 * mBoundPadding;
        height = height <= maxHeight ? height : maxHeight;

        return new RectF(left, top, left + width, top + height);
    }

    public void clear() {
        mPath.reset();
        mPaths.clear();
        mBoundPath.setEmpty();
        mHistoricalDrawing.clear();

        if (mHistoricalDrawing != null) {
            mHistoricalDrawing.clear();
            mCurrentStep = -1;
        }
        //repaint the entire view.
        invalidate();
    }

    private void addToHistorical() {
        if (mBoundPath != null) {
            mCurrentStep++;
            Log.d("AAA", "AddToHistory: " + mCurrentStep);
            if (mHistoricalDrawing != null && mHistoricalDrawing.size() > mCurrentStep) {
                Log.d("AAA", "Remove History: " + mCurrentStep);
                removeHistory(mCurrentStep);
            }

            mHistoricalDrawing.add(new Pair<>(new RectF(mBoundPath), createHistoricalDrawing()));
            //update historical status
            onUpdateHistoricalStatus();

        }
    }

    private ArrayList<Pair<Path, Paint>> createHistoricalDrawing() {
        ArrayList<Pair<Path, Paint>> historyDrawing = new ArrayList<>();
        if (!mPaths.isEmpty()) {
            for (Pair<Path, Paint> item : mPaths) {
                historyDrawing.add(item);
            }
        }
        return historyDrawing;
    }

    private void removeHistory(int from) {
        for (int i = mHistoricalDrawing.size() - 1; i >= from; i--) {
            mHistoricalDrawing.remove(i);
        }
    }

    public void undo() {
        if (!mHistoricalDrawing.isEmpty()) {
            mCurrentStep--;
            Log.d("AAA", "undo: " + mCurrentStep);
            if (mCurrentStep >= 0) {
                restoreFromHistory();
            } else {
                mPaths.clear();
                mBoundPath.setEmpty();
            }
            mPath.reset();
            invalidate();
            onUpdateHistoricalStatus();
        }
    }

    public void redo() {
        if (!mHistoricalDrawing.isEmpty()) {
            mCurrentStep++;
            Log.d("AAA", "redo: " + mCurrentStep);
            if (mCurrentStep <= mHistoricalDrawing.size() - 1) {
                restoreFromHistory();
            }
            mPath.reset();
            invalidate();
            onUpdateHistoricalStatus();
        }
    }

    private void restoreFromHistory() {
        mPaths.clear();
        mPaths.addAll(mHistoricalDrawing.get(mCurrentStep).second);

        RectF historyRectF = mHistoricalDrawing.get(mCurrentStep).first;
        if (!historyRectF.isEmpty()) {
            mBoundPath = new RectF(historyRectF.left, historyRectF.top, historyRectF.right, historyRectF.bottom);
        }
    }

    private void onUpdateHistoricalStatus() {
        if (mUndoRedoListener != null) {
            mUndoRedoListener.onUpdate(mCurrentStep, mHistoricalDrawing.size());
        }
    }

    private double clamp(double n, int max, double min) {
        return n > max ? max : n < min ? min : n;
    }

    private double random(double max, double min) {
        return Math.random() * (max - min) + min;
    }

    public interface HANDWRITING_TYPE {
        int PEN = 1;
        int BRUSH = 2;
        int MARKER = 3;
        int CRAYON = 4;
    }

    public interface UndoRedoListener {
        void onUpdate(int currentStep, int totalStep);
    }

}