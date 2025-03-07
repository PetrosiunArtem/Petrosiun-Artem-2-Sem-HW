package org.example.app.repository;

import org.example.app.entity.Role;
import org.example.app.exception.RoleNotFoundException;

public interface RolesRepository {

    Role getRole(String roleId) throws RoleNotFoundException;

    void putRole(String roleId, Role newRole) throws RoleNotFoundException;

    Role deleteRole(String roleId) throws RoleNotFoundException;
}
