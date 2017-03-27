package mickael.koc.channelmessaging2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by kocm on 06/02/2017.
 */
public class AmisActivity extends AppCompatActivity {

    private ListView amislist;
    public AmisActivity()
    {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amis);
        amislist = (ListView)findViewById(R.id.lstvamis);
        UserDataSource u = new UserDataSource(this);
        try {
            u.open();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        amislist.setAdapter(new FriendsAdapter(getApplicationContext(),u.getAllUser()));
    }
}
