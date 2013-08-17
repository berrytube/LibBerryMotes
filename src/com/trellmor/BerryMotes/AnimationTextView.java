package com.trellmor.BerryMotes;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.TextView;

public class AnimationTextView extends TextView implements Drawable.Callback {
	private Handler mHandler = new Handler();
	private AnimationTextWatcher mTextWatcher = new AnimationTextWatcher();
	private boolean mAnimating = false;

	public AnimationTextView(Context context) {
		super(context);
	}

	public AnimationTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AnimationTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		handleAnimationDrawable(true);
		addTextChangedListener(mTextWatcher);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();

		handleAnimationDrawable(false);
		removeTextChangedListener(mTextWatcher);
	}

	private void handleAnimationDrawable(boolean isPlay) {
		CharSequence text = getText();
		handleAnimationDrawable(isPlay, text);
	}

	private void handleAnimationDrawable(boolean isPlay, CharSequence text) {
		if (text instanceof Spanned) {
			Spanned span = (Spanned) text;
			ImageSpan[] spans = span
					.getSpans(0, span.length(), ImageSpan.class);
			for (ImageSpan i : spans) {
				Drawable d = i.getDrawable();
				if (d instanceof AnimationDrawable) {
					AnimationDrawable ad = (AnimationDrawable) d;
					if (isPlay) {
						ad.setCallback(this);
						ad.start();
					} else {
						ad.setCallback(null);
						ad.stop();
					}
				}
			}
		}
	}

	@Override
	public void invalidateDrawable(Drawable dr) {
		if (dr instanceof AnimationDrawable) {
			((AnimationDrawable)dr).start();
		}
		invalidate();
	}

	@Override
	public void scheduleDrawable(Drawable who, Runnable what, long when) {
		if (who != null && what != null) {
			mHandler.postAtTime(what, when);
		}
	}

	@Override
	public void unscheduleDrawable(Drawable who, Runnable what) {
		if (who != null && what != null) {
			mHandler.removeCallbacks(what);
		}
	}

	class AnimationTextWatcher implements TextWatcher {
		@Override
		public void afterTextChanged(Editable s) {
			handleAnimationDrawable(true, s);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			handleAnimationDrawable(false, s);
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	}
}
