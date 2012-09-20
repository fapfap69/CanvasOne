package fap.example.canvasone;

public class CircularFrame extends GenericFrame {

	
	public CircularFrame(int WIDTH, int HEIGHT){
		 super(WIDTH, HEIGHT);
	}
	// Metodi 
	
	// Tipo
	@Override
	public Tipo getType() {
		return Tipo.CIRCULAR;
	}
    
}


