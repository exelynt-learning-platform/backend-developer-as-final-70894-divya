package com.divya.reservation.controller;

import com.divya.reservation.entity.Resource;
import com.divya.reservation.service.ResourceService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceService service;

    public ResourceController(ResourceService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public Resource create(@RequestBody Resource resource) {
        return service.create(resource);
    }

    // GET ALL
    @GetMapping
    public List<Resource> getAll() {
        return service.getAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Resource getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Resource update(
            @PathVariable Long id,
            @RequestBody Resource resource) {

        return service.update(id, resource);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}