package com.divya.reservation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.divya.reservation.entity.Resource;
import com.divya.reservation.repository.ResourceRepository;

@Service
public class ResourceService {

    private final ResourceRepository repository;

    public ResourceService(ResourceRepository repository) {
        this.repository = repository;
    }

    // CREATE
    public Resource create(Resource resource) {
        return repository.save(resource);
    }

    // GET ALL
    public List<Resource> getAll() {
        return repository.findAll();
    }

    // GET BY ID
    public Resource getById(Long id) {
        return repository.findById(id)
                .orElseThrow(
                    () -> new RuntimeException("Resource not found")
                );
    }

    // UPDATE
    public Resource update(Long id, Resource resource) {

        Resource existing = getById(id);

        existing.setName(resource.getName());
        existing.setType(resource.getType());

        return repository.save(existing);
    }

    // DELETE
    public void delete(Long id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("Resource not found");
        }

        repository.deleteById(id);
    }
}