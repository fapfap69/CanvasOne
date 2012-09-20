package fap.example.canvasone;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.Path.FillType;
import android.graphics.Region.Op;
import android.util.Log;

public class ArcFrame extends CircularFrame{

	// Dimensione percentuale del bordo
	private static final float ROUNDCORNER_PER = 0.025f;
	
	private static final float START_ARC = 270.f;
	private static final float SWEEP_ARC = 90.0f;
	
	private int quadrante = 1;
	private Float startArc = START_ARC;
	private Float endArc = START_ARC + SWEEP_ARC;

	private static RectF recOuterOval = new RectF();
	private static RectF recInnerOval = new RectF();
	private static RectF recShadowOval = new RectF();
	private static RectF recStepOval = new RectF();
	private static Point centerOval = new Point();
	private static RectF recVerBor = new RectF();
	private static RectF recHorBor = new RectF();
	
	
	public ArcFrame(int WIDTH, int HEIGHT){
		super(WIDTH, HEIGHT);
	}
	
	// Metodi
	
	@Override
	public void reformatFrame() {
		super.reformatFrame();
		recalculateOvalShape();
		return;
	}
	
	@Override
	public Tipo getType() {
		return Tipo.ARC;
	}
	
	public int getQuadrant() {
		return(calculateQuadrant(startArc));
	}
	public void setQuadrant(int QUADRANT) {
		quadrante = QUADRANT;
		startArc = calculateAngle(QUADRANT);
		endArc = startArc + SWEEP_ARC;
		return;
	}
	public float getSweepAngle() {
		return(SWEEP_ARC);
	}

