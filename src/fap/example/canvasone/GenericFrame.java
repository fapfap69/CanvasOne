package fap.example.canvasone;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path.FillType;
import android.graphics.PathEffect;
import android.graphics.Region.Op;
import android.graphics.Shader;
import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.SweepGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.Environment;
import android.util.Log;

public class GenericFrame {

	// Costanti interne
	private static final int LINEARE = 1;
	private static final int RADIALE = 2;
	// ---------------------------------------
	
	// Aspetto
	private Materiale materiale;
	private Finitura finitura;
	protected int opacity = Dash.OPACITY;
	
	// Contesto
	private Canvas tela;
	private static Bitmap frameImage;
	protected static Paint pennello;
	protected static Paint matita;
	
	// Proprietà
	// Dimensione della Bitmap
	protected int larghezza = 100;
	protected int altezza = 100;
	protected float centerX;
	protected float centerY;
	protected float frameRadius;
//	protected float displayRadius;

	protected RectF recBoundary;  
	protected RectF recOuterFrame;
	protected RectF recInnerFrame;
	protected RectF recOuterShadow;
	protected RectF recInnerStep;
	protected RectF recInnerShadow;
	
	protected Path pathExtFrame = new Path();
	protected Path pathIntFrame = new Path();
	protected Path pathStepFrame = new Path();
	protected Path pathShadowFrame = new Path();
	protected Path pathInShadowFrame = new Path();
	
	boolean isInvalid = false;

	// Costruttore della classe
	public GenericFrame(){
		createFrame(larghezza, altezza, materiale, finitura);
		return ;
	}
	public GenericFrame(final int WIDTH,final int HEIGHT){
		createFrame(WIDTH, HEIGHT, materiale, finitura);
		return ;
	}
	public GenericFrame(final int WIDTH,final int HEIGHT, final Materiale TEXTURE, final Finitura EFFECT){
		createFrame(WIDTH, HEIGHT, TEXTURE, EFFECT);
		return ;
	}
	
	private void createFrame(final int WIDTH,final int HEIGHT, final Materiale TEXTURE, final Finitura EFFECT) {
		
        // Cache current parameters
    	larghezza = WIDTH;
    	altezza = HEIGHT;
    	materiale = TEXTURE;
    	finitura = EFFECT;
    	
        // Crea i pennelli
		PathEffect effect = new PathEffect();
		Xfermode xfermode = new Xfermode();
		float miter = 0.1f;

        pennello = new Paint();
        pennello.setAntiAlias(true);
		pennello.setDither(true);
		pennello.setPathEffect(effect);
		pennello.setStrokeCap(Cap.SQUARE);
		pennello.setStrokeJoin(Join.ROUND);
		pennello.setStrokeMiter(miter);
		pennello.setStyle(Style.FILL_AND_STROKE);
		pennello.setXfermode(xfermode);

        matita = new Paint();
        matita.setAntiAlias(true);
        matita.setDither(true);
        matita.setPathEffect(effect);
        matita.setStrokeCap(Cap.SQUARE);
        matita.setStrokeJoin(Join.ROUND);
        matita.setStrokeMiter(miter);
        matita.setStyle(Style.STROKE);
        matita.setXfermode(xfermode);

        this.reformatFrame();
        
        return;
        
	}

	
	// Metodi 
	
	// Tipo
	public Tipo getType() {
		return Tipo.NONE;
	}
	
	// Dimensioni
	public void getDimensions(int WIDTH, int HEIGHT) {
		WIDTH = larghezza;
		HEIGHT = altezza;
		return;
	}
	public void setDimension(final int WIDTH, final int HEIGHT) {
		larghezza = (WIDTH <= 0) ? 1 : WIDTH;
		altezza = (HEIGHT <= 0) ? 1 : HEIGHT;
		this.reformatFrame();
		return;
	}

	// Materiale
	public Materiale getMaterial() {
		return materiale;
	}
	public void setMaterial(Materiale MATERIAL) {
		if( materiale != MATERIAL) { 
			materiale = MATERIAL;
			isInvalid = true;
		}
		return;
	}
	
	// Finitura
	public Finitura getEffect() {
		return finitura;
	}
	public void setEffect(Finitura EFFECT) {
		if( finitura != EFFECT) {
			finitura = EFFECT;
			isInvalid = true;	
		}
		return;
	}
	
