package es.recicloid.pruebas.unit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.recicloid.models.CollectionPoint;
import es.recicloid.models.Furniture;
import es.recicloid.models.User;
import es.recicloid.utils.json.JsonToFileManagement;
import android.test.AndroidTestCase;

public class TestJsonToFile extends AndroidTestCase{

	JsonToFileManagement jsonToFile;
	
	protected void setUp() throws Exception {
        super.setUp();
}

	protected void tearDown() throws Exception {
	        super.tearDown();
	}
	
	public void testSaveUserInJsonFileAndLoad(){
		final String name = "Anonymous";
		final String phone_number = "600000001";
		jsonToFile = new JsonToFileManagement( getContext(),"testUserjsonToFile.json");
		try {
			jsonToFile.saveInJsonFile(new User(name,phone_number));
		} catch (IOException e) {
			fail(e.toString());
		}
		User user;
		try {
			user = jsonToFile.loadUserForJsonFile();
			assertNotNull(user);
			assertTrue(user.getName().equals(name));
			assertTrue(user.getPhone().equals(phone_number));
		} catch (IOException e) {
			fail(e.toString());
		}
	}
	
	public void testSaveFurnituresInJsonFileAndLoad(){
		final List<Furniture> furnitures = new ArrayList<Furniture>();
		furnitures.add(new Furniture(1,1));
		furnitures.add(new Furniture(2,2));
		jsonToFile = new JsonToFileManagement( 
				getContext(),"testFurnituresjsonToFile.json");
		try {
			jsonToFile.saveFurnituresInJsonFile(furnitures);
		} catch (IOException e) {
			fail(e.toString());
		}
		try {
			List<Furniture> furnitures2 = jsonToFile.loadFurnituresFromLocalFile();
			assertNotNull(furnitures2);
			assertTrue(furnitures.size() == furnitures2.size());
			for(int i = 0;i < furnitures.size();i++){
				assertTrue(furnitures.get(i).getCantidad() == 
						furnitures2.get(i).getCantidad());
				assertTrue(furnitures.get(i).getId() == 
						furnitures2.get(i).getId());
			}
		} catch (IOException e) {
			fail(e.toString());
		}
	}
	
	public void testSaveCollectionPointInJsonFileAndLoad(){
		final double lng = 1.2;
		final double lat = 3.4;
		final CollectionPoint point = new CollectionPoint(lng,lat);
		jsonToFile = new JsonToFileManagement( getContext(),"testUserjsonToFile.json");
		try {
			jsonToFile.saveInJsonFile(point);
		} catch (IOException e) {
			fail(e.toString());
		}
		try {
			CollectionPoint p = jsonToFile.loadCollectionPointFromLocalFile();
			assertNotNull(p);
			assertTrue(p.getLng() == lng);
			assertTrue(p.getLat() == lat);
		} catch (IOException e) {
			fail(e.toString());
		}
	}
	
}
