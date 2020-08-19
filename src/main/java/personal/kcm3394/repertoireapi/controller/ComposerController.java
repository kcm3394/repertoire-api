package personal.kcm3394.repertoireapi.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.kcm3394.repertoireapi.domain.Composer;
import personal.kcm3394.repertoireapi.domain.dtos.ComposerDTO;
import personal.kcm3394.repertoireapi.service.ComposerService;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles user requests related to CRUD operations for Composers and returns an HTTP status
 */
@RestController
@RequestMapping("/api/composer")
public class ComposerController {

    private final ComposerService composerService;

    public ComposerController(ComposerService composerService) {
        this.composerService = composerService;
    }

    @GetMapping
    public ResponseEntity<List<ComposerDTO>> listAllComposers() {
        List<Composer> composers = composerService.getAllComposers();
        List<ComposerDTO> composerDTOs = new ArrayList<>();
        composers.forEach(composer ->
                composerDTOs.add(convertEntityToComposerDTO(composer)));
        return ResponseEntity.ok(composerDTOs);
    }

    @PostMapping("/add")
    public ResponseEntity<ComposerDTO> addOrUpdateComposer(@RequestBody ComposerDTO composerDTO) {
        Composer composer = composerService.saveComposer(convertComposerDTOToEntity(composerDTO));
        return ResponseEntity.ok(convertEntityToComposerDTO(composer));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteComposer(@PathVariable Long id) {
        Composer composer = composerService.findComposerById(id);
        if (composer == null) {
            return ResponseEntity.notFound().build();
        }
        composerService.deleteComposer(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search/name/{nameFragment}")
    public ResponseEntity<List<ComposerDTO>> searchComposersByName(@PathVariable String nameFragment) {
        List<Composer> composers = composerService.searchComposersByName(nameFragment);
        List<ComposerDTO> composerDTOs = new ArrayList<>();
        composers.forEach(composer ->
                composerDTOs.add(convertEntityToComposerDTO(composer)));
        return ResponseEntity.ok(composerDTOs);
    }

    @GetMapping("/search/epoch/{epoch}")
    public ResponseEntity<List<ComposerDTO>> searchComposersByEpoch(@PathVariable String epoch) {
        List<Composer> composers = composerService.searchComposersByEpoch(epoch);
        List<ComposerDTO> composerDTOs = new ArrayList<>();
        composers.forEach(composer ->
                composerDTOs.add(convertEntityToComposerDTO(composer)));
        return ResponseEntity.ok(composerDTOs);
    }

    @GetMapping("/search/composition/{composition}")
    public ResponseEntity<List<ComposerDTO>> searchComposersByComposition(@PathVariable String composition) {
        List<Composer> composers = composerService.searchComposersByComposition(composition);
        List<ComposerDTO> composerDTOs = new ArrayList<>();
        composers.forEach(composer ->
                composerDTOs.add(convertEntityToComposerDTO(composer)));
        return ResponseEntity.ok(composerDTOs);
    }

    private static ComposerDTO convertEntityToComposerDTO(Composer composer) {
        ComposerDTO composerDTO = new ComposerDTO();
        BeanUtils.copyProperties(composer, composerDTO);
        return composerDTO;
    }

    private static Composer convertComposerDTOToEntity(ComposerDTO composerDTO) {
        Composer composer = new Composer();
        BeanUtils.copyProperties(composerDTO, composer);
        return composer;
    }
}
