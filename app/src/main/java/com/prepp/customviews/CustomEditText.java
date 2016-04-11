package com.prepp.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.prepp.R;


/**
 * <p/>
 * Project: <b>eProtege</b><br/>
 * Created by: Anand K. Rai on 10/9/15.<br/>
 * Email id: anand.rai@tothenew.com<br/>
 * <p/>
 */
public class CustomEditText extends EditText {
    public CustomEditText(Context context) {
        super(context);
        init(null);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomView);
            String fontName = a.getString(R.styleable.CustomView_fontName);
            Typeface myTypeface = null;
            if (fontName != null) {
                myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
                setTypeface(myTypeface);
            } else {
                myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
                setTypeface(myTypeface);
            }
            a.recycle();
        }
    }


}



