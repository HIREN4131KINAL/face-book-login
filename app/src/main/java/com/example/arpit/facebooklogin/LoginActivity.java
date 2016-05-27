package com.example.arpit.facebooklogin;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton fbLoginButton;
    static String Name, Email, ProfilePic, DOB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        //You need this method to be used only once to configure
        //your key hash in your App Console at
        // developers.facebook.com/apps

        showHashKey(LoginActivity.this);
        // creating connection detector class instance

        setContentView(R.layout.activity_login);
        fbLoginButton = (LoginButton) findViewById(R.id.login_button);
        fbLoginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));

            fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

                @Override
                public void onSuccess(LoginResult loginResult) {

                    // App code
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {
                                    // Application code
                                    Log.v("Profile ---------   ", response.toString());

                                    try {
                                        Name = object.getString("name");
                                        Email = object.getString("email");
                                        DOB = object.getString("birthday");
                                        Log.v("Email = ", " " + Email);
                                        //                                    Toast.makeText(getApplicationContext(), "Name " + Name, Toast.LENGTH_LONG).show();

                                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender, birthday");
                    request.setParameters(parameters);
                    request.executeAsync();

                    System.out.println("Facebook Login Successful!");
                    System.out.println("Logged in user Details : ");
                    System.out.println("--------------------------");
                    System.out.println("User ID  : " + loginResult.getAccessToken().getUserId());
                    System.out.println("Authentication Token : " + loginResult.getAccessToken().getToken());

                }


                @Override
                public void onCancel() {
                    Toast.makeText(LoginActivity.this, "Login cancelled by user!", Toast.LENGTH_LONG).show();
                    System.out.println("Facebook Login failed!!");

                }

                @Override
                public void onError(FacebookException e) {
                    Toast.makeText(LoginActivity.this, "Login unsuccessful!", Toast.LENGTH_LONG).show();
                    System.out.println("Facebook Login failed!! because of " + e.getCause().toString());
                }
            });

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    public static void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.example.arpit.facebooklogin", PackageManager.GET_SIGNATURES); //Your            package name here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent i) {
        callbackManager.onActivityResult(reqCode, resCode, i);
    }

}
