package fap.example.canvasone;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class SevenSegment {

	private int step;
	private int digitWidth ;
	private int digitHeight;
	private RectF recDigits;
	
	private Path pathSegH;
	private Path pathSegV;
	private Path pathDot;

	private Point trasl[];
	private int typ[];
	private int conf[];
	
	public SevenSegment(final RectF DISPLAYAREA, final int DIGITS, final int DECIMAL ){

		// Digit geometry
    	int nDigits = DIGITS + (DECIMAL>0 ? DECIMAL : 0);
    	float digw = DISPLAYAREA.width() / (float)nDigits;
    	float alt = DISPLAYAREA.height();
    	float digh;
    
    	if(alt / digw < 1.68f) { digw = alt / 1.68f; digh = digw * 1.68f; }
    	else { digh = digw * 1.68f; };  
    
    	digitWidth = (int)digw;
    	digitHeight = (int)digh;
    
    	recDigits = new RectF();
    	recDigits.left = DISPLAYAREA.left + (DISPLAYAREA.width() - digw * nDigits) / 2.0f;
    	recDigits.top = DISPLAYAREA.top + (alt - digh) / 2.0f;
    	recDigits.right = DISPLAYAREA.left + digw * nDigits;
    	recDigits.bottom = DISPLAYAREA.top + digh;

		step = (int)(digw / 15.0f);
    
		pathSegH = new Path();
		pathSegH.moveTo(0, step);
		pathSegH.lineTo(step, 2*step);
		pathSegH.lineTo(7*step, 2*step);
		pathSegH.lineTo(8*step, step);
    	pathSegH.lineTo(7*step, 0);
    	pathSegH.lineTo(step, 0);
    	pathSegH.close();

    	pathSegV = new Path();
    	pathSegV.moveTo(2*step, 0);
    	pathSegV.lineTo(step, step);
    	pathSegV.lineTo(0, 10*step);
    	pathSegV.lineTo(step, 11*step);
    	pathSegV.lineTo(2*step, 10*step);
    	pathSegV.lineTo(3*step, 1);
    	pathSegH.close();
    
    	pathDot = new Path();
    	pathDot.moveTo(step, 0);
    	pathDot.lineTo(0, step);
    	pathDot.lineTo(step, 2*step);
    	pathDot.lineTo(2*step, step);
    	pathDot.close();
    
    	trasl = new Point[8];
    	trasl[0].x = step ; trasl[0].y = step;
    	trasl[1].x = 0 ; trasl[1].y = 12*step;
    	trasl[2].x = 2*step ; trasl[2].y = 22*step;
    	trasl[3].x = 10*step ; trasl[3].y = 12*step;
    	trasl[4].x = 11*step ; trasl[4].y = step;
    	trasl[5].x = 4*step ; trasl[5].y = 0;
    	trasl[6].x = 3*step ; trasl[6].y = 11*step;
    	trasl[7].x = 12*step ; trasl[7].y = 22*step;

    	typ = new int[8];
    	typ[0] = 1;
    	typ[1] = 1;
    	typ[2] = 2;        
    	typ[3] = 1;        
    	typ[4] = 1;
    	typ[5] = 2;
    	typ[6] = 2;
    	typ[7] = 3;

    	conf = new int[10];
    	conf[0] = 0x3F;
    	conf[1] = 0x18;
    	conf[2] = 0x76;        
    	conf[3] = 0x7C;        
    	conf[4] = 0x59;
    	conf[5] = 0x6D;
    	conf[6] = 0x6F;
    	conf[7] = 0x38;
    	conf[8] = 0x7F;
    	conf[9] = 0x7D;
    	
	}
	
	public void drawValue(Canvas tela, Paint penON, Paint penOFF, String value) {
    
		int basex = (int)recDigits.left;
		int basey = (int)recDigits.top;
    
        int dig; 
        int map;
        int i,j,n;
        float cifrax,cifray;

        cifray = 0;
        n = value.length();
        for(j=0;j<n;j++) {
        	dig = Integer.parseInt( value.substring(j, j+1));
        	map = conf[dig];
        	cifrax = digitWidth * j;
        
        	for(i=0;i<8;i++) {
        		tela.translate(basex + cifrax + trasl[i].x, basey + cifray + trasl[i].y);
        		switch(typ[i]){
        		case 1:
        			tela.drawPath(pathSegV, ((0 >> map) == 1) ? penON : penOFF);	
        			break;
        		case 2:
        			tela.drawPath(pathSegH, ((0 >> map) == 1) ? penON : penOFF);	
        			break;
        		case 3:
        			tela.drawPath(pathDot, ((0 >> map) == 1) ? penON : penOFF);	
        			break;
        		}
        	}
        }
        return;
	}
	
	
}
