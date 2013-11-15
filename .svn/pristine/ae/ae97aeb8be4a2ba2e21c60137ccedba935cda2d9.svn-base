package com.ginwave.smshelper;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class GameView extends View implements Runnable {

	private int mWidth;
	private int mHeight;
	
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mWidth = this.getWidth();
		mHeight = this.getHeight();
	}

	private GIFFrameManager mGIFFrameManager = null;
	private Paint mPaint = null;

	public void setGifImage(String pPath){
		try {
			mGIFFrameManager = GIFFrameManager.CreateGifImage(this.fileConnect(new FileInputStream(pPath)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(this).start();
	}
	
	/*public GameView(Context context) {
		super(context);
		mGIFFrameManager = GIFFrameManager.CreateGifImage(this.fileConnect(this
				.getResources().openRawResource(R.drawable.cat)));
		new Thread(this).start();
	}*/

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mWidth = this.getWidth();
		mHeight = this.getHeight();
		mPaint = new Paint();
		if(mGIFFrameManager != null){
			mGIFFrameManager.nextFrame();
			Bitmap bitmap = mGIFFrameManager.getImage();
			int bitmapWidth = bitmap.getWidth();
			int bitmapHeight = bitmap.getHeight();
			float scaleFactor = 1.0f;
			float scaleWidth = mWidth / bitmapWidth;
			float scaleHeight = mHeight / bitmapHeight;
			if(scaleWidth >= scaleHeight && scaleWidth > 0 && scaleHeight > 0){
				scaleFactor = scaleHeight;
			}
			else{
				scaleFactor = scaleWidth;
			}
			bitmap = Bitmap.createScaledBitmap(bitmap, (int)(bitmapWidth * scaleFactor), (int)(bitmapHeight * scaleFactor), false);
			/*if(mWidth > 0 && mHeight > 0){
				bitmap = Bitmap.createScaledBitmap(bitmap, mWidth, mHeight, false);
			}*/
			if (bitmap != null) {
				canvas.drawBitmap(bitmap, (mWidth - bitmap.getWidth()) / 2, 0, mPaint);
			}
		}
	}

	public byte[] fileConnect(InputStream in) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int ch = 0;
		try {
			while ((ch = in.read()) != -1) {
				out.write(ch);
			}
			byte[] b = out.toByteArray();
			out.close();
			out = null;
			in.close();
			in = null;
			return b;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void run() {
		while (!Thread.interrupted()) {
			try {
				Thread.sleep(2500);
				this.postInvalidate();
			} catch (Exception ex) {
				ex.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}

}
