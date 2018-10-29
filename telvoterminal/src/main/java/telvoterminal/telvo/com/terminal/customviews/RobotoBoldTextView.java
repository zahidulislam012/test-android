package telvoterminal.telvo.com.terminal.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by invar on 13-Sep-17.
 */

public class RobotoBoldTextView extends AppCompatTextView {

    public RobotoBoldTextView(Context context) {
        super(context);
        setFont();
    }

    public RobotoBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public RobotoBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface normal = Typeface.createFromAsset(getContext().getAssets(),"fonts/roboto.bold.ttf");
        setTypeface( normal, Typeface.NORMAL );
    }
}
