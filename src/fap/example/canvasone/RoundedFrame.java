package fap.example.canvasone;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Path.FillType;
import android.graphics.Region.Op;

public class RoundedFrame extends GenericFrame{

	// Dimensione percentuale del bordo
	private static final float ROUNDCORNER_PER = 0.04f;
	
	private boolean hasScrew;

	// Metodi
	@Override
	public Tipo getType() {
		return Tipo.ROUNDED;
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
    	recInnerStep = new RectF( recInnerFrame.left - step , recInnerFrame.top - step, recInnerFrame.right + step , recInnerFrame.bottom + step );
    
    	// centro dello strumento 
    	centerX = (recInnerFrame.width() / 2.0f) + recInnerFrame.left;
    	centerY = (recInnerFrame.height() / 2.0f) + recInnerFrame.top;
    	// raggio dello strumento 
    	frameRadius = (recOuterFrame.width() / 2.0f);
    	
    	// Path Del Frame
		pathIntFrame.reset();
		pathIntFrame.addOval(recInnerFrame, Path.Direction.CW);
		pathIntFrame.close();
		
		pathExtFrame.reset();
		pathExtFrame.addRoundRect(recOuterFrame, smoothrad, smoothrad, Path.Direction.CW);
		pathExtFrame.close();
		
		pathStepFrame.reset();
		pathStepFrame.addOval(recInnerStep, Path.Direction.CW);
		pathStepFrame.close();
		
		pathShadowFrame.reset();
		pathShadowFrame.addRoundRect(recOuterShadow, smoothrad, smoothrad, Path.Direction.CW);
		pathShadowFrame.addRoundRect(recOuterFrame, smoothrad, smoothrad, Path.Direction.CCW);
		pathShadowFrame.close();
		
		pathInShadowFrame.reset();
		pathInShadowFrame.addOval(recInnerFrame,Path.Direction.CW);
		pathInShadowFrame.addOval(recInnerShadow, Path.Direction.CCW);
		pathInShadowFrame.close();
		
		isInvalid = true;
		
		return;
    }

	@Override
	public void drawTheFrameBody(Canvas tela) {
		super.drawTheFrameBody(tela);
		if(hasScrew) drawScrews(tela);
        return;
    }

	
	public boolean getHasScrew() {
		return hasScrew;
	}
	public void setHasScrew(boolean HASSCREW) {
		hasScrew = HASSCREW;
		return;
	}
	
	private void drawScrews(Canvas tela) {
		float radius = larghezza * ROUNDCORNER_PER * 2.0f;
		float displa = larghezza * ROUNDCORNER_PER * 2.5f;
				
		Path framePath = new Path();
    	framePath.addRoundRect(recOuterFrame, larghezza * ROUNDCORNER_PER, larghezza * ROUNDCORNER_PER, Path.Direction.CW);
    	tela.clipPath(framePath , Op.REPLACE );

    	drawScrew(tela,recOuterFrame.left + displa, recOuterFrame.top + displa, radius);
    	drawScrew(tela,recOuterFrame.right - displa, recOuterFrame.top + displa, radius);
    	drawScrew(tela,recOuterFrame.left + displa, recOuterFrame.bottom - displa, radius);
    	drawScrew(tela,recOuterFrame.right - displa, recOuterFrame.bottom - displa, radius);
    	
    	return;
		
		
	}
	
    private void drawScrew(Canvas tela, float cx, float cy, float radius) {
    	float inrad = radius * .50f;
    	matita.setARGB(opacity, 20, 20, 20);
    	tela.drawCircle(cx, cy, radius, matita);
    	tela.drawLine(cx-inrad-2,cy-inrad,cx+inrad-2,cy+inrad,matita);
    	
    	matita.setARGB(opacity, 200, 200, 200);
    	tela.drawCircle(cx+2, cy+2, radius, matita);
    	tela.drawLine(cx-inrad+2,cy-inrad,cx+inrad+2,cy+inrad,matita);
    	return;
    }
	
	
}
