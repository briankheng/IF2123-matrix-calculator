package lib;

public class Cramer {
    public static void Solve(Matrix M){
        int i,j,k;
        int pass;
        double temp,factor;
        double res;
        int nRow=M.getRowEff();

        Matrix MUse = new Matrix(nRow, nRow);
        for(i=0;i<nRow;i++){
            for(j=0;j<nRow;j++){
                temp=M.getElmt(i, j);
                MUse.setElmt(i, j, temp);
            }
        }

        double det;
        double xdet;
        det=Determinant.DetCofactor(MUse);
        for(j=0;j<nRow;j++){
            
            for(i=0;i<nRow;i++){
                temp=M.getElmt(i, nRow);
                MUse.setElmt(i, j, temp);
            }
            xdet=Determinant.DetCofactor(MUse);
            res=xdet/det;
            System.out.print(res);
            System.out.println();

            //reverse change
            for(i=0;i<nRow;i++){
                temp=M.getElmt(i, j);
                MUse.setElmt(i, j, temp);
            }




        }





    }

}
