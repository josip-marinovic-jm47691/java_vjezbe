package com.josip.vjezbe;

import com.josip.vjezbe.entities.Book;
import com.josip.vjezbe.entities.Member;
import com.josip.vjezbe.entities.Reservation;
import com.josip.vjezbe.repositories.BookRepository;
import com.josip.vjezbe.repositories.MemberRepository;
import com.josip.vjezbe.repositories.ReservationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedDatabase(MemberRepository memberRepository, BookRepository bookRepository, ReservationRepository reservationRepository) {
        return args -> {

            Member member1 = new Member();
            member1.setName("Josip Marinovic");
            Member member2 = new Member();
            member2.setName("Pisoj Civoniram");
            Member member3 = new Member();
            member3.setName("Jo Ma");
            Member member4 = new Member();
            member4.setName("Oj Am");
            memberRepository.saveAll(Arrays.asList(member1, member2, member3, member4));

            Book book1 = new Book();
            book1.setTitle("Prva knjiga");
            book1.setAuthor("Prvi Autor");
            book1.setGenre("Prvi zanr");
            book1.setPublishedYear(2001);

            Book book2 = new Book();
            book2.setTitle("Druga knjiga");
            book2.setAuthor("Drugi Autor");
            book2.setGenre("Drugi zanr");
            book2.setPublishedYear(2002);

            Book book3 = new Book();
            book3.setTitle("Treca knjiga");
            book3.setAuthor("Treci Autor");
            book3.setGenre("Treci zanr");
            book3.setPublishedYear(2003);

            Book book4 = new Book();
            book4.setTitle("Cetvrta knjiga");
            book4.setAuthor("Cetvrti Autor");
            book4.setGenre("Cetvrti zanr");
            book4.setPublishedYear(2004);
            bookRepository.saveAll(Arrays.asList(book1, book2, book3, book4));

            Reservation reservation1 = new Reservation();
            reservation1.setBook(book1);
            reservation1.setMember(member1);
            reservation1.setStatus("PENDING");
            reservation1.setReservationDate(LocalDateTime.now());

            Reservation reservation2 = new Reservation();
            reservation2.setBook(book2);
            reservation2.setMember(member2);
            reservation2.setStatus("PENDING");
            reservation2.setReservationDate(LocalDateTime.now());

            Reservation reservation3 = new Reservation();
            reservation3.setBook(book3);
            reservation3.setMember(member3);
            reservation3.setStatus("PENDING");
            reservation3.setReservationDate(LocalDateTime.now());

            Reservation reservation4 = new Reservation();
            reservation4.setBook(book4);
            reservation4.setMember(member4);
            reservation4.setStatus("PENDING");
            reservation4.setReservationDate(LocalDateTime.now());
            reservationRepository.saveAll(Arrays.asList(reservation1, reservation2, reservation3, reservation4));
        };
    }
}
