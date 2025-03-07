package org.example.app.service;

import lombok.extern.slf4j.Slf4j;
import org.example.app.entity.Role;
import org.example.app.exception.DatabaseException;
import org.example.app.exception.RoleNotFoundException;
import org.example.app.repository.RolesRepositoryImpl;
import org.springframework.retry.annotation.Backoff;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.retry.annotation.Retryable;

@Service
@Slf4j
public class RolesServiceImpl implements RolesService {
    private final RolesRepositoryImpl rolesRepository;

    public RolesServiceImpl(RolesRepositoryImpl rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    // At Least Once
    @Retryable(retryFor = DatabaseException.class, maxAttempts = 5, backoff = @Backoff(value = 10000))
    @Override
    public Role getRole(String roleId) throws RoleNotFoundException, DatabaseException {
        if (Math.random() > 0.80) {
            throw new DatabaseException("Temporary failure");
        }
        log.info("Функция по взятию роли вызвана в сервисе");
        return rolesRepository.getRole(roleId);
    }

    @Async
    @Override
    public void putRole(String roleId, Role newRole) throws RoleNotFoundException {
        log.info("Функция по замене роли вызвана в сервисе");
        rolesRepository.putRole(roleId, newRole);
    }

    @Override
    public Role deleteRole(String roleId) throws RoleNotFoundException {
        log.info("Функция по удалению роли вызвана в сервисе");
        return rolesRepository.deleteRole(roleId);
    }
}
