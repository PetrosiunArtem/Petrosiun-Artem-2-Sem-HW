package org.example.app.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.app.entity.Role;
import org.example.app.exception.RoleNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
@Slf4j
public class RolesRepositoryImpl implements RolesRepository {
    private final HashMap<String, Role> roles = new HashMap<>();

    @Override
    public Role getRole(String roleId) throws RoleNotFoundException {
        log.info("Функция по взятию роли вызвана в репозитории");

        if (!roles.containsKey(roleId)) {
            throw new RoleNotFoundException("No such role exists");
        }
        return roles.get(roleId);
    }

    @Override
    public void putRole(String roleId, Role newRole) throws RoleNotFoundException {
        log.info("Функция по замене роли вызвана в репозитории");

        if (!roles.containsKey(roleId)) {
            throw new RoleNotFoundException("No such role exists");
        }
        roles.put(roleId, newRole);
    }

    @Override
    public Role deleteRole(String roleId) throws RoleNotFoundException {
        log.info("Функция по удалению роли вызвана в репозитории");

        if (!roles.containsKey(roleId)) {
            throw new RoleNotFoundException("No such role exists");
        }
        return roles.remove(roleId);
    }
}
