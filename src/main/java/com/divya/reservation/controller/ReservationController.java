package com.divya.reservation.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.divya.reservation.entity.Reservation;
import com.divya.reservation.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public Reservation create(@RequestBody Reservation reservation) {
        return service.create(reservation);
    }

    // GET ALL
    @GetMapping
    public List<Reservation> getAll() {
        return service.getAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Reservation getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Reservation update(
            @PathVariable Long id,
            @RequestBody Reservation reservation) {

        return service.update(id, reservation);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {

        service.delete(id);

        return "Reservation deleted successfully";
    }

    // SEARCH BY CUSTOMER NAME
    @GetMapping("/search")
    public List<Reservation> search(
            @RequestParam String name) {

        return service.searchByCustomerName(name);
    }

    // PAGINATION + SORTING
    @GetMapping("/page")
    public Page<Reservation> getReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return service.getReservations(
                page,
                size,
                sortBy,
                direction);
    }

    // FILTER BY PRICE
    @GetMapping("/price")
    public List<Reservation> getByPrice(
            @RequestParam BigDecimal price) {

        return service.getByPrice(price);
    }
}