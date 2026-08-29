package com.divya.reservation.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.divya.reservation.entity.Reservation;
import com.divya.reservation.repository.ReservationRepository;

@Service
public class ReservationService {

    private final ReservationRepository repository;

    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
    }

    // CREATE
    public Reservation create(Reservation reservation) {
        return repository.save(reservation);
    }

    // GET ALL
    public List<Reservation> getAll() {
        return repository.findAll();
    }

    // GET BY ID
    public Reservation getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    // UPDATE
    public Reservation update(Long id, Reservation reservation) {

        Reservation existing = getById(id);

        existing.setCustomerName(reservation.getCustomerName());
        existing.setReservationDate(reservation.getReservationDate());
        existing.setPrice(reservation.getPrice());
        existing.setStatus(reservation.getStatus());

        return repository.save(existing);
    }

    // DELETE
    public void delete(Long id) {
        repository.deleteById(id);
    }

    // SEARCH BY CUSTOMER NAME
    public List<Reservation> searchByCustomerName(String name) {
        return repository.findByCustomerNameContainingIgnoreCase(name);
    }

    // PAGINATION + SORTING
    public Page<Reservation> getReservations(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort;

        if (direction.equalsIgnoreCase("desc")) {
            sort = Sort.by(sortBy).descending();
        } else {
            sort = Sort.by(sortBy).ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        return repository.findAll(pageable);
    }

    // FILTER BY PRICE
    public List<Reservation> getByPrice(BigDecimal price) {
        return repository.findByPrice(price);
    }
}