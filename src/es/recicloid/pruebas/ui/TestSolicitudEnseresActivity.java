package es.recicloid.pruebas.ui;

import com.robotium.solo.Solo;

import es.recicloid.SolicitudEnseres.SolicitudEnseresActivity;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import es.uca.recicloid.R;

/**
 * Las pruebas se realizan sobre la app en castellano.
 * @author Diego Rubio Abujas
 *
 */
public class TestSolicitudEnseresActivity 
	extends ActivityInstrumentationTestCase2<SolicitudEnseresActivity>{
	private Solo solo;

	public TestSolicitudEnseresActivity() {
		super(SolicitudEnseresActivity.class);
	}
	
	@Override
    protected void setUp() throws Exception {
		super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

	private Resources getResoucres(){
		SolicitudEnseresActivity myActivity = this.getActivity();
		return myActivity.getResources();	
	}
	
	public void testDisableButtonToContinue() throws Exception {
		Resources testRes = getResoucres();
		solo.clickOnText("Aceptar");
		Button btn_solicitarRecog  = solo.getButton(testRes.getString(R.string.title_solicitud));
		assertTrue(btn_solicitarRecog.isEnabled() == false);
	}


	public void testSelectAllCategories() throws Exception {
		Resources testRes = getResoucres();
		final String[] categories = testRes.getStringArray(R.array.categorias_enseresa);
		solo.clickOnText("Aceptar");
		solo.clickOnText(categories[0]);
		solo.clickOnText(categories[0]);
		for(int i = 1;i < categories.length;i++){
			solo.clickOnText(categories[i-1]);
			solo.clickOnText(categories[i]);
		}
		for(int i = categories.length-1;i > 0;i--){
			solo.clickOnText(categories[i]);
			solo.clickOnText(categories[i-1]);
		}

	}
	
	public void testSelect4ItemsAndShowDialog(){
		try{
			Resources testRes = getResoucres();
			final String[] categories = testRes.getStringArray(R.array.categorias_enseresa);
			solo.clickOnText("Aceptar");
			solo.clickOnText(categories[0]);
			solo.clickOnText(categories[0]);
			solo.clickOnText(testRes.getString(R.string.item_bathtub));
			solo.clickOnText(testRes.getString(R.string.item_bathtub));
			solo.clickOnText(testRes.getString(R.string.dialog_more_funitures_add)); 
			solo.clickOnText(testRes.getString(R.string.item_bathtub));
			solo.clickOnText(testRes.getString(R.string.dialog_more_funitures_add)); 
			solo.clickOnText(testRes.getString(R.string.item_bathtub));
			solo.clickOnText(testRes.getString(R.string.dialog_more_funitures_add)); 
			solo.searchText(testRes.getString(R.string.dialog_title_4items));
			solo.searchText(testRes.getString(R.string.dialog_descr_4items));
			solo.clickOnText(testRes.getString(R.string.dialog_ok));
			solo.searchText(testRes.getString(R.string.item_bathtub)+"(4)");
		}catch(Exception e){
			fail(e.toString());
		}
	}
	
	@Override
	protected void tearDown() throws Exception {
	    solo.finishOpenedActivities();
	}
}
