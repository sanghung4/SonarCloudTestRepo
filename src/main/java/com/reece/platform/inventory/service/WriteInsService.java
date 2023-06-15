package com.reece.platform.inventory.service;

import com.reece.platform.inventory.dto.WriteInDTO;
import com.reece.platform.inventory.exception.CountNotFoundException;
import com.reece.platform.inventory.exception.LocationNotFoundException;
import com.reece.platform.inventory.exception.WriteInNotFountException;
import com.reece.platform.inventory.model.WriteIn;
import com.reece.platform.inventory.repository.CountRepository;
import com.reece.platform.inventory.repository.LocationCountRepository;
import com.reece.platform.inventory.repository.WriteInRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WriteInsService {
    private final WriteInRepository writeInRepository;
    private  final CountRepository countRepository;

    public Page<WriteInDTO> findAllWriteIns(String branchId, String countId, Pageable pageRequest) {
        return writeInRepository.findAllWriteIns(branchId, countId, pageRequest).map(WriteInDTO::fromEntity);
    }

    public WriteInDTO findById(UUID writeInId) {
        return writeInRepository.findById(writeInId).map(WriteInDTO::fromEntity).orElseThrow(WriteInNotFountException::new);
    }

    public WriteInDTO createWriteIn(String branchId, String countId, WriteInDTO writeInDTO) {
        val count = countRepository.findCount(branchId, countId).orElseThrow(CountNotFoundException::new);
        val writeIn = writeInDTO.toEntity();
        writeIn.setCountId(count);
        writeIn.setLocationName(writeInDTO.getLocationId());
        return WriteInDTO.fromEntity(writeInRepository.save(writeIn));
    }

    public WriteInDTO updateWriteIn(UUID writeInId, WriteInDTO writeInDTO) {
        val writeIn = writeInRepository.findById(writeInId).orElseThrow(WriteInNotFountException::new);
        val updatedWriteIn = writeInDTO.updateEntity(writeIn);
        return WriteInDTO.fromEntity(writeInRepository.save(updatedWriteIn));
    }

    public WriteInDTO resolveWriteIn(UUID writeInId) {
        val writeIn = writeInRepository.findById(writeInId).orElseThrow(WriteInNotFountException::new);
        writeIn.setResolved(true);
        return WriteInDTO.fromEntity(writeInRepository.save(writeIn));
    }
}
