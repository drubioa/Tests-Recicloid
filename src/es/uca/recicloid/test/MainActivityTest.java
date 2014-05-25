package es.uca.recicloid.test;

import es.uca.recicloid.activities.MainActivity;
import android.test.ActivityInstrumentationTestCase2;

public class MainActivityTest 
	extends ActivityInstrumentationTestCase2<MainActivity>{
	@SuppressWarnings("unused")
	private MainActivity mMainActivityTest;
	
	public MainActivityTest(){
		super(MainActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mMainActivityTest = this.getActivity();
	}

	protected void tearDown() throws Exception {
	}
	
	public void testMainActivity() {
	    assertTrue(true);
	}
}