package mickael.koc.channelmessaging2;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by kocm on 30/01/2017.
 */
class DownloadImageTask extends AsyncTask<String, Void, String> {
    private static final String TAG = DownloadImageTask.class.getSimpleName();
    public String bmImage;
    public String nameimage;
    public String path;
    public OnDownloadCompleteListener event;
    public DownloadImageTask(String url,String name,OnDownloadCompleteListener event) {
        this.bmImage=url;
        this.nameimage=name;
        this.event=event;
    }

    @Override
    protected String doInBackground(String... params) {

        File dir = new File(Environment.getExternalStorageDirectory()+"/"+this.nameimage+"/");
        try {


            dir.mkdirs();
            File file = new File(dir, this.nameimage+".jpg");
            downloadFromUrl(this.bmImage,file.getAbsolutePath());
            return file.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /* protected String doInBackground(String... urls) {
        try {
            downloadFromUrl(this.bmImage,this.nameimage);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.nameimage;
    }*/

    @Override
    protected void onPostExecute(String result) {
        this.event.onDownloadCompleteImg(result);
    }

    public void downloadFromUrl(String fileURL, String fileName) {
            try {
                URL url = new URL( fileURL);
                File file = new File(fileName);
                file.createNewFile();
                  /* Open a connection to that URL. */
                URLConnection ucon = url.openConnection();
                    /* Define InputStreams to read from the URLConnection.*/
                InputStream is = ucon.getInputStream();
                    /* Read bytes to the Buffer until there is nothing more to read(-1) and
                   write on the fly in the file.*/
                FileOutputStream fos = new FileOutputStream(file);
                final int BUFFER_SIZE = 23 * 1024;
                BufferedInputStream bis = new BufferedInputStream(is, BUFFER_SIZE);
                byte[] baf = new byte[BUFFER_SIZE];
                int actual = 0;
                while (actual != -1) {
                    fos.write(baf, 0, actual);
                    actual = bis.read(baf, 0, BUFFER_SIZE);
                }
                fos.close();
            } catch (IOException e) {
                    //TODO HANDLER
                Log.e(TAG, "Error while downloading image", e);
            }
        }
}