package app.food.patient_app.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;

import app.food.patient_app.R;
import app.food.patient_app.model.LoginModel;
import app.food.patient_app.model.LoginWithGmailModel;
import app.food.patient_app.sessionmanager.SessionManager;
import app.food.patient_app.util.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edt_LoginEmail, edt_LoginPwd;
    Button btn_Login;
    TextView txt_Forgot, txt_SignUp;
    private static final String TAG = "LoginActivity";
    String mLoginEmail, mLoginPwd, mFirebaseId, mDeviceId;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    SessionManager sessionManager;
    SignInButton SignIn_Google;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;
    String personName, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
        getClick();
    }

    private void initialize() {
        getFilePaths();
        sessionManager = new SessionManager(LoginActivity.this);
        edt_LoginEmail = findViewById(R.id.edt_login_email);
        edt_LoginPwd = findViewById(R.id.edt_login_pwd);
        btn_Login = findViewById(R.id.btn_login);
        mDeviceId = Settings.Secure.getString(LoginActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        mFirebaseId = FirebaseInstanceId.getInstance().getToken();
        txt_Forgot = findViewById(R.id.txt_forgot);
        txt_SignUp = findViewById(R.id.txt_SignUp);

        SignIn_Google = findViewById(R.id.signup_google);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void getClick() {
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValue();
            }
        });
        txt_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        txt_Forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
        SignIn_Google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signOut();
            }
        });
    }

    private void getValue() {
        mLoginEmail = edt_LoginEmail.getText().toString().trim();
        mLoginPwd = edt_LoginPwd.getText().toString().trim();
        getValidate();
    }

    private void getValidate() {
        if (mLoginEmail.equalsIgnoreCase("")) {
            edt_LoginEmail.setError("Required");
            edt_LoginEmail.setFocusable(true);
        } else if (mLoginPwd.equalsIgnoreCase("")) {
            edt_LoginPwd.setError("Required");
            edt_LoginPwd.setFocusable(true);
        } else if (mLoginEmail.matches(emailPattern)) {
            getLoginUser();
        } else {
            edt_LoginEmail.setError("Invalid email address");
            edt_LoginEmail.setFocusable(true);
            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();

        }
    }

    private void getLoginUser() {
        Constant.setSession(LoginActivity.this);
        Constant.progressDialog(LoginActivity.this);
        Call<LoginModel> loginModelCall = Constant.apiService.getUserLogin(mLoginEmail, mLoginPwd, mFirebaseId, mDeviceId);

        loginModelCall.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                Constant.progressBar.dismiss();
                LoginModel.ResultBean resultBean = response.body().getResult().get(0);
                if (response.body().getStatus().equals("0")) {
                    sessionManager.CreateLoginSession(resultBean.getId(), resultBean.getUsername(), mLoginEmail, resultBean.getMobile(), mLoginPwd, resultBean.getFirebase_id(), resultBean.getDevice_id());
                    Toast.makeText(LoginActivity.this, response.body().getMessgae(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, NavigationActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, response.body().getMessgae(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Constant.progressBar.dismiss();
                Log.e(TAG, "onFailure: " + "API call Failed");
            }
        });
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
        signIn();

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());


            try {
                personName = acct.getDisplayName();
                email = acct.getEmail();
                Log.e(TAG, "Name: " + personName + ", email: " + email);
            } catch (Exception e) {
                e.printStackTrace();
            }

            LoginWithGmailAPICALL(personName, email);

        } else {
            Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
        }
    }

    private void LoginWithGmailAPICALL(final String personName, final String email) {
        Constant.setSession(LoginActivity.this);
        Constant.progressDialog(LoginActivity.this);
        Call<LoginWithGmailModel> login = Constant.apiService.loginWithGmail(email, personName,mFirebaseId,mFirebaseId);
        login.enqueue(new Callback<LoginWithGmailModel>() {
            @Override
            public void onResponse(Call<LoginWithGmailModel> call, Response<LoginWithGmailModel> response) {
                LoginWithGmailModel.ResultBean resultBean = response.body().getResult().get(0);
                Constant.progressBar.dismiss();
                sessionManager.CreateLoginSession(resultBean.getId(),personName, email, "", "",mFirebaseId, mFirebaseId);
                Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<LoginWithGmailModel> call, Throwable t) {
                Constant.progressBar.dismiss();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

    public void getFilePaths() {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID;
        //Stores all the images from the gallery in Cursor
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        //Total number of images
        int count = cursor.getCount();

        //Create an array to store path to all the images
        String[] arrPath = new String[count];
        Log.e(TAG, "getFilePaths:-----> "+count );
        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            //Store the path of the image
            arrPath[i] = cursor.getString(dataColumnIndex);
            Log.i("PATH", arrPath[i]);
        }
        cursor.close();


    }
}