	// Metodo per il disegno della Bitmap
	public Bitmap drawFrame(boolean FORCEREDRAW){
		
		if(!isInvalid && !FORCEREDRAW) {
            return frameImage;
		} else {
			isInvalid = false;
		}
        // -----------------------------

        // Altrimenti bisogna fare il repaint
        
        // Crea la nuova bitmap
        frameImage = Bitmap.createBitmap(larghezza, altezza, Config.ARGB_8888);
        // ed il contesto su cui disegnare
        Canvas tela = new Canvas(frameImage);
        // ed imposta i pennelli sull'attuale
        pennello.setAlpha(opacity);
        matita.setAlpha(opacity);
        matita.setARGB(opacity, Dash.OUTLINEGRAY, Dash.OUTLINEGRAY, Dash.OUTLINEGRAY);
        
        // Costruisce il Path per la texture dei materiali
        this.buildTheShader(materiale);
        
        // Disegnare il frame ...
        drawTheFrameBody(tela);
        drawTheEffect(finitura, tela);
        drawTheShadow(tela);
        drawTheBorder(tela);

        // fine !
        return frameImage;
    }
        
        
	// Costruisce lo shader per il pennello in funzione del materiale
    protected int buildTheShader(Materiale MATERIAL) {

    	float[] posizioni;
        int[] colori;
        LinearGradient GRADLIN;
        RadialGradient GRADRAD;

        switch (MATERIAL) {
    	case BLACK_METAL:
    		posizioni = new float[] {0.0f,45.0f,125.0f,180.0f,245.0f,315.0f,360.0f};
    		colori = new int[] {Color.argb(opacity,254,254,254),Color.argb(opacity,0,0,0),Color.argb(opacity,153,153,153),Color.argb(opacity,0,0,0),Color.argb(opacity,153,153,153),Color.argb(opacity,0,0,0),Color.argb(opacity,254,254,254)};
    		GRADRAD = new RadialGradient(centerX, centerY, frameRadius, colori, posizioni, Shader.TileMode.REPEAT);
    		pennello.setShader(GRADRAD);
    		return(RADIALE);

        case METAL:
        	posizioni = new float[] {0.0f,0.07f,0.12f,1.0f};
        	colori = new int[] {Color.argb(opacity,254,254,254),Color.argb(opacity,210, 210, 210),Color.argb(opacity,179, 179, 179),Color.argb(opacity,213, 213, 213)};
        	GRADLIN = new LinearGradient(0, 0,larghezza, altezza, colori, posizioni, Shader.TileMode.REPEAT);
        	pennello.setShader(GRADLIN);
    		return(LINEARE);

        case SHINY_METAL:
            posizioni = new float[] {0.0f,45.0f,90.0f,125.0f,180.0f,235.0f,270.0f,315.0f,360.0f};
            colori = new int[] {Color.argb(opacity,254, 254, 254),Color.argb(opacity,210, 210, 210),Color.argb(opacity,179,179,179),Color.argb(opacity,238,238,238),Color.argb(opacity,160,160,160),Color.argb(opacity,238,238,238),Color.argb(opacity,179,179,179),Color.argb(opacity,210,210,210),Color.argb(opacity,254,254,254)};
    		GRADRAD = new RadialGradient(centerX, centerY, frameRadius, colori, posizioni, Shader.TileMode.REPEAT);
    		pennello.setShader(GRADRAD);
    		return(RADIALE);

        case GLOSSY_METAL:
    		posizioni = new float[] {0.0f, 0.96f, 1.0f};
    		colori = new int[] {Color.argb(opacity,207,207,207),Color.argb(opacity,204,203,204),Color.argb(opacity,244,244,244)};
    		GRADRAD = new RadialGradient(centerX, centerY, frameRadius, colori, posizioni, Shader.TileMode.REPEAT);
    		pennello.setShader(GRADRAD);
    		return(RADIALE);

            // area 1,5% - 97% --> Lineare Verticale {0.0f, 0.23f, 0.36f, 0.59f, 0.76f, 1.0f} {(.97,.97.,97),.78,.76.74),(1,1,1),(.11,.11,.11),(.78,.76,.75),(.81,.81,.81)}
            // area 6% - 87% --> Fill 0xF6F6F6
            // area precedente piu' stretta di 2px,2px,-4px,-4px ---> fill 0x333333

        case BRASS:
            posizioni = new float[] {0.0f,0.05f,0.10f,0.50f,0.90f,0.95f,1.0f};
            colori = new int[] {Color.argb(opacity,249,243,155),Color.argb(opacity,246,226,101),Color.argb(opacity,240,225,132),Color.argb(opacity,90,57,22),Color.argb(opacity,249,237,139),Color.argb(opacity,243,226,108),Color.argb(opacity,202,182,113)};
            GRADLIN = new LinearGradient(0,0, larghezza, altezza,colori,posizioni,Shader.TileMode.REPEAT);
            pennello.setShader(GRADLIN);
    		return(LINEARE);

        case STEEL:
            posizioni = new float[] {0.0f,0.05f,0.10f,0.50f,0.90f,0.95f,1.0f};
            colori = new int[] {Color.argb(opacity,231, 237, 237),Color.argb(opacity,189, 199, 198),Color.argb(opacity,192, 201, 200),Color.argb(opacity,23, 31, 33),Color.argb(opacity,196, 205, 204),Color.argb(opacity,194, 204, 203),Color.argb(opacity,189, 201, 199)};
            GRADLIN = new LinearGradient(0,0, larghezza, altezza, colori, posizioni,Shader.TileMode.REPEAT);
            pennello.setShader(GRADLIN);
    		return(LINEARE);

        case CHROME:
        	posizioni = new float[] {0.0f,0.09f,0.12f,0.16f,0.25f,0.29f,0.33f,0.38f,0.48f,0.52f,0.63f,0.68f,0.8f,0.83f,0.87f,0.97f,1.0f};
        	colori = new int[] {Color.argb(opacity,255, 255, 255),Color.argb(opacity,255, 255, 255),Color.argb(opacity,136, 136, 138),Color.argb(opacity,164, 185, 190),Color.argb(opacity,158, 179, 182),Color.argb(opacity,112, 112, 112),Color.argb(opacity,221, 227, 227),Color.argb(opacity,155, 176, 179),Color.argb(opacity,156, 176, 177),Color.argb(opacity,254, 255, 255),Color.argb(opacity,255, 255, 255),Color.argb(opacity,156, 180, 180),Color.argb(opacity,198, 209, 211),Color.argb(opacity,246, 248, 247),Color.argb(opacity,204, 216, 216),Color.argb(opacity,164, 188, 190),Color.argb(opacity,255, 255, 255)};
    		GRADRAD = new RadialGradient(centerX, centerY, frameRadius, colori, posizioni, Shader.TileMode.REPEAT);
    		pennello.setShader(GRADRAD);
    		return(RADIALE);

        case GOLD:
        	posizioni = new float[] {0.0f,0.15f,0.22f,0.3f,0.38f,0.44f,0.51f,0.6f,0.68f,0.75f,1.0f};
        	colori = new int[] {Color.argb(opacity,255,255,207), Color.argb(opacity,255, 237, 96), Color.argb(opacity,254,199,57), Color.argb(opacity,255, 249, 203), Color.argb(opacity,255,199,64), Color.argb(opacity,252,194,60), Color.argb(opacity,255, 204, 59), Color.argb(opacity,213, 134, 29), Color.argb(opacity,255, 201, 56), Color.argb(opacity,212, 135, 29), Color.argb(opacity,247, 238, 101) };
            GRADLIN = new LinearGradient(0,0, larghezza, altezza, colori, posizioni,Shader.TileMode.REPEAT);
            pennello.setShader(GRADLIN);
    		return(LINEARE);
                
        case ANTHRACITE:
        	posizioni = new float[] {0.0f,0.06f,0.12f,1.0f};
        	colori = new int[] {Color.argb(opacity,118, 117, 135),Color.argb(opacity,74, 74, 82),Color.argb(opacity,50, 50, 54),Color.argb(opacity,97, 97, 108)};
            GRADLIN = new LinearGradient(0,0, larghezza, altezza, colori, posizioni,Shader.TileMode.REPEAT);
            pennello.setShader(GRADLIN);
    		return(LINEARE);

        case TILTED_GRAY:
        	posizioni = new float[] {0.0f,0.07f,0.16f,0.33f,0.55f,0.79f,1.0f};
        	colori = new int[] {Color.argb(opacity,255, 255, 255),Color.argb(opacity,210, 210, 210),Color.argb(opacity,179, 179, 179),Color.argb(opacity,255, 255, 255),Color.argb(opacity,197, 197, 197),Color.argb(opacity,255, 255, 255),Color.argb(opacity,102, 102, 102)};
            GRADLIN = new LinearGradient(0.233f * larghezza, 0.084f * altezza, 0.813f * larghezza, 0.911f * altezza, colori, posizioni,Shader.TileMode.REPEAT);
            pennello.setShader(GRADLIN);
    		return(LINEARE);
        	
        case TILTED_BLACK:
        	posizioni = new float[] {0.0f,0.21f,0.47f,0.99f,1.0f};
        	colori = new int[] {Color.argb(opacity,102, 102, 102),Color.argb(opacity,0, 0, 0),Color.argb(opacity,102, 102, 102),Color.argb(opacity,0, 0, 0),Color.argb(opacity,0, 0,0)};
            GRADLIN = new LinearGradient(0.229f * larghezza, 0.079f * altezza, 0.803f * larghezza, 0.899f * altezza, colori, posizioni,Shader.TileMode.REPEAT);
            pennello.setShader(GRADLIN);
    		return(LINEARE);

        default:
        	posizioni = new float[] {0.0f,0.07f,0.12f,1.0f};
        	colori = new int[] {Color.argb(opacity,254, 254, 254),Color.argb(opacity,210, 210, 210),Color.argb(opacity,179, 179, 179),Color.argb(opacity,213, 213, 213)};
            GRADLIN = new LinearGradient(0,0, larghezza, altezza, colori, posizioni,Shader.TileMode.REPEAT);
            pennello.setShader(GRADLIN);
    		return(LINEARE);

        }   	
    	
    }
    // -----------------------------------------
    // Drawing methods ....
    public void drawTheFrameBody(Canvas tela) {
        pathExtFrame.setFillType(FillType.WINDING);
        tela.clipPath(pathExtFrame, Op.REPLACE);
        pathIntFrame.setFillType(FillType.WINDING);
        tela.clipPath(pathIntFrame, Op.DIFFERENCE);
        tela.drawPaint(pennello);
        return;
    }

