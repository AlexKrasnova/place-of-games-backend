package ru.geekbrains.traineeship.placeofgamesbackend.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.EventDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.EventRepository;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class EventControllerIntegrationTest {
    private static final String BASE_URL = "/api/v1/product";

    @Autowired
    private EventRepository eventRepository;

    @LocalServerPort
    private String port;

    @Autowired
    private TestRestTemplate restTemplate;

    @AfterEach
    void tearDown() {
        eventRepository.deleteAll();
    }
    @Test
    @DisplayName("Успещный Поиск по идентификатору")
    public void findByIdSuccess() {
        Event event = new Event();
        event.setName("test");
        event.setDescription("Test event");
        Event savedEvent = eventRepository.save(event);
        ResponseEntity<EventDTO> actual = restTemplate.getForEntity("http://localhost:" + port + BASE_URL + "/" + savedEvent.getId(),EventDTO.class);
        Assertions.assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(actual.getBody()).isNotNull();
    }

  /*  @Test
    public void findByIdFail() {
        ResponseEntity<EventDTO> actual = restTemplate.getForEntity("http://localhost:" + port + BASE_URL + "/1", EventDTO.class);
        Assertions.assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }*/


}
