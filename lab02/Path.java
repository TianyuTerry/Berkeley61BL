/** A class that represents a path via pursuit curves. */
public class Path {

    // TODO

    public Point curr;
    public Point next;

    public Path (double x,double y) {
    	this.next = new Point(x,y);
    	this.curr = new Point(1.0,1.0);
    }

    public void iterate(double dx, double dy) {
    	this.curr = this.next;
    	this.next = new Point(this.curr.x+dx, this.curr.y+dy);
    }
	public static void main(String[] args){
		Point mypoint = new Point(3,4);
		System.out.println(mypoint);
	}
}
