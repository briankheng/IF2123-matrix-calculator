import lib.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Interpolate {
    public static void SolveInterpolate(){
        // Menerima input dari user
        Scanner sc = new Scanner(System.in);
        BufferedReader scFile = new BufferedReader(new InputStreamReader(System.in));
        int n = 0;
        double[]x = new double[1001];
        double[]y = new double[1001];
        double X = -1;
        System.out.printf("1. Masukan dari keyboard\n2. Masukan dari file\n");
        int choice = sc.nextInt();
        while(choice != 1 && choice != 2){
            System.out.printf("Masukan tidak valid! Silakan ulangi...\n");
            choice = sc.nextInt();
        }
        if(choice == 1){
            System.out.printf("Masukkan jumlah titik: ");
            n = sc.nextInt();
            System.out.printf("Masukkan titik-titik x dan y:\n");
            for(int i = 0; i < n; i++){
                x[i] = sc.nextDouble();
                y[i] = sc.nextDouble();
            }
            System.out.printf("Masukkan nilai x yang akan ditaksir nilai fungsinya: ");
            X = sc.nextDouble();
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
                    while(file.hasNextLine()){
                        file.nextLine();
                        n++;
                    }
                    n--;
                    file.close();
                    
                    file = new Scanner(new File("../test/"+fileName));
                    for(int i = 0; i < n; i++){
                        x[i] = file.nextDouble();
                        y[i] = file.nextDouble();
                    }
                    X = file.nextDouble();
                    file.close();
                }
                catch(FileNotFoundException err){
                    err.printStackTrace();
                    found = false;
                }
            }
        }

        // Melakukan proses menentukan nilai a0, a1, a2, ...
        Matrix M = new Matrix(n, n+1);
        for(int i = 0; i < n; i++){
            double cur = 1;
            for(int j = 0; j < n+1; j++){
                if(j == n) M.setElmt(i, j, y[i]);
                else{
                    M.setElmt(i, j, cur);
                    cur *= x[i];
                }
            }
        }
        SPL solution = new SPL();
        solution.GaussJordan(M);
        double[] a = solution.x;
        
        // Input nilai X dan output f(X)
        double cur = 1, ans = 0;
        for(int i = 0; i < n; i++){
            ans += cur * a[i];
            cur *= X;
        }
        System.out.printf("f(x) = ");
        for(int i = n-1; i >= 0; i--){
            if(i == n-1){
                if(a[i] < 0) System.out.printf("-%fx^%d", Math.abs(a[i]), i);
                else System.out.printf("%fx^%d", Math.abs(a[i]), i);
            }
            else if(i == 0){
                if(a[i] < 0) System.out.printf(" - %f,\n", Math.abs(a[i]));
                else System.out.printf(" + %f,\n", Math.abs(a[i]));
            }
            else if(i == 1){
                if(a[i] < 0) System.out.printf(" - %fx", Math.abs(a[i]));
                else System.out.printf(" + %fx", Math.abs(a[i]));
            }
            else{
                if(a[i] < 0) System.out.printf(" - %fx^%d", Math.abs(a[i]), i);
                else System.out.printf(" + %fx^%d", Math.abs(a[i]), i);
            }
        }
        System.out.printf("f(%f) = %f\n\n", X, ans);
        // Simpan jawaban dalam file
        System.out.printf("Apakah jawaban ingin disimpan dalam file?\n1. Ya\n2. Tidak\n");
        choice = sc.nextInt();
        while(choice != 1 && choice != 2){
            System.out.printf("Masukan tidak valid! Silakan ulangi...\n");
            choice = sc.nextInt();
        }
        if(choice == 1){
            String fileName = "";
            System.out.printf("Masukkan nama file: ");
            try{
                fileName = scFile.readLine();
            }
            catch(IOException err){
                err.printStackTrace();
            }
            try{
                FileWriter file = new FileWriter("../test/"+fileName);
                file.write("f(x) = ");
                for(int i= n-1;i >= 0; i--){
                    if(i == n-1){
                        if(a[i] < 0) file.write("-"+Double.toString(Math.abs(a[i]))+"x^"+Integer.toString(i));
                        else file.write(Double.toString(Math.abs(a[i]))+"x^"+Integer.toString(i));
                    }
                    else if(i == 0){
                        if(a[i] < 0) file.write(" - "+Double.toString(Math.abs(a[i]))+",\n");
                        else file.write(" + "+Double.toString(Math.abs(a[i]))+",\n");
                    }
                    else if(i == 1){
                        if(a[i] < 0) file.write(" - "+Double.toString(Math.abs(a[i]))+"x");
                        else file.write(" + "+Double.toString(Math.abs(a[i]))+"x");
                    }
                    else{
                        if(a[i] < 0) file.write(" - "+Double.toString(Math.abs(a[i]))+"x^"+Integer.toString(i));
                        else file.write(" + "+Double.toString(Math.abs(a[i]))+"x^"+Integer.toString(i));
                    }
                }
                file.write("f("+Double.toString(X)+") = "+Double.toString(ans)+"\n");
                file.close();
            }
            catch(IOException err){
                err.printStackTrace();
            }
        }
    }
}
