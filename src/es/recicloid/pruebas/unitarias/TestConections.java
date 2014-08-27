package es.recicloid.pruebas.unitarias;

import android.location.Location;
import es.recicloid.clases.CollectionPoint;
import es.recicloid.logic.conections.ConectorToServices;
import junit.framework.TestCase;

public class TestConections extends TestCase{

	ConectorToServices conector = new ConectorToServices();
	
	/**
	 * Se comprueba la conexion con el servicio solicitando
	 * el punto de reogida mas cercano de una ubicacion valida.
	 */
	public void testConectToGetCollectionPoint(){
		try{
			CollectionPoint realCollectionPoint = new CollectionPoint(-6.193095,36.536233);
			Location valid_location = new Location("testlocation");
			valid_location.setLatitude(36.536234);
			valid_location.setLongitude(-6.193096);
			CollectionPoint point = conector.getNearestCollectionPoint(valid_location, false);
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
			Location valid_location = new Location("testlocation");
			valid_location.setLatitude(0);
			valid_location.setLongitude(0);
			CollectionPoint point = conector.getNearestCollectionPoint(valid_location, false);
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
}
