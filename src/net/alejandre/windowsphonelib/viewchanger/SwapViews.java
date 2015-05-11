package net.alejandre.windowsphonelib.viewchanger;

import net.alejandre.windowsphonelib.rotator.Flip3dAnimationVertical;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public final class SwapViews implements Runnable {
	private boolean mIsFirstView;
	View front;
	View back;

	public SwapViews(boolean isFirstView, View front, View back) {
		mIsFirstView = isFirstView;
		this.front = front;
		this.back = back;
	}

	public void run() {
		final float centerX = front.getWidth() / 2.0f;
		final float centerY = front.getHeight() / 2.0f;
		Flip3dAnimationVertical rotation;

		if (mIsFirstView) {
			front.setVisibility(View.GONE);
			back.setVisibility(View.VISIBLE);
			back.requestFocus();

			rotation = new Flip3dAnimationVertical(-90, 0, centerX, centerY);
		} else {
			back.setVisibility(View.GONE);
			front.setVisibility(View.VISIBLE);
			front.requestFocus();

			rotation = new Flip3dAnimationVertical(90, 0, centerX, centerY);
		}

		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new DecelerateInterpolator());

		if (mIsFirstView) {
			back.startAnimation(rotation);
		} else {
			front.startAnimation(rotation);
		}
	}
}
