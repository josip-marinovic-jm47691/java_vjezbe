package com.josip.vjezbe.controllers;

import com.josip.vjezbe.entities.Book;
import com.josip.vjezbe.entities.Member;
import com.josip.vjezbe.entities.Reservation;
import com.josip.vjezbe.services.BookService;
import com.josip.vjezbe.services.MemberService;
import com.josip.vjezbe.services.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final BookService bookService;
    private final MemberService memberService;

    public ReservationController(ReservationService reservationService, BookService bookService, MemberService memberService) {
        this.reservationService = reservationService;
        this.bookService = bookService;
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Map<String, Long> payload) {
        Long bookId = payload.get("bookId");
        Long memberId = payload.get("memberId");

        Book book = bookService.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + bookId));
        Member member = memberService.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));

        Reservation newReservation = reservationService.createReservation(member, book);
        return ResponseEntity.ok(newReservation);
    }

    @PutMapping("/{id}/fulfill")
    public ResponseEntity<Reservation> fulfillReservation(@PathVariable Long id, @RequestParam Long memberId) {
        Reservation fulfilledReservation = reservationService.fulfillReservation(id, memberId);
        return ResponseEntity.ok(fulfilledReservation);
    }

}