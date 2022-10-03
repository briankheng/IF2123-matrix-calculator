# Algeo01-21049

## General Information
Program created with Java to fulfill "Tugas Besar 1 Algeo 2022/2023". The program functionality is mainly related to matrix, including solving linear equations, determinants, inverse matrix, polynomial interpolation, bicubic interpolation, and multiple linear regression.
### Contributors:
| Name  | NIM |
| ------------- | ------------- |
| Brian Kheng  |  13521049  |
| Farizki Kurniawan  | 13521082  |
| Frankie Huang | 13521092 |

## Features
* **Solving Linear Equations** <br>
Linear equations can be solved with the Gauss elimination method, the Gauss Jordan elimination method, the Inverse Matrix method, and the Cramer method. The answer could be single, many, or no solutions.
* **Finding Determinants** <br>
Determinants can be found with the Cofactor method and the OBE method. The answer could exist or not.
* **Finding Inverse Matrix** <br>
An inverse matrix can be found with the Gauss-Jordan elimination method and the Adjoin Matrix method. The answer could exist or not.
* **Solving Polynomial Interpolation** <br>
Polynomial Interpolation is solved by creating a polynomial equation from the points given.
* **Solving Bicubic Interpolation** <br>
Bicubic Interpolation is solved by an interpolation technique in 2D data that could be used for image enlargement.
* **Solving Multiple Linear Regression** <br>
Multiple Linear Regression is solved by making a linear equation from the points given.
* **Scaling Image** <br>
Scaling image using bicubic interpolation.

## Structure
```bash
.
├───README.md
│
├───bin
│   ├───lib
│   │   ├───Balikan.class
│   │   ├───Determinant.class
│   │   ├───Kofaktor.class
│   │   ├───Matrix.class
│   │   └───SPL.class
│   │
│   ├───Bicubic.class
│   │       
│   ├───ImageProcessing.class
│   │
│   ├───Interpolate.class
│   │
│   ├───Main.class
│   │
│   ├───Main.jar
│   │
│   └───Regresi.class
│
├───doc
│
├───src
│   ├───lib
│   │   ├───Balikan.java
│   │   ├───Determinant.java
│   │   ├───Kofaktor.java
│   │   ├───Matrix.java
│   │   └───SPL.java
│   │
│   ├───Bicubic.java
│   │       
│   ├───ImageProcessing.java
│   │
│   ├───Interpolate.java
│   │
│   ├───Main.java
│   │
│   └───Regresi.java
│
└───test
```

## Built With
* [Java](https://www.java.com/en/)

## Getting Started
1. Clone Repository
   ```sh
   git clone https://github.com/briankheng/Algeo01-21049.git
   ```
2. Open folder "Algeo01-21049" in the terminal, then run:
   ```sh
   cd src
   javac -d ../bin Main.java
   cd../bin
   java Main
   ```
## Contact
Brian Kheng - 13521049@std.stei.itb.ac.id <br>
Farizki Kurniawan - 13521082@std.stei.itb.ac.id <br>
Frankie Huang - 13521092@std.stei.itb.ac.id
