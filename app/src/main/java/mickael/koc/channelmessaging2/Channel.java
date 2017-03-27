package mickael.koc.channelmessaging2;

/**
 * Created by kocm on 23/01/2017.
 */
public class Channel {
    private int channelID;
    public String name;
    private int connectedusers;

    public String getname()
    {
        return this.name;
    }

    public int getnbuser() {
        return this.connectedusers;
    }

    public int getid() {
        return this.channelID;
    }
}
