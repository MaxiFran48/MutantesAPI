package com.utn.mutantesapi.repository;

import com.utn.mutantesapi.entity.DnaRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface DnaRecordRepository extends JpaRepository<DnaRecord, Long> {

    Optional<DnaRecord> findByDnaHash(String dnaHash);

    long countByIsMutant(boolean isMutant);

    void deleteByDnaHash(String dnaHash);

    boolean existsByDnaHash(String dnaHash);

    long countByIsMutantAndCreatedAtBetween(boolean isMutant, LocalDateTime start, LocalDateTime end);

    long countByIsMutantAndCreatedAtAfter(boolean isMutant, LocalDateTime start);

    long countByIsMutantAndCreatedAtBefore(boolean isMutant, LocalDateTime end);


}
