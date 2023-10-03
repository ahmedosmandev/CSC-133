package com.mycompany.a1;

import com.codename1.charts.models.Point;
import com.codename1.charts.util.ColorUtil;

public class Flag extends Fixed {

	private int sequenceNumber;

	public Flag(int size, Point location, int sequenceNumber) {
		super(size, location, ColorUtil.rgb(0, 0, 255));
		this.sequenceNumber = sequenceNumber;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	@Override
	public String toString() {
		String parentDesc = super.toString();
		String myDesc = " sequence Number=" + sequenceNumber;
		return "Flag:" + parentDesc + myDesc;
	}

}