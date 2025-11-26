package com.utn.mutantesapi.service;

import com.utn.mutantesapi.dto.StatsResponse;
import com.utn.mutantesapi.repository.DnaRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatsServiceTest {

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private StatsService statsService;

    @Test
    @DisplayName("Debe devolver estadísticas con ratio correcto cuando hay mutantes y humanos")
    void getStats_shouldReturnCorrectStats_whenMutantsAndHumansExist() {
        // Arrange
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(40L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(100L);

        // Act
        StatsResponse stats = statsService.getStats(null, null);

        // Assert
        assertEquals(40, stats.getCountMutantDna());
        assertEquals(100, stats.getCountHumanDna());
        assertEquals(0.4, stats.getRatio(), 0.001); // El delta es para la precisión de doubles
    }

    @Test
    @DisplayName("Debe devolver ratio 0.0 cuando no hay mutantes")
    void getStats_shouldReturnRatioZero_whenNoMutantsExist() {
        // Arrange
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(0L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(100L);

        // Act
        StatsResponse stats = statsService.getStats(null, null);

        // Assert
        assertEquals(0, stats.getCountMutantDna());
        assertEquals(100, stats.getCountHumanDna());
        assertEquals(0.0, stats.getRatio());
    }

    @Test
    @DisplayName("Debe devolver ratio igual al número de mutantes cuando no hay humanos")
    void getStats_shouldReturnRatioEqualToMutantCount_whenNoHumansExist() {
        // Arrange
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(40L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(0L);

        // Act
        StatsResponse stats = statsService.getStats(null, null);

        // Assert
        assertEquals(40, stats.getCountMutantDna());
        assertEquals(0, stats.getCountHumanDna());
        assertEquals(40.0, stats.getRatio()); // El ratio es simplemente el conteo de mutantes
    }

    @Test
    @DisplayName("Debe devolver ratio 0.0 cuando no hay datos en la base de datos")
    void getStats_shouldReturnRatioZero_whenNoDataExists() {
        // Arrange
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(0L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(0L);

        // Act
        StatsResponse stats = statsService.getStats(null, null);

        // Assert
        assertEquals(0, stats.getCountMutantDna());
        assertEquals(0, stats.getCountHumanDna());
        assertEquals(0.0, stats.getRatio());
    }

    @Test
    @DisplayName("Debe manejar números grandes correctamente")
    void getStats_shouldHandleLargeNumbers() {
        // Arrange
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(150000L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(300000L);

        // Act
        StatsResponse stats = statsService.getStats(null, null);

        // Assert
        assertEquals(150000, stats.getCountMutantDna());
        assertEquals(300000, stats.getCountHumanDna());
        assertEquals(0.5, stats.getRatio());
    }

    @Test
    @DisplayName("Debe calcular el ratio con precisión para decimales periódicos")
    void getStats_shouldCalculateRatioWithPrecision() {
        // Arrange
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(1L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(3L);

        // Act
        StatsResponse stats = statsService.getStats(null, null);

        // Assert
        assertEquals(1, stats.getCountMutantDna());
        assertEquals(3, stats.getCountHumanDna());
        assertEquals(0.333, stats.getRatio(), 0.001); // Verificamos la precisión hasta 3 decimales
    }
}
