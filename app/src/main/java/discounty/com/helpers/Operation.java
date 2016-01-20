package discounty.com.helpers;


import android.util.Log;

public abstract class Operation {

    abstract public void doJob();

    public void handleException(Exception e) {
        Log.d("OPERATION_EXCEPTION", e.getMessage());
        e.printStackTrace();
    }
}
