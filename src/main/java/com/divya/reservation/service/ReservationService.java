package com.divya.reservation.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.divya.reservation.entity.Reservation;
import com.divya.reservation.entity.User;
import com.divya.reservation.repository.ReservationRepository;
import com.divya.reservation.repository.UserRepository;

@Service
public class ReservationService {

    private final ReservationRepository repository;
    private final UserRepository userRepository;

    public ReservationService(
            ReservationRepository repository,
            UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    // CREATE - USER taken from JWT username
    public Reservation create(Reservation reservation, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        reservation.setUser(user);

        if (reservation.getStatus() == null ||
                reservation.getStatus().isBlank()) {
            reservation.setStatus("PENDING");
        }

        return repository.save(reservation);
    }

    // ADMIN - GET ALL
    public List<Reservation> getAll() {
        return repository.findAll();
    }

    // USER - OWN RESERVATIONS
    public List<Reservation> getMyReservations(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return repository.findByUser(user);
    }

    // GET BY ID
    public Reservation getById(Long id) {
        return repository.findById(id)
                .orElseThrow(
                    () -> new RuntimeException("Reservation not found")
                );
    }

    // UPDATE
    public Reservation update(Long id, Reservation reservation) {

        Reservation existing = getById(id);

        existing.setCustomerName(reservation.getCustomerName());
        existing.setReservationDate(reservation.getReservationDate());
        existing.setPrice(reservation.getPrice());

        if (reservation.getStatus() != null) {
            existing.setStatus(reservation.getStatus());
        }

        return repository.save(existing);
    }

    // DELETE
    public void delete(Long id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("Reservation not found");
        }

        repository.deleteById(id);
    }

    // SEARCH
    public List<Reservation> searchByCustomerName(String name) {
        return repository.findByCustomerNameContainingIgnoreCase(name);
    }

    // PAGINATION + SORTING
    public Page<Reservation> getReservations(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return repository.findAll(pageable);
    }

    // FILTER BY STATUS
    public List<Reservation> getByStatus(String status) {
        return repository.findByStatus(status);
    }

    // FILTER BY MIN PRICE
    public List<Reservation> getByMinPrice(BigDecimal price) {
        return repository.findByPriceGreaterThanEqual(price);
    }

    // FILTER BY MAX PRICE
    public List<Reservation> getByMaxPrice(BigDecimal price) {
        return repository.findByPriceLessThanEqual(price);
    }

    // FILTER BY MIN + MAX PRICE
    public List<Reservation> getByPriceRange(
            BigDecimal minPrice,
            BigDecimal maxPrice) {

        return repository.findByPriceBetween(minPrice, maxPrice);
    }
}