package code.sas_server.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;



public class SAS_Server_Main extends Activity//extends MapActivity 
{
	public static EditText  longitude_xml, latitude_xml, phone_xml;
	
	public static String latitude, longitude, phone = "None";
	
	public static String accuracy = "100.0";
	
	public static SAS_Server_DB dataBase;
	
	public static String clientNumber;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);

    	dataBase  = new SAS_Server_DB();

    	longitude_xml  =  (EditText) findViewById(R.id.longitudeValue);
    	latitude_xml  = (EditText) findViewById(R.id.latitudeValue);
    	phone_xml = (EditText) findViewById(R.id.phonevalue);

    	longitude_xml.setText(longitude);
    	latitude_xml.setText(latitude);
    	phone_xml.setText(phone);
    }
//---------------------------------------------------------------------------------------

    public void onStart()
    {
 	  super.onStart();
 	  longitude_xml.setText(longitude);
 	  latitude_xml.setText(latitude);
 	  phone_xml.setText(phone);
    }
//---------------------------------------------------------------------------------------
    
    public void onResume()
    {
 	  super.onResume();
 	  longitude_xml.setText(longitude);
 	  latitude_xml.setText(latitude);
 	  phone_xml.setText(phone);
    }
//---------------------------------------------------------------------------------------
   
    /*
     * This function is the map view button handler it moved to SAS_Server_Map_View and 
     * show the map after saving the inserted coordinates.
     */
    public void mapButtonHandler(View view) 
    {
    	SaveTheInsertedCoordinates(); //save the inserted values
    	
    	//if one of the coordinates is empty - print illegal message
    	if (latitude_xml.getText().toString().isEmpty() || longitude_xml.getText().toString().isEmpty())
    	{
    		Toast.makeText(this,"Please insert 2 Legal Coordinates!" , Toast.LENGTH_SHORT).show();
    	}
    	
    	else
    	{
    		Toast.makeText(this, "Loading The Coordinates: " + 
						"\n" + 
					   	"Latitude: " + 
						latitude_xml.getText() + 
						"\n" + 
						"Longitude: " +
						longitude_xml.getText(), Toast.LENGTH_SHORT);
    		
    		Intent intentExercise = new Intent(view.getContext(), SAS_Server_Map_View.class);
    		startActivityForResult(intentExercise, 0);
    	}
    }
//---------------------------------------------------------------------------------------
    /*
     * This function is the sms DB button handler it moved to SAS_Server_DB_Activity and load the SMS messages. 
     */
    public void smsButtonHandler(View view) 
    {
    	Intent intentExercise = new Intent(view.getContext(), SAS_Server_DB_Activity.class);
    	startActivity(intentExercise);
    }
//---------------------------------------------------------------------------------------
    
    /*
     * This function save the EditText values that inserted on the main screen.
     */
    public void SaveTheInsertedCoordinates()
    {
    	longitude_xml = (EditText)findViewById(R.id.longitudeValue);
        latitude_xml = (EditText)findViewById(R.id.latitudeValue);
        phone_xml = (EditText)findViewById(R.id.phonevalue);
    }
//---------------------------------------------------------------------------------------
    
    public void showAllPointsButtonHandler(View view)
    {
    	Toast.makeText(this, "In Developement" , Toast.LENGTH_SHORT).show();
//    	Toast.makeText(this, dataBase.getNodeById(1).status , Toast.LENGTH_SHORT).show();
    	
    }
//---------------------------------------------------------------------------------------
    
    
    
    
    
        
    
    
}
