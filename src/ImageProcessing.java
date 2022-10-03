import lib.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.lang.Math;

public class ImageProcessing
{
	/*****	Global variable	*****/
	// Double cubic interpolation variables
	public static int[][] columnExtendMatrix;
	public static int[][] columnInterpolateMatrix;
	public static int[][] rowExtendMatrix;
	public static int[][] rowInterpolateMatrix;

	public static int[][] interpolateMatrix;

	public static Matrix xValueMatrix;

	// Bicubic interpolation variables
	public static double[][] valueXandY = { {0	,0	,0	,0	,0	,36	,0	,0	,0	,0	,0	,0	,0	,0	,0	,0},
											{0	,0	,0	,0	,-12,-18,36	,-6	,0	,0	,0	,0	,0	,0	,0	,0},
											{0	,0	,0	,0	,18	,-36,18	,0	,0	,0	,0	,0	,0	,0	,0	,0},
											{0	,0	,0	,0	,-6	,18	,-18,6 	,0	,0	,0	,0	,0	,0	,0	,0},
											{0	,-12,0	,0	,0	,-18,0	,0	,0	,36	,0	,0	,0	,-6	,0	,0},
											{4	,6 	,-12,2	,6 	,9	,-18,3 	,-12,-18,36	,-6	,2	,3 	,-6	,1},
											{-6	,12	,-6	,0	,-9	,18	,-9	,0	,18	,-36,18	,0	,-3	,6 	,-3	,0},
											{2 	,-6 ,6 	,-2 ,3 	,-9 ,9 	,-3 ,-6 ,18 ,-18,6 	,1 	,-3 ,3 	,-1},
											{0 	,18 ,0 	,0 	,0 	,-36,0	,0	,0	,18	,0	,0	,0	,0	,0	,0},
											{-6	,-9	,18	,-3	,12	,18	,-36,6 	,-6	,-9	,18	,-3	,0	,0	,0	,0},
											{9	,-18,9	,0	,-18,36	,-18,0	,9	,-18,9	,0	,0	,0	,0	,0},
											{-3	,9	,-9	,3 	,6 	,-18,18	,-6	,-3	,9	,-9	,3 	,0	,0	,0	,0},
											{0	,-6	,0	,0	,0	,18	,0	,0	,0	,-18,0	,0	,0	,6 	,0	,0},
											{2 	,3 	,-6	,1 	,-6	,-9	,18	,-3	,6 	,9 	,-18,3 	,-2 ,-3 ,6 	,-1},
											{-3	,6 	,-3 ,0	,9	,-18,9	,0	,-9	,18	,-9	,0	,3 	,-6	,3 	,0},
											{1 	,-3	,3 	,-1 ,-3	,9	,-9	,3 	,3 	,-9	,9	,-3	,-1	,3 	,-3	,1} };



	/*****	DRIVER	*****/
	public static void ImageProcessingDriver()
	{
		// Constant values
		// String readDir = "../test/image/TC-ImageProcessing-4.png";
		// String writeDir = "../test/image/output.jpg";
		// int heightFactor = 64;
		// int widthFactor = 64;

		Scanner sc = new Scanner(System.in);

		System.out.printf("Masukkan nama file image yang ingin diperbesar: ");
		String readDir = "../test/image/" + sc.nextLine();

		System.out.printf("Masukkan nama file image hasil output: ");
		String writeDir = "../test/image/" + sc.nextLine();

		System.out.printf("Masukkan nilai faktor pembesaran tinggi: ");
		int heightFactor = sc.nextInt();

		System.out.printf("Masukkan nilai faktor pembesaran lebar: ");
		int widthFactor = sc.nextInt();


        System.out.println("PILIHAN\n1. Metode double cubic interpolation\n2. Metode bicubic interpolation");
		int choice = sc.nextInt();

		while (choice < 1 | choice > 2)
		{
            System.out.printf("Masukan tidak valid! Silakan ulangi...\n");
            choice = sc.nextInt();
		}

		if (choice == 1)
			doubleCubic(readDir, writeDir, heightFactor, widthFactor);
		else
			bicubic(readDir, writeDir, heightFactor, widthFactor);
	}



