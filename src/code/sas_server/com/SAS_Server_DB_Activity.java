package code.sas_server.com;


import java.util.ArrayList;


import code.sas_server.com.SAS_Server_Sms_Receiver;
import code.sas_server.com.SAS_Server_DB.EmergencyNode;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SAS_Server_DB_Activity extends Activity implements OnClickListener, OnItemClickListener
{
	//Our database
	SAS_Server_DB dataBase;
	
	//List of emergencies
	ArrayList<String> smsList = new ArrayList<String>();
	
	Context context;
	boolean inArchive = false;
	

	public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        setTheme( android.R.style.Theme_Light );
        setContentView(R.layout.update_database);
        
        this.dataBase = SAS_Server_Main.dataBase;
       
        context   = getApplicationContext();
        
        //IZIK register popup menu for SMS List
        //
        registerForContextMenu(findViewById(R.id.SMSList));
        
        this.findViewById( R.id.UpdateList ).setOnClickListener(this);
        this.findViewById( R.id.ArchiveList ).setOnClickListener(this);
		
        //Update data base when start.
        findViewById(R.id.UpdateList).performClick();
    }
	
	public void onCreateContextMenu(ContextMenu menu, View v,
	        ContextMenuInfo menuInfo)
	{
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    
	    if(inArchive)
	    	inflater.inflate(R.layout.pop_up_archive, menu);
	    else
	        inflater.inflate(R.layout.pop_up, menu);
	}
	
	public boolean onContextItemSelected(MenuItem item)
	{
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
	            .getMenuInfo();
	    
	    String[]           splitted; 
	    String             aux,id;
	    int                nodeId;
	    EmergencyNode      currNode;
	 
	    //get selected item from list
	    aux      = smsList.get((int) info.id);
	    splitted = aux.split("\n"); 
	    id       = splitted[0];
	    id       = id.substring(4);
	    nodeId   = Integer.parseInt(id);
	    currNode = dataBase.getNodeById(nodeId);
	    
	    splitted = currNode.text.split("\n");
	    
        //Take care on active node.
	    if(currNode.status.equals("active"))
	    	switch (item.getItemId())
	    	{
	    	case R.id.Remove:
	    		dataBase.getRealNodeById(nodeId).status = "archive";
	    		findViewById(R.id.UpdateList).performClick();
	    		Toast.makeText( context ,"Moved to archive" ,Toast.LENGTH_SHORT ).show();
	    		
	    		break;


	    	case R.id.Fetch:
	    		 SAS_Server_Main.latitude      = splitted[1];
	             SAS_Server_Main.longitude     = splitted[2];
	             SAS_Server_Main.accuracy	   = splitted[4];
	             SAS_Server_Main.phone = currNode.source;
	             Toast.makeText( context ,"Coordinates Applied From Active" + "\n" + "ID : " + currNode.nodeId + "\n" + SAS_Server_Main.accuracy  ,Toast.LENGTH_SHORT ).show();
	    		 startActivity(new Intent(this.getApplicationContext() , SAS_Server_Main.class ));
	    		 break;
	    	}
	    //Take care on archive node
	    if(currNode.status.equals("archive"))
	    	switch (item.getItemId())
	    	{
	    	case R.id.Return:
	    		dataBase.getRealNodeById(nodeId).status = "active";
	    		findViewById(R.id.ArchiveList).performClick();
	    		Toast.makeText( context ,"Moved Back To Active" ,Toast.LENGTH_SHORT ).show();
	    		break;


	    	case R.id.FetchArchive:
	    		 SAS_Server_Main.latitude    = splitted[1];
	             SAS_Server_Main.longitude   = splitted[2];
	             SAS_Server_Main.accuracy	 = splitted[4];;
	             SAS_Server_Main.phone = currNode.source;
	    		Toast.makeText( context ,"Coordinates Applied From Archive" + "\n" + "ID : " + currNode.nodeId ,Toast.LENGTH_SHORT ).show();
	    		startActivity(new Intent(this.getApplicationContext() , SAS_Server_Main.class ));
	    		break;
	    	}
	    
	    return false;
	}

    //Click on single event 
	public void onItemClick( AdapterView<?> parent, View view, int pos, long id ) 
	{
		String latitude,longitude,sender,data = null,accuracy,time;
		String[] splitted;
		int nodeId;
		
		String item = ((TextView)view).getText().toString();
		splitted    = item.split("\n");
		
		data        = splitted[0].substring(4);
		nodeId      = Integer.parseInt(data);
		

		try 
		{
			
		    splitted = dataBase.getNodeById(nodeId ).text.split("\n"); 
		   
		    time   = dataBase.getNode(nodeId).timeArrived;
			sender = dataBase.getNode(nodeId).source;
			
			latitude  = splitted[1];
			longitude = splitted[2];
		
			
			//Update real node's field 'accuracy' 
			dataBase.getRealNodeById(nodeId).accuracy =  splitted[4];
	
			data = "";
			for ( int i = 5; i < splitted.length; ++i )
			{
			    data += splitted[i];
			}
		    
			//instead of this toast , location on map can be shown.
			Toast.makeText( this,"Sender : " + sender  + "\n"+
					             "Time   : " + time    + "\n"+
					             "Latitude : " + latitude + "\n" +
					             "Longitude: " + longitude + "\n" +
					             "Text   : " + data            , Toast.LENGTH_SHORT ).show();
		} 
		
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
    
    
	//Click on button Update database.Builds DataBase from inbox.
		public void onClick( View v ) 
		{
			ContentResolver contentResolver = getContentResolver();
			Cursor cursor = contentResolver.query( Uri.parse( "content://sms/inbox" ),null,null, null, null);

			int indexBody = cursor.getColumnIndex( SAS_Server_Sms_Receiver.BODY );
			int indexAddr = cursor.getColumnIndex( SAS_Server_Sms_Receiver.ADDRESS );
			int indexDate = cursor.getColumnIndex( SAS_Server_Sms_Receiver.DATE);
			EmergencyNode auxNode;
			
			String aux = "";
			
			if ( indexBody < 0 || !cursor.moveToFirst() ) return;
			
			smsList.clear();
			
			//Fill the database...
			//Check each sms for required format.
			if( v.getId() == R.id.UpdateList )
			{
				inArchive = false;
				do
				{

					aux = cursor.getString( indexBody );
					if(aux.substring(0, 4).equals("SAS1"))
					{
						this.dataBase.addNode(aux, cursor.getString( indexDate ),cursor.getString( indexAddr ) );
						//smsList.add( str );
					}
				}
				while( cursor.moveToNext() );


				//Fill the GUI. GUI works with database, not with inbox.
				for( int i = 1  ; i <= this.dataBase.getSize() ; i++)
				{
					auxNode = dataBase.getNodeById(i);
					if(!auxNode.status.equals("archive"))
						smsList.add("ID #"+ auxNode.nodeId     + "\n"+
								"Sender:"    + auxNode.source     + "\n"+
								"Status :"   + auxNode.status     + "\n"+ 
								"Arrived: " +  auxNode.timeArrived+ "\n"+
						"_________________________________");
				}
			}
			//Fill the Archive
			if( v.getId() == R.id.ArchiveList)
			{
				inArchive = true;
				
				
				//Fill the GUI. GUI works with database, not with inbox.
				for( int i = 1  ; i <= this.dataBase.getSize() ; i++)
				{
					auxNode = dataBase.getNodeById(i);
					if(auxNode.status.equals("archive"))
						smsList.add("ID #"+ auxNode.nodeId     + "\n"+
								"Sender:"    + auxNode.source     + "\n"+
								"Status :"   + auxNode.status     + "\n"+ 
								"Arrived: " +  auxNode.timeArrived+ "\n"+
						"_________________________________");
				}
			}
			
		

			
			ListView smsListView = (ListView) findViewById( R.id.SMSList );
			smsListView.setAdapter( new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, smsList) );
			smsListView.setOnItemClickListener( this );
		}
}