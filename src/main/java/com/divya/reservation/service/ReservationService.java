package com.divya.reservation.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.divya.reservation.entity.Reservation;
import com.divya.reservation.entity.Resource;
import com.divya.reservation.entity.User;
import com.divya.reservation.repository.ReservationRepository;
import com.divya.reservation.repository.ResourceRepository;
import com.divya.reservation.repository.UserRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            UserRepository userRepository,
            ResourceRepository resourceRepository) {

        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.resourceRepository = resourceRepository;
    }

    // USER/ADMIN CREATE
    public Reservation create(
            Reservation reservation,
            String username,
            Long resourceId) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(
                    () -> new RuntimeException("User not found")
                );

        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(
                    () -> new RuntimeException("Resource not found")
                );

        reservation.setUser(user);
        reservation.setResource(resource);

        if (reservation.getStatus() == null ||
                reservation.getStatus().isBlank()) {

            reservation.setStatus("PENDING");
        }

        validateStatus(reservation.getStatus());

        return reservationRepository.save(reservation);
    }

    // ADMIN - ALL
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    // USER - OWN
    public List<Reservation> getMyReservations(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(
                    () -> new RuntimeException("User not found")
                );

        return reservationRepository.findByUser(user);
    }

    public Reservation getById(Long id) {

        return reservationRepository.findById(id)
                .orElseThrow(
                    () -> new RuntimeException("Reservation not found")
                );
    }

    // ADMIN UPDATE
    public Reservation update(
            Long id,
            Reservation reservation,
            Long resourceId) {

        Reservation existing = getById(id);

        existing.setCustomerName(
                reservation.getCustomerName()
        );

        existing.setReservationDate(
                reservation.getReservationDate()
        );

        existing.setPrice(
                reservation.getPrice()
        );

        if (reservation.getStatus() != null) {
            validateStatus(reservation.getStatus());
            existing.setStatus(reservation.getStatus());
        }

        if (resourceId != null) {

            Resource resource =
                    resourceRepository.findById(resourceId)
                    .orElseThrow(
                        () -> new RuntimeException(
                            "Resource not found"
                        )
                    );

            existing.setResource(resource);
        }

        return reservationRepository.save(existing);
    }

    public void delete(Long id) {

        if (!reservationRepository.existsById(id)) {
            throw new RuntimeException("Reservation not found");
        }

        reservationRepository.deleteById(id);
    }

    public List<Reservation> searchByCustomerName(String name) {

        return reservationRepository
                .findByCustomerNameContainingIgnoreCase(name);
    }

    public Page<Reservation> getReservations(
            int page,
            int size,
            String sortBy,
            String direction) {

        if (page < 0) {
            page = 0;
        }

        if (size < 1) {
            size = 5;
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        return reservationRepository.findAll(pageable);
    }

    public List<Reservation> getByStatus(String status) {

        validateStatus(status);

        return reservationRepository.findByStatus(status);
    }

    public List<Reservation> getByMinPrice(BigDecimal price) {

        return reservationRepository
                .findByPriceGreaterThanEqual(price);
    }

    public List<Reservation> getByMaxPrice(BigDecimal price) {

        return reservationRepository
                .findByPriceLessThanEqual(price);
    }

    public List<Reservation> getByPriceRange(
            BigDecimal minPrice,
            BigDecimal maxPrice) {

        if (minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException(
                    "Minimum price cannot exceed maximum price"
            );
        }

        return reservationRepository
                .findByPriceBetween(minPrice, maxPrice);
    }

    private void validateStatus(String status) {

        if (!status.equals("PENDING") &&
            !status.equals("CONFIRMED") &&
            !status.equals("CANCELLED")) {

            throw new IllegalArgumentException(
                    "Status must be PENDING, CONFIRMED or CANCELLED"
            );
        }
    }
}