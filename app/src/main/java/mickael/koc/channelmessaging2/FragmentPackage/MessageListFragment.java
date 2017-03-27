package mickael.koc.channelmessaging2.FragmentPackage;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import mickael.koc.channelmessaging2.Datalayer;
import mickael.koc.channelmessaging2.Message;
import mickael.koc.channelmessaging2.MessageAdapter;
import mickael.koc.channelmessaging2.Messages;
import mickael.koc.channelmessaging2.R;
import mickael.koc.channelmessaging2.UserDataSource;
import mickael.koc.channelmessaging2.OnDownloadCompleteListener;

/**
 * Created by Anas on 17/03/2017.
 */

public class MessageListFragment extends Fragment implements OnDownloadCompleteListener, View.OnClickListener,AdapterView.OnItemClickListener {

    public static final String PREFS_NAME = "MyPrefsFile";
    private List<Message> lstmessage;
    public ListView lstvmessage;
    public Button btnsend;
    public EditText edtmsg;
    public ImageView img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 5);
            }
        }

        View v = inflater.inflate(R.layout.activity_message_fragment,container);
        lstvmessage = (ListView)v.findViewById(R.id.listView2);
        btnsend = (Button)v.findViewById(R.id.button2);
        btnsend.setOnClickListener(this);
        img = (ImageView)v.findViewById(R.id.imageView);
        lstvmessage.setOnItemClickListener(this);
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                SharedPreferences Gsettings = getActivity().getSharedPreferences(PREFS_NAME, 0);
                String access = Gsettings.getString("access", "");
                String channelid = Gsettings.getString("channelid","");
                HashMap<String,String> n = new HashMap<String,String>();
                n.put("url","http://www.raphaelbischof.fr/messaging/?function=getmessages");
                n.put("accesstoken",access);
                n.put("channelid",channelid);
                Datalayer d = new Datalayer(n);
                d.setOnNewsDownloadComplete(MessageListFragment.this);
                d.execute();
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(r, 1000);

        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 5: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity().getApplicationContext(),"Permission accordé",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),"Permission refusé",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity().getApplicationContext(),"Message envoyé",Toast.LENGTH_SHORT).show();
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
            lstvmessage.setAdapter(new MessageAdapter(getActivity().getApplicationContext(),lstmessage));
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
            edtmsg=(EditText) getActivity().findViewById(R.id.editText);
            SharedPreferences Gsettings = getActivity().getSharedPreferences(PREFS_NAME, 0);
            String access = Gsettings.getString("access", "");
            String channelid = Gsettings.getString("channelid","");
            HashMap<String,String> n = new HashMap<String,String>();
            n.put("url","http://www.raphaelbischof.fr/messaging/?function=sendmessage");
            n.put("accesstoken",access);
            n.put("channelid",channelid);
            n.put("message",edtmsg.getText().toString());//
            Datalayer d = new Datalayer(n,0,5);
            d.setOnNewsDownloadComplete(MessageListFragment.this);
            d.execute();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String pos = Integer.toString(position);
        String userID = Integer.toString(lstmessage.get(position).getUserID());
        String nameuser = lstmessage.get(position).getname();
        String imguser = lstmessage.get(position).getimg();
        Toast.makeText(getActivity().getApplicationContext(),userID+" "+nameuser+"  "+imguser,Toast.LENGTH_SHORT).show();
        UserDataSource u = new UserDataSource(getContext());

        try {
            u.open();
            u.createUser(lstmessage.get(position).getUserID(),nameuser,imguser);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
