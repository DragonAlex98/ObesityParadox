package threedimensional.cell;

public abstract class Cell {

	private int state;
	
	public int getState() {
		return this.state;
	}
	
	public void setState(int state) {
		this.state = state;
	}
}