    public void  drawTheShadow(Canvas tela) {
    	pennello.setShader(null);
    	pennello.setARGB(Dash.SHADOWOPACITY, Dash.SHADOWGRAY, Dash.SHADOWGRAY, Dash.SHADOWGRAY);
    	pathShadowFrame.setFillType(FillType.WINDING);
    	tela.clipPath(pathShadowFrame, Op.REPLACE);
    	tela.drawPaint(pennello);
    	return;
    }

    public void  drawTheDisplayShadow(Canvas tela) {
    	pennello.setShader(null);
    	pennello.setARGB(Dash.SHADOWOPACITY, Dash.SHADOWGRAY, Dash.SHADOWGRAY, Dash.SHADOWGRAY);
    	pathShadowFrame.setFillType(FillType.WINDING);
    	tela.clipPath(pathInShadowFrame, Op.REPLACE);
    	tela.drawPaint(pennello);
    	return;
    }
    
    public void drawTheBorder(Canvas tela) {
    	tela.clipPath(pathExtFrame, Op.REPLACE );
    	tela.drawPath(pathExtFrame,  matita);
    	tela.drawPath(pathIntFrame,  matita);
    	return;
    }
    
    public void drawTheEffect(Finitura EFFECT, Canvas tela) {

    	float[] posizioni;
    	int[] colori;
    	RadialGradient gradienteR = null; // = new RadialGradient(1,1,1,0,0,null);
    	LinearGradient gradienteL = null; // = new LinearGradient(1,1,2,2,0,0, null);

    	if (EFFECT == Finitura.INNER_FRAME) {
			posizioni = new float[] {0.0f,0.8f,0.85f,0.90f,1.0f};
			colori = new int[] {Color.argb(183,0, 0, 0),Color.argb(25,148, 148, 148),Color.argb(159,0, 0, 0),Color.argb(81,0, 0, 0),Color.argb(158,255, 255, 255)};
			gradienteL = new LinearGradient(0, altezza * 0.06f, 0, altezza * 0.88f,colori,posizioni,Shader.TileMode.MIRROR);
			pennello.setShader(gradienteL);
			
			pathStepFrame.setFillType(FillType.WINDING);
			tela.clipPath(pathStepFrame, Op.REPLACE);
			pathIntFrame.setFillType(FillType.WINDING);
			tela.clipPath(pathIntFrame, Op.DIFFERENCE);
			tela.drawPaint(pennello);

    	} else {
        	switch (EFFECT) {
        		case NONE:
        		default:
        			break;

        		case BULGE:
        			posizioni = new float[] {0.0f,0.82f,0.83f,0.86f,0.87f,1.0f};
    				colori = new int[] {Color.argb(0, 0, 0, 0),Color.argb(76,0,0,0),Color.argb(95,0,0,0),Color.argb(153,219,219,219),Color.argb(151,255,255,255),Color.argb(102,0,0,0)};
    				gradienteR = new RadialGradient(centerX, centerY, frameRadius, colori, posizioni, Shader.TileMode.REPEAT);
    				break;

        		case CONE:
        			posizioni = new float[] {0.0f,0.82f,0.8201f,0.96f,0.9601f,1.0f};
        			colori = new int[] {Color.argb(0, 0, 0, 0),Color.argb(50, 0, 0, 0),Color.argb(51,9, 9, 9),Color.argb(124,255, 255, 255),Color.argb(127,223, 223, 223),Color.argb(76, 0, 0, 0)};
        			gradienteR = new RadialGradient(centerX, centerY, frameRadius, colori, posizioni, Shader.TileMode.REPEAT);
        			break;
        			
        		case TORUS:
        			posizioni = new float[] {0.0f,0.82f,0.8201f,0.92f,1.0f};
        			colori = new int[] {Color.argb(0, 0, 0, 0),Color.argb(50, 0, 0, 0),Color.argb(51,13,13,13),Color.argb(64,255, 255, 255),Color.argb(76, 0, 0, 0)};
        			gradienteR = new RadialGradient(centerX, centerY, frameRadius, colori, posizioni, Shader.TileMode.REPEAT);
        			break;

        	}
			pennello.setShader(gradienteR);
			pathExtFrame.setFillType(FillType.WINDING);
			tela.clipPath(pathExtFrame, Op.REPLACE);
			pathIntFrame.setFillType(FillType.WINDING);
			tela.clipPath(pathIntFrame, Op.DIFFERENCE);
			tela.drawPaint(pennello);
    	}
    	return;
    }
    // -------------------------------------------------------------------------------

