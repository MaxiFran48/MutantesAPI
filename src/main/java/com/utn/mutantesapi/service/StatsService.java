package com.utn.mutantesapi.service;

import com.utn.mutantesapi.dto.StatsResponse;
import com.utn.mutantesapi.repository.DnaRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final DnaRecordRepository dnaRecordRepository;

    public StatsResponse getStats(LocalDate startDate, LocalDate endDate) {

        long mutantCount;
        long humanCount;

        if (endDate != null && startDate != null) {
            mutantCount = dnaRecordRepository.countByIsMutantAndCreatedAtBetween(true, startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));
            humanCount = dnaRecordRepository.countByIsMutantAndCreatedAtBetween(false, startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));
        } else if (endDate != null) {
            mutantCount = dnaRecordRepository.countByIsMutantAndCreatedAtBefore(true, endDate.atTime(LocalTime.MAX));
            humanCount = dnaRecordRepository.countByIsMutantAndCreatedAtBefore(false, endDate.atTime(LocalTime.MAX));
        } else if (startDate != null) {
            mutantCount = dnaRecordRepository.countByIsMutantAndCreatedAtAfter(true, startDate.atStartOfDay());
            humanCount = dnaRecordRepository.countByIsMutantAndCreatedAtAfter(false, startDate.atStartOfDay());
        } else {
            mutantCount = dnaRecordRepository.countByIsMutant(true);
            humanCount = dnaRecordRepository.countByIsMutant(false);
        }

        double ratio;

        if (humanCount == 0) {
            ratio = (double) mutantCount; // Si no hay humanos, el ratio es el número de mutantes
        } else {
            ratio = (double) mutantCount / humanCount;
        }
        return new StatsResponse(mutantCount, humanCount, ratio);
    }

}
