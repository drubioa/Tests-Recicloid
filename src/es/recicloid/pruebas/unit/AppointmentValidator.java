package es.recicloid.pruebas.unit;

import java.util.List;

import org.joda.time.LocalDate;

import es.recicloid.models.ProvisionalAppointment;
import android.test.AndroidTestCase;

public abstract class AppointmentValidator  extends AndroidTestCase{
	
	/**
	 * Validate if the appointment is correct, and all these fields are in correct format.
	 * @param appointment
	 */
	public static void validAppointment(ProvisionalAppointment appointment){
		assertNotNull(appointment);
		assertNotNull(appointment.getTelephone());
		assertTrue(appointment.getTelephone().charAt(0) == '6');
		assertTrue(appointment.getNumFurnitures() > 0);
		assertNotNull(appointment.getFch_collection());
		LocalDate collection_date = appointment.getFch_collection();
		assertNotNull(appointment.getFch_request());
		LocalDate request_date = appointment.getFch_request();
		assertTrue(collection_date.isAfter(request_date));
		assertNotNull(appointment.getCollectionPointId());
	}
	
	public static void validAppointment(List<ProvisionalAppointment> appointments){
		for(ProvisionalAppointment appointment : appointments){
			AppointmentValidator.validAppointment(appointment);
		}
	}
}
