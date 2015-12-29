package discounty.com.interfaces;

import discounty.com.models.AccessToken;
import discounty.com.models.Customer;
import discounty.com.models.DiscountCard;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface DiscountyService {

    String ACCESS_GRANT_TYPE = "password";

    String REFRESH_GRANT_TYPE = "refresh_token";

    @POST("/api/v1/customers/full_info")
    Observable<Customer> getFullCustomerInfo();

    @PUT("/api/v1/customers/update")
    Observable<Customer> updateCustomer(@Query("first_name") String firstName,
                                        @Query("last_name") String lastName,
                                        @Query("country") String country,
                                        @Query("city") String city,
                                        @Query("phone_number") String phoneNumber,
                                        @Query("password") String password);

    @POST("/api/v1/signup/customer")
    Observable<Customer> signupCustomer(@Query("email") String email,
                                        @Query("password") String password,
                                        @Query("first_name") String firstName,
                                        @Query("last_name") String lastName);

    @GET("/api/v1/discount_cards/{id}")
    Observable<DiscountCard> getDiscountCardById(@Path("id") int id);

    @PUT("/api/v1/discount_cards/{id}")
    Observable<DiscountCard> updateDiscountCardById(@Path("id") int id,
                                                    @Query("name") String name,
                                                    @Query("description") String description,
                                                    @Query("shop") String shop,
                                                    @Query("barcode") String barcode,
                                                    @Query("discount_percentage") Double discountPercentage,
                                                    @Query("extra_info") String extraInfo,
                                                    @Query("barcode_type") String barcodeType);

    @DELETE("/api/v1/discount_cards/{id}")
    Observable<DiscountCard> deleteDiscountCardById(@Path("id") int id);

    @POST("/api/v1/discount_cards/new")
    Observable<DiscountCard> createDiscountCard(@Query("name") String name,
                                                @Query("description") String description,
                                                @Query("barcode_type") String barcodeType,
                                                @Query("barcode") String barcode,
                                                @Query("discount_percentage") Double discountPercentage,
                                                @Query("extra_info") String extraInfo,
                                                @Query("shop") String shop);

    @GET("/api/test/hello.json")
    Observable<String> getTestGetHello();

    @POST("/api/test/hello.json")
    Observable<String> getTestPostHello();

    @POST("/oauth/token")
    @FormUrlEncoded
    Observable<AccessToken> getAccessToken(@Field("grant_type") String grantType,
                                           @Field("username") String email,
                                           @Field("password") String password);

    @POST("/oauth/token")
    @FormUrlEncoded
    Observable<AccessToken> refreshAccessToken(@Field("grant_type") String grantType,
                                               @Field("refresh_token") String refreshToken);
}
