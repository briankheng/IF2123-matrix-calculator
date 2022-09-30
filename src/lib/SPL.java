package lib;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

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

    public static void DriverSPL(){
        Scanner sc = new Scanner(System.in);
        BufferedReader scFile = new BufferedReader(new InputStreamReader(System.in));
        System.out.printf("PILIHAN\n1. Metode eliminasi Gauss\n2. Metode eliminasi Gauss-Jordan\n3. Metode matriks balikan\n4. Kaidah Cramer\n");
        Integer choice = sc.nextInt();
        while(choice < 1 || choice > 4){
            System.out.printf("Masukan tidak valid! Silakan ulangi...\n");
            choice = sc.nextInt();
        }
        Matrix M = Matrix.inputMatrix();
        SPL solution = new SPL();
        if(choice == 1){    
            solution.Gauss(M);
        }
        else if(choice == 2){
            solution.GaussJordan(M);
        }
        else if(choice == 3){
            solution.InversMatrix(M);
        }
        else{
            solution.Cramer(M);
            // Kaidah Cramer
        }
        for(int i = 0; i < solution.nEff; i++){
            System.out.printf(solution.ans[i]);
        }
        // Simpan jawaban dalam file
        System.out.printf("Apakah jawaban ingin disimpan dalam file?\n1. Ya\n2. Tidak\n");
        choice = sc.nextInt();
        while(choice != 1 && choice != 2){
            System.out.printf("Masukan tidak valid! Silakan ulangi...\n");
            choice = sc.nextInt();
        }
        if(choice == 1){
            String fileName = "";
            System.out.printf("Masukkan nama file: ");
            try{
                fileName = scFile.readLine();
            }
            catch(IOException err){
                err.printStackTrace();
            }
            try{
                FileWriter file = new FileWriter("../test/"+fileName);
                for(int i= 0; i < solution.nEff; i++){
                    file.write(solution.ans[i]);
                }
                file.close();
            }
            catch(IOException err){
                err.printStackTrace();
            }
        }
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
            M = EselonBarisTereduksi(M);
            SolveManySolution(M);
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
            SolveManySolution(M);
        }

        for (int i = 0; i < M.getRowEff(); i++)
        {
            for (int j = 0; j < M.getColEff(); j++)
            {
                System.out.printf(M.getElmt(i,j) + " ");
            }
            System.out.printf("\n");
        }
    }
    
    public void InversMatrix(Matrix M){
        if(M.getRowEff() != M.getColEff()-1 || Determinant.DetCofactor(M) == 0){
            this.ans[0] = "Tidak dapat menggunakan metode matriks balikan!\n";
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
        A = Balikan.BalikanAdjoin(A);
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

    public void Cramer(Matrix M){
        if(M.getRowEff() != M.getColEff()-1 || Determinant.DetCofactor(M) == 0){
            this.ans[0] = "Tidak dapat menggunakan metode Cramer!\n";
            this.nEff = 1;
            return;
        }
        
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
            this.x[j] = res;
            this.ans[j] = "X"+Integer.toString(j+1)+" = "+Double.toString(this.x[j])+"\n";
            //reverse change
            for(i=0;i<nRow;i++){
                temp=M.getElmt(i, j);
                MUse.setElmt(i, j, temp);
            }

        }
        this.nEff=nRow;
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

    public void SolveManySolution(Matrix M){
        boolean[] visited = new boolean[M.getColEff()-1];
        char[] parametric = new char[M.getColEff()-1];
        Integer cur = 17;
        this.nEff = M.getColEff()-1;
        for(int i = 0; i < M.getColEff()-1; i++) visited[i] = false;
        for(int i = 0; i < M.getRowEff(); i++){
            for(int j = i; j < M.getColEff()-1; j++){
                if(M.getElmt(i, j) == 1){
                    visited[j] = true;
                    String temp = "";
                    if(Math.abs(M.getElmt(i, M.getColEff()-1)) > 1e-8){
                        temp += Double.toString(M.getElmt(i, M.getColEff()-1));
                    }
                    for(int k = j+1; k < M.getColEff()-1; k++){
                        if(Math.abs(M.getElmt(i, k)) > 1e-8){
                            if(!visited[k]){
                                visited[k] = true;
                                parametric[k] = (char)(97+cur);
                                this.ans[k] = "X" + Integer.toString(k+1) + " = " + Character.toString(parametric[k]) + "\n";
                                cur = (cur+1)%26;
                            }
                            if(M.getElmt(i, k) > 0) temp += (temp.length() == 0 ? "" : " - ") + (Math.abs(M.getElmt(i, k)) != 1.0 ? Double.toString(Math.abs(M.getElmt(i, k))) : "") + Character.toString(parametric[k]);
                            else temp += (temp.length() == 0 ? "" : " + ") + (Math.abs(M.getElmt(i, k)) != 1.0 ? Double.toString(Math.abs(M.getElmt(i, k))) : "") + Character.toString(parametric[k]);
                        }
                    }
                    this.ans[j] = "X" + Integer.toString(j+1) + " = " + temp + "\n";
                    break;
                }
                else{
                    if(!visited[j]){
                        visited[j] = true;
                        parametric[j] = (char)(97+cur);
                        this.ans[j] = "X" + Integer.toString(j+1) + " = " + Character.toString(parametric[j]) + "\n";
                        cur = (cur+1)%26;
                    }
                }
            }
        }
    }
}
