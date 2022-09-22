public class BalAd
{
	public float[][] Adjoin(float[][] matrix, int rowSize, int colSize)
	{
		float[][] transpose = new float [colSize][rowSize];

		for (int i = 0; i < rowSize; i++)
			for (int j = 0; j < colSize; j++)
				transpose[i][j] = matrix[j][i];

		return transpose;
	}

	public float[][] Balikan(float[][] matrix, int rowSize, int colSize)
	{
		// float determinan = determinan(matrix, rowSize, colSize);
		float[][] matrixAdjoin = Adjoin(matrix, rowSize, colSize);

		for (int i = 0; i < rowSize; i++)
			for (int j = 0; j < colSize; j++)
				matrixAdjoin[i][j] /= determinan;

		return matrixAdjoin;
	}
}