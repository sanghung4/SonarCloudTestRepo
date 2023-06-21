package com.reece.platform.accounts.service;

import com.reece.platform.accounts.exception.BranchNotFoundException;
import com.reece.platform.accounts.model.DTO.BranchDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional(readOnly = true)
public class BranchService {

    @Value("${product_service_url}")
    private String productServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Gets the branch info from the branch service
     * @param branchId
     * @return BranchDTO - info about the branch including contact info
     * @throws BranchNotFoundException
     */
    public BranchDTO getBranch(String branchId) throws BranchNotFoundException {
        String getBranchUrl = String.format("%s/branches/%s", productServiceUrl, branchId);

        try {
            ResponseEntity<BranchDTO> response = restTemplate.getForEntity(getBranchUrl, BranchDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException ex) {
            if (ex.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) throw new BranchNotFoundException();
            else throw ex;
        }
    }

    public List<BranchDTO> getAllBranches() throws BranchNotFoundException {
        String branchUrl = String.format("%s/branches/getAll", productServiceUrl);
        try {
            ResponseEntity<BranchDTO[]> response = restTemplate.getForEntity(branchUrl, BranchDTO[].class);
            return Arrays.stream(Objects.requireNonNull(response.getBody())).collect(Collectors.toList());
        } catch (HttpClientErrorException ex) {
            if (ex.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) throw new BranchNotFoundException();
            else throw ex;
        }
    }

}
