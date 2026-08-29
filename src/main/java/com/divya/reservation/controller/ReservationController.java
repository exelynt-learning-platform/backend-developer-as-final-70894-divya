package com.divya.reservation.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.divya.reservation.entity.Reservation;
import com.divya.reservation.service.ReservationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    // USER + ADMIN - CREATE
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Reservation> create(
            @Valid @RequestBody Reservation reservation,
            Authentication authentication) {

        return ResponseEntity.ok(
                service.create(reservation, authentication.getName())
        );
    }

    // ADMIN - GET ALL
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Reservation>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // USER - OWN RESERVATIONS
    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Reservation>> getMyReservations(
            Authentication authentication) {

        return ResponseEntity.ok(
                service.getMyReservations(authentication.getName())
        );
    }

    // ADMIN - GET BY ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Reservation> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getById(id));
    }

    // ADMIN - UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Reservation> update(
            @PathVariable Long id,
            @Valid @RequestBody Reservation reservation) {

        return ResponseEntity.ok(
                service.update(id, reservation)
        );
    }

    // ADMIN - DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ADMIN - SEARCH
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Reservation>> search(
            @RequestParam String name) {

        return ResponseEntity.ok(
                service.searchByCustomerName(name)
        );
    }

    // ADMIN - PAGINATION + SORTING
    @GetMapping("/page")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Reservation>> getReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(
                service.getReservations(
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

    // ADMIN - FILTER STATUS
    @GetMapping("/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Reservation>> getByStatus(
            @RequestParam String status) {

        return ResponseEntity.ok(
                service.getByStatus(status)
        );
    }

    // ADMIN - MIN PRICE
    @GetMapping("/min-price")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Reservation>> getByMinPrice(
            @RequestParam BigDecimal price) {

        return ResponseEntity.ok(
                service.getByMinPrice(price)
        );
    }

    // ADMIN - MAX PRICE
    @GetMapping("/max-price")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Reservation>> getByMaxPrice(
            @RequestParam BigDecimal price) {

        return ResponseEntity.ok(
                service.getByMaxPrice(price)
        );
    }

    // ADMIN - PRICE RANGE
    @GetMapping("/price-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Reservation>> getByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {

        return ResponseEntity.ok(
                service.getByPriceRange(minPrice, maxPrice)
        );
    }
}