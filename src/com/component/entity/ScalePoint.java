package com.component.entity;

import android.view.MotionEvent;

/**
 * 缩放的点（左上，右上，右下，左下四个点）
 * 
 * @author leaf
 * 
 */
public class ScalePoint {
	private float x;// 中心x
	private float y;// 中心y
	private float radius;// 半径

	public ScalePoint(float x, float y, float radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
	}

	/**
	 * 移动distanceX,distanceY的距离
	 * 
	 * @param distanceX
	 * @param distanceY
	 */
	public void moveBy(float distanceX, float distanceY) {
		this.x += distanceX;
		this.y += distanceY;
	}
	
	/**
	 * 判断是否选中缩放点
	 * @param event
	 * @return
	 */
	public boolean isInTarget(MotionEvent event) {
		if ((event.getX() - this.x) * (event.getX() - this.x)
				+ (event.getY() - this.y) * (event.getY() - this.y) < (this.radius + 5)
				* (this.radius + 5)) {
			return true;
		}
		return false;
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

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
}
