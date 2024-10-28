package com.wellsfargo.counselor.service;

import com.wellsfargo.counselor.dto.AdvisorDTO;
import com.wellsfargo.counselor.dto.ClientDTO;
import com.wellsfargo.counselor.entity.Advisor;
import com.wellsfargo.counselor.entity.Client;
import com.wellsfargo.counselor.repository.AdvisorRepository;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdvisorService {

    private final AdvisorRepository advisorRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AdvisorService(AdvisorRepository advisorRepository, ModelMapper modelMapper) {
        this.advisorRepository = advisorRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public AdvisorDTO createAdvisor(AdvisorDTO advisorDTO) {
//        if(advisorDTO.getAdvisorId() == null || advisorDTO.getAdvisorId().isEmpty()){
//            advisorDTO.setAdvisorId(UUID.randomUUID().toString());
//        }
// This if condition serves as a fallback for when the DTO does not have a default value
// for advisorId set by @Builder.Default. It ensures that advisorId is generated
// if it is not provided in the AdvisorDTO.

        if(advisorRepository.existsById(advisorDTO.getAdvisorId())){
            throw new IllegalArgumentException("Advisor already exists with ID: " + advisorDTO.getAdvisorId());
        }

        Advisor advisor = modelMapper.map(advisorDTO, Advisor.class);

        Advisor savedAdvisor = advisorRepository.save(advisor);
        return modelMapper.map(savedAdvisor, AdvisorDTO.class);
    }

    @Transactional(readOnly = true)
    public AdvisorDTO getAdvisorById(String advisorId) {
        Advisor advisor = advisorRepository.findById(advisorId)
                .orElseThrow(() -> new IllegalArgumentException("Advisor not found with ID: "+advisorId));
        return modelMapper.map(advisor, AdvisorDTO.class);
    }

    @Transactional(readOnly = true)
    public List<AdvisorDTO> getAllAdvisors() {
        List<Advisor> advisors = advisorRepository.findAll();
        return advisors.stream()
                .map(advisor -> modelMapper.map(advisor, AdvisorDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public AdvisorDTO updateAdvisor(String advisorId, AdvisorDTO advisorDTO) {
        Advisor existingAdvisor = advisorRepository.findById(advisorId)
                .orElseThrow(() -> new IllegalArgumentException("Advisor not found with ID: "+advisorId));
        modelMapper.map(advisorDTO, existingAdvisor);
        Advisor updatedAdvisor = advisorRepository.save(existingAdvisor);
        return modelMapper.map(updatedAdvisor, AdvisorDTO.class);
    }

    @Transactional
    public void deleteAdvisor(String advisorId){
        if(!advisorRepository.existsById(advisorId)){
            throw new IllegalArgumentException("Advisor not found");
        }
        advisorRepository.deleteById(advisorId);
    }

    @Transactional
    public AdvisorDTO addClientToAdvisor(String advisorId, ClientDTO clientDTO){
        Advisor advisor = advisorRepository.findById(advisorId)
                .orElseThrow(() -> new IllegalArgumentException("Advisor not found with ID: "+advisorId));

        List<Client> clientsInAdvisor = advisor.getClients();
        Client client = modelMapper.map(clientDTO, Client.class);

        if(!clientsInAdvisor.contains(client)){
            clientsInAdvisor.add(client);
            client.setAdvisor(advisor); // Set Advisor to Client
            advisorRepository.save(advisor); // When saving an Advisor, the associated Client is also saved by CascadeType.ALL.
        } else {
            throw new IllegalArgumentException("Client already exists in this Advisor");
        }

        return modelMapper.map(advisor, AdvisorDTO.class);
    }

    @Transactional
    public void removeClientFromAdvisor(String advisorId, ClientDTO clientDTO){
        Advisor advisor = advisorRepository.findById(advisorId)
                .orElseThrow(() -> new IllegalArgumentException("Advisor not found with ID: "+advisorId));

        List<Client> clientsInAdvisor = advisor.getClients();
        Client client = modelMapper.map(clientDTO, Client.class);

        if(clientsInAdvisor.contains(client)){
            clientsInAdvisor.remove(client);
            client.setAdvisor(null);
            advisorRepository.save(advisor);
        } else {
            throw new IllegalArgumentException("The specified Client does not exist in this Advisor");
        }
    }

}
