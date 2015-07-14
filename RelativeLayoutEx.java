import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import travel.ozon.mobile.view.utils.OnInterceptTouchEventListener;

public class RelativeLayoutEx extends RelativeLayout {
	private static final String TAG = "RelativeLayoutEx";
	private OnSizeChangedListener mOnSizeChangedListener;
	private OnInterceptTouchEventListener mOnInterceptTouchListener;
	private OnDispatchTouchEventListener mOnDispatchTouchEventListener;
	private OnMeasureListener mOnMeasureListener;
	private OnLayoutListener mOnLayoutListener;
	private OnFirstDrawListener mOnFirstDrawListener;
	private boolean mMotionEventsEnabled = true;

	public RelativeLayoutEx(Context context) {
		super(context);
	}

	public RelativeLayoutEx(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RelativeLayoutEx(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setOnSizeChangedListener(OnSizeChangedListener listener) {
		mOnSizeChangedListener = listener;
	}


	public void setOnInterceptTouchEventListener(OnInterceptTouchEventListener listener) {
		mOnInterceptTouchListener = listener;
	}

	public void setOnDispatchTouchEventListener(OnDispatchTouchEventListener listener) {
		mOnDispatchTouchEventListener = listener;
	}

	public void setOnMeasureListener(OnMeasureListener onMeasureListener) {
		mOnMeasureListener = onMeasureListener;
	}

	public void setOnLayoutListener(OnLayoutListener onLayoutListener) {
		mOnLayoutListener = onLayoutListener;
	}

	public void setOnFirstDrawListener(OnFirstDrawListener onFirstDrawListener) {
		mOnFirstDrawListener = onFirstDrawListener;
		setWillNotDraw(false);
	}

	public void setMotionEventsEnabled(boolean enabled) {
		mMotionEventsEnabled = enabled;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final boolean result = super.dispatchTouchEvent(event);

		if (mOnDispatchTouchEventListener != null) {
			mOnDispatchTouchEventListener.afterDispatchTouchEvent(event);
		}
		return result;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			return mMotionEventsEnabled ? (mOnInterceptTouchListener == null || mOnInterceptTouchListener.onInterceptTouchEvent(ev)) &&
					super.onInterceptTouchEvent(ev) : true;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			return true;
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (mOnMeasureListener != null) {
			mOnMeasureListener.onBeforeMeasure(widthMeasureSpec, heightMeasureSpec);
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if (mOnMeasureListener != null) {
			mOnMeasureListener.onAfterMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (mOnLayoutListener != null && changed) {
			mOnLayoutListener.onBeforeLayout(l, t, r, b);
		}

		super.onLayout(changed, l, t, r, b);

		if (mOnLayoutListener != null && changed) {
			mOnLayoutListener.onAfterLayout(l, t, r, b);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (mOnSizeChangedListener != null) {
			mOnSizeChangedListener.onSizeChanged(w, h, oldw, oldh);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (mOnFirstDrawListener != null) {
			setWillNotDraw(true);
			mOnFirstDrawListener.onFirstDraw();
			mOnFirstDrawListener = null;
		}
	}

	public interface OnSizeChangedListener {
		void onSizeChanged(int w, int h, int oldw, int oldh);
	}

	public interface OnMeasureListener {
		void onBeforeMeasure(int widthMeasureSpec, int heightMeasureSpec);
		void onAfterMeasure(int widthMeasureSpec, int heightMeasureSpec);
	}

	public interface OnLayoutListener {
		void onBeforeLayout(int l, int t, int r, int b);
		void onAfterLayout(int l, int t, int r, int b);
	}

	public interface OnDispatchTouchEventListener {
		void afterDispatchTouchEvent(MotionEvent event);
	}

	public interface OnFirstDrawListener {
		void onFirstDraw();
	}
}