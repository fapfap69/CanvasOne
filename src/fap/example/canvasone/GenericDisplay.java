package fap.example.canvasone;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Region.Op;

public class GenericDisplay {
	
	// Opacità dello strumento
	private int opacity = Dash.OPACITY;

	// Contesto
	private Canvas tela;
	private static Bitmap displayImage;
	private static Paint matita;
	private static Paint ledOn;
	private static Paint ledOff;
	private static Paint pennello;
	
	// Grafica
	private boolean hasBorder = false;
	private RectF displayBitmap = new RectF();
	private RectF displayArea = new RectF();
	private Point displayPosition = new Point();
	private RectF recDigits;
	
	private int foreColor = Color.WHITE;
	private int backColor = Color.BLACK;
	private int borderColor = Color.GRAY;
	
	private int numOfDigits = 3;
	private int numOfDecimal = 0;

    private Path pathOuterDisplay;
    private Path pathShadowDisplay;
    private Path pathInnerDisplay;
	
	private boolean isInvalid = false;
	
	// Dinamica
	private float inertia = 0.0f;

	// Visualizzazione 
	private SevenSegment cifre;
	
	// Costruttore x tipo 
	public GenericDisplay(final GenericFrame FRAME, final RectF DISPLAYAREA) {
		displayBitmap.set(FRAME.recBoundary);
		displayArea.set(DISPLAYAREA);
		displayPosition.x = displayBitmap.centerX();
		displayPosition.y = displayBitmap.centerY();
		this.buildDisplay();
	}
	
	public GenericDisplay(final GenericFrame FRAME, final RectF DISPLAYAREA, final Point DISPLAYPOSITION) {
		displayBitmap.set(FRAME.recBoundary);
		displayArea.set(DISPLAYAREA);
		displayPosition.x = DISPLAYPOSITION.x;
		displayPosition.y = DISPLAYPOSITION.y;
		this.buildDisplay();
	}

	public GenericDisplay(final CircularFrame FRAME, final int XPOSITION, final int YPOSITION, final int WIDTH, final int HEIGHT) {
		displayBitmap.set(FRAME.recBoundary);
		displayArea.set(0,0,WIDTH-1,HEIGHT-1);
		displayPosition.x = XPOSITION;
		displayPosition.y = YPOSITION;
		this.buildDisplay();
	}
	// Metodi ..................
	public void setDisplay(final int FORECOLOR, final int BACKCOLOR, final boolean HASBORDER, final int BORDERCOLOR, final int DIGITS, final int DECIMAL){
		foreColor = FORECOLOR;
		backColor = BACKCOLOR;
		borderColor = BORDERCOLOR;
		hasBorder = HASBORDER;
		numOfDigits = DIGITS;
		numOfDecimal = DECIMAL;
		this.buildDisplay();
		isInvalid = true;
		return;
	}
	
	// Setting ...
	public void setValue(float VALUE) {
		
		return;
	}


	public Bitmap drawDisplay(boolean FORCED, float VALUE){
		String buffer = String.format("%"+ numOfDigits + "." + numOfDecimal+ "f", VALUE);
		return(this.drawDisplay(FORCED, buffer));
	}
	public Bitmap drawDisplay(boolean FORCED, int VALUE){
		String buffer = String.format("%"+ numOfDigits + "d", VALUE);
		return(this.drawDisplay(FORCED, buffer));
	}
	
