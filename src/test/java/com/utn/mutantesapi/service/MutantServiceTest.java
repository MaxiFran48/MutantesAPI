package com.utn.mutantesapi.service;

import com.utn.mutantesapi.entity.DnaRecord;
import com.utn.mutantesapi.repository.DnaRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Usamos la extensión de Mockito para inicializar los mocks
public class MutantServiceTest {

    @Mock
    private MutantDetector mutantDetector;

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks // Inyecta los mocks (@Mock) en esta instancia del servicio
    private MutantService mutantService;

    @Test
    @DisplayName("Debe devolver resultado 'true' de la caché si el ADN ya existe y es mutante")
    void analyzeDnaAndSave_shouldReturnTrueFromCache_whenDnaExistsAndIsMutant() {
        // Arrange: Preparación del escenario
        String[] dna = {"AAAA", "BBBB", "CCCC", "DDDD"};
        DnaRecord existingRecord = new DnaRecord();
        existingRecord.setMutant(true);

        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.of(existingRecord));

        // Act: Ejecución de la lógica
        boolean result = mutantService.analyzeDnaAndSave(dna);

        // Assert: Verificación de los resultados
        assertTrue(result, "Debería devolver true para un mutante cacheado");
        verify(dnaRecordRepository, times(1)).findByDnaHash(anyString());
        verify(mutantDetector, never()).isMutant(any()); // No se debe llamar al detector
        verify(dnaRecordRepository, never()).save(any()); // No se debe guardar nada nuevo
    }

    @Test
    @DisplayName("Debe devolver resultado 'false' de la caché si el ADN ya existe y es humano")
    void analyzeDnaAndSave_shouldReturnFalseFromCache_whenDnaExistsAndIsHuman() {
        // Arrange
        String[] dna = {"ATGC", "CAGT", "TTAT", "AGAC"};
        DnaRecord existingRecord = new DnaRecord();
        existingRecord.setMutant(false);

        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.of(existingRecord));

        // Act
        boolean result = mutantService.analyzeDnaAndSave(dna);

        // Assert
        assertFalse(result, "Debería devolver false para un humano cacheado");
        verify(dnaRecordRepository, times(1)).findByDnaHash(anyString());
        verify(mutantDetector, never()).isMutant(any());
        verify(dnaRecordRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe analizar y guardar un nuevo ADN que resulta ser mutante")
    void analyzeDnaAndSave_shouldAnalyzeAndSaveNewMutantDna() {
        // Arrange
        String[] dna = {"AAAA", "AAAA", "CCCC", "DDDD"};
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty()); // No existe en BD
        when(mutantDetector.isMutant(dna)).thenReturn(true); // El detector lo marca como mutante

        // Act
        boolean result = mutantService.analyzeDnaAndSave(dna);

        // Assert
        assertTrue(result, "El resultado del análisis debe ser true");
        verify(mutantDetector, times(1)).isMutant(dna); // Se debe llamar al detector
        verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class)); // Se debe guardar el resultado
    }

    @Test
    @DisplayName("Debe analizar y guardar un nuevo ADN que resulta ser humano")
    void analyzeDnaAndSave_shouldAnalyzeAndSaveNewHumanDna() {
        // Arrange
        String[] dna = {"ATGC", "CAGT", "TTAT", "AGAC"};
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(dna)).thenReturn(false); // El detector lo marca como humano

        // Act
        boolean result = mutantService.analyzeDnaAndSave(dna);

        // Assert
        assertFalse(result, "El resultado del análisis debe ser false");
        verify(mutantDetector, times(1)).isMutant(dna);
        verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class));
    }

    @Test
    @DisplayName("Debe guardar el registro con el estado 'isMutant' correcto (true)")
    void analyzeDnaAndSave_shouldSaveRecordWithCorrectMutantStatus() {
        // Arrange
        String[] dna = {"AAAA", "AAAA", "CCCC", "DDDD"};
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(dna)).thenReturn(true);

        ArgumentCaptor<DnaRecord> dnaRecordCaptor = ArgumentCaptor.forClass(DnaRecord.class);

        // Act
        mutantService.analyzeDnaAndSave(dna);

        // Assert
        verify(dnaRecordRepository).save(dnaRecordCaptor.capture()); // Captura el objeto que se pasa a save()
        DnaRecord savedRecord = dnaRecordCaptor.getValue();

        assertNotNull(savedRecord.getDnaHash());
        assertTrue(savedRecord.isMutant(), "El registro guardado debe tener isMutant = true");
        assertNotNull(savedRecord.getCreatedAt());
    }

    @Test
    @DisplayName("Debe guardar el registro con el estado 'isMutant' correcto (false)")
    void analyzeDnaAndSave_shouldSaveRecordWithCorrectHumanStatus() {
        // Arrange
        String[] dna = {"ATGC", "CAGT", "TTAT", "AGAC"};
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(dna)).thenReturn(false);

        ArgumentCaptor<DnaRecord> dnaRecordCaptor = ArgumentCaptor.forClass(DnaRecord.class);

        // Act
        mutantService.analyzeDnaAndSave(dna);

        // Assert
        verify(dnaRecordRepository).save(dnaRecordCaptor.capture());
        DnaRecord savedRecord = dnaRecordCaptor.getValue();
        
        assertNotNull(savedRecord.getDnaHash());
        assertFalse(savedRecord.isMutant(), "El registro guardado debe tener isMutant = false");
        assertNotNull(savedRecord.getCreatedAt());
    }
}