	// Converte il quadrante in angolo 
	private float calculateAngle(int quadrant) {
		return(360.0f - quadrant * 90.0f);
	}
	private int calculateQuadrant(float angle) {
		return (int) ((startArc - 360.0f) / 90.0f);
	}
	// ricalcola l'ovale che contiene il quadrante
	private void recalculateOvalShape() {

		float bordo = (recOuterFrame.width() - recInnerFrame.width()) / 2.0f;
		float step = larghezza * (Dash.BORDER_PER * Dash.INNERSTEP_PER);
		float shad = larghezza * Dash.SHADOW_PER;
		
		recOuterOval.left = recOuterFrame.left;
		recOuterOval.top = recOuterFrame.top;
		
		recInnerOval.left = recInnerFrame.left;
		recInnerOval.top = recInnerFrame.top;
		recInnerOval.right = recInnerFrame.left + recInnerFrame.width() * 2.0f;
		recInnerOval.bottom = recInnerFrame.top + recInnerFrame.height() * 2.0f;

		recOuterOval.right = recInnerOval.right + bordo;
		recOuterOval.bottom = recInnerOval.bottom + bordo;

		recShadowOval.left = recOuterOval.left + shad;
		recShadowOval.top = recOuterOval.top + shad;
		recShadowOval.right = recOuterOval.right + shad;
		recShadowOval.bottom = recOuterOval.bottom + shad;

		recStepOval.left = recInnerOval.left - step;
		recStepOval.top = recInnerOval.top - step;
		recStepOval.right = recInnerOval.right + step;
		recStepOval.bottom = recInnerOval.bottom + step;
				
		centerOval.x = recInnerFrame.right;
		centerOval.y = recInnerFrame.bottom;

		pathIntFrame.reset();
		pathIntFrame.addArc(recInnerOval, startArc, SWEEP_ARC);
		pathIntFrame.lineTo(centerOval.x, centerOval.y);
		pathIntFrame.close();
		
		pathExtFrame.reset();
		pathStepFrame.reset();
		pathShadowFrame.reset();
		
		switch(quadrante) {
		case 1:
			recVerBor.left = centerOval.x - bordo;
			recVerBor.top = recOuterOval.top;
			recVerBor.right = centerOval.x;
			recVerBor.bottom = centerOval.y + bordo;
			recHorBor.left = recVerBor.left;
			recHorBor.top = centerOval.y;
			recHorBor.right = recOuterOval.right;
			recHorBor.bottom = recVerBor.bottom;
			
			pathExtFrame.addArc(recOuterOval, startArc, SWEEP_ARC);
			pathExtFrame.lineTo(recOuterOval.right, centerOval.y + bordo);
			pathExtFrame.lineTo(centerOval.x - bordo, centerOval.y + bordo);
			pathExtFrame.lineTo(centerOval.x - bordo, recOuterOval.top);

			pathStepFrame.addArc(recStepOval, startArc, SWEEP_ARC);
			pathStepFrame.lineTo(recStepOval.right, centerOval.y + step);
			pathStepFrame.lineTo(centerOval.x - step, centerOval.y + step);
			pathStepFrame.lineTo(centerOval.x - step, recStepOval.top);
			
			pathShadowFrame.moveTo(recShadowOval.right, centerOval.y);
			pathShadowFrame.lineTo(recShadowOval.right, centerOval.y + bordo + shad);
			pathShadowFrame.lineTo(centerOval.x - bordo + shad, centerOval.y + bordo + shad);
			pathShadowFrame.lineTo(centerOval.x - bordo + shad, centerOval.y + bordo +1);
			pathShadowFrame.lineTo(recOuterOval.right +1, centerOval.y + bordo +1);
			pathShadowFrame.lineTo(recOuterOval.right +1, centerOval.y -3);
			
			break;
			
		case 2:
			recVerBor.left = centerOval.x;
			recVerBor.top = recOuterOval.top;
			recVerBor.right = centerOval.x + bordo;
			recVerBor.bottom = centerOval.y + bordo;
		    recHorBor.left = recOuterOval.left;
		    recHorBor.top = centerOval.y;
		    recHorBor.right = recVerBor.right;
		    recHorBor.bottom = recVerBor.bottom;
		    
			pathExtFrame.addArc(recOuterOval, startArc, SWEEP_ARC);
			pathExtFrame.lineTo(centerOval.x + bordo, recOuterOval.top);
			pathExtFrame.lineTo(centerOval.x + bordo, centerOval.y + bordo);
			pathExtFrame.lineTo(recOuterOval.left, centerOval.y + bordo);

			pathStepFrame.addArc(recStepOval, startArc, SWEEP_ARC);
			pathStepFrame.lineTo(centerOval.x + step, recStepOval.top);
			pathStepFrame.lineTo(centerOval.x + step, centerOval.y + step);
			pathStepFrame.lineTo(recStepOval.left, centerOval.y + step);
			
			pathShadowFrame.moveTo(centerOval.x + bordo +1, centerOval.y + bordo +1);
			pathShadowFrame.lineTo(recOuterOval.left + shad, centerOval.y + bordo +1);
			pathShadowFrame.lineTo(recOuterOval.left + shad, centerOval.y + bordo + shad);
			pathShadowFrame.lineTo(centerOval.x + bordo + shad +1, centerOval.y + bordo + shad);
			pathShadowFrame.lineTo(centerOval.x + bordo + shad +1, recOuterOval.top - shad);
			pathShadowFrame.lineTo(centerOval.x + bordo + 1, recOuterOval.top - shad);

			break;
		case 3:
			recVerBor.left = centerOval.x;
			recVerBor.top = centerOval.y - bordo;
			recVerBor.right = centerOval.x + bordo;
			recVerBor.bottom = recOuterOval.bottom;
		    recHorBor.left = recOuterOval.left;
		    recHorBor.top = recVerBor.top;
		    recHorBor.right = recVerBor.right;
		    recHorBor.bottom = centerOval.y;
		    
			pathExtFrame.addArc(recOuterOval, startArc, SWEEP_ARC);
			pathExtFrame.lineTo(recOuterOval.left, centerOval.y - bordo);
			pathExtFrame.lineTo(centerOval.x + bordo, centerOval.y - bordo);
			pathExtFrame.lineTo(centerOval.x + bordo, recOuterOval.bottom);

			pathStepFrame.addArc(recStepOval, startArc, SWEEP_ARC);
			pathStepFrame.lineTo(recStepOval.left, centerOval.y - step);
			pathStepFrame.lineTo(centerOval.x + step, centerOval.y - step);
			pathStepFrame.lineTo(centerOval.x + step, recStepOval.bottom);

			pathShadowFrame.moveTo(centerOval.x + bordo + shad +1, recOuterOval.bottom + shad +1);
			pathShadowFrame.lineTo(centerOval.x + bordo + shad +1, centerOval.y - bordo + shad);
			pathShadowFrame.lineTo(centerOval.x + bordo +1, centerOval.y - bordo + shad);
			pathShadowFrame.lineTo(centerOval.x + bordo +1, recOuterOval.bottom +1);
			pathShadowFrame.lineTo(centerOval.x -4, recOuterOval.bottom +1);
			pathShadowFrame.lineTo(centerOval.x, recOuterOval.bottom +shad +1);
			
			break;
		case 4:
			recVerBor.left = centerOval.x - bordo;
			recVerBor.top = centerOval.y - bordo;
			recVerBor.right = centerOval.x;
			recVerBor.bottom = recOuterOval.bottom;
			recHorBor.left = recVerBor.left;
		    recHorBor.top = recVerBor.top;
		    recHorBor.right = recOuterOval.right;
		    recHorBor.bottom = centerOval.y;
		    
			pathExtFrame.addArc(recOuterOval, startArc, SWEEP_ARC);
			pathExtFrame.lineTo(centerOval.x - bordo, recOuterOval.bottom);
			pathExtFrame.lineTo(centerOval.x - bordo, centerOval.y - bordo);
			pathExtFrame.lineTo(recOuterOval.right, centerOval.y - bordo);
		    
			pathStepFrame.addArc(recStepOval, startArc, SWEEP_ARC);
			pathStepFrame.lineTo(centerOval.x - step, recStepOval.bottom);
			pathStepFrame.lineTo(centerOval.x - step, centerOval.y - step);
			pathStepFrame.lineTo(recStepOval.right, centerOval.y - step);

			pathShadowFrame.moveTo(recShadowOval.right, centerOval.y);
			pathShadowFrame.arcTo(recShadowOval, startArc, SWEEP_ARC);
			pathShadowFrame.lineTo(centerOval.x-bordo, recShadowOval.bottom);
			pathShadowFrame.lineTo(centerOval.x-bordo, recOuterOval.bottom);
			pathShadowFrame.lineTo(centerOval.x, recOuterOval.bottom);
			pathShadowFrame.arcTo(recOuterOval, startArc+90.0f, -SWEEP_ARC );
			
			break;
		}
		
		pathStepFrame.close();
		pathExtFrame.close();
		pathShadowFrame.close();
		
		return;
	}
		
	
	private void cambiaView(Canvas tela, int quadr){
		switch(quadr){
		case -1:
			tela.restore();
			break;
		case 1:
			tela.save();
			tela.translate(-recVerBor.left-1, 0);
			break;
		case 2:
			tela.save();
			tela.translate(0, 0);
			break;
		case 3:
			tela.save();
			tela.translate(0, -recHorBor.top-1);
			break;
		case 4:
			tela.save();
			tela.translate(-recVerBor.left-1,- recHorBor.top -1);
			break;
		default:
			tela.save();
			tela.translate(0, 0);
			break;
		}
		return;
	}
	
	
	
