package com.divya.reservation.controller;

import com.divya.reservation.entity.Resource;
import com.divya.reservation.service.ResourceService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceService service;

    public ResourceController(ResourceService service) {
        this.service = service;
    }

    // ADMIN - CREATE
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Resource> create(
            @Valid @RequestBody Resource resource) {

        return ResponseEntity.ok(service.create(resource));
    }

    // ADMIN + USER - READ ALL
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<Resource>> getAll() {

        return ResponseEntity.ok(service.getAll());
    }

    // ADMIN + USER - READ BY ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Resource> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getById(id));
    }

    // ADMIN - UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Resource> update(
            @PathVariable Long id,
            @Valid @RequestBody Resource resource) {

        return ResponseEntity.ok(service.update(id, resource));
    }

    // ADMIN - DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}