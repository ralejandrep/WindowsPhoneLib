package net.alejandre.windowsphonelib.timers;

import java.util.Timer;
import java.util.TimerTask;

import net.alejandre.windowsphonelib.rotator.Flip3dAnimationVertical;
import net.alejandre.windowsphonelib.tiles.FlipTile;
import net.alejandre.windowsphonelib.viewchanger.ChangerViews;
import android.app.Activity;
import android.view.animation.AccelerateInterpolator;

public class RotationTimer extends Timer {

	private Activity act;
	private boolean isFront;
	private boolean isFirstTime;
	private FlipTile oFlipTile;

	private TimerTask timerTask = new TimerTask() {
		@Override
		public void run() {

			act.runOnUiThread(new Runnable() {
				public void run()
				{
					if(!isFirstTime) {

						if (isFront) {       
							applyRotation(0, 90);
							isFront = !isFront;
						} else {    
							applyRotation(0, -90);
							isFront = !isFront;
						}

					} else {
						isFirstTime = false;
					}

				}
			});

		}
	};


	public RotationTimer(String name) {
		super(name);
		isFront = true;
		isFirstTime = true;
	}

	public void setActivity(Activity act) {
		this.act = act;
	}

	public void setFlipTile(FlipTile ft) {
		oFlipTile = ft;
	}

	public void start(int delay) {
		this.schedule(timerTask, 0 , (long) delay);
	}

	private void applyRotation(float start, float end) {
		// Find the center of image
		final float centerX = oFlipTile.getContainer().getWidth() / 2.0f;
		final float centerY = oFlipTile.getContainer().getHeight() / 2.0f;

		// Create a new 3D rotation with the supplied parameter
		// The animation listener is used to trigger the next animation
		final Flip3dAnimationVertical rotation =
				new Flip3dAnimationVertical(start, end, centerX, centerY);
		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new ChangerViews(isFront, oFlipTile.getfLayout(), oFlipTile.getbLayout()));

		if (isFront)
		{
			oFlipTile.getfLayout().startAnimation(rotation);
		} else {
			oFlipTile.getbLayout().startAnimation(rotation);
		}

	}
}
