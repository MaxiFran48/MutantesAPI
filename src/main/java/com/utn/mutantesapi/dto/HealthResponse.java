package com.utn.mutantesapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "Respuesta que contiene datos sobre el estado del servidor")
public class HealthResponse {
    @Schema(
            description = "Estado del servidor",
            example = "UP",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String status;

    @Schema(
            description = "Fecha y hora de la petición",
            example = "2025-01-07T15:30:00",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime timestamp;
}
