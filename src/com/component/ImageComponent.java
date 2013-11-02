package com.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.component.entity.InnerImage;
import com.component.entity.ScalePoint;
import com.component.entity.TouchPoint;

/**
 * 缩放组件的主要组件
 * 
 * @author leaf
 * 
 */
public class ImageComponent extends View {
	private static final float SPACE = 1;// 边线和图片之间的距离
	private static final int RADIUS = 4;// 圆的半径

	private static final int DEFAULT_WIDTH = 62;
	private static final int DEFAULT_HEIGHT = 62;

	private TouchPoint mLastTouchEvent;

	private InnerImage mImage;

	private ScalePoint leftUpperPoint;
	private ScalePoint rightUpperPoint;
	private ScalePoint leftLowerPoint;
	private ScalePoint rightLowerPoint;
	private ScalePoint selectScalePoint;
	private ScalePoint selectXScalePoint;
	private ScalePoint selectYScalePoint;

	public ImageComponent(Context context) {
		super(context, null);
		init();
	}

	public ImageComponent(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ImageComponent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/**
	 * 初始化变量
	 */
	private void init() {
		this.leftUpperPoint = new ScalePoint(RADIUS + SPACE, RADIUS + SPACE,
				RADIUS);
		this.rightUpperPoint = new ScalePoint(3 * (RADIUS + SPACE)
				+ DEFAULT_WIDTH, RADIUS + SPACE, RADIUS);
		this.leftLowerPoint = new ScalePoint(RADIUS + SPACE, 3
				* (RADIUS + SPACE) + DEFAULT_HEIGHT, RADIUS);
		this.rightLowerPoint = new ScalePoint(3 * (RADIUS + SPACE)
				+ DEFAULT_WIDTH, 3 * (RADIUS + SPACE) + DEFAULT_HEIGHT, RADIUS);

		this.mImage = new InnerImage();
		this.mImage.setX(this.leftUpperPoint.getX()
				+ this.leftUpperPoint.getRadius() + SPACE);
		this.mImage.setY(this.leftUpperPoint.getY()
				+ this.leftUpperPoint.getRadius() + SPACE);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Bitmap bitmap = this.mImage.getBitmap();
		if (bitmap != null) {

			canvas.drawBitmap(bitmap, this.mImage.getX(), this.mImage.getY(),
					getpaint());

			Paint paint = getpaint();
			paint.setColor(Color.BLUE);
			// 画点
			canvas.drawCircle(this.leftUpperPoint.getX(),
					this.leftUpperPoint.getY(),
					this.leftUpperPoint.getRadius(), paint);
			canvas.drawCircle(this.rightUpperPoint.getX(),
					this.rightUpperPoint.getY(),
					this.rightUpperPoint.getRadius(), paint);
			canvas.drawCircle(this.rightLowerPoint.getX(),
					this.rightLowerPoint.getY(),
					this.rightLowerPoint.getRadius(), paint);
			canvas.drawCircle(this.leftLowerPoint.getX(),
					this.leftLowerPoint.getY(),
					this.leftLowerPoint.getRadius(), paint);
			
			// 画边线
			canvas.drawLine(this.leftUpperPoint.getX(),
					this.leftUpperPoint.getY(), this.rightUpperPoint.getX(),
					this.rightUpperPoint.getY(), paint);
			canvas.drawLine(this.leftUpperPoint.getX(),
					this.leftUpperPoint.getY(), this.leftLowerPoint.getX(),
					this.leftLowerPoint.getY(), paint);
			canvas.drawLine(this.rightUpperPoint.getX(),
					this.rightUpperPoint.getY(), this.rightLowerPoint.getX(),
					this.rightLowerPoint.getY(), paint);
			canvas.drawLine(this.leftLowerPoint.getX(),
					this.leftLowerPoint.getY(), this.rightLowerPoint.getX(),
					this.rightLowerPoint.getY(), paint);
		}
	}

	/**
	 * 更新数据并且重画
	 */
	private void update() {
		// 计算缩放后图片的宽度和高度
		float radius = this.leftUpperPoint.getRadius();
		float x = Math.min(this.leftUpperPoint.getX() + radius + SPACE,
				this.rightUpperPoint.getX() + radius + SPACE);
		float y = Math.min(this.leftUpperPoint.getY() + radius + SPACE,
				this.leftLowerPoint.getY() + radius + SPACE);
		this.mImage.setX(x);
		this.mImage.setY(y);

		float w = this.rightUpperPoint.getX() - this.leftUpperPoint.getX();
		float h = this.leftLowerPoint.getY() - this.leftUpperPoint.getY();

		if (w > 0) {
			w -= 2 *( radius +  SPACE);
		}
		else {
			w += 2 *( radius +  SPACE);
		}
		
		if(h > 0) {
			h -= 2 *( radius +  SPACE);
		}
		else {
			h += 2 *( radius +  SPACE);
		}

		this.mImage.scaleTo(w, h);

		this.invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			boolean flag = false;
			if (this.mImage.isInTarget(event)) {
				// 检测是否移动
				flag = true;
			}

			// 检测是否缩放，确定选择缩放的点和其同x和同y的两个点
			if (this.leftUpperPoint.isInTarget(event)) {
				this.selectScalePoint = this.leftUpperPoint;
				this.selectXScalePoint = this.rightUpperPoint;
				this.selectYScalePoint = this.leftLowerPoint;
				flag = true;
			} else if (this.rightUpperPoint.isInTarget(event)) {
				this.selectScalePoint = this.rightUpperPoint;
				this.selectXScalePoint = this.leftUpperPoint;
				this.selectYScalePoint = this.rightLowerPoint;
				flag = true;
			} else if (this.rightLowerPoint.isInTarget(event)) {
				this.selectScalePoint = this.rightLowerPoint;
				this.selectXScalePoint = this.leftLowerPoint;
				this.selectYScalePoint = this.rightUpperPoint;
				flag = true;
			} else if (this.leftLowerPoint.isInTarget(event)) {
				this.selectScalePoint = this.leftLowerPoint;
				this.selectXScalePoint = this.rightLowerPoint;
				this.selectYScalePoint = this.leftUpperPoint;
				flag = true;
			}

			if (flag) {
				this.mLastTouchEvent = new TouchPoint(event.getX(),
						event.getY());
			} else {
				return false;
			}

			break;
		case MotionEvent.ACTION_MOVE:
			if (this.mLastTouchEvent != null) {
				if (this.selectScalePoint != null) {
					// 缩放
					this.selectScalePoint.moveBy(event.getX()
							- this.mLastTouchEvent.getX(), event.getY()
							- this.mLastTouchEvent.getY());
					this.selectXScalePoint.moveBy(0, event.getY()
							- this.mLastTouchEvent.getY());
					this.selectYScalePoint.moveBy(event.getX()
							- this.mLastTouchEvent.getX(), 0);
				} else {
					// 移动
					this.moveBy(event.getX() - this.mLastTouchEvent.getX(),
							event.getY() - this.mLastTouchEvent.getY());
				}

				this.mLastTouchEvent = new TouchPoint(event.getX(),
						event.getY());
			}
			this.update();

			break;
		case MotionEvent.ACTION_UP:
			this.mLastTouchEvent = null;
			this.selectScalePoint = null;
			break;

		default:
			break;
		}
		return true;
	}

	/**
	 * 组件移动从原来的位置偏移
	 * 
	 * @param distanceX
	 * @param distanceY
	 */
	private void moveBy(float distanceX, float distanceY) {
		this.leftUpperPoint.moveBy(distanceX, distanceY);
		this.rightUpperPoint.moveBy(distanceX, distanceY);
		this.rightLowerPoint.moveBy(distanceX, distanceY);
		this.leftLowerPoint.moveBy(distanceX, distanceY);
		this.mImage.moveBy(distanceX, distanceY);

		invalidate();
	}

	/**
	 * 生成画笔
	 * 
	 * @return
	 */
	private Paint getpaint() {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		return paint;
	}

	public Bitmap getBitmap() {
		return this.mImage.getBitmap();
	}

	public void setBitmap(Bitmap mBitmap) {
		this.mImage.setBitmap(mBitmap);
		this.update();
	}

}
