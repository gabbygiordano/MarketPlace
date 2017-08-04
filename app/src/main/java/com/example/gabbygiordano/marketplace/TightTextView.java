package com.example.gabbygiordano.marketplace;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;

/**
 * Tightly wraps the text when setting the maxWidth.
 * @author sky
 */
public class TightTextView extends android.support.v7.widget.AppCompatTextView {
    private boolean hasMaxWidth;

    public TightTextView(Context context) {
        this(context, null, 0);
    }

    public TightTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TightTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (hasMaxWidth) {
            int specModeW = MeasureSpec.getMode(widthMeasureSpec);
            if (specModeW != MeasureSpec.EXACTLY) {
                Layout layout = getLayout();
                int linesCount = layout.getLineCount();
                if (linesCount > 1) {
                    float textRealMaxWidth = 0;
                    for (int n = 0; n < linesCount; ++n) {
                        textRealMaxWidth = Math.max(textRealMaxWidth, layout.getLineWidth(n));
                    }
                    int w = Math.round(textRealMaxWidth);
                    if (w < getMeasuredWidth()) {
                        super.onMeasure(MeasureSpec.makeMeasureSpec(w, MeasureSpec.AT_MOST),
                                heightMeasureSpec);
                    }
                }
            }
        }
    }


    @Override
    public void setMaxWidth(int maxpixels) {
        super.setMaxWidth(maxpixels);
        hasMaxWidth = true;
    }

    @Override
    public void setMaxEms(int maxems) {
        super.setMaxEms(maxems);
        hasMaxWidth = true;
    }
}