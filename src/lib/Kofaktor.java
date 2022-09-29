package lib;

public class Kofaktor 
{
	public static double findDetKofaktor(Matrix matrixKofaktor, int row, int col)
	/* Matriks harus berbentuk persegi */
	{
		int size = matrixKofaktor.getRowEff();

		Matrix matrix = new Matrix(size-1,size-1);

		int i = 0;
		while (i < size)
		{
			if (i == row)
			{
				i++; continue;
			}

			int j = 0;
			while (j < size)
			{
				if (j == col)
				{
					j++; continue;
				}

				if (i < row)
				{
					if (j < col)
						matrix.setElmt(i,j,matrixKofaktor.getElmt(i,j));
					else
						matrix.setElmt(i,j-1,matrixKofaktor.getElmt(i,j));
				}
				else
				{
					if (j < col)
						matrix.setElmt(i-1,j,matrixKofaktor.getElmt(i,j));
					else
						matrix.setElmt(i-1,j-1,matrixKofaktor.getElmt(i,j));
				}
				j++;
			}
			i++;
		}

		return Determinant.DetCofactor(matrix);
	}

	public static Matrix Kofaktor(Matrix matrix)
	/* Matriks harus berbentuk persegi */
	{
		int size = matrix.getRowEff();

		Matrix matrixKofaktor = new Matrix(size,size);
		
		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				matrixKofaktor.setElmt(i,j,findDetKofaktor(matrix,i,j));
				if ((i+j)%2 == 1 && matrixKofaktor.getElmt(i,j) != 0)
					matrixKofaktor.setElmt(i,j,matrixKofaktor.getElmt(i,j)*-1);
			}
		}

		return matrixKofaktor;
	}
}