package es.uca.recicloid.test;


import com.robotium.solo.Solo;

import es.uca.recicloid.R;
import es.uca.recicloid.activities.UbicacionRecogidaActivity;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

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
	
	public void testUbicacionAutomatica(){
		Resources testRes = getResoucres();
		solo.clickOnText(testRes.getString(R.string.dialog_selectlocation_auto));
	}
	
	@Override
	protected void tearDown() throws Exception {
	    solo.finishOpenedActivities();
	}
}
