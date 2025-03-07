package org.example.app.service;

import org.example.app.dto.TagDto;
import org.example.app.entity.Tag;
import org.example.app.exception.DatabaseException;
import org.example.app.exception.TagNotFoundException;

public interface TagsService {

    TagDto getTag(Long tagId) throws TagNotFoundException, DatabaseException;

    TagDto putTag(Long tagId, Tag newTag) throws TagNotFoundException;

    TagDto deleteTag(Long tagId) throws TagNotFoundException;

    TagDto createTag(Tag tag);
}
