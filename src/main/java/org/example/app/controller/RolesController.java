package org.example.app.controller;

import org.example.app.entity.Role;
import org.example.app.exception.DatabaseException;
import org.example.app.exception.RoleNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface RolesController {

    ResponseEntity<Role> getRole(@PathVariable String roleId) throws RoleNotFoundException, DatabaseException;

    ResponseEntity<Role> deleteRole(@PathVariable String roleId) throws RoleNotFoundException;

    ResponseEntity<Role> putRole(@PathVariable String roleId, Role newRole) throws RoleNotFoundException;
}
