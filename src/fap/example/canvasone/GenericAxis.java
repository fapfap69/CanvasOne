package fap.example.canvasone;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Region.Op;

public class GenericAxis {

	// Opacità dello strumento
	private int opacity = Dash.OPACITY;

	// Contesto
	private Canvas tela;
	private static Bitmap axisImage;
	private static Paint matita;
	
	// Scala
	private Etichetta unita; 
	
	private float startAngle = 0.0f;
	private float endAngle = 360.0f;
	
	private float scaleMin = 0.0f;
	private float scaleMax = 100.0f;
	private float scalePriDiv = 10.0f;
	private float scaleSecDiv = 1.0f;
	
	private float scaleRadius = 10.0f;
	
	private float scaleScaleParam = 1.0f;
	
	private boolean hasScale = false;
	private boolean isLogaritmic = false;
	private boolean isExternal = false;
	private boolean hasLabels = false;
	private boolean isRotated = true;
	
	private String scaleLabelFormat = "%3.0f";
	
	private float[] scaleRangesValues = new float[5];
	private int[] scaleRangesColors = new int[5];
	
	// Grafica
	private GaugeAxePosition axisPositionType;
	private boolean hasLinea = false;
	private boolean hasEndMarks = false;
	private Point axisPosition = new Point();
	private RectF displayBitmap = new RectF();
	private RectF displayArea = new RectF();
	private int scaleColor = Color.BLACK;
	private int scaleFontSize = 10;
	
	private boolean isInvalid = false;
	
	// Dinamica
	private float inertia = 1.0f;
	
	
	// Metodi ..................
	
	// Costruttore x tipo 
	public GenericAxis(GenericFrame FRAME, GaugeAxePosition POSITIONTYPE) {
		displayBitmap.set(FRAME.recBoundary);
		displayArea.set(FRAME.recInnerFrame);
		hasScale = axisPositionBuild(POSITIONTYPE, 0.0f, 0.0f, axisPosition);
		formatAxis();
	}
	public GenericAxis(GenericFrame FRAME, GaugeAxePosition POSITIONTYPE, float X, float Y) {
		displayBitmap.set(FRAME.recBoundary);
		displayArea.set(FRAME.recInnerFrame);
		hasScale = axisPositionBuild(POSITIONTYPE, X,Y, axisPosition);
		formatAxis();
	}

	// setta la scala
	public void setScale(float STARTANGLE, float ENDANGLE, float RADIUS, float MIN, float MAX, 
						 float PRIMDIVISION, float SECODIVISION, int COLOR, int FONTSIZE,
						 boolean HASLINEA, boolean HASLABEL,
						 boolean ISEXTERNAL, boolean ISROTATED, boolean ISLOGARITMIC){
		
		startAngle = STARTANGLE;
		endAngle = ENDANGLE;
		
		scaleRadius = RADIUS;
		scaleMin = Math.min(MAX, MIN);
		scaleMax = Math.max(MIN, MAX);
		
		scalePriDiv = (PRIMDIVISION <= 0) ? -1 : PRIMDIVISION;
		scaleSecDiv = (SECODIVISION <= 0) ? -1 : SECODIVISION;
				
		hasLinea = HASLINEA;
		isExternal = ISEXTERNAL;
		hasLabels = HASLABEL;
		isRotated = ISROTATED;
		isLogaritmic = ISLOGARITMIC;
		
		scaleColor = COLOR;
		scaleFontSize = FONTSIZE;
		
		hasScale = (scaleRadius<=0 || scaleMin==scaleMax || endAngle<=0 || !hasScale) ? false : true;

		formatAxis();
		
		this.isInvalid = true;
		
		return;
	}
	
