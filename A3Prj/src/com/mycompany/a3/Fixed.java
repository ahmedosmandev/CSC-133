package com.mycompany.a2;

import com.codename1.charts.models.Point;

public class Fixed extends GameObject {

	public Fixed(int color) {
		super(color);
	}

	public Fixed(int color, int size) {
		super(color, size);
	}

	public Fixed(int size, Point location, int color) {
super(color);
super.setLocation(location);
}

	@Override
	public void setX(float x) {

	}

	@Override
	public void setY(float y) {

	}
}