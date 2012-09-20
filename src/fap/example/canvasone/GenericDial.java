package fap.example.canvasone;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path.FillType;
import android.graphics.Region.Op;

public class GenericDial {

	// Opacità dello strumento
	private int opacity = Dash.OPACITY;

	// Contesto
	private Canvas tela;
	private static Bitmap displayImage;
	private static Paint pennello;
	private static Paint matita;
	
	// Proprietà
	protected int larghezza = 100;
	protected int altezza = 100;
	protected float centerX = 50;
	protected float centerY = 50;
	protected DisplayBackGround tipoSfondo = DisplayBackGround.BLACK;;

	// variabili
	private File backGroundFile = new File(Dash.DEFAULT_BACKGROUND_NAME);
	private int foreCol = Color.BLACK;
	
	protected Path pathDial = new Path();
	protected Path pathDialShadow = new Path();
	protected RectF recDialSurface = new RectF();
	
	boolean isInvalid = false;
	
	Etichetta titolo = null;
	
	// Costruttore della classe
	public GenericDial(GenericFrame FRAME) {
		createDial(FRAME);
	}
		
	public GenericDial(GenericFrame FRAME, DisplayBackGround TYPE) {
		createDial(FRAME);
		this.setDialType(TYPE);
		return;
	}
		
	private void createDial(GenericFrame FRAME) {		

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
        matita.setARGB(opacity, Dash.OUTLINEGRAY, Dash.OUTLINEGRAY, Dash.OUTLINEGRAY);

		// Cache current parameters
        this.reformatDial(FRAME);
        
        return;
        
	}
	
	public String getBackGroundFileName() {
		return(backGroundFile.getPath() );
	}
	
	public void setBackGroundFileName(String FILENAME) {
		backGroundFile = new File(FILENAME);
		if( !backGroundFile.exists()) {
			backGroundFile = new File(Dash.DEFAULT_BACKGROUND_NAME);
		}
        // Invalidate the BitmapCache
        this.isInvalid = true;
	}
		
	public DisplayBackGround getDialType() {
		return(tipoSfondo);
	};
	public void setDialType( DisplayBackGround TYPE) {
		if( tipoSfondo != TYPE) {
			tipoSfondo = TYPE;
	        // Invalidate the BitmapCache
	        this.isInvalid = true;
		}
		return;
	}
	public String getDialTitle() {
		if(titolo != null)
			return(titolo.contenuto);
		else
			return("");
	}
	public void setDialTitle(String TEXT) {
		titolo = new Etichetta(TEXT, _calculateTitleArea());
		return;
	}
	
	private RectF _calculateTitleArea() {
		RectF area = new RectF();
		float larg = recDialSurface.width() * Dash.DIALTITLEWIDTH_PER;
		float alt = recDialSurface.height() * Dash.DIALTITLEEIGHT_PER;
		
		area.left = recDialSurface.centerX() - (larg / 2.0f);
		area.right = recDialSurface.centerX() + (larg / 2.0f);
		area.top = recDialSurface.centerY() - (2 * alt);  
		area.bottom = recDialSurface.centerY() - alt;  
		return(area);
	}
	
