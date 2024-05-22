package ex03;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
public class threadDownload extends Thread{
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    private final Integer threadIndex;
    private final Integer fileIndex;
    private final String link;

    public threadDownload(String link, int fileNumber, int threadIndex ){
        this.threadIndex = threadIndex+1;
        this.fileIndex = fileNumber+1;
        this.link = link;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        System.out.println("Thread-" + threadIndex + " start download file number " + fileIndex + " at " + startTime);

        try {
            URI uri = new URI(link);
            URL url = uri.toURL();
            InputStream in = new BufferedInputStream(url.openStream());
            String fileName = link.substring(link.lastIndexOf('/') + 1);
            FileOutputStream out = new FileOutputStream(fileName);
            byte[] buffer = new byte[1024];
            int ret;
            while ((ret = in.read(buffer, 0, 1024)) != -1) {
                out.write(buffer, 0, ret);
            }
            in.close();
            out.close();
            long endTime = System.currentTimeMillis();
            LogHelper.log("Thread-" + threadIndex + " finish download file number " + fileIndex + " at " + endTime + ", duration: " + (endTime - startTime) + " ms");
        } catch (Exception e) {
            System.out.println(ANSI_RED+"Error downloading file " + fileIndex + ": "+ ANSI_RESET + e.getMessage());
        }
    }

}
