package lib;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Balikan
{
	/*****	Global variable	*****/
	public static boolean isInversExist = true;



	/*****	DRIVER	*****/
	public static void DriverBalikan()
	{
		// User input
		Scanner sc = new Scanner(System.in);
        BufferedReader scFile = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("PILIHAN\n1. Metode matriks adjoin\n2. Metode eliminasi Gauss-Jordan\nPilihan: ");
        Integer choice = sc.nextInt();
        while(choice < 1 || choice > 2){
            System.out.println("Masukan tidak valid! Silakan ulangi...");
            System.out.printf("Pilihan: ");
            choice = sc.nextInt();
        }

        Matrix matrix = Matrix.inputMatrix();
        while ((matrix.getRowEff() != matrix.getColEff()) || (matrix.getRowEff() == 1 && matrix.getColEff() == 1))
        {
        	if (matrix.getRowEff() != matrix.getColEff())
	        	System.out.println("Matriks harus berbentuk persegi! Silahkan ulangi...");
	        if (matrix.getRowEff() == 1 && matrix.getColEff() == 1)
	        	System.out.println("Matriks tidak boleh berukuran 1! Silahkan ulangi...");

        	matrix = Matrix.inputMatrix();
        }

        // Calculate the inverse
        if(choice == 1)
            matrix = BalikanAdjoin(matrix);
        else 
        	matrix = BalikanGaussJordan(matrix);

        // Output
        if (!isInversExist)
        {
        	System.out.println("Matriks tidak memiliki invers!");
        }
        else
        {
        	// Cetak invers dari matriks
	        System.out.println("Invers dari matriks input adalah: ");
	        matrix.printMatrix();

        	// Simpan jawaban dalam file
        	System.out.printf("Apakah jawaban ingin disimpan dalam file?\n1. Ya\n2. Tidak\n");
	        choice = sc.nextInt();
	        while (choice != 1 && choice != 2)
	        {
	            System.out.printf("Masukan tidak valid! Silakan ulangi...\n");
	            choice = sc.nextInt();
	        }
	        if (choice == 1) {
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
	                
					for (int i = 0; i < matrix.getRowEff(); i++)
					{
						for (int j = 0; j < matrix.getColEff()-1; j++)
						{
							file.write(matrix.getElmt(i,j) + " ");
						}
						file.write(matrix.getElmt(i,matrix.getColEff()-1) + "\n");
					}
	                
	                file.close();
	            }
	            catch (IOException err) {
	                err.printStackTrace();
	            }
	        }
        }
	}

	public static Matrix swapRow(Matrix matrix, int n, int m)
	// Input 	: Matrix m, the matrix to be swapped at; int n and m, the row index to be swapped
	// Output	: Matrix m where row n and m are swapped
	{
		double temp;

		// Loop through the matrix and swap the value
		for (int i = 0; i < matrix.getColEff(); i++)
		{
			temp = matrix.getElmt(n,i);
			matrix.setElmt(n,i,matrix.getElmt(m,i));
			matrix.setElmt(m,i,temp);
		}

		return matrix;
	}

	public static Matrix Adjoin(Matrix matrix)
	// Input 	: Square matrix
	// Output	: Adjoin matrix
	{
		int size = matrix.getRowEff();

		// Calculate the cofactor of the matrix
		Matrix kofaktor = Kofaktor.Kofaktor(matrix);
		Matrix transpose = new Matrix(size,size);

		// Loop through the matrix and set the value
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				transpose.setElmt(i,j,kofaktor.getElmt(j,i));

		return transpose;
	}

	public static Matrix BalikanAdjoin(Matrix matrix)
	// Input 	: Square matrix
	// Output	: Inverse matrix
	{
		int size = matrix.getRowEff();

		// Get the adjoin matrix and the determinant
		Matrix matrixAdjoin = Adjoin(matrix);
		double determinan = Determinant.DetOBE(matrix);

		// Check whether the determinant is 0 or NaN, if so then the inverse doesn't exist
		if (determinan == 0 || Double.isNaN(determinan))
		{
			isInversExist = false;
			return matrix;
		}

		// Loop through the matrix and set the element
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				matrixAdjoin.setElmt(i,j,matrixAdjoin.getElmt(i,j)/determinan);

		return matrixAdjoin;
	}

	public static Matrix BalikanGaussJordan(Matrix matrix)
	// Input 	: Square matrix
	// Output	: Inverse matrix
	{
		// Create a new matrix to store the inverse value
		Matrix invers = new Matrix(matrix.getRowEff(), matrix.getColEff()*2);

		// Loop through the matrix and set it to an identity matrix
		for (int i = 0; i < matrix.getRowEff(); i++)
		{
			for (int j = 0; j < matrix.getColEff(); j++)
			{
				if (i == j)
					invers.setElmt(i,j+matrix.getColEff(),1);
				invers.setElmt(i,j,matrix.getElmt(i,j));
			}
		}

		// Loop through the matrix and check if there is an all zero row, if so the inverse doesn't exist
		for (int i = 0; i < invers.getRowEff(); i++)
		{
			for (int j = 0; j < invers.getRowEff(); j++)
			{
				if (i == j)
					continue;

				int k = i+1;
				if (invers.getElmt(i,i) == 0)
				{
					isInversExist = false;
					while (k < invers.getRowEff())
					{
						if (invers.getElmt(k,i) != 0)
						{
							invers = swapRow(invers, i, k);
							isInversExist = true;
						}
						k++;
					}
				}

				if (!isInversExist)
					return matrix;

				double sub = -1*invers.getElmt(j,i)/invers.getElmt(i,i);
				for (k = i; k < invers.getColEff(); k++)
					invers.setElmt(j,k,invers.getElmt(j,k)+sub*invers.getElmt(i,k));
			}
		}

		// Loop through the inverse matrix and do ERO 
		for (int i = 0; i < invers.getRowEff(); i++)
		{
			double div = invers.getElmt(i,i);
			for (int j = 0; j < invers.getColEff(); j++)
			{
				if (invers.getElmt(i,j) == 0)
					continue;
				invers.setElmt(i,j,invers.getElmt(i,j)/div);
			}
		}

		for (int i = 0; i < matrix.getRowEff(); i++)
		{
			for (int j = 0; j < matrix.getColEff(); j++)
			{
				matrix.setElmt(i,j,invers.getElmt(i,j+matrix.getColEff()));
			}
		}

		return matrix;
	}
}