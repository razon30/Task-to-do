package techfie.razon.tasktodo;

/**
 * Created by razon30 on 08-12-16.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Dmytro Denysenko on 5/6/15.
 */
public class CanaroTextView extends TextView {
    public CanaroTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CanaroTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CanaroTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                    "canaro_extra_bold.otf");
            setTypeface(tf);
        }
    }

}