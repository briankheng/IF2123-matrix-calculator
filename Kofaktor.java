// last update: 20th september 2022
// p sure this one is working as intended.

public class Kofaktor 
{
	public static float findDetKofaktor(float[][] matrixKofaktor, int row, int col, int rowSize, int colSize)
	{
		float[][] matrix = new float[rowSize-1][colSize-1];

		int i = 0;
		while (i < rowSize)
		{
			if (i == row)
			{
				i++; continue;
			}

			int j = 0;
			while (j < colSize)
			{
				if (j == col)
				{
					j++; continue;
				}

				if (i < row)
				{
					if (j < col)
						matrix[i][j] = matrixKofaktor[i][j];
					else
						matrix[i][j-1] = matrixKofaktor[i][j];
				}
				else
				{
					if (j < col)
						matrix[i-1][j] = matrixKofaktor[i][j];
					else
						matrix[i-1][j-1] = matrixKofaktor[i][j];
				}
				j++;
			}
			i++;
		}

		// return determinan(matrixKofaktor);
	}

	public float[][] Kofaktor(float[][] matrix, int rowSize, int colSize)
	{
		float[][] matrixKofaktor = new float[rowSize][colSize];
		
		for (int i = 0; i < rowSize; i++)
			for (int j = 0; j < colSize; j++)
				matrixKofaktor[i][j] = findDetKofaktor(matrix, i, j, rowSize. colSize);

		return matrixKofaktor;
	}

	public 
}