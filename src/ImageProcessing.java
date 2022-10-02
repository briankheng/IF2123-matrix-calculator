import lib.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.lang.Math;

// Haha i want to die lmfao

public class ImageProcessing
{
	/* Global variable */
	public static int[][] columnExtendMatrix;
	public static int[][] columnInterpolateMatrix;
	public static int[][] rowExtendMatrix;
	public static int[][] rowInterpolateMatrix;

	public static int[][] interpolateMatrix;

	public static Matrix xValueMatrix;



	/* Methods */
	public static int setValue(int value)
	{
		if (value < 0)
			return 0;
		if (value > 255)
			return 255;

		return value;
	}

	public static int RGBValue(int[] ordinate, double x)
	{

		int[] alpha = new int[4];	int[] alphaVal = new int[4];
		int[] red = new int[4]; 	int[] redVal = new int[4];
		int[] green = new int[4]; 	int[] greenVal = new int[4];
		int[] blue = new int[4]; 	int[] blueVal = new int[4]; 

		int[] value = new int[4];

		// Decompile a pixel into ARGB value
		for (int i = 0; i < 4; i++)
		{
			alpha[i] = (ordinate[i] >> 24) & 0xff;
			red[i] = (ordinate[i] >> 16) & 0xff;
			green[i] = (ordinate[i] >> 8) & 0xff;
			blue[i] = ordinate[i] & 0xff;
		}

		// Finding for constant of each ARGB value
		for (int i = 0; i < 4; i++)
		{
			// Set all value to 0
			alphaVal[i] = 0; 
			redVal[i] = 0; 
			greenVal[i] = 0; 
			blueVal[i] = 0;
			
			for (int j = 0; j < 4; j++)
			{
				// Add the value 
				alphaVal[i] += xValueMatrix.getElmt(i,j) * alpha[j] * Math.pow(x,i);
				redVal[i] += xValueMatrix.getElmt(i,j) * red[j] * Math.pow(x,i);
				greenVal[i] += xValueMatrix.getElmt(i,j) * green[j] * Math.pow(x,i);
				blueVal[i] += xValueMatrix.getElmt(i,j) * blue[j] * Math.pow(x,i);
			}
		}

		// Calculating the value of each constants
		for (int i = 0; i < 4; i++)
		{
			value[0] += alphaVal[i];
			value[1] += redVal[i];
			value[2] += greenVal[i];
			value[3] += blueVal[i];
		}

		return (setValue(value[0]) << 24) | (setValue(value[1]) << 16) | (setValue(value[2]) << 8) | setValue(value[3]);
	}

	public static int getValue(int index, double x, boolean interpolateHeight)
	{
		xValueMatrix = new Matrix(4,4);
		int[] abscissa = {-1, 0, 1, 2};
		int[] ordinate = new int[4];

		if (interpolateHeight)
		{
			ordinate[0] = rowExtendMatrix[(int)Math.floor(x)-1][index];
			ordinate[1] = rowExtendMatrix[(int)Math.floor(x)][index];
			ordinate[2] = rowExtendMatrix[(int)Math.ceil(x)][index];
			ordinate[3] = rowExtendMatrix[(int)Math.ceil(x)+1][index];

			/* Store the xValuematrix value */
			for (int i = 0; i < 4; i++)
				for (int j = 0; j < 4; j++)
					xValueMatrix.setElmt(i, j, Math.pow(abscissa[i], j));

			xValueMatrix = Balikan.BalikanGaussJordan(xValueMatrix);
		}
		else
		{
			ordinate[0] = columnExtendMatrix[index][(int)Math.floor(x)-1];
			ordinate[1] = columnExtendMatrix[index][(int)Math.floor(x)];
			ordinate[2] = columnExtendMatrix[index][(int)Math.ceil(x)];
			ordinate[3] = columnExtendMatrix[index][(int)Math.ceil(x)+1];

			/* Store the xValueMatrix value */
			for (int i = 0; i < 4; i++)
				for (int j = 0; j < 4; j++)
					xValueMatrix.setElmt(i, j, Math.pow(abscissa[i], j));

			/* Get the inverse of xValueMatrix */
			xValueMatrix = Balikan.BalikanGaussJordan(xValueMatrix);
		}
		
		return RGBValue(ordinate, x-Math.floor(x));
	}

