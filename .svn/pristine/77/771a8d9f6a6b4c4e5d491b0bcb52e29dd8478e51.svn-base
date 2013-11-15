package com.ginwave.smshelper;

import java.util.Vector;
import android.graphics.Bitmap;

public class GIFFrameManager {
	private Vector<Bitmap> frames;
	private int index;

	public GIFFrameManager() {
		frames = new Vector<Bitmap>(1);
		index = 0;
	}

	public void addImage(Bitmap image) {
		frames.addElement(image);
	}

	public int size() {
		return frames.size();
	}

	public Bitmap getImage() {
		if (size() == 0) {
			return null;
		} else {
			return (Bitmap) frames.elementAt(index);
		}
	}

	public void nextFrame() {
		if (index + 1 < size()) {
			index++;
		} else {
			index = 0;
		}
	}

	public static GIFFrameManager CreateGifImage(byte bytes[]) {
		try {
			GIFFrameManager GF = new GIFFrameManager();
			Bitmap image = null;
			GIFEncoder gifdecoder = new GIFEncoder(bytes);
			for (; gifdecoder.moreFrames(); gifdecoder.nextFrame()) {
				try {
					image = gifdecoder.decodeImage();
					if (GF != null && image != null) {
						GF.addImage(image);
					}
					continue;
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			gifdecoder.clear();
			gifdecoder = null;
			return GF;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
