package net.alejandre.windowsphonelib.tiles;

import net.alejandre.windowsphonelib.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

//	// info about tiles:
//	// http://javiersuarezruiz.wordpress.com/2012/04/30/windows-phone-tiles-secundarios/
//	// http://msdn.microsoft.com/en-us/library/windows/apps/hh202948%28v=vs.105%29.aspx
//
//	// info about attrs xml:
//	// http://stackoverflow.com/questions/3441396/defining-custom-attrs
//	// https://github.com/android/platform_frameworks_base/blob/master/core/res/res/values/attrs.xml
//
//	// info about custom views:
//	// http://lslutnfra.blogspot.com.es/2013/09/view-personalizada-en-android.html
//	// http://www.sgoliver.net/blog/?p=1457
//	// http://www.sgoliver.net/blog/?p=1467

//	// all code extracted to do the rotation:
//  // http://www.inter-fuser.com/2009/08/android-animations-3d-flip.html

public class SmallFlipTile extends RelativeLayout {

	/* *********************
	 * *********************
	 * *********************
	 * VARIABLES
	 * *********************
	 * *********************
	 * *********************
	 */

	private final static String TAG="SmallFlipTile"; // tag for debuging.
	private int frontBg=-1; // resource id background to the front layout.
	private Bitmap frontBitmap=null; // bitmap to set the image.
	private ScaleType scaleType = ScaleType.CENTER; // scaleType for the front background.
	private int CircleBg=-1; // resource id background to the circle counter.
	private String counter=""; // String that contains the number to show in the front counter.
	private boolean showCounter=true; // this set if the counter and the circle must be visible.
	
	private View ftLayout; // this is the inflated xml layout.
	private RelativeLayout FrontRoot; // the front root layout view.

	/* *********************
	 * *********************
	 * *********************
	 * END VARIABLES
	 * *********************
	 * *********************
	 * *********************
	 */

	/* *********************
	 * *********************
	 * *********************
	 * CONSTRUCTORS
	 * *********************
	 * *********************
	 * *********************
	 */

	public SmallFlipTile(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Log.i(TAG,"constructor Context context, AttributeSet attrs, int defStyle");
		// processing layout xml properties:
		processingProperties(attrs);
		inflateViews(context);
		//this.addView(ftLayout);
		//this.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
	
	}

	public SmallFlipTile(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.i(TAG,"constructor Context context, AttributeSet attrs");

		// processing layout xml properties:
		processingProperties(attrs);

		inflateViews(context);

		//this.addView(ftLayout);


		//this.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);

	}

	public SmallFlipTile(Context context) {
		super(context);
		Log.i(TAG,"constructor Context context");
		// Default
		setCircleBackgroundResId(-1);
		// Default
		showCounter(true);
		inflateViews(context);
		//this.addView(ftLayout);
		
		//this.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
	}

	/* *********************
	 * *********************
	 * *********************
	 * END CONSTRUCTORS
	 * *********************
	 * *********************
	 * *********************
	 */
	
	/* *********************
	 * *********************
	 * *********************
	 * FILL PROPERTIES
	 * *********************
	 * *********************
	 * *********************
	 */
	
	private void processingProperties(AttributeSet attrs) {
		// Procesamos los atributos XML personalizados
		TypedArray a =
				getContext().obtainStyledAttributes(attrs,
						R.styleable.Tile);

		int fcr = a.getResourceId(R.styleable.Tile_BackgroundImage, -1);
		if (fcr != -1) {
			setFrontBackgroundResourceId(fcr);
		}

		setScaleType(a.getInt(R.styleable.Tile_ImageScaleType, -1));
		// Default
		setCircleBackgroundResId(-1);
		// Default
		showCounter(true);

		a.recycle();
	}

	/* *********************
	 * *********************
	 * *********************
	 * END FILL PROPERTIES
	 * *********************
	 * *********************
	 * *********************
	 */

	/* *********************
	 * *********************
	 * *********************
	 * GETTERS AND SETTERS 
	 * VALUES OF CONTENT 
	 * *********************
	 * *********************
	 * *********************
	 */

	public int getFrontBg() {
		return frontBg;
	}

	public void setFrontBackgroundResourceId(int frontBg) {
		this.frontBg = frontBg;
	}

	public int getCircleBg() {
		return CircleBg;
	}

	public void setCircleBackgroundResId(int circleBg) {
		CircleBg = circleBg;
	}

	public String getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = String.valueOf(counter);
	}

	public boolean isShowingCounter() {
		return showCounter;
	}

	public void showCounter(boolean showCounter) {
		this.showCounter = showCounter;
	}
	
	public void setFrontImageBitmap(Bitmap frontBitmap) {
		this.frontBitmap = frontBitmap;
	}

	/* *********************
	 * *********************
	 * *********************
	 * ********************* 
	 * END GETTERS AND SETTERS 
	 * VALUES OF CONTENT 
	 * *********************
	 * *********************
	 * *********************
	 */

	/* *********************
	 * *********************
	 * *********************
	 * *********************
	 * LAYOUT METHODS
	 * *********************
	 * *********************
	 * *********************
	 */
	
	/**
	 * Method inflateViews:
	 * This method inflate the layouts of the flip tile view.
	 * @param context -> activity context.
	 */
	private void inflateViews(Context context) {
		// instantiate the LayoutInfalter:
		LayoutInflater inflater = LayoutInflater.from(context);
		// inflating the xml layouts:
		ftLayout = inflater.inflate(R.layout.small_flip_tile_template, this);
		
		//this.addView(ftLayout);
	}

	public void show() {
		FrontRoot = (RelativeLayout) ftLayout.findViewById(R.id.FTFroot);
		ImageView frontImage = (ImageView) ftLayout.findViewById(R.id.FTFimage);
		RelativeLayout rlCircle = (RelativeLayout) ftLayout.findViewById(R.id.FTFcount);
		TextView tvCounter = (TextView) ftLayout.findViewById(R.id.FTFcounter);

		if(frontBg != -1)
			frontImage.setImageResource(frontBg);
		else if(frontBitmap != null)
			frontImage.setImageBitmap(frontBitmap);
		if(scaleType != null)
			frontImage.setScaleType(scaleType);
		if(showCounter && counter != null) {
			if(CircleBg != -1) 
				rlCircle.setBackgroundResource(CircleBg);
			tvCounter.setText(counter);
		} else {
			rlCircle.setVisibility(View.GONE);
		}

	}

	public View getfLayout() {
		return FrontRoot;
	}

	public void setScaleType(ScaleType scaleType) {
		this.scaleType = scaleType;
	}

	private void setScaleType(int scaleType) {
		switch (scaleType) {
		case 1:
			this.scaleType = ScaleType.CENTER;
			break;
		case 2:
			this.scaleType = ScaleType.CENTER_CROP;
			break;
		case 3:
			this.scaleType = ScaleType.CENTER_INSIDE;
			break;
		case 4:
			this.scaleType = ScaleType.FIT_CENTER;
			break;
		case 5:
			this.scaleType = ScaleType.FIT_END;
			break;
		case 6:
			this.scaleType = ScaleType.FIT_START;
			break;
		case 7:
			this.scaleType = ScaleType.FIT_XY;
			break;
		case 8:
			this.scaleType = ScaleType.MATRIX;
			break;
		default:
			this.scaleType = null;
			break;
		}
	}

	/* *********************
	 * *********************
	 * *********************
	 * *********************
	 * END LAYOUT METHODS
	 * *********************
	 * *********************
	 * *********************
	 */
}
