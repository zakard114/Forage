package com.wellsfargo.counselor.controller;

import com.wellsfargo.counselor.dto.SecurityDTO;
import com.wellsfargo.counselor.service.SecurityService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/securities")
public class SecurityController {

    private final SecurityService securityService;

    @Autowired
    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @PostMapping
    public ResponseEntity<SecurityDTO> createSecurity(@Valid @RequestBody SecurityDTO securityDTO) {
        if(securityDTO.getPortfolioId() == null){
            throw new IllegalArgumentException("Portfolio ID cannot be null");
        }

        SecurityDTO createdSecurity = securityService.createSecurity(securityDTO);
        log.info("portfolioId in Security Check: ");
        log.info(createdSecurity.getPortfolioId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSecurity);
    }

    @GetMapping("/{securityId}")
    public ResponseEntity<SecurityDTO> getSecurityById(@PathVariable String securityId) {
        SecurityDTO securityDTO = securityService.getSecurityById(securityId);
        return ResponseEntity.status(HttpStatus.OK).body(securityDTO);
    }

    @GetMapping
    public ResponseEntity<List<SecurityDTO>> getAllSecurity() {
        List<SecurityDTO> securityDTOList = securityService.getAllSecurity();
        return ResponseEntity.status(HttpStatus.OK).body(securityDTOList);
    }

    @PutMapping("/{securityId}")
    public ResponseEntity<SecurityDTO> updateSecurity(@PathVariable String securityId, @Valid @RequestBody SecurityDTO securityDTO) {
        SecurityDTO updatedSecurity = securityService.updateSecurity(securityId, securityDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedSecurity);
    }

    @DeleteMapping("/{securityId}")
    public ResponseEntity<Void> deleteSecurity(@PathVariable String securityId) {
        securityService.deleteSecurity(securityId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
