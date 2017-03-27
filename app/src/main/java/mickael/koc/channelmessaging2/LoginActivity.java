package mickael.koc.channelmessaging2;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements OnDownloadCompleteListener, View.OnClickListener {

    private Button btnvalider;
    private EditText edtlogin;
    private TextView txtdef;
    private ImageView logo;
    private EditText edtpassword;
    public static final String PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String message = "";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnvalider = (Button) findViewById(R.id.button);
        edtlogin = (EditText) findViewById(R.id.edtlogin);
        logo = (ImageView)findViewById(R.id.imageView2);
        txtdef = (TextView)findViewById(R.id.txtdef);
        edtpassword = (EditText) findViewById(R.id.edtpassword);
        btnvalider.setOnClickListener(this);

        final Handler mHandlerTada = new Handler();
        final int mShortDelay = 4000; //milliseconds
        final LinearLayout rl = (LinearLayout) findViewById(R.id.llBackground);
        //final FlowingGradientClass grad = new FlowingGradientClass();
        mHandlerTada.postDelayed(new Runnable(){
            public void run(){
/*
                grad.setBackgroundResource(R.drawable.translate)
                        .onLinearLayout(rl)
                        .setTransitionDuration(4000)
                        .start();*/
                txtdef.clearAnimation();
                YoYo.with(Techniques.Tada)
                        .duration(700)
                        .repeat(1)
                        .playOn(findViewById(R.id.imageView2));
                mHandlerTada.postDelayed(this, mShortDelay);
            }
        }, mShortDelay);

    }



    @Override
    public void onDownloadComplete(String news,int param2) {
       //MARCHE :
        // Toast.makeText(getApplicationContext(),news,Toast.LENGTH_SHORT).show();
        Gson gson = new Gson();
        Jsonlogin obj2 = gson.fromJson(news, Jsonlogin.class);

        if(obj2.code==200) {
            //================SHARED PREFERENCE (LIKE $_SESSION)
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("access", obj2.getaccess()); // Commit the edits! editor.commit();
            editor.commit();
            //===================================
            //================GET PREFERENCE
            SharedPreferences Gsettings = getSharedPreferences(PREFS_NAME, 0);
            String access = Gsettings.getString("access", "");
            //===================================
            changeActivity();

        }
        else
        {
            Toast.makeText(getApplicationContext(), "Identifiant incorrect", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDownloadCompleteImg(String result) {

    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.button:
                    Animation animSlideLeft = AnimationUtils.loadAnimation(this, R.anim.slide_left);
                    txtdef.startAnimation(animSlideLeft);
                    HashMap<String,String> test = new HashMap<String, String>();
                    test.put("url","http://www.raphaelbischof.fr/messaging/?function=connect");
                    test.put("username",this.edtlogin.getText().toString());
                    test.put("password",this.edtpassword.getText().toString());
                    Datalayer d = new Datalayer(test);
                    d.setOnNewsDownloadComplete(this);
                    d.execute();


                break;


        }


    }

    public void changeActivity()
    {
        Intent loginIntent = new Intent(LoginActivity.this, ChannelActivity.class);
        startActivity(loginIntent, ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, logo, "logo").toBundle());
      /*  Intent myintent = new Intent(getApplicationContext(),ChannelActivity.class);
        startActivity(myintent);*/
    }

}
