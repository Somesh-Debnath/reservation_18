package com.nk.reservation.services;

import java.util.List;
import java.util.Optional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.nk.reservation.entity.Reservation;
import com.nk.reservation.entity.ReservationTypes;
import com.nk.reservation.repository.ReservationRepository;

@Service
public class ReservationServices implements IReservationServices{

    Logger logger=LoggerFactory.getLogger(ReservationServices.class);

    @Autowired
    private ReservationRepository reservationRepository;

    ReservationServices(ReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }


    @Override
    public void addNewReservation(Reservation reservation){
        if(checkValidReservationAmount(reservation)&&checkValidReservationDate(reservation)&&checkValidReservationTypes(reservation)){
            reservationRepository.save(reservation);
            logger.info("done adding");
        }else{
            throw new IllegalArgumentException("Could not add reservation");
        }
    }
    
    @Override
    public List<Reservation> getReservationsByTravelRequestId(int travelRequestId) {
        return reservationRepository.findByTravelRequestId(travelRequestId);
    }
    
    @Override
    public Optional<Reservation> getReservationById(int id) {
        return reservationRepository.findById(id);
    }
    
    @Override
    public ByteArrayInputStream downloadReservationsDoc(int id) {

        Reservation reservation = getReservationById(id).get();

        ByteArrayOutputStream out=new ByteArrayOutputStream();

        Document document = new Document();

        PdfWriter.getInstance(document, out);
        
        document.open();
        
        // Create a font for the chunks
            Font font = FontFactory.getFont(FontFactory.COURIER, 12);

            // Create a paragraph for each field value
            Paragraph idParagraph = new Paragraph("id: " + reservation.getId(), font);
            Paragraph travelRequestIdParagraph = new Paragraph("travelRequestId: " + reservation.getTravelRequestId(), font);
            Paragraph reservationDoneByEmployeeIdParagraph = new Paragraph("reservationDoneByEmployeeId: " + reservation.getReservationDoneByEmployeeId(), font);
            Paragraph createdOnParagraph = new Paragraph("createdOn: " + reservation.getCreatedOn(), font);
            Paragraph reservationDoneWithEntityParagraph = new Paragraph("reservationDoneWithEntity: " + reservation.getReservationDoneWithEntity(), font);
            Paragraph reservationDateParagraph = new Paragraph("reservationDate: " + reservation.getReservationDate(), font);
            Paragraph amountParagraph = new Paragraph("amount: " + reservation.getAmount(), font);
            Paragraph confirmationIDParagraph = new Paragraph("confirmationID: " + reservation.getConfirmationID(), font);
            Paragraph remarksParagraph = new Paragraph("remarks: " + reservation.getRemarks(), font);
            Paragraph reservationTypeIdParagraph = new Paragraph(""+reservation.getReservationTypeId(), font);
            Paragraph reservationDocsParagraph = new Paragraph("reservationDocs: " + reservation.getReservationDocs(), font);

            // Add each paragraph to the document
            document.add(idParagraph);
            document.add(travelRequestIdParagraph);
            document.add(reservationDoneByEmployeeIdParagraph);
            document.add(createdOnParagraph);
            document.add(reservationDoneWithEntityParagraph);
            document.add(reservationDateParagraph);
            document.add(amountParagraph);
            document.add(confirmationIDParagraph);
            document.add(remarksParagraph);
            document.add(reservationTypeIdParagraph);
            document.add(reservationDocsParagraph);
        document.close();
        
        return new ByteArrayInputStream(out.toByteArray());
        
    }
    





    //Not Required
    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }


    public boolean checkValidReservationDate(Reservation reservation){

        //sample date, have to fetch from travel planner table
        LocalDate TravelStartdate = LocalDate.now().plusDays(1);
        LocalDate resvDate = reservation.getReservationDate();
        ReservationTypes reservationType = reservation.getReservationTypeId();

        // Rule a: ReservationDate for a train/bus reservation must be 1 day before the fromdate mentioned in Travel Planner
        if (reservationType.getId() == 1 || reservationType.getId() == 2) {
            if (!TravelStartdate.isEqual(resvDate.plusDays(1))) {
                throw new IllegalArgumentException("Train/Bus reservation must be 1 day before the travel plan start date");
            }
        }
        // Rule b: ReservationDate for hotel must be same as the from date in travel plan
        if (reservationType.getId() == 3) {
            if (!TravelStartdate.isEqual(resvDate)) {
                throw new IllegalArgumentException("Hotel reservation date must be same as the travel plan start date");
            }
        }

        return true;
    }

    public boolean checkValidReservationTypes(Reservation reservation){

        // Rule c: There must be exactly 3 reservations per travel - one for flight/bus/train, one for hotel, and one for cab travel to hotel from flight/bus/train
        List<Reservation> reservations = reservationRepository.findByTravelRequestId(reservation.getTravelRequestId());
        int reservationCount = reservations.size();

        if(reservationCount >=3){
            throw new IllegalArgumentException("must be exactly 3 reservations per travelRequesat");
        }else if(reservationCount == 0){
            return true;
        }

        reservations.add(reservation);
        int flightCount = 0;
        int hotelCount = 0;
        int cabCount = 0;
        for (Reservation rsv : reservations) {
            if (rsv.getReservationTypeId().getId() == 1 || rsv.getReservationTypeId().getId() == 2 || rsv.getReservationTypeId().getId() == 3) {
                flightCount++;
            }
            if (rsv.getReservationTypeId().getId() == 4) {
                hotelCount++;
            }
            if (rsv.getReservationTypeId().getId() == 5) {
                cabCount++;
            }
        }

        if (flightCount > 1 || hotelCount > 1 || cabCount > 1) {
            throw new IllegalArgumentException("must be exactly 3 reservations of diffrent type per travelRequesat");
        }

        return true;
    }

    // d.	The amount of all 3 reservations must not exceed the 70% of the allocated budget
    public boolean checkValidReservationAmount(Reservation reservation){

        if(reservation.getAmount()<0){
            return false;
        }

        //sample budget, have to fetch from travel planner table
        double travelBudget = 1000;

        // Rule c: There must be exactly 3 reservations per travel - one for flight/bus/train, one for hotel, and one for cab travel to hotel from flight/bus/train
        List<Reservation> reservations = reservationRepository.findByTravelRequestId(reservation.getTravelRequestId());

        reservations.add(reservation);
        double totalAmount = 0;
        for (Reservation rsv : reservations) {
            totalAmount += rsv.getAmount();
        }

        if (totalAmount > 0.7 * travelBudget) {
            throw new IllegalArgumentException("The amount of all 3 reservations must not exceed the 70% of the allocated budget");
        }

        return true;
    }
}









