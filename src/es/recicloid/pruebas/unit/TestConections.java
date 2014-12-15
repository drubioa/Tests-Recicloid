package es.recicloid.pruebas.unit;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpException;
import org.joda.time.LocalDate;

import android.location.Location;
import android.test.AndroidTestCase;
import es.recicloid.models.CollectionPoint;
import es.recicloid.models.CollectionRequest;
import es.recicloid.models.Furniture;
import es.recicloid.models.ProvisionalAppointment;
import es.recicloid.models.User;
import es.recicloid.utils.conections.ConectorToCollectionPointService;
import es.recicloid.utils.conections.ConectorToCollectionPointServiceImp;
import es.recicloid.utils.conections.ConectorToDailyAppointmentService;
import es.recicloid.utils.conections.ConectorToDailyAppointmentServiceImp;
import es.recicloid.utils.conections.ConectorToServices;
import es.recicloid.utils.conections.ConectorToUserService;
import es.recicloid.utils.conections.ConectorToUserServiceImp;

public class TestConections extends AndroidTestCase{
	final int MAX_FURNITURES_PER_DAY = 4;
	final int MAX_FURNITURES_PER_USER = 12;
	ConectorToServices conector;

	protected void setUp() throws Exception {
        super.setUp();
	}

	protected void tearDown() throws Exception {
	        super.tearDown();
	}
	
	/**
	 * Se comprueba la conexion con el servicio solicitando
	 * el punto de reogida mas cercano de una ubicacion valida.
	 */
	public void testConectToGetCollectionPoint(){
		try{
			ConectorToCollectionPointService conector = 
					new ConectorToCollectionPointServiceImp(this.getContext());
			CollectionPoint realCollectionPoint = 
					new CollectionPoint(-6.193095,36.536233);
			Location valid_location = new Location("testlocation");
			valid_location.setLatitude(36.536234);
			valid_location.setLongitude(-6.193096);
			CollectionPoint point = 
					conector.getNearestCollectionPoint(valid_location, false);
			assertNotNull(point);
			assertTrue(point.getLng()+"|"+point.getLat(),
					point.getLng() == realCollectionPoint.getLng() 
					&& point.getLat() == realCollectionPoint.getLat());
		}
		catch(Exception e){
			fail("ERROR: "+e.toString());
		}
	}
	
	/**
	 * Se comprueba la conexion enviando una ubicacion no valida y comprobando
	 * que se devuelve el error con codigo 400.
	 */
	public void testConectToGetInvalidCollectionPoint(){
		try{
			ConectorToCollectionPointService conector = 
					new ConectorToCollectionPointServiceImp(this.getContext());
			Location valid_location = new Location("testlocation");
			valid_location.setLatitude(0);
			valid_location.setLongitude(0);
			CollectionPoint point = 
					conector.getNearestCollectionPoint(valid_location, false);
			assertNotNull(point);
		}
		catch(RuntimeException e){
			if(e.toString().equals(
					"java.lang.RuntimeException: Failed : HTTP error code : 400")){
				assertTrue(true);
			}
			else{
				fail("ERROR: "+e.toString());
			}
		}
		catch(Exception e){
			fail("ERROR: "+e.toString());
		}		
	}
	
	/**
	 * Add a new user to the collection request service, and finally delete it.
	 */
	public void testAddAndDeleteUser(){
		final String name = "Diego";
		final String phone_number = "601010101";
		final User user = new User(name,phone_number);
		addUser(user);
		deleteUser(user);
	}
	
	private void addUser(User user){
		try {
			ConectorToUserService conector = 
					new ConectorToUserServiceImp(this.getContext());
			conector.postNewUser(user.getName(), user.getPhone());
		} catch (Exception e) {
			fail(e.toString());
		}	
	}
	
	private void deleteUser(User user){
		try {
			ConectorToUserService conector = 
					new ConectorToUserServiceImp(this.getContext());
			conector.deleteUser(user);
		} catch (Exception e) {
			fail(e.toString());
		}	
	}
	
