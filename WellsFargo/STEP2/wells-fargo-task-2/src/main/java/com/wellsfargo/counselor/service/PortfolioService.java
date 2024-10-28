package com.wellsfargo.counselor.service;

import com.wellsfargo.counselor.dto.PortfolioDTO;
import com.wellsfargo.counselor.dto.SecurityDTO;
import com.wellsfargo.counselor.entity.Client;
import com.wellsfargo.counselor.entity.Portfolio;
import com.wellsfargo.counselor.entity.Security;
import com.wellsfargo.counselor.repository.ClientRepository;
import com.wellsfargo.counselor.repository.PortfolioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final ModelMapper modelMapper;
    private final ClientRepository clientRepository;

    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository, ModelMapper modelMapper, ClientRepository clientRepository) {
        this.portfolioRepository = portfolioRepository;
        this.modelMapper = modelMapper;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public PortfolioDTO createPortfolio(PortfolioDTO portfolioDTO) {
        if(portfolioRepository.existsById(portfolioDTO.getPortfolioId())) {
            throw new IllegalArgumentException("Advisor already exists with ID: " + portfolioDTO.getPortfolioId());
        }

        String clientId = portfolioDTO.getClientId();
        Client client = clientRepository.findById(clientId)
                .orElseThrow(()-> new IllegalArgumentException("Client with ID: " + clientId + " not found"));

        Portfolio portfolio = modelMapper.map(portfolioDTO, Portfolio.class);
        portfolio.setClient(client);

        Portfolio savedPortfolio = portfolioRepository.save(portfolio);
        return modelMapper.map(savedPortfolio, PortfolioDTO.class);
    }

    @Transactional(readOnly = true)
    public PortfolioDTO getPortfolioById(String portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found with ID: " + portfolioId));
        return modelMapper.map(portfolio, PortfolioDTO.class);
    }

    @Transactional(readOnly = true)
    public List<PortfolioDTO> getAllPortfolios() {
        List<Portfolio> portfolios = portfolioRepository.findAll();
        return portfolios.stream()
                .map(portfolio -> modelMapper.map(portfolio, PortfolioDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public PortfolioDTO updatePortfolio(String portfolioId, PortfolioDTO portfolioDTO) {
        Portfolio existingPortfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("Advisor not found with ID: "+portfolioId));
        modelMapper.map(portfolioDTO, existingPortfolio);
        Portfolio updatedPortfolio = portfolioRepository.save(existingPortfolio);
        return modelMapper.map(updatedPortfolio, PortfolioDTO.class);
    }

    @Transactional
    public void deletePortfolio(String portfolioId) {
        if(!portfolioRepository.existsById(portfolioId)){
            throw new IllegalArgumentException("Advisor not found with ID: "+portfolioId);
        }
        portfolioRepository.deleteById(portfolioId);
    }

    @Transactional
    public PortfolioDTO addSecurityToPortfolio(String portfolioId, SecurityDTO securityDTO) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(()-> new IllegalArgumentException("Portfolio not found with ID: "+portfolioId));

        List<Security> securitiesInPortfolio = portfolio.getSecurities();
        Security security = modelMapper.map(securityDTO, Security.class);

        if(!securitiesInPortfolio.contains(security)){
            securitiesInPortfolio.add(security);
            security.setPortfolio(portfolio);
            portfolioRepository.save(portfolio);
        } else {
            throw new IllegalArgumentException("Security already exists in this Portfolio");
        }

        return modelMapper.map(security, PortfolioDTO.class);
    }

    @Transactional
    public void removeSecurityFromPortfolio(String portfolioId, SecurityDTO securityDTO) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(()-> new IllegalArgumentException("Portfolio not found with ID: "+portfolioId));

        List<Security> securitiesInPortfolio = portfolio.getSecurities();
        Security security = modelMapper.map(securityDTO, Security.class);

        if(securitiesInPortfolio.contains(security)){
            securitiesInPortfolio.remove(security);
            security.setPortfolio(null);
            portfolioRepository.save(portfolio);
        } else {
            throw new IllegalArgumentException("The specified Security does not exist in this Portfolio");
        }
    }
}
