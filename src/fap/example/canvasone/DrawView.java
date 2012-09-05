package fap.example.canvasone;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

public class DrawView extends View implements OnTouchListener {
	

	private Bitmap bmpBack;
    private Bitmap bmpCock;
    
    public int winWidth;
    public int winHeight;
    
    private static final String TAG = "DrawView";
    	List<Point> points = new ArrayList<Point>();
    	List<CircularGenericGauge> gauges = new ArrayList<CircularGenericGauge>();
    	Paint paintBack = new Paint();
    	Paint paint = new Paint();
    
    
    public DrawView(MainActivity context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);

        this.setOnTouchListener(this);

        // pippo
        prepareEnvironment(context);

        CircularGenericGauge strum = new CircularGenericGauge();
    
        strum.setPosition(250,250);
        strum.setDimension(200);
        strum.setColor(Color.argb(255, 125, 125, 125));
        
        gauges.add(strum);
        
        /*
        
        
  */      
        return;
	}

	@Override
    public void onDraw(Canvas canvas) {

		// draw the backGround
    	drawBackground(canvas, bmpBack);

//    	canvas.drawBitmap(bmpCock, 20, 50, paint);
  //  	primo.drawGauge(canvas);
        for (CircularGenericGauge strum : gauges) {
            strum.drawGauge(canvas);
        }
    	
    	
    	Log.d(TAG, "onDraw! " );
        for (Point point : points) {
            canvas.drawCircle(point.x, point.y, 5, paint);
            // Log.d(TAG, "Painting: "+point);
        }
    }

    public boolean onTouch(View view, MotionEvent event) {
        // if(event.getAction() != MotionEvent.ACTION_DOWN)
        // return super.onTouchEvent(event);
        Point point = new Point();
        point.x = event.getX();
        point.y = event.getY();
        points.add(point);
        invalidate();
        Log.d(TAG, "point: " + point + " n.of points"+points.size() );
        return true;
    }
    
    private void drawBackground(Canvas canvas,  Bitmap bmp) {

    	
    	canvas.drawBitmap(bmp, 0, 0, paint);
    	return;
    	
    }
    
    private void prepareEnvironment(Context context) {

    	
  	   WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
       Display display = wm.getDefaultDisplay();
       
       
       DisplayMetrics metr = new DisplayMetrics();
       display.getMetrics(metr);

       Log.d(TAG, "metr: " + metr.toString() );
       
       winWidth = metr.widthPixels;
       winHeight = metr.heightPixels;

       Log.d(TAG, "window: " + winWidth + " ,"+winHeight );
        
       // set the Paint Obj
       paint.setColor(Color.RED);
       paint.setAntiAlias(true);
        
       paintBack.setColor(Color.BLUE);
       paintBack.setAntiAlias(true);
       paintBack.setDither(false);
         
       // resize Bitmaps
       bmpBack = BitmapFactory.decodeResource(context.getResources(), R.drawable.backgnd_1);      //carichiamo l'immagine in una bitmap
       
       int iBitmapWidth = bmpBack.getWidth();
       int iBitmapHeigth = bmpBack.getHeight();

       Log.d(TAG, "bitmap: " + iBitmapWidth + " ,"+ iBitmapHeigth );
       
       float hScale = (float)winWidth / (float)iBitmapWidth;
       float vScale = (float)winHeight / (float)iBitmapHeigth;
       
       Log.d(TAG, "scale: " + hScale + " ,"+ vScale );
       
       // create a matrix for the manipulation
       Matrix trasf = new Matrix();
       trasf.postScale(hScale, vScale);
       bmpCock  = Bitmap.createBitmap(bmpBack, 0, 0, iBitmapWidth, iBitmapHeigth, trasf, true);
       // Finally we have the background
       bmpBack = bmpCock.createBitmap(bmpCock);
       
       
       
       // next ...
       bmpCock = BitmapFactory.decodeResource(context.getResources(), R.drawable.cockpit1);      //carichiamo l'immagine in una bitmap
    	
       return;
    }
    
}

class Point {
    float x, y;

    @Override
    public String toString() {
        return x + ", " + y;
    }
}