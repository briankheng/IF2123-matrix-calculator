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
        for(int i=0;i<M.getRowEff();i++){
            for(int j=0;j<M.getColEff();j++){
                System.out.print(M.getElmt(i, j));
                System.out.print(" ");
               
            } System.out.println();
        }
    }
}
