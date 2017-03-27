package mickael.koc.channelmessaging2;

/**
 * Created by kocm on 20/01/2017.
 */
public class Jsonlogin {
    private String response;
    public int code;
    private String accesstoken;

    Jsonlogin()
    {}

    public String getaccess()
    {
        if(this.accesstoken!=null)
            return this.accesstoken.toString();
        else
            return "erreur";
    }
    public int getcode() {
        if(this.code != 200)
            return this.code;
        else
            return 0;
    }
}
