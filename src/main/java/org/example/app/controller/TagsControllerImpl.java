package org.example.app.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.example.app.dto.TagDto;
import org.example.app.entity.Tag;
import org.example.app.exception.TagNotFoundException;
import org.example.app.service.TagsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CircuitBreaker(name = "CircuitBreakerAPI")
@RequiredArgsConstructor
@RequestMapping("/second-memory")
public class TagsControllerImpl implements TagsController {
  private final TagsServiceImpl tagsService;

  @Override
  public ResponseEntity<TagDto> getTag(Long tagId) throws TagNotFoundException {
    TagDto tagDto = tagsService.getTag(tagId);
    return ResponseEntity.ok(tagDto);
  }

  @Override
  public ResponseEntity<TagDto> deleteTag(Long tagId) throws TagNotFoundException {
    TagDto tagDto = tagsService.deleteTag(tagId);
    return ResponseEntity.ok(tagDto);
  }

  @Override
  public ResponseEntity<TagDto> putTag(Long tagId, Tag newTag) throws TagNotFoundException {
    TagDto tagDto = tagsService.putTag(tagId, newTag);
    return ResponseEntity.ok(tagDto);
  }

  @Override
  public ResponseEntity<TagDto> createTag(Tag tag) {
    TagDto tagDto = tagsService.createTag(tag);
    return ResponseEntity.status(201).body(tagDto);
  }
}