	public Bitmap drawDisplay(boolean FORCED, String VALUE){

		if(!isInvalid && !FORCED) {
			return(displayImage);
		} else {
			this.isInvalid = false;
		}
		// -----------------------------i
	        
		// Crea la nuova bitmap
		displayImage = Bitmap.createBitmap((int)displayBitmap.width() , (int)displayBitmap.height(), Config.ARGB_8888);
		// ed il contesto su cui disegnare
	    tela = new Canvas(displayImage);
		tela.clipRect(displayBitmap, Op.REPLACE);

	    // Disegna il bordo
	    if(hasBorder) {
	    	tela.drawPath(pathOuterDisplay, matita);
	    }

	    // Disegna lo sfondo del display
    	pennello.setColor(backColor);
    	pennello.setAlpha(opacity);
    	tela.drawPath(pathInnerDisplay, pennello);
    	
    	// Disegna l'ombra
    	pennello.setARGB(Dash.SHADOWOPACITY, Dash.SHADOWGRAY, Dash.SHADOWGRAY, Dash.SHADOWGRAY);
    	tela.drawPath(pathShadowDisplay, pennello);
    	
    	VALUE = String.format("%"+ numOfDigits + "s", VALUE);
    	cifre.drawValue(tela, ledOn, ledOff, VALUE);
    	
		return(displayImage);
	}
	// ----------------
	
	
	private void buildDisplay(){
		
        // Crea i pennelli
		PathEffect effect = new PathEffect();
		Xfermode xfermode = new Xfermode();
		float miter = 1.0f;
        matita = new Paint();
        matita.setAntiAlias(true);
        matita.setDither(true);
        matita.setPathEffect(effect);
        matita.setStrokeCap(Cap.SQUARE);
        matita.setStrokeJoin(Join.ROUND);
        matita.setStrokeMiter(miter);
        matita.setStyle(Style.STROKE);
        matita.setXfermode(xfermode);
        matita.setColor(borderColor);

        ledOn = new Paint();
        ledOn.setAntiAlias(true);
        ledOn.setDither(true);
        ledOn.setPathEffect(effect);
        ledOn.setStrokeCap(Cap.SQUARE);
        ledOn.setStrokeJoin(Join.ROUND);
        ledOn.setStrokeMiter(miter);
        ledOn.setStyle(Style.FILL_AND_STROKE);
        ledOn.setXfermode(xfermode);
        ledOn.setColor(foreColor);
        
        ledOff = new Paint();
        ledOff.setAntiAlias(true);
        ledOff.setDither(true);
        ledOff.setPathEffect(effect);
        ledOff.setStrokeCap(Cap.SQUARE);
        ledOff.setStrokeJoin(Join.ROUND);
        ledOff.setStrokeMiter(miter);
        ledOff.setStyle(Style.FILL_AND_STROKE);
        ledOff.setXfermode(xfermode);
        float[] hsv = new float[3];
        Color.colorToHSV(foreColor, hsv);
        hsv[2] = hsv[2] / 3.0f;
        int alpha = Color.alpha(foreColor);
        ledOff.setColor(Color.HSVToColor(hsv));
        ledOff.setAlpha(alpha);
        
        pennello = new Paint();
        pennello.setAntiAlias(true);
        pennello.setDither(true);
        pennello.setPathEffect(effect);
        pennello.setStrokeCap(Cap.SQUARE);
        pennello.setStrokeJoin(Join.ROUND);
        pennello.setStrokeMiter(miter);
        pennello.setStyle(Style.FILL_AND_STROKE);
        pennello.setXfermode(xfermode);
        pennello.setColor(backColor);
        pennello.setAlpha(Color.alpha(backColor));
		

        float lar, alt, sh;
        float le,to,ri,bo;
        lar = displayArea.width();
        alt = displayArea.height();
        le = displayPosition.x - (lar/2.0f);
        ri = displayPosition.x + (lar/2.0f);
        to = displayPosition.y - (alt/2.0f);
        bo = displayPosition.y + (alt/2.0f);
        sh = Dash.SHADOW_PER * lar * 4;
        
        pathOuterDisplay = new Path();
        RectF appo = new RectF();
        appo.set(le, to, ri, bo);
        pathOuterDisplay.addRoundRect(appo, 4, 4, Direction.CW);

        pathShadowDisplay = new Path();
        pathShadowDisplay.moveTo(le+1, to+1);
        pathShadowDisplay.lineTo(ri-1, to+1);
        pathShadowDisplay.lineTo(ri-1, to+sh+1);
        pathShadowDisplay.lineTo(le+sh+1, to+sh+1);
        pathShadowDisplay.lineTo(le+sh+1, bo-1);
        pathShadowDisplay.lineTo(le+1, bo-1);
        pathShadowDisplay.close();
        
        pathInnerDisplay = new Path();
        appo.set(le+1, to+1, ri-1, bo-1);        
        pathInnerDisplay.addRoundRect(appo,  4,4,Direction.CW);
	
        recDigits = new RectF();
        recDigits.set(le+4, to+4, ri-4, bo-4);
        
        cifre = new SevenSegment(recDigits,numOfDigits,numOfDecimal);
        
        return;
	}

	
	
	
}
