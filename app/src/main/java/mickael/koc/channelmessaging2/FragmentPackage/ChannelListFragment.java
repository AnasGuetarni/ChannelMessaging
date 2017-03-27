package mickael.koc.channelmessaging2.FragmentPackage;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import mickael.koc.channelmessaging2.AmisActivity;
import mickael.koc.channelmessaging2.Channel;
import mickael.koc.channelmessaging2.ChannelActivity;
import mickael.koc.channelmessaging2.ChannelAdapter;
import mickael.koc.channelmessaging2.Channels;
import mickael.koc.channelmessaging2.Datalayer;
import mickael.koc.channelmessaging2.MessageActivity;
import mickael.koc.channelmessaging2.R;
import mickael.koc.channelmessaging2.OnDownloadCompleteListener;


public class ChannelListFragment extends Fragment implements AdapterView.OnItemClickListener, OnDownloadCompleteListener {
    private ListView lvFragment;
    public static final String PREFS_NAME = "MyPrefsFile";
    public ListView lstchannel;
    public Button btnamis;
    private String[] listItems;// = {"item 1", "item 2 ", "list", "android", "item 3", "foobar", "bar", };
    private List<Channel> listChan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_channel_fragment,container);
        super.onCreate(savedInstanceState);
        lstchannel = (ListView)v.findViewById(R.id.listView);
        btnamis = (Button)v.findViewById(R.id.btnamis);
        SharedPreferences Gsettings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        String access = Gsettings.getString("access", "");
        HashMap<String,String> n = new HashMap<String,String>();
        n.put("url","http://www.raphaelbischof.fr/messaging/?function=getchannels");
        n.put("accesstoken",access);
        Datalayer d = new Datalayer(n);
        d.setOnNewsDownloadComplete(this);
        btnamis.setOnClickListener((ChannelActivity)getActivity()) ;
        d.execute();
        return v;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lstchannel.setOnItemClickListener(this);
    }

    @Override
    public void onDownloadComplete(String news, int param2) {
        Gson gson = new Gson();
        Channels obj2 = gson.fromJson(news, Channels.class);
        obj2.toString();
        listChan=obj2.getChannels();
        lstchannel.setAdapter(new ChannelAdapter(getActivity().getApplicationContext(),obj2.getChannels()));
        lstchannel.setOnItemClickListener(this);
        Toast.makeText(getActivity().getApplicationContext(), news, Toast.LENGTH_SHORT).show();
    }

    public void onDownloadCompleteImg(String result) {

    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String pos = Integer.toString(position);
        String chanID = Integer.toString(listChan.get(position).getid());
        Toast.makeText(getActivity().getApplicationContext(),pos,Toast.LENGTH_SHORT).show();
        changeActivity();
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("channelid", chanID);
        editor.commit();
    }

    public void changeActivity()
    {
        Intent myintent = new Intent(getActivity().getApplicationContext(),MessageActivity.class);
        startActivity(myintent);
    }

    public void onClick(View v) {
        if(v.getId()==R.id.btnamis)
        {
            Intent myintent = new Intent(getActivity().getApplicationContext(),AmisActivity.class);
            startActivity(myintent);
            Toast.makeText(getActivity().getApplicationContext(),"Good",Toast.LENGTH_SHORT).show();
        }
    }
}

