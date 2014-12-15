package es.recicloid.pruebas.ui;

import com.robotium.solo.Solo;

import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import es.recicloid.main.MainActivity;
import es.recicloid.models.User;
import es.recicloid.utils.conections.ConectorToUserService;
import es.recicloid.utils.conections.ConectorToUserServiceImp;
import es.uca.recicloid.R;

public class TestMakeAnExampleRequest 
extends ActivityInstrumentationTestCase2<MainActivity>{
	private Solo solo;
	private User user = new User("Anonimo","600000015");
	
	public TestMakeAnExampleRequest() {
		super(MainActivity.class);
	}
	
	@Override
    protected void setUp() throws Exception {
		super.setUp();
		deleteUser(user);
        solo = new Solo(getInstrumentation(), getActivity());
    }
	
	private Resources getResoucres(){
		MainActivity myActivity = this.getActivity();
		return myActivity.getResources();	
	}
	
	public void testMake1ExampleUrbanRequest(){
		Resources testRes = getResoucres();
		solo.clickOnText(testRes.getString(R.string.title_solicitud));
		solo.clickOnText("Aceptar");
		solo.clickOnText(testRes.getString(R.string.item_bathtub));
		solo.clickOnText("Solicitar recogida");
		solo.clickOnText("Establecer manualmente");
		solo.clickOnScreen((float)610, (float)630);
		solo.clickOnText("Solicitar recogida");
		insertUserAndPhone(user.getName(),user.getPhone());
		solo.clickOnText("Solicitar recogida");
		solo.clickOnCheckBox(0);
		solo.clickOnButton(testRes.getString(R.string.title_solicitud));
		solo.clickOnButton(testRes.getString(R.string.cofirmation));
	}
	
	private void insertUserAndPhone(String userName,String phoneNumber){
		EditText editTextName = solo.getEditText(1);
		assertNotNull(editTextName);
		EditText editTextPhone = solo.getEditText(0);
		assertNotNull(editTextPhone);
		solo.enterText(editTextName, userName);
		solo.enterText(editTextPhone, phoneNumber);
	}

	@Override
	protected void tearDown() throws Exception {
	    deleteUser(user);
	    solo.finishOpenedActivities();
	}
	
	private void deleteUser(User user){
		try {
			ConectorToUserService conector = 
					new ConectorToUserServiceImp(getInstrumentation().getTargetContext()
							.getApplicationContext());
			conector.deleteUser(user);
		} catch (Exception e) {
			e.toString();
		}	
	}
}
