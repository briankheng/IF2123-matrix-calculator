package lib;

import java.util.*;

public class BalikanAdjoin
{
	public static Matrix Adjoin(Matrix matrix)
	/* Matriks harus berbentuk persegi */
	/* Input berupa matrix biasa */
	/* Output berupa matrix adjoin */
	{
		int size = matrix.getRowEff();

		Matrix kofaktor = Kofaktor.Kofaktor(matrix);
		Matrix transpose = new Matrix(size,size);

		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				transpose.setElmt(i,j,kofaktor.getElmt(j,i));

		return transpose;
	}

	public static Matrix Balikan(Matrix matrix)
	/* Matriks harus berbentuk persegi */
	/* Input berupa matrix biasa */
	/* Output berupa invers matrix */
	{
		int size = matrix.getRowEff();

		Matrix matrixAdjoin = Adjoin(matrix);
		double determinan = Determinant.DetCofactor(matrix);

		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				matrixAdjoin.setElmt(i,j,matrixAdjoin.getElmt(i,j)/determinan);

		return matrixAdjoin;
	}
}