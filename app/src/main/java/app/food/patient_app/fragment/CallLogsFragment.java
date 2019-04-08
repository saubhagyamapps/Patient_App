package app.food.patient_app.fragment;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.wickerlabs.logmanager.LogObject;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.food.patient_app.R;
import app.food.patient_app.adapter.AppUsageMonthlyAdaptar;
import app.food.patient_app.model.AllCallsDataModel;
import app.food.patient_app.model.CalllogsListModel;
import app.food.patient_app.model.GetSMSCountModel;
import app.food.patient_app.model.GetSocialUsageListModel;
import app.food.patient_app.model.ImageCountModel;
import app.food.patient_app.model.NewAddedCountactCountModel;
import app.food.patient_app.receiver.MessageListener;
import app.food.patient_app.receiver.MessageReceiver;
import app.food.patient_app.util.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallLogsFragment extends Fragment implements MessageListener {
    View mView;
    private static final int SOME_VALUE_TO_START_APP_ONE = 0;
    String[] selection;
    String calltype = null;
    TextView textView = null;
    int number, type, duration;
    String phNumber, callType, callDuration;
    Date callDate, date;
    Date callDayTime;
    String mName;
    String result;
    JsonObject jsonObject;
    private static final int READ_LOGS = 725;
    private ListView logList;
    private Runnable logsRunnable;
    private String[] requiredPermissions = {Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS};
    LogObject logObject;
    ArrayList contactInfoArray;
    ArrayList ContactName;
    private static final String TAG = "CallLogsFragment";
    CalllogsListModel calllogsListModel;
    ArrayList<CalllogsListModel> calllogsListModels = new ArrayList<>();
    JSONObject singObj;
    String mMonthId = "";
    TextView txt_Date, txt_Total_Calls, txt_Total_Duration, txt_Total_MissedCall;
    Date todayDate;
    String mCurrentDate;
    RecyclerView recyclerView_apps;
    AppUsageMonthlyAdaptar appUsageMonthlyAdaptar;
    TextView txtToday_Add_contact, txtToday_Add_images,txt_current_date,txt_Sms_Count;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_call_logs, container, false);
        getActivity().setTitle("Social interaction");
        initializ();
        //getCallsData();
        return mView;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initializ() {
        Constant.setSession(getActivity());
        todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mCurrentDate = df.format(todayDate);
        ContactName = new ArrayList();
        contactInfoArray = new ArrayList<>();
        txtToday_Add_contact = mView.findViewById(R.id.txtToday_Add_contact);
        txtToday_Add_images = mView.findViewById(R.id.txtToday_Add_images);
        txt_current_date = mView.findViewById(R.id.txt_current_date);
        txt_Date = mView.findViewById(R.id.txt_date);
        txt_Total_Calls = mView.findViewById(R.id.txt_totalcalls);
        txt_Total_Duration = mView.findViewById(R.id.txt_callsduration);
        txt_Total_MissedCall = mView.findViewById(R.id.txt_totalmissedcall);
        txt_Sms_Count = (TextView) mView.findViewById(R.id.txt_sms_count);

        recyclerView_apps = mView.findViewById(R.id.app_recycle);
        //appUsageMonthlyAdaptar = new AppUsageMonthlyAdaptar(getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_apps.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView_apps.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider, getActivity().getTheme()));

        MessageReceiver.bindListener(this);
        getCallsData();
        getMonthlyUsage();
        getNewCountactList();
        getFilePaths(getActivity());
        GetSMSCount();
    }

    public void getFilePaths(Context context) {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID;
        //Stores all the images from the gallery in Cursor
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        //Total number of images
        int count = cursor.getCount();

        //Create an array to store path to all the images
        String[] arrPath = new String[count];
        Log.e(TAG, "getFilePaths:-----> " + count);
        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            //Store the path of the image
            arrPath[i] = cursor.getString(dataColumnIndex);
            Log.i("PATH", arrPath[i]);
        }
        APICALLIMAGESCOUNT(count);

        cursor.close();


    }

    private void APICALLIMAGESCOUNT(int count) {
        txt_current_date.setText(mCurrentDate);
        Call<ImageCountModel> countModelCall = Constant.apiService.getImageCount(Constant.mUserId, mCurrentDate, String.valueOf(count));
        countModelCall.enqueue(new Callback<ImageCountModel>() {
            @Override
            public void onResponse(Call<ImageCountModel> call, Response<ImageCountModel> response) {
                try {
                    if (response.body().getStatus().equals("0")) {
                        txtToday_Add_images.setText("Today " + response.body().getResult().get(0).getCount() + " new images add");
                    }
                }catch (Exception e){

                }

            }

            @Override
            public void onFailure(Call<ImageCountModel> call, Throwable t) {
                Log.e(TAG, "onFailure: ->APICALLIMAGESCOUNT"+t.getMessage() );
            }
        });
    }

    private void getNewCountactList() {

        Call<NewAddedCountactCountModel> addedCountactCountModelCall=Constant.apiService.getNewAddedCountactCount(Constant.mUserId,mCurrentDate);
        addedCountactCountModelCall.enqueue(new Callback<NewAddedCountactCountModel>() {
            @Override
            public void onResponse(Call<NewAddedCountactCountModel> call, Response<NewAddedCountactCountModel> response) {
                txtToday_Add_contact.setText("Today " + response.body().getCount() + " new Contact add");
            }

            @Override
            public void onFailure(Call<NewAddedCountactCountModel> call, Throwable t) {

            }
        });
    }

    private void getCallsData() {
        Constant.setSession(getActivity());
        Constant.progressDialog(getActivity());
        Call<AllCallsDataModel> allCallsDataModelCall = Constant.apiService.getAllCallData(Constant.mUserId, mCurrentDate);
        allCallsDataModelCall.enqueue(new Callback<AllCallsDataModel>() {
            @Override
            public void onResponse(Call<AllCallsDataModel> call, Response<AllCallsDataModel> response) {
                Constant.progressBar.dismiss();

                String Duration_call = "";
                float sum = Integer.parseInt(response.body().getTotal_callduration());
                if (sum >= 0 && sum < 3600) {

                    result = String.valueOf(sum / 60);
                    String decimal = result.substring(0, result.lastIndexOf("."));
                    String point = "0" + result.substring(result.lastIndexOf("."));

                    int minutes = Integer.parseInt(decimal);
                    float seconds = Float.parseFloat(point) * 60;

                    DecimalFormat formatter = new DecimalFormat("#");
                    if (minutes == 0) {
                        Duration_call = formatter.format(seconds) + " secs";
                    } else {
                        Duration_call = minutes + " min " + formatter.format(seconds) + " secs";
                    }
                } else if (sum >= 3600) {

                    result = String.valueOf(sum / 3600);
                    String decimal = result.substring(0, result.lastIndexOf("."));
                    String point = "0" + result.substring(result.lastIndexOf("."));

                    int hours = Integer.parseInt(decimal);
                    float minutes = Float.parseFloat(point) * 60;

                    DecimalFormat formatter = new DecimalFormat("#");
                    Duration_call = hours + " hrs " + formatter.format(minutes) + " min";

                }
                txt_Date.setText(mCurrentDate);
                txt_Total_Calls.setText("Total number of calls  " + response.body().getTotal_call());
                txt_Total_Duration.setText("Total calls duration  " + Duration_call);
                txt_Total_MissedCall.setText("Total missed calls  " + response.body().getTotal_missedcall());
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<AllCallsDataModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                Constant.progressBar.dismiss();
            }
        });


    }

    private void getMonthlyUsage() {
        Constant.setSession(getActivity());
        Call<GetSocialUsageListModel> socialusageListModelCall = Constant.apiService.CallSocialList(Constant.mUserId, mCurrentDate);
        socialusageListModelCall.enqueue(new Callback<GetSocialUsageListModel>() {
            @Override
            public void onResponse(Call<GetSocialUsageListModel> call, Response<GetSocialUsageListModel> response) {
                if (response.body().getStatus().equals("0")) {
                    List<GetSocialUsageListModel.ResultBean> resultBeans = response.body().getResult();
                    appUsageMonthlyAdaptar = new AppUsageMonthlyAdaptar(getActivity(), resultBeans);
                    recyclerView_apps.setHasFixedSize(true);
                    appUsageMonthlyAdaptar.notifyDataSetChanged();
                    recyclerView_apps.setAdapter(appUsageMonthlyAdaptar);
                }
            }

            @Override
            public void onFailure(Call<GetSocialUsageListModel> call, Throwable t) {

            }
        });

    }

    @Override
    public void messageReceived(String message) {
        Toast.makeText(getActivity(), "New Message Received: " + message, Toast.LENGTH_SHORT).show();

    }
    private void GetSMSCount()
    {
        Constant.setSession(getActivity());
        Call<GetSMSCountModel> getSMSCountModelCall = Constant.apiService.getSMSCount(Constant.mUserId,mCurrentDate);
        getSMSCountModelCall.enqueue(new Callback<GetSMSCountModel>() {
            @Override
            public void onResponse(Call<GetSMSCountModel> call, Response<GetSMSCountModel> response) {

                if (response.body().getStatus().equals("0"))
                {
                    txt_Sms_Count.setText("Today's SMS Count   " +response.body().getCount());
                }
                else {
                    Log.e(TAG, "onResponse: "+"cannot fetch count");
                }
            }

            @Override
            public void onFailure(Call<GetSMSCountModel> call, Throwable t) {

            }
        });
    }
}

















