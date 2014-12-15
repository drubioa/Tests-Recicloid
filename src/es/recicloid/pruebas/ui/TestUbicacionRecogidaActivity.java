package es.recicloid.pruebas.ui;


import com.robotium.solo.Solo;

import es.recicloid.UbicacionRecogida.UbicacionRecogidaActivity;
import es.uca.recicloid.R;
import android.content.res.Resources;
import android.os.Handler;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;

public class TestUbicacionRecogidaActivity 
extends ActivityInstrumentationTestCase2<UbicacionRecogidaActivity>{
	private Solo solo;
	
	public TestUbicacionRecogidaActivity() {
		super(UbicacionRecogidaActivity.class);
	}

	@Override
    protected void setUp() throws Exception {
		super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }	
	
	private Resources getResoucres(){
		UbicacionRecogidaActivity myActivity = this.getActivity();
		return myActivity.getResources();	
	}
	
	/**
	 * Se comprueba si al escoger una ubicacion manual se muestra por pantalla.
	 * Estas pruebas estan planificadas para una Nexus 5 con pantalla vertical.
	 */
	@SmallTest
	public void testUbicacionManualPuntoUrbano(){
		Resources testRes = getResoucres();
		solo.clickOnText(testRes.getString(R.string.dialog_selectlocation_manual));
		solo.clickOnScreen(682, 630);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable(){
		@Override
		      public void run(){
		   }
		}, 3000);
		assertTrue(solo.searchText("Calle Venus, 11"));
		Button btn = solo.getButton(testRes.getString(R.string.title_solicitud));
		assertTrue(btn.isEnabled());
	}
	
	@Override
	protected void tearDown() throws Exception {
	    solo.finishOpenedActivities();
	}
}
