package fap.example.canvasone;

import fap.example.canvasone.Material.Type;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;

public class Material {
	
	// Tipi di materiali
	public enum Type{
		
		BLACK_METAL,
		METAL,
		SHINY_METAL,
		GLOSSY_METAL,
		BRASS,
		STEEL,
		CHROME,
		GOLD,
		ANTHRACITE,
		TILTED_GRAY,
		TILTED_BLACK,
		CUSTOM,

		// Custom gradient
		LINEARORIZONTAL,
		LINEARVERTICAL,
		LINEAROBLIQUE,
		RADIAL,
		NONE
		
	}

	protected int opacity = Dash.OPACITY;
	
	// Proprieta
	private float[] posizioni;
	private int[] colori;
	private LinearGradient GRADLIN;
	private RadialGradient GRADRAD;

	public static int color1;
	public static int color2; 
	
	// Metodo per i materiali

	public Material(final Type MATERIAL, final RectF AREA, Paint PENNELLO) {
		CreateMaterial(MATERIAL, AREA, PENNELLO);
		return;
	}		
	public Material(final Type MATERIAL, final RectF AREA, final int COLOR1, final int COLOR2, Paint PENNELLO) {
		color1 = COLOR1;
		color2 = COLOR2;
		CreateMaterial(MATERIAL, AREA, PENNELLO);
		return;
	}
		
