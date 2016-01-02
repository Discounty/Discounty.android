package discounty.com.helpers;


import android.content.Context;
import android.net.ConnectivityManager;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkHelper {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static boolean isInternetAvailable(Context context) {
        if (isNetworkConnected(context)) {
            try {
                InetAddress ipAddress = InetAddress.getByName("http://getdiscounty.com");
                return !ipAddress.equals("");
            } catch (UnknownHostException e) {
                return false;
            }
        }
        return false;
    }
}
