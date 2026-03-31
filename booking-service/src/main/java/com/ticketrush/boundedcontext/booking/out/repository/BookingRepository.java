package com.ticketrush.boundedcontext.booking.out.repository;

import com.ticketrush.boundedcontext.booking.domain.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {}
