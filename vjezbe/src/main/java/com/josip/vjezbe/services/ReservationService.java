package com.josip.vjezbe.services;

import com.josip.vjezbe.entities.Book;
import com.josip.vjezbe.entities.Member;
import com.josip.vjezbe.entities.Notification;
import com.josip.vjezbe.entities.Reservation;
import com.josip.vjezbe.repositories.NotificationRepository;
import com.josip.vjezbe.repositories.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final NotificationRepository notificationRepository;

    public ReservationService(ReservationRepository reservationRepository, NotificationRepository notificationRepository) {
        this.reservationRepository = reservationRepository;
        this.notificationRepository = notificationRepository;
    }

    public Reservation createReservation(Member member, Book book) {
        Reservation reservation = new Reservation();
        reservation.setMember(member);
        reservation.setBook(book);
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setStatus("PENDING");
        return reservationRepository.save(reservation);
    }

    public Reservation fulfillReservation(Long reservationId, Long memberId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        List<Reservation> pendingReservations = reservationRepository.findAllByBookIdAndStatusOrderByReservationDateAsc(
                reservation.getBook().getId(), "PENDING");

        if (pendingReservations.isEmpty()) {
            throw new IllegalStateException("No pending reservations found for this book.");
        }

        Reservation firstPendingReservation = pendingReservations.get(0);
        if (!firstPendingReservation.getId().equals(reservationId)) {
            throw new IllegalArgumentException("You cannot fulfill a reservation that is not the next in line.");
        }

        if (!firstPendingReservation.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("Only the member who made the reservation can fulfill it.");
        }

        reservation.setStatus("FULFILLED");
        reservationRepository.save(reservation);

        Optional<Reservation> nextReservation = pendingReservations.size() > 1 ? Optional.of(pendingReservations.get(1)) : Optional.empty();

        nextReservation.ifPresent(pendingReservation -> {
            Notification notification = new Notification();
            notification.setMember(pendingReservation.getMember());
            notification.setReservation(pendingReservation);
            notification.setMessage("Book '" + reservation.getBook().getTitle() + "' is now available to be borrowed");
            notification.setTimestamp(LocalDateTime.now());
            notificationRepository.save(notification);
        });

        return reservation;
    }
}