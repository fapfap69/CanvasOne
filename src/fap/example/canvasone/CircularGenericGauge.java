package fap.example.canvasone;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.util.Log;

public class CircularGenericGauge {

    private static final String TAG = "GenericGauge";
   
	private int positionX = 0;
	private int positionY = 0;
	private int dimension = 100;
	private Materiale frameMaterial = Materiale.STEEL;
	private Finitura frameEffect = Finitura.INNER_FRAME;
	private DisplayBackGround displayBkgd = DisplayBackGround.IVORY;
	
	
	protected int opacity = Dash.OPACITY;
	
	CircularFrame frame;
	GenericDial quadrante; 
	GenericAxes assi;

	GenericAxis asse;
	GenericDisplay display;
	
	// Constructor
//	public CircularGenericGauge() {
//		this.buildCircularGenericGauge(10, 10, 100);
//	}
	public CircularGenericGauge(int XPOS, int YPOS, int DIM) {
		this.buildCircularGenericGauge(XPOS, YPOS, DIM);
    	return;
	}
	

	// This method recalculate the geometry...
	public void reformatGauge() {
		frame.setDimension(dimension, dimension);
		quadrante.reformatDial(frame);
		
		return;
	}
	
	
	public void drawGauge(Canvas TELA) {
		
		Bitmap bmp = frame.drawFrame(false);
		TELA.drawBitmap(bmp, positionX, positionY, null);
		bmp = quadrante.drawDial(false);
		TELA.drawBitmap(bmp, positionX, positionY, null);
		bmp = asse.drawAxis(false);
		TELA.drawBitmap(bmp, positionX, positionY, null);
		bmp = display.drawDisplay(false);
		TELA.drawBitmap(bmp, positionX, positionY, null);
		
		return;
	}

	// Access & Setting Methods
	public void setPosition(final int XPOS, final int YPOS){
		positionX = (XPOS < 0)? 1 : XPOS;
		positionY = (YPOS < 0)? 1 : YPOS;
//		reformatGauge();
		return;
	}
	public void getPosition(int XPOS, int YPOS){
		XPOS = positionX;
		YPOS = positionY;
		return;
	}

	public void setDimension(final int DIM){
		dimension = DIM;
		this.reformatGauge();
		return;
	}
	public int getDimension(int DIM){
		DIM = dimension;
		return dimension;
	}
	
	public void setOpacity(final int OPA){
		opacity = OPA;
// force a redraw ??
		return;
	}
	public int getOpacity(int OPA){
		OPA = opacity;
		return opacity;
	}

	// ----------------------------------
	// Build the Gauge and instance the element
	private void buildCircularGenericGauge(int XPOS, int YPOS, int DIM) {

		// Setta la geometria
		positionX = (XPOS < 0)? 1 : XPOS;
		positionY = (YPOS < 0)? 1 : YPOS;
		dimension = DIM;
		
		Log.d(TAG, "Create! " );
		// ---- crea le istanze
    	frame = new CircularFrame(dimension,dimension);
    	frame.setMaterial(frameMaterial);
    	frame.setEffect(frameEffect);
    	
    	quadrante = new GenericDial(frame, DisplayBackGround.IVORY);

    	quadrante.setDialTitle("PROVA");
    	// --
    		quadrante.titolo.hasBorder = false;
    		quadrante.titolo.setColors(Color.BLACK, Color.argb(0, 250, 205, 100));
    		quadrante.titolo.setAlign(Align.CENTER);
    	
/*
    	assi = new GenericAxes();
    	assi.addAxe(GaugeAxePosition.LEFT, "Pippo", "cm");
		assi.addAxe(GaugeAxePosition.RIGHT, "Lillo", "Km");
*/
    	asse = new GenericAxis(frame, GaugeAxePosition.CENTER);
    	asse.setUnit("m/sil");
    	asse.setScale(300, 299, 180, 1, 12.9f, 1,0, Color.rgb(25, 80, 40), 25, true, true, false, false, false);	
		
    	RectF disp = new RectF();
		disp.set(0, 0, 200, 180);
		Point corn = new Point();
		corn.x = 200;
		corn.y = 200;
		
    	display = new GenericDisplay(frame, disp,corn);
    	display.setDisplay(Color.BLUE, Color.RED, true, 4,0);

    	this.reformatGauge();
		
    	return;
		
		
	}
	
	
}
