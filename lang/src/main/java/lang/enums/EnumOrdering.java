package lang.enums;

public class EnumOrdering
{
    public static void main(String[] args)
    {
        System.out.println(AnEnum.A.compareTo(AnEnum.B)); // A < B
        System.out.println(AnEnum.B.compareTo(AnEnum.C)); // B < C
        System.out.println(AnEnum.B.compareTo(AnEnum.A)); // B > A
    }

    static enum AnEnum
    {
        A
        , B
        , C;
    }
}
