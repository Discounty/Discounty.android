package discounty.com.helpers;


import java.text.NumberFormat;
import java.text.ParsePosition;

public class NumbersHelper {

    public static boolean isNumeric(String str) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition position = new ParsePosition(0);
        formatter.parse(str, position);

        boolean doubleCheck = str.matches("-?\\d+(\\.\\d+)?");

        return str.length() == position.getIndex() && doubleCheck;
    }
}
