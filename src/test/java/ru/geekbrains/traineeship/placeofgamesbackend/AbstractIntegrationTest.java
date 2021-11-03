package ru.geekbrains.traineeship.placeofgamesbackend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AbstractIntegrationTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @SneakyThrows
    protected <T> T getResponse(MvcResult mvcResult, Class<T> valueType) {
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), valueType);
    }

    @SneakyThrows
    protected <T> T getResponse(MvcResult mvcResult, TypeReference<T> valueType) {
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), valueType);
    }

}
