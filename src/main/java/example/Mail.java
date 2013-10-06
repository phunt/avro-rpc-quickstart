package com.fst.task;

import org.apache.avro.util.Utf8;

public interface Mail{

	public static final org.apache.avro.Protocol PROTOCOL = org.apache.avro.Protocol.parse("{\"namespace\": \"example\",\"protocol\": \"Mail\",\"types\": [{\"name\": \"Message\", \"type\": \"record\", \"fields\": [{\"name\": \"to\",   \"type\": \"string\"},{\"name\": \"from\", \"type\": \"string\"},{\"name\": \"body\", \"type\": \"string\"} ]}],\"messages\": { \"send\": {\"request\": [{\"name\": \"message\", \"type\": \"Message\"}],\"response\": \"string\" } }}");
	
	public Utf8 send(Message message);
}

