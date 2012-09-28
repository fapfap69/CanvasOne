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
	GenericAxis asse2;
	
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
	
	static int Value = 0;
	public void drawGauge(Canvas TELA, float ValDisplay) {
	
		String DispBuffer;
		
		Bitmap bmp = frame.drawFrame(false);
		TELA.drawBitmap(bmp, positionX, positionY, null);
		bmp = quadrante.drawDial(false);
		TELA.drawBitmap(bmp, positionX, positionY, null);

		bmp = asse.drawAxis(false);
		TELA.drawBitmap(bmp, positionX, positionY, null);
		bmp = asse2.drawAxis(false);
		TELA.drawBitmap(bmp, positionX, positionY, null);

		bmp = display.drawDisplay(true, Value++);
		TELA.drawBitmap(bmp, positionX, positionY, null);
		
		bmp = asse.drawIndicator(true, ValDisplay);
		TELA.drawBitmap(bmp, positionX, positionY, null);
		bmp = asse2.drawIndicator(true, ValDisplay);
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
   		quadrante.titolo.hasBorder = false;
   		quadrante.titolo.setColors(Color.BLACK, Color.argb(0, 250, 205, 100));
   		quadrante.titolo.setAlign(Align.CENTER);

    	// --
    	asse = new GenericAxis(frame, GaugeAxePosition.LEFT);
    	asse.setUnit("m/sil");
    	float[] thi = {1.0f, 0.5f, 0.1f};
    	asse.setScale(300.0f, 60.0f, 100.0f, 0.0f, 10.0f, thi, Color.rgb(25, 80, 40), 25, true, true, false, false, false);	
//    	asse.setPointer(PointerType.HAND2, Material.Type.BRASS);
    	asse.setPointer(PointerType.NEEDLE4, Material.Type.LINEARVERTICAL, Color.argb(opacity, 00, 0, 0), Color.argb(opacity, 200, 200, 200), (int)(DIM/6.0f) );
    	

    	asse2 = new GenericAxis(frame, GaugeAxePosition.RIGHT);
    	asse2.setUnit("m/sil");
    	asse2.setScale(120.0f, 240.0f, 100.0f, 1.0f, 3.0f, thi, Color.rgb(25, 80, 40), 25, true, true, false, false, false);	
//    	asse.setPointer(PointerType.HAND2, Material.Type.BRASS);
    	asse2.setPointer(PointerType.NEEDLE4, Material.Type.LINEARVERTICAL, Color.argb(opacity, 00, 0, 0), Color.argb(opacity, 200, 200, 200), (int)(DIM/6.0f) );

    	
    	// --
    	display = new GenericDisplay(frame, 250, 320, 100, 50);
    	display.setDisplay(Color.RED, Color.BLACK, true, Color.GRAY, 4, 0);

    	this.reformatGauge();
		
    	return;
		
		
	}
	
	
}
