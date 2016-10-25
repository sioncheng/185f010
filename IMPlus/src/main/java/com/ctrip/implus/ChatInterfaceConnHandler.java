package com.ctrip.implus;

import akka.actor.UntypedActor;
import akka.io.Tcp;
import akka.io.Tcp.Received;
import akka.io.Tcp.ConnectionClosed;
import akka.io.TcpMessage;
import akka.util.ByteString;
import scala.io.BufferedSource;
import scala.io.Source;

import java.io.File;

/**
 * Created by chengyq on 2016/10/25.
 */
public class ChatInterfaceConnHandler extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Received) {
            final ByteString data = ((Received) message).data();
            sb.append(data.decodeString("UTF8"));
            final int index = sb.indexOf("\r\n");
            if (index > 0) {
                final String firstLine = sb.substring(0, index).toLowerCase();
                final String res = processReq(firstLine);
                getSender().tell(TcpMessage.write(ByteString.fromString(res)), getSelf());
                getContext().stop(getSelf());
            }

        } else if (message instanceof ConnectionClosed) {
            final ConnectionClosed cc = (ConnectionClosed) message;
            System.out.println("connection closed " + cc.toString());
            getContext().stop(getSelf());
        }
    }

    private String processReq(String firstLine) {

        final String[] arr = firstLine.split(" ");

        String status = "HTTP/1.1 200 OK";
        String contentType = "Content-Type: text/html; charset=utf-8";
        String filename = "";

        if ("get".equals(arr[0])) {

            filename = arr[1];
            if ("/".equals(filename)) {
                filename = "index.html";
            }

            File file = new File("./src/main/resources/groupchat/" + filename);
            if (!file.exists()) {
                filename = "404.html";
                status = "HTTP/1.1 404 Not Found";
            }

            String[] filenameParts = filename.split("\\.");
            String ext = filenameParts[filenameParts.length - 1].toLowerCase();

            if (".js".equals(ext)) {
                contentType = "Content-Type: text/javascript; charset=utf-8";
            }
        } else {
            status = "HTTP/1.1 400 Bad Request";
            filename = "invalid.html";
        }

        String[] filenameParts = filename.split("\\.");
        String ext = filenameParts[filenameParts.length - 1].toLowerCase();

        if (".js".equals(ext)) {
            contentType = "Content-Type: text/javascript; charset=utf-8";
        }

        File file = new File("./src/main/resources/groupchat/" + filename);
        final BufferedSource bfs = Source.fromFile(file, "UTF8");

        String head = status
                + CRLF + "Cache-Control: no-cache"
                + CRLF + contentType
                + CRLF + "Content-Length: " + bfs.length()
                + CRLF + "Vary: Accept-Encoding"
                + CRLF + "Server: Microsoft-IIS/7.5";

        String body = Source.fromFile(file, "UTF8").mkString();

        return head + CRLF + CRLF + body;
    }


    private StringBuffer sb = new StringBuffer();

    private static final String CRLF = "\r\n";
}