	@Override
    public void drawTheFrameBody(Canvas tela) {
    	
		cambiaView(tela,quadrante);

		pathExtFrame.setFillType(FillType.WINDING);
		tela.clipPath(pathExtFrame, Op.REPLACE);
		pathIntFrame.setFillType(FillType.WINDING);
		tela.clipPath(pathIntFrame, Op.DIFFERENCE);
        tela.drawPaint(pennello);
        
		cambiaView(tela,-1);
		
        return;
        
    }

	@Override
    public void drawTheBorder(Canvas tela) {

		cambiaView(tela,quadrante);

        tela.drawPath(pathExtFrame, matita);
        tela.drawPath(pathIntFrame, matita);
        
		cambiaView(tela,-1);
		
        return;
    }    

	@Override
	public void drawTheShadow(Canvas tela) {
    	
    	cambiaView(tela,quadrante);
    	
    	pennello.setShader(null);
    	pennello.setARGB(Dash.SHADOWOPACITY, Dash.SHADOWGRAY, Dash.SHADOWGRAY, Dash.SHADOWGRAY);
    	
    	pathShadowFrame.setFillType(FillType.WINDING);
    	tela.clipPath(pathShadowFrame, Op.REPLACE);
    	tela.drawPaint(pennello);
		cambiaView(tela,-1);
		
    	return;
    	
    }
	
	
	@Override
    public void drawTheEffect(Finitura EFFECT, Canvas tela) {


    	cambiaView(tela,quadrante);
    	
    	float[] posizioni;
    	int[] colori;
    	RadialGradient gradienteR = null; // = new RadialGradient(1,1,1,0,0,null);
    	LinearGradient gradienteL = null; // = new LinearGradient(1,1,2,2,0,0, null);
		
    	switch (EFFECT) {
    		case NONE:
    		default:
    			break;

    		case BULGE:
    			posizioni = new float[] {0.0f,0.82f,0.83f,0.86f,0.87f,1.0f};
    			colori = new int[] {Color.argb(0, 0, 0, 0),Color.argb(76,0,0,0),Color.argb(95,0,0,0),Color.argb(153,219,219,219),Color.argb(151,255,255,255),Color.argb(102,0,0,0)};
    			gradienteR = new RadialGradient(centerX, centerY, frameRadius * (float)Math.sqrt(2.0d), colori, posizioni, Shader.TileMode.REPEAT);
    			pennello.setShader(gradienteR);
    			pathExtFrame.setFillType(FillType.WINDING);
    			tela.clipPath(pathExtFrame, Op.REPLACE);
    			pathIntFrame.setFillType(FillType.WINDING);
    			tela.clipPath(pathIntFrame, Op.DIFFERENCE);
    			tela.drawPaint(pennello);
    			break;

    		case CONE:
    			posizioni = new float[] {0.0f,0.82f,0.8201f,0.96f,0.9601f,1.0f};
    			colori = new int[] {Color.argb(0, 0, 0, 0),Color.argb(50, 0, 0, 0),Color.argb(51,9, 9, 9),Color.argb(124,255, 255, 255),Color.argb(127,223, 223, 223),Color.argb(76, 0, 0, 0)};
    			gradienteR = new RadialGradient(centerX, centerY, frameRadius * (float)Math.sqrt(2.0d), colori, posizioni, Shader.TileMode.REPEAT);
    			pennello.setShader(gradienteR);
    			pathExtFrame.setFillType(FillType.WINDING);
    			tela.clipPath(pathExtFrame, Op.REPLACE);
    			pathIntFrame.setFillType(FillType.WINDING);
    			tela.clipPath(pathIntFrame, Op.DIFFERENCE);
    			tela.drawPaint(pennello);
    			break;

    		case TORUS:
    			posizioni = new float[] {0.0f,0.82f,0.8201f,0.92f,1.0f};
    			colori = new int[] {Color.argb(0, 0, 0, 0),Color.argb(50, 0, 0, 0),Color.argb(51,13,13,13),Color.argb(64,255, 255, 255),Color.argb(76, 0, 0, 0)};
    			gradienteR = new RadialGradient(centerX, centerY, frameRadius * (float)Math.sqrt(2.0d) , colori, posizioni, Shader.TileMode.REPEAT);
    			pennello.setShader(gradienteR);
    			pathExtFrame.setFillType(FillType.WINDING);
    			tela.clipPath(pathExtFrame, Op.REPLACE);
    			pathIntFrame.setFillType(FillType.WINDING);
    			tela.clipPath(pathIntFrame, Op.DIFFERENCE);
    			tela.drawPaint(pennello);
    			break;

    		case INNER_FRAME:
    			posizioni = new float[] {0.0f,0.8f,0.85f,0.90f,1.0f};
    			colori = new int[] {Color.argb(183,0, 0, 0),Color.argb(25,148, 148, 148),Color.argb(159,0, 0, 0),Color.argb(81,0, 0, 0),Color.argb(158,255, 255, 255)};
    			gradienteL = new LinearGradient(0, altezza * 0.06f, 0, altezza * 0.88f,colori,posizioni,Shader.TileMode.MIRROR);
    			pennello.setShader(gradienteL);
    			
    			pathStepFrame.setFillType(FillType.WINDING);
    			tela.clipPath(pathStepFrame, Op.REPLACE);
    			pathIntFrame.setFillType(FillType.WINDING);
    			tela.clipPath(pathIntFrame, Op.DIFFERENCE);
    			tela.drawPaint(pennello);
    			break;
    	}
    	
		cambiaView(tela,-1);
    	return;
    }
	
		
}
