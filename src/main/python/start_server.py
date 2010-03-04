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

from BaseHTTPServer import BaseHTTPRequestHandler, HTTPServer
import avro.ipc as ipc
import avro.protocol as protocol
import avro.schema as schema

PROTOCOL = protocol.parse(open("../avro/mail.avpr").read())

class MailResponder(ipc.Responder):
    def __init__(self):
        ipc.Responder.__init__(self, PROTOCOL)

    def invoke(self, msg, req):
        if msg.name == 'send':
            message = req['message']
            return ("Sent message to " + message['to']
                    + " from " + message['from']
                    + " with body " + message['body'])
        else:
            raise schema.AvroException("unexpected message:", msg.getname())

class MailHandler(BaseHTTPRequestHandler):
  def do_POST(self):
    self.responder = MailResponder()
    call_request_reader = ipc.FramedReader(self.rfile)
    call_request = call_request_reader.read_framed_message()
    resp_body = self.responder.respond(call_request)
    self.send_response(200)
    self.send_header('Content-Type', 'avro/binary')
    self.end_headers()
    resp_writer = ipc.FramedWriter(self.wfile)
    resp_writer.write_framed_message(resp_body)

server_addr = ('localhost', 9090)

if __name__ == '__main__':
    server = HTTPServer(server_addr, MailHandler)
    server.allow_reuse_address = True
    server.serve_forever()
