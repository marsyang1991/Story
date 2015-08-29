package com.yang.MyView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

public class MyEditText extends EditText {
	private Paint mPaint;

	public MyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	protected void onDraw(Canvas canvas) {

		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(2);
		if (this.isFocused() == true)
			mPaint.setColor(Color.parseColor("#FD8914"));
		else
			mPaint.setColor(Color.parseColor("#B5B5B5"));

		// canvas.drawRoundRect(new RectF(2+this.getScrollX(),
		// 2+this.getScrollY(), this.getWidth()-3+this.getScrollX(),
		// this.getHeight()+ this.getScrollY()-1), 3,3, mPaint);
		canvas.drawLine(0, this.getHeight() - 1, this.getWidth() - 1,
				this.getHeight() - 1, mPaint);
		canvas.drawLine(0, this.getHeight() - 1, 0,
				this.getHeight() - this.getHeight() / 3, mPaint);
		canvas.drawLine(this.getWidth() - 1, this.getHeight() - 1,
				this.getWidth() - 1, this.getHeight() - this.getHeight() / 3,
				mPaint);
		super.onDraw(canvas);

	}
}
