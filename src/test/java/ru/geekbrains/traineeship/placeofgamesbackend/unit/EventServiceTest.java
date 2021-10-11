package ru.geekbrains.traineeship.placeofgamesbackend.unit;

import ru.geekbrains.traineeship.placeofgamesbackend.BaseTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.EventDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.exception.EventNotFoundException;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.EventRepository;
import ru.geekbrains.traineeship.placeofgamesbackend.service.EventService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(classes =EventService.class)
public class EventServiceTest extends BaseTest {

    @Autowired
    private EventService eventService;

    @MockBean
    private EventRepository productRepository;

    @Test
    public void findAllSuccess() {
        Mockito.doReturn(Collections.emptyList()).when(productRepository).findAll();
        List<Event> actual = eventService.findAll();
        Assertions.assertThat(actual).isEmpty();
    }

    @Test
    public void findByIdWithException() {
        long id = 1L;
        Mockito.doReturn(Optional.empty()).when(productRepository).findById(id);
        assertThrows(EventNotFoundException.class, () -> eventService.findById(id));
    }

}
