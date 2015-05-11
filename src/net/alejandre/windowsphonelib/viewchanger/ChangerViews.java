package net.alejandre.windowsphonelib.viewchanger;

import android.view.View;
import android.view.animation.Animation;

public class ChangerViews implements Animation.AnimationListener {
	private boolean mCurrentView;
	View front;
	View back;

	public ChangerViews(boolean currentView, View front, View back) {
		mCurrentView = currentView;
		this.front = front;
		this.back = back;
	}

	public void onAnimationStart(Animation animation) {
	}

	public void onAnimationEnd(Animation animation) {
		front.post(new SwapViews(mCurrentView, front, back));
	}

	public void onAnimationRepeat(Animation animation) {
	}

}
