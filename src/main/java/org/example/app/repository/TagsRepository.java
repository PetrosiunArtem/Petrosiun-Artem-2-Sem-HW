package org.example.app.repository;

import org.example.app.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional(readOnly = true)
public interface TagsRepository extends JpaRepository<Tag, Long> {
}
