package org.example.app.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.app.dto.TagDto;
import org.example.app.entity.Tag;
import org.example.app.exception.TagNotFoundException;
import org.example.app.service.TagsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@CircuitBreaker(name = "CircuitBreakerAPI")
@RequiredArgsConstructor
public class TagsControllerImpl implements TagsController {
    private final TagsServiceImpl tagsService;

    @GetMapping("/tags/get/{tagId}")
    @Override
    public ResponseEntity<TagDto> getTag(@PathVariable Long tagId) throws TagNotFoundException {
        TagDto tagDto = tagsService.getTag(tagId);
        return ResponseEntity.ok(tagDto);
    }

    @DeleteMapping("/tags/delete/{tagId}")
    @Override
    public ResponseEntity<TagDto> deleteTag(@PathVariable Long tagId) throws TagNotFoundException {
        TagDto tagDto = tagsService.deleteTag(tagId);
        return ResponseEntity.ok(tagDto);
    }


    @PutMapping("/tags/put/{tagId}")
    @Override
    public ResponseEntity<TagDto> putTag(@PathVariable Long tagId, @RequestBody Tag newTag) throws TagNotFoundException {
        TagDto tagDto = tagsService.putTag(tagId, newTag);
        return ResponseEntity.ok(tagDto);
    }

    @PostMapping("/tags/create")
    @Override
    public ResponseEntity<TagDto> createTag(@RequestBody Tag tag) {
        TagDto tagDto = tagsService.createTag(tag);
        return ResponseEntity.status(201).body(tagDto);
    }
}
