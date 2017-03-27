package mickael.koc.channelmessaging2;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * Created by kocm on 06/02/2017.
 */
public class FriendsAdapter extends ArrayAdapter<User> {
    private final Context context;
    private final List<User> values;

    public FriendsAdapter(Context context, List<User> Value) {
        super(context, R.layout.activity_amis, Value);
        this.context = context;
        this.values = Value;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layoutamis, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.txtvamis);
        ImageView imgamis = (ImageView) rowView.findViewById(R.id.picamis);
        File dir = new File(Environment.getExternalStorageDirectory()+"/"+values.get(position).getImage()+"/");
        dir.mkdirs();
        File file = new File(dir, values.get(position).getImage()+".jpg");
        if(file.exists())
        {
            imgamis.setImageBitmap(BitmapFactory.decodeFile(file.toString()));
        }
        else {
            DownloadImageTask pic = new DownloadImageTask(values.get(position).getName(), values.get(position).getImage(), new OnDownloadCompleteListener() {
                @Override
                public void onDownloadComplete(String news, int param1) {

                }

                @Override
                public void onDownloadCompleteImg(String result) {

                }
            });
            pic.execute();
        }
        textView.setText(values.get(position).getName());
        return rowView;
    }

}
