package com.utn.mutantesapi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MutantDetector {

    final static int SEQUENCE_LENGTH = 4;
    final static int SEQUENCES_TO_BE_MUTANT = 2;

    public boolean checkHorizontal(char[][] matrix, int row, int column) {
        char baseDnaCell = matrix[row][column];
        return baseDnaCell == matrix[row][column + 1]
                && baseDnaCell == matrix[row][column + 2]
                && baseDnaCell == matrix[row][column + 3];
    }

    public boolean checkVertical(char[][] matrix, int row, int column) {
        char baseDnaCell = matrix[row][column];
        return baseDnaCell == matrix[row + 1][column]
                && baseDnaCell == matrix[row + 2][column]
                && baseDnaCell == matrix[row + 3][column];
    }

    public boolean checkDescendingDiagonal(char[][] matrix, int row, int column) {
        char baseDnaCell = matrix[row][column];
        return baseDnaCell == matrix[row + 1][column + 1]
                && baseDnaCell == matrix[row + 2][column + 2]
                && baseDnaCell == matrix[row + 3][column + 3];
    }

    public boolean checkAscendingDiagonal(char[][] matrix, int row, int column) {
        char baseDnaCell = matrix[row][column];
        return baseDnaCell == matrix[row - 1][column + 1]
                && baseDnaCell == matrix[row - 2][column + 2]
                && baseDnaCell == matrix[row - 3][column + 3];
    }

    public boolean isMutant(String[] dna) {

        //Creación de la matriz de caracteres
        final int matrixSize = dna.length;
        final char[][] dnaMatrix = new char[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            dnaMatrix[i] = dna[i].toCharArray();
        }

        int boundary = matrixSize - SEQUENCE_LENGTH;

        int sequencesCount = 0;

        // Chequeo celda por celda en solo for
        for (int row = 0; row < matrixSize; row++) {
            for (int column = 0; column < matrixSize; column++) {

                //Chequeamos que haya espacio para revisar
                if (column <= boundary) {
                    if (checkHorizontal(dnaMatrix, row, column)) {
                        sequencesCount++;
                        log.debug("Secuencia horizontal encontrada en la fila {} y columna {}", row, column);
                        if (sequencesCount >= SEQUENCES_TO_BE_MUTANT) return true;
                    }
                }

                if (row <= boundary) {
                    if (checkVertical(dnaMatrix, row, column)) {
                        sequencesCount++;
                        log.debug("Secuencia vertical encontrada en la fila {} y columna {}", row, column);
                        if (sequencesCount >= SEQUENCES_TO_BE_MUTANT) return true;
                    }
                }

                if (row <= boundary && column <= boundary) {
                    if (checkDescendingDiagonal(dnaMatrix, row, column)) {
                        sequencesCount++;
                        log.debug("Secuencia diagonal descendente encontrada en la fila {} y columna {}", row, column);
                        if (sequencesCount >= SEQUENCES_TO_BE_MUTANT) return true;
                    }
                }

                if (column <= boundary && row >= (SEQUENCE_LENGTH - 1)) {
                    if (checkAscendingDiagonal(dnaMatrix, row, column)) {
                        sequencesCount++;
                        log.debug("Secuencia diagonal ascendente encontrada en la fila {} y columna {}", row, column);
                        if (sequencesCount >= SEQUENCES_TO_BE_MUTANT) return true;
                    }
                }

            }
        }

        return false;
    }

}
