package com.utn.mutantesapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utn.mutantesapi.dto.DnaRequest;
import com.utn.mutantesapi.dto.StatsResponse;
import com.utn.mutantesapi.exception.ResourceNotFoundException;
import com.utn.mutantesapi.service.MutantService;
import com.utn.mutantesapi.service.StatsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print; // <-- IMPORTANTE: Importar 'print'
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MutantService mutantService;

    @MockBean
    private StatsService statsService;

    @Test
    @DisplayName("POST /mutant debe devolver 200 OK para un ADN mutante")
    void checkMutant_whenIsMutant_shouldReturn200Ok() throws Exception {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        when(mutantService.analyzeDnaAndSave(any())).thenReturn(true);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new DnaRequest(dna))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /mutant debe devolver 403 Forbidden para un ADN humano")
    void checkMutant_whenIsHuman_shouldReturn403Forbidden() throws Exception {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"};
        when(mutantService.analyzeDnaAndSave(any())).thenReturn(false);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new DnaRequest(dna))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /mutant debe devolver 400 Bad Request para un ADN no cuadrado (Validado por @ValidDnaSequence)")
    void checkMutant_whenDnaIsInvalid_shouldReturn400BadRequest() throws Exception {
        String[] dna = {"ATGC", "CAGT", "TTA"}; // Matriz no cuadrada

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new DnaRequest(dna))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /stats debe devolver 200 OK con las estadísticas correctas")
    void getStats_shouldReturn200OkAndStats() throws Exception {
        StatsResponse stats = new StatsResponse(50L, 150L, 0.333);
        when(statsService.getStats(null, null)).thenReturn(stats);

        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").value(50))
                .andExpect(jsonPath("$.count_human_dna").value(150))
                .andExpect(jsonPath("$.ratio").value(0.333));
    }

    @Test
    @DisplayName("DELETE /mutant/{hash} debe devolver 204 No Content si el hash existe")
    void deleteDnaRecord_whenHashExists_shouldReturn204NoContent() throws Exception {
        String existingHash = "some-valid-hash";
        doNothing().when(mutantService).deleteDnaRecord(existingHash);

        mockMvc.perform(delete("/mutant/{hash}", existingHash))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /mutant/{hash} debe devolver 404 Not Found si el hash no existe")
    void deleteDnaRecord_whenHashDoesNotExist_shouldReturn404NotFound() throws Exception {
        String nonExistentHash = "non-existent-hash";
        doThrow(new ResourceNotFoundException("Registro no encontrado"))
                .when(mutantService).deleteDnaRecord(nonExistentHash);

        mockMvc.perform(delete("/mutant/{hash}", nonExistentHash))
                .andExpect(status().isNotFound());
    }
}
