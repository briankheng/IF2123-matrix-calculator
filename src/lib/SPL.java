package lib;

public class SPL {
    public double[] x;
    public String[] ans;
    public Integer nEff;

    /* Constructor */
    public SPL(){
        this.ans = new String[100001];
        this.x = new double[100001];
        this.nEff = 0;
    }

    public void Gauss(Matrix M){
        M = EselonBaris(M);

        // Check no solution
        for(int i = 0; i < M.getRowEff(); i++){
            boolean allZero = true;
            for(int j = 0; j < M.getColEff() - 1; j++){
                if(Math.abs(M.getElmt(i, j)) > 1e-8) {
                    allZero = false;
                }
            }
            if(allZero && Math.abs(M.getElmt(i, M.getColEff()-1)) > 1e-8){
                this.ans[0] = "Solusi tidak ada!\n";
                this.nEff = 1;
                return;
            }
        }

        // Check single or many solution
        boolean isSingleSolution = true;
        if(M.getRowEff() < M.getColEff()-1) isSingleSolution = false;
        if(isSingleSolution){
            for(int i = 0; i < M.getColEff()-1; i++){
                if(M.getElmt(i, i) != 1) isSingleSolution = false;
            }
        }
        
        // Backward substitution (Single solution)
        if(isSingleSolution){
            for(int i = M.getColEff()-2; i >= 0; i--){
                this.x[i] = M.getElmt(i, M.getColEff()-1);
                for(int j = i+1; j < M.getColEff()-1; j++){
                    this.x[i] -= M.getElmt(i, j) * this.x[j];
                }
            }
            for(int i = 0; i < M.getColEff()-1; i++){
                this.ans[i] = "X"+Integer.toString(i+1)+" = "+Double.toString(this.x[i])+"\n";
            }
            this.nEff = M.getColEff()-1;
        }
        else{
            // Many solution
            
        }
    }

    public void GaussJordan(Matrix M){
        M = EselonBarisTereduksi(M);

        // Check no solution
        for(int i = 0; i < M.getRowEff(); i++){
            boolean allZero = true;
            for(int j = 0; j < M.getColEff() - 1; j++){
                if(M.getElmt(i, j) != 0) {
                    allZero = false;
                }
            }
            if(allZero && Math.abs(M.getElmt(i, M.getColEff()-1)) > 1e-8){
                this.ans[0] = "Solusi tidak ada!\n";
                this.nEff = 1;
                return;
            }
        }

        // Check single or many solution
        boolean isSingleSolution = true;
        if(M.getRowEff() < M.getColEff()-1) isSingleSolution = false;
        if(isSingleSolution){
            for(int i = 0; i < M.getColEff()-1; i++){
                if(M.getElmt(i, i) != 1) isSingleSolution = false;
            }
        }
        
        // Backward substitution (Single solution)
        if(isSingleSolution){
            for(int i = M.getColEff()-2; i >= 0; i--){
                this.x[i] = M.getElmt(i, M.getColEff()-1);
            }
            for(int i = 0; i < M.getColEff()-1; i++){
                this.ans[i] = "X"+Integer.toString(i+1)+" = "+Double.toString(this.x[i])+"\n";
            }
            this.nEff = M.getColEff()-1;
        }
        else{
            // Many solution

        }
    }
    
    public void InversMatrix(Matrix M){
        if(M.getRowEff() != M.getColEff()-1 || Determinant.DetCofactor(M) == 0){
            this.ans[0] = "Tidak dapat menggunakan metode matriks balikan!";
            this.nEff = 1;
            return;
        }
        Matrix A = new Matrix(M.getRowEff(), M.getRowEff());
        Matrix B = new Matrix(M.getRowEff(), 1);
        for(int i = 0; i < M.getRowEff(); i++){
            for(int j = 0; j < M.getColEff(); j++){
                if(j == M.getColEff()-1) B.setElmt(i, 0, M.getElmt(i, j));
                else A.setElmt(i, j, M.getElmt(i, j));
            }
        }
        A = BalikanAdjoin.Balikan(A);
        Matrix res = new Matrix(M.getRowEff(), 1);
        for(int i = 0; i < A.getRowEff(); i++){
            res.setElmt(i, 0, 0);
            for(int k = 0; k < A.getColEff(); k++){
                res.setElmt(i, 0, res.getElmt(i, 0)+A.getElmt(i, k)*B.getElmt(k, 0));
            }
        }
        for(int i = 0; i < res.getRowEff(); i++){
            this.x[i] = res.getElmt(i, 0);
        }
        for(int i = 0; i < res.getRowEff(); i++){
            this.ans[i] = "X"+Integer.toString(i+1)+" = "+Double.toString(this.x[i])+"\n";
        }
        this.nEff = res.getRowEff();
    }

    /* Fungsi */
    public Matrix EselonBaris(Matrix M){
        int idxRow = 0;
        for(int k = 0; k < M.getColEff() - 1; k++){
            if(idxRow == M.getRowEff()) break;
            boolean isZero = (Math.abs(M.getElmt(idxRow, k)) < 1e-8);
            int tempRow = idxRow;
            while(isZero && tempRow+1 < M.getRowEff()){
                tempRow++;
                if(Math.abs(M.getElmt(tempRow, k)) > 1e-8){
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
                    if(Math.abs(M.getElmt(i, k)) > 1e-8){
                        double diff = M.getElmt(i, k);
                        for(int j = k; j < M.getColEff(); j++){
                            M.setElmt(i, j, M.getElmt(i, j) - (M.getElmt(idxRow, j) * diff));
                        }
                    }
                }
                idxRow++;
            }
        }
        return M;
    }

    public Matrix EselonBarisTereduksi(Matrix M){
        M = EselonBaris(M);
        for(int i = M.getRowEff()-1; i >= 0; i--){
            for(int j = 0; j < M.getColEff()-1; j++){
                if(M.getElmt(i, j) == 1){
                    for(int k = i-1; k >= 0; k--){
                        double diff = M.getElmt(k, j);
                        for(int l = j; l < M.getColEff(); l++){
                            M.setElmt(k, l, M.getElmt(k, l) - (M.getElmt(i, l) * diff));
                        }
                    }
                    break;
                }
            }
        }
        return M;
    }
}
