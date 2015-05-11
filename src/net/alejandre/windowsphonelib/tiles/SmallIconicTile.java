package net.alejandre.windowsphonelib.tiles;

import net.alejandre.windowsphonelib.R;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

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

public class SmallIconicTile extends FrameLayout {

	/* *********************
	 * *********************
	 * *********************
	 * VARIABLES
	 * *********************
	 * *********************
	 * *********************
	 */

	private final static String TAG="SmallIconicTile"; // tag for debuging.
	private int iconImage=-1; // resource id background to the layout.
	private Bitmap iconImageBitmap=null; // bitmap to set the icon.
	private int bgColor=-1; // color for the background.
	private int txtColor=-16777216; // color for the texts and counter.

	private View ftLayout; // this is the inflated xml layout.
	private RelativeLayout FrontRoot; // the front root layout view.

	private Context context;

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

	public SmallIconicTile(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Log.i(TAG,"constructor Context context, AttributeSet attrs, int defStyle");
		// processing layout xml properties:
		processingProperties(attrs);
		inflateViews(context);

		this.context = context;
		//this.addView(ftLayout);
		//this.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);

	}

	public SmallIconicTile(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.i(TAG,"constructor Context context, AttributeSet attrs");

		// processing layout xml properties:
		processingProperties(attrs);

		inflateViews(context);

		this.context = context;
		//this.addView(ftLayout);

		//this.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);

	}

	public SmallIconicTile(Context context) {
		super(context);
		Log.i(TAG,"constructor Context context");
		inflateViews(context);
		this.context = context;
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

		// background color.
		setBackgroundColor(a.getColor(R.styleable.Tile_BackgroundColor, -1));
		// icon image.
		setBackgroundImage(a.getResourceId(R.styleable.Tile_IconImage, -1));

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

	public void setBackgroundImage(int frontBg) {
		this.iconImage = frontBg;
	}

	public void setImageBitmap(Bitmap iconImageBitmap) {
		this.iconImageBitmap = iconImageBitmap;
	}

	public void setBackgroundColor(int backColor) {
		this.bgColor = backColor;
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
		ftLayout = inflater.inflate(R.layout.small_iconic_tile_template, this);

		//this.addView(ftLayout);
	}
	
	public void show() {
		FrontRoot = (RelativeLayout) ftLayout.findViewById(R.id.SITRoot);
		ImageView icon = (ImageView) ftLayout.findViewById(R.id.SITIcon);
	
		// setting the data...
		// bg color:
		FrontRoot.setBackgroundColor(bgColor);
		// icon:
		if(iconImage != -1) {
			icon.setImageResource(iconImage);
		} else if(iconImageBitmap != null) {
			icon.setImageBitmap(iconImageBitmap);
		}

	}




	public View getfLayout() {
		return FrontRoot;
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
