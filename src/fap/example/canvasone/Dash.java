package fap.example.canvasone;

import android.graphics.Typeface;


public class Dash {

	// Constant & default values

	// Opacità dello strumento
	static final int OPACITY = 254;
	
	// Colori
	static final int OUTLINEGRAY = 18;
	static final int SHADOWGRAY = 48;
	static final int SHADOWOPACITY = 30;
	
	// Files
	static final String DEFAULT_BACKGROUND_NAME = "DefaultBackGround.png";
	
	// Font...
	static final Typeface LABEFONTTYPFACE = Typeface.SANS_SERIF;
	static final float LABELFONTSIZE = 10.0f;
	
	// Dimensioni ....
	// percentuale dell'ombra
	static final float SHADOW_PER = 0.01f;
	// percentuale del bordo
	static final float BORDER_PER = 0.1f;
	// percentuale del bordo interno
	static final float INNERSTEP_PER = 0.25f;
	// percentuale dell'ombra all'interno
	static final float THICK_PER = 0.02f;

	static final float DIALTITLEWIDTH_PER = 0.2f;
	static final float DIALTITLEEIGHT_PER = 0.1f;
	static final float LABELBORDERDISTANCE = 2.0f;

	static final float SCALEUNITWIDTH_PER = 0.1f;
	static final float SCALEUNITEIGHT_PER = 0.05f;
	static final float SCALEPRIMARYDIV_PER = 0.1f;
	static final float SCALESECONDARYDIV_PER = 0.05f;
	
	
	// Environment Variables
	private static int numberOfGauges = 0;
	private static Object gaugesIds[];
	
	public void Dash(){
		return;
	}
	
	public int getNumberOfGauges(){
		return numberOfGauges;
	}

	public void addGauge(final Object Gauge){
		gaugesIds[numberOfGauges] = new Object();
		gaugesIds[numberOfGauges] = Gauge;
		numberOfGauges++;
		return ;
	}

	public Object getGaugeId(final int Pointer){
		if(Pointer > (numberOfGauges-1)) return null;
		else return(gaugesIds[Pointer]);
	}
}
