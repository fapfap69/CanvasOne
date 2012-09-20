package fap.example.canvasone;

import android.graphics.Path;
import android.graphics.RectF;

public class RectangularFrame extends GenericFrame{

	// Dimensione percentuale del bordo
	private static final float ROUNDCORNER_PER = 0.025f;

	
	public RectangularFrame(int WIDTH, int HEIGHT){
		super(WIDTH, HEIGHT);
	}
	
	
	// Metodi
	@Override
	public Tipo getType() {
		return Tipo.RECTANGULAR;
	}
	

    // Calculate the Gauge dimensions
	@Override
    public void reformatFrame() {

    	float inShad = larghezza * Dash.THICK_PER;
    	float outShad = larghezza * Dash.SHADOW_PER;
    	float bordo = larghezza * Dash.BORDER_PER;
    	float step = larghezza * (Dash.BORDER_PER * Dash.INNERSTEP_PER);

    	float smoothrad = larghezza * ROUNDCORNER_PER;
    	
    	// Superfice totale
    	recBoundary = new RectF(0,0,larghezza-1,altezza-1);  
    	// Superficie occupata dallo strumento senza l'ombra
    	recOuterFrame = new RectF(0.0f,0.0f, larghezza - outShad , altezza - outShad);
    	// Superficie del bordo interno 
    	recInnerFrame = new RectF( bordo , bordo , recOuterFrame.right - bordo , recOuterFrame.bottom - bordo );
    	// Superficie occupata dall'ombra
    	recOuterShadow = new RectF( outShad , outShad ,larghezza , altezza);
    	recInnerShadow = new RectF( recInnerFrame.left + inShad , recInnerFrame.top + inShad ,recInnerFrame.right , recInnerFrame.bottom);
    	// Superficie occupata dall'dente interno
    	recInnerStep = new RectF( recOuterFrame.left + step , recOuterFrame.top + step, recOuterFrame.right - step , recOuterFrame.bottom - step );
    
    	// centro dello strumento 
    	centerX = (recInnerFrame.width() / 2.0f) + recInnerFrame.left;
    	centerY = (recInnerFrame.height() / 2.0f) + recInnerFrame.top;
    	// raggio dello strumento 
    	frameRadius = (recOuterFrame.width() / 2.0f);
    	
    	// Path Del Frame
		pathIntFrame.reset();
		pathIntFrame.addRoundRect(recInnerFrame, smoothrad, smoothrad, Path.Direction.CW);
		pathIntFrame.close();
		
		pathExtFrame.reset();
		pathExtFrame.addRoundRect(recOuterFrame, smoothrad, smoothrad, Path.Direction.CW);
		pathExtFrame.close();
		
		pathStepFrame.reset();
		pathStepFrame.addRoundRect(recInnerStep, smoothrad, smoothrad, Path.Direction.CW);
		pathStepFrame.close();
		
		pathShadowFrame.reset();
		pathShadowFrame.addRoundRect(recOuterShadow, smoothrad, smoothrad, Path.Direction.CW);
		pathShadowFrame.addRoundRect(recOuterFrame, smoothrad, smoothrad, Path.Direction.CCW);
		pathShadowFrame.close();
		
		pathInShadowFrame.reset();
		pathInShadowFrame.addRoundRect(recInnerFrame, smoothrad, smoothrad, Path.Direction.CW);
		pathInShadowFrame.addRoundRect(recInnerShadow, smoothrad, smoothrad, Path.Direction.CW);
		pathInShadowFrame.close();

		isInvalid = true;
		
		return;
    }
	
}
