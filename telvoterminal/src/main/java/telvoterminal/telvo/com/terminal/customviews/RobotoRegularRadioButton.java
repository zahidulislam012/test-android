package telvoterminal.telvo.com.terminal.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;

/**
 * Created by invar on 17-Sep-17.
 */

public class RobotoRegularRadioButton extends AppCompatRadioButton {

    public RobotoRegularRadioButton(Context context) {
        super(context);
        setFont();
    }

    public RobotoRegularRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public RobotoRegularRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface normal = Typeface.createFromAsset(getContext().getAssets(),"fonts/roboto.regular.ttf");
        setTypeface( normal, Typeface.NORMAL );
    }
}
