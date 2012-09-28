package fap.example.canvasone;

import fap.example.canvasone.Material.Type;
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
import android.graphics.Path.FillType;
import android.graphics.Region.Op;


public class Indicatore {

	// Opacità dello strumento
	private int opacity = Dash.OPACITY;

	// Proprietà
    public Point axis = new Point();
    public int lunghezza = 0;
    public int larghezza = 0;
    public float radius = 0;
    public int color1 = Color.BLACK;
    public int color2 = Color.GRAY;
	public PointerType tipo = PointerType.HAND1;

    // Variabili
	float formfactor = 0.16f;
	
    private Bitmap pointerImage;
    private Bitmap pointerShadowImage;
    private Material.Type matType = Material.Type.BLACK_METAL;
    private Material mat;

    Canvas tela; 
    private static Paint pennello = new Paint();
    private static Paint matita = new Paint();
    
    private static float anglePosition = 0.0f;
    
    // Costruttore 
    public Indicatore(final PointerType TYPE, final float RADIUS, final int COLOR, final int BACKCOLOR) 
    {
    	tipo = TYPE;	
    	radius = RADIUS;
    	color1 = COLOR;
    	color2 = BACKCOLOR;
   		matType = Material.Type.LINEARVERTICAL;

   		creaPennelli();
   		drawStandardPointer();
    	
    	return;
    }
    
   public Indicatore(final PointerType TYPE, final float RADIUS, Material.Type MATERIAL_TYPE,final int COLOR, final int BACKCOLOR) 
   {
	   	tipo = TYPE;
   		radius = RADIUS;
   		matType = MATERIAL_TYPE;
	
   		creaPennelli();	
   		drawStandardPointer();
   		
   		return;
   }

   // 
   public Bitmap getPointer() {
	   return pointerImage;
   }

   public Bitmap getPointerShadow() {
	   return pointerShadowImage;
   }

