package io.github.adamshurwitz.retrorecycler.DesignComponents;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import io.github.adamshurwitz.retrorecycler.R;


/**
 * Created by ahurwitz on 5/13/17.
 */

public class RippleTextView extends AppCompatTextView {

    private boolean rippleEnabled;
    private int backgroundColor;
    private int rippleColor;

    public RippleTextView(Context context) {
        super(context);
        //Retrieve attribute values at runtime
        getXMLAttributes(context, null);
        RippleEffect.addRippleEffect(this, rippleEnabled, backgroundColor, rippleColor);
    }

    public RippleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //Retrieve attribute values at runtime
        getXMLAttributes(context, attrs);
        RippleEffect.addRippleEffect(this, rippleEnabled, backgroundColor, rippleColor);
    }

    public RippleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //Retrieve attribute values at runtime
        getXMLAttributes(context, attrs);
        RippleEffect.addRippleEffect(this, rippleEnabled, backgroundColor, rippleColor);
    }

    private void getXMLAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.RippleText,
                0, 0);
        try {
            rippleEnabled = typedArray.getBoolean(
                    R.styleable.RippleText_rippleEnabled,
                    true);
            backgroundColor = typedArray.getColor(
                    R.styleable.RippleText_backgroundColor,
                    getResources().getColor(R.color.background_default));
            rippleColor = typedArray.getColor(
                    R.styleable.RippleText_rippleColor,
                    getResources().getColor(R.color.ripple_default));
        } finally {
            typedArray.recycle();
        }
    }

    public int getRippleColor() {
        return rippleColor;
    }

    public void setRippleColor(int rippleColor) {
        this.rippleColor = rippleColor;
        invalidate();
    }

}
