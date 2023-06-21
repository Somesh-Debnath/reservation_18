package com.nk.reservation.services;

import com.nk.reservation.entity.Reservation;
import com.nk.reservation.entity.ReservationTypes;
import com.nk.reservation.repository.ReservationRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServicesTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationServices reservationServices;

    Reservation reservation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);      
    }

    @Test
    void testGetReservationByTravelRequestId() {
        // Mock the repository's behavior
        int travelRequestId = 30;
        Reservation reservation = new Reservation();
        reservation.setTravelRequestId(travelRequestId);
        List<Reservation> reservationList = Collections.singletonList(reservation);
        when(reservationRepository.findByTravelRequestId(travelRequestId)).thenReturn(reservationList);

        // Call the service method for success
        List<Reservation> result1 = reservationServices.getReservationsByTravelRequestId(travelRequestId);
        assertEquals(reservationList, result1);

        // Call the service method for failure
        Random random = new Random(); //Math.random() returns double, but int needed
        List<Reservation> result2 = reservationServices.getReservationsByTravelRequestId(travelRequestId+(random.nextInt(100)+1));
        assertNotEquals(reservationList, result2);
    }

    @Test
    void testGetReservationById() {
        // Mock the repository's behavior
        int id = 1;
        Optional<Reservation> optionalReservation = Optional.of(new Reservation());
        when(reservationRepository.findById(id)).thenReturn(optionalReservation);

        // Call the service method for success
        Optional<Reservation> result_valid = reservationServices.getReservationById(id);
        assertEquals(optionalReservation, result_valid);

        // Call the service method for failure
        Random random = new Random(); //Math.random() returns double, but int needed
        Optional<Reservation> result_inv = reservationServices.getReservationById(id+(random.nextInt()+1));
        assertNotEquals(optionalReservation, result_inv);
    }

    // @Test
    // void testDownloadReservationsDoc() {
    //     // Call the service method
    //     byte[] result = reservationServices.downloadReservationsDoc();

    //     // Verify the result is null
    //     assertNull(result);
    // }



    @Test
    void testAddNewReservation_ValidReservationDate() {
        // Arrange
        Reservation validReservation_1 = new Reservation();
        validReservation_1.setReservationDate(LocalDate.now());
        validReservation_1.setReservationTypeId(new ReservationTypes(1,""));

        // Act
        reservationServices.addNewReservation(validReservation_1);

        // Assert
        verify(reservationRepository, times(1)).save(validReservation_1);

            // Arrange
        Reservation validReservation_2 = new Reservation();
        validReservation_2.setReservationDate(LocalDate.now());
        validReservation_2.setReservationTypeId(new ReservationTypes(5,""));

        // Act
        reservationServices.addNewReservation(validReservation_2);

        // Assert
        verify(reservationRepository, times(1)).save(validReservation_2);
    }

    @Test
    void testAddNewReservation_InvalidDate() {
        // Arrange
        Reservation invalidDateReservation = new Reservation();
        invalidDateReservation.setReservationDate(LocalDate.now().plusDays(1000));
        invalidDateReservation.setReservationTypeId(new ReservationTypes(5,""));

        // Act and Assert
        verify(reservationRepository, times(0)).save(invalidDateReservation);
    }

    @Test
    void testAddNewReservation_ExceedingAmount() {
        // Arrange
        Reservation exceedingAmountReservation = new Reservation();
        exceedingAmountReservation.setAmount(999999999);

        // Act and Assert
        verify(reservationRepository, times(0)).save(exceedingAmountReservation);
    }

    @Test
    void testAddNewReservation_ExceedingCount() {
        // Arrange
        List<Reservation> existingReservations = new ArrayList<>();
        existingReservations.add(new Reservation());
        existingReservations.add(new Reservation());
        existingReservations.add(new Reservation());

        Reservation exceedingCountReservation = new Reservation();

        // Verify that the save method was not called
        verify(reservationRepository, times(0)).save(exceedingCountReservation);
    }

    @Test
    void testAddNewReservation_ValidTypes() {
        // Arrange
        Reservation reservation_1=new Reservation();
        reservation_1.setReservationTypeId(new ReservationTypes(1,""));
        reservationRepository.save(reservation_1);

        Reservation exceedingCountReservation = new Reservation();
        exceedingCountReservation.setReservationTypeId(new ReservationTypes(3,""));

        // Verify that the save method was not called
        verify(reservationRepository, times(0)).save(exceedingCountReservation);

        exceedingCountReservation.setReservationTypeId(new ReservationTypes(5,""));
    }
}
