package pacifier.com.app.network;

import com.google.gson.annotations.SerializedName;

public class TokenResponse {
    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }
}
