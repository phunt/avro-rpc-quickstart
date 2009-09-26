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

/**
 *
 */
public class Main {
    public static class MailImpl implements Mail {
        public Utf8 send(Message message) {
            return new Utf8("Send message to " + message.to.toString()
                            + " from " + message.from.toString()
                            + " with body " + message.body.toString());
        }
      }

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Usage: <to> <from> <body>");
        }

        SocketServer server = new SocketServer(new SpecificResponder(
                Mail.class, new MailImpl()), new InetSocketAddress(0));
        SocketTransceiver client =
            new SocketTransceiver(new InetSocketAddress(server.getPort()));
        Mail proxy = (Mail) SpecificRequestor.getClient(Mail.class, client);

        Mail.Message message = new Mail.Message();
        message.to = new Utf8(args[0]);
        message.from = new Utf8(args[1]);
        message.body = new Utf8(args[2]);
        System.out.println("Result: " + proxy.send(message));

        client.close();
        server.close();
    }
}
