import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Xu Zheng
 * @description
 */
public class HttpServer {
    public static void main(String[] args) {
        Socket client = null;
        try{
            int serverPort = 7777; // the server port we are using

            // Create a new server socket
            ServerSocket listenSocket = new ServerSocket(serverPort);
            Scanner in;
            PrintWriter out;
            while (true){
                client = listenSocket.accept();
                // Set up "in" to read from the client socket
                in = new Scanner(client.getInputStream());

                // Set up "out" to write to the client socket
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())));

                boolean isFirstLine = true;
                String data = in.nextLine();
                while (data!="") {
//                    System.out.println(in.hasNextLine());
                    if (isFirstLine) {
                        String[] strs = data.split(" ");
                        if (strs[0].equals("GET")) {
                            String file = strs[1];
                            try {
                                BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(new File("src"+file))));
                                String line = br1.readLine();
                                out.println("HTTP/1.1 200 OK\n\n");
                                while (line != null) {
                                    out.println(line);
                                    line = br1.readLine();
                                }
                                br1.close();
                                out.flush();
                            }catch (FileNotFoundException e){
                                out.println("HTTP/1.1 404 Not Found\n\n");
                                out.println("<html><head></head><body><h1>404 Not Found</h1></body></html>");
                                out.flush();
                            }
                        } else {
                            //405
                            out.println("HTTP/1.1 405 Method Not Allowed\n\n");
                            out.flush();
                        }
                        isFirstLine = false;
                    }
                    data = in.nextLine();
                }
                System.out.println("finish one request");
                client.close();
                in.close();
                out.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
