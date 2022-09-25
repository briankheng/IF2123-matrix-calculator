import java.util.Scanner;

import lib.BalikanAdjoin;
import lib.Cramer;
import lib.Determinant;
import lib.Kofaktor;
import lib.MLR;
import lib.Matrix;
import lib.SPL;

public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int row = sc.nextInt();
        int col = sc.nextInt();
        Matrix M = new Matrix(row, col);
        
        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                double x = sc.nextDouble();
                M.setElmt(i, j, x);
            }
        }

        double det;
        //test determinant cofactor (HARUS SQUARE)
        
        det=Determinant.DetOBE(M);
        System.out.print("\n");
        System.out.print(det);

        // test adjoin (harus square jg)
        //Matrix Adj = BalikanAdjoin.Adjoin(M); Adj.printMatrix();
        //Matrix Bal = BalikanAdjoin.Balikan(M); Bal.printMatrix();

        //Cramer.Solve(M);

        //test Regresi Linear Ganda
        //matrix berupa tabel data
        //MLR.Solve(M);
    }
}