	public void testGet1ProvisionalAppointment(){
		final String phone_number = "689301243";
		final int N = 1;
		final int urbanPointId = 1;
		try {
			getProvisionalAppointment(phone_number,N,urbanPointId);
			deletePendingRequest(phone_number);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	private List<ProvisionalAppointment> getProvisionalAppointment(String phone_number,
		int num_furnitures, int point_id) throws Exception{
		ConectorToDailyAppointmentService conector = 
				new ConectorToDailyAppointmentServiceImp(this.getContext());
		List<ProvisionalAppointment> appointments = 
				conector.getProvisionalAppointments(phone_number, num_furnitures, point_id);
		assertNotNull(appointments);
		int totalFurnitures = 0;
		AppointmentValidator.validAppointment(appointments);
		for(ProvisionalAppointment a : appointments){
			totalFurnitures += a.getNumFurnitures();	
		}
		assertTrue(totalFurnitures == num_furnitures);
		return appointments;
	}
	
	public void testGet2ProvisionalAppointment(){
		final String phone_number = "689301243";
		final int N = MAX_FURNITURES_PER_DAY + 1;
		final int urbanPointId = 1;
		try {
			validAppointments(
					getProvisionalAppointment(phone_number,N,urbanPointId));
			deletePendingRequest(phone_number);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testGet3ProvisionalAppointment(){
		final String phone_number = "689301243";
		final int N = MAX_FURNITURES_PER_DAY * 2 + 1;
		final int urbanPointId = 1;
		try {
			validAppointments(
					getProvisionalAppointment(phone_number,N,urbanPointId));
			deletePendingRequest(phone_number);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testGetMoreOf3ProvisionalAppointment(){
		final String phone_number = "689301243";
		final int N = MAX_FURNITURES_PER_DAY * 3 + 1;
		final int urbanPointId = 1;
		try {
			getProvisionalAppointment(phone_number,N,urbanPointId);
			fail("Not show error when did ilegal request.");
		} catch (Exception e) {
			assertTrue(e.toString().contains("error code : 500"));
		}finally{
			try {
				deletePendingRequest(phone_number);
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}
	
	public void testGet2ProvisionalAppointmentsOfSameRequest(){
		final String phone_number = "689301243";
		final int N = 1;
		final int urbanPointId = 1;
		try {
			validAppointments(
					getProvisionalAppointment(phone_number,N,urbanPointId));
			getProvisionalAppointment(phone_number,N,urbanPointId);
			fail("Not show error when did ilegal request.");
		} catch (Exception e) {
			assertTrue(e.toString().contains("error code : 400"));
		}finally{
			try {
				deletePendingRequest(phone_number);
			} catch (Exception e) {
				fail(e.toString());
			}	
		}
	}
	
	public void testGetAndConfirm1ProvisionalAppointment(){
		final String name = "Diego";
		final String phone_number = "698352111";
		final User user = new User(name,phone_number);
		final int urbanPointId = 1;
		final int num_furnitures = 1;
		try{
			// First Add User.
			addUser(user);
			// Get provisional Appointment.
			List<ProvisionalAppointment> appointments  = getProvisionalAppointment(
					phone_number,num_furnitures,urbanPointId);
			validAppointments(appointments);
			// Confirm Appointment.
			for(ProvisionalAppointment a : appointments){
				assertTrue(a.getNumFurnitures() > 0);
				List<Furniture> furnitures = 
						generateExampleFurnituresList(a.getNumFurnitures());
				assertTrue(!furnitures.isEmpty());
				confirmAppointment(a,furnitures);
			}
		} catch (Exception e) {
		fail(e.toString());
		}finally{
			try {
				deletePendingRequest(phone_number);
			} catch (Exception e) {
				fail(e.toString());
			}
			finally{
				deleteUser(user);
			}
		}
		
	}
	
	public void testGetAndConfirm2ProvisionalAppointment(){
		final String name = "Diego";
		final String phone_number = "698347111";
		final User user = new User(name,phone_number);
		final int urbanPointId = 1;
		final int num_furnitures = MAX_FURNITURES_PER_DAY+1;
		try{
			// First Add User.
			addUser(user);
			// Get provisional Appointment.
			List<ProvisionalAppointment> appointments  = getProvisionalAppointment(
					phone_number,num_furnitures,urbanPointId);
			validAppointments(appointments);
			// Confirm Appointment.
			for(ProvisionalAppointment a : appointments){
				assertTrue(a.getNumFurnitures() > 0);
				List<Furniture> furnitures = 
						generateExampleFurnituresList(a.getNumFurnitures());
				assertTrue(!furnitures.isEmpty());
				confirmAppointment(a,furnitures);
			}
		} catch (Exception e) {
		fail(e.toString());
		}finally{
			try {
				deletePendingRequest(phone_number);
			} catch (Exception e) {
				fail(e.toString());
			}
			finally{
				deleteUser(user);
			}
		}
		
	}
	
	public void testGetAndConfirm3ProvisionalAppointment(){
		final String name = "Diego";
		final String phone_number = "698347111";
		final User user = new User(name,phone_number);
		final int urbanPointId = 1;
		final int num_furnitures = MAX_FURNITURES_PER_DAY*2+1;
		try{
			// First Add User.
			addUser(user);
			// Get provisional Appointment.
			List<ProvisionalAppointment> appointments  = getProvisionalAppointment(
					phone_number,num_furnitures,urbanPointId);
			validAppointments(appointments);
			// Confirm Appointment.
			for(ProvisionalAppointment a : appointments){
				assertTrue(a.getNumFurnitures() > 0);
				List<Furniture> furnitures = 
						generateExampleFurnituresList(a.getNumFurnitures());
				assertTrue(!furnitures.isEmpty());
				confirmAppointment(a,furnitures);
			}
		} catch (Exception e) {
		fail(e.toString());
		}finally{
			try {
				deletePendingRequest(phone_number);
			} catch (Exception e) {
				fail(e.toString());
			}
			finally{
				deleteUser(user);
			}
		}
		
	}
	
	private void deletePendingRequest(String phone) 
			throws IOException, URISyntaxException, HttpException{
		ConectorToDailyAppointmentService conector = 
				new ConectorToDailyAppointmentServiceImp(this.getContext());
		conector.deletePendingAppointments(phone);
	}
	
	private void confirmAppointment(ProvisionalAppointment provisionalAppointment,
			List<Furniture> furnitures) throws IOException{
		if(furnitures.isEmpty()){
			throw new IllegalArgumentException("no have furnitures.");
		}

		ConectorToDailyAppointmentService conector = 
				new ConectorToDailyAppointmentServiceImp(this.getContext());
		CollectionRequest req = null;
		try {
			req = new CollectionRequest(provisionalAppointment,furnitures);
			assertTrue(req.checkCorrectRequest());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		try {
			conector.confirmAppointment(req);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	/**
	 * Se comprueba que todos los campos de una cita no sean nulos 
	 * y tengan un formato valido.
	 * @param apointmnet
	 */
	private void validAppointments(List<ProvisionalAppointment> apointmnets){
		for(ProvisionalAppointment a : apointmnets){
			assertNotNull(a.getTelephone());
			assertTrue(a.getTelephone().charAt(0) == '6');
			assertTrue(a.getNumFurnitures() > 0);
			assertNotNull(a.getFch_collection());
			assertNotNull(a.getFch_request());
			assertTrue(a.getFch_collection().isAfter(new LocalDate()));
			assertNotNull(a.getCollectionPointId());
		}

	}
	
	public List<Furniture> generateExampleFurnituresList(int num_furnitures){
		if(num_furnitures < 1){
			throw new IllegalArgumentException("invalid number of furnitures.");
		}
		List<Furniture> furnitures = new ArrayList<Furniture>();
		for(int i = 1;i <= num_furnitures;i++){
			furnitures.add(new Furniture(i,1));
		}
		return furnitures;
	}
}
