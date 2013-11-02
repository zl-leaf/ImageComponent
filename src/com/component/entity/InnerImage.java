package com.component.entity;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.MotionEvent;

/**
 * 显示的图片
 * 
 * @author leaf
 * 
 */
public class InnerImage {
	private float x;// 左上角x
	private float y;// 左上角y
	private Bitmap source;
	private Bitmap bitmap;

	public InnerImage() {
		this.x = 0;
		this.y = 0;
	}

	public boolean isInTarget(MotionEvent event) {
		float left = this.x - 5;
		float top = this.y - 5;
		float right = this.x + this.getWidth() + 10;
		float bottom = this.y + this.getHeight() + 10;
		if ((event.getX() - left) * (event.getX() - right) <= 0
				&& (event.getY() - top) * (event.getY() - bottom) <= 0) {
			return true;
		}
		return false;
	}

	public void scaleTo(float width, float height) {
		float scaleX = width
				/ (float) (this.source == null ? 0 : this.source.getWidth());
		float scaleY = height
				/ (float) (this.source == null ? 0 : this.source.getHeight());
		if (this.source != null && scaleX != 0 && scaleY != 0
				&& (scaleX != 1 || scaleY != 1)) {
			Matrix matrix = new Matrix();
			matrix.postScale(scaleX, scaleY);
			this.bitmap = Bitmap.createBitmap(this.source, 0, 0,
					this.source.getWidth(), this.source.getHeight(), matrix,
					true);
		}
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

	public int getWidth() {
		if (this.bitmap == null) {
			return 0;
		} else {
			return this.bitmap.getWidth();
		}
	}

	public int getHeight() {
		if (this.bitmap == null) {
			return 0;
		} else {
			return this.bitmap.getHeight();
		}
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.source = this.bitmap = bitmap;
	}

}
