package telvoterminal.telvo.com.terminal.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 * Created by invar on 14-Sep-17.
 */

public class RobotoRegularEditText extends AppCompatEditText {

    public RobotoRegularEditText(Context context) {
        super(context);
        setFont();
    }

    public RobotoRegularEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public RobotoRegularEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface normal = Typeface.createFromAsset(getContext().getAssets(),"fonts/roboto.regular.ttf");
        setTypeface( normal, Typeface.NORMAL );
    }
}
