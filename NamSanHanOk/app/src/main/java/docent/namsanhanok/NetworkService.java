package docent.namsanhanok;

import java.util.ArrayList;

import docent.namsanhanok.Category.CategoryResult;
import docent.namsanhanok.Docent.DocentData;
import docent.namsanhanok.Docent.DocentResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NetworkService {

    // 카테고리
    @Headers("Content-Type: application/json")
    @POST("/category_info/ko")
    Call<CategoryResult> getCategoryResult(@Body String cmd);



    // 도슨트
    @Headers("Content-Type: application/json")
    @POST("/docent_info/ko")
    Call<DocentResult> getDocentResult(@Body String cmd);

}
