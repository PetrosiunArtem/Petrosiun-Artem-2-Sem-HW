package org.example.app.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.app.dto.TagDto;
import org.example.app.entity.Tag;
import org.example.app.exception.TagNotFoundException;
import org.example.app.mapper.TagMapper;
import org.example.app.repository.TagsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TagsServiceImpl implements TagsService {
    private final TagsRepository tagsRepository;
    private final TagMapper tagMapper;

    @Override
    @Transactional
    public TagDto getTag(Long tagId) throws TagNotFoundException {
        log.info("Функция по взятию тега вызвана в сервисе");
        Optional<Tag> response = tagsRepository.findById(tagId);
        if (response.isEmpty()) {
            throw new TagNotFoundException();
        }
        Tag tag = response.get();
        return tagMapper.toDto(tag);
    }

    @Override
    @Transactional
    public TagDto createTag(Tag tag) {
        log.info("Функция по созданию тега вызвана в сервисе");
        tagsRepository.save(tag);
        return tagMapper.toDto(tag);
    }

    @Override
    @Transactional
    public TagDto putTag(Long tagId, Tag newTag) throws TagNotFoundException {
        log.info("Функция по замене тега вызвана в сервисе");
        Optional<Tag> response = tagsRepository.findById(tagId);
        if (response.isEmpty()) {
            throw new TagNotFoundException();
        }
        Tag tag = response.get();
        tag.setName(newTag.getName());
        tagsRepository.save(tag);
        return tagMapper.toDto(tag);
    }

    @Override
    @Transactional
    public TagDto deleteTag(Long tagId) throws TagNotFoundException {
        log.info("Функция по удалению тега вызвана в сервисе");
        Optional<Tag> response = tagsRepository.findById(tagId);
        if (response.isEmpty()) {
            throw new TagNotFoundException();
        }
        Tag tag = response.get();
        tagsRepository.deleteById(tagId);
        return tagMapper.toDto(tag);
    }
}
