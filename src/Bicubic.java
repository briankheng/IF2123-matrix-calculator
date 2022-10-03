import lib.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.lang.Math;

public class Bicubic
{
	/*****	DRIVER	*****/
	public static void SolveBicubic()
	// Input matrix 4x4, berisi nilai f(i,j) dan nilai (a,b)
	// Output nilai f(a,b)
	{
		// Read file dari input user
		Scanner sc = new Scanner(System.in);
		BufferedReader scFile = new BufferedReader(new InputStreamReader(System.in));

		// Variables
		Matrix zValue = new Matrix(4,4);
		double a = 0, b = 0;
		int n = 0;

		// Read the values from the file
		Boolean found = false;
		while (!found)
		{
			found = true;
			String fileName = "";
			System.out.printf("Masukkan nama file: ");
			try {
				fileName = scFile.readLine();
			}
			catch (IOException err) {
				err.printStackTrace();
			}
			try {
                Scanner file = new Scanner(new File("../test/"+fileName));
				while (file.hasNextDouble())
				{
					if (n == 4)
					{
						a = file.nextDouble();
						b = file.nextDouble();
					}
					else
					{
						zValue.setElmt(0,n,file.nextDouble());
						zValue.setElmt(1,n,file.nextDouble());
						zValue.setElmt(2,n,file.nextDouble());
						zValue.setElmt(3,n,file.nextDouble());
					}
					n++;
				}
			}
			catch (FileNotFoundException err) {
				err.printStackTrace();
				found = false;
			}
		}

		// Get the value from getValue function
		double value = getValue(zValue, a, b);

		System.out.println("f(" + a + "," + b + ") = " + value);

		// Save the result into a file
        System.out.printf("Apakah jawaban ingin disimpan dalam file?\n1. Ya\n2. Tidak\nPilihan: ");
        int choice = sc.nextInt();
        while (choice != 1 && choice != 2)
        {
            System.out.printf("Masukan tidak valid! Silakan ulangi...\nPilihan: ");
            choice = sc.nextInt();
        }
        if (choice == 1)
        {
            String fileName = "";
            System.out.printf("Masukkan nama file: ");
            try {
                fileName = scFile.readLine();
            }
            catch (IOException err) {
                err.printStackTrace();
            }
            try {
                FileWriter file = new FileWriter("../test/"+fileName);
                file.write("f(" + a + "," + b + ") = " + value + "\n");
                file.close();
            }
            catch (IOException err) {
                err.printStackTrace();
            }
        }
	}


	public static double getValue(Matrix matrix, double a, double b)
	// Input 	: Matrix m with values of 2 adjacent integer value of point (a,b); Double a and b, the x and y point respectively
	// Output	: The value of f(a,b)
	{
		// Constant value
		double[][] XandYValue = {
								{0	,0	,0	,0	,0	,36	,0	,0	,0	,0	,0	,0	,0	,0	,0	,0},
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
								{1 	,-3	,3 	,-1 ,-3	,9	,-9	,3 	,3 	,-9	,9	,-3	,-1	,3 	,-3	,1}
							 };

		// Variables
		Matrix constants = new Matrix(4,4);
		double sum = 0;

		// Loop through the XandYValue to find the sum
		for (int i = 0; i < 16; i++)
		{
			sum = 0;
			for (int j = 0; j < 16; j++)
				sum += matrix.getElmt(j/4,j%4) * XandYValue[i][j] / 36;

			constants.setElmt(i/4, i%4, sum);
		}

		// Calculate the value
		double value = 0;
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				value += constants.getElmt(i,j) * Math.pow(a,i) * Math.pow(b,j);

		return value;
	}
}