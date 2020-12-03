package personal.kcm3394.songcomposerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import personal.kcm3394.songcomposerservice.model.Composer;
import personal.kcm3394.songcomposerservice.model.Epoch;
import personal.kcm3394.songcomposerservice.model.dtos.ComposerDto;
import personal.kcm3394.songcomposerservice.service.ComposerService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ComposerController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ComposerControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ComposerService composerService;

    @Test
    void should_return_page_of_composers() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        when(composerService.getAllComposers(any(Pageable.class))).thenReturn(getAllComposers(pageable));

        mockMvc.perform(get("/api/v2/composers")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Wolfgang Amadeus Mozart"))
                .andExpect(jsonPath("$.content[1].name").value("Giacomo Puccini"))
                .andExpect(jsonPath("$.pageable.paged").value("true"));
    }

    @Test
    void should_return_created_composer() throws Exception {
        when(composerService.saveComposer(any(Composer.class))).thenReturn(buildMozart());
        when(composerService.findComposerByNameAndEpoch(anyString(), any(Epoch.class))).thenReturn(null);

        mockMvc.perform(post("/api/v2/composers/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buildMozart()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Wolfgang Amadeus Mozart"));
    }

    @Test
    void should_return_400_when_adding_composer_without_name() throws Exception {
        ComposerDto dto = new ComposerDto();

        mockMvc.perform(post("/api/v2/composers/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_when_adding_composer_without_epoch() throws Exception {
        ComposerDto dto = new ComposerDto();
        dto.setName("John Smith");

        mockMvc.perform(post("/api/v2/composers/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_when_adding_existing_composer() throws Exception {
        when(composerService.findComposerByNameAndEpoch(anyString(), any(Epoch.class))).thenReturn(buildMozart());

        mockMvc.perform(post("/api/v2/composers/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buildMozart()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_updated_composer() throws Exception {
        Composer update = buildMozart();
        update.setEpoch(Epoch.TWENTY_FIRST_CENTURY);
        when(composerService.findComposerById(anyLong())).thenReturn(Optional.of(buildMozart()));
        when(composerService.saveComposer(any(Composer.class))).thenReturn(update);

        mockMvc.perform(put("/api/v2/composers/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Wolfgang Amadeus Mozart"))
                .andExpect(jsonPath("$.epoch").value("TWENTY_FIRST_CENTURY"));
    }

    @Test
    void should_return_400_when_updating_composer_without_id() throws Exception {
        ComposerDto dto = new ComposerDto();
        dto.setName("John Smith");
        dto.setEpoch(Epoch.TWENTY_FIRST_CENTURY);

        mockMvc.perform(put("/api/v2/composers/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_404_when_updating_composer_with_unassigned_id() throws Exception {
        ComposerDto dto = new ComposerDto();
        dto.setId(5L);
        dto.setName("John Smith");
        dto.setEpoch(Epoch.TWENTY_FIRST_CENTURY);
        when(composerService.findComposerById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v2/composers/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_200_when_composer_deleted() throws Exception {
        when(composerService.findComposerById(1L)).thenReturn(Optional.of(buildMozart()));

        mockMvc.perform(delete("/api/v2/composers/delete/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_404_when_composer_id_does_not_exist() throws Exception {
        when(composerService.findComposerById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/v2/composers/delete/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_page_with_mozart_when_search_by_name() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        ArrayList<Composer> justMozart = new ArrayList<>();
        justMozart.add(buildMozart());
        PageImpl<Composer> justMozartPage = new PageImpl<>(justMozart, pageable, justMozart.size());
        when(composerService.searchComposersByName(anyString(), any(Pageable.class))).thenReturn(justMozartPage);

        mockMvc.perform(get("/api/v2/composers/search/name/moz")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Wolfgang Amadeus Mozart"))
                .andExpect(jsonPath("$.pageable.paged").value("true"));
    }

    @Test
    void should_return_list_with_mozart_when_search_by_epoch() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        ArrayList<Composer> justMozart = new ArrayList<>();
        justMozart.add(buildMozart());
        PageImpl<Composer> justMozartPage = new PageImpl<>(justMozart, pageable, justMozart.size());
        when(composerService.searchComposersByEpoch(any(Epoch.class), any(Pageable.class))).thenReturn(justMozartPage);

        mockMvc.perform(get("/api/v2/composers/search/epoch/classical")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Wolfgang Amadeus Mozart"))
                .andExpect(jsonPath("$.pageable.paged").value("true"));
    }

    @Test
    void should_return_list_with_mozart_when_search_by_composition() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        ArrayList<Composer> justMozart = new ArrayList<>();
        justMozart.add(buildMozart());
        PageImpl<Composer> justMozartPage = new PageImpl<>(justMozart, pageable, justMozart.size());
        when(composerService.searchComposersByComposition(anyString(), any(Pageable.class))).thenReturn(justMozartPage);

        mockMvc.perform(get("/api/v2/composers/search/composition/dove")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Wolfgang Amadeus Mozart"))
                .andExpect(jsonPath("$.pageable.paged").value("true"));
    }

    private Composer buildMozart() {
        return Composer.builder()
                .id(1L)
                .name("Wolfgang Amadeus Mozart")
                .birthDate(LocalDate.of(1756, 1, 1))
                .deathDate(LocalDate.of(1791, 1, 1))
                .epoch(Epoch.CLASSICAL)
                .build();
    }

    private Composer buildPuccini() {
        return Composer.builder()
                .id(2L)
                .name("Giacomo Puccini")
                .birthDate(LocalDate.of(1858, 12, 22))
                .deathDate(LocalDate.of(1924, 11, 29))
                .epoch(Epoch.LATE_ROMANTIC)
                .build();
    }

    private Page<Composer> getAllComposers(Pageable pageable) {
        Composer mozart = buildMozart();
        Composer puccini = buildPuccini();
        List<Composer> composers = new ArrayList<>();
        composers.add(mozart);
        composers.add(puccini);

        return new PageImpl<>(composers, pageable, composers.size());
    }
}
