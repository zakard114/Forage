package com.wellsfargo.counselor.controller;

import com.wellsfargo.counselor.dto.PortfolioDTO;
import com.wellsfargo.counselor.dto.SecurityDTO;
import com.wellsfargo.counselor.service.PortfolioService;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @Autowired
    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @PostMapping
    public ResponseEntity<PortfolioDTO> createPortfolio(@Valid @RequestBody PortfolioDTO portfolioDTO) {

        if(portfolioDTO.getClientId() == null){
            throw new IllegalArgumentException("Client ID cannot be null");
        }

        PortfolioDTO createdPortfolio = portfolioService.createPortfolio(portfolioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPortfolio);
    }

    @GetMapping("/{portfolioId}")
    public ResponseEntity<PortfolioDTO> getPortfolioById(@PathVariable String portfolioId) {
        PortfolioDTO portfolioDTO = portfolioService.getPortfolioById(portfolioId);
        return ResponseEntity.status(HttpStatus.OK).body(portfolioDTO);
    }

    @GetMapping
    public ResponseEntity<List<PortfolioDTO>> getAllPortfolios(){
        List<PortfolioDTO> portfolioDTOList = portfolioService.getAllPortfolios();
        return ResponseEntity.status(HttpStatus.OK).body(portfolioDTOList);
    }

    @PutMapping("/{portfolioId}")
    public ResponseEntity<PortfolioDTO> updatePortfolio(@PathVariable String portfolioId, @Valid @RequestBody PortfolioDTO portfolioDTO) {
        PortfolioDTO portfolio = portfolioService.updatePortfolio(portfolioId, portfolioDTO);
        return ResponseEntity.status(HttpStatus.OK).body(portfolio);
    }

    @DeleteMapping("/{portfolioId}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable String portfolioId) {
        portfolioService.deletePortfolio(portfolioId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{portfolioId}/security")
    public ResponseEntity<PortfolioDTO> addSecurityToPortfolio(@PathVariable String portfolioId
            ,@Nonnull @Valid @RequestBody SecurityDTO securityDTO) {
       PortfolioDTO addSecurityToPortfolio = portfolioService.addSecurityToPortfolio(portfolioId, securityDTO);
       return ResponseEntity.status(HttpStatus.OK).body(addSecurityToPortfolio);
    }

    @DeleteMapping("/{portfolioId}/security")
    public ResponseEntity<Void> removeSecurityFromPortfolio(@PathVariable String portfolioId
            , @Nonnull @Valid @RequestBody SecurityDTO securityDTO) {
        portfolioService.removeSecurityFromPortfolio(portfolioId, securityDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
