package ru.geekbrains.traineeship.placeofgamesbackend.unit;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.geekbrains.traineeship.placeofgamesbackend.controller.EventController;
import ru.geekbrains.traineeship.placeofgamesbackend.dto.EventDTO;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;
import ru.geekbrains.traineeship.placeofgamesbackend.service.EventService;

import java.util.List;

@WebMvcTest(controllers = EventController.class)
public class EventControllerMockMvcTest {

    private static final String BASE_URL = "/api/v1/product";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EventService eventService;

    @Test
    public void findAllSuccess() throws Exception {
        EventDTO event =  EventDTO.builder()
        .id(1L)
                .name("test")
                        .build();

      

        Mockito.doReturn(List.of(event)).when(eventService).findAll();

        mvc.perform(MockMvcRequestBuilders.get(BASE_URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[{\"id\":1,\"name\":\"test\",\"price\":100}]", true));
    }
}
