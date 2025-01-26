package com.josip.vjezbe.repositories;

import com.josip.vjezbe.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByBookIdAndStatusOrderByReservationDateAsc(Long bookId, String status);

}