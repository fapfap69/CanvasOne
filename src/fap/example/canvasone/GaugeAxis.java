package fap.example.canvasone;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class GaugeAxis {

	static final int MAXNUMAXIS = 6;
	
	
	int numberOfAxis = 0;
	Point[] axisCoords = new Point[MAXNUMAXIS];
	GaugeAxePosition[] axisTypes = new GaugeAxePosition[MAXNUMAXIS]; 
	Etichetta[] titolo = new Etichetta[MAXNUMAXIS]; 
	Etichetta[] unita = new Etichetta[MAXNUMAXIS]; 

	// Costruttore
	public void GaugeAxis(){
		numberOfAxis = 0;
	}	
	public int getNumberOfAxis() {
		return numberOfAxis;
	}
	public GaugeAxePosition getAxe(final int AXE, final RectF DISPLAYAREA, Point AXECOORD) {
		if(AXE > (numberOfAxis -1)) return(GaugeAxePosition.NONE);
		calculateAxePosition(axisTypes[AXE], DISPLAYAREA, AXECOORD);
		return(axisTypes[AXE]);
	}
	public void addAxe(final GaugeAxePosition AXETYPE, final String AXENAME, final String AXEUNIT) {
		axisCoords[numberOfAxis] = new Point();
		axisTypes[numberOfAxis] = AXETYPE;
//		unita[numberOfAxis] = new Etichetta();
//		unita[numberOfAxis].contenuto = AXEUNIT;
		numberOfAxis++;
		return;
	}
	public void calculateAxePosition(final GaugeAxePosition AXETYPE, RectF DISPLAYAREA, Point PUNTO){
		double radius = 0.0d;
		double angle = 0.0d;
		
		switch(AXETYPE){
		case CENTER:
			radius = 0.0d;
			angle = 0.0d;
			break;
		case TOP:
			radius = DISPLAYAREA.width() * 0.35f;
			angle = Math.PI * 0.50d;
			break;
		case BOTTOM:
			radius = DISPLAYAREA.width() * 0.35f;
			angle = Math.PI * 1.5d;
			break;
		case LEFT:
			radius = DISPLAYAREA.width() * 0.35f;
			angle = Math.PI;
			break;
		case RIGHT:
			radius = DISPLAYAREA.width() * 0.35f;
			angle = 0.0d;
			break;
		case TOPLEFT:
			radius = DISPLAYAREA.width() * 0.35f;
			angle = Math.PI * 0.75d;
			break;
		case BOTTOMLEFT:
			radius = DISPLAYAREA.width() * 0.35f;
			angle = Math.PI * 1.25d;
			break;
		case TOPRIGHT:
			radius = DISPLAYAREA.width() * 0.35f;
			angle = Math.PI * 0.25d;
			break;
		case BOTTOMRIGHT:
			radius = DISPLAYAREA.width() * 0.35f;
			angle = Math.PI * 1.75d;
			break;
		case INNERTOPLEFT:
			radius = DISPLAYAREA.width() * 0.20f;
			angle = Math.PI * 0.75d;
			break;
		case INNERBOTTOMLEFT:
			radius = DISPLAYAREA.width() * 0.20f;
			angle = Math.PI * 1.25d;
			break;
		case INNERTOPRIGHT:
			radius = DISPLAYAREA.width() * 0.20f;
			angle = Math.PI * 0.25d;
			break;
		case INNERBOTTOMRIGHT:
			radius = DISPLAYAREA.width() * 0.20f;
			angle = Math.PI * 1.75d;
			break;
		}
		PUNTO.x = DISPLAYAREA.centerX() + (float)(radius*Math.cos(angle));
		PUNTO.y = DISPLAYAREA.centerY() + (float)(radius*Math.sin(angle));
		return;
	}
	
	public void calculateTitlePosition(final GaugeAxePosition AXETYPE, final Point CENTER, Point PUNTO){
		switch(AXETYPE){
		case CENTER:
			PUNTO.x = CENTER.x;
			PUNTO.y = CENTER.y;
			break;
		case TOP:
			PUNTO.x = CENTER.x;
			PUNTO.y = CENTER.y;
			break;
		case BOTTOM:
			PUNTO.x = CENTER.x;
			PUNTO.y = CENTER.y;
			break;
		case LEFT:
			PUNTO.x = CENTER.x;
			PUNTO.y = CENTER.y;
			break;
		case RIGHT:
			PUNTO.x = CENTER.x;
			PUNTO.y = CENTER.y;
			break;
		case TOPLEFT:
			PUNTO.x = CENTER.x;
			PUNTO.y = CENTER.y;
			break;
		case BOTTOMLEFT:
			PUNTO.x = CENTER.x;
			PUNTO.y = CENTER.y;
			break;
		case TOPRIGHT:
			PUNTO.x = CENTER.x;
			PUNTO.y = CENTER.y;
			break;
		case BOTTOMRIGHT:
			PUNTO.x = CENTER.x;
			PUNTO.y = CENTER.y;
			break;
		case INNERTOPLEFT:
			PUNTO.x = CENTER.x;
			PUNTO.y = CENTER.y;
			break;
		case INNERBOTTOMLEFT:
			PUNTO.x = CENTER.x;
			PUNTO.y = CENTER.y;
			break;
		case INNERTOPRIGHT:
			PUNTO.x = CENTER.x;
			PUNTO.y = CENTER.y;
			break;
		case INNERBOTTOMRIGHT:
			PUNTO.x = CENTER.x;
			PUNTO.y = CENTER.y;
			break;
		}
		return;
		
	}
	
	
	public void drawAxe(final int AXE, final RectF DISPLAYAREA, Canvas tela, Paint matita) {
		if(AXE > (numberOfAxis -1)) return;
		
		Point  centro = new Point();
		calculateAxePosition(axisTypes[AXE], DISPLAYAREA, centro);
	
		tela.drawLine(centro.x-4, centro.y, centro.x+4, centro.y, matita);
		tela.drawLine(centro.x, centro.y-4, centro.x, centro.y+4, matita);
		tela.drawCircle(centro.x, centro.y, 3, matita);
		
		Point punt = new Point();
		calculateTitlePosition(axisTypes[AXE], centro, punt);
		
		return;
	}

	
	
}
