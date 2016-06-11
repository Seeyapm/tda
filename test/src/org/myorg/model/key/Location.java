package org.myorg.model.key;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class Location implements WritableComparable<Location> {
	private String coordinate;

	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		coordinate = in.readUTF();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(coordinate);

	}

	@Override
	public String toString() {

		return coordinate + ",";
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((coordinate == null) ? 0 : coordinate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (coordinate == null) {
			if (other.coordinate != null)
				return false;
		} else if (!coordinate.equals(other.coordinate))
			return false;
		return true;
	}

	@Override
	public int compareTo(Location other) {
		return coordinate.compareTo(other.getCoordinate());

	}

}
