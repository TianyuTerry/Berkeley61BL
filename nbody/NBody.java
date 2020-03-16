public class NBody {

	public static double readRadius(String filename) {
		In in = new In(filename);
		in.readInt();
		double radius = in.readDouble();
		return radius;
	}

	public static Planet[] readPlanets(String filename) {
		In in = new In(filename);
		int size = in.readInt();
		Planet[] array = new Planet[size];
		in.readDouble();
		while (size > 0) {
			double xxPos = in.readDouble();
			double yyPos = in.readDouble();
			double xxVel = in.readDouble();
			double yyVel = in.readDouble();
			double mass = in.readDouble();
			String imgFileName = in.readString();
			array[array.length - size] = new Planet(xxPos, yyPos, xxVel, yyVel, mass, imgFileName);
			size--;
		}
		return array;
	}

	public static void main(String[] args) {

		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];
		double radius = readRadius(filename);
		Planet[] planets = readPlanets(filename);

		StdDraw.enableDoubleBuffering();
		StdDraw.setScale(-radius, radius);
		StdDraw.clear();
		StdDraw.picture(0,0,"images/starfield.jpg");
		StdDraw.show();

		for (Planet p : planets) {
			p.draw();
		}

		StdDraw.enableDoubleBuffering();

		for (double time = 0; time <= T; time += dt) {
			double[] xForces = new double[planets.length];
			double[] yForces = new double[planets.length];
			for (int i = 0; i < planets.length; i++) {
				xForces[i] = planets[i].calcNetForceExertedByX(planets);
				yForces[i] = planets[i].calcNetForceExertedByY(planets);
			}
			for (int i = 0; i < planets.length; i++) {
				planets[i].update(dt, xForces[i], yForces[i]);
			}
			StdDraw.clear();
			StdDraw.picture(0,0,"images/starfield.jpg");
			for (Planet p : planets) {
				p.draw();
			}
			StdDraw.show();
			StdDraw.pause(10);
		}

		StdOut.printf("%d\n", planets.length);
		StdOut.printf("%.2e\n", radius);
		for (int i = 0; i < planets.length; i += 1) {
    		StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                  planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                  planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
		}
	}
}