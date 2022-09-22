package lib;

import java.util.*;

public class Determinant{

    public static double DetCofactor(Matrix M){
        int i,j,im,jm;
        int pass;
        double temp,factor;
        double res;
        int nRow=M.getRowEff();
        if(nRow==1)res=M.getElmt(0, 0);
        else if(nRow==2)res=M.getElmt(0, 0)*M.getElmt(1, 1)-M.getElmt(0, 1)*M.getElmt(1, 0);
        else{
            res=0;
            Matrix M2 = new Matrix(nRow-1, nRow-1);

            for(pass=0;pass<nRow;pass++){
                im=1;
                for(i=0;i<nRow-1;i++){
                    jm=0;
                    for(j=0;j<nRow-1;j++){
                        if(jm==pass) jm++;
                        temp=M.getElmt(im, jm);
                        M2.setElmt(i, j, temp);
                        jm++;
                    }
                    im++;
                }
                factor=M.getElmt(0, pass);
                res+=factor*DetCofactor(M2)*(pass%2==1?-1:1);

            }


        }
        return res;


    }

}