	public void setUnit(String UNIT){
		unita = new Etichetta(UNIT, unitPositionBuild(axisPosition));
		this.isInvalid = true;
		
		
		return;
	}
	
	
	// Calcula e setta
	private void formatAxis(){
		
		// Parametri di scala
		double arcRad = (double)(360 + endAngle - startAngle) * (Math.PI / 180.0d);
		double esc = isLogaritmic ? (Math.log10((double)scaleMax)-Math.log10((double)scaleMin)) : (double)(scaleMax - scaleMin);
		scaleScaleParam = (float)(arcRad / esc);
		
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
        matita.setARGB((int)((float)Color.alpha(scaleColor) * ((float)opacity / 255.0f)), Color.red(scaleColor), Color.green(scaleColor), Color.blue(scaleColor));

        this.isInvalid = true;
        
        return;
		
	}
	
	// Draw the Axis !!
	public Bitmap drawAxis(boolean forceRedraw) {

		// Take image from cache 
		if (!this.isInvalid && !forceRedraw) {
			return axisImage;
        } else {
        	this.isInvalid = false;
        }
        // -----------------------------i
	        
		// Crea la nuova bitmap
		axisImage = Bitmap.createBitmap((int)displayBitmap.width() , (int)displayBitmap.height(), Config.ARGB_8888);

		// No scale but bitmap void
		if(!hasScale) return(axisImage);

	    // ed il contesto su cui disegnare
	    tela = new Canvas(axisImage);

        
    	// Draw AxisCross
		tela.clipRect(displayArea, Op.REPLACE);
		tela.drawLine(axisPosition.x-8, axisPosition.y, axisPosition.x+8, axisPosition.y, matita);
		tela.drawLine(axisPosition.x, axisPosition.y-8, axisPosition.x, axisPosition.y+8, matita);
		tela.drawCircle(axisPosition.x, axisPosition.y, 6, matita);

		// Draw Scale line
		if(hasLinea) {
			RectF arco = new RectF();
			tela.clipRect(displayArea, Op.REPLACE);			
			arco.set(axisPosition.x - scaleRadius, axisPosition.y - scaleRadius, axisPosition.x + scaleRadius, axisPosition.y + scaleRadius);
			tela.drawArc(arco, startAngle, 360 + endAngle-startAngle, false, matita);
		}
		// Draw Graduazioni 
		tela.clipRect(displayArea, Op.REPLACE);
		drawScaleTick(tela, 1, scalePriDiv, Dash.SCALEPRIMARYDIV_PER);
		drawScaleTick(tela, 2, scaleSecDiv, Dash.SCALESECONDARYDIV_PER);

		
    	// Draw the Unit
    	if(unita != null) {
    		tela.clipRect(displayArea, Op.REPLACE);
    		unita.drawEtichetta(tela);
    	}
    	
    	// Draw the numeric scale
    	if(hasLabels){
    		tela.clipRect(displayArea, Op.REPLACE);
    		drawScaleLabels(tela);
    	}
   	
        return(axisImage);
        
	}
	
	// routine accessorie
	private boolean axisPositionBuild(final GaugeAxePosition POSITIONTYPE, final float X, final float Y, Point POSITION){

		double radius = 0.0d;
		double angle = 0.0d;
		
		axisPositionType = POSITIONTYPE;
		
		switch(axisPositionType){
		case CENTER:
			radius = 0.0d;
			angle = 0.0d;
			break;
		case TOP:
			radius = displayArea.width() * 0.35f;
			angle = Math.PI * 0.50d;
			break;
		case BOTTOM:
			radius = displayArea.width() * 0.35f;
			angle = Math.PI * 1.5d;
			break;
		case LEFT:
			radius = displayArea.width() * 0.35f;
			angle = Math.PI;
			break;
		case RIGHT:
			radius = displayArea.width() * 0.35f;
			angle = 0.0d;
			break;
		case TOPLEFT:
			radius = displayArea.width() * 0.35f;
			angle = Math.PI * 0.75d;
			break;
		case BOTTOMLEFT:
			radius = displayArea.width() * 0.35f;
			angle = Math.PI * 1.25d;
			break;
		case TOPRIGHT:
			radius = displayArea.width() * 0.35f;
			angle = Math.PI * 0.25d;
			break;
		case BOTTOMRIGHT:
			radius = displayArea.width() * 0.35f;
			angle = Math.PI * 1.75d;
			break;
		case INNERTOPLEFT:
			radius = displayArea.width() * 0.20f;
			angle = Math.PI * 0.75d;
			break;
		case INNERBOTTOMLEFT:
			radius = displayArea.width() * 0.20f;
			angle = Math.PI * 1.25d;
			break;
		case INNERTOPRIGHT:
			radius = displayArea.width() * 0.20f;
			angle = Math.PI * 0.25d;
			break;
		case INNERBOTTOMRIGHT:
			radius = displayArea.width() * 0.20f;
			angle = Math.PI * 1.75d;
			break;
		case CUSTOM:
			POSITION.x = (X>=displayArea.left && X<=displayArea.right) ? X : -1;
			POSITION.y = (Y>=displayArea.top && Y<=displayArea.bottom) ? Y : -1;
			return((X==-1 || Y==-1) ? false : true);
		case NONE:
			POSITION.x = 0;
			POSITION.y = 0;
			return(false);
		default:
			return(false);
		}
		POSITION.x = displayArea.centerX() + (float)(radius*Math.cos(angle));
		POSITION.y = displayArea.centerY() + (float)(radius*Math.sin(angle));
		return(true);
	}
		
