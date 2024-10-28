package com.wellsfargo.counselor.service;

import com.wellsfargo.counselor.dto.AdvisorDTO;
import com.wellsfargo.counselor.dto.ClientDTO;
import com.wellsfargo.counselor.entity.Advisor;
import com.wellsfargo.counselor.entity.Client;
import com.wellsfargo.counselor.entity.Portfolio;
import com.wellsfargo.counselor.repository.AdvisorRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdvisorServiceTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AdvisorRepository advisorRepository; // Dependent repository (mock object)

    @InjectMocks
    private AdvisorService advisorService; // Class to test

    private Advisor advisor;
    private AdvisorDTO advisorDTO;
    private List<Advisor> advisors;
    private List<AdvisorDTO> advisorDTOs;
    private Client client;
    private ClientDTO clientDTO;
    private List<Client> clients;

    private AutoCloseable closeable; // Variable for Mockito resource management


    @BeforeEach // Specifies a method to be called before each test method is executed.
                // Initialize objects, allocate necessary resources, and set up the test environment here.
    void setUp() {
        // Initialize objects or set up resources needed for testing
        closeable = MockitoAnnotations.openMocks(this); // Mockito initialization

        initializeTestData();
    }

    private void initializeTestData() {
        advisorDTO = new AdvisorDTO("1", "John", "Doe",
                "123 Main St", "555-1234", "john.doe@example.com");
        advisor = new Advisor(
                advisorDTO.getAdvisorId(),
                advisorDTO.getFirstName(),
                advisorDTO.getLastName(),
                advisorDTO.getAddress(),
                advisorDTO.getPhone(),
                advisorDTO.getEmail()
        );

        advisors = List.of(advisor); // List.of() creates an immutable list

        advisorDTOs = List.of(advisorDTO);

        clientDTO = ClientDTO.builder()
                .advisorId("1")
                .firstName("Mac")
                .lastName("Lovin")
                .address("456 Main St, Stadtville")
                .phone("+1-222-3333")
                .email("mac.lovin@example.com")
                .portfolios(new ArrayList<>()) // Empty portfolio list
                .build();

        client = new Client(
                clientDTO.getClientId(),
                advisor,
                clientDTO.getPortfolios().stream()
                        .map(portfolioDTO -> modelMapper.map(portfolioDTO, Portfolio.class))
                        .toList(),
                clientDTO.getFirstName(),
                clientDTO.getLastName(),
                clientDTO.getAddress(),
                clientDTO.getPhone(),
                clientDTO.getEmail()
        );

        clients = new ArrayList<>(); // Creates a mutable list
        clients.add(client);
    }

    @AfterEach // Specifies the method that is called after each test method is executed.
               // Used to release allocated resources or restore state modified by the test
    void tearDown() throws Exception {
        // Clean up resources or restore the state after tests
        closeable.close(); // Clean up resources
    }

    @Test
    void createAdvisor() {
        // Test logic
        when(modelMapper.map(advisorDTO, Advisor.class)).thenReturn(advisor);
        when(advisorRepository.save(advisor)).thenReturn(advisor);
        when(modelMapper.map(advisor, AdvisorDTO.class)).thenReturn(advisorDTO);

        AdvisorDTO createdAdvisor = advisorService.createAdvisor(advisorDTO);

        assertNotNull(createdAdvisor); // Check that createdAdvisor is not null
        assertEquals(advisorDTO, createdAdvisor); // Check that the created advisor is equal to the expected DTO
        verify(advisorRepository).save(advisor);  // Verify that the advisor was saved
    }

    @Test
    void getAdvisorById() {
        // Advisor ID to use for testing
        String advisorId = "1";

        when(modelMapper.map(advisorDTO, Advisor.class)).thenReturn(advisor);
        // Assumption: An Advisor with the specified ID exists.
        when(advisorRepository.findById(advisorId)).thenReturn(Optional.of(advisor));
        when(modelMapper.map(advisor, AdvisorDTO.class)).thenReturn(advisorDTO);

        // Call service method
        AdvisorDTO foundAdvisor = advisorService.getAdvisorById(advisorId);

        // Test validation
        assertNotNull(foundAdvisor); // Check that the advisor is not null
        assertEquals(advisorDTO, foundAdvisor); // Check that the returned DTO is equal to the expected value
        verify(advisorRepository).findById(advisorId); // 해당 ID로 레포지토리에서 조회했는지 검증
    }

    @Test
    void getAllAdvisors() {
        when(advisorRepository.findAll()).thenReturn(advisors);
        when(modelMapper.map(advisor, AdvisorDTO.class)).thenReturn(advisorDTOs.get(0));

        List<AdvisorDTO> advisorDTOList = advisorService.getAllAdvisors();

        assertNotNull(advisorDTOList);
        assertEquals(1, advisorDTOList.size());
        assertEquals(advisorDTOs, advisorDTOList);

        verify(advisorRepository).findAll();
    }

    @Test
    void updateAdvisor() {
        String advisorId = "1";

        when(advisorRepository.findById(advisorId)).thenReturn(Optional.of(advisor));
        when(modelMapper.map(advisorDTO, Advisor.class)).thenReturn(advisor);
        when(modelMapper.map(advisor, AdvisorDTO.class)).thenReturn(advisorDTO);
        when(advisorRepository.save(advisor)).thenReturn(advisor);

        AdvisorDTO updatedAdvisor = advisorService.updateAdvisor(advisorId, advisorDTO);

        assertNotNull(updatedAdvisor);
        assertEquals(advisorDTO, updatedAdvisor);
        verify(advisorRepository).findById(advisorId);
        verify(advisorRepository).save(advisor);
    }

    @Test
    void deleteAdvisor() {
        String advisorId = "1";

        when(advisorRepository.existsById(advisorId)).thenReturn(true);
        doNothing().when(advisorRepository).deleteById(advisorId);

        advisorService.deleteAdvisor(advisorId);

        verify(advisorRepository).existsById(advisorId);
        verify(advisorRepository).deleteById(advisorId);
    }

    @Test
    void addClientToAdvisor() {
        // Mocking the findById method of advisorRepository
        when(advisorRepository.findById(advisor.getAdvisorId())).thenReturn(Optional.of(advisor));

        // Mocking modelMapper settings
        when(modelMapper.map(clientDTO, Client.class)).thenReturn(client);
        when(modelMapper.map(advisor, AdvisorDTO.class)).thenReturn(advisorDTO);

        // Initial client list setup
        advisor.setClients(new ArrayList<>());

        AdvisorDTO result = advisorService.addClientToAdvisor(advisor.getAdvisorId(), clientDTO);

        // Check that the client was added to the advisor's list
        assertTrue(advisor.getClients().contains(client));
        assertEquals(advisor.getAdvisorId(), result.getAdvisorId());
        verify(advisorRepository).save(advisor);

    }

    @Test
    void addClientToAdvisor_ClientAlreadyExists() {
        String advisorId = advisorDTO.getAdvisorId();
        // Mocking the findById method of advisorRepository
        when(advisorRepository.findById(advisorId)).thenReturn(Optional.of(advisor));
        // Mocking modelMapper settings
        when(modelMapper.map(clientDTO, Client.class)).thenReturn(client);

        // Initial client list setup (set to include an existing client)
        advisor.setClients(clients);

        // Verify that the addClientToAdvisor method throws an exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                advisorService.addClientToAdvisor(advisorId, clientDTO)
        );

        // Check that the exception message is correct
        assertEquals("Client already exists in this Advisor", exception.getMessage());

        // Verify that the save method of advisorRepository was not called
        verify(advisorRepository, never()).save(advisor);

    }

    @Test
    void removeClientFromAdvisor() {
        String advisorId = advisorDTO.getAdvisorId();
        when(advisorRepository.findById(advisorId)).thenReturn(Optional.of(advisor));
        when(modelMapper.map(clientDTO, Client.class)).thenReturn(client);

        // Add a client to the initial client list for testing
        advisor.setClients(clients);

        // Remove the client from the advisor
        advisorService.removeClientFromAdvisor(advisorId, clientDTO);

        // Check that the client was removed from the advisor's client list
        assertFalse(advisor.getClients().contains(client));

        // Verify that the save method of advisorRepository was called
        verify(advisorRepository).save(advisor);

    }

    @Test
    void removeClientFromAdvisor_ClientNotExists() {
        String advisorId = advisorDTO.getAdvisorId();
        when(advisorRepository.findById(advisorId)).thenReturn(Optional.of(advisor));
        when(modelMapper.map(clientDTO, Client.class)).thenReturn(client);

        // Set the advisor's client list to be empty
        advisor.setClients(new ArrayList<>());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                advisorService.removeClientFromAdvisor(advisorId, clientDTO)
                );

        assertEquals("The specified Client does not exist in this Advisor", exception.getMessage());

        // Verify that the save method of advisorRepository was not called
        verify(advisorRepository, never()).save(advisor);

    }

}