package com.utn.mutantesapi.service;

import com.utn.mutantesapi.entity.DnaRecord;
import com.utn.mutantesapi.exception.ResourceNotFoundException;
import com.utn.mutantesapi.repository.DnaRecordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MutantService {

    private final DnaRecordRepository dnaRecordRepository;
    private final MutantDetector mutantDetector;

    private String calculateDnaHash(String[] dna) {
        String dnaString = String.join("", dna);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes =  md.digest(dnaString.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al calcular el hash del ADN", e);
        }
    }

    @Transactional
    public boolean analyzeDnaAndSave (String[] dna) {
        String dnaHash = calculateDnaHash(dna);
        Optional<DnaRecord> existingRecord = dnaRecordRepository.findByDnaHash(dnaHash);
        if (existingRecord.isPresent()) {
            return existingRecord.get().isMutant();
        }

        boolean isMutant = mutantDetector.isMutant(dna);

        DnaRecord dnaRecord = new DnaRecord();
        dnaRecord.setMutant(isMutant);
        dnaRecord.setDnaHash(dnaHash);
        dnaRecord.setCreatedAt(LocalDateTime.now());
        dnaRecordRepository.save(dnaRecord);

        return isMutant;
    }

    @Transactional
    public void deleteDnaRecord(String dnaHash) {
        if (!dnaRecordRepository.existsByDnaHash(dnaHash)) {
            throw new ResourceNotFoundException("Registro de ADN con hash " + dnaHash + " no encontrado.");
        }

        dnaRecordRepository.deleteByDnaHash(dnaHash);
    }

}
