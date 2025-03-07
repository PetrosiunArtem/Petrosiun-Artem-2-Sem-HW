package org.example.app.controller;

import org.example.app.dto.TagDto;
import org.example.app.entity.Tag;
import org.example.app.exception.TagNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface TagsController {

    ResponseEntity<TagDto> getTag(@PathVariable Long tagId) throws TagNotFoundException;

    ResponseEntity<TagDto> deleteTag(@PathVariable Long tagId) throws TagNotFoundException;

    ResponseEntity<TagDto> putTag(@PathVariable Long tagId, Tag newTag) throws TagNotFoundException;

    ResponseEntity<TagDto> createTag(Tag tag);
}
