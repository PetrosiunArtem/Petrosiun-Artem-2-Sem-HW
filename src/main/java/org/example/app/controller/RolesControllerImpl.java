package org.example.app.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.example.app.entity.Role;
import org.example.app.exception.DatabaseException;
import org.example.app.exception.RoleNotFoundException;
import org.example.app.service.RolesServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@CircuitBreaker(name = "CircuitBreakerAPI")
public class RolesControllerImpl implements RolesController {
    private final RolesServiceImpl rolesService;

    public RolesControllerImpl(RolesServiceImpl rolesService) {
        this.rolesService = rolesService;
    }

    @GetMapping("/roles/get/{roleId}")
    @Override
    public ResponseEntity<Role> getRole(@PathVariable String roleId) throws RoleNotFoundException, DatabaseException {
        Role role = rolesService.getRole(roleId);
        return ResponseEntity.ok(role);
    }

    @DeleteMapping("/roles/delete/{roleId}")
    @Override
    public ResponseEntity<Role> deleteRole(@PathVariable String roleId) throws RoleNotFoundException {
        Role role = rolesService.deleteRole(roleId);
        return ResponseEntity.ok(role);
    }

    @PutMapping("/roles/put/{roleId}")
    @Override
    public ResponseEntity<Role> putRole(@PathVariable String roleId, Role newRole) throws RoleNotFoundException {
        rolesService.putRole(roleId, newRole);
        return ResponseEntity.ok(newRole);
    }
}
