package com.utn.mutantesapi.dto;

import com.utn.mutantesapi.validation.ValidDnaSequence;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Schema(description = "Petición que contiene la secuencia de ADN a verificar")
@AllArgsConstructor
@NoArgsConstructor
public class DnaRequest {

    @Schema(
            description = "Array de strings que representa la matriz NxN de ADN. Debe ser cuadrada y solo contener A, T, C, G.",
            example = "[\"ATGCGA\",\"CAGTGC\",\"TTATGT\",\"AGAAGG\",\"CCCCTA\",\"TCACTG\"]",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "El campo dna no puede estar vacio")
    @NotNull(message = "El campo dna no puede ser nulo")
    @ValidDnaSequence
    private String[] dna;
}
