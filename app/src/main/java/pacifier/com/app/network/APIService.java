package pacifier.com.app.network;

import org.json.JSONObject;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface APIService {
    @GET("/token.json")
    Call<TokenResponse> getToken();

    @POST("/checkout/{amount}")
    Call<JSONObject> checkout(@Path("amount") float amount, @Body String payment_method_nonce);
}
