package discounty.com.helpers;


public class OperationHelper {

    public static void doWithRetry(int maxAttempts, Operation operation) {
        for (int count = 0; count < maxAttempts; ++count) {
            try {
                operation.doJob();
                count = maxAttempts;
            } catch (Exception e) {
                operation.handleException(e);
            }
        }
    }
}
