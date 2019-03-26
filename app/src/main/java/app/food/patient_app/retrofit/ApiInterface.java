package app.food.patient_app.retrofit;

import app.food.patient_app.model.AllCallsDataModel;
import app.food.patient_app.model.CaloriesDataModel;
import app.food.patient_app.model.ForgotPasswordModel;
import app.food.patient_app.model.GetCaloriesModel;
import app.food.patient_app.model.GetGooGleFitActivityModel;
import app.food.patient_app.model.GetMoodNotesModel;
import app.food.patient_app.model.GetSMSCountModel;
import app.food.patient_app.model.GetSocialUsageListModel;
import app.food.patient_app.model.ImageCountModel;
import app.food.patient_app.model.LoginModel;
import app.food.patient_app.model.LoginWithGmailModel;
import app.food.patient_app.model.MoodInsertModel;
import app.food.patient_app.model.NewAddedCountactCountModel;
import app.food.patient_app.model.NewContackGetModel;
import app.food.patient_app.model.RegisterModel;
import app.food.patient_app.model.RemainingCallModel;
import app.food.patient_app.model.ResetPasswordModel;
import app.food.patient_app.model.SendActivityModel;
import app.food.patient_app.model.SendSMSCountModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {


    @FormUrlEncoded
    @POST("signup")
    Call<RegisterModel> getRegisterUser(@Field("username") String username,
                                        @Field("email") String email,
                                        @Field("mobile") String mobile,
                                        @Field("password") String password,
                                        @Field("firebase_id") String firebase_id,
                                        @Field("device_id") String device_id);

    @FormUrlEncoded
    @POST("signin")
    Call<LoginModel> getUserLogin(@Field("email") String email,
                                  @Field("password") String password,
                                  @Field("firebase_id") String firebase_id,
                                  @Field("device_id") String device_id);

    @FormUrlEncoded
    @POST("forgot_password")
    Call<ForgotPasswordModel> getResetLink(@Field("email") String email);

    @FormUrlEncoded
    @POST("reset_password")
    Call<ResetPasswordModel> getResetPasswrod(@Field("id") String id,
                                              @Field("oldpass") String oldpass,
                                              @Field("newpass") String newpass);

    @FormUrlEncoded
    @POST("InsertNotes")
    Call<MoodInsertModel> SetMoodDetails(@Field("user_id") String user_id,
                                         @Field("date") String date,
                                         @Field("time") String time,
                                         @Field("mode") String mode,
                                         @Field("activities") String activities,
                                         @Field("notes") String notes);

    @FormUrlEncoded
    @POST("GetNotes")
    Call<GetMoodNotesModel> getMoodDetails(@Field("user_id") String user_id);

    /*@FormUrlEncoded
    @POST("insertcall_duration")
    Call<CallLogsInsertModel> getInsertCallLogs(@Field("user_id") String user_id,
                                                @Field("date") String date,
                                                @Field("mobile") String mobile,
                                                @Field("call_type") String call_type,
                                                @Field("total_call") String total_call,
                                                @Field("total_missedcall") String total_missedcall,
                                                @Field("call_duration") String call_duration,
                                                @Field("total_callduration") String total_callduration);*/


   /* @FormUrlEncoded
    @POST("insertcall_duration")
    Call<String> InsertCallData(@Field("gsonObject") String gsonObject,
                              @Field("user_id") String user_id);*/

    @FormUrlEncoded
    @POST("getcall_duration")
    Call<AllCallsDataModel> getAllCallData(@Field("user_id") String user_id,
                                           @Field("date") String date);

    @FormUrlEncoded
    @POST("social_timespent")
    Call<String> InsertSocialData(@Field("user_id") String user_id,
                                  @Field("date") String date,
                                  @Field("gsonObject") String gsonObject);

    @FormUrlEncoded
    @POST("insertcall_duration")
    Call<String> sendLocation(@Field("gsonObject") String gsonObject,
                              @Field("user_id") String user_id,
                              @Field("date") String date);

    @FormUrlEncoded
    @POST("remaining_call")
    Call<RemainingCallModel> remainingCall(@Field("user_id") String user_id,
                                           @Field("date") String date);

    @FormUrlEncoded
    @POST("CountingAppTime")
    Call<GetSocialUsageListModel> CallSocialList(@Field("user_id") String user_id,
                                                 @Field("date") String date);

    @FormUrlEncoded
    @POST("InsertContact")
    Call<NewContackGetModel> getNewContact(@Field("user_id") String user_id,
                                           @Field("date") String date,
                                           @Field("contact_name") String contact_name,
                                           @Field("contact_number") String contact_number);

    @FormUrlEncoded
    @POST("LoginWithGmail")
    Call<LoginWithGmailModel> loginWithGmail(@Field("email") String email,
                                             @Field("username") String username,
                                             @Field("firebase_id") String firebase_id,
                                             @Field("device_id") String device_id);

    @FormUrlEncoded
    @POST("ImageCountData")
    Call<ImageCountModel> getImageCount(@Field("user_id") String user_id,
                                        @Field("date") String date,
                                        @Field("total") String total);

    @FormUrlEncoded
    @POST("CountUserdata")
    Call<NewAddedCountactCountModel> getNewAddedCountactCount(@Field("user_id") String user_id,
                                                              @Field("date") String date);

    @FormUrlEncoded
    @POST("getsms_count")
    Call<GetSMSCountModel> getSMSCount(@Field("user_id") String user_id,
                                       @Field("date") String date);

    @FormUrlEncoded
    @POST("insert_sms")
    Call<SendSMSCountModel> InsertSMS(@Field("user_id") String user_id,
                                      @Field("date") String date,
                                      @Field("sendor_number") String sendor_number);

    @FormUrlEncoded
    @POST("getcalories_data")
    Call<GetCaloriesModel> getCaloriesData(@Field("user_id") String user_id,
                                           @Field("date") String date);

    @FormUrlEncoded
    @POST("test1")
    Call<String> InsertGoogleFitActivity(@Field("user_id") String user_id,
                                         @Field("date") String date,
                                         @Field("gsonObject") String gsonObject);
    @FormUrlEncoded
    @POST("test2")
    Call<GetGooGleFitActivityModel> getActivityData(@Field("user_id") String user_id,
                                                    @Field("date") String date);

    @FormUrlEncoded
    @POST("insert_calories")
    Call<CaloriesDataModel> InsertCalories(@Field("user_id") String user_id,
                                           @Field("date") String date,
                                           @Field("calories") String calories);
}
