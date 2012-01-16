package code.sas_server.com;

import java.util.Date;


//Data Base.
//There is number of functions that are not in use, add to archive, get from it
//Can be useful for additional buttons.
public class SAS_Server_DB 
{
	
	
	//Main data base.
    private EmergencyNode head;
    private EmergencyNode tail;
    
  
    //sizes.
    private int           size;

     
    //constructor
    public SAS_Server_DB()
    {   	
    	size        = 0;
    }
    
    //return number of active emergencies
    public int getSize()
    {
    	return size;
    }
    
   
   
    public void addNode(String body, String time,String source)
    {
    	EmergencyNode node = new EmergencyNode();
    	EmergencyNode aux;
    	
    	//Parse time
    	 Date d = new Date();
		 d.setTime(Long.parseLong(time)) ;
		
    	
		 
    	node.text          = body;
    	node.source        = source;
    	node.status        = "active";
    	node.timeArrived   = d.toLocaleString();
    	
    	//check if such case not existed
    	aux  = this.head;
    	for( ; aux != null; aux = aux.next)
    	{
    		if(aux.timeArrived.equals(node.timeArrived)  && aux.source.equals(node.source)  )       //Already exist
    			return;
    	}
    	
    	//check in archive
    
    	
    	if(head == null)               //list is empty.
    	{
    		head = tail = node;
    		size = 1;
    		node.nodeId = 1;
    	}
    	else                           //list is not empty.
    	{
    		size++;
    		node.nodeId = size;
    		tail.next = node;
    		tail = node;	
    	}
    }
    
   
    //
    public EmergencyNode getNodeById(int id)
    {
    	if((id < 1) || (id > size))
    		return null;
    	
    	EmergencyNode auxCurr = this.head;
    	
    	
    	for( int i = 1; auxCurr != null; i++ )
    	{
    		if(auxCurr.nodeId == id)
    		 return new EmergencyNode(auxCurr);
    		auxCurr = auxCurr.next;
    	}
    	
    	return (null);
    }
    //----------------------------------------
    public EmergencyNode getRealNodeById(int id)
    {
    	if((id < 1) || (id > size))
    		return null;
    	
    	EmergencyNode auxCurr = this.head;
    	
    	
    	for( int i = 1; auxCurr != null; i++ )
    	{
    		if(auxCurr.nodeId == id)
    		 return auxCurr;
    		auxCurr = auxCurr.next;
    	}
    	
    	return (null);
    }
    
    //get node number x,not Id x.
    public EmergencyNode getNode(int number)
    {
    	if((number < 1) || (number > size))
    		return null;
    	
    	EmergencyNode auxCurr = this.head;
    	
    	
    	for( int i = 1; i < number; i++ )
    	{
    		
    		auxCurr = auxCurr.next;
    	}
    	
    	return (new EmergencyNode(auxCurr));
    }
    
   
   
	
	//Class represents single situation, Node for linked list
	public class EmergencyNode
	{
		EmergencyNode next;
		
		int    nodeId;
		String timeArrived;
		String status,
		       source,
		       altitude , longitude,
		       text,
		       accuracy;
		
		//c-tor
		public EmergencyNode()
		{
			nodeId      = 0;
			next        = null;
			timeArrived = "";
			status      = "unknown";
		}
		
		//copy c-tor
		public EmergencyNode(EmergencyNode node)
		{
			nodeId      = node.nodeId;
			next        = node.next;
			timeArrived = node.timeArrived;
			status      = node.status;
			altitude    = node.altitude;
			longitude   = node.longitude;
			text        = node.text;
			source      = node.source;
		}
		
		
	}
}


