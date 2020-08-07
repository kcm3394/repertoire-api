package personal.kcm3394.repertoireapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import personal.kcm3394.repertoireapi.domain.Composer;
import personal.kcm3394.repertoireapi.domain.enums.Epoch;
import personal.kcm3394.repertoireapi.service.ComposerService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ComposerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ComposerService composerService;

    @Test
    @WithMockUser
    void shouldReturnListOfComposers() throws Exception {
        when(composerService.getAllComposers()).thenReturn(getAllComposers());

        mockMvc.perform(get("/api/composer")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Wolfgang Amadeus Mozart"))
                .andExpect(jsonPath("$.[1].name").value("Giacomo Puccini"));
    }

    @Test
    @WithMockUser
    void shouldReturnCreatedComposer() throws Exception {
        when(composerService.saveComposer(any(Composer.class))).thenReturn(getComposerMozart());

        mockMvc.perform(post("/api/composer/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getComposerMozart()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Wolfgang Amadeus Mozart"));
    }

    @Test
    @WithMockUser
    void shouldReturn200WhenComposerDeleted() throws Exception {
        when(composerService.findComposerById(1L)).thenReturn(getComposerMozart());

        mockMvc.perform(delete("/api/composer/delete/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn401WhenAccessingComposersWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/composer")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenComposerIdDoesNotExist() throws Exception {
        when(composerService.findComposerById(1L)).thenReturn(null);

        mockMvc.perform(delete("/api/composer/delete/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private Composer getComposerMozart() {
        Composer mozart = new Composer();
        mozart.setId(1L);
        mozart.setName("Wolfgang Amadeus Mozart");
        mozart.setBirthDate(LocalDate.of(1756, 1, 1));
        mozart.setDeathDate(LocalDate.of(1791, 1, 1));
        mozart.setEpoch(Epoch.CLASSICAL);

        return mozart;
    }

    private Composer getComposerPuccini() {
        Composer puccini = new Composer();
        puccini.setId(2L);
        puccini.setName("Giacomo Puccini");
        puccini.setBirthDate(LocalDate.of(1858, 12, 22));
        puccini.setDeathDate(LocalDate.of(1924, 11, 29));
        puccini.setEpoch(Epoch.LATE_ROMANTIC);

        return puccini;
    }

    private List<Composer> getAllComposers() {
        Composer mozart = getComposerMozart();
        Composer puccini = getComposerPuccini();
        List<Composer> composers = new ArrayList<>();
        composers.add(mozart);
        composers.add(puccini);

        return composers;
    }
}