   // this create the imagine for the indicators 
   public void drawStandardPointer() {
	   
	   // This if the radius is out of bound
       if (radius <= 0) {
    	   pointerImage = Bitmap.createBitmap(1 , 1, Config.ARGB_8888);
    	   pointerShadowImage = Bitmap.createBitmap(1 , 1, Config.ARGB_8888);
    	   return;
       }

       // star the game
       float lun,lar;
       RectF asse = new RectF();
       RectF area = new RectF();
       Path pathIndicatore = new Path();
    	
       float f1,f2,r,a;
		
       // crea l'immagine della lancetta in direzione Nord
       switch(tipo) {
       case HAND1:  // Accuminata
        	
        	// Geometria della lancetta
        	lun = radius + (radius * formfactor) / 2.0f; // aggiunge il semicerchio del perno
        	lar = (radius * formfactor ); // dimensiona la lancetta
        	r = lar / 2.0f; // raggio del perno
        	a = 35.0f; // semi angolo occupato dalla lancetta
        	f1 = r * (float)Math.sin(Math.toRadians(a)); // proiezioni dell'attacco sul perno
        	f2 = r * (float)Math.cos(Math.toRadians(a));

            // Ora Disegna la lancetta
    		// disegna il semi perno...
        	asse.set(0,radius-r,lar,radius+r);
    		pathIndicatore.arcTo(asse, 270.0f + a, 360.0f -2*a );
    		pathIndicatore.lineTo(r, 0);
    		pathIndicatore.close();

    		// Setta le dimensioni
    		pathIndicatore.computeBounds(area, true);
        	lunghezza = ((int)area.height()+1);
        	larghezza = ((int)area.width()+1);
        	axis.x = lar / 2.0f;
        	axis.y = lun - (lar / 2.0f);

        	drawTheIndicator(pathIndicatore);
        	
            break;
        
        case HAND2:  // a punta quadrata senza perno

        	// Geometria della lancetta
        	lun = radius + (radius * formfactor) / 2.0f; // aggiunge il semicerchio del perno
        	lar = (radius * formfactor ); // dimensiona la lancetta
        	r = lar / 2.0f; // raggio del perno
        	a = 30.0f; // semi angolo occupato dalla lancetta
        	f1 = r * (float)Math.sin(Math.toRadians(a)); // proiezioni dell'attacco sul perno
        	f2 = r * (float)Math.cos(Math.toRadians(a));

        	// ... e la lancetta
        	pathIndicatore.moveTo(r - f1, 0);
        	pathIndicatore.lineTo(r - f1, radius-f2);
        	pathIndicatore.lineTo(r + f1, radius-f2);
        	pathIndicatore.lineTo(r + f1, 0);
        	pathIndicatore.close();

    		// Setta le dimensioni
        	lunghezza = ((int) lun) +1;
        	larghezza = ((int) lar) +1;
        	axis.x = lar / 2.0f;
        	axis.y = lun - (lar / 2.0f);

        	drawTheIndicator(pathIndicatore);
        	
        	break;
        	
        case HAND3:  // a punta quadrata

        	// Geometria della lancetta
        	lun = radius + (radius * formfactor) / 2.0f; // aggiunge il semicerchio del perno
        	lar = (radius * formfactor ); // dimensiona la lancetta
        	r = lar / 2.0f; // raggio del perno
        	a = 30.0f; // semi angolo occupato dalla lancetta
        	f1 = r * (float)Math.sin(Math.toRadians(a)); // proiezioni dell'attacco sul perno
        	f2 = r * (float)Math.cos(Math.toRadians(a));

        	// disegna il semi perno...
        	asse.set(0,radius-r,lar,radius+r);
        	pathIndicatore.arcTo(asse, 270.0f+a, 360.0f-2*a );
        	// ... e la lancetta
        	pathIndicatore.lineTo(r - f1, 0);
        	pathIndicatore.lineTo(r + f1, 0);
        	pathIndicatore.close();

    		// Setta le dimensioni
        	lunghezza = ((int) lun) +1;
        	larghezza = ((int) lar) +1;
        	axis.x = lar / 2.0f;
        	axis.y = lun - (lar / 2.0f);

        	drawTheIndicator(pathIndicatore);
        	
        	break;

        case HAND4:  // a punta tonda

        	// Geometria della lancetta
        	lun = radius + (radius * formfactor) / 2.0f; // aggiunge il semicerchio del perno
        	lar = (radius * formfactor ); // dimensiona la lancetta
        	r = lar / 2.0f; // raggio del perno
        	a = 30.0f; // semi angolo occupato dalla lancetta
        	f1 = r * (float)Math.sin(Math.toRadians(a)); // proiezioni dell'attacco sul perno
        	f2 = r * (float)Math.cos(Math.toRadians(a));

        	// disegna il semi perno...
        	asse.set(0,radius-r,lar,radius+r);
        	pathIndicatore.arcTo(asse, 270.0f+a, 360.0f-2*a );
        	// ... e la lancetta
        	pathIndicatore.lineTo(r - f1, radius * 0.08f);
        	pathIndicatore.cubicTo(r - f1, radius * 0.08f, r, 0, r + f1, radius * 0.08f);
        	pathIndicatore.lineTo(r + f1, radius * 0.08f);
        	pathIndicatore.close();

    		// Setta le dimensioni
        	lunghezza = ((int) lun) +1;
        	larghezza = ((int) lar) +1;
        	axis.x = lar / 2.0f;
        	axis.y = lun - (lar / 2.0f);

        	drawTheIndicator(pathIndicatore);
        	
        	break;

        case HAND5:  // a punta tonda sottile

        	// Geometria della lancetta
        	lun = radius + (radius * formfactor) / 2.0f; // aggiunge il semicerchio del perno
        	lar = (radius * formfactor ); // dimensiona la lancetta
        	r = lar / 2.0f; // raggio del perno
        	a = 10.0f; // semi angolo occupato dalla lancetta
        	f1 = r * (float)Math.sin(Math.toRadians(a)); // proiezioni dell'attacco sul perno
        	f2 = r * (float)Math.cos(Math.toRadians(a));

        	// disegna il semi perno...
        	asse.set(0,radius-r,lar,radius+r);
        	pathIndicatore.arcTo(asse, 270.0f+a, 360.0f-2*a );
        	// ... e la lancetta
        	pathIndicatore.lineTo(r - f1, radius * 0.08f);
        	pathIndicatore.cubicTo(r - f1, radius * 0.08f, r, 0, r + f1, radius * 0.08f);
        	pathIndicatore.lineTo(r + f1, radius * 0.08f);
        	pathIndicatore.close();

    		// Setta le dimensioni
        	lunghezza = ((int) lun) +1;
        	larghezza = ((int) lar) +1;
        	axis.x = lar / 2.0f;
        	axis.y = lun - (lar / 2.0f);

        	drawTheIndicator(pathIndicatore);
        	
        	break;
        	
        case NEEDLE1:  // Ago
           	
        	// Geometria della lancetta
        	lun = radius + (radius * formfactor) / 2.0f; // aggiunge il semicerchio del perno 
        	lar = (radius * formfactor ); // dimensiona la lancetta
        	r = lar / 2.0f; // raggio del perno
        	a = 15.0f; // semi angolo occupato dalla lancetta
        	f1 = r * (float)Math.sin(Math.toRadians(a)); // proiezioni dell'attacco sul perno
        	f2 = r * (float)Math.cos(Math.toRadians(a));

        	// Ora Disegna la lancetta
        	pathIndicatore.moveTo(r, 0);
        	pathIndicatore.lineTo(r + f1, radius - f2 );
        	asse.set(0.0f,radius-r,lar,radius+r);
        	pathIndicatore.arcTo(asse, 270.0f+a, 360.0f-2*a);
        	pathIndicatore.close();
        	
    		// Setta le dimensioni
        	lunghezza = ((int) lun) +1;
        	larghezza = ((int) lar) +1;
        	// posiziona l'asse all'estremo inferiore 
        	axis.x = lar / 2.0f;
        	axis.y = radius;

        	drawTheIndicator(pathIndicatore);
		
        	break;
        	
        case MOONEND:  // Con la luna
           	
        	// Geometria della lancetta
        	lun = radius + (radius * formfactor) / 2.0f + (radius / 2.0f); // aggiunge il semicerchio del perno + la coda
        	lar = (radius * formfactor ); // dimensiona la lancetta
        	r = lar / 2.0f;
        	a = 15.0f;
        	f1 = r * (float)Math.sin(Math.toRadians(a));
        	f2 = r * (float)Math.cos(Math.toRadians(a));

        	// Ora Disegna la lancetta
        	pathIndicatore.moveTo(r, 0);
        	pathIndicatore.lineTo(r + f1, radius * 0.10f);
        	pathIndicatore.lineTo(r + f1, radius - f2 );
        	asse.set(0.0f,radius-r,lar,radius+r);
        	pathIndicatore.arcTo(asse, 270.0f+a, 180.0f-2*a);
        	pathIndicatore.lineTo(r + f1, radius * 1.50f - f2);
        	asse.set(0.0f, radius * 1.5f - r, lar, radius * 1.5f + r);
        	pathIndicatore.arcTo(asse, 270.0f+a, 90.0f-a );
          	pathIndicatore.lineTo(0, radius *1.5f);
        	asse.set(0.0f, radius * 1.5f - r, lar, radius * 1.5f + r);
        	pathIndicatore.arcTo(asse, 180.0f, 90.0f-a );
        	pathIndicatore.lineTo(r - f1, radius + f2);
        	asse.set(0.0f,radius-r,lar,radius+r);
        	pathIndicatore.arcTo(asse, 90.0f+a, 180.0f-2*a);
        	pathIndicatore.lineTo(r -f1, radius * 0.10f);
        	pathIndicatore.close();
        	
    		// Setta le dimensioni
        	lunghezza = ((int) lun) +1;
        	larghezza = ((int) lar) +1;
        	axis.x = lar / 2.0f;
        	axis.y = radius;

        	drawTheIndicator(pathIndicatore);
		
        	break;
        	
        case HOLE1:

        	// Geometria della lancetta
        	lun = radius * 1.35f; // aggiunge il semicerchio del perno + la coda
        	lar = (radius * formfactor); // dimensiona la lancetta
        	r = lar / 2.0f;
        	a = 15.0f;
        	f1 = r * (float)Math.sin(Math.toRadians(a));
        	f2 = r * (float)Math.cos(Math.toRadians(a));
        	
        	pathIndicatore.moveTo(r * 0.85f, radius * 0.25f);
        	pathIndicatore.lineTo(r * 1.15f, radius * 0.25f);
        	pathIndicatore.lineTo(r * 1.50f, radius * 0.85f);
        	pathIndicatore.lineTo(r * 0.50f, radius * 0.85f);
        	pathIndicatore.lineTo(r * 0.85f, radius * 0.25f);
        	pathIndicatore.close();
        	
        	pathIndicatore.moveTo(r * 0.80f, radius * 0.00f);
        	pathIndicatore.lineTo(r * 0.20f, radius * 0.70f);
        	pathIndicatore.cubicTo(r * 0.20f, radius * 0.70f, r * 0.00f, radius * 1.00f, r * 0.20f, radius * 1.30f);
        	pathIndicatore.cubicTo(r * 0.20f, radius * 1.30f, r * 1.00f, radius * 1.35f, r * 1.80f, radius * 1.30f);
        	pathIndicatore.cubicTo(r * 1.80f, radius * 1.30f, r * 2.00f, radius * 1.00f, r * 1.80f, radius * 0.70f);
        	pathIndicatore.lineTo(r * 1.20f, radius * 0.00f);
        	pathIndicatore.lineTo(r * 0.80f, radius * 0.00f);
        	pathIndicatore.close();
        	
    		// Setta le dimensioni
        	lunghezza = ((int) lun) +1;
        	larghezza = ((int) lar) +1;
        	axis.x = r;
        	axis.y = radius;

        	drawTheIndicator(pathIndicatore);
        	break;
        	
        case SWISS:

        	// Geometria della lancetta svizzera
        	lun = radius * 1.25f; // aggiunge la coda
        	lar = (radius * formfactor); // dimensiona la lancetta
        	r = lar / 2.0f;
        	a = 5.0f;
        	f1 = r * (float)Math.sin(Math.toRadians(a));
        	f2 = r * (float)Math.cos(Math.toRadians(a));

        	pathIndicatore.moveTo(r-f1, r+f2);
        	asse.set(0,0,2*r,2*r);
        	pathIndicatore.addArc(asse, 90.0f+a, 360.0f-2*a);
        	pathIndicatore.lineTo(r +f1, radius * 1.25f);
        	pathIndicatore.lineTo(r -f1, radius * 1.25f);
        	pathIndicatore.lineTo(r -f1, r+f2);
        	pathIndicatore.close();
        	
        	// Setta le dimensioni
        	lunghezza = ((int) lun) +1;
        	larghezza = ((int) lar) +1;
        	axis.x = r;
        	axis.y = radius;

        	drawTheIndicator(pathIndicatore);
        	break;

        case SWISS2:

        	// Geometria della lancetta svizzera
        	lun = radius * 1.25f; // aggiunge la coda
        	lar = (radius * formfactor); // dimensiona la lancetta
        	r = lar / 2.0f;
        	a = 5.0f;
        	f1 = r * (float)Math.sin(Math.toRadians(a));
        	f2 = r * (float)Math.cos(Math.toRadians(a));

        	pathIndicatore.moveTo(r*1.75f, r);
        	asse.set(r*0.25f,r*0.25f,r*1.75f,r*1.75f);
        	pathIndicatore.arcTo(asse, 0, -359.0f, false); 
        	pathIndicatore.close();

        	pathIndicatore.moveTo(r-f1, r+f2);
        	asse.set(0,0,2*r,2*r);
        	pathIndicatore.arcTo(asse, 90.0f+a, 360.0f-2*a, false);
        	pathIndicatore.lineTo(r +f1, radius * 1.25f);
        	pathIndicatore.lineTo(r -f1, radius * 1.25f);
        	pathIndicatore.lineTo(r -f1, r+f2);
        	pathIndicatore.close();
        	
        	// Setta le dimensioni
        	lunghezza = ((int) lun) +1;
        	larghezza = ((int) lar) +1;
        	axis.x = r;
        	axis.y = radius;

        	drawTheIndicator(pathIndicatore);
        	break;
        	
        case NEEDLE2:

        	// Geometria della lancetta
        	lun = radius * 1.05f; // aggiunge il semicerchio del perno + la coda
        	lar = (radius * formfactor); // dimensiona la lancetta
        	r = lar / 2.0f;
        	a = 15.0f;
        	f1 = r * (float)Math.sin(Math.toRadians(a));
        	f2 = r * (float)Math.cos(Math.toRadians(a));
   
        	pathIndicatore.moveTo(r * 1.00f, radius * 0.00f);
        	pathIndicatore.cubicTo(r * 1.00f, radius * 0.00f, r * 0.80f, radius * 0.90f, r * 0.60f, radius * 1.00f);
        	pathIndicatore.cubicTo(r * 0.60f, radius * 1.00f, r * 1.00f, radius * 1.05f, r * 1.40f, radius * 1.00f);
        	pathIndicatore.cubicTo(r * 1.40f, radius * 1.00f, r * 1.20f, radius * 0.90f, r * 1.00f, radius * 0.00f);
        	pathIndicatore.close();
        	
    		// Setta le dimensioni
        	lunghezza = ((int) lun) +1;
        	larghezza = ((int) lar) +1;
        	axis.x = r;
        	axis.y = radius;

        	drawTheIndicator(pathIndicatore);
        	break;
        	
        case NEEDLE3:

        	// Geometria della lancetta
        	lun = radius * 1.05f; // aggiunge il semicerchio del perno + la coda
        	lar = (radius * formfactor); // dimensiona la lancetta
        	r = lar / 2.0f;
        	a = 15.0f;
        	f1 = r * (float)Math.sin(Math.toRadians(a));
        	f2 = r * (float)Math.cos(Math.toRadians(a));
   
        	pathIndicatore.moveTo(r * 1.00f, radius * 0.00f);
        	pathIndicatore.cubicTo(r * 1.00f, radius * 0.00f, r * 0.70f, radius * 0.80f, r * 0.40f, radius * 1.00f);
        	pathIndicatore.cubicTo(r * 0.40f, radius * 1.00f, r * 1.00f, radius * 1.05f, r * 1.60f, radius * 1.00f);
        	pathIndicatore.cubicTo(r * 1.60f, radius * 1.00f, r * 1.30f, radius * 0.80f, r * 1.00f, radius * 0.00f);
        	pathIndicatore.close();
        	
    		// Setta le dimensioni
        	lunghezza = ((int) lun) +1;
        	larghezza = ((int) lar) +1;
        	axis.x = r;
        	axis.y = radius;

        	drawTheIndicator(pathIndicatore);
        	break;
        	
        case NEEDLE4:

        	// Geometria della lancetta
        	lun = radius * 1.10f; // aggiunge  la coda
        	lar = (radius * formfactor); // dimensiona la lancetta
        	r = lar / 2.0f;
        	a = 15.0f;
        	f1 = r * (float)Math.sin(Math.toRadians(a));
        	f2 = r * (float)Math.cos(Math.toRadians(a));
   
        	pathIndicatore.moveTo(r * 1.00f, radius * 0.00f);
        	pathIndicatore.cubicTo(r * 1.00f, radius * 0.00f,r * 0.30f, radius * 0.50f,r * 0.00f, radius * 0.60f);
        	pathIndicatore.cubicTo(r * 0.00f, radius * 0.60f, r * 0.30f, radius * 0.70f,r * 0.40f, radius * 0.90f);
        	pathIndicatore.lineTo(r * 0.40f, radius * 1.10f);
        	pathIndicatore.lineTo(r * 1.60f, radius * 1.10f);
        	pathIndicatore.lineTo(r * 1.60f, radius * 0.90f);
        	pathIndicatore.cubicTo(r * 1.60f, radius * 0.90f, r * 1.70f, radius * 0.70f,r * 2.00f, radius * 0.60f);
        	pathIndicatore.cubicTo(r * 2.00f, radius * 0.60f, r * 1.70f, radius * 0.50f,r * 1.00f, radius * 0.0f);
        	pathIndicatore.close();
        	
    		// Setta le dimensioni
        	lunghezza = ((int) lun) +1;
        	larghezza = ((int) lar) +1;
        	axis.x = r;
        	axis.y = radius;

        	drawTheIndicator(pathIndicatore);
        	break;
        	
        	
        }

    return;    
    }

/*


        radPointerImage.flush();
        radPointerImage = UTIL.createImage(WIDTH, WIDTH, Transparency.TRANSLUCENT);
        final Graphics2D G2 = radPointerImage.createGraphics();
        G2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        G2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        //G2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //G2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        //G2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        final int IMAGE_WIDTH = radPointerImage.getWidth();
        final int IMAGE_HEIGHT = radPointerImage.getHeight();

        final GeneralPath POINTER;
        final Point2D POINTER_START;
        final Point2D POINTER_STOP;
        final float[] POINTER_FRACTIONS;
        final Color[] POINTER_COLORS;
        final java.awt.Paint POINTER_GRADIENT;

        switch (POINTER_TYPE) {
            case TYPE2:
                POINTER = new GeneralPath();
                POINTER.setWindingRule(Path2D.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.lineTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.46261682242990654);
                POINTER.lineTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.3411214953271028);
                POINTER.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.3411214953271028);
                POINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.46261682242990654);
                POINTER.lineTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.curveTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.4719626168224299, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.49065420560747663, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5);
                POINTER.curveTo(IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5186915887850467, IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.5327102803738317, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5327102803738317);
                POINTER.curveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.5327102803738317, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5186915887850467, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5);
                POINTER.curveTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.49065420560747663, IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.4719626168224299, IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.closePath();
                POINTER_START = new Point2D.Double(0, POINTER.getBounds2D().getMaxY());
                POINTER_STOP = new Point2D.Double(0, POINTER.getBounds2D().getMinY());
                POINTER_FRACTIONS = new float[]{
                    0.0f,
                    0.36f,
                    0.3601f,
                    1.0f
                };
                if (POINTER_COLOR != ColorDef.CUSTOM) {
                    POINTER_COLORS = new Color[]{
                        BACKGROUND_COLOR.LABEL_COLOR,
                        BACKGROUND_COLOR.LABEL_COLOR,
                        POINTER_COLOR.LIGHT,
                        POINTER_COLOR.LIGHT
                    };
                } else {
                    POINTER_COLORS = new Color[]{
                        BACKGROUND_COLOR.LABEL_COLOR,
                        BACKGROUND_COLOR.LABEL_COLOR,
                        CUSTOM_POINTER_COLOR.LIGHT,
                        CUSTOM_POINTER_COLOR.LIGHT
                    };
                }
                POINTER_GRADIENT = new LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
                G2.setPaint(POINTER_GRADIENT);
                G2.fill(POINTER);
                break;

            case TYPE3:
                POINTER = new GeneralPath(new Rectangle2D.Double(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.1308411214953271, IMAGE_WIDTH * 0.009345794392523364, IMAGE_HEIGHT * 0.37383177570093457));
                if (POINTER_COLOR != ColorDef.CUSTOM) {
                    G2.setColor(POINTER_COLOR.LIGHT);
                } else {
                    G2.setColor(CUSTOM_POINTER_COLOR.LIGHT);
                }
                G2.fill(POINTER);
                break;

            case TYPE4:
                POINTER = new GeneralPath();
                POINTER.setWindingRule(Path2D.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.1261682242990654);
                POINTER.lineTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.13551401869158877);
                POINTER.lineTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5);
                POINTER.lineTo(IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.602803738317757);
                POINTER.lineTo(IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.602803738317757);
                POINTER.lineTo(IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5);
                POINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.13551401869158877);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.1261682242990654);
                POINTER.closePath();
                POINTER_START = new Point2D.Double(POINTER.getBounds2D().getMinX(), 0);
                POINTER_STOP = new Point2D.Double(POINTER.getBounds2D().getMaxX(), 0);
                POINTER_FRACTIONS = new float[]{
                    0.0f,
                    0.51f,
                    0.52f,
                    1.0f
                };
                if (POINTER_COLOR != ColorDef.CUSTOM) {
                    POINTER_COLORS = new Color[]{
                        POINTER_COLOR.DARK,
                        POINTER_COLOR.DARK,
                        POINTER_COLOR.LIGHT,
                        POINTER_COLOR.LIGHT
                    };
                } else {
                    POINTER_COLORS = new Color[]{
                        CUSTOM_POINTER_COLOR.DARK,
                        CUSTOM_POINTER_COLOR.DARK,
                        CUSTOM_POINTER_COLOR.LIGHT,
                        CUSTOM_POINTER_COLOR.LIGHT
                    };
                }
                POINTER_GRADIENT = new LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
                G2.setPaint(POINTER_GRADIENT);
                G2.fill(POINTER);
                break;

            case TYPE5:
                POINTER = new GeneralPath();
                POINTER.setWindingRule(Path2D.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.4953271028037383);
                POINTER.lineTo(IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.4953271028037383);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.14953271028037382);
                POINTER.lineTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.4953271028037383);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.4953271028037383);
                POINTER.closePath();
                POINTER_START = new Point2D.Double(POINTER.getBounds2D().getMinX(), 0);
                POINTER_STOP = new Point2D.Double(POINTER.getBounds2D().getMaxX(), 0);
                POINTER_FRACTIONS = new float[]{
                    0.0f,
                    0.4999f,
                    0.5f,
                    1.0f
                };
                if (POINTER_COLOR != ColorDef.CUSTOM) {
                    POINTER_COLORS = new Color[]{
                        POINTER_COLOR.LIGHT,
                        POINTER_COLOR.LIGHT,
                        POINTER_COLOR.MEDIUM,
                        POINTER_COLOR.MEDIUM
                    };
                } else {
                    POINTER_COLORS = new Color[]{
                        CUSTOM_POINTER_COLOR.LIGHT,
                        CUSTOM_POINTER_COLOR.LIGHT,
                        CUSTOM_POINTER_COLOR.MEDIUM,
                        CUSTOM_POINTER_COLOR.MEDIUM
                    };
                }
                POINTER_GRADIENT = new LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
                G2.setPaint(POINTER_GRADIENT);
                G2.fill(POINTER);
                if (POINTER_COLOR != ColorDef.CUSTOM) {
                    G2.setColor(POINTER_COLOR.DARK);
                } else {
                    G2.setColor(CUSTOM_POINTER_COLOR.DARK);
                }
                G2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
                G2.draw(POINTER);
                break;

            case TYPE6:
                POINTER = new GeneralPath();
                POINTER.setWindingRule(Path2D.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.48598130841121495);
                POINTER.lineTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.3925233644859813);
                POINTER.lineTo(IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.3177570093457944);
                POINTER.lineTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.3177570093457944);
                POINTER.lineTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.3878504672897196);
                POINTER.lineTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.48598130841121495);
                POINTER.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.48598130841121495);
                POINTER.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.3878504672897196);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.3177570093457944);
                POINTER.lineTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.3925233644859813);
                POINTER.lineTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.48598130841121495);
                POINTER.lineTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.48598130841121495);
                POINTER.closePath();
                POINTER_START = new Point2D.Double(POINTER.getBounds2D().getMaxY(), 0);
                POINTER_STOP = new Point2D.Double(POINTER.getBounds2D().getMinY(), 0);
                POINTER_FRACTIONS = new float[]{
                    0.0f,
                    0.25f,
                    0.75f,
                    1.0f
                };
                if (POINTER_COLOR != ColorDef.CUSTOM) {
                    POINTER_COLORS = new Color[]{
                        POINTER_COLOR.LIGHT,
                        POINTER_COLOR.MEDIUM,
                        POINTER_COLOR.MEDIUM,
                        POINTER_COLOR.LIGHT
                    };
                } else {
                    POINTER_COLORS = new Color[]{
                        CUSTOM_POINTER_COLOR.LIGHT,
                        CUSTOM_POINTER_COLOR.MEDIUM,
                        CUSTOM_POINTER_COLOR.MEDIUM,
                        CUSTOM_POINTER_COLOR.LIGHT
                    };
                }
                POINTER_GRADIENT = new LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
                G2.setPaint(POINTER_GRADIENT);
                G2.fill(POINTER);
                if (POINTER_COLOR != ColorDef.CUSTOM) {
                    G2.setColor(POINTER_COLOR.DARK);
                } else {
                    G2.setColor(CUSTOM_POINTER_COLOR.DARK);
                }
                G2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
                G2.draw(POINTER);
                break;

            case TYPE7:
                POINTER = new GeneralPath();
                POINTER.setWindingRule(Path2D.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.5);
                POINTER.lineTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.5);
                POINTER.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.closePath();
                POINTER_START = new Point2D.Double(POINTER.getBounds2D().getMinX(), 0);
                POINTER_STOP = new Point2D.Double(POINTER.getBounds2D().getMaxX(), 0);
                POINTER_FRACTIONS = new float[]{
                    0.0f,
                    1.0f
                };
                if (POINTER_COLOR != ColorDef.CUSTOM) {
                    POINTER_COLORS = new Color[]{
                        POINTER_COLOR.DARK,
                        POINTER_COLOR.MEDIUM
                    };
                } else {
                    POINTER_COLORS = new Color[]{
                        CUSTOM_POINTER_COLOR.DARK,
                        CUSTOM_POINTER_COLOR.MEDIUM
                    };
                }
                POINTER_GRADIENT = new LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
                G2.setPaint(POINTER_GRADIENT);
                G2.fill(POINTER);
                break;

            case TYPE8:
                POINTER = new GeneralPath();
                POINTER.setWindingRule(Path2D.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5327102803738317);
                POINTER.lineTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5);
                POINTER.curveTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.14953271028037382);
                POINTER.curveTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5327102803738317);
                POINTER.closePath();
                POINTER_START = new Point2D.Double(POINTER.getBounds2D().getMinX(), 0);
                POINTER_STOP = new Point2D.Double(POINTER.getBounds2D().getMaxX(), 0);
                POINTER_FRACTIONS = new float[]{
                    0.0f,
                    0.46f,
                    0.47f,
                    1.0f
                };
                if (POINTER_COLOR != ColorDef.CUSTOM) {
                    POINTER_COLORS = new Color[]{
                        POINTER_COLOR.LIGHT,
                        POINTER_COLOR.LIGHT,
                        POINTER_COLOR.MEDIUM,
                        POINTER_COLOR.MEDIUM
                    };
                } else {
                    POINTER_COLORS = new Color[]{
                        CUSTOM_POINTER_COLOR.LIGHT,
                        CUSTOM_POINTER_COLOR.LIGHT,
                        CUSTOM_POINTER_COLOR.MEDIUM,
                        CUSTOM_POINTER_COLOR.MEDIUM
                    };
                }
                POINTER_GRADIENT = new LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
                G2.setPaint(POINTER_GRADIENT);
                G2.fill(POINTER);
                if (POINTER_COLOR != ColorDef.CUSTOM) {
                    G2.setColor(POINTER_COLOR.DARK);
                } else {
                    G2.setColor(CUSTOM_POINTER_COLOR.DARK);
                }
                G2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
                G2.draw(POINTER);
                break;


            case TYPE10:
                POINTER = new GeneralPath();
                POINTER.setWindingRule(Path2D.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.14953271028037382);
                POINTER.curveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.14953271028037382, IMAGE_WIDTH * 0.4439252336448598, IMAGE_HEIGHT * 0.49065420560747663, IMAGE_WIDTH * 0.4439252336448598, IMAGE_HEIGHT * 0.5);
                POINTER.curveTo(IMAGE_WIDTH * 0.4439252336448598, IMAGE_HEIGHT * 0.5327102803738317, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5560747663551402, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5560747663551402);
                POINTER.curveTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5560747663551402, IMAGE_WIDTH * 0.5560747663551402, IMAGE_HEIGHT * 0.5327102803738317, IMAGE_WIDTH * 0.5560747663551402, IMAGE_HEIGHT * 0.5);
                POINTER.curveTo(IMAGE_WIDTH * 0.5560747663551402, IMAGE_HEIGHT * 0.49065420560747663, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.14953271028037382, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.14953271028037382);
                POINTER.closePath();
                POINTER_START = new Point2D.Double(POINTER.getBounds2D().getMinX(), 0);
                POINTER_STOP = new Point2D.Double(POINTER.getBounds2D().getMaxX(), 0);
                POINTER_FRACTIONS = new float[]{
                    0.0f,
                    0.4999f,
                    0.5f,
                    1.0f
                };
                if (POINTER_COLOR != ColorDef.CUSTOM) {
                    POINTER_COLORS = new Color[]{
                        POINTER_COLOR.LIGHT,
                        POINTER_COLOR.LIGHT,
                        POINTER_COLOR.MEDIUM,
                        POINTER_COLOR.MEDIUM
                    };
                } else {
                    POINTER_COLORS = new Color[]{
                        CUSTOM_POINTER_COLOR.LIGHT,
                        CUSTOM_POINTER_COLOR.LIGHT,
                        CUSTOM_POINTER_COLOR.MEDIUM,
                        CUSTOM_POINTER_COLOR.MEDIUM
                    };
                }
                POINTER_GRADIENT = new LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
                G2.setPaint(POINTER_GRADIENT);
                G2.fill(POINTER);
                G2.setColor(POINTER_COLOR.MEDIUM);
                G2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
                G2.draw(POINTER);
                break;

            case TYPE11:
                POINTER = new GeneralPath();
                POINTER.setWindingRule(Path2D.WIND_EVEN_ODD);
                POINTER.moveTo(0.5 * IMAGE_WIDTH, 0.16822429906542055 * IMAGE_HEIGHT);
                POINTER.lineTo(0.48598130841121495 * IMAGE_WIDTH, 0.5 * IMAGE_HEIGHT);
                POINTER.curveTo(0.48598130841121495 * IMAGE_WIDTH, 0.5 * IMAGE_HEIGHT, 0.48130841121495327 * IMAGE_WIDTH, 0.5841121495327103 * IMAGE_HEIGHT, 0.5 * IMAGE_WIDTH, 0.5841121495327103 * IMAGE_HEIGHT);
                POINTER.curveTo(0.514018691588785 * IMAGE_WIDTH, 0.5841121495327103 * IMAGE_HEIGHT, 0.5093457943925234 * IMAGE_WIDTH, 0.5 * IMAGE_HEIGHT, 0.5093457943925234 * IMAGE_WIDTH, 0.5 * IMAGE_HEIGHT);
                POINTER.lineTo(0.5 * IMAGE_WIDTH, 0.16822429906542055 * IMAGE_HEIGHT);
                POINTER.closePath();
                if (POINTER_COLOR != ColorDef.CUSTOM) {
                    POINTER_GRADIENT = new LinearGradientPaint(new Point2D.Double(0, POINTER.getBounds2D().getMinY()), new Point2D.Double(0, POINTER.getBounds2D().getMaxY()), new float[]{0.0f, 1.0f}, new Color[]{POINTER_COLOR.MEDIUM, POINTER_COLOR.DARK});
                    G2.setPaint(POINTER_GRADIENT);
                    G2.fill(POINTER);
                    G2.setColor(POINTER_COLOR.VERY_DARK);
                } else {
                    POINTER_GRADIENT = new LinearGradientPaint(new Point2D.Double(0, POINTER.getBounds2D().getMinY()), new Point2D.Double(0, POINTER.getBounds2D().getMaxY()), new float[]{0.0f, 1.0f}, new Color[]{CUSTOM_POINTER_COLOR.MEDIUM, CUSTOM_POINTER_COLOR.DARK});
                    G2.setPaint(POINTER_GRADIENT);
                    G2.fill(POINTER);
                    G2.setColor(CUSTOM_POINTER_COLOR.VERY_DARK);
                }
                G2.setStroke(new BasicStroke(0.004672897196261682f * IMAGE_WIDTH, 0, 1));
                G2.draw(POINTER);
                break;

            case TYPE12:
                POINTER = new GeneralPath();
                POINTER.setWindingRule(Path2D.WIND_EVEN_ODD);
                POINTER.moveTo(0.5 * IMAGE_WIDTH, 0.16822429906542055 * IMAGE_HEIGHT);
                POINTER.lineTo(0.48598130841121495 * IMAGE_WIDTH, 0.5 * IMAGE_HEIGHT);
                POINTER.lineTo(0.5 * IMAGE_WIDTH, 0.5046728971962616 * IMAGE_HEIGHT);
                POINTER.lineTo(0.5093457943925234 * IMAGE_WIDTH, 0.5 * IMAGE_HEIGHT);
                POINTER.lineTo(0.5 * IMAGE_WIDTH, 0.16822429906542055 * IMAGE_HEIGHT);
                POINTER.closePath();
                if (POINTER_COLOR != ColorDef.CUSTOM) {
                    G2.setPaint(new LinearGradientPaint(new Point2D.Double(0.5 * IMAGE_WIDTH, 0.16822429906542055 * IMAGE_HEIGHT), new Point2D.Double(0.5 * IMAGE_WIDTH, 0.5046728971962616 * IMAGE_HEIGHT), new float[]{0.0f, 1.0f}, new Color[]{POINTER_COLOR.MEDIUM, POINTER_COLOR.DARK}));
                    G2.fill(POINTER);
                    G2.setColor(POINTER_COLOR.VERY_DARK);
                } else {
                    G2.setPaint(new LinearGradientPaint(new Point2D.Double(0.5 * IMAGE_WIDTH, 0.16822429906542055 * IMAGE_HEIGHT), new Point2D.Double(0.5 * IMAGE_WIDTH, 0.5046728971962616 * IMAGE_HEIGHT), new float[]{0.0f, 1.0f}, new Color[]{CUSTOM_POINTER_COLOR.MEDIUM, CUSTOM_POINTER_COLOR.DARK}));
                    G2.fill(POINTER);
                    G2.setColor(CUSTOM_POINTER_COLOR.VERY_DARK);
                }
                G2.setStroke(new BasicStroke((0.004672897196261682f * IMAGE_WIDTH), 0, 1));
                G2.draw(POINTER);
                break;

            case TYPE13:
                POINTER = new GeneralPath();
                POINTER.setWindingRule(Path2D.WIND_EVEN_ODD);
                POINTER.moveTo(0.48598130841121495 * IMAGE_WIDTH, 0.16822429906542055 * IMAGE_HEIGHT);
                POINTER.lineTo(0.5 * IMAGE_WIDTH, 0.1308411214953271 * IMAGE_HEIGHT);
                POINTER.lineTo(0.5093457943925234 * IMAGE_WIDTH, 0.16822429906542055 * IMAGE_HEIGHT);
                POINTER.lineTo(0.5093457943925234 * IMAGE_WIDTH, 0.5093457943925234 * IMAGE_HEIGHT);
                POINTER.lineTo(0.48598130841121495 * IMAGE_WIDTH, 0.5093457943925234 * IMAGE_HEIGHT);
                POINTER.lineTo(0.48598130841121495 * IMAGE_WIDTH, 0.16822429906542055 * IMAGE_HEIGHT);
                POINTER.closePath();
                if (POINTER_COLOR != ColorDef.CUSTOM) {
                    G2.setPaint(new LinearGradientPaint(new Point2D.Double(0.5 * IMAGE_WIDTH, 0.5 * IMAGE_HEIGHT), new Point2D.Double(0.5 * IMAGE_WIDTH, 0.1308411214953271 * IMAGE_HEIGHT), new float[]{0.0f, 0.849999f, 0.85f, 1.0f}, new Color[]{BACKGROUND_COLOR.LABEL_COLOR, BACKGROUND_COLOR.LABEL_COLOR, POINTER_COLOR.MEDIUM, POINTER_COLOR.MEDIUM}));
                } else {
                    G2.setPaint(new LinearGradientPaint(new Point2D.Double(0.5 * IMAGE_WIDTH, 0.5 * IMAGE_HEIGHT), new Point2D.Double(0.5 * IMAGE_WIDTH, 0.1308411214953271 * IMAGE_HEIGHT), new float[]{0.0f, 0.849999f, 0.85f, 1.0f}, new Color[]{BACKGROUND_COLOR.LABEL_COLOR, BACKGROUND_COLOR.LABEL_COLOR, CUSTOM_POINTER_COLOR.MEDIUM, CUSTOM_POINTER_COLOR.MEDIUM}));
                }
                G2.fill(POINTER);
                break;

            case TYPE14:
                POINTER = new GeneralPath();
                POINTER.setWindingRule(Path2D.WIND_EVEN_ODD);
                POINTER.moveTo(0.48598130841121495 * IMAGE_WIDTH, 0.16822429906542055 * IMAGE_HEIGHT);
                POINTER.lineTo(0.5 * IMAGE_WIDTH, 0.1308411214953271 * IMAGE_HEIGHT);
                POINTER.lineTo(0.5093457943925234 * IMAGE_WIDTH, 0.16822429906542055 * IMAGE_HEIGHT);
                POINTER.lineTo(0.5093457943925234 * IMAGE_WIDTH, 0.5093457943925234 * IMAGE_HEIGHT);
                POINTER.lineTo(0.48598130841121495 * IMAGE_WIDTH, 0.5093457943925234 * IMAGE_HEIGHT);
                POINTER.lineTo(0.48598130841121495 * IMAGE_WIDTH, 0.16822429906542055 * IMAGE_HEIGHT);
                POINTER.closePath();
                if (POINTER_COLOR != ColorDef.CUSTOM) {
                    G2.setPaint(new LinearGradientPaint(new Point2D.Double(0.48598130841121495 * IMAGE_WIDTH, 0), new Point2D.Double(0.5093457943925234 * IMAGE_HEIGHT, 0), new float[]{0f, 0.5f, 1f}, new Color[]{POINTER_COLOR.VERY_DARK, POINTER_COLOR.LIGHT, POINTER_COLOR.VERY_DARK}));
                } else {
                    G2.setPaint(new LinearGradientPaint(new Point2D.Double(0.48598130841121495 * IMAGE_WIDTH, 0), new Point2D.Double(0.5093457943925234 * IMAGE_HEIGHT, 0), new float[]{0f, 0.5f, 1f}, new Color[]{CUSTOM_POINTER_COLOR.VERY_DARK, CUSTOM_POINTER_COLOR.LIGHT, CUSTOM_POINTER_COLOR.VERY_DARK}));
                }

                G2.fill(POINTER);

                break;

            case TYPE1:

            default:
                POINTER = new GeneralPath();
                POINTER.setWindingRule(Path2D.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.curveTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.4158878504672897, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.40186915887850466);
                POINTER.curveTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.38317757009345793, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.1308411214953271, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.curveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.1308411214953271, IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.38317757009345793, IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.397196261682243);
                POINTER.curveTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.4158878504672897, IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.curveTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.48130841121495327, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.49065420560747663, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5);
                POINTER.curveTo(IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5186915887850467, IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.5327102803738317, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5327102803738317);
                POINTER.curveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.5327102803738317, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5186915887850467, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5);
                POINTER.curveTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.49065420560747663, IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.48130841121495327, IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.closePath();
                POINTER_START = new Point2D.Double(0, POINTER.getBounds2D().getMinY());
                POINTER_STOP = new Point2D.Double(0, POINTER.getBounds2D().getMaxY());
                POINTER_FRACTIONS = new float[]{
                    0.0f,
                    0.3f,
                    0.6f,
                    1.0f
                };
                if (POINTER_COLOR != ColorDef.CUSTOM) {
                    POINTER_COLORS = new Color[]{
                        POINTER_COLOR.VERY_DARK,
                        POINTER_COLOR.MEDIUM,
                        POINTER_COLOR.MEDIUM,
                        POINTER_COLOR.VERY_DARK
                    };
                } else {
                    POINTER_COLORS = new Color[]{
                        CUSTOM_POINTER_COLOR.VERY_DARK,
                        CUSTOM_POINTER_COLOR.MEDIUM,
                        CUSTOM_POINTER_COLOR.MEDIUM,
                        CUSTOM_POINTER_COLOR.VERY_DARK
                    };
                }
                POINTER_GRADIENT = new LinearGradientPaint(POINTER_START, POINTER_STOP, POINTER_FRACTIONS, POINTER_COLORS);
                G2.setPaint(POINTER_GRADIENT);
                G2.fill(POINTER);
                G2.setColor(POINTER_COLOR.LIGHT);
                G2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
                G2.draw(POINTER);
                break;
        }

        G2.dispose();

        // Cache current parameters
        radWidth = WIDTH;
        radPointerType = POINTER_TYPE;
        radPointerColor = POINTER_COLOR;
        backgroundColor = BACKGROUND_COLOR;
        radCustomPointerColor = CUSTOM_POINTER_COLOR;

        return radPointerImage;
    }

    /**
     * Creates the pointer shadow image for a centered radial gauge.
     * The image parameters and the image will be cached. If the
     * current request has the same parameters as the last request
     * it will return the already created image instead of creating
     * a new image.
     * @param WIDTH
     * @param POINTER_TYPE
     * @return a buffered image that contains the pointer shadow image for a centered radial gauge
     
    public BufferedImage createStandardPointerShadow(final int WIDTH, final PointerType POINTER_TYPE) {
        if (WIDTH <= 0) {
            return UTIL.createImage(1, 1, Transparency.TRANSLUCENT);
        }

        if (radWidthShadow == WIDTH && radPointerTypeShadow == POINTER_TYPE) {
            return radPointerShadowImage;
        }

        final Color SHADOW_COLOR = new Color(0.0f, 0.0f, 0.0f, 0.65f);

        radPointerShadowImage.flush();
        radPointerShadowImage = UTIL.createImage(WIDTH, WIDTH, Transparency.TRANSLUCENT);
        final Graphics2D G2 = radPointerShadowImage.createGraphics();
        G2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        G2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        //G2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //G2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        //G2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        final int IMAGE_WIDTH = radPointerShadowImage.getWidth();
        final int IMAGE_HEIGHT = radPointerShadowImage.getHeight();

        final GeneralPath POINTER;

        switch (POINTER_TYPE) {
            case TYPE1:
                POINTER = new GeneralPath();
                POINTER.setWindingRule(Path2D.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.curveTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.4158878504672897, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.40186915887850466);
                POINTER.curveTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.38317757009345793, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.1308411214953271, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.curveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.1308411214953271, IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.38317757009345793, IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.397196261682243);
                POINTER.curveTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.4158878504672897, IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.curveTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.48130841121495327, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.49065420560747663, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5);
                POINTER.curveTo(IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5186915887850467, IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.5327102803738317, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5327102803738317);
                POINTER.curveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.5327102803738317, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5186915887850467, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5);
                POINTER.curveTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.49065420560747663, IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.48130841121495327, IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;

            case TYPE2:
                POINTER = new GeneralPath();
                POINTER.setWindingRule(Path2D.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.lineTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.46261682242990654);
                POINTER.lineTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.3411214953271028);
                POINTER.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.3411214953271028);
                POINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.46261682242990654);
                POINTER.lineTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.curveTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.4719626168224299, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.49065420560747663, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5);
                POINTER.curveTo(IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5186915887850467, IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.5327102803738317, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5327102803738317);
                POINTER.curveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.5327102803738317, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5186915887850467, IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5);
                POINTER.curveTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.49065420560747663, IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.4719626168224299, IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;

            case TYPE3:

                break;

            case TYPE4:
                POINTER = new GeneralPath();
                POINTER.setWindingRule(Path2D.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.1261682242990654);
                POINTER.lineTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.13551401869158877);
                POINTER.lineTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5);
                POINTER.lineTo(IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.602803738317757);
                POINTER.lineTo(IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.602803738317757);
                POINTER.lineTo(IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5);
                POINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.13551401869158877);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.1261682242990654);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;

            case TYPE5:
                POINTER = new GeneralPath();
                POINTER.setWindingRule(Path2D.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.4953271028037383);
                POINTER.lineTo(IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.4953271028037383);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.14953271028037382);
                POINTER.lineTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.4953271028037383);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.4953271028037383);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;

            case TYPE6:
                POINTER = new GeneralPath();
                POINTER.setWindingRule(Path2D.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.48598130841121495);
                POINTER.lineTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.3925233644859813);
                POINTER.lineTo(IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.3177570093457944);
                POINTER.lineTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.3177570093457944);
                POINTER.lineTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.3878504672897196);
                POINTER.lineTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.48598130841121495);
                POINTER.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.48598130841121495);
                POINTER.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.3878504672897196);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.3177570093457944);
                POINTER.lineTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.3925233644859813);
                POINTER.lineTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.48598130841121495);
                POINTER.lineTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.48598130841121495);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;

            case TYPE7:
                POINTER = new GeneralPath();
                POINTER.setWindingRule(Path2D.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.5);
                POINTER.lineTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.5);
                POINTER.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;

            case TYPE8:
                POINTER = new GeneralPath();
                POINTER.setWindingRule(Path2D.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5327102803738317);
                POINTER.lineTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5);
                POINTER.curveTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5, IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.14953271028037382);
                POINTER.curveTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.45794392523364486, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5);
                POINTER.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5327102803738317);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;

            case TYPE9:
                POINTER = new GeneralPath();
                POINTER.setWindingRule(Path2D.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.2336448598130841);
                POINTER.lineTo(IMAGE_WIDTH * 0.5046728971962616, IMAGE_HEIGHT * 0.2336448598130841);
                POINTER.lineTo(IMAGE_WIDTH * 0.514018691588785, IMAGE_HEIGHT * 0.4392523364485981);
                POINTER.lineTo(IMAGE_WIDTH * 0.48598130841121495, IMAGE_HEIGHT * 0.4392523364485981);
                POINTER.lineTo(IMAGE_WIDTH * 0.4953271028037383, IMAGE_HEIGHT * 0.2336448598130841);
                POINTER.closePath();
                POINTER.moveTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.lineTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.5280373831775701);
                POINTER.curveTo(IMAGE_WIDTH * 0.4719626168224299, IMAGE_HEIGHT * 0.5280373831775701, IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.602803738317757, IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.602803738317757);
                POINTER.curveTo(IMAGE_WIDTH * 0.4766355140186916, IMAGE_HEIGHT * 0.6074766355140186, IMAGE_WIDTH * 0.48130841121495327, IMAGE_HEIGHT * 0.6074766355140186, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.6074766355140186);
                POINTER.curveTo(IMAGE_WIDTH * 0.5186915887850467, IMAGE_HEIGHT * 0.6074766355140186, IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.6074766355140186, IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.602803738317757);
                POINTER.curveTo(IMAGE_WIDTH * 0.5233644859813084, IMAGE_HEIGHT * 0.602803738317757, IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.5280373831775701, IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.5280373831775701);
                POINTER.lineTo(IMAGE_WIDTH * 0.5280373831775701, IMAGE_HEIGHT * 0.4719626168224299);
                POINTER.lineTo(IMAGE_WIDTH * 0.5093457943925234, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.lineTo(IMAGE_WIDTH * 0.49065420560747663, IMAGE_HEIGHT * 0.1308411214953271);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;

            case TYPE10:
                POINTER = new GeneralPath();
                POINTER.setWindingRule(Path2D.WIND_EVEN_ODD);
                POINTER.moveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.14953271028037382);
                POINTER.curveTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.14953271028037382, IMAGE_WIDTH * 0.4439252336448598, IMAGE_HEIGHT * 0.49065420560747663, IMAGE_WIDTH * 0.4439252336448598, IMAGE_HEIGHT * 0.5);
                POINTER.curveTo(IMAGE_WIDTH * 0.4439252336448598, IMAGE_HEIGHT * 0.5327102803738317, IMAGE_WIDTH * 0.4672897196261682, IMAGE_HEIGHT * 0.5560747663551402, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5560747663551402);
                POINTER.curveTo(IMAGE_WIDTH * 0.5327102803738317, IMAGE_HEIGHT * 0.5560747663551402, IMAGE_WIDTH * 0.5560747663551402, IMAGE_HEIGHT * 0.5327102803738317, IMAGE_WIDTH * 0.5560747663551402, IMAGE_HEIGHT * 0.5);
                POINTER.curveTo(IMAGE_WIDTH * 0.5560747663551402, IMAGE_HEIGHT * 0.49065420560747663, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.14953271028037382, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.14953271028037382);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;

            case TYPE11:
                POINTER = new GeneralPath();
                POINTER.setWindingRule(Path2D.WIND_EVEN_ODD);
                POINTER.moveTo(0.5 * IMAGE_WIDTH, 0.16822429906542055 * IMAGE_HEIGHT);
                POINTER.lineTo(0.48598130841121495 * IMAGE_WIDTH, 0.5 * IMAGE_HEIGHT);
                POINTER.curveTo(0.48598130841121495 * IMAGE_WIDTH, 0.5 * IMAGE_HEIGHT, 0.48130841121495327 * IMAGE_WIDTH, 0.5841121495327103 * IMAGE_HEIGHT, 0.5 * IMAGE_WIDTH, 0.5841121495327103 * IMAGE_HEIGHT);
                POINTER.curveTo(0.514018691588785 * IMAGE_WIDTH, 0.5841121495327103 * IMAGE_HEIGHT, 0.5093457943925234 * IMAGE_WIDTH, 0.5 * IMAGE_HEIGHT, 0.5093457943925234 * IMAGE_WIDTH, 0.5 * IMAGE_HEIGHT);
                POINTER.lineTo(0.5 * IMAGE_WIDTH, 0.16822429906542055 * IMAGE_HEIGHT);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;

            case TYPE12:
                POINTER = new GeneralPath();
                POINTER.setWindingRule(Path2D.WIND_EVEN_ODD);
                POINTER.moveTo(0.5 * IMAGE_WIDTH, 0.16822429906542055 * IMAGE_HEIGHT);
                POINTER.lineTo(0.48598130841121495 * IMAGE_WIDTH, 0.5 * IMAGE_HEIGHT);
                POINTER.lineTo(0.5 * IMAGE_WIDTH, 0.5046728971962616 * IMAGE_HEIGHT);
                POINTER.lineTo(0.5093457943925234 * IMAGE_WIDTH, 0.5 * IMAGE_HEIGHT);
                POINTER.lineTo(0.5 * IMAGE_WIDTH, 0.16822429906542055 * IMAGE_HEIGHT);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;

            case TYPE13:
                POINTER = new GeneralPath();
                POINTER.setWindingRule(Path2D.WIND_EVEN_ODD);
                POINTER.moveTo(0.48598130841121495 * IMAGE_WIDTH, 0.16822429906542055 * IMAGE_HEIGHT);
                POINTER.lineTo(0.5 * IMAGE_WIDTH, 0.1308411214953271 * IMAGE_HEIGHT);
                POINTER.lineTo(0.5093457943925234 * IMAGE_WIDTH, 0.16822429906542055 * IMAGE_HEIGHT);
                POINTER.lineTo(0.5093457943925234 * IMAGE_WIDTH, 0.5093457943925234 * IMAGE_HEIGHT);
                POINTER.lineTo(0.48598130841121495 * IMAGE_WIDTH, 0.5093457943925234 * IMAGE_HEIGHT);
                POINTER.lineTo(0.48598130841121495 * IMAGE_WIDTH, 0.16822429906542055 * IMAGE_HEIGHT);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;
            case TYPE14:
                POINTER = new GeneralPath();
                POINTER.setWindingRule(Path2D.WIND_EVEN_ODD);
                POINTER.moveTo(0.48598130841121495 * IMAGE_WIDTH, 0.16822429906542055 * IMAGE_HEIGHT);
                POINTER.lineTo(0.5 * IMAGE_WIDTH, 0.1308411214953271 * IMAGE_HEIGHT);
                POINTER.lineTo(0.5093457943925234 * IMAGE_WIDTH, 0.16822429906542055 * IMAGE_HEIGHT);
                POINTER.lineTo(0.5093457943925234 * IMAGE_WIDTH, 0.5093457943925234 * IMAGE_HEIGHT);
                POINTER.lineTo(0.48598130841121495 * IMAGE_WIDTH, 0.5093457943925234 * IMAGE_HEIGHT);
                POINTER.lineTo(0.48598130841121495 * IMAGE_WIDTH, 0.16822429906542055 * IMAGE_HEIGHT);
                POINTER.closePath();
                G2.setColor(SHADOW_COLOR);
                G2.fill(POINTER);
                break;

            default:

                break;
        }

        G2.dispose();

        // Cache current parameters
        radWidthShadow = WIDTH;
        radPointerTypeShadow = POINTER_TYPE;

        return radPointerShadowImage;
    }

    @Override
    public String toString() {
        return "PointerImageFactory";
    }
    */

