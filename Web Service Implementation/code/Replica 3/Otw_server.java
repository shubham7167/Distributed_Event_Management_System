package Helloapl;
import org.omg.CORBA.INITIALIZE;
import org.omg.CORBA.ORB;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import dsd.Event_Interface;

public class Otw_server
{
	private ORB orb;
	static int unique_id = 0;
	HashMap<String,HashMap<String,Integer>> OTWEvents = new HashMap<>();
	
	HashMap<String,Integer> Conference=new HashMap<>();
	HashMap<String,Integer> TradeShows=new HashMap<>();
	HashMap<String,Integer> Seminars=new HashMap<>();
	
	HashMap<String,ArrayList<String>> Otw_Con=new HashMap<>();
	HashMap<String,ArrayList<String>> Otw_Sem=new HashMap<>();
	HashMap<String,ArrayList<String>> Otw_Ts=new HashMap<>();
	
	ArrayList<String> Manager=new ArrayList<>();
	HashMap<String,ArrayList<Integer>>Counter=new HashMap<>();
	
	Logger log=null;
	FileHandler file;
	
	int port;

	private String msg;
	
public void inimain() {
		
		System.out.println("Inside Ottawa Initailize main.....");
	/*	try
        {
            
            log=Logger.getLogger("Otw_server");
            file=new FileHandler("D:/Shubham/Concordia/Distributed System/assignment/dsd/dsd/Logs/Otw_server.log",true);
            log.addHandler(file);
            log.setUseParentHandlers(false);
            SimpleFormatter sf=new SimpleFormatter();
            file.setFormatter(sf);
        
        }catch(IOException e){
        	e.printStackTrace();
        	}
        
		*/
		Seminars.put("OTWM030119",4);
		Seminars.put("OTWA030119",6);
		Seminars.put("OTWE030119",4);
		OTWEvents.put("SEM",Seminars);
		
		Conference.put("OTWM040119",5);
		Conference.put("OTWA040119",7);
		Conference.put("OTWE040119",3);
		OTWEvents.put("CON",Conference);
		
		TradeShows.put("OTWM050119",3);
		TradeShows.put("OTWA050119",6);
		TradeShows.put("OTWE050119",7);
		OTWEvents.put("TS",TradeShows);
		
		Manager.add("OTWM1234");
		Manager.add("OTWM2345");
		Manager.add("OTWM3456");
		Manager.add("OTWM9000");
		Manager.add("OTWM6785");
		
		ArrayList<String> a = new ArrayList<>();
		a.add("OTWM230519");
		a.add("OTWA240519");
		a.add("OTWE220519");
		Otw_Con.put("OTWC1",a);					
		
		ArrayList<String> b = new ArrayList<>();
		b.add("OTWM230519");
		b.add("OTWA240519");
		b.add("OTWE220519");
		Otw_Sem.put("OTWC2",b);
		
		ArrayList<String> c = new ArrayList<>();
		c.add("OTWM230519");
		c.add("OTWA240519");
		c.add("OTWE220519");
		Otw_Ts.put("OTWC3",c);
		
		
	}

private String call_other(String purpose, String customerID,String eventID, String eventType, int port) {
	System.out.println("In call other method");
	String msg="NULL";
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


private String call_other1(String purpose, String eventID, String eventType, int port) {
	System.out.println("In call other1 method");
	String msg="NULL";
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
		System.out.println("In ottawa receive");
		try {
			String msg="";

			DatagramSocket dgs=new DatagramSocket(6001);
			while(true) {
				byte[] buf=new byte[10000];
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
				else if("Remove".equalsIgnoreCase(purpose)){
					String eventID = new String(receivepacket[1]);
					String eventType = new String(receivepacket[2]);						
					System.out.println("going to remove event event");
					msg=RemoveEvent1(eventID.trim(),eventType.trim());
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


	
	public String addEvent(String ManagerID, String eventID, String eventType, int bookingCapacity)  
	{
		String msg;
		if(Manager.contains(ManagerID))
		  {  
	        if(OTWEvents.get(eventType).containsKey(eventID))
	        {
	        	OTWEvents.get(eventType).put(eventID,OTWEvents.get(eventType).get(eventID)+bookingCapacity);
	            
	        	msg="EVENT ADDED SUCCESSFULLY.";
	          
	        }
	        else
	        {
	        	if(eventID.startsWith("OTW"))
	       	     {
	                OTWEvents.get(eventType).put(eventID, bookingCapacity); 
	                msg="EVENT ADDED SUCCESSFULLY.";
	                //log.log(Level.INFO, "Manager with ID: {0} Add Event {1}", new Object[]{ManagerID, eventID});
	       	     }   
	            else
	        		msg="ENTER VALID EVENTID.";
	        }
		  }
		else 
			msg="THIS MANAGERID IS NOT REGISTERED.";
	        
	        return msg;
	}
 

public String removeEvent(String eventID, String eventType) 
{
	 String msg = null;
	System.out.println("in remove event");
		if(eventID.substring(0,3).equalsIgnoreCase("MTL"))
		{
			if(OTWEvents.get(eventType).containsKey(eventID))
			{
				if(OTWEvents.get(eventType).remove(eventID,OTWEvents.get(eventType).get(eventID)))
				{
					if(eventType.equalsIgnoreCase("TS"))
					 {
						System.out.println(Otw_Ts);
						for (Map.Entry<String, ArrayList<String>> entry : Otw_Ts.entrySet())
						{
							if(Otw_Ts.get(entry.getKey()).contains(eventID)) {
								Otw_Ts.get(entry.getKey()).remove(eventID.trim().toString());
						}					
					 }
						System.out.println(Otw_Ts);
						String purpose="Remove";
						String Mtlmsg=call_other1(purpose,eventID,eventType,4001);
						String Tormsg=call_other1(purpose,eventID,eventType,5001);
						System.out.println(Mtlmsg);
						System.out.println(Tormsg);

						msg="EVENT HAS BEEN REMOVED."; 
						System.out.println(msg);
						//log.log(Level.INFO, "Event{0} Removed EventType {1}", new Object[]{ eventID, eventType});
						return msg;
				}
					else if(eventType.equalsIgnoreCase("SEM"))
					 {
						System.out.println(Otw_Sem);
						for (Map.Entry<String, ArrayList<String>> entry : Otw_Sem.entrySet())
						{
							if(Otw_Sem.get(entry.getKey()).contains(eventID)) 
							 {
								Otw_Sem.get(entry.getKey()).remove(eventID.trim().toString());
							  }
						}	
						System.out.println(Otw_Sem);
						String purpose="Remove";
						String Mtlmsg=call_other1(purpose,eventID,eventType,4001);
						String Tormsg=call_other1(purpose,eventID,eventType,5001);
						System.out.println(Mtlmsg);
						System.out.println(Tormsg);

						msg="EVENT HAS BEEN REMOVED."; 
						System.out.println(msg);
						//log.log(Level.INFO, "Event{0} Removed EventType {1}", new Object[]{ eventID, eventType});
						return msg;
						
					}
					else if(eventType.equalsIgnoreCase("CON"))
					{
						System.out.println(Otw_Con);
						for (Map.Entry<String, ArrayList<String>> entry : Otw_Con.entrySet()) 
						{
							if(Otw_Con.get(entry.getKey()).contains(eventID)) 
							{
								Otw_Con.get(entry.getKey()).remove(eventID.trim().toString());
							}	
						}
						System.out.println(Otw_Con);
						String purpose="Remove";
						String Mtlmsg=call_other1(purpose,eventID,eventType,4001);
						String Tormsg=call_other1(purpose,eventID,eventType,5001);
						System.out.println(Mtlmsg);
						System.out.println(Tormsg);

						msg="EVENT HAS BEEN REMOVED."; 
						System.out.println(msg);
						//log.log(Level.INFO, "Event{0} Removed EventType {1}", new Object[]{ eventID, eventType});
						return msg;
					}
					else
					{
						msg="ENTER THE VALID EVENTID.";	
						return msg;
					}
				}
				else
				{
					msg="THIS EVENT IS NOT REGISTERED.";	
					return msg;
				}
             }
			else
			{
				msg="THIS EVENT IS NOT REGISTERED.";	
				return msg;
			}
		}
	
		else {
			msg="ENTER THE VALID EVENTID.";
			return msg;
		}
	}

	private String RemoveEvent1(String eventID, String eventType) 
	{
		String msg=null;
		String month=eventID.substring(6,8);
		int m=Integer.parseInt(month);
		if(eventType.equalsIgnoreCase("TS")) {
			System.out.println(Otw_Ts);
			for (Map.Entry<String, ArrayList<String>> entry : Otw_Ts.entrySet()) {
				if(Otw_Ts.get(entry.getKey()).contains(eventID)) {
				Otw_Ts.get(entry.getKey()).remove(eventID.trim().toString());
				System.out.println(Counter);
				Counter.get(entry.getKey()).set(m-1, Counter.get(entry.getKey()).get(m-1)-1);
				System.out.println(Counter);
				}					
		}
			System.out.println(Otw_Ts);
			msg="EVENT HAS BEEN REMOVED.";
	}
		else if(eventType.equalsIgnoreCase("SEM")) {
			System.out.println(Otw_Sem);
			for (Map.Entry<String, ArrayList<String>> entry : Otw_Sem.entrySet()) {
				if(Otw_Sem.get(entry.getKey()).contains(eventID)) {
					Otw_Sem.get(entry.getKey()).remove(eventID.trim().toString());
					System.out.println(Counter);
					Counter.get(entry.getKey()).set(m-1, Counter.get(entry.getKey()).get(m-1)-1);
					System.out.println(Counter);
					}
				}	
			System.out.println(Otw_Sem);
			msg="EVENT HAS BEEN REMOVED.";
			
		}
		else if(eventType.equalsIgnoreCase("CON")){
			System.out.println(Otw_Con);
			for (Map.Entry<String, ArrayList<String>> entry : Otw_Con.entrySet()) {
				if(Otw_Con.get(entry.getKey()).contains(eventID)) {
					Otw_Con.get(entry.getKey()).remove(eventID.trim().toString());
					System.out.println(Counter);
					Counter.get(entry.getKey()).set(m-1, Counter.get(entry.getKey()).get(m-1)-1);
					System.out.println(Counter);
				}	
			}
			System.out.println(Otw_Con);
			msg="EVENT HAS BEEN REMOVED.";
		}
		else
		{
			msg="ENTER THE VALID EVENTTYPE.";
		}

		return msg;
	}
	

	public String listEventAvailability(String eventType) {
		String rly="";
		String msg="";
		System.out.println("Inside list availability");
		if(eventType.matches("CON"))
		  {
			for(Entry<String, HashMap<String, Integer>> map:OTWEvents.entrySet()) 
			  {
				 msg=OTWEvents.get("CON").toString();
			  }
			
			String Torevent = call_other1("ListEventAvailability","null",eventType,5001);
			String Mtlevent = call_other1("ListEventAvailability","null",eventType,4001);
			
			rly = msg+"\n"+Torevent+"\n"+Mtlevent;
		  }
		else if(eventType.matches("TS"))
		  {
			for(Entry<String, HashMap<String, Integer>> map1:OTWEvents.entrySet()) 
			  {
				 msg=OTWEvents.get("TS").toString();
			  }
			
			String Torevent = call_other1("ListEventAvailability","null",eventType,5001);
			String Mtlevent = call_other1("ListEventAvailability","null",eventType,4001);
			
			rly = msg+"\n"+Torevent+"\n"+Mtlevent;
		  }
		else if(eventType.matches("SEM"))
		  {
			for(Entry<String, HashMap<String, Integer>> map2:OTWEvents.entrySet()) 
			    {
				 msg=OTWEvents.get("SEM").toString();
				 System.out.println(msg);
           		}
			
			String Torevent = call_other1("ListEventAvailability","null",eventType,5001);
			String Mtlevent = call_other1("ListEventAvailability","null",eventType,4001);
			
			rly = msg+"\n"+Torevent+"\n"+Mtlevent;
		  }
		else
		{
			rly="ENTER THE VALID EVENTTYPE.";
		}
		
		return rly;
	}

	private String listEventAvailability1(String eventType) {
		String rly="";
		String msg="";
		System.out.println("Inside list availability1");
		if(eventType.matches("CON"))
		  {
			for(Entry<String, HashMap<String, Integer>> map:OTWEvents.entrySet()) 
			  {
				 msg=OTWEvents.get("CON").toString();
			  }
						
			rly = msg;
			System.out.println(rly);
		  }
		else if(eventType.matches("TS"))
		  {
			for(Entry<String, HashMap<String, Integer>> map1:OTWEvents.entrySet()) 
			  {
				 msg=OTWEvents.get("TS").toString();
			  }
			
			rly = msg;
			System.out.println(rly);
		  }
		else if(eventType.matches("SEM"))
		  {
			System.out.println("otw-sem");
			for(Entry<String, HashMap<String, Integer>> map2:OTWEvents.entrySet()) 
			    {
				 msg=OTWEvents.get("SEM").toString();
           		}
						
			rly = msg;
			System.out.println(rly);
		  }
		else
		{
			rly="ENTER THE VALID EVENTTYPE.";
		}
		
		return rly;
	}

	
public String bookEvent(String customerID, String eventID, String eventType)
{
  String rly="";
  System.out.println("in book event");
  if(eventID.substring(0,3).equalsIgnoreCase("OTW")) 
  {
	if(eventID.substring(0,4).equalsIgnoreCase("OTWE")||eventID.substring(0,4).equalsIgnoreCase("OTWM")|| eventID.substring(0,4).equalsIgnoreCase("OTWA"))
	 {
		if(customerID.substring(0,4).equalsIgnoreCase("OTWC")) 
		 {
			if(eventType.equalsIgnoreCase("CON"))
			 {
					if(alreadyPresent(customerID,eventID,eventType))
					{
						rly="YOU HAVE ALREADY BOOKED THIS EVENT.";
						return rly;
					}
					else
					{
						if(OTWEvents.get(eventType).containsKey(eventID)) 
						{
							int eve=OTWEvents.get(eventType).get(eventID);
							if(eve>0) 
							 {
									Otw_Con.putIfAbsent(customerID, new ArrayList<>());
									Otw_Con.get(customerID).add(eventID);
									OTWEvents.get(eventType).put(eventID,OTWEvents.get(eventType).get(eventID)-1);
									System.out.println(Otw_Con);
									System.out.println(OTWEvents);
									rly="BOOKING IS SUCCESSFUL.";
                                  //  log.log(Level.INFO, "Customer with ID: {0} book in Event {1} For {2}", new Object[]{customerID, eventID, eventType});

									return rly;
									
								}
							else
							{
								rly="THIS EVENT IS FULL.";
								return rly;
							}
						}
						else 
						{
							rly="ENTER THE VALID EVENTID.";
							return rly;
						}
					}
				}
				if(eventType.equalsIgnoreCase("TS")) {
					System.out.println("in ts");
					if(alreadyPresent(customerID,eventID,eventType)){
						rly="YOU HAVE ALREADY BOOKED THIS EVENT.";
						return rly;
					}
					else {
						if(OTWEvents.get(eventType).containsKey(eventID)) {
							int eve=OTWEvents.get(eventType).get(eventID);
							if(eve>0) {
									Otw_Ts.putIfAbsent(customerID, new ArrayList<>());
									Otw_Ts.get(customerID).add(eventID);
									OTWEvents.get(eventType).put(eventID,OTWEvents.get(eventType).get(eventID)-1);
									System.out.println(Otw_Ts);
									System.out.println(OTWEvents);
									rly="BOOKING IS SUCCESSFUL.";
									return rly;
							}
							else
							{
								rly="THIS EVENT IS FULL.";
								return rly;
							}
						}
						else {
							rly="ENTER THE VALID EVENTID.";
							return rly;
					}
				}
				}
				if(eventType.equalsIgnoreCase("SEM")) {
					if(alreadyPresent(customerID,eventID,eventType)){
						System.out.println("hey4");
						rly="YOU HAVE ALREADY BOOKED THIS EVENT.";
						return rly;
					}
					else {
						System.out.println("hey5");
						if(OTWEvents.get(eventType).containsKey(eventID))
						{
							System.out.println("hey6");
							int eve=OTWEvents.get(eventType).get(eventID);
							if(eve>0) {
								System.out.println("hey7");
									Otw_Sem.putIfAbsent(customerID, new ArrayList<>());
									Otw_Sem.get(customerID).add(eventID);
									OTWEvents.get(eventType).put(eventID,OTWEvents.get(eventType).get(eventID)-1);
									System.out.println(Otw_Sem);
									System.out.println(OTWEvents);
									rly="BOOKING IS SUCCESSFUL.";
									return rly;
							}
							else
							{
								rly="THIS EVENT IS FULL.";
								return rly;
							}
						}
						else 
						{
							rly="ENTER THE VALID EVENTID.";
							return rly;
						}
					}					
				}
				else
				{
					rly="ENTER THE VALID EVENTID.";
					return rly;
				}
			}
			else 
			{
				rly="ENTER THE VALID CUSTOMERID.";
				return rly;
			}
		}
			else {
				rly="ENTER THE VALID EVENTID.";
				return rly;
			}		
	}
  else	if(eventID.substring(0,3).equalsIgnoreCase("MTL") || (eventID.substring(0,3).equalsIgnoreCase("TOR"))) {
			
			System.out.println("going to different Server 3");
			Counter.putIfAbsent(customerID,new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0)));
			String month=eventID.substring(6,8);
			int m=Integer.parseInt(month);
			int count=Counter.get(customerID).get(m-1);
			System.out.println(count);
			System.out.println(Counter);
			if(count>2) {
				rly="YOU HAVE ALREADY REGISTERED 3 EVENTS OUTSIDE OTTAWA.";
				return rly;
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
						Otw_Sem.putIfAbsent(customerID, new ArrayList<>());
						Otw_Sem.get(customerID).add(eventID);
						System.out.println(Otw_Sem);
					}
						if(eventType.equalsIgnoreCase("CON")) {
							Otw_Con.putIfAbsent(customerID, new ArrayList<>());
							Otw_Con.get(customerID).add(eventID);
							System.out.println(Otw_Con);
						}
							if(eventType.equalsIgnoreCase("TS")) {
								Otw_Ts.putIfAbsent(customerID, new ArrayList<>());
								Otw_Ts.get(customerID).add(eventID);
								System.out.println(Otw_Ts);
							}
							rly="BOOKING IS SUCCESSFUL.";
							return rly;	
						}
						else {
							rly="FAILED TO BOOK THE EVENT.";
							return rly;
						}
				
			}
			if(eventID.charAt(0)=='T') {
				port=5001;
				String purpose="BookEvent";
				System.out.println("Sending request from ottawa to toronto");
				String msg=call_other(purpose,customerID,eventID,eventType,port);
				System.out.println(msg);
				if(msg.equalsIgnoreCase("Enrolled")) {
					System.out.println("Enroll: "+msg);
					Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)+1);
					System.out.println(Counter);
					if(eventType.equalsIgnoreCase("SEM")) {
						Otw_Sem.putIfAbsent(customerID, new ArrayList<>());
						Otw_Sem.get(customerID).add(eventID);
						System.out.println(Otw_Sem);
					}
						if(eventType.equalsIgnoreCase("CON")) {
							Otw_Con.putIfAbsent(customerID, new ArrayList<>());
							Otw_Con.get(customerID).add(eventID);
							System.out.println(Otw_Con);
						}
							if(eventType.equalsIgnoreCase("TS")) {
								Otw_Ts.putIfAbsent(customerID, new ArrayList<>());
								Otw_Ts.get(customerID).add(eventID);
								System.out.println(Otw_Ts);
							}
							rly="BOOKING IS SUCCESSFUL.";
							return rly;	
				}
				else {
					rly="FAILED TO BOOK THE EVENT.";
					return rly;
				}
				
			}
		}
		else {
			rly="ENTER THE VALID EVENTID.";
			return rly;
		     }
			rly="ENTER THE VALID EVENTID.";
			return rly;
	}
 
	private String book(String eventID,String customerID, String eventType) {
		String rly="";
		System.out.println("In Ottawa book Event");
				
				if(eventType.equalsIgnoreCase("CON")) {
					if(alreadyPresent(customerID,eventID,eventType)){
						rly="YOU HAVE ALREADY BOOOKED THIS EVENT.";
						return rly;
					}
					else {
						System.out.println("con2");
						if(OTWEvents.get(eventType).containsKey(eventID)) {
							System.out.println("con3");
							int eve=OTWEvents.get(eventType).get(eventID);
							if(eve>0) 
							 {
									Otw_Con.putIfAbsent(customerID, new ArrayList<>());
									Otw_Con.get(customerID).add(eventID);
									OTWEvents.get(eventType).put(eventID,OTWEvents.get(eventType).get(eventID)-1);
									rly="BOOKING IS SUCCESSFUL.";
									System.out.println(Counter);
									System.out.println(OTWEvents);
									return rly;
							 }
							else
							{
								rly="THIS EVENT IS FULL.";
								return rly;
							}
						}
						else
						{
							rly="ENTER THE VALID EVENTID.";
							return rly;
						}
					}
						
			}
				else if(eventType.equalsIgnoreCase("TS")) {
					if(alreadyPresent(customerID,eventID,eventType)){
						rly="YOU HAVE ALREADY BOOKED THIS EVENT.";
						return rly;
					}
					else {
						if(OTWEvents.get(eventType).containsKey(eventID)) {
							int eve=OTWEvents.get(eventType).get(eventID);
							if(eve>0) {
									Otw_Ts.putIfAbsent(customerID, new ArrayList<>());
									Otw_Ts.get(customerID).add(eventID);
									OTWEvents.get(eventType).put(eventID,OTWEvents.get(eventType).get(eventID)-1);
									rly="BOOKING IS SUCCESSFUL.";
									System.out.println(Counter);
									System.out.println(OTWEvents);
									return rly;
								}
							else
							{
								rly="THIS EVENT IS FULL.";
								return rly;
							}
						}
						else
						{
							rly="ENTER THE VALID EVENTID.";
							return rly;
						}
							
						
				}
		}
				else if(eventType.equalsIgnoreCase("SEM")) {
					if(alreadyPresent(customerID,eventID,eventType)){
						rly="YOU HAVE ALREADY BOOKED THIS EVENT.";
						return rly;
					}
					else {
						System.out.println("sem2");
						if(OTWEvents.get(eventType).containsKey(eventID)) {
							int eve=OTWEvents.get(eventType).get(eventID);
							if(eve>0) {
									Otw_Sem.putIfAbsent(customerID, new ArrayList<>());
									Otw_Sem.get(customerID).add(eventID);
									OTWEvents.get(eventType).put(eventID,OTWEvents.get(eventType).get(eventID)-1);
									rly="BOOKING IS SUCCESSFUL.";
									System.out.println(Counter);
									System.out.println(OTWEvents);
									return rly;
								}
							else
							{
								rly="THIS EVENT IS FULL.";
								return rly;
							}
						}
						else
						{
							rly="ENTER THE VALID EVENTID.";
							return rly;
						}
							
						}
					}
				else
					rly="ENTER THE VALID EVENTID";
		return rly;
	}
	
	private boolean alreadyPresent(String customerID, String eventID, String eventType) {
		for(Map.Entry<String, ArrayList<String>> pair:Otw_Con.entrySet()) {
			if(pair.getKey().equals(customerID)&&pair.getValue().contains(eventID)) {
				return true;
			}
		}
		for(Map.Entry<String, ArrayList<String>> pair : Otw_Ts.entrySet()) {
			if(pair.getKey().equals(customerID)&&pair.getValue().contains(eventID)) {
				return true;
			}
		}
		for(Map.Entry<String, ArrayList<String>> pair : Otw_Sem.entrySet()) {
				if(pair.getKey().equals(customerID)&&pair.getValue().contains(eventID)) {
					return true;
				}
		}
			return false;
	}
	
	public String getBookingSchedule(String customerID) 
	{
		 String msg1="EVENTS BOOKED : \nConferences: "+Otw_Con.get(customerID)+
					"\nSeminars: " +Otw_Sem.get(customerID)+
			    	"\nTradeShows: " +Otw_Ts.get(customerID);
		 msg1=msg1.replace("null", "[]");
		return msg1;
	    
	}

	
	public String cancelEvent(String customerID, String eventID, String eventType) 
	{
		String msg="";
		if(Otw_Sem.containsKey(customerID)||Otw_Ts.containsKey(customerID)||Otw_Con.containsKey(customerID)) 
		{
		if(eventID.charAt(0)=='T')
        {   
	           port=5001;
	           String purpose="Cancel";  
				String rly=call_other1(purpose,eventID,eventType,port);
				System.out.println(rly);
				if(rly.trim().toString().equalsIgnoreCase("Y")){      	   
					String month=eventID.substring(6,8);
					int m=Integer.parseInt(month);
					if(eventType.equalsIgnoreCase("SEM")) {
						Otw_Sem.get(customerID).remove(eventID);
					}
					else if(eventType.equalsIgnoreCase("CON")) {
						Otw_Con.get(customerID).remove(eventID);
					}
					else if(eventType.equalsIgnoreCase("TS")) {
						Otw_Ts.get(customerID).remove(eventID);	
					}
						Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)-1);
						System.out.println(Counter);
						msg="EVENT HAS BEEN CANCELED.";
	                  //  log.log(Level.INFO, "Customer with ID: {0} cancel in Event {1} For {2}", new Object[]{customerID, eventID, eventType});

					}
	          else{
	        	  return msg;    
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
	      						Otw_Sem.get(customerID).remove(eventID);
	      					}
	      					else if(eventType.equalsIgnoreCase("CON")) {
	      						Otw_Con.get(customerID).remove(eventID);
	      					}
	      					else if(eventType.equalsIgnoreCase("TS")) {
	      						Otw_Ts.get(customerID).remove(eventID);	
	      					}
	      						Counter.get(customerID).set(m-1, Counter.get(customerID).get(m-1)-1);
	      						System.out.println(Counter);
	      						msg="EVENT HAS BEEN CANCELED.";
	      					}
	      	          else{
	      	        	  return msg;      
	      	               }          
	        }
	      else if(eventID.charAt(0)=='O')
	      {
             if(OTWEvents.get(eventType).containsKey(eventID))
             {   
        	   if(eventType.equalsIgnoreCase("CON")) 
        	   {
               	   if(Otw_Con.containsKey(customerID))
               	   { 
               		   if(Otw_Con.get(customerID).contains(eventID)) 
               		   {
         	             Otw_Con.get(customerID).remove(eventID);
         	             OTWEvents.get(eventType).put(eventID,OTWEvents.get(eventType).get(eventID)+1);
         	            msg="EVENT HAS BEEN CANCELED.";
               		   }
               		   else {
               			msg="CUSTOMER NOT REGISTERED FOR THIS EVENT.";
         			   }
             	  }
         	      else 
         	      {
         	    	 msg="CUSTOMER NOT REGISTERED FOR THIS EVENT.";
                   }
             }
        	   else if(eventType.equalsIgnoreCase("TS")) {
               	   if(Otw_Ts.containsKey(customerID)){ 
               		   if(Otw_Ts.get(customerID).contains(eventID)) {
               			   Otw_Ts.get(customerID).remove(eventID);
               			   OTWEvents.get(eventType).put(eventID,OTWEvents.get(eventType).get(eventID)+1);
               			msg="EVENT HAS BEEN CANCELED.";
               		   }
               		else {
               			msg="CUSTOMER NOT REGISTERED FOR THIS EVENT.";
         			   }
         	   	}
         	   else {
         		  msg="CUSTOMER NOT REGISTERED FOR THIS EVENT.";
                   }
            }
             else if(eventType.equalsIgnoreCase("SEM")){
            	   if(Otw_Sem.containsKey(customerID)){ 
            		   if(Otw_Sem.get(customerID).contains(eventID)) {
            			   Otw_Sem.get(customerID).remove(eventID);
            			   OTWEvents.get(eventType).put(eventID,OTWEvents.get(eventType).get(eventID)+1);
            			   msg="EVENT HAS BEEN CANCELED.";
            		   }
            		   else {
            			   msg="CUSTOMER NOT REGISTERED FOR THIS EVENT.";
            			   }
            	   	}
            	   else {
            		   msg="CUSTOMER NOT REGISTERED FOR THIS EVENT.";
                      }
           }
             else {
            	 msg="ENTER THE VALID EVENTTYPE.";
             }
           	}
           else
           {
        	   msg="EVENT DOES NOT EXIST.";
           }
		}
	    else
	       {
	    	   msg="ENTER THE VALID EVENTID.";
	       }
	
		}
		else {
			msg="CUSTOMER NOT REGISTERED FOR THIS EVENT.";
		}
		return msg;
	}
	
	public String cancelEvent1(String eventID, String eventType) 
	{
		String msg="";
		System.out.println("inside cancel1");
		if(OTWEvents.get(eventType).containsKey(eventID))
	           {    
 	               OTWEvents.get(eventType).put(eventID,OTWEvents.get(eventType).get(eventID)+1);
 	              System.out.println(OTWEvents);
	                   msg="Y"; 
	                   return msg;
	             }
	    
	     else
	           {          
	    	      msg="EVENT DOES NOT EXIST.";                          
	             return msg;
	           }
	       
	}
	public String sayhello() {
		return "HELLO";
	}
	
	public void setORB(ORB orb_val) {
		orb=orb_val;
		inimain();
		Thread t=new Thread(()->{
			receive();
			
		});
		t.start();
			
	}
	

	public String swapEvent(String customerID, String newEventID, String newEventType, String oldEventID, String oldEventType)
	{
		
		String msg = null;
		System.out.println("In Swap Event");
		
		msg=cancelEvent(customerID,oldEventID,oldEventType);
		System.out.println(msg);
		if(msg.equalsIgnoreCase("EVENT HAS BEEN CANCELED."))
		{
			msg=bookEvent(customerID,newEventID,newEventType);
			if(msg.equalsIgnoreCase("BOOKING IS SUCCESSFUL.")) 
			{
				msg="SWAPPING SUCCESSFUL.";
				return msg;
		    }
		    else
		    {
			  String msg1=bookEvent(customerID,oldEventID,oldEventType);
			  return "SWAP FAILED.";

			}
		}
		else
		{
			msg="Event is not booked, so can't cancel";
			return "SWAP FAILED.";
		}

		
	}
		


	public static void main(String[] args) 
	{
		System.out.println("OTW Server Started...");
		Otw_server ms = new Otw_server();
		ms.inimain();
		Thread t=new Thread(()->{
			ms.receive();
			});
			t.start();
		while(true) {
			
			  MulticastSocket ms1=null;
	       	  DatagramSocket ds1=null;
	       	  while(true){
	       		  System.out.println("Receiving Replica Message and sending response to FE");
	       		  try{
	    					ms1=new MulticastSocket(1700);
	    					ds1=new DatagramSocket(3005);
	    					InetAddress ip=InetAddress.getByName("230.0.0.2");
	    					ms1.joinGroup(ip);
	    					byte []buf=new byte[1000];
	    					String msg="";
	    					String reply="";
	    					DatagramPacket dp1=new DatagramPacket(buf,buf.length);
	    					ms1.receive(dp1);
	    					msg=new String(dp1.getData());
	    					System.out.println();
	    					System.out.println("MSG from Replica Manager: "+msg);
	    					StringTokenizer st=new StringTokenizer(msg,",");
	    					ms.unique_id=Integer.parseInt(st.nextToken());
	    					String id=st.nextToken();
	    					String method=st.nextToken();
	    					System.out.println("unique id:"+ms.unique_id+" ID:"+id+" Method:"+method);
	    					
	    					if(method.equals("BOOK")){
	    						String eventID=st.nextToken();
	    						String eventType=st.nextToken();
	    						System.out.println("customerID:"+id+"courseId:"+eventID+" eventType:"+eventType);
	    						reply = ms.bookEvent(id.trim(),eventID.trim(),eventType.trim());
	    						System.out.println(reply);
	    						InetAddress ip1=InetAddress.getByName("localhost");
	    						dp1=new DatagramPacket(reply.getBytes(),reply.length(),ip1,7002);
	    						ds1.send(dp1);
	    					}
	    					else if(method.equals("CANCEL")){
	    						String eventID=st.nextToken();
	    						String eventType=st.nextToken();
	    						System.out.println(msg);
	    						reply=ms.cancelEvent(id.trim(),eventID.trim(),eventType.trim());
	    						System.out.println(reply);
	    						InetAddress ip1=InetAddress.getByName("localhost");
	    						dp1=new DatagramPacket(reply.getBytes(),reply.length(),ip1,7002);
	    						ds1.send(dp1);
	    					}
	    					else if(method.equals("SWAP")){
	    						String newEventID=st.nextToken();
	    						String newEventType=st.nextToken();
	    						String oldEventId=st.nextToken();
	    						String oldEventType=st.nextToken();
	    						reply=ms.swapEvent(id.trim(),newEventID.trim(),newEventType.trim(),oldEventId.trim(),oldEventType.trim());
	    						System.out.println(reply);
	    						InetAddress ip1=InetAddress.getByName("localhost");
	    						dp1=new DatagramPacket(reply.getBytes(),reply.length(),ip1,7002);
	    						ds1.send(dp1);
	    					}
	    					else if(method.trim().equals("GETSCHEDULE")){
	    								
	    						reply=ms.getBookingSchedule(id.trim());
	    						System.out.println(reply);
	    						InetAddress ip1=InetAddress.getByName("localhost");
	    						dp1=new DatagramPacket(reply.getBytes(),reply.length(),ip1,7002);
	    						ds1.send(dp1);
	    						
	    					}
	    					else if(method.equals("ADD")){
	    						String eventID=st.nextToken();
	    						String eventType=st.nextToken().trim();
	    						int cap=Integer.parseInt(st.nextToken().trim());
	    						reply=ms.addEvent(id.trim(),eventID.trim(),eventType.trim(),cap);
	    						System.out.println(reply);
	    						InetAddress ip1=InetAddress.getByName("localhost");
	    						dp1=new DatagramPacket(reply.getBytes(),reply.length(),ip1,7002);
	    						ds1.send(dp1);
	    					}
	    					else if(method.equals("REMOVE")){

	    						String eventID=st.nextToken();
	    						String eventType=st.nextToken();
	    						reply=ms.removeEvent(eventID.trim(),eventType.trim());
	    						System.out.println(reply);
	    						InetAddress ip1=InetAddress.getByName("localhost");
	    						dp1=new DatagramPacket(reply.getBytes(),reply.length(),ip1,7002);
	    						ds1.send(dp1);
	    					}
	    					else if(method.equals("LIST")){
	    						String eventType=st.nextToken();
	    						reply=ms.listEventAvailability(eventType.trim());
	    						InetAddress ip1=InetAddress.getByName("localhost");
	    						dp1=new DatagramPacket(reply.getBytes(),reply.length(),ip1,7002);
	    						ds1.send(dp1);
	    					}	
	       		  }
	       		  catch(Exception e){
	       			  System.out.println("Thread Exception: "+e.getMessage());
	       		  }
	       		  finally{
	       			  if(ds1!=null)
	       				  ds1.close();
	       			  if(ms1!=null)
	       				  ms1.close();
	       		  }
	   
	       	  }
		}
	}

}
