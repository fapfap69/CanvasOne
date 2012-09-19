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


public class Frame {

	// Opacità dello strumento
	private static final int OPACITY = 254;

	// Dimensione percentuale dell'ombra
	private static final float SHADOW_PER = 0.02f;
	private static final float UNSHADOW_PER = 0.98f;
	
	private static final float ROUNDCORNER_PER = 0.04f;
//	private static final float BACKGROUND_PER = 0.86f;
	private static final float BORDER_PER = 0.1f;

	
	private Tipo cirType;
	private int cirWidth;
	private int cirHeight;
	private Materiale cirDesign;
	private Finitura cirEffect;
	private Bitmap cirFrameImage;
	private Boolean cirScrew;
	
	private Canvas tela;
	
    /**
     * Creates the frame image for a radial gauge.
     * The image parameters and the image will be cached. If the
     * current request has the same parameters as the last request
     * it will return the already created image instead of creating
     * a new image.
     * If an image is passed to the method, it will paint to the image and
     * return this image. This will reduce the memory consumption.
     * @param WIDTH
     * @param FRAME_DESIGN
     * @param CUSTOM_FRAME_DESIGN
     * @param FRAME_BASECOLOR
     * @param FRAME_BASECOLOR_ENABLED
     * @param FRAME_EFFECT
     * @param BACKGROUND_IMAGE
     * @return a buffered image that contains the frame image for a radial gauge
     */
	
	public Frame() {
        // Cache current parameters
    	cirWidth = 1;
    	cirHeight = 1;
        cirType = Tipo.CIRCULAR;
    	cirDesign = Materiale.BLACK_METAL;
    	cirEffect = Finitura.NONE;
	}

	public Tipo getType() {
		return cirType;
	}
	public void setType(Tipo TYPE) {
		cirType = TYPE;
		return;
	}
	public Materiale getDesign() {
		return cirDesign;
	}
	public void setDesign(Materiale DESIGN) {
		cirDesign = DESIGN;
		return;
	}
	public Finitura getEffect() {
		return cirEffect;
	}
	public void setEffect(Finitura EFFECT) {
		cirEffect = EFFECT;
		return;
	}
	

