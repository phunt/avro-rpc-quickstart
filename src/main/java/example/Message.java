package com.fst.task;

import org.apache.avro.Schema;
import org.apache.avro.util.Utf8;

public class Message extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord  {
	
	@SuppressWarnings("deprecation")
	public static final org.apache.avro.Schema SCHEMA$ = org.apache.avro.Schema.parse("{\"type\":\"record\",\"name\":\"Message\",\"namespace\":\"example\",\"fields\":[{\"name\":\"body\",\"type\":\"string\"},{\"name\":\"from\",\"type\":[\"string\"]},{\"name\":\"to\",\"type\":[\"string\"]}]}");
	private Utf8 body;
	private Utf8 from;
	private Utf8 to;
	
	
	public Utf8 getBody() {
		return body;
	}
	public void setBody(Utf8 body) {
		this.body = body;
	}
	public Utf8 getFrom() {
		return from;
	}
	public void setFrom(Utf8 from) {
		this.from = from;
	}
	public Utf8 getTo() {
		return to;
	}
	public void setTo(Utf8 to) {
		this.to = to;
	}
	@Override
	public Schema getSchema() {
		return SCHEMA$; 
	}
	@Override
	public Object get(int field) {
		switch (field) {
		    case 0: return body;
		    case 1: return from;
		    case 2: return to;
		    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
		}
	}
	@Override
	public void put(int field, Object value) {
		switch (field) {
	    case 0: body = new Utf8(value.toString()); break;
	    case 1: from = new Utf8(value.toString()); break;
	    case 2: to = new Utf8(value.toString()); break;
	    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
		}
	}
	
	
	

}
