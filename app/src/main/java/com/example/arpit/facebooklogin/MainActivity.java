package com.example.arpit.facebooklogin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    TextView Name,Email,DOB;
    ImageView ProfilePic;
    Button Logout;
    String ImgURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Name=(TextView)findViewById(R.id.Name);
        Email=(TextView)findViewById(R.id.Email);
        DOB=(TextView)findViewById(R.id.Dob);
        Logout = (Button)findViewById(R.id.btn_Logout);
        ProfilePic=(ImageView)findViewById(R.id.imageView);

        Name.setText("Name : "+LoginActivity.Name);
        Email.setText("Email : "+LoginActivity.Email);
        DOB.setText("DOB : "+LoginActivity.DOB);

        //For the profile picture
        try{
            Profile profile = Profile.getCurrentProfile();
            ImgURL = "https://graph.facebook.com/"+profile.getId()+"/picture?type=large";

            Picasso.with(MainActivity.this)
                    .load("" + ImgURL)
                    .into(ProfilePic);
        }catch (Exception e){

            e.printStackTrace();
        }

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "Logout clicked...", Toast.LENGTH_SHORT).show();
                //For Logout Facebook SDK
                LoginManager.getInstance().logOut();
                Intent intent =new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

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

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
