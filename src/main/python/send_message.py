#!/usr/bin/env python

# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import sys
import socket

import avro.ipc as ipc
import avro.genericipc as genericipc
import avro.protocol as protocol
import avro.schema as schema

PROTOCOL = protocol.parse(open("../avro/mail.avpr").read())

class MailResponder(genericipc.Responder):
    def __init__(self):
        ipc.ResponderBase.__init__(self, PROTOCOL)

    def invoke(self, msg, req):
        if msg.getname() == 'send':
            return ("Sent message to " + message['to']
                    + " from " + message['from']
                    + " with body " + message['body'])
        else:
            raise schema.AvroException("unexpected message:", msg.getname())

def start_server():
    addr = ('localhost', 0)
    global server
    server = ipc.SocketServer(MailResponder(), addr)

class UsageError(Exception):
    def __init__(self, value):
        self.value = value
    def __str__(self):
        return repr(self.value)

if __name__ == '__main__':
    if len(sys.argv) != 4:
        raise UsageError("Usage: <to> <from> <body>")

    # usually this would be another app, but for simplicity
    global server
    start_server()

    # client code - attach to the server and send a message
    sock = socket.socket()
    sock.connect(server.getaddress())
    client = ipc.SocketTransceiver(sock)
    requestor = genericipc.Requestor(PROTOCOL, client)
    
    # fill in the Message record and send it
    message = dict()
    message['to'] = sys.argv[1]
    message['from'] = sys.argv[2]
    message['body'] = sys.argv[3]

    params = dict()
    params['message'] = message
    print("Result: " + requestor.request('send', params))

    # cleanup
    client.close()
    server.close()
