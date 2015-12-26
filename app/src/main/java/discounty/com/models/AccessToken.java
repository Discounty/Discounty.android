package discounty.com.models;


public class AccessToken {

    private String accessToken;

    private String tokenType;

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        if (!Character.isUpperCase(tokenType.charAt(0))) {
            tokenType =
                    Character
                        .toString(tokenType.charAt(0))
                        .toUpperCase() + tokenType.substring(1);
        }
        return tokenType;
    }
}
