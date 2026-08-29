package com.divya.reservation.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.divya.reservation.entity.Reservation;
import com.divya.reservation.entity.User;

public interface ReservationRepository
        extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUser(User user);

    List<Reservation> findByStatus(String status);

    List<Reservation> findByPriceGreaterThanEqual(BigDecimal price);

    List<Reservation> findByPriceLessThanEqual(BigDecimal price);

    List<Reservation> findByPriceBetween(
            BigDecimal minPrice,
            BigDecimal maxPrice);

    List<Reservation> findByCustomerNameContainingIgnoreCase(
            String name);
}