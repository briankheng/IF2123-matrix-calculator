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

        int n = 0, m = 0;
        double x;
        Matrix M = new Matrix(100,100);
        Matrix MTarget = new Matrix(1,100);
        System.out.printf("1. Masukan dari keyboard\n2. Masukan dari file\n");
        int choice = sc.nextInt();
        while(choice != 1 && choice != 2){
            System.out.printf("Masukan tidak valid! Silakan ulangi...\n");
            choice = sc.nextInt();
        }
        
        if(choice == 1){
            System.out.printf("Masukkan jumlah variabel(tidak termasuk y): ");
            n = sc.nextInt();
            System.out.printf("Masukkan jumlah data: ");
            m = sc.nextInt();
            Matrix MData = new Matrix(n+1, m);
            Matrix Minput = new Matrix(1, n);
            System.out.printf("Masukkan titik-titik x dan y, dengan urutan x1,x2,..,xn,y:\n");
            for(int j = 0; j < m; j++){
                for(int i = 0; i < n+1; i++){
                    x = sc.nextDouble();
                    MData.setElmt(i, j, x);
                }
            }
            System.out.printf("Masukkan titik-titik x yang ingin dihampiri, dengan urutan x1,x2,..,xn:\n");
            for(int i = 0; i < n; i++){
                x = sc.nextDouble();
                Minput.setElmt(0, i, x);
            }
            M = MData;
            MTarget = Minput;
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
                    m=0;
                    int check;
                    while(file.hasNextLine()){
                        n++;
                        check= file.nextLine().split(" ").length;
                        if(check>m){
                            m = check;
                        }
                    }
                    n--;
                    int swap=n;
                    n=m;
                    m=swap;
                    file.close();

                    Matrix MData = new Matrix(n, m);
                    Matrix Minput= new Matrix(1, n);

                    /* debug
                    String estkse=Integer.toString(m);
                    System.out.println(estkse);
                    */

                    file = new Scanner(new File("../test/"+fileName));
                    for(int i = 0; i < m; i++){
                        for(int j = 0; j< n; j++){
                            x=file.nextDouble();
                            MData.setElmt(j, i, x);
                        }
                    }

                    for(int i = 0; i < n-1; i++){
                        x = file.nextDouble();
                        Minput.setElmt(0, i, x);

                    }


                    file.close();
                    M=MData;
                    MTarget=Minput;

                }
                
                catch(FileNotFoundException err){
                    err.printStackTrace();
                    found = false;
                }
            }
        }
        /* keeping this as reference
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
                    n = file.nextInt();
                    m = file.nextInt();
                    Matrix MData = new Matrix(n+1, m);
                    Matrix Minput= new Matrix(1, n);
                    for(int i = 0; i < n+1; i++){
                        for(int j = 0; j < m; j++){
                            x = file.nextDouble();
                            MData.setElmt(i, j, x);
                        }
                    }
                    for(int i = 0; i < n; i++){
                        x = file.nextDouble();
                        Minput.setElmt(0, i, x);

                    }
                    M = MData;
                    MTarget = Minput;
                }
                catch(FileNotFoundException err){
                    err.printStackTrace();
                    found = false;
                }
            }
            
        }
        */
        // Fungsi Regresi
        int i, j, k, nRow=M.getRowEff(), nCol=M.getColEff();
        double temp, sum;

        Matrix MUse = new Matrix(nRow, nRow+1);
        i = 0;
        for(j = 0; j < nRow+1; j++){
            if(j == 0) MUse.setElmt(i, j, nCol);
            else{
                sum = 0;
                for(k = 0; k < nCol; k++){
                    temp = M.getElmt(j-1, k);
                    sum += temp;
                }
                MUse.setElmt(i, j, sum);
            }
        }

        for(i = 1; i < nRow; i++){
            for(j = 0; j < nRow+1; j++){
                if(j == 0){
                    sum = 0;
                    for(k = 0; k < nCol; k++){
                        temp = M.getElmt(i-1, k);
                        sum += temp;
                    }
                    MUse.setElmt(i, j, sum);
                }
                else{
                    sum = 0;
                    for(k = 0; k < nCol; k++){
                        temp = M.getElmt(i-1, k) * M.getElmt(j-1, k);
                        sum += temp;
                    }
                    MUse.setElmt(i, j, sum);
                }
            }
        }
        SPL solution = new SPL();
        solution.GaussJordan(MUse);
        String save;
        double ysub = 0, curr;
        
        System.out.printf("Persamaannya adalah\ny = ");
        
        for(i = 0; i < solution.nEff; i++){
            curr = solution.x[i];
            if(i != 0) curr *= MTarget.getElmt(0, i-1);
            ysub += curr;
        }

        for(i = 0; i < solution.nEff; i++){
            save = " ";
            if(i != 0 && solution.x[i] >= 0) save += "+";
            save += Double.toString(solution.x[i]);
            if(i != 0) save += " x" + Integer.toString(i);
            System.out.printf(save);
        }
        System.out.printf("\nHampiran nilai y-nya adalah y=");
        String yans = Double.toString(ysub);
        System.out.printf(yans);
        System.out.printf("\n");

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
                file.write("Luaran untuk Regresi adalah y = ");

                for(i = 0; i < solution.nEff; i++){
                    save = " ";
                    if(i != 0 && solution.x[i] > 0) save += "+";
                    save += Double.toString(solution.x[i]);
                    if(i != 0) save += " x" + Integer.toString(i);
                    file.write(save);
                }
            
                file.write("\nHampiran untuk nilai y-nya adalah y = ");
                file.write(yans);
                file.write("\n");
                file.close();
            }
            catch(IOException err){
                err.printStackTrace();
            }
        }
    }
}
