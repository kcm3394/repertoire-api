package personal.kcm3394.songcomposerservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.kcm3394.songcomposerservice.model.Composer;
import personal.kcm3394.songcomposerservice.model.Epoch;
import personal.kcm3394.songcomposerservice.model.dtos.ComposerDto;
import personal.kcm3394.songcomposerservice.service.ComposerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v2/composers")
@RequiredArgsConstructor
@Slf4j
public class ComposerController {

    private final ComposerService composerService;

    @GetMapping
    public ResponseEntity<Page<ComposerDto>> listAllComposers(Pageable pageable) {
        log.info("Finding all composers");
        Page<Composer> composers = composerService.getAllComposers(pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfCompDtos(composers, pageable));
    }

    @PostMapping("/add")
    public ResponseEntity<ComposerDto> addComposer(@RequestBody ComposerDto composerDto) {
        if (composerDto.getName() == null) {
            //TODO custom error response
            log.error("Composer name must not be null");
            return ResponseEntity.badRequest().build();
        }

        if (composerDto.getEpoch() == null) {
            //TODO custom error response
            log.error("Epoch must not be null");
            return ResponseEntity.badRequest().build();
        }

        log.info("Trying to add " + composerDto.getName());
        Composer similar = composerService.findComposerByNameAndEpoch(composerDto.getName(), composerDto.getEpoch());
        if (similar != null && composerDto.getName().equals(similar.getName()) && composerDto.getEpoch().equals(similar.getEpoch())) {
            //TODO custom error response
            log.error("Composer with name " + composerDto.getName() + " and epoch " + composerDto.getEpoch().toString() + " already exists");
            return ResponseEntity.badRequest().build();
        }

        log.info("Adding composer: " + composerDto.getName());
        Composer composer = composerService.saveComposer(convertComposerDtoToEntity(composerDto));
        return ResponseEntity.ok(convertEntityToComposerDto(composer));
    }

    @PutMapping("/update")
    public ResponseEntity<ComposerDto> updateComposer(@RequestBody ComposerDto composerDto) {
        if (composerDto.getId() == null) {
            //TODO custom error response
            log.error("Composer id must not be null to update composer");
            return ResponseEntity.badRequest().build();
        }

        Optional<Composer> optionalComposer = composerService.findComposerById(composerDto.getId());
        if (optionalComposer.isEmpty()) {
            //TODO custom error response
            log.error("Composer not found with id: " + composerDto.getId());
            return ResponseEntity.notFound().build();
        }
        Composer composer = optionalComposer.get();

        log.info("Updating composer: " + composerDto.getName());
        composer.setName(composerDto.getName());
        if (composerDto.getBirthDate() != null) {
            composer.setBirthDate(composerDto.getBirthDate());
        }
        if (composerDto.getDeathDate() != null) {
            composer.setDeathDate(composerDto.getDeathDate());
        }
        composer.setEpoch(composerDto.getEpoch());

        Composer updated = composerService.saveComposer(composer);
        return ResponseEntity.ok(convertEntityToComposerDto(updated));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteComposer(@PathVariable Long id) {
        Optional<Composer> composer = composerService.findComposerById(id);
        if (composer.isEmpty()) {
            //todo custom error response
            log.error("Composer not found with id: " + id);
            return ResponseEntity.notFound().build();
        }
        log.info("Deleting composer with id: " + id);
        composerService.deleteComposer(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search/name/{nameFragment}")
    public ResponseEntity<Page<ComposerDto>> searchComposersByName(@PathVariable String nameFragment, Pageable pageable) {
        log.info("Searching for composers with name like: " + nameFragment);
        Page<Composer> composers = composerService.searchComposersByName(nameFragment, pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfCompDtos(composers, pageable));
    }

    @GetMapping("/search/epoch/{epoch}")
    public ResponseEntity<Page<ComposerDto>> searchComposersByEpoch(@PathVariable Epoch epoch, Pageable pageable) {
        log.info("Searching for composers from epoch: " + epoch);
        Page<Composer> composers = composerService.searchComposersByEpoch(epoch, pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfCompDtos(composers, pageable));
    }

    @GetMapping("/search/composition/{composition}")
    public ResponseEntity<Page<ComposerDto>> searchComposersByComposition(@PathVariable String composition, Pageable pageable) {
        log.info("Searching for composers with song titled: " + composition);
        Page<Composer> composers = composerService.searchComposersByComposition(composition, pageable);
        return ResponseEntity.ok(convertPageOfEntitiesToPageImplOfCompDtos(composers, pageable));
    }

    private static PageImpl<ComposerDto> convertPageOfEntitiesToPageImplOfCompDtos(Page<Composer> composers, Pageable pageable) {
        List<ComposerDto> composerDtos = new ArrayList<>();
        composers.forEach(composer ->
                composerDtos.add(convertEntityToComposerDto(composer)));
        return new PageImpl<>(composerDtos, pageable, composers.getTotalElements());
    }

    private static ComposerDto convertEntityToComposerDto(Composer composer) {
        ComposerDto composerDto = new ComposerDto();
        BeanUtils.copyProperties(composer, composerDto);
        return composerDto;
    }

    private static Composer convertComposerDtoToEntity(ComposerDto composerDto) {
        Composer composer = new Composer();
        BeanUtils.copyProperties(composerDto, composer);
        return composer;
    }
}
