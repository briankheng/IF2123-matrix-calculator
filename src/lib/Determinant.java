package lib;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Determinant{
    public static void DriverDeterminan(){
		Scanner sc = new Scanner(System.in);
        BufferedReader scFile = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("PILIHAN\n1. Metode determinan dengan OBE\n2. Metode determinan dengan kofaktor\nPilihan: ");
        Integer choice = sc.nextInt();
        while(choice < 1 || choice > 2){
            System.out.printf("Masukan tidak valid! Silakan ulangi...\nPilihan: ");
            choice = sc.nextInt();
        }

        Matrix M = new Matrix(0, 0);
        System.out.printf("1. Masukan dari keyboard\n2. Masukan dari file\nPilihan: ");
        int ichoice = sc.nextInt();
        while(ichoice != 1 && ichoice != 2){
            System.out.printf("Masukan tidak valid! Silakan ulangi...\nPilihan: ");
            ichoice = sc.nextInt();
        }
        if(ichoice == 1){
            System.out.printf("Masukkan panjang matriks persegi: \n");
            Integer n = sc.nextInt();
            System.out.printf("Masukkan matriks: \n");
            Matrix tempMatrix = new Matrix(n, n);
            for(int i = 0; i < n; i++){
                for(int j = 0; j < n; j++){
                    double x = sc.nextDouble();
                    tempMatrix.setElmt(i, j, x);
                }
            }
            M = tempMatrix;
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
                    Integer n = 0, m = 0;
                    while(file.hasNextLine()){
                        n++;
                        m = file.nextLine().split(" ").length;
                    }
                    file.close();

                    Matrix tempMatrix = new Matrix(n, m);
                    file = new Scanner(new File("../test/"+fileName));
                    for(int i = 0; i < n; i++){
                        for(int j = 0; j < m; j++){
                            double x = file.nextDouble();
                            tempMatrix.setElmt(i, j, x);
                        }
                    }
                    file.close();

                    M = tempMatrix;
                }
                catch(FileNotFoundException err){
                    err.printStackTrace();
                    found = false;
                }
            }
        }

        Matrix matrix = M;
        
        while ((matrix.getRowEff() != matrix.getColEff()))
        {
        	if (matrix.getRowEff() != matrix.getColEff())
	        	System.out.println("Matriks harus berbentuk persegi! Silahkan ulangi...");
        	matrix = Matrix.inputMatrix();
        }
        double det;
        if(choice == 1)
            det = DetOBE(matrix);
        else 
        	det = DetCofactor(matrix);

        // Cetak invers dari matriks
	    System.out.println("Determinan dari matriks input adalah: ");
        String st = Double.toString(det);
	    System.out.println(st);

        // Simpan jawaban dalam file
        System.out.printf("Apakah jawaban ingin disimpan dalam file?\n1. Ya\n2. Tidak\nPilihan: ");
	    choice = sc.nextInt();
	    while (choice != 1 && choice != 2)
	    {
	        System.out.printf("Masukan tidak valid! Silakan ulangi...\nPilihan: ");
	        choice = sc.nextInt();
	    }
        if (choice == 1) {
            String fileName = "";
            System.out.printf("Masukkan nama file: ");
            try {
                fileName = scFile.readLine();
            }
            catch (IOException err) {
                err.printStackTrace();
            }
            try {
                FileWriter file = new FileWriter("../test/"+fileName);
                file.write(st);
                file.close();
            }
            catch (IOException err) {
                err.printStackTrace();
            }
        }
	}

    public static double DetOBE(Matrix M){
        int i, j, k;
        double temp, factor, res;
        int nRow = M.getRowEff();

        Matrix M2 = new Matrix(nRow, nRow);
        for(i = 0; i < nRow; i++){
            for(j = 0; j < nRow; j++){
                temp = M.getElmt(i, j);
                M2.setElmt(i, j, temp);
            }
        }
        int swap = 1;
        double diag, cdt;
        for(i = 0; i < nRow-1; i++){
            diag = M2.getElmt(i, i);
            if(diag == 0){
                for(j = i+1; j < nRow; j++){
                    cdt = M2.getElmt(j, i);
                    if(cdt != 0){
                        for(k = 0; k < nRow; k++){
                            temp = M2.getElmt(i, k);
                            M2.setElmt(i, k, M2.getElmt(j, k));
                            M2.setElmt(j, k, temp);
                        }
                        swap *= -1;
                        break;
                    }
                }
            }
            for(j = i+1; j < nRow; j++){
                factor = M2.getElmt(j, i)/M2.getElmt(i, i);
                factor *= -1;
                for(k = 0; k < nRow; k++){
                    temp = M2.getElmt(i, k) * factor;
                    M2.setElmt(j, k, M2.getElmt(j, k) + temp);
                }
            }
        }
        res = swap;
        for(i = 0; i < nRow; i++){
            res *= M2.getElmt(i, i);
        }
        return res;
    }

    public static double DetCofactor(Matrix M){
        int i, j, im, jm, pass, nRow = M.getRowEff();
        double temp, factor, res;
        if(nRow == 1) res = M.getElmt(0, 0);
        else if(nRow == 2) res = M.getElmt(0, 0)*M.getElmt(1, 1)-M.getElmt(0, 1)*M.getElmt(1, 0);
        else{
            res = 0;
            Matrix M2 = new Matrix(nRow-1, nRow-1);
            for(pass = 0; pass < nRow; pass++){
                im = 1;
                for(i = 0; i < nRow-1; i++){
                    jm = 0;
                    for(j = 0;j < nRow-1; j++){
                        if(jm == pass) jm++;
                        temp = M.getElmt(im, jm);
                        M2.setElmt(i, j, temp);
                        jm++;
                    }
                    im++;
                }
                factor = M.getElmt(0, pass);
                res += factor*DetCofactor(M2)*(pass%2==1? -1 : 1);
            }
        }
        return res;
    }
}
