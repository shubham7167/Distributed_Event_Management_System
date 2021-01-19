package dsd;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;
import java.rmi.*;
import java.rmi.registry.*; 
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Tor_server extends UnicastRemoteObject implements Event_Interface
{
  
	HashMap<String,HashMap<String,Integer>> TOREvents = new HashMap<>();
	
	HashMap<String,Integer> Conference=new HashMap<>();
	HashMap<String,Integer> TradeShows=new HashMap<>();
	HashMap<String,Integer> Seminars=new HashMap<>();
	
	HashMap<String,ArrayList<String>> Tor_Con=new HashMap<>();
	HashMap<String,ArrayList<String>> Tor_Sem=new HashMap<>();
	HashMap<String,ArrayList<String>> Tor_Ts=new HashMap<>();
	
	ArrayList<String> Manager=new ArrayList<>();
	HashMap<String,ArrayList<Integer>>Counter=new HashMap<>();
	
	Logger log=null;
	FileHandler file;
	
	int port;

	private String msg;
	
public void inimain() {
		
		System.out.println("Inside Toronto Initailize main.....");
		
		try
        {
            
            log=Logger.getLogger("Tor_server");
            file=new FileHandler("C:/Users/patel/eclipse-workspace/dsd/dsd/Logs/Tor_server.log",true);
            log.addHandler(file);
            log.setUseParentHandlers(false);
            SimpleFormatter sf=new SimpleFormatter();
            file.setFormatter(sf);
        
        }catch(IOException e){
        	e.printStackTrace();
        	}
        

		Seminars.put("TORM020119",4);
		Seminars.put("TORA020119",6);
		Seminars.put("TORE020119",8);
		TOREvents.put("SEM",Seminars);
		
		Conference.put("TORM030119",3);
		Conference.put("TORA030119",7);
		Conference.put("TORE030120",5);
		TOREvents.put("CON",Conference);
		
		TradeShows.put("TORM040119",4);
		TradeShows.put("TORA040119",8);
		TradeShows.put("TORE040119",9);
		TOREvents.put("TS",TradeShows);
		
		Manager.add("TORM1234");
		Manager.add("TORM2345");
		Manager.add("TORM3456");
		
		ArrayList<String> a = new ArrayList<>();
		a.add("TORM230519");
		a.add("TORA240519");
		a.add("TORE220519");
		Tor_Con.put("TORC1",a);					//montreal  customer 1
		
		ArrayList<String> b = new ArrayList<>();
		b.add("TORM230519");
		b.add("TORA240519");
		b.add("TORE220519");
		Tor_Sem.put("TORC2",b);
		
		ArrayList<String> c = new ArrayList<>();
		c.add("TORM230519");
		c.add("TORA240519");
		c.add("TORE220519");
		Tor_Ts.put("TORC3",c);
	}
	
	


	private String call_other(String purpose, String customerID,String eventID, String eventType, int port) {
	System.out.println("In call other method");
	String msg="null";
		try {
		
		
		DatagramSocket dgs=new DatagramSocket();
		byte[] mpurpose=new byte[10000];
		byte[] meventID=new byte[10000];
		byte[] meventType=new byte[10000];
		byte[] mcustomerID=new byte[10000];
		
		InetAddress inet = InetAddress.getByName("localhost");
		
		mpurpose=purpose.getBytes();
		meventID=eventID.getBytes();
		meventType=eventType.getBytes();
		mcustomerID=customerID.getBytes();
		String packet1 = new String(new String(mpurpose)+","+new String(meventID)+","+new String(mcustomerID)+","+new String(meventType));
        byte[] packet = packet1.getBytes();
		DatagramPacket dp=new DatagramPacket(packet,packet.length,inet,port);
		dgs.send(dp);
		System.out.println("Packet Sent...");

		byte[] recmsg=new byte[10000];
		DatagramPacket dpr=new DatagramPacket(recmsg,recmsg.length);
		dgs.receive(dpr);
		
	   msg = new String(dpr.getData());
		msg=msg.trim().toString();
		System.out.println("Message received: "+msg );
		return msg;
		
	}
	
	catch(Exception e) {
		System.out.println(e.getMessage());
	}
		return msg;
	}
     //completed...

    private String call_other1(String purpose, String eventID, String eventType, int port) {
		System.out.println("In call other1 method");
		String msg="null";
			try {
			DatagramSocket dgs=new DatagramSocket();
			byte[] mpurpose=new byte[10000];
			byte[] meventID=new byte[10000];
			byte[] meventType=new byte[10000];		
		
			InetAddress inet = InetAddress.getByName("localhost");
			
			mpurpose=purpose.getBytes();
			meventID=eventID.getBytes();
			meventType=eventType.getBytes();
			
			String packet1 = new String(new String(mpurpose)+","+new String(meventID)+","+new String(meventType));
            byte[] packet = packet1.getBytes();
			DatagramPacket dp=new DatagramPacket(packet,packet.length,inet,port);
			dgs.send(dp);
			System.out.println(new String(packet1));
			System.out.println("Packet Sent...");
			
			byte[] recmsg=new byte[10000];
			DatagramPacket dpr=new DatagramPacket(recmsg,recmsg.length);
			dgs.receive(dpr);
		
		   msg = new String(dpr.getData());
		   return msg;
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
				return msg;
	}
    
    void receive() {
    	System.out.println("In toronto receive");
		try {
			String msg="";
			DatagramSocket dgs=new DatagramSocket(5001);

			while(true) {
				byte[] buf = new byte[10000];
					DatagramPacket dp=new DatagramPacket(buf,buf.length);
					dgs.receive(dp);
					String str = new String(dp.getData(),0,dp.getLength());
					String[] receivepacket = str.split(",");
					String purpose = new String(receivepacket[0]);
				     System.out.println(purpose);
				     
					if("BookEvent".equalsIgnoreCase(purpose)) {
						String eventID = new String(receivepacket[1]);
						String customerID = new String(receivepacket[2]);
						String eventType = new String(receivepacket[3]);						
						System.out.println("going to book event");
						msg=book(eventID.trim(),customerID.trim(),eventType.trim());
					 }
					else if("ListEventAvailability".equalsIgnoreCase(purpose)) {
						String eventType = new String(receivepacket[2]);
						System.out.println("going to list availability");
						msg=listEventAvailability1(eventType.trim());
					}
					
					else if("Cancel".equalsIgnoreCase(purpose))
					 {
						String eventID = new String(receivepacket[1]);
						String eventType = new String(receivepacket[2]);						
						System.out.println("going to cancel event");
						msg=cancelEvent1(eventID.trim(),eventType.trim());
					 }
					else if("Remove".equalsIgnoreCase(purpose))
					 {
						String eventID = new String(receivepacket[1]);
						String eventType = new String(receivepacket[2]);						
						System.out.println("going to remove event");
						msg=removeEvent1(eventID.trim(),eventType.trim());
					 }
					
					
					byte[] msgs=new byte[10000];
					msgs=msg.getBytes();
					DatagramPacket dps=new DatagramPacket(msgs,msgs.length,dp.getAddress(),dp.getPort());
					dgs.send(dps);
				}
		}
		
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public String addEvent(String ManagerID, String eventID, String eventType, int bookingCapacity)  throws RemoteException,IOException
	{
		String msg;
		  if(Manager.contains(ManagerID))
		  { 
	        if(TOREvents.get(eventType).containsKey(eventID))
	        {
	        	TOREvents.get(eventType).put(eventID,TOREvents.get(eventType).get(eventID)+bookingCapacity);
	            
	        	msg=eventID+" Event Already Exist...Booking Capacity Updated";
	          
	        }
	        else
	        {
	        	if(eventID.startsWith("TOR"))
	       	    {
	                TOREvents.get(eventType).put(eventID, bookingCapacity); 
	                msg=eventID+" Event Succesfully Added";
	                log.log(Level.INFO, "Manager with ID: {0} Add Event {1}", new Object[]{ManagerID, eventID});

	       	    }
	       	   else
	       		    msg="Enter Valid EventID.";

	        }
		 }
		else 
			msg="This Manager ID is not registered.";
	        
	        return msg;
	}
	

public String removeEvent(String eventID, String eventType) throws RemoteException
 {
	String msg = null;
	System.out.println("in remove event");
	if(eventID.substring(0,3).equalsIgnoreCase("MTL"))
	{
		if(TOREvents.get(eventType).containsKey(eventID)) 
		{
		   if(TOREvents.get(eventType).remove(eventID,TOREvents.get(eventType).get(eventID)))
		     {
			    if(eventType.equalsIgnoreCase("TS")) 
			      {
					System.out.println(Tor_Ts);
					for (Map.Entry<String, ArrayList<String>> entry : Tor_Ts.entrySet()) 
					{
						if(Tor_Ts.get(entry.getKey()).contains(eventID)) 
						 {
							Tor_Ts.get(entry.getKey()).remove(eventID.trim().toString());
						 }					
					}
					System.out.println(Tor_Ts);
					String purpose="Remove";
					String Mtlmsg=call_other1(purpose,eventID,eventType,4001);
					String Otwmsg=call_other1(purpose,eventID,eventType,6001);
					System.out.println(Mtlmsg);
					System.out.println(Otwmsg);

					msg=eventID+" Event has been removed."; 
					System.out.println(msg);
					log.log(Level.INFO, "Event{0} Removed EventType {1}", new Object[]{ eventID, eventType});
					return msg;
				  }
			    else if(eventType.equalsIgnoreCase("SEM"))
			      {
					System.out.println(Tor_Sem);
					for (Map.Entry<String, ArrayList<String>> entry : Tor_Sem.entrySet()) 
					  {
						 if(Tor_Sem.get(entry.getKey()).contains(eventID)) 
						   {
								Tor_Sem.get(entry.getKey()).remove(eventID.trim().toString());
							}
					  }	
				    System.out.println(Tor_Sem);
					String purpose="Remove";
					String Mtlmsg=call_other1(purpose,eventID,eventType,4001);
					String Otwmsg=call_other1(purpose,eventID,eventType,6001);
					System.out.println(Mtlmsg);
					System.out.println(Otwmsg);

					msg=eventID+" Event has been removed."; 
					System.out.println(msg);
					log.log(Level.INFO, "Event{0} Removed EventType {1}", new Object[]{ eventID, eventType});
					return msg;
						
				  }
				else if(eventType.equalsIgnoreCase("CON"))
				  {
						System.out.println(Tor_Con);
						for (Map.Entry<String, ArrayList<String>> entry : Tor_Con.entrySet()) 
						{
							if(Tor_Con.get(entry.getKey()).contains(eventID))
							 {
								Tor_Con.get(entry.getKey()).remove(eventID.trim().toString());
						   	}	
						}
						System.out.println(Tor_Con);
						String purpose="Remove";
						String Mtlmsg=call_other1(purpose,eventID,eventType,4001);
						String Otwmsg=call_other1(purpose,eventID,eventType,6001);
						System.out.println(Mtlmsg);
						System.out.println(Otwmsg);

						msg=eventID+" Event has been removed."; 
						System.out.println(msg);
						log.log(Level.INFO, "Event{0} Removed EventType {1}", new Object[]{ eventID, eventType});
						return msg;
					}
				else
				{
					msg="This event is not registered.";	
					return msg;
				}
			}	
		   else
			{
				msg="This event is not registered.";	
				return msg;
			}
         }
		else
		{
			msg="This event is not registered.";	
			return msg;
		}	}
	
		else {
			msg="Enter the valid Event Id.";
		}
		return msg;
	}
     //completed...
	public String removeEvent1(String eventID, String eventType) throws RemoteException
	{
	  String msg=null;
	  String month=eventID.substring(6,8);
	  int m=Integer.parseInt(month);
	  if(eventType.equalsIgnoreCase("TS"))
	  {
		System.out.println(Tor_Ts);
		for (Map.Entry<String, ArrayList<String>> entry : Tor_Ts.entrySet()) 
		{
			if(Tor_Ts.get(entry.getKey()).contains(eventID)) 
			{
				Tor_Ts.get(entry.getKey()).remove(eventID.trim().toString());
			  System.out.println(Counter);
			  Counter.get(entry.getKey()).set(m-1, Counter.get(entry.getKey()).get(m-1)-1);
			  System.out.println(Counter);
			}					
	    }
		System.out.println(Tor_Ts);
		msg=eventID+"Event has been removed.";
      }
	  else if(eventType.equalsIgnoreCase("SEM")) 
	  {
		System.out.println(Tor_Sem);
		for (Map.Entry<String, ArrayList<String>> entry : Tor_Sem.entrySet()) 
		 {
			if(Tor_Sem.get(entry.getKey()).contains(eventID)) 
			{
				Tor_Sem.get(entry.getKey()).remove(eventID.trim().toString());
				System.out.println(Counter);
				Counter.get(entry.getKey()).set(m-1, Counter.get(entry.getKey()).get(m-1)-1);
				System.out.println(Counter);
			 }
		 }	
		System.out.println(Tor_Sem);	
		msg=eventID+"Event has been removed.";
	  }
	  else if(eventType.equalsIgnoreCase("CON"))
	   {
		 System.out.println(Tor_Con);
		for (Map.Entry<String, ArrayList<String>> entry : Tor_Con.entrySet()) 
		{
			if(Tor_Con.get(entry.getKey()).contains(eventID))
			{
				Tor_Con.get(entry.getKey()).remove(eventID.trim().toString());
				System.out.println(Counter);
				Counter.get(entry.getKey()).set(m-1, Counter.get(entry.getKey()).get(m-1)-1);
				System.out.println(Counter);
			}	
		}
		System.out.println(Tor_Con);
		msg=eventID+"Event has been removed.";
	  }
	else
	{
		msg="Enter valid Event Type.";
	}

	return msg;
}
	@Override
	public String listEventAvailability(String eventType) {
		String rly="";
		String msg="";
		System.out.println("Inside list availability");
		if(eventType.matches("CON"))
		 {
			for(Entry<String, HashMap<String, Integer>> map:TOREvents.entrySet()) 
			  {
				 msg=TOREvents.get("CON").toString();
			  }

			String Mtlevent = call_other1("ListEventAvailability","null",eventType,4001);
			String Otwevent = call_other1("ListEventAvailability","null",eventType,6001);
			
			rly = msg+"\n"+Mtlevent+"\n"+Otwevent;
		 }
		else if(eventType.matches("TS"))
		 {
			for(Entry<String, HashMap<String, Integer>> map1:TOREvents.entrySet())
			  {
				 msg=TOREvents.get("TS").toString();
			  }

			String Mtlevent = call_other1("ListEventAvailability","null",eventType,4001);
			String Otwevent = call_other1("ListEventAvailability","null",eventType,6001);
			
			rly = msg+"\n"+Mtlevent+"\n"+Otwevent;
		 }
		else if(eventType.matches("SEM"))
		 {
			for(Entry<String, HashMap<String, Integer>> map2:TOREvents.entrySet()) 
			  {
				 msg=TOREvents.get("SEM").toString();
		      }

			String Mtlevent = call_other1("ListEventAvailability","null",eventType,4001);
			String Otwevent = call_other1("ListEventAvailability","null",eventType,6001);
			
			rly = msg+"\n"+Mtlevent+"\n"+Otwevent;
		}
		else
		{
			rly="Enter the valid Event Type";
		}

		return rly;
	}
      //completed...
	public String listEventAvailability1(String eventType) {
		String rly="";
		String msg="";
		System.out.println("Inside list availability1");
		if(eventType.matches("CON"))
		 {
			for(Entry<String, HashMap<String, Integer>> map:TOREvents.entrySet()) 
			  {
				 msg=TOREvents.get("CON").toString();
			  }			
			rly = msg;
			System.out.println(rly);
		 }
		else if(eventType.matches("TS"))
		 {
			for(Entry<String, HashMap<String, Integer>> map1:TOREvents.entrySet())
			  {
				 msg=TOREvents.get("TS").toString();
			  }
			rly = msg;
			System.out.println(rly);
		 }
		else if(eventType.matches("SEM"))
		 {
			System.out.println("tor-sem");
			for(Entry<String, HashMap<String, Integer>> map2:TOREvents.entrySet()) 
			  {
				 msg=TOREvents.get("SEM").toString();
		      }
			rly = msg;
			System.out.println(rly);
		}
		else
		{
			rly="Enter the valid Event Type";
		}

		return rly;
	}
	@Override
public String bookEvent(String customerID, String eventID, String eventType) throws RemoteException,IOException
{
	String rly="";
	if(eventID.substring(0,3).equalsIgnoreCase("TOR")) 
	 {
		if(eventID.substring(0,4).equalsIgnoreCase("TORE")||eventID.substring(0,4).equalsIgnoreCase("TORM")|| eventID.substring(0,4).equalsIgnoreCase("TORA"))
		{
			if(customerID.substring(0,4).equalsIgnoreCase("TORC"))
			{
				if(eventType.equalsIgnoreCase("CON")) 
				{
					if(alreadyPresent(customerID,eventID,eventType))
					{
						rly="You have already book the event";
						return rly;
					}
					else 
					{
						if(TOREvents.get(eventType).containsKey(eventID)) 
						{
							int eve=TOREvents.get(eventType).get(eventID);
							if(eve>0) 
							{
									Tor_Con.putIfAbsent(customerID, new ArrayList<>());
									Tor_Con.get(customerID).add(eventID);
									TOREvents.get(eventType).put(eventID,TOREvents.get(eventType).get(eventID)-1);
									rly="Booking for "+eventID+" is succesful for the " +customerID;
                                    log.log(Level.INFO, "Customer with ID: {0} book in Event {1} For {2}", new Object[]{customerID, eventID, eventType});

									return rly;
							}
							else
							{
								rly="This event is full.";
							}
						}
						else {
							rly="enter valid event id";
							return rly;
							}
					}
					
				}
				else if(eventType.equalsIgnoreCase("TS")) 
				{
					if(alreadyPresent(customerID,eventID,eventType))
					{
						rly="You have already book the event";
						return rly;
					}
					else {
						if(TOREvents.get(eventType).containsKey(eventID)) 
						{
							int eve=TOREvents.get(eventType).get(eventID);
							if(eve>0) {
									Tor_Ts.putIfAbsent(customerID, new ArrayList<>());
									Tor_Ts.get(customerID).add(eventID);
									TOREvents.get(eventType).put(eventID,TOREvents.get(eventType).get(eventID)-1);
									rly="Booking for "+eventID+" is succesful for the " +customerID;
									return rly;
								}
							else
							{
								rly="This event is full.";
							}
							}
						else {
							rly="enter valid event id";
							return rly;
						 }
						}
					return rly;
					}
				else if(eventType.equalsIgnoreCase("SEM"))
				{
					if(alreadyPresent(customerID,eventID,eventType)){
						rly="You have already book the event";
						return rly;
					}
					else {
						if(TOREvents.get(eventType).containsKey(eventID)) {
							int eve=TOREvents.get(eventType).get(eventID);
							if(eve>0) {
									Tor_Sem.putIfAbsent(customerID, new ArrayList<>());
									Tor_Sem.get(customerID).add(eventID);
									TOREvents.get(eventType).put(eventID,TOREvents.get(eventType).get(eventID)-1);
									rly="Booking for "+eventID+" is succesful for the " +customerID;
									return rly;
								}
							else
							{
								rly="This event is full.";
							}
						}
						else {
							rly="enter valid event id";
							return rly;
						}
					}
					
				}
				else
				{
					rly="Enter valid Event Type.";
					return rly;
				}
		}
	else
	{
		rly="Enter valid Customer ID";
		return rly;
	}
  }
		else {
			rly="Enter valid Event ID.";
		}
		return rly;
	
}
	else if(eventID.substring(0,3).equalsIgnoreCase("OTW") || (eventID.substring(0,3).equalsIgnoreCase("MTL"))) 
	{
			System.out.println("going to different Server 3");
			Counter.putIfAbsent(customerID,new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0)));
			String month=eventID.substring(6,8);
			int m=Integer.parseInt(month);
			int count=Counter.get(customerID).get(m-1);
			System.out.println(Counter);
			if(count>2) {
				rly="You have already registered in 3 evenTs outside Toronto.";
				return rly;
			}
			if(eventID.charAt(0)=='O') {
				port=6001;
				String purpose="BookEvent";
				System.out.println("Sending request from ottawa to montreal");
				String msg=call_other(purpose,customerID,eventID,eventType,port);
				System.out.println(msg);
				if(msg.equalsIgnoreCase("Enrolled")) 
				{
					System.out.println("Enroll: "+msg);
					Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)+1);
					System.out.println(Counter);
					if(eventType.equalsIgnoreCase("SEM")) {
						Tor_Sem.putIfAbsent(customerID, new ArrayList<>());
						Tor_Sem.get(customerID).add(eventID);
						System.out.println(Tor_Sem);
					}
						if(eventType.equalsIgnoreCase("CON")) {
							Tor_Con.putIfAbsent(customerID, new ArrayList<>());
							Tor_Con.get(customerID).add(eventID);
							System.out.println(Tor_Con);
						}
							if(eventType.equalsIgnoreCase("TS")) {
								Tor_Ts.putIfAbsent(customerID, new ArrayList<>());
								Tor_Ts.get(customerID).add(eventID);
								System.out.println(Tor_Ts);
							}
							rly="Booking for "+eventID+" is succesful for the " +customerID;
							return rly;	
				}
				else {
					rly="Error in booking..Try again.";
					return rly;
				}
				
			}
			if(eventID.charAt(0)=='M') {
				port=4001;
				String purpose="BookEvent";
				System.out.println("Sending request from ottawa to montreal");
				String msg=call_other(purpose,customerID,eventID,eventType,port);
				System.out.println(msg);
				if(msg.equalsIgnoreCase("Enrolled")) {
					System.out.println("Enroll: "+msg);
					Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)+1);
					System.out.println(Counter);
					if(eventType.equalsIgnoreCase("SEM")) {
						Tor_Sem.putIfAbsent(customerID, new ArrayList<>());
						Tor_Sem.get(customerID).add(eventID);
						System.out.println(Tor_Sem);
					}
						if(eventType.equalsIgnoreCase("CON")) {
							Tor_Con.putIfAbsent(customerID, null);
							Tor_Con.get(customerID).add(eventID);
							System.out.println(Tor_Con);
						}
							if(eventType.equalsIgnoreCase("TS")) {
								Tor_Ts.putIfAbsent(customerID, null);
								Tor_Ts.get(customerID).add(eventID);
								System.out.println(Tor_Ts);
							}
							rly="Booking for "+eventID+" is succesful for the " +customerID;
							return rly;	
				}
				else {
					rly="Error in booking..Try again.";
					return rly;
				}
				
			}
		}
		else {
			rly="Enter valid Event id";
			return rly;
		     }
			rly="Enter valid Event id";
			return rly;
	}
	private String book(String eventID, String customerID,String eventType) {
		String rly="";
		System.out.println("In tornoto book Event");
				
				if(eventType.equalsIgnoreCase("CON")) {
					if(alreadyPresent(customerID,eventID,eventType)){
						rly="You have already book the event";
						return rly;
					}
					else {
						if(TOREvents.get(eventType).containsKey(eventID)) {
							int eve=TOREvents.get(eventType).get(eventID);
							if(eve>0) {
									Tor_Con.putIfAbsent(customerID, new ArrayList<>());
									Tor_Con.get(customerID).add(eventID);
									TOREvents.get(eventType).put(eventID,TOREvents.get(eventType).get(eventID)-1);
									rly="Enrolled";
									System.out.println(TOREvents);
									return rly;
							}
							else
							{
								rly="This event is full.";
								return rly;
							}
						}
						else
						{
							rly="Enter valid Event ID";
							return rly;
						}
						
					}
				}
				if(eventType.equalsIgnoreCase("TS")) {
					if(alreadyPresent(customerID,eventID,eventType)){
						rly="You have already book the event";
						return rly;
					}
					else {
						if(TOREvents.get(eventType).containsKey(eventID)) {
							int eve=TOREvents.get(eventType).get(eventID);
							if(eve>0) {
									Tor_Con.putIfAbsent(customerID, new ArrayList<>());
									Tor_Con.get(customerID).add(eventID);
									TOREvents.get(eventType).put(eventID,TOREvents.get(eventType).get(eventID)-1);
									rly="Enrolled";
									System.out.println(TOREvents);
									return rly;
							}
							else
							{
								rly="This event is full.";
								return rly;
							}
						}
						else
						{
							rly="Enter valid Event ID";
							return rly;
						}
						
					}
					
				}
				if(eventType.equalsIgnoreCase("SEM")) {
					if(alreadyPresent(customerID,eventID,eventType)){
						rly="You have already book the event";
						return rly;
					}
					else {
						if(TOREvents.get(eventType).containsKey(eventID)) {
							int eve=TOREvents.get(eventType).get(eventID);
							if(eve>0) {
									Tor_Con.putIfAbsent(customerID, new ArrayList<>());
									Tor_Con.get(customerID).add(eventID);
									TOREvents.get(eventType).put(eventID,TOREvents.get(eventType).get(eventID)-1);
									rly="Enrolled";
									System.out.println(TOREvents);
									return rly;
							}
							else
							{
								rly="This event is full.";
								return rly;
							}
						}
						else
						{
							rly="Enter valid Event ID";
							return rly;
						}
						
					}
					
				}
				else
					rly="Enter valid Event Type.";	
		return rly;
	}
	
	private boolean alreadyPresent(String customerID, String eventID, String eventType) {
		for(Map.Entry<String, ArrayList<String>> pair:Tor_Con.entrySet()) {
			if(pair.getKey().equals(customerID)&&pair.getValue().contains(eventID)) {
				return true;
			}
		}
		for(Map.Entry<String, ArrayList<String>> pair : Tor_Ts.entrySet()) {
			if(pair.getKey().equals(customerID)&&pair.getValue().contains(eventID)) {
				return true;
			}
		}
		for(Map.Entry<String, ArrayList<String>> pair : Tor_Sem.entrySet()) {
				if(pair.getKey().equals(customerID)&&pair.getValue().contains(eventID)) {
					return true;
				}
		}
			return false;
	}
     //completed... 
	public String getBookingSchedule(String customerID) throws RemoteException
	{
		
		    String msg1="Events booked by "+customerID+"\n" +"Conference Events: "+Tor_Con.get(customerID)+ "\n" +
				"Seminar Events: " +Tor_Sem.get(customerID)+"\n"+
		    	"Trade Show Events"	+Tor_Ts.get(customerID);
		    
		    return msg1;
		
	}

	@Override
	public String cancelEvent(String customerID, String eventID, String eventType)
	{
		String msg="";
	if(Tor_Sem.containsKey(customerID)||Tor_Ts.containsKey(customerID)||Tor_Con.containsKey(customerID)) 
	{
	   if(eventID.charAt(0)=='O')
       {   
           port=6001;
           String purpose="Cancel";  
			String rly=call_other1(purpose,eventID,eventType,port);
			System.out.println(rly);
			if(rly.trim().toString().equalsIgnoreCase("Y")){      	   
				String month=eventID.substring(6,8);
				int m=Integer.parseInt(month);
				if(eventType.equalsIgnoreCase("SEM")) {
					Tor_Sem.get(customerID).remove(eventID);
				}
				else if(eventType.equalsIgnoreCase("CON")) {
					Tor_Con.get(customerID).remove(eventID);
				}
				else if(eventType.equalsIgnoreCase("TS")) {
					Tor_Ts.get(customerID).remove(eventID);	
				}
					Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)-1);
					System.out.println(Counter);
					msg=eventID+" Event has been removed.";
                    log.log(Level.INFO, "Customer with ID: {0} cancel in Event {1} For {2}", new Object[]{customerID, eventID, eventType});

				}
          else{
        	  msg="This eventID is not available in toronto..Enter valid event ID ";    
               }       
        }
	  else if(eventID.charAt(0)=='M')
        {   
          port=4001;
          String purpose="Cancel";  
      				String rly=call_other1(purpose,eventID,eventType,port);
      				System.out.println(rly);
      				if(rly.trim().toString().equalsIgnoreCase("Y")){      	   
      					String month=eventID.substring(6,8);
      					int m=Integer.parseInt(month);
      					if(eventType.equalsIgnoreCase("SEM")) {
      						Tor_Sem.get(customerID).remove(eventID);
      					}
      					else if(eventType.equalsIgnoreCase("CON")) {
      						Tor_Con.get(customerID).remove(eventID);
      					}
      					else if(eventType.equalsIgnoreCase("TS")) {
      						Tor_Ts.get(customerID).remove(eventID);	
      					}
      						Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)-1);
      						System.out.println(Counter);
      						msg=eventID+" Event has been removed.";
      					}
      	          else{
      	     	      msg="This eventID is not in ottawa ";     
      	               }          
        }
    else if(eventID.charAt(0)=='T')
    {
       System.out.println("Inside if");
       if(TOREvents.get(eventType).containsKey(eventID))
       {   
    	   if(eventType.equalsIgnoreCase("CON")) 
    	   {
           	   if(Tor_Con.containsKey(customerID)){ 
           		   if(Tor_Con.get(customerID).contains(eventID)) {
     	           Tor_Con.get(customerID).remove(eventID);
     	           TOREvents.get(eventType).put(eventID,TOREvents.get(eventType).get(eventID)+1);
     	          msg=eventID+" Event has been removed.";  
           		   }
           		else {
     			   msg="You haven't booked this event.";
     			   }
     	   	}
     	   else {
     		   msg="You haven't booked this event.";
               }
           }
      
    	   else if(eventType.equalsIgnoreCase("TS"))
    	   {
           	   if(Tor_Ts.containsKey(customerID)){ 
           		   if(Tor_Ts.get(customerID).contains(eventID)) 
           		   {
           			   Tor_Ts.get(customerID).remove(eventID);
           			   TOREvents.get(eventType).put(eventID,TOREvents.get(eventType).get(eventID)+1);
           			msg=eventID+" Event has been removed.";
           		   }
           		else {
     			   msg="You haven't booked this event.";
     			   }
     	   	}
     	   else {
     		   msg="You haven't booked this event.";
               }
        }
         else if(eventType.equalsIgnoreCase("SEM"))
         {
        	   if(Tor_Sem.containsKey(customerID))
        	   { 
        		   if(Tor_Sem.get(customerID).contains(eventID)) 
        		   {
        			   Tor_Sem.get(customerID).remove(eventID);
        			   TOREvents.get(eventType).put(eventID,TOREvents.get(eventType).get(eventID)+1);
        			   msg=eventID+" Event has been removed.";
        		   }
        		   else {
        			   msg="You haven't booked this event.";
        			   }
        	   	}
        	   else {
        		   msg="You haven't booked this event.";
                  }
       }
         else {
        	 msg="Enter valid Event Id.";
         }
       	}
       else {
       msg="EVENT DOESN'T EXIST..Enter valid Event Id.";
       }
	}
   else
   {
	   msg="Enter valid EventID";
   }

	}
	else {
	msg="You haven't booked this event.";
	}
	return msg;
}
	
	public String cancelEvent1(String eventID, String eventType)
	{
		String msg;
 	   System.out.println("Inside cancel 1");
        if(TOREvents.get(eventType).containsKey(eventID))
        {   
      	           TOREvents.get(eventType).put(eventID,TOREvents.get(eventType).get(eventID)+1); 
      	           System.out.println(TOREvents);
  	              msg="Y"; 
	                   return msg;
	             }
	    
	     else
	           {          
	             msg="N";                        
	             return msg;
	           }
	}


  public static void main(String[] args) 	throws RemoteException, AlreadyBoundException 
   {
	  Registry torreg=LocateRegistry.createRegistry(5555);
		Tor_server ms=new Tor_server();
		torreg.rebind("ks_tor",ms);
		ms.inimain();      
   }
  
	protected Tor_server() throws RemoteException {
		super();
		Thread t1=new Thread(()->{
		receive();
		});
		t1.start();
	}
}
