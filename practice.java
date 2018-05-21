import java.util.Scanner;

public class practice {
    public static void main(String[] args) {
        String MyHello = "Hello World";
        int MyInt = 8;
        System.out.println(MyHello + " " + MyInt);

        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        int value = sc.nextInt();
        System.out.println(name + " " + value);
        sc.close();
    }
}