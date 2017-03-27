package mickael.koc.channelmessaging2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kocm on 23/01/2017.
 */
public class ChannelActivity extends AppCompatActivity implements OnDownloadCompleteListener,AdapterView.OnItemClickListener,View.OnClickListener {

    public static final String PREFS_NAME = "MyPrefsFile";
    public ListView lstchannel;
    public Button btnamis;
    private String[] listItems;// = {"item 1", "item 2 ", "list", "android", "item 3", "foobar", "bar", };
    private List<Channel> listChan;
    @Override
    protected void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        lstchannel = (ListView)findViewById(R.id.listView);
        btnamis = (Button)findViewById(R.id.btnamis);
        SharedPreferences Gsettings = getSharedPreferences(PREFS_NAME, 0);
        String access = Gsettings.getString("access", "");
        HashMap<String,String> n = new HashMap<String,String>();
        n.put("url","http://www.raphaelbischof.fr/messaging/?function=getchannels");
        n.put("accesstoken",access);
        Datalayer d = new Datalayer(n);
        d.setOnNewsDownloadComplete(this);
        btnamis.setOnClickListener(this) ;
        d.execute();
    }

    @Override
    public void onDownloadComplete(String news, int param2) {
        Gson gson = new Gson();
        Channels obj2 = gson.fromJson(news, Channels.class);
        obj2.toString();
        listChan=obj2.getChannels();
        lstchannel.setAdapter(new ChannelAdapter(getApplicationContext(),obj2.getChannels()));
        lstchannel.setOnItemClickListener(this);
        Toast.makeText(getApplicationContext(), news, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloadCompleteImg(String result) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String pos = Integer.toString(position);
        String chanID = Integer.toString(listChan.get(position).getid());
        Toast.makeText(getApplicationContext(),pos,Toast.LENGTH_SHORT).show();
        changeActivity();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("channelid", chanID);
        editor.commit();
    }



    public void changeActivity()
    {
        Intent myintent = new Intent(getApplicationContext(),MessageActivity.class);
        startActivity(myintent);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnamis)
        {
            Intent myintent = new Intent(getApplicationContext(),AmisActivity.class);
            startActivity(myintent);
            Toast.makeText(getApplicationContext(),"Good",Toast.LENGTH_SHORT).show();
        }
    }
}
