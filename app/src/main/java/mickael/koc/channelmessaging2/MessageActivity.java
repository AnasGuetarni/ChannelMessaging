package mickael.koc.channelmessaging2;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kocm on 27/01/2017.
 */
public class MessageActivity extends AppCompatActivity implements OnDownloadCompleteListener, View.OnClickListener,AdapterView.OnItemClickListener{

    public static final String PREFS_NAME = "MyPrefsFile";
    private List<Message> lstmessage;
    public ListView lstvmessage;
    public Button btnsend;
    public EditText edtmsg;
    public ImageView img;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation? (to avoid a never ask again response)
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        5);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        setContentView(R.layout.activity_message);
        lstvmessage = (ListView)findViewById(R.id.listView2);
        btnsend = (Button)findViewById(R.id.button2);
        btnsend.setOnClickListener(this);
        img = (ImageView)findViewById(R.id.imageView);
        lstvmessage.setOnItemClickListener(this);
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                SharedPreferences Gsettings = getSharedPreferences(PREFS_NAME, 0);
                String access = Gsettings.getString("access", "");
                String channelid = Gsettings.getString("channelid","");
                HashMap<String,String> n = new HashMap<String,String>();
                n.put("url","http://www.raphaelbischof.fr/messaging/?function=getmessages");
                n.put("accesstoken",access);
                n.put("channelid",channelid);
                Datalayer d = new Datalayer(n);
                d.setOnNewsDownloadComplete(MessageActivity.this);
                d.execute();
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(r, 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 5: {
                // If request is cancelled, the result arrays are empty.
                                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                                Toast.makeText(getApplicationContext(),"Permission accordé",Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(),"Permission refusé",Toast.LENGTH_SHORT).show();
                                }
                                return;
                            }
                // other 'case' lines to check for other
                // permissions this app might request
                        }
    }

    @Override
    public void onDownloadComplete(String news, int param2) {
        if(param2==5)
        {
            Toast.makeText(getApplicationContext(),"Message envoyé",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Gson gson = new Gson();
            Messages obj2 = gson.fromJson(news, Messages.class);
            lstmessage=obj2.getlist();

            int index = lstvmessage.getFirstVisiblePosition();

            View v = lstvmessage.getChildAt(0);

            int top = (v == null) ? 0 : (v.getTop() - lstvmessage.getPaddingTop());

            // SET ADAPTER
            lstvmessage.setAdapter(new MessageAdapter(getApplicationContext(),lstmessage));
            // restore index and position
            lstvmessage.setSelectionFromTop(index, top);


        }

    }

    @Override
    public void onDownloadCompleteImg(String result) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button2)
        {
            edtmsg=(EditText) findViewById(R.id.editText);
            SharedPreferences Gsettings = getSharedPreferences(PREFS_NAME, 0);
            String access = Gsettings.getString("access", "");
            String channelid = Gsettings.getString("channelid","");
            HashMap<String,String> n = new HashMap<String,String>();
            n.put("url","http://www.raphaelbischof.fr/messaging/?function=sendmessage");
            n.put("accesstoken",access);
            n.put("channelid",channelid);
            n.put("message",edtmsg.getText().toString());//
            Datalayer d = new Datalayer(n,0,5);
            d.setOnNewsDownloadComplete(MessageActivity.this);
            d.execute();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String pos = Integer.toString(position);
        String userID = Integer.toString(lstmessage.get(position).getUserID());
        String nameuser = lstmessage.get(position).getname();
        String imguser = lstmessage.get(position).getimg();
        Toast.makeText(getApplicationContext(),userID+" "+nameuser+"  "+imguser,Toast.LENGTH_SHORT).show();
        UserDataSource u = new UserDataSource(this);

        try {
            u.open();
            u.createUser(lstmessage.get(position).getUserID(),nameuser,imguser);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
