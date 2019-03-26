package app.food.patient_app.model;

public class GetGooGleFitActivityModel {


    /**
     * status : 0
     * activity : 3
     * duration : 2249818
     * date : 2019-03-23
     * user_id : 20
     */

    private String status;
    private String activity;
    private String duration;
    private String date;
    private int user_id;

    public GetGooGleFitActivityModel(String activity, String duration) {
        this.activity = activity;
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
