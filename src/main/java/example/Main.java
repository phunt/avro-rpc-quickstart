/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package example;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.avro.ipc.SocketServer;
import org.apache.avro.ipc.SocketTransceiver;
import org.apache.avro.specific.SpecificRequestor;
import org.apache.avro.specific.SpecificResponder;
import org.apache.avro.util.Utf8;

import example.proto.Mail;
import example.proto.Message;

/**
 * Start a server, attach a client, and send a message.
 */
public class Main {
    public static class MailImpl implements Mail {
        // in this simple example just return details of the message
        public Utf8 send(Message message) {
            return new Utf8("Sent message to " + message.to.toString()
                            + " from " + message.from.toString()
                            + " with body " + message.body.toString());
        }
      }

    private static SocketServer server;
    private static void startServer() throws IOException {
        // the server implements the Mail protocol (MailImpl)
        server = new SocketServer(new SpecificResponder(
                Mail.class, new MailImpl()), new InetSocketAddress(0));
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Usage: <to> <from> <body>");
            System.exit(1);
        }

        // usually this would be another app, but for simplicity
        startServer();

        // client code - attach to the server and send a message
        SocketTransceiver client =
            new SocketTransceiver(new InetSocketAddress(server.getPort()));
        Mail proxy = (Mail) SpecificRequestor.getClient(Mail.class, client);

        // fill in the Message record and send it
        Message message = new Message();
        message.to = new Utf8(args[0]);
        message.from = new Utf8(args[1]);
        message.body = new Utf8(args[2]);
        System.out.println("Result: " + proxy.send(message));

        // cleanup
        client.close();
        server.close();
    }
}
