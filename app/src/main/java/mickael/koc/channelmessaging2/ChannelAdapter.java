package mickael.koc.channelmessaging2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by kocm on 27/01/2017.
 */
public class ChannelAdapter extends ArrayAdapter<Channel>{
    private final Context context;
    private final List<Channel>values;

    public ChannelAdapter(Context context,List<Channel> Value) {
        super(context, R.layout.activity_channel, Value);
        this.context = context;
        this.values = Value;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.textView3);
        TextView stextView = (TextView) rowView.findViewById(R.id.textView4);
       // TextView textView=(TextView) rowView;
      // values.get(position).getname();
        textView.setText("Channel : "+ values.get(position).getname());
        stextView.setText("Nb user connecté : "+ values.get(position).getnbuser());
        return rowView;
    }



}