	public Bitmap createFrame(final int WIDTH,final int HEIGHT) {
		return createFrame(WIDTH, HEIGHT, cirType, cirDesign, cirEffect, cirScrew);
	}
	
	
	public Bitmap createFrame(final int WIDTH,final int HEIGHT,
    		final Tipo TYPE,
    		final Materiale DESIGN, 
    		final Finitura EFFECT, 
    		final Boolean SCREW) {
        if (WIDTH <= 0) {
            return Bitmap.createBitmap(1, 1, Config.ALPHA_8);
        }

        // Take image from cache 
        if (cirType == TYPE && cirWidth == WIDTH && cirHeight == HEIGHT && cirDesign == DESIGN && cirEffect == EFFECT) {
            return cirFrameImage;
        }

        // Crea la nuova bitmap
        cirFrameImage = Bitmap.createBitmap(WIDTH, HEIGHT, Config.ARGB_8888);
        
        // il contesto su cui disegnare
        Canvas tela = new Canvas(cirFrameImage);

        
   
        // Crea i pennelli
		PathEffect effect = new PathEffect();
		Xfermode xfermode = new Xfermode();
		float miter = 0.3f;

        Paint pennello = new Paint();
        pennello.setAntiAlias(true);
		pennello.setAlpha(OPACITY);
		pennello.setAntiAlias(true);
		pennello.setDither(true);
		pennello.setPathEffect(effect);
		pennello.setStrokeCap(Cap.SQUARE);
		pennello.setStrokeJoin(Join.ROUND);
		pennello.setStrokeMiter(miter);
		pennello.setStyle(Style.FILL_AND_STROKE);
		pennello.setXfermode(xfermode);

		Paint penEffetto = new Paint();
        penEffetto.setAntiAlias(true);
        penEffetto.setAlpha(OPACITY);
        penEffetto.setAntiAlias(true);
        penEffetto.setDither(true);
        penEffetto.setPathEffect(effect);
        penEffetto.setStrokeCap(Cap.SQUARE);
        penEffetto.setStrokeJoin(Join.ROUND);
        penEffetto.setStrokeMiter(miter);
        penEffetto.setStyle(Style.FILL_AND_STROKE);
        penEffetto.setXfermode(xfermode);

        Paint penShadow = new Paint();
        penShadow.setAntiAlias(true);
        penShadow.setAlpha(64);  // the shadow
        penShadow.setAntiAlias(true);
        penShadow.setDither(true);
        penShadow.setPathEffect(effect);
        penShadow.setStrokeCap(Cap.SQUARE);
        penShadow.setStrokeJoin(Join.ROUND);
        penShadow.setStrokeMiter(miter);
        penShadow.setStyle(Style.FILL_AND_STROKE);
        penShadow.setXfermode(xfermode);
        penShadow.setARGB(64, 102, 102, 102);
        
        Paint matita = new Paint();
        matita.setAntiAlias(true);
        matita.setAlpha(OPACITY);
        matita.setAntiAlias(true);
        matita.setDither(true);
        matita.setPathEffect(effect);
        matita.setStrokeCap(Cap.SQUARE);
        matita.setStrokeJoin(Join.ROUND);
        matita.setStrokeMiter(0.1f);
        matita.setStyle(Style.STROKE);
        matita.setXfermode(xfermode);
        matita.setARGB(255, 48, 48, 48);
        
        // Calcolo delle dimensioni
        RectF recBoundary = new RectF(0,0,WIDTH-1,HEIGHT-1);
        RectF recOuterFrame = new RectF(0,0,(int)(WIDTH * UNSHADOW_PER ) ,(int)(HEIGHT * UNSHADOW_PER));
        RectF recInnerFrame = new RectF((int)(recOuterFrame.right * BORDER_PER),(int)(recOuterFrame.bottom * BORDER_PER),(int)(recOuterFrame.right * (1-BORDER_PER) ) ,(int)(recOuterFrame.bottom * (1-BORDER_PER)));
        RectF recOuterShadow = new RectF((int)(WIDTH * SHADOW_PER),(int)(HEIGHT * SHADOW_PER),WIDTH, HEIGHT);

        final float CENTER_X = (recInnerFrame.width() / 2.0f) + recInnerFrame.left;
        final float CENTER_Y = (recInnerFrame.height() / 2.0f) + recInnerFrame.top;
        final float RADIUS = (recOuterFrame.width() / 2.0f);
        

        float[] posizioni;
        int[] colori;
        RadialGradient gradienteR;
        LinearGradient gradienteL;

        // frame
        Path framePath = new Path();
        Path effectPath = new Path();
        
        
        switch (DESIGN) {
        	case BLACK_METAL:
        		posizioni = new float[] {0.0f,45.0f,125.0f,180.0f,245.0f,315.0f,360.0f};
        		colori = new int[] {Color.argb(OPACITY,254,254,254),Color.argb(OPACITY,0,0,0),Color.argb(OPACITY,153,153,153),Color.argb(OPACITY,0,0,0),Color.argb(OPACITY,153,153,153),Color.argb(OPACITY,0,0,0),Color.argb(OPACITY,254,254,254)};
        		gradienteR = new RadialGradient(CENTER_X, CENTER_Y, RADIUS, colori, posizioni, Shader.TileMode.REPEAT);
        		pennello.setShader(gradienteR);
                break;

            case METAL:
            	posizioni = new float[] {0.0f,0.07f,0.12f,1.0f};
            	colori = new int[] {Color.argb(OPACITY,254,254,254),Color.argb(OPACITY,210, 210, 210),Color.argb(OPACITY,179, 179, 179),Color.argb(OPACITY,213, 213, 213)};
            	gradienteL = new LinearGradient(0,0,WIDTH,HEIGHT,colori,posizioni,Shader.TileMode.REPEAT);
        		pennello.setShader(gradienteL);
                break;

            case SHINY_METAL:
                posizioni = new float[] {0.0f,45.0f,90.0f,125.0f,180.0f,235.0f,270.0f,315.0f,360.0f};
                colori = new int[] {Color.argb(OPACITY,254, 254, 254),Color.argb(OPACITY,210, 210, 210),Color.argb(OPACITY,179,179,179),Color.argb(OPACITY,238,238,238),Color.argb(OPACITY,160,160,160),Color.argb(OPACITY,238,238,238),Color.argb(OPACITY,179,179,179),Color.argb(OPACITY,210,210,210),Color.argb(OPACITY,254,254,254)};
        		gradienteR = new RadialGradient(CENTER_X, CENTER_Y, RADIUS, colori, posizioni, Shader.TileMode.REPEAT);
        		pennello.setShader(gradienteR);
                break;

            case GLOSSY_METAL:
        		posizioni = new float[] {0.0f, 0.96f, 1.0f};
        		colori = new int[] {Color.argb(OPACITY,207,207,207),Color.argb(OPACITY,204,203,204),Color.argb(OPACITY,244,244,244)};
        		gradienteR = new RadialGradient(CENTER_X, CENTER_Y, RADIUS, colori, posizioni, Shader.TileMode.REPEAT);
        		pennello.setShader(gradienteR);
                break;

                // area 1,5% - 97% --> Lineare Verticale {0.0f, 0.23f, 0.36f, 0.59f, 0.76f, 1.0f} {(.97,.97.,97),.78,.76.74),(1,1,1),(.11,.11,.11),(.78,.76,.75),(.81,.81,.81)}
                // area 6% - 87% --> Fill 0xF6F6F6
                // area precedente piu' stretta di 2px,2px,-4px,-4px ---> fill 0x333333

            case BRASS:
                posizioni = new float[] {0.0f,0.05f,0.10f,0.50f,0.90f,0.95f,1.0f};
                colori = new int[] {Color.argb(OPACITY,249,243,155),Color.argb(OPACITY,246,226,101),Color.argb(OPACITY,240,225,132),Color.argb(OPACITY,90,57,22),Color.argb(OPACITY,249,237,139),Color.argb(OPACITY,243,226,108),Color.argb(OPACITY,202,182,113)};
            	gradienteL = new LinearGradient(0,0,WIDTH,HEIGHT,colori,posizioni,Shader.TileMode.REPEAT);
        		pennello.setShader(gradienteL);
                break;

            case STEEL:
                posizioni = new float[] {0.0f,0.05f,0.10f,0.50f,0.90f,0.95f,1.0f};
                colori = new int[] {Color.argb(OPACITY,231, 237, 237),Color.argb(OPACITY,189, 199, 198),Color.argb(OPACITY,192, 201, 200),Color.argb(OPACITY,23, 31, 33),Color.argb(OPACITY,196, 205, 204),Color.argb(OPACITY,194, 204, 203),Color.argb(OPACITY,189, 201, 199)};
            	gradienteL = new LinearGradient(0,0,WIDTH,HEIGHT,colori,posizioni,Shader.TileMode.REPEAT);
        		pennello.setShader(gradienteL);
                break;

            case CHROME:
            	posizioni = new float[] {0.0f,0.09f,0.12f,0.16f,0.25f,0.29f,0.33f,0.38f,0.48f,0.52f,0.63f,0.68f,0.8f,0.83f,0.87f,0.97f,1.0f};
            	colori = new int[] {Color.argb(OPACITY,255, 255, 255),Color.argb(OPACITY,255, 255, 255),Color.argb(OPACITY,136, 136, 138),Color.argb(OPACITY,164, 185, 190),Color.argb(OPACITY,158, 179, 182),Color.argb(OPACITY,112, 112, 112),Color.argb(OPACITY,221, 227, 227),Color.argb(OPACITY,155, 176, 179),Color.argb(OPACITY,156, 176, 177),Color.argb(OPACITY,254, 255, 255),Color.argb(OPACITY,255, 255, 255),Color.argb(OPACITY,156, 180, 180),Color.argb(OPACITY,198, 209, 211),Color.argb(OPACITY,246, 248, 247),Color.argb(OPACITY,204, 216, 216),Color.argb(OPACITY,164, 188, 190),Color.argb(OPACITY,255, 255, 255)};
        		gradienteR = new RadialGradient(CENTER_X, CENTER_Y, RADIUS, colori, posizioni, Shader.TileMode.REPEAT);
        		pennello.setShader(gradienteR);
                break;

            case GOLD:
            	posizioni = new float[] {0.0f,0.15f,0.22f,0.3f,0.38f,0.44f,0.51f,0.6f,0.68f,0.75f,1.0f};
            	colori = new int[] {Color.argb(OPACITY,255,255,207), Color.argb(OPACITY,255, 237, 96), Color.argb(OPACITY,254,199,57), Color.argb(OPACITY,255, 249, 203), Color.argb(OPACITY,255,199,64), Color.argb(OPACITY,252,194,60), Color.argb(OPACITY,255, 204, 59), Color.argb(OPACITY,213, 134, 29), Color.argb(OPACITY,255, 201, 56), Color.argb(OPACITY,212, 135, 29), Color.argb(OPACITY,247, 238, 101) };
            	gradienteL = new LinearGradient(0,0,WIDTH,HEIGHT,colori,posizioni,Shader.TileMode.REPEAT);
        		pennello.setShader(gradienteL);
                break;
                    
            case ANTHRACITE:
            	posizioni = new float[] {0.0f,0.06f,0.12f,1.0f};
            	colori = new int[] {Color.argb(OPACITY,118, 117, 135),Color.argb(OPACITY,74, 74, 82),Color.argb(OPACITY,50, 50, 54),Color.argb(OPACITY,97, 97, 108)};
            	gradienteL = new LinearGradient(0,0,WIDTH,HEIGHT,colori,posizioni,Shader.TileMode.REPEAT);
        		pennello.setShader(gradienteL);
                break;

            case TILTED_GRAY:
            	posizioni = new float[] {0.0f,0.07f,0.16f,0.33f,0.55f,0.79f,1.0f};
            	colori = new int[] {Color.argb(OPACITY,255, 255, 255),Color.argb(OPACITY,210, 210, 210),Color.argb(OPACITY,179, 179, 179),Color.argb(OPACITY,255, 255, 255),Color.argb(OPACITY,197, 197, 197),Color.argb(OPACITY,255, 255, 255),Color.argb(OPACITY,102, 102, 102)};
            	gradienteL = new LinearGradient(0.233f * WIDTH ,0.084f * HEIGHT, 0.813f * WIDTH, 0.911f * HEIGHT,colori,posizioni,Shader.TileMode.REPEAT);
        		pennello.setShader(gradienteL);
                break;
            	
            case TILTED_BLACK:
            	posizioni = new float[] {0.0f,0.21f,0.47f,0.99f,1.0f};
            	colori = new int[] {Color.argb(OPACITY,102, 102, 102),Color.argb(OPACITY,0, 0, 0),Color.argb(OPACITY,102, 102, 102),Color.argb(OPACITY,0, 0, 0),Color.argb(OPACITY,0, 0,0)};
            	gradienteL = new LinearGradient(0.229f * WIDTH ,0.079f * HEIGHT, 0.803f * WIDTH, 0.899f * HEIGHT,colori,posizioni,Shader.TileMode.REPEAT);
        		pennello.setShader(gradienteL);
                break;

            default:
            	posizioni = new float[] {0.0f,0.07f,0.12f,1.0f};
            	colori = new int[] {Color.argb(OPACITY,254, 254, 254),Color.argb(OPACITY,210, 210, 210),Color.argb(OPACITY,179, 179, 179),Color.argb(OPACITY,213, 213, 213)};
            	gradienteL = new LinearGradient(0, 0, WIDTH, HEIGHT,colori,posizioni,Shader.TileMode.REPEAT);
        		pennello.setShader(gradienteL);
                break;

        }

        switch (EFFECT) {
        	case NONE:
        	default:
        		penEffetto.setShader(null);
        		break;

        	case BULGE:
        		posizioni = new float[] {0.0f,0.82f,0.83f,0.86f,0.87f,1.0f};
        		colori = new int[] {Color.argb(0, 0, 0, 0),Color.argb(76,0,0,0),Color.argb(95,0,0,0),Color.argb(153,219,219,219),Color.argb(151,255,255,255),Color.argb(102,0,0,0)};
        		gradienteR = new RadialGradient(CENTER_X, CENTER_Y, RADIUS, colori, posizioni, Shader.TileMode.REPEAT);
        		penEffetto.setShader(gradienteR);
        		break;

        	case CONE:
        		posizioni = new float[] {0.0f,0.82f,0.8201f,0.96f,0.9601f,1.0f};
        		colori = new int[] {Color.argb(0, 0, 0, 0),Color.argb(50, 0, 0, 0),Color.argb(51,9, 9, 9),Color.argb(124,255, 255, 255),Color.argb(127,223, 223, 223),Color.argb(76, 0, 0, 0)};
        		gradienteR = new RadialGradient(CENTER_X, CENTER_Y, RADIUS, colori, posizioni, Shader.TileMode.REPEAT);
        		penEffetto.setShader(gradienteR);
        		break;

        	case TORUS:
        		posizioni = new float[] {0.0f,0.82f,0.8201f,0.92f,1.0f};
        		colori = new int[] {Color.argb(0, 0, 0, 0),Color.argb(50, 0, 0, 0),Color.argb(51,13,13,13),Color.argb(64,255, 255, 255),Color.argb(76, 0, 0, 0)};
        		gradienteR = new RadialGradient(CENTER_X, CENTER_Y, RADIUS, colori, posizioni, Shader.TileMode.REPEAT);
        		penEffetto.setShader(gradienteR);
        		break;

        	case INNER_FRAME:
        		posizioni = new float[] {0.0f,0.8f,0.85f,0.90f,1.0f};
        		colori = new int[] {Color.argb(183,0, 0, 0),Color.argb(25,148, 148, 148),Color.argb(159,0, 0, 0),Color.argb(81,0, 0, 0),Color.argb(158,255, 255, 255)};
            	gradienteL = new LinearGradient(0, HEIGHT * 0.06f, 0, HEIGHT * 0.88f,colori,posizioni,Shader.TileMode.MIRROR);
        	//	gradienteR = new RadialGradient(CENTER_X, CENTER_Y, RADIUS, colori, posizioni, Shader.TileMode.REPEAT);
        	//	penEffetto.setShader(gradienteR);
        		penEffetto.setShader(gradienteL);
        		break;
        }

        
        switch(TYPE){
        	case CIRCULAR:


        		framePath.setFillType(FillType.WINDING);
                framePath.addOval(recOuterFrame, Path.Direction.CW);
                tela.clipPath(framePath, Op.REPLACE);
                framePath.reset();
                framePath.addOval(recInnerFrame, Path.Direction.CW);
                tela.clipPath(framePath, Op.DIFFERENCE);
                tela.drawPaint(pennello);
                tela.drawPaint(penEffetto);
                
        		framePath.setFillType(FillType.WINDING);
                framePath.addOval(recOuterShadow, Path.Direction.CW);
                tela.clipPath(framePath, Op.REPLACE);
                framePath.reset();
                framePath.addOval(recOuterFrame, Path.Direction.CW);
                tela.clipPath(framePath, Op.DIFFERENCE);
                tela.drawPaint(penShadow);
             
                framePath.addRect(recBoundary, Path.Direction.CW);
                tela.clipPath(framePath , Op.REPLACE );
        		tela.drawCircle(CENTER_X, CENTER_Y, recOuterFrame.width()/2f , matita);
        		tela.drawCircle(CENTER_X, CENTER_Y, recInnerFrame.width()/2f , matita);

        		
        		
        		break;
        	case ROUNDED:
        		
        		pennello.setStrokeMiter(1);
        		pennello.setStyle(Style.STROKE);
        		pennello.setARGB(255, 128, 128, 128);
        		tela.drawRoundRect(recOuterFrame, WIDTH * ROUNDCORNER_PER, WIDTH * ROUNDCORNER_PER, pennello);
        		
        		framePath.setFillType(FillType.WINDING);
        		framePath.addRoundRect(recOuterFrame, WIDTH * ROUNDCORNER_PER, WIDTH * ROUNDCORNER_PER, Path.Direction.CW);
                tela.clipPath(framePath, Op.REPLACE);
                framePath.reset();
                framePath.addOval(recInnerFrame, Path.Direction.CW);
                tela.clipPath(framePath, Op.DIFFERENCE);
                tela.drawPaint(pennello);
                tela.drawPaint(penEffetto);
         		
        		framePath.setFillType(FillType.WINDING);
        		framePath.addRoundRect(recOuterShadow, WIDTH * ROUNDCORNER_PER, WIDTH * ROUNDCORNER_PER, Path.Direction.CW);
                tela.clipPath(framePath, Op.REPLACE);
                framePath.reset();
                framePath.addRoundRect(recOuterFrame, WIDTH * ROUNDCORNER_PER, WIDTH * ROUNDCORNER_PER, Path.Direction.CW);
                tela.clipPath(framePath, Op.DIFFERENCE);
                tela.drawPaint(penShadow);
    		
        		break;
        	default:
        		break;
        }
        
        
        
        
 //       tela.clipPath(framePath);
 //       tela.drawPaint(pennello);
 //       tela.drawPaint(penEffetto);
      //  tela.clipPath(shadowPath);
      //  tela.drawPaint(penShadow);

        /* 
         * */
        // Cache current parameters
        cirType = TYPE;
    	cirWidth = WIDTH;
    	cirHeight = HEIGHT;
    	cirDesign = DESIGN;
    	cirEffect = EFFECT;
    	cirScrew = SCREW;
    
        return cirFrameImage;
        
    }
}


