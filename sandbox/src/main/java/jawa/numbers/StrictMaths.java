package jawa.numbers;

public class StrictMaths
{
    /**
     * Initial value of int: -1
     * Initial value of float: 0.5
     * Absolute value of int: 1
     * Absolute value of float: 0.5
     *
     * acos value of int: NaN
     * acos value of double: 1.5159376794536454
     */
    public static void main(String[] args)
    {
        // Declaring the variables
        int valI = -1;
        float valF = .5f;

        // Printing the values
        System.out.println("Initial value of int: " + valI);
        System.out.println("Initial value of float: " + valF);


        // Use of .abs() method to get the absoluteValue
        int absI = StrictMath.abs(valI);
        float absF = StrictMath.abs(valF);

        System.out.println("Absolute value of int: " + absI);
        System.out.println("Absolute value of float: " + absF);
        System.out.println();

        // Use of acos() method
        // Value greater than 1, so passing NaN
        double acosI = StrictMath.acos(60);
        System.out.println("acos value of int: " + acosI);
        double x = StrictMath.PI;

        // Use of toRadian() method
        x = StrictMath.toRadians(x);
        double acosJ = StrictMath.acos(x);
        System.out.println("acos value of double: " + acosJ);
    }

}
