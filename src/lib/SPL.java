package lib;

import java.util.*;

public class SPL {
    public static void Gauss(Matrix M){
        // Kasus normal, persamaan > variabel, variabel > persamaan
        // no solution 0 0 0 | -1
        // many solution variabel > persamaan or 0 0 0 | 0 -> pake parametrik
        // one solution
        int idxRow = 0;
        for(int k = 0; k < M.getColEff() - 1; k++){
            if(idxRow == M.getRowEff()) break;
            boolean isZero = (M.getElmt(idxRow, k) == 0);
            int tempRow = idxRow;
            while(isZero && tempRow+1 < M.getRowEff()){
                tempRow++;
                if(M.getElmt(tempRow, k) != 0){
                    // Swap row
                    double[] temp = new double[M.getColEff()];
                    for(int j = 0; j < M.getColEff(); j++){
                        temp[j] = M.getElmt(idxRow, j);
                    }
                    for(int j = 0; j < M.getColEff(); j++){
                        M.setElmt(idxRow, j, M.getElmt(tempRow, j));
                        M.setElmt(tempRow, j, temp[j]);
                    }
                    isZero = false;
                }
            }
            
            if(!isZero){
                double factor = M.getElmt(idxRow, k);
                for(int j = k; j < M.getColEff(); j++){
                    M.setElmt(idxRow, j, M.getElmt(idxRow, j)/factor);
                }
                
                for(int i = idxRow + 1; i < M.getRowEff(); i++){
                    if(M.getElmt(i, k) != 0){
                        double diff = M.getElmt(i, k);
                        for(int j = k; j < M.getColEff(); j++){
                            M.setElmt(i, j, M.getElmt(i, j) - (M.getElmt(idxRow, j) * diff));
                        }
                    }
                }
                idxRow++;
            }
        }
        // Check no solution
        for(int i = 0; i < M.getRowEff(); i++){
            boolean allZero = true;
            for(int j = 0; j < M.getColEff() - 1; j++){
                if(M.getElmt(i, j) != 0) {
                    allZero = false;
                }
            }
            if(allZero && M.getElmt(i, M.getColEff()-1) != 0){
                System.out.println("Solusi tidak ada.");
                return;
            }
        }
        // Backward substitution (Single solution)
        double[] x = new double[M.getRowEff()];
        for(int i = M.getRowEff()-1; i >= 0; i--){
            x[i] = M.getElmt(i, M.getRowEff());
            for(int j = i+1; j < M.getRowEff(); j++){
                x[i] -= M.getElmt(i, j) * x[j];
            }
        }
        // Solusi parametrik??
    }

    public static void GaussJordan(Matrix M){
        int idxRow = 0;
        for(int k = 0; k < M.getColEff() - 1; k++){
            if(idxRow == M.getRowEff()) break;
            boolean isZero = (M.getElmt(idxRow, k) == 0);
            int tempRow = idxRow;
            while(isZero && tempRow+1 < M.getRowEff()){
                tempRow++;
                if(M.getElmt(tempRow, k) != 0){
                    // Swap row
                    double[] temp = new double[M.getColEff()];
                    for(int j = 0; j < M.getColEff(); j++){
                        temp[j] = M.getElmt(idxRow, j);
                    }
                    for(int j = 0; j < M.getColEff(); j++){
                        M.setElmt(idxRow, j, M.getElmt(tempRow, j));
                        M.setElmt(tempRow, j, temp[j]);
                    }
                    isZero = false;
                }
            }
            
            if(!isZero){
                double factor = M.getElmt(idxRow, k);
                for(int j = k; j < M.getColEff(); j++){
                    M.setElmt(idxRow, j, M.getElmt(idxRow, j)/factor);
                }
                
                for(int i = 0; i < M.getRowEff(); i++){
                    if(M.getElmt(i, k) != 0 && i != idxRow){
                        double diff = M.getElmt(i, k);
                        for(int j = k; j < M.getColEff(); j++){
                            M.setElmt(i, j, M.getElmt(i, j) - (M.getElmt(idxRow, j) * diff));
                        }
                    }
                }
                idxRow++;
            }
        }
        // Check no solution
        for(int i = 0; i < M.getRowEff(); i++){
            boolean allZero = true;
            for(int j = 0; j < M.getColEff() - 1; j++){
                if(M.getElmt(i, j) != 0) {
                    allZero = false;
                }
            }
            if(allZero && M.getElmt(i, M.getColEff()-1) != 0){
                System.out.println("Solusi tidak ada.");
                return;
            }
        }
        // Backward substitution (Single solution)
        double[] x = new double[M.getRowEff()];
        for(int i = M.getRowEff()-1; i >= 0; i--){
            x[i] = M.getElmt(i, M.getRowEff());
        }
        // Solusi parametrik??
    }
    public static void InversMatrix(Matrix M){
        // Kasus normal row == col
        if(M.getColEff()-1 != M.getRowEff()){
            System.out.println("Tidak dapat menggunakan metode invers matrix!");
            return;
        }
        Matrix A = new Matrix(M.getRowEff(), M.getRowEff());
        Matrix B = new Matrix(M.getRowEff(), 1);
        for(int i=0;i<M.getRowEff();i++){
            for(int j=0;j<M.getColEff();j++){
                if(j==M.getRowEff()) B.setElmt(i, 0, M.getElmt(i, j));
                else A.setElmt(i, j, M.getElmt(i, j));
            }
        }
        A = BalikanAdjoin.Balikan(A);
        Matrix res = new Matrix(M.getRowEff(), 1);
        for(int i = 0; i < M.getRowEff(); i++){
            res.setElmt(i, 0, 0);
            for(int k = 0; k < A.getColEff(); k++){
                res.setElmt(i, 0, res.getElmt(i, 0)+A.getElmt(i, k)*B.getElmt(k, 0));
            }
        }
        double[] x = new double[A.getRowEff()];
        for(int i = 0; i < A.getRowEff(); i++){
            x[i] = res.getElmt(i, 0);
        }
    }
}
