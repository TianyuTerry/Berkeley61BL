public class Planet {

	double xxPos;
	double yyPos;
	double xxVel;
	double yyVel;
	double mass;
	String imgFileName;
	private static final double g = 6.67e-11;

	public Planet(double xP, double yP, double xV, double yV, double m, String img) {
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}

	public Planet(Planet p) {
		this.xxPos = p.xxPos;
		this.yyPos = p.yyPos;
		this.xxVel = p.xxVel;
		this.yyVel = p.yyVel;
		this.mass = p.mass;
		this.imgFileName = p.imgFileName;
	}

	public double calcDistance(Planet p) {
		double square = (this.xxPos - p.xxPos) * (this.xxPos - p.xxPos) + (this.yyPos - p.yyPos) * (this.yyPos - p.yyPos);
		double distance = Math.sqrt(square);
		return distance;
	}

	public double calcForceExertedBy(Planet p) {
		double force = g * p.mass * this.mass / (this.calcDistance(p)*this.calcDistance(p));
		return force;
	}

	public double calcForceExertedByX(Planet p) {
		double dx = p.xxPos - this.xxPos;
		double forceX = calcForceExertedBy(p) * dx / calcDistance(p);
		return forceX;
	}

	public double calcForceExertedByY(Planet p) {
		double dy = p.yyPos - this.yyPos;
		double forceY = calcForceExertedBy(p) * dy / calcDistance(p);
		return forceY;
	}

	public double calcNetForceExertedByX(Planet[] array) {
		double netforceX = 0;
		for (Planet p : array) {
			if (!this.equals(p)) {
				netforceX += calcForceExertedByX(p);
			}
		}
		return netforceX;
	}

	public double calcNetForceExertedByY(Planet[] array) {
		double netforceY = 0;
		for (Planet p : array) {
			if (!this.equals(p)) {
				netforceY += calcForceExertedByY(p);
			}
		}
		return netforceY;
	}

	public void update(double dt, double forceX, double forceY) {
		double aX = forceX / mass;
		double aY = forceY / mass;
		xxVel += dt * aX;
		yyVel += dt * aY;
		xxPos += dt * xxVel;
		yyPos += dt * yyVel;
	}

	public void draw(){
		StdDraw.picture(xxPos,yyPos,"images/" + imgFileName);
		StdDraw.show();
	}

	
}