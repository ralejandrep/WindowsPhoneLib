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

public class MediumIconicTile extends FrameLayout {

	/* *********************
	 * *********************
	 * *********************
	 * VARIABLES
	 * *********************
	 * *********************
	 * *********************
	 */

	private final static String TAG="BigIconicTile"; // tag for debuging.
	private String title=""; // title to the Iconic Tile.
	private String counter=""; // String that contains the number to show in the counter.
	private int count=-1;
	private int iconImage=-1; // resource id background to the layout.
	private Bitmap iconImageBitmap=null; // bitmap to set the icon.
	private int bgColor=-1; // color for the background.
	private int txtColor=-16777216; // color for the texts and counter.

	private View ftLayout; // this is the inflated xml layout.
	private RelativeLayout FrontRoot; // the front root layout view.
	private ViewFlipper CounterRoot; // the root view for the counter (needed for animating the counter changes).
	private TextView tvCounter; // text view that contains the counter.

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

	public MediumIconicTile(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Log.i(TAG,"constructor Context context, AttributeSet attrs, int defStyle");
		// processing layout xml properties:
		processingProperties(attrs);
		inflateViews(context);

		this.context = context;
		//this.addView(ftLayout);
		//this.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);

	}

	public MediumIconicTile(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.i(TAG,"constructor Context context, AttributeSet attrs");

		// processing layout xml properties:
		processingProperties(attrs);

		inflateViews(context);

		this.context = context;
		//this.addView(ftLayout);

		//this.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);

	}

	public MediumIconicTile(Context context) {
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
		// title.
		setTitle(a.getString(R.styleable.Tile_Title));
		// icon image.
		setBackgroundImage(a.getResourceId(R.styleable.Tile_IconImage, -1));
		// all texts color.
		setTextColor(a.getColor(R.styleable.Tile_TextColor, -16777216));

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

	public void setTitle(String title) {
		this.title = title;
	}

	public void setBackgroundImage(int frontBg) {
		this.iconImage = frontBg;
	}

	public void setCounter(int counter) {
		count = counter;
		this.counter = String.valueOf(counter);
	}

	public void setImageBitmap(Bitmap iconImageBitmap) {
		this.iconImageBitmap = iconImageBitmap;
	}

	public void setBackgroundColor(int backColor) {
		this.bgColor = backColor;
	}

	public void setTextColor(int txtColor) {
		Log.i(TAG, txtColor+"");
		this.txtColor = txtColor;
	}

	public void incrementCounter() {
		((Activity) context).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				count += 1;
				counter = String.valueOf(count);
				refreshCounter();
			}
		});
	}

	private void refreshCounter() {
		CounterRoot.showNext();
		new Thread(new Runnable() {	
			@Override
			public void run() {
				try {
					while(CounterRoot.isFlipping())
						Thread.sleep(200);
					tvCounter.post(new Runnable() {
						@Override
						public void run() {
							tvCounter.setText(counter);
						}
					});
					CounterRoot.stopFlipping();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
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
		ftLayout = inflater.inflate(R.layout.medium_iconic_tile_template, this);

		//this.addView(ftLayout);
	}

	public void show() {
		FrontRoot = (RelativeLayout) ftLayout.findViewById(R.id.MITRoot);
		TextView tvTitle = (TextView) ftLayout.findViewById(R.id.MITTitle);
		ImageView icon = (ImageView) ftLayout.findViewById(R.id.MITIcon);
		CounterRoot = (ViewFlipper) ftLayout.findViewById(R.id.MITCounterFlipper);

		CounterRoot.setInAnimation(AnimationUtils.loadAnimation(context,
				R.anim.push_up_in));
		CounterRoot.setOutAnimation(AnimationUtils.loadAnimation(context,
				R.anim.push_up_out));

		tvCounter = (TextView) CounterRoot.getChildAt(0);
		//tvNextCounter = (TextView) CounterRoot.getChildAt(1);
		//tvCounter = (TextView) ftLayout.findViewById(R.id.BITCounter);
		//firstCounter = true;

		// setting the data...
		// bg color:
		FrontRoot.setBackgroundColor(bgColor);
		// icon:
		if(iconImage != -1) {
			icon.setImageResource(iconImage);
		} else if(iconImageBitmap != null) {
			icon.setImageBitmap(iconImageBitmap);
		}
		// title:
		tvTitle.setText( (title != null) ? title : "" );
		// counter:
		if(count == -1) {
			//CounterRoot.setVisibility(View.GONE);
			CounterRoot.setVisibility(View.GONE);
		} else {
			tvCounter.setText(counter);
		}
		Log.i(TAG, txtColor+"");
		if(txtColor != -16777216) {
			tvTitle.setTextColor(txtColor);
			tvCounter.setTextColor(txtColor);
			//tvNextCounter.setTextColor(txtColor);
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
