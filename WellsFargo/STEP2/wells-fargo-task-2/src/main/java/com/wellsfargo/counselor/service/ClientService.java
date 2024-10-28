package com.wellsfargo.counselor.service;

import com.wellsfargo.counselor.dto.ClientDTO;
import com.wellsfargo.counselor.dto.PortfolioDTO;
import com.wellsfargo.counselor.entity.Advisor;
import com.wellsfargo.counselor.entity.Client;
import com.wellsfargo.counselor.entity.Portfolio;
import com.wellsfargo.counselor.repository.AdvisorRepository;
import com.wellsfargo.counselor.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;
    private final AdvisorRepository advisorRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository, ModelMapper modelMapper, AdvisorRepository advisorRepository) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
        this.advisorRepository = advisorRepository;
    }

    @Transactional
    public ClientDTO createClient(ClientDTO clientDTO){
//        log.info("Foreign Entity TEST");
//        log.info(clientDTO.getAdvisorId());

        // Check if the client already exists in the repository
        if(clientRepository.existsById(clientDTO.getClientId())){
            // Throw an exception if the client already exists with the given ID
            throw new IllegalArgumentException("Client already exists with ID: " + clientDTO.getClientId());
        }

        // Get the advisor ID from the clientDTO
        String advisorId = clientDTO.getAdvisorId();
        // Find the advisor by ID, throw an exception if not found
        Advisor advisor = advisorRepository.findById(advisorId)
                .orElseThrow(() -> new IllegalArgumentException("Advisor not found with ID: "+advisorId));

        // Map the clientDTO to a Client entity
        Client client = modelMapper.map(clientDTO, Client.class);
        // Set the advisor for the Client entity
        client.setAdvisor(advisor);

//        log.info("checking adviserID");
//        log.info(modelMapper.map(client.getAdvisor(), AdvisorDTO.class).getAdvisorId());

        // Save the Client entity in the repository and return the mapped ClientDTO
        Client savedClient = clientRepository.save(client);

//        log.info("checking adviserID -2");
//        log.info(modelMapper.map(client.getAdvisor(), AdvisorDTO.class).getAdvisorId());

        // In the Client entity, the advisor field is an Advisor object due to the @ManyToOne relationship.
        // In the DTO, a String type for advisorId minimizes direct entity exposure, focusing on data transfer.
        // Thus, the mapping is handled in the Mapper configuration; it will return null otherwise.
        return modelMapper.map(savedClient, ClientDTO.class);
    }

    @Transactional(readOnly = true)
    public ClientDTO getClientById(String clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with ID: "+clientId));
        return modelMapper.map(client, ClientDTO.class);
    }

    @Transactional(readOnly = true)
    public List<ClientDTO> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .map(client -> modelMapper.map(client, ClientDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public ClientDTO updateClient(String clientId, ClientDTO clientDTO) {
        Client existingClient = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with ID: "+clientId));
        modelMapper.map(clientDTO, existingClient);
        Client updatedClient = clientRepository.save(existingClient);
        return modelMapper.map(updatedClient, ClientDTO.class);
    }

    @Transactional
    public void deleteClient(String clientId){
        if(!clientRepository.existsById(clientId)){
            throw new IllegalArgumentException("Client not found with ID: "+clientId);
        }
        clientRepository.deleteById(clientId);
    }

    @Transactional
    public ClientDTO addPortfolioToClient(String clientId, PortfolioDTO portfolioDTO){
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with ID: "+clientId));

        List<Portfolio> portfoliosInClient = client.getPortfolios();
        Portfolio portfolio = modelMapper.map(portfolioDTO, Portfolio.class);

        if(!portfoliosInClient.contains(portfolio)){
            portfoliosInClient.add(portfolio);
            portfolio.setClient(client);
            clientRepository.save(client);
        } else {
            throw new IllegalArgumentException("Portfolio already exists in this Client");
        }
        return modelMapper.map(client, ClientDTO.class);
    }

    @Transactional
    public void removePortfolioFromClient(String clientId, PortfolioDTO portfolioDTO){
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with ID: "+clientId));

        List<Portfolio> portfoliosInClient = client.getPortfolios();
        Portfolio portfolio = modelMapper.map(portfolioDTO, Portfolio.class);

        if(!portfoliosInClient.contains(portfolio)){
            portfoliosInClient.remove(portfolio);
            portfolio.setClient(null);
            clientRepository.save(client);
        } else {
            throw new IllegalArgumentException("The specified Portfolio does not exist in this Client");
        }
    }

}