package fap.example.canvasone;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;

public class SevenSegment {

	private float step;
	private float digitWidth ;
	private float digitHeight;
	private RectF recDigits;
	
	private Path pathSegH;
	private Path pathSegV;
	private Path pathDot;

	private Point trasl[];
	private int typ[];
	private int conf[];
	
	public SevenSegment(final RectF DISPLAYAREA, final int DIGITS, final int DECIMAL ){

		// Digit geometry
    	int nDigits = DIGITS; 
    	// .... ?  (DECIMAL>0 ? DECIMAL : 0);
    	float digw = DISPLAYAREA.width() / (float)nDigits;
    	float alt = DISPLAYAREA.height();
    	float digh;
    	float rat = 1.68f;
    	if(alt / digw < rat) { digw = alt / rat; digh = digw * rat; }
    	else { digh = digw * rat; };  
    
    	digitWidth = (int)digw;
    	digitHeight = (int)digh;
    
    	recDigits = new RectF();
    	recDigits.left = DISPLAYAREA.left + (DISPLAYAREA.width() - digitWidth * nDigits) / 2.0f;
    	recDigits.top = DISPLAYAREA.top + (alt - digitHeight) / 2.0f;
    	recDigits.right = recDigits.left + digitWidth * nDigits;
    	recDigits.bottom = recDigits.top + digitHeight;

		step = (digitWidth / 18.0f);
    
/*		pathSegH = new Path();
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
 */
		
		pathSegH = new Path();
		pathSegH.moveTo(0, 2*step);
		pathSegH.lineTo(2*step, 4*step);
		pathSegH.lineTo(8*step, 4*step);
		pathSegH.lineTo(10*step, 2*step);
    	pathSegH.lineTo(8*step, 0);
    	pathSegH.lineTo(2*step, 0);
    	pathSegH.close();

    	pathSegV = new Path();
    	pathSegV.moveTo(3*step, 0);
    	pathSegV.lineTo(step, 2*step);
    	pathSegV.lineTo(0, 10*step);
    	pathSegV.lineTo(2*step, 12*step);
    	pathSegV.lineTo(4*step, 10*step);
    	pathSegV.lineTo(5*step, 2*step);
    	pathSegV.close();
    
    	pathDot = new Path();
    	pathDot.moveTo(step, 0);
    	pathDot.lineTo(0, 2*step);
    	pathDot.lineTo(step, 4*step);
    	pathDot.lineTo(3*step, 4*step);
    	pathDot.lineTo(4*step, 2*step);
    	pathDot.lineTo(3*step, 0);
    	pathDot.close();
		
		
    	trasl = new Point[8];
    	trasl[0] = new Point();	trasl[0].x = step ; trasl[0].y = 2*step;
    	trasl[1] = new Point();	trasl[1].x = 0 ; trasl[1].y = 15*step;
    	trasl[2] = new Point();	trasl[2].x = 3*step ; trasl[2].y = 25*step;
    	trasl[3] = new Point();	trasl[3].x = 12*step ; trasl[3].y = 15*step;
    	trasl[4] = new Point();	trasl[4].x = 13*step ; trasl[4].y = 2*step;
    	trasl[5] = new Point();	trasl[5].x = 5*step ; trasl[5].y = 0;
    	trasl[6] = new Point();	trasl[6].x = 4*step ; trasl[6].y = 13*step;
    	trasl[7] = new Point();	trasl[7].x = 14*step ; trasl[7].y = 26*step;

    	typ = new int[8];
    	typ[0] = 1;
    	typ[1] = 1;
    	typ[2] = 2;        
    	typ[3] = 1;        
    	typ[4] = 1;
    	typ[5] = 2;
    	typ[6] = 2;
    	typ[7] = 3;

    	conf = new int[11];
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
    	conf[10] = 0x00;
    	
	}
	
	public void drawValue(Canvas tela, Paint penON, Paint penOFF, String value) {
    
		int basex = (int)recDigits.left;
		int basey = (int)recDigits.top;
    
        int dig; 
        int map,mask;
        int i,j,n,k;
        float cifrax,cifray;
        boolean isDot = false;
        
        cifray = 0;
        n = value.length();
        k=0;
        for(j=0;j<n;j++) {
        	if(value.substring(j, j+1).equals(" "))
        		dig = 10; // blank value
        	else {
        		dig = Integer.parseInt( value.substring(j, j+1));
        		if(value.length() > j+1 && value.substring(j+1, j+2).equals(".")) {
        			isDot = true;
        			j++;
        		} else {
        			isDot = false;
        		}
        	}
        	map = conf[dig] | (isDot ? 128 : 0);
        	mask = 0x1;
        	cifrax = digitWidth * k;
        	for(i=0;i<8;i++) {
        		tela.setMatrix(null);
        		tela.translate(basex + cifrax + trasl[i].x, basey + cifray + trasl[i].y);
        		switch(typ[i]){
        		case 1:
        			tela.drawPath(pathSegV, ((map & mask) != 0) ? penON : penOFF);
        			break;
        		case 2:
        			tela.drawPath(pathSegH, ((map & mask) != 0) ? penON : penOFF);	
        			break;
        		case 3:
        			tela.drawPath(pathDot, ((map & mask) != 0) ? penON : penOFF);	
        			break;
        		}
        		mask = mask << 1;
        	}
        	tela.translate(0,0);
        	k++;
        }
        return;
	}
	
	
}
