package lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Matrix {
    private int row, col;
    private double[][] Matrix;

    /* Constructor */
    public Matrix(int row, int col){
        this.row = row;
        this.col = col;
        this.Matrix = new double[row][col];
    }

    /* Selector */
    public double getElmt(int i, int j){
        return this.Matrix[i][j];
    }
    public int getRowEff(){
        return this.row;
    }
    public int getColEff(){
        return this.col;
    }
    public void setElmt(int i, int j, double x){
        this.Matrix[i][j] = x;
    }

    /* Input */
    public static Matrix inputMatrix(){
        Scanner sc = new Scanner(System.in);
        BufferedReader scFile = new BufferedReader(new InputStreamReader(System.in));
        Matrix M = new Matrix(0, 0);
        System.out.printf("1. Masukan dari keyboard\n2. Masukan dari file\n");
        int choice = sc.nextInt();
        while(choice != 1 && choice != 2){
            System.out.printf("Masukan tidak valid! Silakan ulangi...\n");
            choice = sc.nextInt();
        }
        if(choice == 1){
            System.out.printf("Masukkan banyak baris: ");
            Integer n = sc.nextInt();
            System.out.printf("Masukkan banyak kolom: ");
            Integer m = sc.nextInt();
            System.out.printf("Masukkan matriks: \n");
            Matrix tempMatrix = new Matrix(n, m);
            for(int i = 0; i < n; i++){
                for(int j = 0; j < m; j++){
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
        return M;
    }

    /* Output */
    public void printMatrix()
    {
        for (int i = 0; i < this.row; i++)
        {
            for (int j = 0; j < this.col; j++)
            {
                System.out.print(getElmt(i,j)+" ");
            }
            System.out.print("\n");
        }
    }
}
