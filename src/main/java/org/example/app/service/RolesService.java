package org.example.app.service;

import org.example.app.entity.Role;
import org.example.app.exception.DatabaseException;
import org.example.app.exception.RoleNotFoundException;

public interface RolesService {

    Role getRole(String roleId) throws RoleNotFoundException, DatabaseException;

    void putRole(String roleId, Role newRole) throws RoleNotFoundException;

    Role deleteRole(String roleId) throws RoleNotFoundException;
}
