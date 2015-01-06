package es.recicloid.pruebas.unit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.LocalDate;
import android.location.Location;
import android.test.AndroidTestCase;
import android.util.Log;
import es.recicloid.models.CollectionPoint;
import es.recicloid.models.CollectionRequest;
import es.recicloid.models.Furniture;
import es.recicloid.models.ProvisionalAppointment;
import es.recicloid.models.User;
import es.recicloid.utils.conections.ConectionToPostNewUser;
import es.recicloid.utils.conections.ConectorToCollectionPointService;
import es.recicloid.utils.conections.ConectorToConfirmAppointment;
import es.recicloid.utils.conections.ConectorToDeletePendingRequests;
import es.recicloid.utils.conections.ConectorToGetCollectionReq;
import es.recicloid.utils.conections.ConectorToGetProvisAppointment;
import es.recicloid.utils.conections.ConectorToUserService;
import es.recicloid.utils.conections.ConectorToUserServiceImp;
import es.recicloid.utils.conections.InfoToFindCollectionPoint;
import es.recicloid.utils.conections.InfoToGetProvAppointments;

public class TestConections extends AndroidTestCase{
	final int MAX_FURNITURES_PER_DAY = 4;
	final int MAX_FURNITURES_PER_USER = 12;
	

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
					new ConectorToCollectionPointService(this.getContext());
			CollectionPoint realCollectionPoint = 
					new CollectionPoint(-6.193095,36.536233);
			Location valid_location = new Location("testlocation");
			valid_location.setLatitude(36.536234);
			valid_location.setLongitude(-6.193096);
			InfoToFindCollectionPoint info
				= new InfoToFindCollectionPoint(valid_location,false);
			conector.execute(info);
			assertNull(conector.exception);
			CollectionPoint point = conector.get();
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
					new ConectorToCollectionPointService(this.getContext());
			Location valid_location = new Location("testlocation");
			valid_location.setLatitude(0);
			valid_location.setLongitude(0);
			InfoToFindCollectionPoint info
				= new InfoToFindCollectionPoint(valid_location,false);
			conector.execute(info);
			assertNull(conector.exception);
			CollectionPoint point = conector.get();
			assertNull(point);
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
			fail(e.toString());
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
			fail(e.toString());
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
		final String phone = "698352111";
		final User user = new User(name,phone);
		final int urbanPointId = 1;
		final int num_furnitures = 1;
		try{
			// First Add User.
			addUser(user);
			// Get provisional Appointment.
			List<ProvisionalAppointment> appointments  = getProvisionalAppointment(
					phone,num_furnitures,urbanPointId);
			validAppointments(appointments);
			// Confirm Appointment.
			for(ProvisionalAppointment a : appointments){
				assertTrue(a.getNumFurnitures() > 0);
				List<Furniture> furnitures = 
						generateExampleFurnituresList(a.getNumFurnitures());
				assertTrue(!furnitures.isEmpty());
				confirmAppointment(a,furnitures);
			}
			checkValidConfirmedRequest(phone,1);
		} catch (Exception e) {
		fail(e.toString());
		}finally{
			try {
				deletePendingRequest(phone);
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
		final String phone = "698347111";
		final User user = new User(name,phone);
		final int urbanPointId = 1;
		final int num_furnitures = MAX_FURNITURES_PER_DAY+1;
		try{
			// First Add User.
			addUser(user);
			// Get provisional Appointment.
			List<ProvisionalAppointment> appointments  = getProvisionalAppointment(
					phone,num_furnitures,urbanPointId);
			validAppointments(appointments);
			// Confirm Appointment.
			for(ProvisionalAppointment a : appointments){
				assertTrue(a.getNumFurnitures() > 0);
				List<Furniture> furnitures = 
						generateExampleFurnituresList(a.getNumFurnitures());
				assertTrue(!furnitures.isEmpty());
				confirmAppointment(a,furnitures);
			}
			checkValidConfirmedRequest(phone,2);
		} catch (Exception e) {
		fail(e.toString());
		}finally{
			try {
				deletePendingRequest(phone);
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
		final String phone = "698347111";
		final User user = new User(name,phone);
		final int urbanPointId = 1;
		final int num_furnitures = MAX_FURNITURES_PER_DAY*2+1;
		try{
			// First Add User.
			addUser(user);
			// Get provisional Appointment.
			List<ProvisionalAppointment> appointments  = getProvisionalAppointment(
					phone,num_furnitures,urbanPointId);
			validAppointments(appointments);
			// Confirm Appointment.
			for(ProvisionalAppointment a : appointments){
				assertTrue(a.getNumFurnitures() > 0);
				List<Furniture> furnitures = 
						generateExampleFurnituresList(a.getNumFurnitures());
				assertTrue(!furnitures.isEmpty());
				confirmAppointment(a,furnitures);
			}
			checkValidConfirmedRequest(phone,3);
		} catch (Exception e) {
		fail(e.toString());
		}finally{
			try {
				deletePendingRequest(phone);
			} catch (Exception e) {
				fail(e.toString());
			}
			finally{
				deleteUser(user);
			}
		}
		
	}

	/**
	 * Se comprueba que al intentar obtener el numero de peticiones pendientes de un usuario que no ha confirmado
	 * ninguna, esta es cero.
	 */
	public void testGetPendingOfUserNoGotPending(){
		final String name = "Anonymous";
		final String phone = "600010203";
		final User user = new User(name,phone);
		try{
			addUser(user);
			checkValidConfirmedRequest(phone,0);
		} catch (Exception e){
			if(!e.toString().contains("HTTP error code : 204")){
				fail(e.toString());
			}
		}finally{
			deleteUser(user);
		}
	}

	public void testGetPendingOfInexistUser(){
		final String phone = "95683010101";
		try{
			checkValidConfirmedRequest(phone,0);
		}catch(Exception e){
			if(!e.toString().contains("HTTP error code : 404")){
					fail(e.toString());
			}
		}
	}

	public void testGet1ProvisionalAppointment(){
		final String phone_number = "689301246";
		final int N = 1;
		final int urbanPointId = 1;
		try {
			getProvisionalAppointment(phone_number,N,urbanPointId);
			deletePendingRequest(phone_number);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	private void addUser(User user){
		try {
			ConectionToPostNewUser conector = 
					new ConectionToPostNewUser(this.getContext());
			conector.execute(user);
			if(!conector.get()){
				fail("No se creo el usuario ");
			}
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
	
	private List<ProvisionalAppointment> getProvisionalAppointment(String phone_number,
		int num_furnitures, int point_id) throws Exception{
		ConectorToGetProvisAppointment conector =
				new ConectorToGetProvisAppointment(getContext());
		InfoToGetProvAppointments info = new 
				InfoToGetProvAppointments(phone_number,num_furnitures,point_id);
		conector.execute(info);
		List<ProvisionalAppointment> appointments = conector.get();
		if(conector.exception != null){
			throw conector.exception;
		}
		assertNotNull(appointments);
		int totalFurnitures = 0;
		AppointmentValidator.validAppointment(appointments);
		for(ProvisionalAppointment a : appointments){
			totalFurnitures += a.getNumFurnitures();	
		}
		assertTrue(totalFurnitures == num_furnitures);
		return appointments;
	}
	
	private void checkValidConfirmedRequest(String phone,int numOfRequests) 
			throws Exception{
		ConectorToGetCollectionReq conector =
				new ConectorToGetCollectionReq(getContext());
		conector.execute(phone);
		List<CollectionRequest> req = conector.get();
		if(conector.exception != null){
			throw conector.exception;
		}
		// Se esperan al menos un numOfRequest dias.
		assertTrue(req.size() >= numOfRequests);
	}
	
	private void deletePendingRequest(String phone) throws Exception{
		ConectorToDeletePendingRequests conector = 
				new ConectorToDeletePendingRequests(this.getContext());
		conector.execute(phone);
		if(!conector.get()){
			Log.e("deletePendingRequest","Cannot delete pending requests");
			if(conector.exception != null){
				// Si hay una exception la lanza
				throw conector.exception;
			}
		}
	}
	
	private void confirmAppointment(ProvisionalAppointment provisionalAppointment,
			List<Furniture> furnitures) throws IOException{
		if(furnitures.isEmpty()){
			throw new IllegalArgumentException("no have furnitures.");
		}

		ConectorToConfirmAppointment conector = 
				new ConectorToConfirmAppointment(this.getContext());
		CollectionRequest req = null;
		try {
			req = new CollectionRequest(provisionalAppointment,furnitures);
			assertTrue(req.checkCorrectRequest());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		try {
			conector.execute(req);
			if(!conector.get()){
				Log.e("ConectorToConfirmAppointment","Cannot confirm appointment");
				if(conector.exception != null){
					throw conector.exception;
				}
			}
		} catch (Exception e) {
			Log.e("confirmAppointment",e.toString());
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
