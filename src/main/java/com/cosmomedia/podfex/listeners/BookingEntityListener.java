//package com.cosmomedia.location.listeners;
//
//
//import jakarta.persistence.PrePersist;
//import jakarta.persistence.PreUpdate;
//
//import java.util.Date;
//
//public class BookingEntityListener {
//
//    @PrePersist
//    @PreUpdate
//    public void calculateDuration(Booking booking) {
//        Date pickUpDateTime = booking.getPickUpDateTime();
//        Date dropOffDateTime = booking.getDropOffDateTime();
//
//        if (pickUpDateTime != null && dropOffDateTime != null) {
//            long diffInMilliseconds = Math.abs(dropOffDateTime.getTime() - pickUpDateTime.getTime());
//            long diffInMinutes = diffInMilliseconds / (60 * 1000);
//            booking.setDuration((double) diffInMinutes);
//        }
//    }
//}
