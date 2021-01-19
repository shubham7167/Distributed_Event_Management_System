package dsd;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Otw_server extends UnicastRemoteObject implements Event_Interface
{
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
		try
        {
            
            log=Logger.getLogger("Otw_server");
            file=new FileHandler("C:/Users/patel/eclipse-workspace/dsd/dsd/Logs/Otw_server.log",true);
            log.addHandler(file);
            log.setUseParentHandlers(false);
            SimpleFormatter sf=new SimpleFormatter();
            file.setFormatter(sf);
        
        }catch(IOException e){
        	e.printStackTrace();
        	}
        
		
		Seminars.put("OTWM030119",4);
		Seminars.put("OTWA030119",6);
		Seminars.put("OTWE030119",4);
		OTWEvents.put("SEM",Seminars);
		
		Conference.put("OTWM040119",5);
		Conference.put("OTWA040119",7);
		Conference.put("OTWE040120",3);
		OTWEvents.put("CON",Conference);
		
		TradeShows.put("OTWM050119",3);
		TradeShows.put("OTWA050119",6);
		TradeShows.put("OTWE050119",7);
		OTWEvents.put("TS",TradeShows);
		
		Manager.add("OTWM1234");
		Manager.add("OTWM2345");
		Manager.add("OTWM3456");
		
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


	
	public String addEvent(String ManagerID, String eventID, String eventType, int bookingCapacity)  throws RemoteException,IOException
	{
		String msg;
		if(Manager.contains(ManagerID))
		  {  
	        if(OTWEvents.get(eventType).containsKey(eventID))
	        {
	        	OTWEvents.get(eventType).put(eventID,OTWEvents.get(eventType).get(eventID)+bookingCapacity);
	            
	        	msg=eventID+" Event Already Exist...Booking Capacity Updated";
	          
	        }
	        else
	        {
	        	if(eventID.startsWith("OTW"))
	       	     {
	                OTWEvents.get(eventType).put(eventID, bookingCapacity); 
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
 
	@Override
public String removeEvent(String eventID, String eventType) throws RemoteException
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

						msg=eventID+" Event has been removed."; 
						System.out.println(msg);
						log.log(Level.INFO, "Event{0} Removed EventType {1}", new Object[]{ eventID, eventType});
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

						msg=eventID+" Event has been removed."; 
						System.out.println(msg);
						log.log(Level.INFO, "Event{0} Removed EventType {1}", new Object[]{ eventID, eventType});
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
			}
		}
	
		else {
			msg="Enter the valid Event Id.";
		}
		return msg;
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
			msg=eventID+"Event has been removed.";
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
			msg=eventID+"Event has been removed.";
			
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
			rly="Enter the valid Event Type";
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
			rly="Enter the valid Event Type";
		}
		
		return rly;
	}

	
public String bookEvent(String customerID, String eventID, String eventType) throws RemoteException,IOException
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
						rly="You have already book the event";
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
									rly="Booking for "+eventID+" is succesful for the " +customerID;
                                    log.log(Level.INFO, "Customer with ID: {0} book in Event {1} For {2}", new Object[]{customerID, eventID, eventType});

									return rly;
									
								}
							else
							{
								rly="This event is full.";
							}
						}
						else 
						{
							rly="enter valid event id";
							return rly;
						}
					}
				}
				if(eventType.equalsIgnoreCase("TS")) {
					System.out.println("in ts");
					if(alreadyPresent(customerID,eventID,eventType)){
						rly="You have already book the event";
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
				if(eventType.equalsIgnoreCase("SEM")) {
					if(alreadyPresent(customerID,eventID,eventType)){
						System.out.println("hey4");
						rly="You have already book the event";
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
									rly="Booking for "+eventID+" is succesful for the " +customerID;
									return rly;
							}
							else
							{
								rly="This event is full.";
							}
						}
						else 
						{
							rly="Enter valid event id";
						}
					}					
				}
				else
				{
					rly="Enter valid Event Type.";
				}
			}
			else 
			{
				rly="Enter valid Customer ID.";
			}
		}
			else {
				rly="Enter valid Event ID.";
			}
			
      return rly;			
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
				rly="You have already registered in 3 events outside Ottawa.";
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
							rly="Booking for "+eventID+" is succesful for the " +customerID;
							return rly;	
						}
						else {
							rly="Error in booking..Try again.";
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
 
	private String book(String eventID,String customerID, String eventType) {
		String rly="";
		System.out.println("In Ottawa book Event");
				
				if(eventType.equalsIgnoreCase("CON")) {
					if(alreadyPresent(customerID,eventID,eventType)){
						rly="You have already book the event";
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
									rly="Enrolled";
									System.out.println(Counter);
									System.out.println(OTWEvents);
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
				else if(eventType.equalsIgnoreCase("TS")) {
					if(alreadyPresent(customerID,eventID,eventType)){
						rly="You have already book the event";
						return rly;
					}
					else {
						if(OTWEvents.get(eventType).containsKey(eventID)) {
							int eve=OTWEvents.get(eventType).get(eventID);
							if(eve>0) {
									Otw_Ts.putIfAbsent(customerID, new ArrayList<>());
									Otw_Ts.get(customerID).add(eventID);
									OTWEvents.get(eventType).put(eventID,OTWEvents.get(eventType).get(eventID)-1);
									rly="Enrolled";
									System.out.println(Counter);
									System.out.println(OTWEvents);
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
				else if(eventType.equalsIgnoreCase("SEM")) {
					if(alreadyPresent(customerID,eventID,eventType)){
						rly="You have already book the event";
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
									rly="Enrolled";
									System.out.println(Counter);
									System.out.println(OTWEvents);
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
	
	public String getBookingSchedule(String customerID) throws RemoteException
	{
	    String msg1="Events booked by "+customerID+"\n" +"Conference Events: "+Otw_Con.get(customerID)+ "\n" +
			"Seminar Events: " +Otw_Sem.get(customerID)+"\n"+
	    	"Trade Show Events:"	+Otw_Ts.get(customerID);
		return msg1;
	    
	}

	@Override
	public String cancelEvent(String customerID, String eventID, String eventType) throws RemoteException
	{
		String msg="";
		if(Otw_Sem.containsKey(customerID)||Otw_Ts.containsKey(customerID)||Otw_Con.containsKey(customerID)) {
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
						msg=eventID+" Event has been removed.";
	                    log.log(Level.INFO, "Customer with ID: {0} cancel in Event {1} For {2}", new Object[]{customerID, eventID, eventType});

					}
	          else{
	        	  msg="This eventID is not available in Ottawa..Enter valid event ID ";    
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
	      						msg=eventID+" Event has been removed.";
	      					}
	      	          else{
	      	        	msg="This eventID is not available in Montreal..Enter valid event ID ";       
	      	               }          
	        }
	    else if(eventID.charAt(0)=='O'){
           if(OTWEvents.get(eventType).containsKey(eventID)){   
        	   if(eventType.equalsIgnoreCase("CON")) {
               	   if(Otw_Con.containsKey(customerID)){ 
               		   if(Otw_Con.get(customerID).contains(eventID)) {
         	           Otw_Con.get(customerID).remove(eventID);
         	           OTWEvents.get(eventType).put(eventID,OTWEvents.get(eventType).get(eventID)+1);
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
        	   else if(eventType.equalsIgnoreCase("TS")) {
               	   if(Otw_Ts.containsKey(customerID)){ 
               		   if(Otw_Ts.get(customerID).contains(eventID)) {
               			   Otw_Ts.get(customerID).remove(eventID);
               			   OTWEvents.get(eventType).put(eventID,OTWEvents.get(eventType).get(eventID)+1);
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
             else if(eventType.equalsIgnoreCase("SEM")){
            	   if(Otw_Sem.containsKey(customerID)){ 
            		   if(Otw_Sem.get(customerID).contains(eventID)) {
            			   Otw_Sem.get(customerID).remove(eventID);
            			   OTWEvents.get(eventType).put(eventID,OTWEvents.get(eventType).get(eventID)+1);
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
           msg="EVENT DOENST EXIST..Enter valid Event Id.";
           }
		}
	
		}
		else {
		msg="You haven't booked this event.";
		}
		return msg;
	}
	
	public String cancelEvent1(String eventID, String eventType) throws RemoteException
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
	             msg="N";                        
	             return msg;
	           }
	       
	}

	public static void main(String[] args) throws RemoteException, AlreadyBoundException 
	{
		Registry otwreg=LocateRegistry.createRegistry(6666);
		Otw_server ms=new Otw_server();
		otwreg.rebind("ks_otw",ms);
		ms.inimain();
	}

	protected Otw_server() throws RemoteException {
		super();
		Thread t1=new Thread(()->{
		receive();
		});
		t1.start();
	}

}
