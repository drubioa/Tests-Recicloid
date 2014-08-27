package es.recicloid.pruebas.funcionales;

import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.robotium.solo.Solo;

import es.recicloid.activities.servrecog.CondicionesUsoActivity;
import es.uca.recicloid.R;

public class TestCondicionesUseActivity 
	extends ActivityInstrumentationTestCase2<CondicionesUsoActivity>{
	private Solo solo;

	public TestCondicionesUseActivity() {
		super(CondicionesUsoActivity.class);
	}
	
	@Override
    protected void setUp() throws Exception {
		super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }
	
	private Resources getResoucres(){
		CondicionesUsoActivity myActivity = this.getActivity();
		return myActivity.getResources();	
	}
	
	public void testMustAcceptTerms(){
		Resources testRes = getResoucres();
		solo.searchText(testRes.getString(R.string.accept_conditions));
		Button btn = solo.getButton(testRes.getString(R.string.title_solicitud));
		assertTrue(btn.isEnabled() == false);
		solo.clickOnCheckBox(0);
		solo.clickOnButton(testRes.getString(R.string.title_solicitud));
		assertTrue(btn.isEnabled());
	}

	@Override
	protected void tearDown() throws Exception {
	    solo.finishOpenedActivities();
	}
}
