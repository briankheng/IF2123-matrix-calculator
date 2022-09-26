import java.util.Scanner;

import lib.BalikanAdjoin;
import lib.Cramer;
import lib.Determinant;
import lib.Kofaktor;
import lib.Matrix;
import lib.SPL;

public class Main {
    public static void main(String[] args){
        System.out.println("test");

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
        System.out.println("test");

        // double det;
        // // test determinant cofactor (HARUS SQUARE)
        // det=Determinant.DetCofactor(M);
        // System.out.println(det);

        // test adjoin (harus square jg)
        // Matrix Adj = BalikanAdjoin.Adjoin(M); Adj.printMatrix();
        Matrix Bal = BalikanAdjoin.Balikan(M); Bal.printMatrix();

        // Cramer.Solve(M);
    }
}
