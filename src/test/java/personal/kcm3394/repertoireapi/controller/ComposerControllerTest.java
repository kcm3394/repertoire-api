package personal.kcm3394.repertoireapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import personal.kcm3394.repertoireapi.domain.Composer;
import personal.kcm3394.repertoireapi.domain.enums.Epoch;
import personal.kcm3394.repertoireapi.service.ComposerService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    void should_return_page_of_composers() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        when(composerService.getAllComposers(any(Pageable.class))).thenReturn(getAllComposers(pageable));

        mockMvc.perform(get("/api/composer")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Wolfgang Amadeus Mozart"))
                .andExpect(jsonPath("$.content[1].name").value("Giacomo Puccini"))
                .andExpect(jsonPath("$.pageable.paged").value("true"));
    }

    @Test
    @WithMockUser
    void should_return_created_composer() throws Exception {
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
    void should_return_200_when_composer_deleted() throws Exception {
        when(composerService.findComposerById(1L)).thenReturn(Optional.of(getComposerMozart()));

        mockMvc.perform(delete("/api/composer/delete/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_401_when_accessing_composers_without_auth() throws Exception {
        mockMvc.perform(get("/api/composer")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void should_return_404_when_composer_id_does_not_exist() throws Exception {
        when(composerService.findComposerById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/composer/delete/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void should_return_page_with_mozart_when_search_by_name() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        ArrayList<Composer> justMozart = new ArrayList<>();
        justMozart.add(getComposerMozart());
        PageImpl<Composer> justMozartPage = new PageImpl<>(justMozart, pageable, justMozart.size());
        when(composerService.searchComposersByName(anyString(), any(Pageable.class))).thenReturn(justMozartPage);

        mockMvc.perform(get("/api/composer/search/name/moz")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Wolfgang Amadeus Mozart"))
                .andExpect(jsonPath("$.pageable.paged").value("true"));
    }

    @Test
    @WithMockUser
    void should_return_list_with_mozart_when_search_by_epoch() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        ArrayList<Composer> justMozart = new ArrayList<>();
        justMozart.add(getComposerMozart());
        PageImpl<Composer> justMozartPage = new PageImpl<>(justMozart, pageable, justMozart.size());
        when(composerService.searchComposersByEpoch(anyString(), any(Pageable.class))).thenReturn(justMozartPage);

        mockMvc.perform(get("/api/composer/search/epoch/classical")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Wolfgang Amadeus Mozart"))
                .andExpect(jsonPath("$.pageable.paged").value("true"));
    }

    @Test
    @WithMockUser
    void should_return_list_with_mozart_when_search_by_composition() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        ArrayList<Composer> justMozart = new ArrayList<>();
        justMozart.add(getComposerMozart());
        PageImpl<Composer> justMozartPage = new PageImpl<>(justMozart, pageable, justMozart.size());
        when(composerService.searchComposersByComposition(anyString(), any(Pageable.class))).thenReturn(justMozartPage);

        mockMvc.perform(get("/api/composer/search/composition/dove")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Wolfgang Amadeus Mozart"))
                .andExpect(jsonPath("$.pageable.paged").value("true"));
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

    private Page<Composer> getAllComposers(Pageable pageable) {
        Composer mozart = getComposerMozart();
        Composer puccini = getComposerPuccini();
        List<Composer> composers = new ArrayList<>();
        composers.add(mozart);
        composers.add(puccini);

        return new PageImpl<>(composers, pageable, composers.size());
    }
}
