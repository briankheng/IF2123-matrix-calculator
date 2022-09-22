package lib;

public class Matrix {
    private int row, col;
    private double[][] Matrix;

    /* Constructor */
    public Matrix(int row, int col){
        this.row = row;
        this.col = col;
        this.Matrix = new double[row][col];
    }

    /* Selector */
    public double getElmt(int i, int j){
        return this.Matrix[i][j];
    }
    public int getRowEff(){
        return this.row;
    }
    public int getColEff(){
        return this.col;
    }
    public void setElmt(int i, int j, double x){
        this.Matrix[i][j] = x;
    }

    /* Output */
    public void printMatrix()
    {
        for (int i = 0; i < this.row; i++)
        {
            for (int j = 0; j < this.col; j++)
            {
                System.out.print(getElmt(i,j)+" ");
            }
            System.out.print("\n");
        }
    }
}
