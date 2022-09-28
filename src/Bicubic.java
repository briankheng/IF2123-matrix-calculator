import lib.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Bicubic
{
	public static void SolveBicubic()
	// Input matrix 4x4, berisi nilai f(i,j) dan nilai (a,b)
	// Output nilai f(a,b)
	{
		// Read file dari input user
		Scanner sc = new Scanner(System.in);
		BufferedReader scFile = new BufferedReader(new InputStreamReader(System.in));

		double[] input = new double[16];
		double a = 0, b = 0;
		int n = 0;

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
					// System.out.println(file.nextDouble());
					if (n == 4)
					{
						a = file.nextDouble();
						b = file.nextDouble();
					}
					else
					{
						input[0+n] = file.nextDouble();
						input[4+n] = file.nextDouble();
						input[8+n] = file.nextDouble();
						input[12+n] = file.nextDouble();
					}
					n++;
				}
			}
			catch (FileNotFoundException err) {
				err.printStackTrace();
				found = false;
			}
		}

		double[][] invers = {{0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
							 {0, 0, 0, 0, -1.0/3, -1.0/2, 1, -1.0/6, 0, 0, 0, 0, 0, 0, 0, 0},
							 {0, 0, 0, 0, 1.0/2, -1, 1.0/2, 0, 0, 0, 0, 0, 0, 0, 0, 0},
							 {0, 0, 0, 0, -1.0/6, 1.0/2, -1.0/2, 1.0/6, 0, 0, 0, 0, 0, 0, 0, 0},
							 {0, -1.0/3, 0, 0, 0, -1.0/2, 0, 0, 0, 1, 0, 0, 0, -1.0/6, 0, 0},
							 {1.0/9, 1.0/6, -1.0/3, 1.0/18, 1.0/6, 1.0/4, -1.0/2, 1.0/12, -1.0/3, -1.0/2, 1, -1.0/6, 1.0/18, 1.0/12, -1.0/6, 1.0/36},
							 {-1.0/6, 1.0/3, -1.0/6, 0, -1.0/4, 1.0/2, -1.0/4, 0, 1.0/2, -1, 1.0/2, 0, -1.0/12, 1.0/6, -1.0/12, 0},
							 {1.0/18, -1.0/6, 1.0/6, -1.0/18, 1.0/12, -1.0/4, 1.0/4, -1.0/12, -1.0/6, 1.0/2, -1.0/2, 1.0/6, 1.0/36, -1.0/12, 1.0/12, -1.0/36},
							 {0, 1.0/2, 0, 0, 0, -1, 0, 0, 0, 1.0/2, 0, 0, 0, 0, 0, 0},
							 {-1.0/6, -1.0/4, 1.0/2, -1.0/12, 1.0/3, 1.0/2, -1, 1.0/6, -1.0/6, -1.0/4, 1.0/2, -1.0/12, 0, 0, 0, 0},
							 {1.0/4, -1.0/2, 1.0/4, 0, -1.0/2, 1, -1.0/2, 0, 1.0/4, -1.0/2, 1.0/4, 0, 0, 0, 0, 0},
							 {-1.0/12, 1.0/4, -1.0/4, 1.0/12, 1.0/6, -1.0/2, 1.0/2, -1.0/6, -1.0/12, 1.0/4, -1.0/4, 1.0/12, 0, 0, 0, 0},
							 {0, -1.0/6, 0, 0, 0, 1.0/2, 0, 0, 0, -1.0/2, 0, 0, 0, 1.0/6, 0, 0},
							 {1.0/18, 1.0/12, -1.0/6, 1.0/36, -1.0/6, -1.0/4, 1.0/2, -1.0/12, 1.0/6, 1.0/4, -1.0/2, 1.0/12, -1.0/18, -1.0/12, 1.0/6, -1.0/36},
							 {-1.0/12, 1.0/6, -1.0/12, 0, 1.0/4, -1.0/2, 1.0/4, 0, -1.0/4, 1.0/2, -1.0/4, 0, 1.0/12, -1.0/6, 1.0/12, 0},
							 {1.0/36, -1.0/12, 1.0/12, -1.0/36, -1.0/12, 1.0/4, -1.0/4, 1.0/12, 1.0/12, -1.0/4, 1.0/4, -1.0/12, -1.0/36, 1.0/12, -1.0/12, 1.0/36}};

		Matrix constants = new Matrix(16,1);
		for (int i = 0; i < 16; i++)
		{
			double sum = 0;
			for (int j = 0; j < 16; j++)
			{
				sum += input[j] * invers[i][j];
			}
			constants.setElmt(i,0,sum);
		}

		double value = 0;
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				double temp = constants.getElmt(4*i+j,0);

				int ti = i, tj = j;
				while (ti > 0)
				{
					temp *= a;
					ti--;
				}
				while (tj > 0)
				{
					temp *= b;
					tj--;
				}

				value += temp;
			}
		}

		System.out.println("f(" + a + "," + b + ") = " + value);

		// Simpan jawaban dalam file
        System.out.printf("Apakah jawaban ingin disimpan dalam file?\n1. Ya\n2. Tidak\n");
        int choice = sc.nextInt();
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
                file.write("f(" + Double.toString(a) + "," + Double.toString(b) + ") = " + Double.toString(value) + "\n");
                file.close();
            }
            catch(IOException err){
                err.printStackTrace();
            }
        }
	}	
}