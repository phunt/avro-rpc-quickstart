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

import example.proto.Calc;

/**
 *
 */
public class Main {
    public static class CalcImpl implements Calc {
        public Utf8 add(Utf8 arg1, Utf8 arg2) {
            return new Utf8(Integer.toString(Integer.parseInt(arg1.toString())
                + Integer.parseInt(arg2.toString())));
        }
        public Utf8 sub(Utf8 arg1, Utf8 arg2) {
            return new Utf8(Integer.toString(Integer.parseInt(arg1.toString())
                    - Integer.parseInt(arg2.toString())));
        }
      }

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Usage: add|sub <arg1> <arg2>");
        }

        SocketServer server = new SocketServer(new SpecificResponder(
                Calc.class, new CalcImpl()), new InetSocketAddress(0));
        SocketTransceiver client =
            new SocketTransceiver(new InetSocketAddress(server.getPort()));
        Calc proxy = (Calc) SpecificRequestor.getClient(Calc.class, client);

        if (args[0].equals("add")) {
            System.out.println("Result: " + proxy.add(new Utf8(args[1]),
                    new Utf8(args[2])));
        } else if (args[0].equals("sub")) {
            System.out.println("Result: " + proxy.sub(new Utf8(args[1]),
                    new Utf8(args[2])));
        } else {
            System.out.println("Unknown operation " + args[0]);
        }

        client.close();
        server.close();
    }
}
