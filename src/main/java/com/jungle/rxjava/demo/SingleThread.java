package com.jungle.rxjava.demo;


import sun.misc.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleThread {
    public static final byte[] RESPONSE = ("HTTP/1.1 OK\r\nContent-length: 2\r\n\r\nOK").getBytes();

    public static void main(String[] args) throws IOException {
        try (ServerSocket socket = new ServerSocket(8080, 100);) {
            while (!Thread.currentThread().isInterrupted()) {
                Socket accept = socket.accept();
                handle(accept);
            }
        }
    }

    private static void handle(Socket accept) throws IOException {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                readFullRequest(accept);
                accept.getOutputStream().write(RESPONSE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            accept.close();
        }


    }

    private static void readFullRequest(Socket accept) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(accept.getInputStream()));) {
            String line = bufferedReader.readLine();
            while (line != null && !line.isEmpty()) {
                line = bufferedReader.readLine();
            }

        } catch (Exception ignored) {

        }


    }

}