    // Recalculate the Frame geometry
    //
    public void reformatFrame() {

    	float inShad = larghezza * Dash.THICK_PER;
    	float outShad = larghezza * Dash.SHADOW_PER;
    	float bordo = larghezza * Dash.BORDER_PER;
    	float step = larghezza * (Dash.BORDER_PER * Dash.INNERSTEP_PER);
    	
    	// Calcola i rettangoli che definiscono le aree
    	// Superfice totale
    	recBoundary = new RectF(0,0,larghezza-1,altezza-1);  
    	// Superficie occupata dallo strumento senza l'ombra
    	recOuterFrame = new RectF(0.0f,0.0f, larghezza - outShad , altezza - outShad);
    	// Superficie del bordo interno 
    	recInnerFrame = new RectF( bordo , bordo , recOuterFrame.right - bordo , recOuterFrame.bottom - bordo );
    	// Superficie occupata dall'ombra Esterna ed interna
    	recOuterShadow = new RectF( outShad , outShad ,larghezza , altezza);
    	recInnerShadow = new RectF( recInnerFrame.left + inShad , recInnerFrame.top + inShad ,recInnerFrame.right , recInnerFrame.bottom);
    	// Superficie occupata dall'gradino del frame
    	recInnerStep = new RectF( recInnerFrame.left - step , recInnerFrame.top - step, recInnerFrame.right + step , recInnerFrame.bottom + step );
    
    	// centro dello strumento 
    	centerX = (recInnerFrame.width() / 2.0f) + recInnerFrame.left;
    	centerY = (recInnerFrame.height() / 2.0f) + recInnerFrame.top;
    	// raggio dello strumento 
    	frameRadius = (recOuterFrame.width() / 2.0f);
//    	displayRadius = (recInnerFrame.width() / 2.0f);
    	
    	// calcola i Paths del Frame
		pathIntFrame.reset();
		pathIntFrame.addOval(recInnerFrame, Path.Direction.CW);
		pathIntFrame.close();
		
		pathExtFrame.reset();
		pathExtFrame.addOval(recOuterFrame, Path.Direction.CW);
		pathExtFrame.close();
		
		pathStepFrame.reset();
		pathStepFrame.addOval(recInnerStep, Path.Direction.CW);
		pathStepFrame.close();
		
		pathShadowFrame.reset();
		pathShadowFrame.addOval(recOuterShadow, Path.Direction.CW);
		pathShadowFrame.addOval(recOuterFrame, Path.Direction.CCW);
		pathShadowFrame.close();
		
		pathInShadowFrame.reset();
		pathInShadowFrame.addOval(recInnerFrame, Path.Direction.CW);
		pathInShadowFrame.addOval(recInnerShadow, Path.Direction.CCW);
		pathInShadowFrame.close();

		isInvalid = true;
		
		return;
    }
    
}