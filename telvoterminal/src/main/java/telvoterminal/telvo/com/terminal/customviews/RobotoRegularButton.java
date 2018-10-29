package telvoterminal.telvo.com.terminal.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class RobotoRegularButton extends AppCompatButton {

    public RobotoRegularButton(Context context) {
        super(context);
        setFont();
    }

    public RobotoRegularButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public RobotoRegularButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface normal = Typeface.createFromAsset(getContext().getAssets(),"fonts/roboto.regular.ttf");
        setTypeface( normal, Typeface.NORMAL );
    }
}