package org.example.app.repository;

import org.example.app.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface FilesRepository extends JpaRepository<File, Long> {
    @Query("SELECT id FROM File")
    List<Long> findAllId();
}
