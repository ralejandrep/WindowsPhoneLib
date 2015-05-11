package net.alejandre.windowsphonelib.tiles;

import net.alejandre.windowsphonelib.R;
import net.alejandre.windowsphonelib.interfaces.OnFlipTileClickListener;
import net.alejandre.windowsphonelib.timers.RotationTimer;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

public class FlipTile extends FrameLayout {

	/* *********************
	 * *********************
	 * *********************
	 * VARIABLES
	 * *********************
	 * *********************
	 * *********************
	 */

	private final static String TAG="FlipTile"; // tag for debuging.
	private String title=""; // title to the Flip Tile.
	private String backTitle=""; // title to the back Face.
	private int tTextSize=-1; // title text size value.
	private int tTypeFace=-1; // title typeface to make it bold, italic or bold and italic.
	private int titleColor=-1; // color for the title.
	private String backContent=""; // back content text for the Flip Tile.
	private int bcTextSize=-1; // title text size value.
	private int bcTypeFace=-1; // title typeface to make it bold, italic or bold and italic.
	private int backContColor=-1; // color for the title.
	private int frontBg=-1; // resource id background to the front layout.
	private Bitmap frontBitmap=null; // bitmap to set the image.
	private ScaleType scaleType = ScaleType.CENTER; // scaleType for the front background.
	private int CircleBg=-1; // resource id background to the circle counter.
	private int backBg=-1; // color background to the back layout.
	private int delay=-1; // delay to change the layouts.
	private String counter=""; // String that contains the number to show in the front counter.
	private boolean showCounter=true; // this set if the counter and the circle must be visible.
	private boolean customListener=false; // this set if the Flip Tile will use the OnFlipTileClickListener interface or not.
	
	private RotationTimer timer; // this object change the views front and back with the specified delay.
	private Context ctx; // context of the activity.
	private ViewGroup vgContainer; // FrameLayout that contains our views that do the rotation.
	private View ftLayout; // this is the inflated xml layout.
	private RelativeLayout FrontRoot; // the front root layout view.
	private RelativeLayout BackRoot; // the back root layout view.
	
	private OnFlipTileClickListener clickListeners;

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

	public FlipTile(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Log.i(TAG,"constructor Context context, AttributeSet attrs, int defStyle");
		// processing layout xml properties:
		processingProperties(attrs);
		inflateViews(context);
		//this.addView(ftLayout);
		ctx = context;
		//this.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
	
	}

	public FlipTile(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.i(TAG,"constructor Context context, AttributeSet attrs");

		// processing layout xml properties:
		processingProperties(attrs);

		inflateViews(context);

		//this.addView(ftLayout);

		ctx = context;
		//this.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);

	}

	public FlipTile(Context context) {
		super(context);
		Log.i(TAG,"constructor Context context");
		// Default
		setCircleBackgroundResId(-1);
		// Default
		showCounter(true);
		inflateViews(context);
		//this.addView(ftLayout);
		ctx = context;
		
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

		setTitle(a.getString(R.styleable.Tile_Title));
		// title size:
		setTitleTextSize(a.getInt(R.styleable.Tile_TitleSize, -1));
		// title color:
		setTitleColor(a.getColor(R.styleable.Tile_TitleColor, -1));
		// title typeface:
		setTitleTypeFace(a.getInt(R.styleable.Tile_TitleTextStyle, -1));

		setBackTitle(a.getString(R.styleable.Tile_BackTitle));
		
		setBackContent(a.getString(R.styleable.Tile_BackContent));
		// BackContent size:
		setBackContentTextSize(a.getInt(R.styleable.Tile_BackContentSize, -1));
		// BackContent color:
		setBackContentColor(a.getColor(R.styleable.Tile_BackContentColor, -1));
		// BackContent typeface:
		setBackContentTypeFace(a.getInt(R.styleable.Tile_BackContentTextStyle, -1));

		int fcr = a.getResourceId(R.styleable.Tile_BackgroundImage, -1);
		if (fcr != -1) {
			setFrontBackgroundResourceId(fcr);
		}

		int bcc = a.getColor(R.styleable.Tile_BackBackgroundColor, -1);
		if (bcc != -1) {
			setBackBackgroundColor(bcc);
		}

		setDelay(a.getInt(R.styleable.Tile_Delay, -1));

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
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if(title != null)
			this.title = title;
		else
			this.title = "";
	}

	public String getBackTitle() {
		return backTitle;
	}

	public void setBackTitle(String backTitle) {
		if(backTitle != null)
			this.backTitle = backTitle;
		else if(title != null)
			this.backTitle = title;
		else
			this.backTitle = "";
	}

	public String getBackContent() {
		return backContent;
	}

	public void setBackContent(String backContent) {
		if(backContent != null)
			this.backContent = backContent;
		else
			this.backContent = "";
	}

	public int getFrontBg() {
		return frontBg;
	}

	public void setFrontBackgroundResourceId(int frontBg) {
		this.frontBg = frontBg;
	}

	public int getBackBg() {
		return backBg;
	}

	public void setBackBackgroundColor(int backBg) {
		this.backBg = backBg;
	}

	public int getCircleBg() {
		return CircleBg;
	}

