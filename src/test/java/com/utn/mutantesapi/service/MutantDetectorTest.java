package com.utn.mutantesapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase MutantDetector.
 * Estos tests se enfocan exclusivamente en la lógica del algoritmo de detección,
 * asumiendo que el input ya ha sido validado por capas anteriores.
 */
public class MutantDetectorTest {

    private MutantDetector mutantDetector;

    @BeforeEach
    void setUp() {
        // Se crea una nueva instancia antes de cada test para asegurar el aislamiento.
        mutantDetector = new MutantDetector();
    }

    // --- Casos de Prueba para MUTANTES (deben devolver true) ---

    @Test
    @DisplayName("MUTANT: Debe detectar con 2 secuencias horizontales")
    void isMutant_withTwoHorizontalSequences() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATTT",
            "AGACGG",
            "CCCCGA", // Secuencia 1
            "TCACTG"
        };
        // Agregamos una más para que sea mutante
        String[] dnaMutant = {
            "ATGCGA",
            "CAGTGC",
            "TTATTT",
            "AGACGG",
            "CCCCGA", // Secuencia 1
            "TCAAAA"  // Secuencia 2
        };
        assertTrue(mutantDetector.isMutant(dnaMutant));
    }

    @Test
    @DisplayName("MUTANT: Debe detectar con 2 secuencias verticales")
    void isMutant_withTwoVerticalSequences() {
        String[] dna = {
            "ATGCGA",
            "AAGTGC",
            "ATATGT",
            "AGAAGG",
            "ACCCTA",
            "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("MUTANT: Debe detectar con 2 secuencias diagonales descendentes")
    void isMutant_withTwoDescendingDiagonalSequences() {
        String[] dna = {
            "ATGCGA",
            "CATGCA",
            "GCATGC",
            "TGCATG",
            "GTAGCT",
            "AGCTAG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("MUTANT: Debe detectar con 2 secuencias diagonales ascendentes")
    void isMutant_withTwoAscendingDiagonalSequences() {
        String[] dna = {
            "TTAAA",
            "GATAA",
            "AGAAA",
            "AAATT",
            "TAGAA"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("MUTANT: Debe detectar con secuencias mixtas (horizontal y vertical)")
    void isMutant_withMixedHorizontalAndVertical() {
        String[] dna = {"AAAA", "AGCC", "AGCC", "AGCC"};
        assertTrue(mutantDetector.isMutant(dna));
    }
    
    @Test
    @DisplayName("MUTANT: Debe detectar con secuencias mixtas (horizontal y diagonal)")
    void isMutant_withMixedHorizontalAndDiagonal() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("MUTANT: Debe detectar con secuencias superpuestas")
    void isMutant_withOverlappingSequences() {
        String[] dna = {"AAAAAA", "TTCGAA", "GGCCTA", "CCGTGC", "TCACTG", "TCACTG"};
        assertTrue(mutantDetector.isMutant(dna));
    }

    // --- Casos de Prueba para HUMANOS (deben devolver false) ---

    @Test
    @DisplayName("HUMAN: No debe detectar con una sola secuencia horizontal")
    void isNotMutant_withOnlyOneHorizontalSequence() {
        String[] dna = {
            "AAAA",
            "CTGC",
            "GTCA",
            "TTCG"
        }; // Garantizado que solo tiene una secuencia
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("HUMAN: No debe detectar con una sola secuencia vertical")
    void isNotMutant_withOnlyOneVerticalSequence() {
        String[] dna = {"ACGT", "ATGA", "ACGA", "ATCA"};
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("HUMAN: No debe detectar con una sola secuencia diagonal")
    void isNotMutant_withOnlyOneDiagonalSequence() {
        String[] dna = {"ACGT", "TCGA", "GTAC", "TCGT"};
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("HUMAN: No debe detectar sin ninguna secuencia")
    void isNotMutant_withNoSequences() {
        String[] dna = {"ATGC", "CAGT", "TTAT", "AGAC"};
        assertFalse(mutantDetector.isMutant(dna));
    }
}