   // ------------------------------------
   private void drawTheIndicator(Path pathIndicatore) {
	   RectF area = new RectF();
	   area.set(0, 0, larghezza, lunghezza);
	   mat = new Material(matType, area, color1, color2, pennello);
	   // il contesto su cui disegnare
	   pointerImage = Bitmap.createBitmap(larghezza, lunghezza, Config.ARGB_8888);
	   tela = new Canvas(pointerImage);
	   // colora 
	   pathIndicatore.setFillType(FillType.WINDING);
	   tela.clipPath(pathIndicatore, Op.REPLACE);
	   tela.drawPaint(pennello);
	   tela.clipRect(0, 0, larghezza, lunghezza, Op.REPLACE);
	   tela.drawPath(pathIndicatore, matita);

	   // adesso fa l'ombra
	   pointerShadowImage = Bitmap.createBitmap(larghezza, lunghezza, Config.ARGB_8888);
	   tela = new Canvas(pointerShadowImage);
	   pennello.setARGB(Dash.SHADOWOPACITY, Dash.SHADOWGRAY, Dash.SHADOWGRAY, Dash.SHADOWGRAY);
	   pennello.setShader(null);
	   pathIndicatore.setFillType(FillType.WINDING);
	   tela.clipPath(pathIndicatore, Op.REPLACE);
	   tela.drawPaint(pennello);
	   return;
	}
   
   
   private void creaPennelli(){
       // Crea i pennelli
		PathEffect effect = new PathEffect();
		Xfermode xfermode = new Xfermode();
		float miter = 0.1f;
		pennello.setAntiAlias(true);
		pennello.setDither(true);
		pennello.setPathEffect(effect);
		pennello.setStrokeCap(Cap.SQUARE);
		pennello.setStrokeJoin(Join.ROUND);
		pennello.setStrokeMiter(miter);
		pennello.setStyle(Style.FILL_AND_STROKE);
		pennello.setXfermode(xfermode);
		pennello.setAlpha(opacity);

		matita.setAntiAlias(true);
		matita.setDither(true);
		matita.setPathEffect(effect);
		matita.setStrokeCap(Cap.SQUARE);
		matita.setStrokeJoin(Join.ROUND);
		matita.setStrokeMiter(0.5f);
		matita.setStyle(Style.STROKE);
		matita.setXfermode(xfermode);
		matita.setARGB(opacity-100, Dash.OUTLINEGRAY, Dash.OUTLINEGRAY, Dash.OUTLINEGRAY);
		return;
   }

   
   
   
}


