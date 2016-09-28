/*
 * Copyright (C) 2016 Evgenii Zagumennyi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.congnt.androidbasecomponent.view.speechview;
/*
 * Copyright (C) 2016 Evgenii Zagumennyi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class RecognitionProgressView extends View implements RecognitionListener {
    private static final int CIRCLE_RADIUS_DP = 5;
    private static final int CIRCLE_SPACING_DP = 11;
    private static final int ROTATION_RADIUS_DP = 25;
    private static final int IDLE_FLOATING_AMPLITUDE_DP = 3;

    private static final int[] DEFAULT_BARS_HEIGHT_DP = {60, 46, 70, 54, 64};
    private static final float MDPI_DENSITY = 1.5f;
    public static int bars_count = 5;
    private final List<RecognitionBar> recognitionBars = new ArrayList<>();
    private Paint paint;
    private BarParamsAnimator animator;
    private int radius;
    private int spacing;
    private int rotationRadius;
    private int amplitude;
    private float density;
    private boolean isSpeaking;
    private boolean animating;
    private SpeechRecognizer speechRecognizer;
    private RecognitionListener recognitionListener;
    private int barColor = -1;
    private int[] barColors;
    private int[] barMaxHeights;
    private boolean enableEndOfSpeechAnimation = true;

    public RecognitionProgressView(Context context) {
        super(context);
        init();
    }

    public RecognitionProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecognitionProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setBars_count(int bars_count) {
        this.bars_count = bars_count;
    }

    public void setEndOfSpeechAnimation(boolean enableEndOfSpeechAnimation) {
        this.enableEndOfSpeechAnimation = enableEndOfSpeechAnimation;
    }

    /**
     * Set SpeechRecognizer which is view animated with
     */
    public void setSpeechRecognizer(SpeechRecognizer recognizer) {
        speechRecognizer = recognizer;
        speechRecognizer.setRecognitionListener(this);
    }

    /**
     * Set RecognitionListener to receive callbacks from {@link SpeechRecognizer}
     */
    public void setRecognitionListener(RecognitionListener listener) {
        recognitionListener = listener;
    }

    /**
     * Starts animating view
     */
    public void play() {
        startIdleInterpolation();
        animating = true;
        invalidate();
    }

    /**
     * Stops animating view
     */
    public void stop() {
        if (animator != null) {
            animator.stop();
            animator = null;
        }
        animating = false;
        resetBars();
    }

    /**
     * Set one color to all bars in view
     */
    public void setSingleColor(int color) {
        barColor = color;
    }

    /**
     * Set different colors to bars in view
     *
     * @param colors - array with size = {@link #bars_count}
     */
    public void setColors(int[] colors) {
        if (colors == null) return;

        barColors = new int[bars_count];
        if (colors.length < bars_count) {
            System.arraycopy(colors, 0, barColors, 0, colors.length);
            for (int i = colors.length; i < bars_count; i++) {
                barColors[i] = colors[0];
            }
        } else {
            System.arraycopy(colors, 0, barColors, 0, bars_count);
        }
    }

    /**
     * Set sizes of bars in view
     *
     * @param heights - array with size = {@link #bars_count},
     *                if not set uses default bars heights
     */
    public void setBarMaxHeightsInDp(int[] heights) {
        if (heights == null) return;

        barMaxHeights = new int[bars_count];
        if (heights.length < bars_count) {
            System.arraycopy(heights, 0, barMaxHeights, 0, heights.length);
            for (int i = heights.length; i < bars_count; i++) {
                barMaxHeights[i] = heights[0];
            }
        } else {
            System.arraycopy(heights, 0, barMaxHeights, 0, bars_count);
        }
    }

    private void init() {
        paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GRAY);

        density = getResources().getDisplayMetrics().density;

        radius = (int) (CIRCLE_RADIUS_DP * density);
        spacing = (int) (CIRCLE_SPACING_DP * density);
        rotationRadius = (int) (ROTATION_RADIUS_DP * density);
        amplitude = (int) (IDLE_FLOATING_AMPLITUDE_DP * density);

        if (density <= MDPI_DENSITY) {
            amplitude *= 2;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (recognitionBars.isEmpty()) {
            initBars();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (recognitionBars.isEmpty()) {
            return;
        }

        if (animating) {
            animator.animate();
        }

        for (int i = 0; i < recognitionBars.size(); i++) {
            RecognitionBar bar = recognitionBars.get(i);
            if (barColors != null) {
                paint.setColor(barColors[i]);
            } else if (barColor != -1) {
                paint.setColor(barColor);
            }
            canvas.drawRoundRect(bar.getRect(), radius, radius, paint);
        }

        if (animating) {
            invalidate();
        }
    }

    private void initBars() {
        final List<Integer> heights = initBarHeights();
        int firstCirclePosition = (int) (getMeasuredWidth() / 2 -
                (bars_count - 1) / 2.0f * spacing -
                bars_count * radius);
        for (int i = 0; i < bars_count; i++) {
            int x = firstCirclePosition + (2 * radius + spacing) * i;
            RecognitionBar bar = new RecognitionBar(x, getMeasuredHeight() / 2, 2 * radius, heights.get(i), radius);
            recognitionBars.add(bar);
        }
    }

    private List<Integer> initBarHeights() {
        final List<Integer> barHeights = new ArrayList<>();
        if (barMaxHeights == null) {
            for (int i = 0; i < bars_count; i++) {
                barHeights.add((int) (DEFAULT_BARS_HEIGHT_DP[i] * density));
            }
        } else {
            for (int i = 0; i < bars_count; i++) {
                barHeights.add((int) (barMaxHeights[i] * density));
            }
        }
        return barHeights;
    }

    private void resetBars() {
        for (RecognitionBar bar : recognitionBars) {
            bar.setX(bar.getStartX());
            bar.setY(bar.getStartY());
            bar.setHeight(radius * 2);
            bar.update();
        }
    }

    private void startIdleInterpolation() {
        animator = new IdleAnimator(recognitionBars, amplitude);
        animator.start();
    }

    private void startRmsInterpolation() {
        resetBars();
        animator = new RmsAnimator(recognitionBars);
        animator.start();
    }

    private void startTransformInterpolation() {
        resetBars();
        animator = new TransformAnimator(recognitionBars, getWidth() / 2, getHeight() / 2, rotationRadius);
        animator.start();
        ((TransformAnimator) animator).setOnInterpolationFinishedListener(new TransformAnimator.OnInterpolationFinishedListener() {
            @Override
            public void onFinished() {
                startRotateInterpolation();
            }
        });
    }

    private void startRotateInterpolation() {
        animator = new RotatingAnimator(recognitionBars, getWidth() / 2, getHeight() / 2);
        animator.start();
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        if (recognitionListener != null) {
            recognitionListener.onReadyForSpeech(params);
        }
    }

    @Override
    public void onBeginningOfSpeech() {
        if (recognitionListener != null) {
            recognitionListener.onBeginningOfSpeech();
        }
        isSpeaking = true;
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        if (recognitionListener != null) {
            recognitionListener.onRmsChanged(rmsdB);
        }
        if (animator == null || rmsdB < 1f) {
            return;
        }
        if (!(animator instanceof RmsAnimator) && isSpeaking) {
            startRmsInterpolation();
        }
        if (animator instanceof RmsAnimator) {
            ((RmsAnimator) animator).onRmsChanged(rmsdB);
        }
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        if (recognitionListener != null) {
            recognitionListener.onBufferReceived(buffer);
        }
    }

    @Override
    public void onEndOfSpeech() {
        if (recognitionListener != null) {
            recognitionListener.onEndOfSpeech();
        }
        isSpeaking = false;
        if (enableEndOfSpeechAnimation)
            startTransformInterpolation();
    }

    @Override
    public void onError(int error) {
        if (recognitionListener != null) {
            recognitionListener.onError(error);
        }
    }

    @Override
    public void onResults(Bundle results) {
        if (recognitionListener != null) {
            recognitionListener.onResults(results);
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        if (recognitionListener != null) {
            recognitionListener.onPartialResults(partialResults);
        }
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        if (recognitionListener != null) {
            recognitionListener.onEvent(eventType, params);
        }
    }
}