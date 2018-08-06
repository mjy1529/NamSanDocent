package docent.namsanhanok;

import docent.namsanhanok.Category.CategoryResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NetworkService {

//    @GET ("/category_info/ko/{category_id}")
//    Call<CategoryResult> getCategoryResult(@Path("category_id") int category_id);

    // 카테고리
    @Headers("Content-Type: application/json")
    @POST("/category_info/ko")
    Call<CategoryResult> getCategoryResult(@Body String cmd);

}
