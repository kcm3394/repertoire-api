package personal.kcm3394.repertoireapi.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<Page<ComposerDTO>> listAllComposers(Pageable pageable) {
        Page<Composer> composers = composerService.getAllComposers(pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfCompDTOs(composers, pageable));
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
    public ResponseEntity<Page<ComposerDTO>> searchComposersByName(@PathVariable String nameFragment, Pageable pageable) {
        Page<Composer> composers = composerService.searchComposersByName(nameFragment, pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfCompDTOs(composers, pageable));
    }

    @GetMapping("/search/epoch/{epoch}")
    public ResponseEntity<Page<ComposerDTO>> searchComposersByEpoch(@PathVariable String epoch, Pageable pageable) {
        Page<Composer> composers = composerService.searchComposersByEpoch(epoch, pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfCompDTOs(composers, pageable));
    }

    @GetMapping("/search/composition/{composition}")
    public ResponseEntity<Page<ComposerDTO>> searchComposersByComposition(@PathVariable String composition, Pageable pageable) {
        Page<Composer> composers = composerService.searchComposersByComposition(composition, pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfCompDTOs(composers, pageable));
    }

    private static PageImpl<ComposerDTO> convertPageOfEntitiesToPageImplOfCompDTOs(Page<Composer> composers, Pageable pageable) {
        List<ComposerDTO> composerDTOs = new ArrayList<>();
        composers.forEach(composer ->
                composerDTOs.add(convertEntityToComposerDTO(composer)));
        return new PageImpl<>(composerDTOs, pageable, composers.getTotalElements());
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
