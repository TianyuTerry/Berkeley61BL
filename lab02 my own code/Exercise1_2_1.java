class Dog {
	public int weightInPounds;

	public Dog(int w) {
		weightInPounds = w;
	}

	public void makeNoise() {
		if (weightInPounds < 10) {
            System.out.println("yipyipyip!");
        } else if (weightInPounds < 30) {
            System.out.println("bark. bark.");
        } else {
            System.out.println("woof!");
        }
	}
	public static Dog maxDog(Dog d1, Dog d2) {
		if (d1.weightInPounds > d2.weightInPounds) {
			return d1;
		}
	return d2;
	}
}

public class Exercise1_2_1 {
	public static void main(String[] args) {
		Dog d1,d2;
		d1 = new Dog(20);
		d2 = new Dog(80);
		int answer = Dog.maxDog(d1, d2).weightInPounds;
		System.out.println(answer);
	}
}