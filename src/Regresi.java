import lib.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Regresi {
    public static void SolveRegression(){
        // Menerima input dari user
        Scanner sc = new Scanner(System.in);
        BufferedReader scFile = new BufferedReader(new InputStreamReader(System.in));

        int n = 0;
        int m=0;
        double x;
        Matrix M= new Matrix(100,100);

        System.out.printf("1. Masukan dari keyboard\n2. Masukan dari file\n");
        int choice = sc.nextInt();
        while(choice != 1 && choice != 2){
            System.out.printf("Masukan tidak valid! Silakan ulangi...\n");
            choice = sc.nextInt();
        }
        
        if(choice == 1){
            System.out.printf("Masukkan jumlah variabel: ");
            n = sc.nextInt();
            System.out.printf("Masukkan jumlah data: ");
            m = sc.nextInt();
            Matrix MData = new Matrix(n,m);
            
            System.out.printf("Masukkan titik-titik x dan y:\n");
            for(int j = 0; j < m; j++){

                for(int i=0;i<n;i++){
                    x=sc.nextDouble();
                    MData.setElmt(i, j, x);
                }
            }
            M=MData;
        }
        else{
            Boolean found = false;
            while(!found){
                found = true;
                String fileName = "";
                System.out.printf("Masukkan nama file: ");
                try{
                    fileName = scFile.readLine();
                }
                catch(IOException err){
                    err.printStackTrace();
                }
                try{
                    Scanner file = new Scanner(new File("../test/"+fileName));
                    n=file.nextInt();
                    m=file.nextInt();
                    Matrix MData = new Matrix(n,m);

                    for(int i = 0; i < n; i++){
                        for(int j=0;j<m;j++){
                            x=file.nextDouble();
                            MData.setElmt(i, j, x);
                        }
                    }
                    M=MData;
                }
                catch(FileNotFoundException err){
                    err.printStackTrace();
                    found = false;
                }
            }
            
        }
        MLR.Solve(M);
        
    }
}
