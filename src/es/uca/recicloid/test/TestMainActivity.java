package es.uca.recicloid.test;

import com.robotium.solo.Solo;

import es.uca.recicloid.R;
import es.uca.recicloid.activities.MainActivity;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

public class TestMainActivity 
	extends ActivityInstrumentationTestCase2<MainActivity>{
	private Solo solo;

	public TestMainActivity() {
		super(MainActivity.class);
	}
	
	@Override
    protected void setUp() throws Exception {
		super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }
	
	private Resources getResoucres(){
		MainActivity myActivity = this.getActivity();
		return myActivity.getResources();	
	}
	
	public void testAllOptionAreSelectable(){
		Resources testRes = getResoucres();
		solo.clickOnText(testRes.getString(R.string.title_solicitud));
		solo.sendKey(Solo.DOWN);
	}
	
	public void testExistAllOptiones(){
		Resources testRes = getResoucres();
		solo.searchText(testRes.getString(R.string.title_solicitud));
		solo.searchText(testRes.getString(R.string.descr_solicitud));
	}

	@Override
	protected void tearDown() throws Exception {
	    solo.finishOpenedActivities();
	}
}
