import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import network.Requests;
import network.Response;
import server.HTTPServer;

import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;

public class Main {

    public static void main(String[] args) {
        HTTPServer server = new HTTPServer(8087);
        server.run();



    }

    private static void try1() {
        ServerSocket server = getServer(8087);

        long counter = 0L;

        while(true) {
            System.out.printf("Round:%d\n", counter++);

            try {
                Socket socket = server.accept();
                System.out.printf("Socket accept.\n");

                OutputStream os = socket.getOutputStream();
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader read = new BufferedReader(isr);
                OutputStreamWriter osw = new OutputStreamWriter(os);

                int count = 0;

                while(count == 0) {
                    count = is.available();
                    System.out.printf("Count:%d\n", count);
                }

                byte fromIsData[] = new byte[count];

                is.read(fromIsData);
                String data = new String(fromIsData, StandardCharsets.UTF_8);


                Requests r = new Requests(data);
                System.out.printf("%s\n", r.makeString());
                System.out.printf("method:%s\n", r.get("method"));


                if(r.get("method").matches("GET")) {
                    Response response = root();

                    osw.write(response.makeHeader());
                    String ip = socket.getInetAddress().getHostAddress();
                    response.setWebDataVariable("-!ip-", ip);

                    osw.write(response.getWeb());


                    //osw.write(makeGETResponseMessage(r.get("path"), r.get("version")));
                    //System.out.printf("Sending successful\n");
                }
                osw.close();
                os.close();

                socket.close();

            } catch(IOException ioe){

            }

        }
    }


    private static Response root() {
        Response r = new Response();
        r.putHeader("Content-Type:", "text/html;charset=UTF-8");
        r.setHtml(new File("res/index.html"));

        return r;
    }


    private static ServerSocket getServer(int port) {
        ServerSocket server = null;

        try {
            server = new ServerSocket(port);
        } catch(IOException ioe) {

        }

        return server;
    }

}
