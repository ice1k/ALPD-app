package util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageService {

    public static byte[] getImage(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setReadTimeout(10*1000);
        InputStream inputStream = conn.getInputStream();
        return StreamTool.readInputStream(inputStream);
    }
}
