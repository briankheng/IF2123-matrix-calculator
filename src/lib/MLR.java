package lib;

public class MLR {

    public static void Solve(Matrix M){
        int i,j,k;
        int pass;
        double temp,sum;
        double res;
        int nRow=M.getRowEff();
        int nCol=M.getColEff();

        Matrix MUse = new Matrix(nRow, nRow+1);
        i=0;
        for(j=0;j<nRow+1;j++){
            if(j==0)MUse.setElmt(i, j, nCol);
            else{
                sum=0;
                for(k=0;k<nCol;k++){
                    temp=M.getElmt(j-1, k);
                    sum+=temp;
                }
                MUse.setElmt(i, j, sum);
            }
        }

        for(i=1;i<nRow;i++){
            for(j=0;j<nRow+1;j++){
                if(j==0){
                    sum=0;
                    for(k=0;k<nCol;k++){
                        temp=M.getElmt(i-1, k);
                        sum+=temp;
                    }
                    MUse.setElmt(i, j, sum);
                }
                else{
                    sum=0;
                    for(k=0;k<nCol;k++){
                        temp=M.getElmt(i-1, k) * M.getElmt(j-1, k);
                        sum+=temp;
                    }
                    MUse.setElmt(i, j, sum);
                }

            }

        }
        MUse.printMatrix();
        SPL solution = new SPL();
        solution.GaussJordan(MUse);
        double[] a = solution.x;

    }

}
