package org.myorg.model.key;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class Tweet implements Writable {
	private String text;
	private String user;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public void readFields(DataInput in) throws IOException {

		text = in.readUTF();
		user = in.readUTF();

	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(text);
		out.writeUTF(user);

	}
	@Override
	public String toString() {		
		return text + "," + user;
	}

}
