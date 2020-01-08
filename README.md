# [Avro RPC Quick Start](http://github.com/phunt/avro-rpc-quickstart)

**Author: [Patrick Hunt](http://people.apache.org/~phunt/)** (follow me on [twitter](http://twitter.com/phunt))

## Summary

[This is a starter kit](http://github.com/phunt/avro-rpc-quickstart), a project template if you will, for [Apache Avro](http://avro.apache.org/) intended to bootstrap your Avro based project. You'll learn how to declare a protocol, generate and compile your code, and run a working "Hello World" type example.

### What's Apache Avro?

From the [official site](http://avro.apache.org/): "Avro is a data serialization system"

Avro provides:

* Rich data structures.
* A compact, fast, binary data format.
* A container file, to store persistent data.
* Remote procedure call (RPC).
* Simple integration with dynamic languages. Code generation is not required to read or write data files nor to use or implement RPC protocols. Code generation as an optional optimization, only worth implementing for statically typed languages.

## License

This project is licensed under the Apache License Version 2.0

## Introduction

The sample application included in this project simulates a remote service, Mail, where Avro RPC is used to send a message using the service. This document details how to build and run the sample using Maven. The Avro jar files (and jars they depend upon) will be downloaded automatically.

In this sample project you will find four sets of files:

1. This documentation
1. Sample Avro protocol declaration
1. Java quick start
    * Maven build file
    * Sample application - i.e. Main program
1. Python quick start
    * Sample application - i.e. client/server scripts
1. Ruby quick start
    * Sample application - i.e. client/server scripts

## mail.avpr - Avro Protocol Declaration

Notice that all examples (java, python, and ruby) share the same Avro protocol declaration. I've not demonstrated it here, but the implementations are interoperable - the java client can talk to the python server and vice-versa (which I'll leave as an exercise for the reader).

`src/main/avro` should contain all of the Avro protocol & schema specifications. `mail.avpr` declares our simple "Mail" service. You will see:

1. the name & namespace of the protocol
1. any specialized types used in the messages, Message in this case
1. we are declaring a "send" message type which takes a Message as an argument and returns a result string

Read more about Avro's [protocol declaration](http://avro.apache.org/docs/current/spec.html#Protocol+Declaration)

## Current supported quickstart implementations

* Java
* Python
* Ruby

Each of these implementions is detailed below:

## Java

### Requirements

* Maven

### Java Maven build file

* pom.xml - this file specifies the dependencies of the project, primarily Avro itself.

You'll see the plugin section, which contains:

This plugin element causes the Avro Maven Plugin's compile goal to run during the "generate-sources" maven phase.

```xml
            <plugin>
                <groupId>org.apache.avro</groupId>
                <artifactId>avro-maven-plugin</artifactId>
                <version>${avro.version}</version>
                <executions>
                    <execution>
                        <id>schemas</id>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>schema</goal>
                            <goal>protocol</goal>
                            <goal>idl-protocol</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
```

### Main.java - the main() routine of the Java Mail sample

`src/main/java/example/Main.java`

1. the MailImpl class implements the Mail protocol defined in `mail.avpr`
1. the startServer() method starts the server which implements the Mail service (Mail/MailImpl)
1. the main function takes three arguments; to, from and body of the message. After the server is started a Mail client is created, attached to the service, and used to send a "Message", the result of the RPC call is printed to the console.

### Compiling the Java sample

All generated files (source, class, etc...) are written to the "target" directory.

`mvn compile`

**Note**: integration with eclipse is very simple. Just type "mvn eclipse:eclipse" (see the [maven-eclipse-plugin](http://maven.apache.org/plugins/maven-eclipse-plugin/) documentation for more details).

### Running the Java sample

`mvn -e exec:java -Dexec.mainClass=example.Main -Dexec.args='avro_user pat Hello_World'`

## Python

### Python Requirements

Avro is [available from pypi](http://pypi.python.org/pypi/avro)

It seems that the Avro python egg requires snappy:

```bash
sudo apt-get install libsnappy-dev # 'brew install snappy' if you're on Mac
sudo pip install python-snappy
sudo pip install avro
```

Note that a separate package is provided for Python3:

```bash
sudo pip3 install avro-python3
```

### Python - start_server.py

Run this first to start the python avro Mail server.

1. the MailResponder class implements the Mail protocol defined in `mail.avpr`
1. main starts the server which implements the Mail service (Mail/MailResponder)

### Python - send_message.py

You'll see that the structure of the python code is similar to the java/ruby source.

`src/main/python/send_message.py`

1. the main function takes three arguments; to, from and body of the message. After the server is started a Mail client is created, attached to the service, and used to send a "Message", the result of the RPC call is printed to the console.

#### Run the python

From the src/main/python directory run:

`./start_server.py`

then in a separate shell run:

`./send_message.py avro_user pat Hello_World`

## Ruby

### Ruby Requirements

Install the [avro ruby gem](http://rubygems.org/gems/avro)

`sudo gem install avro`

### Ruby - sample_ipc_server.rb

Run this first to start the ruby avro Mail server.

1. the MailResponder class implements the Mail protocol defined in mail.avpr
1. main starts the server which implements the Mail service (Mail/MailResponder)

### Ruby - sample_ipc_client.rb

You'll see that the structure of the ruby code is similar to the java/python source.

`src/main/ruby/sample_ipc_client.rb`

1. the main function takes three arguments; to, from and body of the message. After the server is started a Mail client is created, attached to the service, and used to send a "Message", the result of the RPC call is printed to the console.

#### Run the ruby

From the src/main/ruby directory run:

`ruby -r 'rubygems' ./sample_ipc_server.rb`

then in a separate shell run:

`ruby -r 'rubygems' ./sample_ipc_client.rb avro_user pat Hello_World`
