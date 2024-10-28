package com.wellsfargo.counselor.service;

import com.wellsfargo.counselor.dto.SecurityDTO;
import com.wellsfargo.counselor.entity.Portfolio;
import com.wellsfargo.counselor.entity.Security;
import com.wellsfargo.counselor.repository.PortfolioRepository;
import com.wellsfargo.counselor.repository.SecurityRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SecurityService {

    private final SecurityRepository securityRepository;
    private final ModelMapper modelMapper;
    private final PortfolioRepository portfolioRepository;

    @Autowired
    public SecurityService(SecurityRepository securityRepository, ModelMapper modelMapper, PortfolioRepository portfolioRepository) {
        this.securityRepository = securityRepository;
        this.modelMapper = modelMapper;
        this.portfolioRepository = portfolioRepository;
    }

    @Transactional
    public SecurityDTO createSecurity(SecurityDTO securityDTO) {
        if(securityRepository.existsById(securityDTO.getSecurityId())){
            throw new IllegalArgumentException("Security already exists with ID: " + securityDTO.getSecurityId());
        }

        String portfolioId = securityDTO.getPortfolioId();
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(()-> new IllegalArgumentException("Portfolio with ID: " + portfolioId + " not found"));

        Security security = modelMapper.map(securityDTO, Security.class);
        security.setPortfolio(portfolio);

        log.info("portfolioId:");
        log.info(security.getPortfolio().getPortfolioId());


        Security savedSecurity = securityRepository.save(security);
        log.info("securityId:");
        log.info(savedSecurity.getSecurityId());
        return modelMapper.map(savedSecurity, SecurityDTO.class);
    }

    @Transactional(readOnly = true)
    public SecurityDTO getSecurityById(String securityId) {
        return securityRepository.findById(securityId)
                .map(security -> modelMapper.map(security, SecurityDTO.class))
                .orElseThrow(() -> new IllegalArgumentException("Security not found with ID: "+securityId));
    }

    @Transactional(readOnly = true)
    public List<SecurityDTO> getAllSecurity() {
        List<Security> securities = securityRepository.findAll();
        return securities.stream()
                .map(security -> modelMapper.map(security, SecurityDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public SecurityDTO updateSecurity(String securityId, SecurityDTO securityDTO) {
        Security existingSecurity = securityRepository.findById(securityId)
                .orElseThrow(() -> new IllegalArgumentException("Security not found with ID: "+securityId));
        modelMapper.map(securityDTO, existingSecurity);
        Security updatedSecurity = securityRepository.save(existingSecurity);
        return modelMapper.map(updatedSecurity, SecurityDTO.class);
    }

    @Transactional
    public void deleteSecurity(String securityId) {
        if(!securityRepository.existsById(securityId)) {
            throw new IllegalArgumentException("Security not found with ID: "+securityId);
        }
        securityRepository.deleteById(securityId);
    }

}