	public void setCircleBackgroundResId(int circleBg) {
		CircleBg = circleBg;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
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
	
	public boolean isCustomListener() {
		return customListener;
	}

	public void setCustomListener(boolean customListener) {
		this.customListener = customListener;
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
		ftLayout = inflater.inflate(R.layout.flip_tile_template, this);

		vgContainer = (ViewGroup) ftLayout.findViewById(R.id.FlipTileRoot);

		//this.addView(ftLayout);
	}

	private void settingFrontView() {
		FrontRoot = (RelativeLayout) ftLayout.findViewById(R.id.FTFroot);
		ImageView frontImage = (ImageView) ftLayout.findViewById(R.id.FTFimage);
		RelativeLayout rlCircle = (RelativeLayout) ftLayout.findViewById(R.id.FTFcount);
		TextView tvCounter = (TextView) ftLayout.findViewById(R.id.FTFcounter);
		TextView tvFrontTitle = (TextView) ftLayout.findViewById(R.id.FTFtitle);

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
		tvFrontTitle.setText(title);
		if(tTextSize != -1)
			tvFrontTitle.setTextSize((float) tTextSize);
		if(titleColor != -1)
			tvFrontTitle.setTextColor(titleColor);
		else 
			tvFrontTitle.setTextColor(Color.BLACK);
		if(tTypeFace != -1)
			tvFrontTitle.setTypeface(null, tTypeFace);

	}

	private void settingBackView() {
		BackRoot = (RelativeLayout) ftLayout.findViewById(R.id.FTBroot);
		TextView tvBackContent = (TextView) ftLayout.findViewById(R.id.FTBcontent);
		TextView tvBackTitle = (TextView) ftLayout.findViewById(R.id.FTBtitle);

		BackRoot.setBackgroundColor(backBg);
		tvBackTitle.setText(backTitle);
		if(tTextSize != -1)
			tvBackTitle.setTextSize((float) tTextSize);
		if(titleColor != -1)
			tvBackTitle.setTextColor(titleColor);
		else 
			tvBackTitle.setTextColor(Color.BLACK);
		if(tTypeFace != -1)
			tvBackTitle.setTypeface(null, tTypeFace);
		tvBackContent.setText(backContent);

		if(bcTextSize != -1)
			tvBackContent.setTextSize((float) bcTextSize);
		if(backContColor != -1)
			tvBackContent.setTextColor(backContColor);
		else 
			tvBackContent.setTextColor(Color.BLACK);
		if(bcTypeFace != -1)
			tvBackContent.setTypeface(null, bcTypeFace);

	}

	public ViewGroup getContainer() {
		return vgContainer;
	}

	public View getfLayout() {
		return FrontRoot;
	}

	public View getbLayout() {
		return BackRoot;
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

	public int getTitleTextSize() {
		return tTextSize;
	}

	public void setTitleTextSize(int tTextSize) {
		this.tTextSize = tTextSize;
	}

	public int getTitleTypeFace() {
		return tTypeFace;
	}

	public void setTitleTypeFace(int tTypeFace) {
		this.tTypeFace = tTypeFace;
	}

	public int getTitleColor() {
		return titleColor;
	}

	public void setTitleColor(int titleColor) {
		this.titleColor = titleColor;
	}

	public int getBackContentTextSize() {
		return bcTextSize;
	}

	public void setBackContentTextSize(int bcTextSize) {
		this.bcTextSize = bcTextSize;
	}

	public int getBackContentTypeFace() {
		return bcTypeFace;
	}

	public void setBackContentTypeFace(int bcTypeFace) {
		this.bcTypeFace = bcTypeFace;
	}

	public int getBackContentColor() {
		return backContColor;
	}

	public void setBackContentColor(int backContColor) {
		this.backContColor = backContColor;
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

	/* *********************
	 * *********************
	 * *********************
	 * *********************
	 * CONTROLS OF ENDING AND START
	 * *********************
	 * *********************
	 * *********************
	 */

	public void start() {
		settingFrontView();
		settingBackView();
		// setting the events to the diferents parts of the FlipTile:
		if(customListener) setUpEvents();
		timer = new RotationTimer(TAG+"Timer");
		timer.setActivity((Activity) ctx);
		timer.setFlipTile(this);
		if(delay != -1)
			timer.start(delay);
		else
			timer.start(5000);
	}

	public void finish() {
		timer.cancel();
	}
	
	/* *********************
	 * *********************
	 * *********************
	 * *********************
	 * END CONTROLS OF ENDING AND START
	 * *********************
	 * *********************
	 * *********************
	 */
	
	/* *********************
	 * *********************
	 * *********************
	 * *********************
	 * CLICK LISTENERS
	 * *********************
	 * *********************
	 * *********************
	 */
	
	private void setUpEvents() {
		FrontRoot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(TAG,"Front Face Clicked!");
				clickListeners.onFrontFaceClick();
			}
		});
		BackRoot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(TAG,"Back Face Clicked!");
				clickListeners.onBackFaceClick();
			}
		});
	}

	public void setOnFlipTileClickListener(OnFlipTileClickListener fcl) {
		clickListeners = fcl;
	}
	
	/* *********************
	 * *********************
	 * *********************
	 * *********************
	 * END OF CLICK LISTENERS
	 * *********************
	 * *********************
	 * *********************
	 */

}
