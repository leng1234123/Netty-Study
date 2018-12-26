package com.netty.protobuf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import org.junit.Test;

/**
 * 阻塞IO
 * @author lengyul
 *
 */
public class PlainOioServer {
	
	@Test
	public void server() throws IOException{
		new PlainOioServer().serve(9908);
	}
	
	@Test
	public void client() throws UnknownHostException, IOException, InterruptedException{
		
		Socket socket =new Socket("127.0.0.1",9908);
		InputStream in = socket.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
		System.out.println(reader.readLine());
		socket.close();
	}
	
	public void serve(int port) throws IOException {
        final ServerSocket socket = new ServerSocket(port);     //1
        try {
            for (;;) {
                final Socket clientSocket = socket.accept();    //2
                System.out.println("Accepted connection from " + clientSocket);

                new Thread(new Runnable() {                        //3
                    @Override
                    public void run() {
                        OutputStream out;
                        try {
                            out = clientSocket.getOutputStream();
                            out.write("Hi!\r\n".getBytes(Charset.forName("UTF-8")));                            //4
                            out.flush();
                            clientSocket.close();                //5

                        } catch (IOException e) {
                            e.printStackTrace();
                            try {
                                clientSocket.close();
                            } catch (IOException ex) {
                                // ignore on close
                            }
                        }
                    }
                }).start();                                        //6
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
}
