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
	private static Paint pennello;
	
	// Grafica
	private boolean hasBorder = false;
	private RectF displayBitmap = new RectF();
	private RectF displayArea = new RectF();
	private Point displayPosition = new Point();
	private RectF recDigits;
	private int digitWidth;
	private int digitHeight;
	
	private int foreColor = Color.WHITE;
	private int backColor = Color.BLACK;
	private int numOfDigits = 3;
	private int numOfDecimal = 0;

	private int scaleFontSize = 20;
	
    private Path pathOuterDisplay;
    private Path pathShadowDisplay;
    private Path pathInnerDisplay;
	
	private boolean isInvalid = false;
	
	// Dinamica
	private float inertia = 0.0f;
	
	
	// Costruttore x tipo 
	public GenericDisplay(final GenericFrame FRAME, final RectF DISPLAYAREA) {
		displayBitmap.set(FRAME.recBoundary);
		displayArea.set(DISPLAYAREA);
		displayPosition.x = displayBitmap.centerX();
		displayPosition.y = displayBitmap.centerY();
	}
	public GenericDisplay(final GenericFrame FRAME, final RectF DISPLAYAREA, final Point DISPLAYPOSITION) {
		displayBitmap.set(FRAME.recBoundary);
		displayArea.set(FRAME.recInnerFrame);
		displayPosition.x = DISPLAYPOSITION.x;
		displayPosition.y = DISPLAYPOSITION.y;
	}

	// Metodi ..................
	public void setDisplay(final int FORECOLOR, final int BACKCOLOR, final boolean HASBORDER, final int DIGITS, final int DECIMAL){
		foreColor = FORECOLOR;
		backColor = BACKCOLOR;
		hasBorder = HASBORDER;
		numOfDigits = DIGITS;
		numOfDecimal = DECIMAL;
		
		return;
	}
	
	public Bitmap drawDisplay(boolean FORCED){

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
    	tela.drawPath(pathInnerDisplay, pennello);
    	
    	// Disegna l'ombra
    	pennello.setARGB(Dash.SHADOWOPACITY, Dash.SHADOWGRAY, Dash.SHADOWGRAY, Dash.SHADOWGRAY);
    	tela.drawPath(pathShadowDisplay, pennello);
    	
		return(displayImage);
	}
	// ----------------
	
	
	private void buildDisplay(){
		
        // Crea i pennelli
		PathEffect effect = new PathEffect();
		Xfermode xfermode = new Xfermode();
		float miter = 0.1f;
        matita = new Paint();
        matita.setAntiAlias(true);
        matita.setDither(true);
        matita.setPathEffect(effect);
        matita.setStrokeCap(Cap.SQUARE);
        matita.setStrokeJoin(Join.ROUND);
        matita.setStrokeMiter(miter);
        matita.setStyle(Style.STROKE);
        matita.setXfermode(xfermode);
        matita.setColor(foreColor);
		
        pennello = new Paint();
        pennello.setAntiAlias(true);
        pennello.setDither(true);
        pennello.setPathEffect(effect);
        pennello.setStrokeCap(Cap.SQUARE);
        pennello.setStrokeJoin(Join.ROUND);
        pennello.setStrokeMiter(miter);
        pennello.setStyle(Style.FILL);
        pennello.setXfermode(xfermode);
        pennello.setColor(backColor);
		

        float lar, alt, sh;
        float le,to,ri,bo;
        lar = displayArea.width();
        alt = displayArea.height();
        le = displayPosition.x - (lar/2.0f);
        ri = displayPosition.x + (lar/2.0f);
        to = displayPosition.y - (alt/2.0f);
        bo = displayPosition.y + (alt/2.0f);
        sh = Dash.SHADOW_PER * lar;
        
        pathOuterDisplay = new Path();
        pathOuterDisplay.addRect(le, to, ri, bo, Direction.CW);

        pathShadowDisplay = new Path();
        pathShadowDisplay.moveTo(le+1, to+1);
        pathShadowDisplay.lineTo(ri-1, to+1);
        pathShadowDisplay.lineTo(ri-1, to+sh+1);
        pathShadowDisplay.lineTo(le+sh+1, to+sh+1);
        pathShadowDisplay.lineTo(le+sh+1, bo-1);
        pathShadowDisplay.lineTo(le+1, bo-1);
        pathShadowDisplay.close();
        
        pathInnerDisplay = new Path();
        pathOuterDisplay.addRect(le+2, to+2, ri-1, bo-1, Direction.CW);
	
        // Digit geometry
        int nDigits = numOfDigits + (numOfDecimal>0 ? numOfDecimal : 0);
        float digw = lar / (float)nDigits;
        float digh;
        
        if(alt / digw < 1.68f) { digw = alt / 1.68f; digh = digw * 1.68f; }
        else { digh = digw * 1.68f; };  
        
        digitWidth = (int)digw;
        digitHeight = (int)digh;
        
        recDigits = new RectF();
        recDigits.left = le + (lar - digw * nDigits) / 2.0f;
        recDigits.top = to + (alt - digh) / 2.0f;
        recDigits.right = le + digw * nDigits;
        recDigits.bottom = to + digh;

        return;
	}
	
	
	
}
