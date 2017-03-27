package mickael.koc.channelmessaging2;

/**
 * Created by kocm on 27/01/2017.
 */
public class Message {
    private int userID;
    private String username;
    private String message;
    private String date;
    private String imageUrl;
    private double latitude;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    private double longitude;
    private String messageImageUrl;
    private String soundUrl;
    public String getname()
    {
        return this.username;
    }

    public String getMessage()
    {
        return this.message;
    }

    public String getdate()
    {
        return this.date;
    }
    public String getimg()
    {
        return this.imageUrl;
    }
}