/*    private void getCallDetails() {

        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = getActivity().managedQuery(CallLog.Calls.CONTENT_URI, null, null,
                null, null);
        int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        sb.append("Call Log :");
        while (managedCursor.moveToNext()) {

            String Name = managedCursor.getString(name);
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));

            StringTokenizer tk = new StringTokenizer(String.valueOf(callDayTime));
            String day = tk.nextToken();
            String Month = tk.nextToken();
            String Date = tk.nextToken();
            String Time = tk.nextToken();
            String TIMEZONE = tk.nextToken();
            String Year = tk.nextToken();

            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;

            }

            if (Name != null && !Name.isEmpty() && !Name.equals("null")) {
                mName = Name;
            } else {
                mName = "Unknown";
            }if (Month.equals("Jan")) {
                mMonthId = "01";
            } else if (Month.equals("Feb")) {
                mMonthId = "02";
            } else if (Month.equals("Mar")) {
                mMonthId = "03";
            } else if (Month.equals("Apr")) {
                mMonthId = "04";
            } else if (Month.equals("May")) {
                mMonthId = "05";
            } else if (Month.equals("Jun")) {
                mMonthId = "06";
            } else if (Month.equals("Jul")) {
                mMonthId = "07";
            } else if (Month.equals("Aug")) {
                mMonthId = "08";
            } else if (Month.equals("Sep")) {
                mMonthId = "09";
            } else if (Month.equals("Oct")) {
                mMonthId = "10";
            } else if (Month.equals("Nov")) {
                mMonthId = "11";
            } else if (Month.equals("Dec")) {
                mMonthId = "12";
            }
            calllogsListModel = new CalllogsListModel(mName, phNumber, callType, Year+"-"+mMonthId+"-"+Date, Time, callDuration);

            sb.append("\nName:---" + Name + "\nPhone Number:--- " + phNumber + " \nCall Type:--- "
                    + dir + " \nCall Date:--- " + callDayTime +
                    " \nCall duration in sec :--- " + callDuration);
            sb.append("\n----------------------------------");
            calllogsListModels.add(calllogsListModel);


        }


        JSONArray contaArray = new JSONArray();
        for (int i = 0; i < calllogsListModels.size(); i++) {
            try {
                singObj = new JSONObject();
                singObj.put("name", calllogsListModels.get(i).getName());
                singObj.put("contactno", calllogsListModels.get(i).getPhone_number());
                singObj.put("type", calllogsListModels.get(i).getCall_type());
                singObj.put("duration", calllogsListModels.get(i).getCall_duration());
                singObj.put("time", calllogsListModels.get(i).getCall_time());
                singObj.put("date", calllogsListModels.get(i).getCall_date());
                contaArray.put(singObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

      *//*  jsonObject=new JsonObject();
        JsonParser pa=new JsonParser();
        JsonElement aa=  pa.parse("k");*//*
        // Log.d("msg", contaArray.toString());


        JsonParser parser = new JsonParser();
        JsonArray array = (JsonArray) parser.parse(contaArray.toString());
        org.json.JSONObject object = singObj;
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(object.toString());
        // textView.setText(sb);
        Log.e(TAG, "getCallDetails: " + array.toString());

        apicall(array);
    }

    private void apicall(JsonArray gsonObject) {
        Log.e(TAG, "apicall: " + Constant.mUserId);
        Constant.setSession(getActivity());
        Call<String> call = Constant.apiService.InsertCallData(gsonObject.toString(), Constant.mUserId);
        call.enqueue(new Callback<String>() {
                         @Override
                         public void onResponse(Call<String> call, Response<String> response) {
                             Log.d("response", "Getting response from server : " + response.message());
                             Log.e(TAG, "onResponse: " + response.message());
                         }

                         @Override
                         public void onFailure(Call<String> call, Throwable t) {
                             Log.d("response", "Getting response from server : " + t);
                         }
                     }
        );
    }*/


    /*StringTokenizer tk = new StringTokenizer(String.valueOf(callDayTime));
    String day = tk.nextToken();
    String Month = tk.nextToken();
    String Date = tk.nextToken();
    String Time = tk.nextToken();
    String TIMEZONE = tk.nextToken();
    String Year = tk.nextToken();*/



    /*float sum = Integer.parseInt(callDuration);
            if (sum >= 0 && sum < 3600) {

        result = String.valueOf(sum / 60);
        String decimal = result.substring(0, result.lastIndexOf("."));
        String point = "0" + result.substring(result.lastIndexOf("."));

        int minutes = Integer.parseInt(decimal);
        float seconds = Float.parseFloat(point) * 60;

        DecimalFormat formatter = new DecimalFormat("#");
        if (minutes == 0) {
        Duration_call = formatter.format(seconds) + " secs";
        } else {
        Duration_call = minutes + " min " + formatter.format(seconds) + " secs";
        }
        } else if (sum >= 3600) {

        result = String.valueOf(sum / 3600);
        String decimal = result.substring(0, result.lastIndexOf("."));
        String point = "0" + result.substring(result.lastIndexOf("."));

        int hours = Integer.parseInt(decimal);
        float minutes = Float.parseFloat(point) * 60;

        DecimalFormat formatter = new DecimalFormat("#");
        Duration_call = hours + " hrs " + formatter.format(minutes) + " min";

        }*/










  /*ContentResolver cr = getActivity().getContentResolver(); //Activity/Application android.content.Context
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            ArrayList<String> alContacts = new ArrayList<String>();
            do {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        alContacts.add(contactNumber);
                        break;
                    }
                    pCur.close();
                }

            } while (cursor.moveToNext());
            Log.e(TAG, "initializ: "+alContacts );
        }
*/

   /* private void initializ()
    {
        Constant.setSession(getActivity());
        logObject =new LogObject();
        logList = mView.findViewById(R.id.LogsList);
        logsRunnable = new Runnable() {
            @Override
            public void run() {
                loadLogs();
            }
        };
            callType = String.valueOf(logObject.getType());
            callDuration = String.valueOf(logObject.getDuration());
        // Checking for permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissionToExecute(requiredPermissions, READ_LOGS, logsRunnable);
        } else {
            logsRunnable.run();
        }

    }
    private void loadLogs() {
        LogsManager logsManager = new LogsManager(getActivity());
        List<LogObject> callLogs = logsManager.getLogs(LogsManager.ALL_CALLS);

        LogsAdapter logsAdapter = new LogsAdapter(getActivity(), R.layout.log_layout, callLogs);
        logList.setAdapter(logsAdapter);
    }
*/

   /* private void setCallDetails()
    {
        Constant.progressDialog(getActivity());
        Call<CallLogsInsertModel> callLogsInsertModelCall =
                Constant.apiService.getInsertCallLogs(Constant.mUserId,"yyyy-mm-dd",logObject.getNumber(),callType,"25"
                        ,"10",callDuration,"1:05:12");

                callLogsInsertModelCall.enqueue(new Callback<CallLogsInsertModel>() {
                    @Override
                    public void onResponse(Call<CallLogsInsertModel> call, Response<CallLogsInsertModel> response) {
                        if (response.body().getStatus().equals("0"))
                        {
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CallLogsInsertModel> call, Throwable t) {

                    }
                });
    }

  //permission
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissionToExecute(String permissions[], int requestCode, Runnable runnable) {

        boolean logs = ContextCompat.checkSelfPermission(getActivity(), permissions[0]) != PackageManager.PERMISSION_GRANTED;
        boolean contacts = ContextCompat.checkSelfPermission(getActivity(), permissions[1]) != PackageManager.PERMISSION_GRANTED;

        if (logs || contacts) {
            requestPermissions(permissions, requestCode);
        } else {
            runnable.run();
        }
    }
    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_LOGS && permissions[0].equals(Manifest.permission.READ_CALL_LOG) && permissions[1].equals(Manifest.permission.READ_CONTACTS)) {
            if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED && grantResults[1] == PermissionChecker.PERMISSION_GRANTED) {
                logsRunnable.run();
            } else {
                new AlertDialog.Builder(getActivity())
                        .setMessage("The app needs these permissions to work, Exit?")
                        .setTitle("Permission Denied")
                        .setCancelable(false)
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                checkPermissionToExecute(requiredPermissions, READ_LOGS, logsRunnable);
                            }
                        })
                        .setNegativeButton("Exit App", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        }
    }*/

