package telvoterminal.telvo.com.terminal.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

/**
 * Created by invar on 18-Sep-17.
 */

public class RobotoLightCheckBox extends AppCompatCheckBox {

    public RobotoLightCheckBox(Context context) {
        super(context);
        setFont();
    }

    public RobotoLightCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public RobotoLightCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface normal = Typeface.createFromAsset(getContext().getAssets(),"fonts/roboto.light.ttf");
        setTypeface( normal, Typeface.NORMAL );
    }
}