	/*****	Image resizing with bicubic interpolation 	*****/
	public static void bicubic(String readDir, String writeDir, int heightFactor, int widthFactor)
	// Input	: String berisi direktori baca dan tulis; dan integer berisi faktor pembesaran lebar dan tinggi
	// Output 	: Image hasil pembesaran tersimpan pada direktori tulis
	{
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

		// Create a 2d array with 4 added width and height to fit the extended matrix
		int[][] extendedMatrix = new int[height+4][width+4];

		// Fill the 4 corners with 4 corner values (0,0); (0,width-1); (height-1,0); (height-1,width-1)
		extendedMatrix[0][0] = bImage.getRGB(0,0);
		extendedMatrix[0][1] = bImage.getRGB(0,0);
		extendedMatrix[1][0] = bImage.getRGB(0,0);
		extendedMatrix[1][1] = bImage.getRGB(0,0);

		extendedMatrix[0][width+2] = bImage.getRGB(width-1,0);
		extendedMatrix[0][width+3] = bImage.getRGB(width-1,0);
		extendedMatrix[1][width+2] = bImage.getRGB(width-1,0);
		extendedMatrix[1][width+3] = bImage.getRGB(width-1,0);

		extendedMatrix[height+2][0] = bImage.getRGB(0,height-1);
		extendedMatrix[height+2][1] = bImage.getRGB(0,height-1);
		extendedMatrix[height+3][0] = bImage.getRGB(0,height-1);
		extendedMatrix[height+3][1] = bImage.getRGB(0,height-1);

		extendedMatrix[height+2][width+2] = bImage.getRGB(width-1,height-1);
		extendedMatrix[height+2][width+3] = bImage.getRGB(width-1,height-1);
		extendedMatrix[height+3][width+2] = bImage.getRGB(width-1,height-1);
		extendedMatrix[height+3][width+3] = bImage.getRGB(width-1,height-1);

		// Fill the left and right side with the adjacent value
		for (int i = 2; i < height + 2 ; i++)
		{
			extendedMatrix[i][0] = bImage.getRGB(0,i-2);
			extendedMatrix[i][1] = bImage.getRGB(0,i-2);
			extendedMatrix[i][width+2] = bImage.getRGB(width-1, i-2);
			extendedMatrix[i][width+3] = bImage.getRGB(width-1, i-2);
		}

		// Fill the top and bottom side with the adjacent value
		for (int i = 2; i < width + 2; i++)
		{
			extendedMatrix[0][i] = bImage.getRGB(i-2,0);
			extendedMatrix[1][i] = bImage.getRGB(i-2,0);
			extendedMatrix[height+2][i] = bImage.getRGB(i-2,height-1);
			extendedMatrix[height+3][i] = bImage.getRGB(i-2,height-1);
		}

		// Fit all remaining matrix
		for (int i = 2; i < height + 2; i++)
			for (int j = 2; j < width + 2; j++)
				extendedMatrix[i][j] = bImage.getRGB(j-2,i-2);

		// Create a matrix to fit the interpolated point
		int[][] interpolatedMatrix = new int[height*heightFactor][width*widthFactor];

		// Create 5 integer matrix with the length of 16 to fit all ARGB value
		int[] colorMatrix = new int[16];
		int[] alphaMatrix = new int[16];
		int[] redMatrix = new int[16];
		int[] greenMatrix = new int[16];
		int[] blueMatrix = new int[16];

		// Calculate the step and limit for index x and y
		double stepX = 1.0 / widthFactor; 	int limitX = width * widthFactor;
		double stepY = 1.0 / heightFactor; 	int limitY = height * widthFactor;

		// Loop through the interpolatedMatrix
		for (int i = 0; i < limitY; i++)
		{
			for (int j = 0; j < limitX; j++)
			{
				// Get the fitted (a,b) value
				double a = 2 + ((i + 0.5) * stepY) - 0.5;
				double b = 2 + ((j + 0.5) * stepX) - 0.5;

				// Calculate the lower and upper bounud for point (a,b) 
				int floorA = (int)Math.floor(a) - 1; int ceilA = (int)Math.ceil(a) + 1;
				int floorB = (int)Math.floor(b) - 1; int ceilB = (int)Math.ceil(b) + 1;

				// Put the value of (a,b) in extendedMatrix to the color matrix
				for (int k = floorA; k <= ceilA; k++)
					for (int l = floorB; l <= ceilB; l++)
						colorMatrix[4 * (k-floorA) + (l-floorB)] = extendedMatrix[k][l];

				// Decompile a point to its ARGB value, store in its matrix accordingly
				for (int k = 0; k < 16; k++)
				{
					alphaMatrix[k] = (colorMatrix[k] >> 24) & 0xff;
					redMatrix[k] = (colorMatrix[k] >> 16) & 0xff;
					greenMatrix[k] = (colorMatrix[k] >> 8) & 0xff;
					blueMatrix[k] = colorMatrix[k] & 0xff;
				}

				// Get the ARGB value of point (a,b)
				int alphaValue = checkValue(bicubicInterpolation(alphaMatrix, a-Math.floor(a), b-Math.floor(b))); 
				int redValue = checkValue(bicubicInterpolation(redMatrix, a-Math.floor(a), b-Math.floor(b)));
				int greenValue = checkValue(bicubicInterpolation(greenMatrix, a-Math.floor(a), b-Math.floor(b)));
				int blueValue = checkValue(bicubicInterpolation(blueMatrix, a-Math.floor(a), b-Math.floor(b)));

				// Fit the result into the interpolatedMatrix
				interpolatedMatrix[i][j] = ((alphaValue << 24) | (redValue << 16) | (greenValue << 8) | blueValue);
			}
		}

		// Multiply the image width and height value with its fitting factor
		width *= widthFactor; 
		height *= heightFactor;

		// Save image to disc 
		bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				bImage.setRGB(j,i,interpolatedMatrix[i][j]);

		try {
			ImageIO.write(bImage, "png", new File(writeDir));
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}

	public static double bicubicInterpolation(int[] zValue, double a, double b)
	// Input	: Array dengan isi 16 integer; berisi nilai-nilai pada titik f(a,b)
	//			  Double a dan b, yaitu nilai titik interpolasi yang ingin dicari
	// Output 	: Double berisi nilai f(a,b)
	{
		// Create a double array of length 16 to fit all 16 constants
		double[] constant = new double[16];

		// Compute the value of constant
		for (int i = 0; i < 16; i++)
		{
			constant[i] = 0;

			for (int j = 0; j < 16; j++)
				constant[i] += (valueXandY[i][j]/36) * (float)zValue[j];
		}

		// Multiply the constant array to a^i b^j for 0<=i<=3 and 0<=j<=3
		double value = 0;
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				value += constant[4*i + j] * Math.pow(a,i) * Math.pow(b,j);

		return value;
	}

	public static int checkValue(double val)
	// Input	: A double of either A/R/G/B value
	// Output 	: An integer set to the according value
	{
		// Check whether val is above 0xff, if so return 0xff
		if (val > 255)
			return 255;
		// Check whether val is below 0x0, if so return 0x0
		if (val < 0)
			return 0;

		// Return casted double to int otherwise
		return (int)val;
	}



	/*****	Image resizing with double cubic interpolation 	*****/
	public static void doubleCubic(String readDir, String writeDir, int heightFactor, int widthFactor)
	// Input	: String berisi direktori baca dan tulis; dan integer berisi faktor pembesaran lebar dan tinggi
	// Output 	: Image hasil pembesaran tersimpan pada direktori tulis
	{
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


		// Set RGB value to columnExtendMatrix, add 2 extra column filled with the first value and the last value 
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


		// Get interpolate result with widthFactor times the original matrix size 
		columnInterpolateMatrix = interpolatePoints(width, height, widthFactor, false);


		// Multiply width by widthFactor 
		width *= widthFactor;


		// Create rowExtendMatrix, add 2 extra row filled with the first value and the last value
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


		// Get interpolate result with heightFactor times the original matrix size 
		rowInterpolateMatrix = interpolatePoints(width, height, heightFactor, true);


		// Multiply height by heightFactor 
		height *= heightFactor;


		// Save image to disc 
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

	public static int[][] interpolatePoints(int width, int height, int factor, boolean interpolateHeight)
	// Input	: 3 integer with the value of image width, image height, and expansion factor; and one boolean that checks whether we're searching for
	// 			  height expansion
	// Output 	: One dimentional 2d expansioned array
	{
		/* Variable declaration and assignment */
		double step;
		int limit;

		if (interpolateHeight)
		{
			// If interpolateHeight == true, then we multiply the height by factor, set the limit (number of points) to height * factor, and set step (difference between two adjacent point) to height / factor 

			interpolateMatrix = new int[height*factor][width];
			limit = height * factor;
			step = 1.0 / factor;

			for (int j = 0; j < width; j++)
				for (int i = 0; i < limit; i++)
					interpolateMatrix[i][j] = getValue(j, 2 + ((i + 0.5) * step) - 0.5, true);
		}
		else 
		{
			// Do the same to width otherwise 
			interpolateMatrix = new int[height][width*factor];
			limit = width * factor;
			step = 1.0 / factor;

			for (int i = 0; i < height; i++)
				for (int j = 0; j < limit; j++)
					interpolateMatrix[i][j] = getValue(i, 2 + ((j + 0.5) * step) - 0.5, false);
		}

		return interpolateMatrix;
	}

	public static int getValue(int index, double x, boolean interpolateHeight)
	// Input	: Integer index, the index of extendedMatrix to be used; double x, the point that we want to find the value of;
	// 			  boolean interpolatedHeight, to check whether we are expanding the height
	// Output 	: Integer value, the ARGB value of point x
	{
		// Create a matrix to store the x value
		xValueMatrix = new Matrix(4,4);
		int[] abscissa = {-1, 0, 1, 2};
		
		// Calculate the xValueMatrix value 
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				xValueMatrix.setElmt(i, j, Math.pow(abscissa[i], j));
		
		// Get the inverse of xValueMatrix 
		xValueMatrix = Balikan.BalikanGaussJordan(xValueMatrix);

		// Create an array with length 4 to fit the value to be used
		int[] ordinate = new int[4];

		if (interpolateHeight)
		{
			ordinate[0] = rowExtendMatrix[(int)Math.floor(x)-1][index];
			ordinate[1] = rowExtendMatrix[(int)Math.floor(x)][index];
			ordinate[2] = rowExtendMatrix[(int)Math.ceil(x)][index];
			ordinate[3] = rowExtendMatrix[(int)Math.ceil(x)+1][index];
		}
		else
		{
			ordinate[0] = columnExtendMatrix[index][(int)Math.floor(x)-1];
			ordinate[1] = columnExtendMatrix[index][(int)Math.floor(x)];
			ordinate[2] = columnExtendMatrix[index][(int)Math.ceil(x)];
			ordinate[3] = columnExtendMatrix[index][(int)Math.ceil(x)+1];
		}
		
		return RGBValue(ordinate, x-Math.floor(x));
	}

	public static int RGBValue(int[] ordinate, double x)
	// Input	: A 2d array with the value of point 2 adjacent integer point to the left and the right of x; and a double x, 
	// 			  the point in the interpolated function we want to find
	// Output 	: An integer of value f(x)
	{
		// Create 9 new integer array with the length 4 to fit all RGB value
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

	public static int setValue(int value)
	// Input	: An integer of either A/R/G/B value
	// Output 	: An integer set to the according value
	{
		// Check whether the value is below 0x0, if so return 0x0
		if (value < 0)
			return 0;
		// Check whether the value is above 0xff, if so return 0xff
		if (value > 255)
			return 255;

		// Else return the intial value
		return value;
	}
}