	public static int[][] interpolatePoints(int width, int height, int factor, boolean interpolateHeight)
	{
		/* Variable declaration and assignment */
		double step;
		int limit;

		if (interpolateHeight)
		{
			/* If interpolateHeight == true, then we multiply the height by factor, set the limit (number of points) to height * factor, and set step (difference between two adjacent point) to height / factor */

			interpolateMatrix = new int[height*factor][width];
			limit = height * factor;
			step = 1.0 / factor;

			for (int j = 0; j < width; j++)
				for (int i = 0; i < limit; i++)
					interpolateMatrix[i][j] = getValue(j, 2 + ((i + 0.5) * step) - 0.5, true);
		}
		else 
		{
			/* Do the same to width otherwise */
			interpolateMatrix = new int[height][width*factor];
			limit = width * factor;
			step = 1.0 / factor;

			for (int i = 0; i < height; i++)
				for (int j = 0; j < limit; j++)
					interpolateMatrix[i][j] = getValue(i, 2 + ((j + 0.5) * step) - 0.5, false);
		}

		return interpolateMatrix;
	}

	public static void main(String[] args)
	{
		// Constant values
		String readDir = "../test/image/test.png";
		String writeDir = "../test/image/output.jpg";
		int widthFactor = 10;
		int heightFactor = 10;

		// Read image
		BufferedImage bImage = null;

		try {
			bImage = ImageIO.read(new File(readDir));
		}
		catch (IOException e) {
			System.out.println(e);
		}

		int width = bImage.getWidth();
		int height = bImage.getHeight();


		/* Set RGB value to columnExtendMatrix, add 2 extra column filled with the first value and the last value */
		columnExtendMatrix = new int[height][width+4];
		
		for (int i = 0; i < height; i++)
		{
			columnExtendMatrix[i][0] = bImage.getRGB(0,i);
			columnExtendMatrix[i][1] = bImage.getRGB(0,i);

			for (int j = 2; j < width + 2; j++)
				columnExtendMatrix[i][j] = bImage.getRGB(j-2, i);

			columnExtendMatrix[i][width+2] = bImage.getRGB(width-1, i);
			columnExtendMatrix[i][width+3] = bImage.getRGB(width-1, i);
		}


		/* Get interpolate result with widthFactor times the original matrix size */
		columnInterpolateMatrix = interpolatePoints(width, height, widthFactor, false);


		/* Multiply width by widthFactor */
		width *= widthFactor;


		/* Create rowExtendMatrix, add 2 extra row filled with the first value and the last value */
		rowExtendMatrix = new int[height+4][width];

		for (int j = 0; j < width; j++)
		{
			rowExtendMatrix[0][j] = columnInterpolateMatrix[0][j];
			rowExtendMatrix[1][j] = columnInterpolateMatrix[0][j];

			for (int i = 2; i < height + 2; i++)
				rowExtendMatrix[i][j] = columnInterpolateMatrix[i-2][j];

			rowExtendMatrix[height+2][j] = columnInterpolateMatrix[height-1][j];
			rowExtendMatrix[height+3][j] = columnInterpolateMatrix[height-1][j];
		}


		/* Get interpolate result with heightFactor times the original matrix size */
		rowInterpolateMatrix = interpolatePoints(width, height, heightFactor, true);


		/* Multiply height by heightFactor */
		height *= heightFactor;


		/* Save image to disc */
		bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				bImage.setRGB(j,i,rowInterpolateMatrix[i][j]);

		try {
			ImageIO.write(bImage, "png", new File(writeDir));
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}
}