	protected void reformatDial(GenericFrame FRAME) {

		larghezza = (FRAME.larghezza <= 0) ? 1 : FRAME.larghezza;
		altezza = (FRAME.altezza <= 0) ? 1 : FRAME.altezza;
		centerX = FRAME.centerX;
		centerY = FRAME.centerY;

        // Calculate dimension
		recDialSurface = FRAME.recInnerFrame;

		pathDial.reset();
        pathDial.addPath(FRAME.pathIntFrame);
        pathDial.close();
        
        pathDialShadow.reset();
        pathDialShadow.addPath(FRAME.pathInShadowFrame);
        pathDialShadow.close();

        // reformat the title
        if(titolo != null) titolo.area =  _calculateTitleArea();

        
        // Invalidate the BitmapCache
        
        this.isInvalid = true;
        
        return;
		
	}
	
	
	// Metodo principale per la Draw del Display
	public Bitmap drawDial(boolean forceRedraw) {

        // Take image from cache 
        if (!this.isInvalid && !forceRedraw) {
            return displayImage;
        } else {
        	this.isInvalid = false;
        }
        // -----------------------------
        
        // Crea la nuova bitmap
    	displayImage = Bitmap.createBitmap(larghezza , altezza, Config.ARGB_8888);

    	// ed il contesto su cui disegnare
        Canvas tela = new Canvas(displayImage);
		pennello.setAlpha(opacity);
        matita.setAlpha(opacity);
        
        // costruisce il Path per la texture dei materiali
        buildTheShader(tipoSfondo);
        
		// Draw 
        pathDial.setFillType(FillType.WINDING);
        tela.clipPath(pathDial, Op.REPLACE);
        tela.drawPaint(pennello);
		
        // Draw the shadow
    	pennello.setShader(null);
    	pennello.setARGB(Dash.SHADOWOPACITY, Dash.SHADOWGRAY, Dash.SHADOWGRAY, Dash.SHADOWGRAY);
    	pathDialShadow.setFillType(FillType.WINDING);
    	tela.clipPath(pathDialShadow, Op.REPLACE);
    	tela.drawPaint(pennello);

    	// Draw the Title....
    	if(titolo != null) {
    		tela.clipRect(recDialSurface, Op.REPLACE);
    		titolo.drawEtichetta(tela);
    	}
    	
/*
    	pathDisplayFrame.reset();
    	pathDisplayFrame.addPath(FRAME.pathIntFrame);
    	tela.clipPath(pathDisplayFrame, Op.REPLACE);
    	// Draw the axis
    	Point appoggio = new Point();
    	for(int i=0;i<Axis.getNumberOfAxis();i++){
    		Axis.drawAxe(i, FRAME.recInnerFrame, tela, matita);
    	}
*/    	
    	
		return displayImage;
	}

		

	// Costruisce lo shader per il pennello in funzione del materiale
    public int buildTheShader(DisplayBackGround BACKGROUND) {
        
        switch (BACKGROUND) {
    	case BLACK:
    		pennello.setARGB(opacity, 0, 0, 0);
    		break;

    	case WHITE:
    		pennello.setARGB(opacity, 250, 250, 250);
    		break;

    	case IVORY:
    		pennello.setARGB(opacity, 240, 230, 200);
    		break;
    		
    	case GRAY:
    		pennello.setARGB(opacity, 69, 69, 69);
    		break;
    		
    	case PICTURE:
    		BitmapFactory bmp = new BitmapFactory();
            BitmapShader shaAppo = new BitmapShader(bmp.decodeFile(backGroundFile.getPath()), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
    		pennello.setShader(shaAppo);
    		break;
    	}
        return(0);
    }
	
    // Ottiene il colore associato al tipo di sfondo
    public int getColorFromType(int FORECOL, int BACKCOL)  {
    	int response;
        switch (tipoSfondo) {
    	case BLACK:
    		FORECOL = Color.argb(opacity, 255, 255, 255);
    		BACKCOL= Color.argb(opacity, 255, 255, 255);
    		break;

    	case WHITE:
    		FORECOL = Color.argb(opacity, 10, 10, 10);
    		BACKCOL= Color.argb(opacity, 250, 250, 250);
    		break;

    	case IVORY:
    		FORECOL = Color.argb(opacity, 15, 25, 55);
    		BACKCOL= Color.argb(opacity, 240, 230, 200);
    		break;
    		
    	case GRAY:
    		FORECOL = Color.argb(opacity, 186, 186, 186);
    		BACKCOL= Color.argb(opacity, 69, 69, 69);
    		break;
    		
    	case PICTURE:
    		FORECOL = Color.argb(opacity, 186, 186, 186);
    		BACKCOL= Color.argb(opacity, 0, 0, 0);
    		break;
    	}
        return(BACKCOL);
    	
    }
}