	// ---
	private void CreateMaterial(final Type MATERIAL, final RectF AREA, Paint PENNELLO) {

		switch (MATERIAL) {
		case BLACK_METAL:
			posizioni = new float[] {0.0f,45.0f,125.0f,180.0f,245.0f,315.0f,360.0f};
			colori = new int[] {Color.argb(opacity,254,254,254),Color.argb(opacity,0,0,0),Color.argb(opacity,153,153,153),Color.argb(opacity,0,0,0),Color.argb(opacity,153,153,153),Color.argb(opacity,0,0,0),Color.argb(opacity,254,254,254)};
			GRADRAD = new RadialGradient(AREA.centerX(), AREA.centerY(), AREA.width()/2.0f, colori, posizioni, Shader.TileMode.REPEAT);
			PENNELLO.setShader(GRADRAD);
			break;

		case METAL:
			posizioni = new float[] {0.0f,0.07f,0.12f,1.0f};
			colori = new int[] {Color.argb(opacity,254,254,254),Color.argb(opacity,210, 210, 210),Color.argb(opacity,179, 179, 179),Color.argb(opacity,213, 213, 213)};
			GRADLIN = new LinearGradient(0,0,AREA.width(), AREA.height(), colori, posizioni, Shader.TileMode.REPEAT);
			PENNELLO.setShader(GRADLIN);
			break;
			
		case SHINY_METAL:
			posizioni = new float[] {0.0f,45.0f,90.0f,125.0f,180.0f,235.0f,270.0f,315.0f,360.0f};
			colori = new int[] {Color.argb(opacity,254, 254, 254),Color.argb(opacity,210, 210, 210),Color.argb(opacity,179,179,179),Color.argb(opacity,238,238,238),Color.argb(opacity,160,160,160),Color.argb(opacity,238,238,238),Color.argb(opacity,179,179,179),Color.argb(opacity,210,210,210),Color.argb(opacity,254,254,254)};
			GRADRAD = new RadialGradient(AREA.centerX(), AREA.centerY(), AREA.width()/2.0f, colori, posizioni, Shader.TileMode.REPEAT);
			PENNELLO.setShader(GRADRAD);
			break;

		case GLOSSY_METAL:
			posizioni = new float[] {0.0f, 0.96f, 1.0f};
			colori = new int[] {Color.argb(opacity,207,207,207),Color.argb(opacity,204,203,204),Color.argb(opacity,244,244,244)};
			GRADRAD = new RadialGradient(AREA.centerX(), AREA.centerY(), AREA.width()/2.0f, colori, posizioni, Shader.TileMode.REPEAT);
			PENNELLO.setShader(GRADRAD);
			break;

    // area 1,5% - 97% --> Lineare Verticale {0.0f, 0.23f, 0.36f, 0.59f, 0.76f, 1.0f} {(.97,.97.,97),.78,.76.74),(1,1,1),(.11,.11,.11),(.78,.76,.75),(.81,.81,.81)}
    // area 6% - 87% --> Fill 0xF6F6F6
    // area precedente piu' stretta di 2px,2px,-4px,-4px ---> fill 0x333333

		case BRASS:
			posizioni = new float[] {0.0f,0.05f,0.10f,0.50f,0.90f,0.95f,1.0f};
			colori = new int[] {Color.argb(opacity,249,243,155),Color.argb(opacity,246,226,101),Color.argb(opacity,240,225,132),Color.argb(opacity,90,57,22),Color.argb(opacity,249,237,139),Color.argb(opacity,243,226,108),Color.argb(opacity,202,182,113)};
			GRADLIN = new LinearGradient(0,0, AREA.width(), AREA.height(),colori,posizioni,Shader.TileMode.REPEAT);
			PENNELLO.setShader(GRADLIN);
			break;

		case STEEL:
			posizioni = new float[] {0.0f,0.05f,0.10f,0.50f,0.90f,0.95f,1.0f};
			colori = new int[] {Color.argb(opacity,231, 237, 237),Color.argb(opacity,189, 199, 198),Color.argb(opacity,192, 201, 200),Color.argb(opacity,23, 31, 33),Color.argb(opacity,196, 205, 204),Color.argb(opacity,194, 204, 203),Color.argb(opacity,189, 201, 199)};
			GRADLIN = new LinearGradient(0,0, AREA.width(), AREA.height(), colori, posizioni,Shader.TileMode.REPEAT);
			PENNELLO.setShader(GRADLIN);
			break;

		case CHROME:
			posizioni = new float[] {0.0f,0.09f,0.12f,0.16f,0.25f,0.29f,0.33f,0.38f,0.48f,0.52f,0.63f,0.68f,0.8f,0.83f,0.87f,0.97f,1.0f};
			colori = new int[] {Color.argb(opacity,255, 255, 255),Color.argb(opacity,255, 255, 255),Color.argb(opacity,136, 136, 138),Color.argb(opacity,164, 185, 190),Color.argb(opacity,158, 179, 182),Color.argb(opacity,112, 112, 112),Color.argb(opacity,221, 227, 227),Color.argb(opacity,155, 176, 179),Color.argb(opacity,156, 176, 177),Color.argb(opacity,254, 255, 255),Color.argb(opacity,255, 255, 255),Color.argb(opacity,156, 180, 180),Color.argb(opacity,198, 209, 211),Color.argb(opacity,246, 248, 247),Color.argb(opacity,204, 216, 216),Color.argb(opacity,164, 188, 190),Color.argb(opacity,255, 255, 255)};
			GRADRAD = new RadialGradient(AREA.centerX(), AREA.centerY(), AREA.width()/2.0f, colori, posizioni, Shader.TileMode.REPEAT);
			PENNELLO.setShader(GRADRAD);
			break;

		case GOLD:
			posizioni = new float[] {0.0f,0.15f,0.22f,0.3f,0.38f,0.44f,0.51f,0.6f,0.68f,0.75f,1.0f};
			colori = new int[] {Color.argb(opacity,255,255,207), Color.argb(opacity,255, 237, 96), Color.argb(opacity,254,199,57), Color.argb(opacity,255, 249, 203), Color.argb(opacity,255,199,64), Color.argb(opacity,252,194,60), Color.argb(opacity,255, 204, 59), Color.argb(opacity,213, 134, 29), Color.argb(opacity,255, 201, 56), Color.argb(opacity,212, 135, 29), Color.argb(opacity,247, 238, 101) };
			GRADLIN = new LinearGradient(0,0, AREA.width(), AREA.height(), colori, posizioni,Shader.TileMode.REPEAT);
			PENNELLO.setShader(GRADLIN);
			break;
        
		case ANTHRACITE:
			posizioni = new float[] {0.0f,0.06f,0.12f,1.0f};
			colori = new int[] {Color.argb(opacity,118, 117, 135),Color.argb(opacity,74, 74, 82),Color.argb(opacity,50, 50, 54),Color.argb(opacity,97, 97, 108)};
			GRADLIN = new LinearGradient(0,0, AREA.width(), AREA.height(), colori, posizioni,Shader.TileMode.REPEAT);
			PENNELLO.setShader(GRADLIN);
			break;

		case TILTED_GRAY:
			posizioni = new float[] {0.0f,0.07f,0.16f,0.33f,0.55f,0.79f,1.0f};
			colori = new int[] {Color.argb(opacity,255, 255, 255),Color.argb(opacity,210, 210, 210),Color.argb(opacity,179, 179, 179),Color.argb(opacity,255, 255, 255),Color.argb(opacity,197, 197, 197),Color.argb(opacity,255, 255, 255),Color.argb(opacity,102, 102, 102)};
			GRADLIN = new LinearGradient(0.233f * AREA.width(), 0.084f * AREA.height(), 0.813f * AREA.width(), 0.911f * AREA.height(), colori, posizioni,Shader.TileMode.REPEAT);
			PENNELLO.setShader(GRADLIN);
			break;
	
		case TILTED_BLACK:
			posizioni = new float[] {0.0f,0.21f,0.47f,0.99f,1.0f};
			colori = new int[] {Color.argb(opacity,102, 102, 102),Color.argb(opacity,0, 0, 0),Color.argb(opacity,102, 102, 102),Color.argb(opacity,0, 0, 0),Color.argb(opacity,0, 0,0)};
			GRADLIN = new LinearGradient(0.229f * AREA.width(), 0.079f * AREA.height(), 0.803f * AREA.width(), 0.899f * AREA.height(), colori, posizioni,Shader.TileMode.REPEAT);
			PENNELLO.setShader(GRADLIN);
			break;

			
		case LINEARORIZONTAL:
			posizioni = new float[] {0.0f,0.50f,0.51f,1.0f};
			colori = new int[] {color1,color1,color2,color2};
			GRADLIN = new LinearGradient(0,0,AREA.width(),0,colori,posizioni,Shader.TileMode.REPEAT);
			PENNELLO.setShader(GRADLIN);
			break;
		case LINEARVERTICAL:
			posizioni = new float[] {0.0f,0.50f,0.51f,1.0f};
			colori = new int[] {color1,color1,color2,color2};
			GRADLIN = new LinearGradient(0,0,0,AREA.height(),colori,posizioni,Shader.TileMode.REPEAT);
			PENNELLO.setShader(GRADLIN);
			break;
		case LINEAROBLIQUE:
			posizioni = new float[] {0.0f,0.50f,0.51f,1.0f};
			colori = new int[] {color1,color1,color2,color2};
			GRADLIN = new LinearGradient(0,0,AREA.width(),AREA.height(),colori,posizioni,Shader.TileMode.REPEAT);
			PENNELLO.setShader(GRADLIN);
			break;
		case RADIAL:
			posizioni = new float[] {0.0f,180.0f,181.0f,360.0f};
			colori = new int[] {color1,color1,color2,color2};
			GRADRAD = new RadialGradient(AREA.centerX(), AREA.centerY(), AREA.width()/2.0f, colori, posizioni, Shader.TileMode.REPEAT);
			PENNELLO.setShader(GRADRAD);
			break;

		case NONE:
		default:
			posizioni = new float[] {0.0f,0.07f,0.12f,1.0f};
			colori = new int[] {color1,color1,color2,color2};
			GRADLIN = new LinearGradient(0,0,AREA.width(),AREA.height(),colori,posizioni,Shader.TileMode.REPEAT);
			PENNELLO.setShader(GRADLIN);
			break;

		}
	return;
	}

}
