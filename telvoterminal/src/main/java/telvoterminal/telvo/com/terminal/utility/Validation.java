package telvoterminal.telvo.com.terminal.utility;

import android.text.TextUtils;
import android.widget.CheckBox;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by invar on 26-Sep-17.
 */

public class Validation {

    public static boolean isTextEmpty(String str) {
        return (TextUtils.isEmpty(str)) ? true : false;
    }

    public static boolean isCheckBoxChecked(CheckBox checkBox) {
        return (checkBox.isChecked()) ? true : false;
    }

    public static boolean isBirthDateValid(String date) {
        Date parsedDate = null;

        try {
            parsedDate = new SimpleDateFormat("dd MMM, yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (parsedDate.before(new Date())) ? true : false;
    }

    public static boolean isCreditCardDateValid(String date) {
        Date parsedDate = null;

        try {
            parsedDate = new SimpleDateFormat("dd MMM, yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (parsedDate.after(new Date())) ? true : false;
    }

    public static boolean isNationalIdValid(String id) {
        return (id.length() >= 13 && id.length() <= 17) ? true : false;
    }

    /*public static boolean isPassportValid(String id) {
        return true;
    }*/

    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isCreditCardValid(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
}
