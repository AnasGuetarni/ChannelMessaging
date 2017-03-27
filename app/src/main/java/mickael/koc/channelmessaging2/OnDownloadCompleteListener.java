package mickael.koc.channelmessaging2;

/**
 * Created by kocm on 20/01/2017.
 */
public interface OnDownloadCompleteListener {
    public void onDownloadComplete(String news,int param1);

    void onDownloadCompleteImg(String result);
}