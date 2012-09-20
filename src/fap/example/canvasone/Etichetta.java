package fap.example.canvasone;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

public class Etichetta {

	public RectF area = new RectF();
	public RectF textArea = new RectF();
	public String contenuto = new String();
	public int foreCol;
	public int backCol;
	public float rotazione;
	public boolean hasBorder = false;

	public boolean isAutoSize = true;
	public float fontSize = Dash.LABELFONTSIZE;
	
	Paint normografo = new Paint();
	
	private Paint.Align allineamento = Align.LEFT;
	private String buffer = new String();
	
	
	public Etichetta(String TEXT, RectF DIMENSION) {

		// get somae infoes
		foreCol = Color.argb(Dash.OPACITY, 0, 0, 0);
		backCol = Color.argb(0, 255, 255, 255);
		contenuto = TEXT;
		area = DIMENSION;
		
		return;
		
	}
	
	public void setAlign(Paint.Align ALIGN){
		allineamento = ALIGN;
		return;
	}
	public void setColors(int FORECOL, int BACKCOL){
		foreCol = FORECOL;
		backCol = BACKCOL;
		return;
	}
	public void setRotate(float ROTATE){
		rotazione = ROTATE;
		return;
	}
	
	
	// Questa funzione riorganizza il testo come font etc...
	private void arrangeText() {
		
		boolean isLeft = (allineamento == Paint.Align.LEFT) ? true : false;
		int pointer;
		float size;
		Rect etich = new Rect();
		
		if(hasBorder){
			textArea.set(area.left + Dash.LABELBORDERDISTANCE,
					area.top + Dash.LABELBORDERDISTANCE,
					area.right - Dash.LABELBORDERDISTANCE,
					area.bottom - Dash.LABELBORDERDISTANCE);
		} else {
			textArea.set(area);
		}
		
		if(isAutoSize) {
			size = Dash.LABELFONTSIZE;
			boolean stayin = true;
			while(stayin) {
				normografo.setTextSize(size);
				normografo.getTextBounds(contenuto, 0, contenuto.length()-1, etich);
				if(etich.width() > textArea.width()) {
					size = size - 1.0f;
					stayin = size > 1.0f ? true : false;
				} else {
					if(etich.width() < (0.8 * textArea.width()) )
						size = size + 1.0f;
					else
						stayin = false;
				}
			}
			fontSize = size;
			buffer = contenuto;
		} else {
			normografo.setTextSize(fontSize);
			pointer = normografo.breakText(contenuto,isLeft,textArea.width(), null);
			buffer = (isLeft) ? contenuto.substring(0,pointer-1) : contenuto.substring(contenuto.length()-pointer,contenuto.length()-1);  
		}
		return;
		
	}
	
	
	
	public void drawEtichetta(Canvas tela) {

		arrangeText();
		
		normografo.setAntiAlias(true);
		normografo.setTextAlign(allineamento);
		normografo.setTextSize(fontSize);
		normografo.setTypeface(Dash.LABEFONTTYPFACE);
		
		tela.rotate(rotazione, area.centerX(), area.centerY());

		normografo.setARGB(Color.alpha(backCol), Color.red(backCol), Color.green(backCol), Color.blue(backCol) );
		normografo.setStyle(Paint.Style.FILL_AND_STROKE);
		tela.drawRect(area, normografo);
		
		normografo.setARGB(Color.alpha(foreCol), Color.red(foreCol), Color.green(foreCol), Color.blue(foreCol) );
		normografo.setStyle(Paint.Style.STROKE);
		
		if(hasBorder) tela.drawRect(area, normografo);
		
		switch(allineamento) {
		case LEFT:
			tela.drawText(buffer, textArea.left, textArea.bottom, normografo);
			break;
		case RIGHT:
			tela.drawText(buffer, textArea.right, textArea.bottom, normografo);
			break;
		case CENTER:
			tela.drawText(buffer, textArea.centerX(), textArea.bottom, normografo);
			break;
		}
		
		
		tela.rotate(0, area.centerX(), area.centerY());
		
		return;
	}
	
}
