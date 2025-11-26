package com.utn.mutantesapi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class ValidDnaSequenceValidator implements ConstraintValidator<ValidDnaSequence, String[]> {

    final static Set<Character> VALID_LETTERS = Set.of('A','T','C','G');
    final static int MIN_MATRIX_SIZE = 4;
    final static int MAX_MATRIX_SIZE = 10000;

    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {

        if (dna == null || dna.length < MIN_MATRIX_SIZE || dna.length > MAX_MATRIX_SIZE) {
            return false;
        }

        int matrixSize = dna.length;

        for (String dnaRow : dna) {
            if (dnaRow == null || dnaRow.length() != matrixSize) {
                return false;
            }

            for (int j = 0; j < matrixSize; j++) {
                if (!VALID_LETTERS.contains(dnaRow.charAt(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
