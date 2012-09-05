package fap.example.canvasone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.PathEffect;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Xfermode;
import android.util.Log;


public class CircularGenericGauge {

    private static final String TAG = "GenericGauge";
	
	private int positionX;
	private int positionY;
	private int dimension;
	private int Opacity;
	private int colRed, colGreen, colBlue;
	private int borderWidth;
	
	Paint pennello;
	
	public CircularGenericGauge() {
    		Log.d(TAG, "Create! " );
	
		// TODO Auto-generated constructor stub
	}

	public void drawGauge(Canvas canvas) {
		// TODO Auto-generated method stub
		drawBorder(canvas);
		
	}

	public void setContext(Context context) {
		
	}
	public void setPosition(int x, int y) {
		positionX = (x < 0 ? 0 : x);
		positionY = (y < 0 ? 0 : y);
		return;
	}
	public void getPosition(Integer x, Integer y) {
		x = positionX;
		y = positionY;
		return;
	}
	public void setDimension(int raggio) {
		dimension = (raggio < 0 ? 10 : raggio);
	}
	public int getDimension() {
		return dimension;
	}
	public void setColor(int color) {
		colRed = Color.red(color);
		colGreen = Color.green(color);
		colBlue = Color.blue(color);
		Opacity = Color.alpha(color);
		return;
	}
	public int getColor() {
		return Color.argb(Opacity, colRed, colGreen, colBlue);
	}

	
	private void drawBorder(Canvas canvas) {

		float SHADOW_START_ANGLE = +135.0f;
		float SHADOW_SWIP_ANGLE = 180.0f;
		int BORDER_WIDTH = 20;
		
		PathEffect effect = new PathEffect();
		float miter = 0.2f;
		borderWidth = BORDER_WIDTH;
		Xfermode xfermode = new Xfermode();
		
		RectF oval = new RectF();

		pennello = new Paint();
		pennello.setAlpha(Opacity);
		pennello.setAntiAlias(true);
		pennello.setDither(true);
		pennello.setPathEffect(effect);
		pennello.setStrokeCap(Cap.SQUARE);
		pennello.setStrokeJoin(Join.ROUND);
		pennello.setStrokeMiter(miter);
		pennello.setStyle(Style.STROKE);
		pennello.setXfermode(xfermode);

		// start to draw
		pennello.setStrokeWidth(1);

		oval.bottom = positionY + dimension;
		oval.top = positionY - dimension;
		oval.left = positionX - dimension;
		oval.right = positionX + dimension;
		pennello.setARGB(Opacity, 255, 255, 255);
		canvas.drawArc(oval, SHADOW_START_ANGLE, SHADOW_SWIP_ANGLE, false, pennello);
		pennello.setARGB(Opacity, 64, 64, 64);
		canvas.drawArc(oval, SHADOW_START_ANGLE+SHADOW_SWIP_ANGLE, SHADOW_SWIP_ANGLE, true, pennello);
		
	//	canvas.drawCircle(positionX, positionY, dimension-(borderWidth/2), pennello);
		
		oval.bottom = positionY + (dimension-borderWidth);
		oval.top = positionY - (dimension-borderWidth);
		oval.left = positionX - (dimension-borderWidth);
		oval.right = positionX + (dimension-borderWidth);
		pennello.setARGB(Opacity, 64, 64, 64);
		canvas.drawArc(oval, SHADOW_START_ANGLE, SHADOW_SWIP_ANGLE, false, pennello);
		pennello.setARGB(Opacity, 255, 255, 255);
		canvas.drawArc(oval, SHADOW_START_ANGLE+SHADOW_SWIP_ANGLE, SHADOW_SWIP_ANGLE, true, pennello);
		
		pennello.setARGB(Opacity, colRed, colGreen, colBlue);
		pennello.setStrokeWidth(borderWidth-2);
		
		RadialGradient gradient = new RadialGradient(positionX-100, positionY-100, 100, // dimension-(borderWidth/2), 
				Color.argb(Opacity, colRed, colGreen, colBlue),
				Color.argb(Opacity, colRed+20, colGreen+20, colBlue+20),
				android.graphics.Shader.TileMode.REPEAT);
		
/*		Shader gradient = new SweepGradient(positionX, positionY-50,
				Color.argb(Opacity, colRed, colGreen, colBlue),
				Color.argb(Opacity, colRed+50, colGreen+50, colBlue+50));
				*/
		pennello.setShader(gradient);
		canvas.drawCircle(positionX, positionY, dimension-(borderWidth/2), pennello);

		return;
	}
	
	
}
