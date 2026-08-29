package com.divya.reservation.repository;

import com.divya.reservation.entity.Reservation;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByCustomerNameContainingIgnoreCase(String name);

    List<Reservation> findByPrice(BigDecimal price);
}