	private RectF unitPositionBuild(Point AXISPOSITION) {		
		float lar = displayArea.width() * Dash.SCALEUNITWIDTH_PER;
		float alt = displayArea.height() * Dash.SCALEUNITEIGHT_PER;
		 
		RectF area = new RectF();
		
		switch(axisPositionType){
		case CENTER:
		case CUSTOM:
			area.set(axisPosition.x - lar/2.0f, axisPosition.y + alt, axisPosition.x + lar/2.0f, axisPosition.y +2.0f*alt);
			break;
		case TOP:
			area.set(axisPosition.x - lar/2.0f, axisPosition.y + alt, axisPosition.x + lar/2.0f, axisPosition.y +2.0f*alt);
			break;
		case BOTTOM:
			area.set(axisPosition.x - lar/2.0f, axisPosition.y - 2.0f*alt, axisPosition.x + lar/2.0f, axisPosition.y -alt);
			break;
		case LEFT:
			area.set(axisPosition.x + lar/2.0f, axisPosition.y - alt/2.0f, axisPosition.x + lar*1.5f, axisPosition.y + alt/2.0f);
			break;
		case RIGHT:
			area.set(axisPosition.x - lar*1.5f, axisPosition.y - alt/2.0f, axisPosition.x - lar/2.0f, axisPosition.y + alt/2.0f);
			break;
		case TOPLEFT:
		case INNERTOPLEFT:
			area.set(axisPosition.x, axisPosition.y + alt, axisPosition.x + lar, axisPosition.y + alt*2.0f);
			break;
		case BOTTOMLEFT:
		case INNERBOTTOMLEFT:
			area.set(axisPosition.x, axisPosition.y - alt*2.0f, axisPosition.x + lar, axisPosition.y - alt);
			break;
		case TOPRIGHT:
		case INNERTOPRIGHT:
			area.set(axisPosition.x - lar, axisPosition.y + alt, axisPosition.x, axisPosition.y + alt*2.0f);
			break;
		case BOTTOMRIGHT:
		case INNERBOTTOMRIGHT:
			area.set(axisPosition.x - lar, axisPosition.y - alt*2.0f, axisPosition.x, axisPosition.y - alt);
			break;
		case NONE:
		default:
			area.set(displayArea.centerX() - lar/2.0f, displayArea.centerY() - alt/2.0f, displayArea.centerX() + lar/2.0f, displayArea.centerY() + alt/2.0f);
			break;
		}		
		return(area);
	}
	
	private float convertValueToAngle(float VALUE){
		if(!isLogaritmic)
			return((float)((double)(VALUE-scaleMin) * scaleScaleParam + Math.toRadians(startAngle)));
		else
			return((float)(((VALUE==scaleMin) ? 0.0d : Math.log10((double)(VALUE-scaleMin)) ) * scaleScaleParam + Math.toRadians(startAngle)));
		
	}

