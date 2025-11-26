package com.utn.mutantesapi.validation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD}) // Esta anotación se podrá usar en campos de una clase
@Retention(RetentionPolicy.RUNTIME) // Estará disponible en tiempo de ejecución
@Constraint(validatedBy = ValidDnaSequenceValidator.class) // La lógica de validación estará en esta clase
public @interface ValidDnaSequence {

    // Mensaje de error por defecto si la validación falla
    String message() default "La secuencia de ADN es inválida. Revisa los requerimientos (matriz NxN, solo caracteres A,T,C,G).";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};


}
