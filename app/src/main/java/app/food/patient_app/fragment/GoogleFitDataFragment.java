package app.food.patient_app.fragment;

import android.content.Context;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.fitness.result.DataSourcesResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import app.food.patient_app.R;
import app.food.patient_app.model.ArrayListGoogleFitModel;
import app.food.patient_app.model.CaloriesDataModel;
import app.food.patient_app.model.GetCaloriesModel;
import app.food.patient_app.model.GetGooGleFitActivityModel;
import app.food.patient_app.util.AppUtil;
import app.food.patient_app.util.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GoogleFitDataFragment extends Fragment implements OnDataPointListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    View mView;
    private static final String TAG = "GoogleFitDataFragment";
    private static final int REQUEST_OAUTH = 1;
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;
    private GoogleApiClient mApiClient;
    Date todayDate;
    String mCurrentDate;
    ArrayListGoogleFitModel googleFitJsonModel;
    private List<ArrayListGoogleFitModel> arrayListGoogleFitModelList;
    JSONObject singObj;
   // GoogleFitAdapter googleFitAdapter;
    List<GetGooGleFitActivityModel> getGooGleFitActivityModels;
    TextView txt_Date, txt_Activity_Name, txt_Duration, txt_Calories;
    String fieldName;
    String fieldvalue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_google_fit_data, container, false);
        getActivity().setTitle("Fitness data");
        initialize();
        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);

        }
        return mView;
    }

    private void initialize()
    {
        txt_Calories = mView.findViewById(R.id.txt_todays_calories);
        txt_Date = mView.findViewById(R.id.txt_fit_date);
        txt_Activity_Name = mView.findViewById(R.id.txt_fit_activity_name);
        txt_Duration = mView.findViewById(R.id.txt_fit_duration);
        arrayListGoogleFitModelList = new ArrayList<>();
        todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mCurrentDate = df.format(todayDate);

        mApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Fitness.SENSORS_API)
                .addApi(Fitness.RECORDING_API)
                .addApi(Fitness.HISTORY_API)
                .addApi(Fitness.CONFIG_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_BODY_READ))
                .addScope(new Scope(Scopes.FITNESS_BODY_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_LOCATION_READ_WRITE))
                .addConnectionCallbacks(this)
                .enableAutoManage(getActivity(), 0, this)
                .addOnConnectionFailedListener(this)
                .build();

        getActivityData();
       // getCaloriestData();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        //for Step Count//
        DataSourcesRequest dataSourceRequest = new DataSourcesRequest.Builder()
                .setDataTypes(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .setDataSourceTypes(DataSource.TYPE_RAW)
                .build();

        ResultCallback<DataSourcesResult> dataSourcesResultCallback = new ResultCallback<DataSourcesResult>() {
            @Override
            public void onResult(DataSourcesResult dataSourcesResult) {
                for (DataSource dataSource : dataSourcesResult.getDataSources()) {
                    if (DataType.TYPE_STEP_COUNT_CUMULATIVE.equals(dataSource.getDataType())) {
                        registerFitnessDataListener(dataSource, DataType.TYPE_STEP_COUNT_CUMULATIVE);
                    }
                }
            }
        };

        //Call Sensor API//

        Fitness.SensorsApi.findDataSources(mApiClient, dataSourceRequest)
                .setResultCallback(dataSourcesResultCallback);

        //Call Recording API

        Fitness.RecordingApi.subscribe(mApiClient, DataType.TYPE_ACTIVITY_SAMPLES).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    Log.i(TAG, "Success");
                } else {
                    Log.i(TAG, "There was a problem subscribing.");
                }
            }
        });


        //Call History API For Activity Code & Duration//

        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.DATE, -1);
        long startTime = cal.getTimeInMillis();
        java.text.DateFormat dateFormat = DateFormat.getDateInstance();
        Log.e("History", "Range Start: " + dateFormat.format(startTime));
        Log.e("History", "Range End: " + dateFormat.format(endTime));

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_ACTIVITY_SEGMENT, DataType.AGGREGATE_ACTIVITY_SUMMARY)
                //.aggregate(DataType.TYPE_CALORIES_EXPENDED,DataType.AGGREGATE_CALORIES_EXPENDED)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        Fitness.HistoryApi.readData(mApiClient, readRequest).setResultCallback(new ResultCallback<DataReadResult>() {
            @Override
            public void onResult(@NonNull DataReadResult dataReadResult) {

                if (dataReadResult.getBuckets().size() > 0) {
                    Log.i("MyApp", "Number of returned buckets of DataSets is: "
                            + dataReadResult.getBuckets().size());
                    for (Bucket bucket : dataReadResult.getBuckets()) {
                        List<DataSet> dataSets = bucket.getDataSets();
                        for (DataSet dataSet : dataSets) {
                            //showDataSet(dataSet);
                            DateFormat dateFormat = DateFormat.getDateInstance();
                            DateFormat timeFormat = DateFormat.getTimeInstance();
                            for (DataPoint dataPoint : dataSet.getDataPoints()) {
                                Log.e("History", "Data point:");
                                Log.e("History", "\tType: " + dataPoint.getDataType().getName());
                                Log.e("", "\tStart: " + dateFormat.format(dataPoint.getStartTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dataPoint.getStartTime(TimeUnit.MILLISECONDS)));
                                Log.e("", "\tEnd: " + dateFormat.format(dataPoint.getEndTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dataPoint.getStartTime(TimeUnit.MILLISECONDS)));
                                Log.e(TAG, "onResult: " + dataPoint.getDataType().getFields());

                                for (Field field : dataPoint.getDataType().getFields()) {
                                    fieldName = field.getName();
                                    fieldvalue = String.valueOf(dataPoint.getValue(field));

                                    Log.e("History", "\tField: " + field.getName() +
                                            " Value: " + dataPoint.getValue(field));
                                    googleFitJsonModel = new ArrayListGoogleFitModel(fieldName, fieldvalue);
                                    arrayListGoogleFitModelList.add(googleFitJsonModel);

                                }

                            }


                        }
                    }
                    JSONArray contaArray = new JSONArray();
                    for (int i = 0; i < arrayListGoogleFitModelList.size(); i++) {
                        try {
                            singObj = new JSONObject();
                            singObj.put("field_name", arrayListGoogleFitModelList.get(i).getField_name());
                            singObj.put("field_value", arrayListGoogleFitModelList.get(i).getField_value());


                            contaArray.put(singObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    JsonParser parser = new JsonParser();
                    JsonArray array = (JsonArray) parser.parse(contaArray.toString());
                    Log.e(TAG, "Array_LIst " + array.toString());
                    insertFitData(array);
                } else {
                    Log.i("MyApp", "No data");
                }
            }
        });

        // For Calories
        DataReadRequest readRequest1 = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)

                .build();
        Fitness.HistoryApi.readData(mApiClient, readRequest1).setResultCallback(new ResultCallback<DataReadResult>() {
            @Override
            public void onResult(@NonNull DataReadResult dataReadResult) {

                if (dataReadResult.getBuckets().size() > 0) {
                    Log.i("MyApp-->", "Number of returned buckets of DataSets is: "
                            + dataReadResult.getBuckets().size());
                    for (Bucket bucket : dataReadResult.getBuckets()) {
                        List<DataSet> dataSets = bucket.getDataSets();
                        for (DataSet dataSet : dataSets) {
                            //showDataSet(dataSet);
                            DateFormat dateFormat = DateFormat.getDateInstance();
                            DateFormat timeFormat = DateFormat.getTimeInstance();
                            for (DataPoint dataPoint : dataSet.getDataPoints()) {
                                Log.e("Calory", "Data point:");
                                Log.e("Calory", "\tType: " + dataPoint.getDataType().getName());
                                Log.e("Calory", "\tStart: " + dateFormat.format(dataPoint.getStartTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dataPoint.getStartTime(TimeUnit.MILLISECONDS)));
                                Log.e("Calory", "\tEnd: " + dateFormat.format(dataPoint.getEndTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dataPoint.getStartTime(TimeUnit.MILLISECONDS)));
                                Log.e(TAG, "onResult: " + dataPoint.getDataType().getFields());

                                for (Field field : dataPoint.getDataType().getFields()) {
                                    String activity_code = field.getName();
                                    String value = String.valueOf(dataPoint.getValue(field));
                                    Log.e(TAG, "nsdflnlfnsn " + activity_code + value);
                                    Log.e("Calory", "\tField: " + field.getName() +
                                            " Value: " + dataPoint.getValue(field));
                                    String Calories_Value = String.valueOf(dataPoint.getValue(field));
                                    Log.e(TAG, "Calories_Value: " + Calories_Value);
                                    InsertCaloriesData(Calories_Value);
                                }
                            }
                        }
                    }
                } else {
                    Log.i("MyApp", "No data");
                }
            }
        });

    }
    private void registerFitnessDataListener(DataSource dataSource, DataType dataType) {

        SensorRequest request = new SensorRequest.Builder()
                .setDataSource(dataSource)
                .setDataType(dataType)
                .setSamplingRate(3, TimeUnit.SECONDS)
                .build();

        Fitness.SensorsApi.add(mApiClient, request, this)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            Log.e("GoogleFit", "SensorApi successfully added");
                        }
                    }
                });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        if (!authInProgress) {
            try {
                authInProgress = true;
                connectionResult.startResolutionForResult(getActivity(), REQUEST_OAUTH);
            } catch (IntentSender.SendIntentException e) {

            }
        } else {
            Log.e("GoogleFit", "authInProgress");
        }
    }

    @Override
    public void onDataPoint(DataPoint dataPoint) {

        for (final Field field : dataPoint.getDataType().getFields()) {
            final Value value = dataPoint.getValue(field);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(getActivity(), "Field: " + field.getName() + " Value: " + value, Toast.LENGTH_SHORT).show();
                    // txt_Activityname_Steps.setText("Activity Type: " +  field.getName() + " " +value);
                    String Steps = String.valueOf(value);
                    //txt_Activity_Date.setText(mCurrentDate);
                    // InsertGoogleFitData(Steps);
                    Log.e(TAG, "run: " + value);

                }
            });


        }
    }
    private void getCaloriestData() {
        Constant.setSession(getActivity());
        Call<GetCaloriesModel> getGoogleFitDataCall = Constant.apiService.getCaloriesData(Constant.mUserId, mCurrentDate);
        getGoogleFitDataCall.enqueue(new Callback<GetCaloriesModel>() {
            @Override
            public void onResponse(Call<GetCaloriesModel> call, Response<GetCaloriesModel> response) {
                try {
                    if (response.body().getStatus().equals("0")) {
                        List<GetCaloriesModel.ResultBean> result = response.body().getResult();
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        txt_Calories.setText("Total Calories Expended:       " + result.get(0).getCalories());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<GetCaloriesModel> call, Throwable t) {

            }
        });
    }
    private void insertFitData(JsonArray s) {
        Constant.progressDialog(getActivity());
        Constant.setSession(getActivity());
        Call<String> stringCall = Constant.apiService.InsertGoogleFitActivity(Constant.mUserId, mCurrentDate, s.toString());
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Constant.progressBar.dismiss();
                Log.e("response", "Getting response from server : " + "Activity Data Inserted Successfully");
                Log.e(TAG, "onResponse: " + response.message());

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Constant.progressBar.dismiss();
                Log.d(TAG, "Getting response from server -->>>: " + "Activity Data insert failed");
                Log.d(TAG, "Getting response from server -->>>: " + t.getMessage());
            }
        });
    }

    private void InsertCaloriesData(String calories_Value) {

        Constant.setSession(getActivity());
        Call<CaloriesDataModel> caloriesDataModelCall = Constant.apiService.InsertCalories(Constant.mUserId, mCurrentDate, calories_Value);
        caloriesDataModelCall.enqueue(new Callback<CaloriesDataModel>() {
            @Override
            public void onResponse(Call<CaloriesDataModel> call, Response<CaloriesDataModel> response) {
                try {
                    if (response.body().getStatus().equals("0")) {
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                        getCaloriestData();
                    } else {
                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CaloriesDataModel> call, Throwable t) {

                Log.e(TAG, "onFailure: " + "Failed");
            }
        });

    }
    private void getActivityData() {
        Constant.setSession(getActivity());
        Call<GetGooGleFitActivityModel> getGooGleFitActivityModelCall = Constant.apiService.getActivityData(Constant.mUserId, mCurrentDate);
        getGooGleFitActivityModelCall.enqueue(new Callback<GetGooGleFitActivityModel>() {
            @Override
            public void onResponse(Call<GetGooGleFitActivityModel> call, Response<GetGooGleFitActivityModel> response) {
                try {
                    if (response.body().getStatus().equals("0")) {
                        if (response.body().getActivity().equals("3")) {
                            String Activity_code = "Still (Not Moving)";
                            txt_Activity_Name.setText("Activity Type:          " + Activity_code);
                        }
                        long Duration = Long.parseLong(response.body().getDuration());

                        txt_Duration.setText("Activity Duration:        " + String.format(getResources().getString(R.string.total), AppUtil.formatMilliSeconds(Duration)));
                        txt_Date.setText(response.body().getDate());


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetGooGleFitActivityModel> call, Throwable t) {
                Log.e(TAG, "onFailure--->>: " +t.getMessage() );
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mApiClient.stopAutoManage(getActivity());
        mApiClient.disconnect();
    }
}