	private void drawScaleTick(Canvas tela, int TYPE, float GRADUATION, float GRADUATIONDIMESION){
		
		// Draw Graduazioni
		if(GRADUATION >-1){
			float value = scaleMin;
			float incre = isLogaritmic ? (float)Math.log10(value) : 0.0f;
			float x1,x2,y1,y2;
			float disp = (isExternal ? scaleRadius * GRADUATIONDIMESION : -scaleRadius * GRADUATIONDIMESION );
			double ang;
			while(value <= scaleMax){
				ang = convertValueToAngle(value);
				x1 = (float) (scaleRadius * Math.cos(ang)) + axisPosition.x;
				y1 = (float) (scaleRadius * Math.sin(ang)) + axisPosition.y;
				x2 = (float) ((scaleRadius + disp) * Math.cos(ang)) + axisPosition.x;
				y2 = (float) ((scaleRadius + disp) * Math.sin(ang)) + axisPosition.y;
				tela.drawLine(x1, y1, x2, y2, matita);
				if(!isLogaritmic){
					value = scaleMin + GRADUATION * ++incre;
				} else {
					if(TYPE == 2){
						incre = incre + GRADUATION;
						if(incre > 10.0f) incre = + GRADUATION;
						value = (float)Math.pow(10.0d, Math.floor(Math.log10(value))) * incre;
						
					} else {
						incre += GRADUATION;
						value = (float)Math.pow(10,incre);
					}
				}
			}
		}
		return;
	}
	
	private void drawScaleLabels(Canvas tela){
		
		// Setta il normografo
		Paint normografo = new Paint();
		normografo.setAntiAlias(true);
		normografo.setTextAlign(Align.CENTER);
		normografo.setTextSize(scaleFontSize);
		normografo.setTypeface(Dash.LABEFONTTYPFACE);
		normografo.setColor(scaleColor);
		normografo.setStyle(Paint.Style.STROKE);
	
		float value = scaleMin;
		float incre = isLogaritmic ? (float)Math.log10(value) : 0.0f;
		float x1,x2,y1,y2;
		float disp = (isExternal ? scaleRadius *  Dash.SCALEPRIMARYDIV_PER + scaleFontSize/2.0f : -(scaleRadius *  Dash.SCALEPRIMARYDIV_PER + scaleFontSize/2.0f) );
		double ang;
		Rect bound = new Rect();
		String buffer = new String();
		
		while(value <= scaleMax){
			ang = convertValueToAngle(value);

			buffer = String.format("%.0f",value); // scaleLabelFormat, value);
			normografo.getTextBounds(buffer, 0, buffer.length()-1, bound);
			x2 = (float) ((scaleRadius + disp) * Math.cos(ang)) + axisPosition.x;
			y2 = (float) ((scaleRadius + disp) * Math.sin(ang)) + axisPosition.y;
			if(isRotated) {	
				tela.rotate( (float)Math.toDegrees(Math.PI/2.0d + ang), x2 , y2);
				tela.drawText(buffer, x2, y2, normografo);
				tela.rotate( -(float)Math.toDegrees(Math.PI/2.0d + ang), x2 , y2);
			} else {
			//	x2 = x2 - bound.centerX();
				y2 = y2 + bound.height() / 2.0f;
				tela.drawText(buffer, x2, y2, normografo);
			}
			if(!isLogaritmic){
				value = scaleMin + scalePriDiv * ++incre;
			} else {
				incre += scalePriDiv;
				value = (float)Math.pow(10,incre);
			}
		}
		return;
	}	

	private void drawAcross(Canvas tela, float x2, float y2, float dim, Paint matita, boolean pos) {
		if(pos) {
			tela.drawLine(x2-dim,y2,x2+dim,y2,matita);
			tela.drawLine(x2,y2-dim,x2,y2+dim,matita);
		} else {
			tela.drawLine(x2-dim,y2-dim,x2+dim,y2+dim,matita);
			tela.drawLine(x2+dim,y2-dim,x2-dim,y2+dim,matita);
		}
		return;
	}

}
