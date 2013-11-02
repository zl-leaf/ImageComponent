package com.component.entity;

/**
 * 点击的点
 * @author leaf
 *
 */
public class TouchPoint {
	private float x = 0;
	private float y = 0;

	public TouchPoint(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
}
