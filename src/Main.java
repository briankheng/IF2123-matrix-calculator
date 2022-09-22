import java.util.Scanner;

import lib.Determinant;
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
        // test determinant cofactor (HARUS SQUARE)
        det=Determinant.DetCofactor(M);
        System.out.print(det);
    }
}
