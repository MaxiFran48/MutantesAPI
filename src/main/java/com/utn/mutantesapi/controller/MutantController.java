package com.utn.mutantesapi.controller;

import io.swagger.v3.oas.annotations.Parameter;
import com.utn.mutantesapi.dto.DnaRequest;
import com.utn.mutantesapi.dto.HealthResponse;
import com.utn.mutantesapi.dto.StatsResponse;
import com.utn.mutantesapi.service.MutantService;
import com.utn.mutantesapi.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Tag(name = "Mutant API", description = "Endpoints para la detección de mutantes y estadísticas")
public class MutantController {
    private final MutantService mutantService;
    private final StatsService statsService;

    @PostMapping("/mutant")
    @Operation(summary = "Verifica una secuencia de ADN para detectar si es mutante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El ADN corresponde a un mutante"),
            @ApiResponse(responseCode = "403", description = "El ADN corresponde a un humano"),
            @ApiResponse(responseCode = "400", description = "La petición es inválida (ej. ADN no cuadrado, caracteres inválidos)")
    })
    public ResponseEntity<Void> checkMutant(@Validated @RequestBody DnaRequest dnaRequest){
        boolean isMutant = mutantService.analyzeDnaAndSave(dnaRequest.getDna());

        if (isMutant) {
            return ResponseEntity.ok().build(); // Si es mutante, devuelve 200 OK.
        } else {
            return ResponseEntity.status(403).build(); //HttpStatus.FORBIDDEN
        }

    }

    @GetMapping("/stats")
    @Operation(summary = "Calcula las estadísticas sobre humanos y mutantes en la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se devuelve un JSON con las estadísticas de los mutantes y humanos"),
    })
    public ResponseEntity<StatsResponse> getStats(
            @Parameter(description = "Fecha de inicio para el filtro (formato YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(description = "Fecha de fin para el filtro (formato YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        StatsResponse stats = statsService.getStats(startDate, endDate);
        return ResponseEntity.ok(stats);

    }

    @GetMapping("/health")
    @Operation(summary = "Verificar el estado del servidor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se devuelve un JSON con las estadísticas de los mutantes y humanos"),
    })
    public ResponseEntity<HealthResponse> getServerStatus() {
        return ResponseEntity.ok(new HealthResponse("UP", LocalDateTime.now()));
    }

    @DeleteMapping("/mutant/{hash}")
    @Operation(summary = "Eliminar un registro del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Registro eliminado con éxito (No Content)"),
            @ApiResponse(responseCode = "404", description = "Registro de ADN no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> deleteDnaRecord(
            @Parameter(description = "Hash del registro de ADN a eliminar", required = true)
            @PathVariable String hash) {

        mutantService.deleteDnaRecord(hash);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
