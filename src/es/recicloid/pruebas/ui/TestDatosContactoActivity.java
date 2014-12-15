package es.recicloid.pruebas.ui;

import com.robotium.solo.Solo;

import es.recicloid.DatosContacto.DatosContactoActivity;
import es.uca.recicloid.R;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

public class TestDatosContactoActivity 
	extends ActivityInstrumentationTestCase2<DatosContactoActivity>{
	private Solo solo;
	
	public TestDatosContactoActivity(){
		super(DatosContactoActivity.class);
	}

	@Override
    protected void setUp() throws Exception {
		super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

	/**
	 * Se comprueba que un usuario y telefono valiros activan el boton de continuar.
	 */
	public void testValidUserAndPhoneActiveButton(){
		Resources res = this.getActivity().getResources();
		Button btn_continuar = solo.getButton(res.getString(R.string.title_solicitud));
		assertFalse(btn_continuar.isEnabled());
		assertNotNull(btn_continuar);
		insertUserAndPhone("Diego","699390216");
		assertTrue(btn_continuar.isEnabled());
	}
	
	public void testInvalidPhoneNumber(){
		Resources res = this.getActivity().getResources();
		Button btn_continuar = solo.getButton(res.getString(R.string.title_solicitud));
		assertFalse(btn_continuar.isEnabled());
		assertNotNull(btn_continuar);
		insertUserAndPhone("Diego","300202020");
		assertFalse(btn_continuar.isEnabled());
		insertUserAndPhone("Diego","1414");
		assertFalse(btn_continuar.isEnabled());
	}
	
	/**
	 * Se comprueba que el sistema no permite dejar en blanco el campo telefono.
	 */
	public void testEmptyPhone(){
		Resources res = this.getActivity().getResources();
		Button btn_continuar = solo.getButton(res.getString(R.string.title_solicitud));
		assertFalse(btn_continuar.isEnabled());
		assertNotNull(btn_continuar);
		insertUserAndPhone("Diego","");
		assertFalse(btn_continuar.isEnabled());
	}
	
	public void testEmptyUser(){
		Resources res = this.getActivity().getResources();
		Button btn_continuar = solo.getButton(res.getString(R.string.title_solicitud));
		assertFalse(btn_continuar.isEnabled());
		assertNotNull(btn_continuar);
		insertUserAndPhone("","699390216");
		assertFalse(btn_continuar.isEnabled());		
	}
	
	/**
	 * Se comprueba que el usuario tenga una longitud minima de 2
	 */
	public void testShortUserName(){
		Resources res = this.getActivity().getResources();
		Button btn_continuar = solo.getButton(res.getString(R.string.title_solicitud));
		assertFalse(btn_continuar.isEnabled());
		insertUserAndPhone("A","699390216");
		assertFalse(btn_continuar.isEnabled());
		insertUserAndPhone("Aa","699390216");
	}
	
	/**
	 * Se testea si el boton conitnuar una vez activado se desactiva al
	 * introducir un usuario o telefono no valido.
	 */
	public void testDesactiveBtnIfRemoveValidUserOrPhone(){
		Resources res = this.getActivity().getResources();
		Button btn_continuar = solo.getButton(res.getString(R.string.title_solicitud));
		assertFalse(btn_continuar.isEnabled());
		assertNotNull(btn_continuar);
		insertUserAndPhone("Diego","699390216");
		assertTrue(btn_continuar.isEnabled());
		insertUserAndPhone("Diego","");
		assertFalse(btn_continuar.isEnabled());
		insertUserAndPhone("Diego","699390216");
		assertTrue(btn_continuar.isEnabled());
		insertUserAndPhone("","699390216");
		assertFalse(btn_continuar.isEnabled());
	}
	
	private void insertUserAndPhone(String userName,String phoneNumber){
		EditText editTextName = solo.getEditText(1);
		assertNotNull(editTextName);
		EditText editTextPhone = solo.getEditText(0);
		assertNotNull(editTextPhone);
		solo.enterText(editTextName, userName);
		solo.enterText(editTextPhone, phoneNumber);
	}
	
}
