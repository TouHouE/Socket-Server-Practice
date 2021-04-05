package network;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.io.*;
import java.util.HashMap;


/**
 * Content-Type will be sat by outside
 */
public class Response {
    public static final String CRLF = "\r\n";
    public static final int BODY_TYPE_HTML = 0, BODY_TYPE_MEDIA = 1, BODY_TYPE_NONE = 2;

    private HashMap<String, String> headerMap = new HashMap<>();
    private byte[] data;
    private String webData;
    private boolean is404 = false;

    public void putHeader(String key, String value) {
        this.headerMap.put(key, value);
    }

    public void setHtml(File html) {
        String data = "", temp = "";

        try {
            FileReader fr = new FileReader(html);
            BufferedReader reader = new BufferedReader(fr);

            while((temp = reader.readLine()) != null) {
                data += temp;
            }

            this.webData = data;
            reader.close();
            fr.close();
        } catch(IOException ioe) {

        }


    }

    public void setWebDataVariable(String target, String word) {
        this.webData = this.webData.replaceAll(target, word);
    }

    public String getWeb() {
        if(is404) {
            File f404 = new File("res/404.html");
            String data = "";

            try {
                FileReader fr = new FileReader(f404);
                BufferedReader r = new BufferedReader(fr);
                String temp = "";

                while ((temp = r.readLine()) != null) {
                    data += temp;
                }
                r.close();
                fr.close();
            } catch (IOException ioe ){

            }
            return data;
        } else {
            return this.webData;
        }
    }

    public void setMedia(File file) {

    }

    public String makeHeader() {
        if(!is404) {
            String header = "HTTP/1.1 OK 200" + CRLF;
            setContentLength();
            for (String key : headerMap.keySet()) {
                header += String.format("%s: %s%s", key, headerMap.get(key), CRLF);
            }

            header += CRLF;
            return header;
        } else {
            String header = "HTTP/1.1 404" + CRLF + CRLF;
            return header;
        }


    }

    public void setContentLength() {
        if(webData == null) {
            this.headerMap.put("Content-Length", "" + data.length);
        } else if(data.length == 0) {
            this.headerMap.put("Content-Length", "" + webData.length());
        } else {

        }
    }

    public void set404() {
        this.is404 = true;
    }

    public int getBodyType() {
        if(webData != null) {
            return BODY_TYPE_HTML;
        } else if(data.length != 0) {
            return BODY_TYPE_MEDIA;
        } else {
            return BODY_TYPE_NONE;
        }
    }

    public void readMedia(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            int count = 0;

            while(count == 0 ) {
                count = fis.available();
            }
            data = new byte[count];
            fis.read(data);
            fis.close();


        } catch(IOException ioe) {

        }
    }

    public byte[] getMedia() {
        return this.data;
    }

    public String showForPeople() {
        String header = this.makeHeader();
        header += (webData != null) ? webData : data;


        return header;
    }
}
