import lib.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        System.out.println("test");

        Scanner sc = new Scanner(System.in);
        System.out.println("MENU");
        System.out.println("1. Sistem Persamaan Linier");
        System.out.println("2. Determinan");
        System.out.println("3. Matriks Balikan");
        System.out.println("4. Interpolasi Polinom");
        System.out.println("5. Interpolasi Bicubic");
        System.out.println("6. Regresi linier berganda");
        System.out.println("7. Keluar");
        int choice = sc.nextInt();
        while(choice < 1 || choice > 7){
            System.out.printf("Masukan tidak valid! Silakan ulangi...\n");
            choice = sc.nextInt();
        }
        if(choice == 1){
            SPL.DriverSPL();
        }
        else if(choice == 2){}
        else if(choice == 3){}
        else if(choice == 4){
            Interpolate.SolveInterpolate();
        }
        else if(choice == 5){}
        else if(choice == 6){}
        else{}
    }
}
