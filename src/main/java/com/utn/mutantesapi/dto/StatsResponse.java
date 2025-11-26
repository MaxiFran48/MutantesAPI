package com.utn.mutantesapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Respuesta que contiene las estadisticas sobre humanos y mutantes")
public class StatsResponse {

    @Schema(
            description = "Cantidad de mutantes guardados en el sistema",
            example = "10",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("count_mutant_dna")
    private long countMutantDna;

    @Schema(
            description = "Cantidad de humanos guardados en el sistema",
            example = "5",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("count_human_dna")
    private long countHumanDna;

    @Schema(
            description = "Ratio mutantes/humanos en el sistema",
            example = "2.5",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private double ratio;
}
