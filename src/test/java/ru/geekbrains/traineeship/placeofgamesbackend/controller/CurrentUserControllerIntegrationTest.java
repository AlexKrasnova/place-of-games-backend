package ru.geekbrains.traineeship.placeofgamesbackend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.geekbrains.traineeship.placeofgamesbackend.AbstractIntegrationTest;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Event;
import ru.geekbrains.traineeship.placeofgamesbackend.model.User;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.EventRepository;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.PlaceRepository;
import ru.geekbrains.traineeship.placeofgamesbackend.repository.UserRepository;

import java.util.List;

public class CurrentUserControllerIntegrationTest extends AbstractIntegrationTest {

    private static final String BASE_URL = "/api/v1/user";

    private static final String TEST_USER = "testUser";

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void tearDown() {
        eventRepository.deleteAll();
        userRepository.deleteAll();
        placeRepository.deleteAll();
    }

    /**
     * Интеграционный тест на проверку успешного получения информации о текущем пользователе.
     * <p>
     * В тесте:
     * 1. Создается и добавляется в бд пользователь.
     * 2. Вызывается GET /api/v1/user.
     * 3. Проверяется, что в результате запроса получается статус 200 OK.
     * 4. Проверяется, что полученные данные пользователя соответсвтуют данным созданного нами пользователя.
     */
    @WithMockUser(value = TEST_USER)
    @Test
    public void getUserDetailsSuccess() throws Exception {

        User user = createUser(TEST_USER);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andReturn();

        User actual = getResponse(result, User.class);

        Assertions.assertThat(actual.getId()).isEqualTo(user.getId());
        Assertions.assertThat(actual.getLogin()).isEqualTo(user.getLogin());
        Assertions.assertThat(actual.getName()).isEqualTo(user.getName());

    }

    /**
     * Интеграционный тест на проверку успешного получения списка мероприятий, которые создал и администрирует пользователь,
     * когда пользователь не администрирует ни одного мероприятия.
     * <p>
     * В тесте:
     * 1. Создается и добавляется в бд пользователь.
     * 2. Вызывается GET /api/v1/user/owned-events.
     * 3. Проверяется, что в результате запроса получается статус 200 OK.
     * 4. Проверяется, что результат является пустым списком.
     */
    @WithMockUser(value = TEST_USER)
    @Test
    public void getOwnedEventsUserNotOwnEvents() throws Exception {

        User user = createUser(TEST_USER);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL + "/owned-events"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andReturn();

        List<Event> actual = getResponse(result, new TypeReference<>() {
        });

        Assertions.assertThat(actual).isEmpty();
    }

    /**
     * Интеграционный тест на проверку успешного получения списка мероприятий, на которые записан пользователь,
     * когда пользователь не записан ни на одно мероприятие.
     * <p>
     * В тесте:
     * 1. Создается и добавляется в бд пользователь.
     * 2. Вызывается GET /api/v1/user/events-to-participate.
     * 3. Проверяется, что в результате запроса получается статус 200 OK.
     * 4. Проверяется, что результат является пустым списком.
     */
    @WithMockUser(value = TEST_USER)
    @Test
    public void getEventsToParticipateUserParticipateInNoEvents() throws Exception {

        User user = createUser(TEST_USER);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL + "/events-to-participate"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andReturn();

        List<Event> actual = getResponse(result, new TypeReference<>() {
        });

        Assertions.assertThat(actual).isEmpty();
    }


    private User createUser(String login) {

        User user = User.builder()
                .login(login)
                .name("name")
                .password("1")
                .build();

        return userRepository.save(user);

    }
}
