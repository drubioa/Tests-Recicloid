package es.uca.recicloid.test;

import java.io.InputStream;

import com.google.android.gms.maps.model.LatLng;

import es.uca.recicloid.activities.UbicacionRecogidaActivity;
import es.uca.recicloid.map.Zone;
import es.uca.recicloid.map.ZoneParser;
import android.test.ActivityInstrumentationTestCase2;

public class ZonesTest 
	extends ActivityInstrumentationTestCase2<UbicacionRecogidaActivity>{
	
	private Zone municipal;
	private UbicacionRecogidaActivity mUbicacionRecogidaActivity;
	
	public ZonesTest(){
		super(UbicacionRecogidaActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mUbicacionRecogidaActivity = this.getActivity();
		ZoneParser parser = new ZoneParser();
		InputStream in = mUbicacionRecogidaActivity.getAssets().open("municipal-area.xml");
		municipal = new Zone(parser.parse(in));
	}

	protected void tearDown() throws Exception {
	}

	public void testLocalidadIsCorrect() {
		assertNotNull(mUbicacionRecogidaActivity);
		assertNotNull(municipal);
		LatLng puertoreal = 
				new LatLng(36.530375900000000000, -6.194416899999965000);
		assertTrue(municipal.isInside(puertoreal));
	}
}