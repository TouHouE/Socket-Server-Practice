package server;

import network.Requests;
import network.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static network.Response.BODY_TYPE_HTML;
import static network.Response.BODY_TYPE_MEDIA;

public class HTTPServer {

    public static final String HTTP_GET = "GET", HTTP_POST = "POST", HTTP_DELETE = "DELETE", HTTP_PUT = "PUT";

    private ServerSocket server;
    private Socket socket;
    private InputStream is;
    private OutputStream os;


    public HTTPServer(int port) {
        try {
            server = new ServerSocket(port);
        } catch(IOException ioe) {

        }
    }

    public void run() {
        while(true) {
            try {
                socket = server.accept();
                is = socket.getInputStream();
                os = socket.getOutputStream();
                Requests req = getRequests();

                switch ((String) req.get("method")) {
                    case HTTP_GET:
                        GET(req);
                        break;
                    case HTTP_POST:
                        POST(req);
                        break;
                    case HTTP_DELETE:
                        DELETE(req);
                        break;
                    case HTTP_PUT:
                        PUT(req);
                        break;
                }


                is.close();
                os.close();
                socket.close();

            } catch(IOException ioe) {

            }

        }
    }

    private void showServerDetail() {
        int port = this.socket.getPort();
        String address = this.socket.getLocalAddress().getHostAddress();


    }

    public Requests getRequests() {
        int count = 0;

        while(count == 0) {
            try {
                count = is.available();
            } catch(IOException ioe) {

            }
        }
        byte dataBytes[] = new byte[count];
        try {
            is.read(dataBytes);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        String data = new String(dataBytes, StandardCharsets.UTF_8);
        System.out.printf("Data:%s", data);


        return new Requests(data);
    }

    private void GET(Requests r) {
        Response res = new Response();

        switch(r.get("path")) {
            case "/":
                res.putHeader("Content-Type", "text/html;charset=UTF-8");
                res.setHtml(new File("res/index.html"));
                res.setWebDataVariable("-!ip-", socket.getInetAddress().getHostAddress());
                write(res);
                break;
            case "img/.*":
                break;
        }

    }

    private void POST(Requests r) {

    }

    private void DELETE(Requests r) {

    }

    private void PUT(Requests r) {

    }

    private void write(Response res) {
        try {
            os.write(res.makeHeader().getBytes(StandardCharsets.UTF_8));
            switch (res.getBodyType()) {
                case BODY_TYPE_HTML:
                    os.write(res.getWeb().getBytes(StandardCharsets.UTF_8));
                    break;
                case BODY_TYPE_MEDIA:
                    os.write(res.getMedia());
                    break;
            }
        } catch(IOException ioe) {

        }

        System.out.printf("%s\n", res.showForPeople());
    }


}
