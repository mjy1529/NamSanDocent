package docent.namsanhanok;

import java.util.ArrayList;

import docent.namsanhanok.Category.CategoryResult;
import docent.namsanhanok.Docent.DocentData;
import docent.namsanhanok.Docent.DocentDetailResult;
import docent.namsanhanok.Docent.DocentResult;
import docent.namsanhanok.Event.EventResult;
import docent.namsanhanok.Home.HomeResult;
import docent.namsanhanok.Home.PackageResult;
import docent.namsanhanok.Notice.NoticeResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NetworkService {

    // 홈
    @Headers("Content-Type: application/json")
    @POST("/home_info/ko")
    Call<HomeResult> getHomeResult(@Body String cmd);

    // 카테고리
    @Headers("Content-Type: application/json")
    @POST("/category_info/ko")
    Call<CategoryResult> getCategoryResult(@Body String cmd);

    // 도슨트
    @Headers("Content-Type: application/json")
    @POST("/docent_info/ko")
    Call<DocentResult> getDocentResult(@Body String cmd);

    // 도슨트 디테일
    @Headers("Content-Type: application/json")
    @POST("/docent_detail_info/ko")
    Call<DocentDetailResult> getDocentDetailResult(@Body String cmd);

    // 알리는 말씀
    @Headers("Content-Type: application/json")
    @POST("/notice_info/ko")
    Call<NoticeResult> getNoticeResult(@Body String cmd);

    // 세시/행사
    @Headers("Content-Type: application/json")
    @POST("/event_info/ko")
    Call<EventResult> getEventResult(@Body String cmd);

    // 문의하기
    @Headers("Content-Type: application/json")
    @POST("/question/ko")
    Call<String> postQuestion(@Body String cmd);

    // package_info
    @Headers("Content-Type: application/json")
    @POST("/package_info")
    Call<PackageResult> getPackageResult(@Body String cmd);

}
