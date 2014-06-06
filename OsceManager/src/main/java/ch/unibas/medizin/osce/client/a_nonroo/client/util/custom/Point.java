package ch.unibas.medizin.osce.client.a_nonroo.client.util.custom;

public class Point {
	int x,y;

	public Point(int x,int y){
	    this.x=x;
	    this.y=y;
	}   
	public int getX() {
	    return x;
	}
	public int getY() {
	    return y;
	}
	@Override
	public String toString() {
	    return x+","+y;
	}
	public boolean isBetween(Point p1,Point p2){
	    if (p1.getX() < x && p2.getX() > x && p1.getY() < y && p2.getY() > y)
	        return true;
	    return false;
	}
	public boolean contained(Point topLeft, Point topRight, Point bottomLeft,Point bottomRight) {
		if(x >= topLeft.x && y >= topLeft.y 
				&& x <= topRight.x && y >= topRight.y 
				&& x >= bottomLeft.x && y <= bottomLeft.y 
				&& x <= bottomRight.x && y <= bottomRight.y)
			return true;
		return false;
	}
}
