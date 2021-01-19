package Helloapl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import Helloapll.Hello;
import Helloapll.HelloHelper;

class HelloImpl extends Helloapll.HelloPOA{

	private ORB orb;
	HashMap<String,String> reply=null;
	int maxdelay=5000;
	DatagramSocket ds1;
	DatagramSocket ds2;
	DatagramSocket ds3;
	
	
	private String call_other(String s_msg) {
		// TODO Auto-generated method stub
		int flag=0;
    	
		DatagramSocket ds=null;
		
		DatagramPacket dp1=null;
		DatagramPacket dp2=null;
		DatagramPacket dp3=null;
		String krisha="",shubham="",dhruvi="";
		
		try {
			long v1,v2,v3;
			final long startTime = System.currentTimeMillis();	
			System.out.println("binding started");
	
			//if(!ds1.isBound()) {
				ds1= new DatagramSocket(7001);//krisha 
			//}
			//if(!ds2.isBound()) {
				ds2=new DatagramSocket(7002);//shubham
			//}
			//if(!ds3.isBound()) {
				ds3=new DatagramSocket(7003);//dhruvi
			//}
			
			ds3.setSoTimeout(maxdelay);
			System.out.println("Binding close");
			ds = new DatagramSocket();
            byte rpurpose[] = s_msg.getBytes();   
            InetAddress addr = InetAddress.getByName("132.205.4.150");//krisha ipconfig
            
            //sequencer send
            
            DatagramPacket dp = new DatagramPacket(rpurpose,rpurpose.length,addr,7005);
            ds.send(dp);
            System.out.println("1");
           
           
            
            byte received2[] = new byte[100000];
            dp2= new DatagramPacket(received2,received2.length);             
            ds2.receive(dp2);
            System.out.println(dp2.getData());
            final long stoptime2= System.currentTimeMillis();
            v2=stoptime2-startTime;
            System.out.println(stoptime2-startTime);
            
            byte received3[] = new byte[100000];
            dp3= new DatagramPacket(received3,received3.length);             
            ds3.receive(dp3);
            System.out.println(dp3.getData());
            final long stoptime3= System.currentTimeMillis();
            v3=stoptime3-startTime;
            System.out.println(stoptime3-startTime);
            
            byte received1[] = new byte[100000];
            dp1= new DatagramPacket(received1,received1.length);       
            ds1.receive(dp1);
            System.out.println(dp1.getData());
            final long stoptime1= System.currentTimeMillis();
            v1=stoptime1-startTime;
            System.out.println(stoptime1-startTime);
            
            long min=v1;
            if(min<v2) {
         	   	min=v2;
            } else if(min<v3) {
            	min=v3;   
            }
            maxdelay=(int) min+10;
            System.out.println("Max Delay:"+maxdelay);
            
            System.out.println("++++++++++++++++++++");
            krisha=new String(dp1.getData()).trim();
   		 	System.out.println(krisha);
   		 
   		 	shubham= new String (dp2.getData()).trim();
   		 	System.out.println(shubham);
   		 
   		 	dhruvi= new String(dp3.getData()).trim();
   		 	System.out.println(dhruvi);
   		 	
   		 	if(!krisha.equals(dhruvi)&&dhruvi.equals(shubham)) {//Majority
   		 		System.out.print("Software bug in krisha");
    		 	DatagramPacket dpwarn= new DatagramPacket("1".getBytes(),"1".length(),dp1.getAddress(),3030);//sending msg to RM
	 			ds.send(dpwarn);
		 		//if(reply.containsKey(shubham)) {
		 	//		return reply.get(shubham);
		 		//}else {
		 			return shubham.trim();
		 	//	}
		 	}
            
		}catch(SocketTimeoutException e) {
			System.out.println("Timed Out");
			flag=1;
		}catch(Exception e) {
			System.out.println("EX : "+e.getMessage());
		}finally {
			if(flag==1) {
				try {
					InetAddress addr = InetAddress.getByName("132.205.4.142");//DHRUVI ipconifg
					DatagramPacket dp = new DatagramPacket("Crash".getBytes(),"Crash".length(),addr,2000);
					System.out.println(flag);
					ds.send(dp);
					
					shubham=new String(dp2.getData()).trim();
					ds.close();
					ds1.close();
					ds2.close();
					ds3.close();
					flag=0;
					return shubham.trim();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//ds.close();
			ds1.close();
			ds2.close();
			ds3.close();
		}
		
		//if(reply.containsKey(shubham)) {
		//	return reply.get(shubham);
		//}else {	
			return shubham.trim();
		//}   
	}
	
	@Override
	public synchronized String bookEvent(String customerID, String eventID, String eventType) {
		// TODO Auto-generated method stub
		System.out.println("Inside BookEvent");
		String s_msg = customerID+",BOOK,"+eventID+","+eventType;
		String r_msg = call_other(s_msg);
		return r_msg;
	}

	@Override
	public synchronized String getBookingSchedule(String customerID) {
		// TODO Auto-generated method stub
		System.out.println("Inside GetBookingSchedule");
		String s_msg = customerID+",GETSCHEDULE";
		String r_msg = call_other(s_msg);;
		return r_msg;
	}

	@Override
	public synchronized String cancelEvent(String customerID, String eventID, String eventType) {
		// TODO Auto-generated method stub
		System.out.println("Inside CancelEvent");
		String s_msg = customerID+",CANCEL,"+eventID+","+eventType;
		String r_msg = call_other(s_msg);;
		return r_msg;
	}

	@Override
	public synchronized String swapEvent(String customerID, String newEventID, String newEventType, String oldEventID,
			String oldEventType) {
		// TODO Auto-generated method stub
		System.out.println("Inside SwapEvent");
		String s_msg = customerID+",SWAP,"+newEventID+","+newEventType+","+oldEventID+","+oldEventType;
		String r_msg = call_other(s_msg);;
		return r_msg;
	}

	@Override
	public synchronized String addEvent(String managerID, String eventID, String eventType, int bookingCapacity) {
		// TODO Auto-generated method stub
		System.out.println("Inside AddEvent");
		String s_msg = managerID+",ADD,"+eventID+","+eventType+","+bookingCapacity;
		String r_msg = call_other(s_msg);;
		return r_msg;
	}

	@Override
	public synchronized String removeEvent(String managerID, String eventID, String eventType) {
		// TODO Auto-generated method stub
		System.out.println("Inside RemoveEvent");
		String s_msg = managerID+",REMOVE,"+eventID+","+eventType;
		String r_msg = call_other(s_msg);;
		return r_msg;
	}

	@Override
	public synchronized String listEventAvailability(String managerID, String eventType) {
		// TODO Auto-generated method stub
		System.out.println("Inside ListEvent");
		String s_msg = managerID+",LIST,"+eventType;
		String r_msg = call_other(s_msg);;
		return r_msg.trim();
	}

	public void setORB(ORB orb_val) {
		// TODO Auto-generated method stub
		orb = orb_val;
	}
	
}


public class FrontEnd {

	public static void main(String[] args) {
		
		  try {

		  
		// create and initialize the ORB
	      ORB orb = ORB.init(args, null);
	      
	      // get reference to rootpoa & activate the POAManager
	      POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
	      rootpoa.the_POAManager().activate();
	     
	      // create servant and register it with the ORB
	      HelloImpl helloImpl = new HelloImpl();
	      helloImpl.setORB(orb); 
	      
	      // get object reference from the servant
	      org.omg.CORBA.Object ref = rootpoa.servant_to_reference(helloImpl);
	      
	      Hello href = HelloHelper.narrow(ref);
	        
	      // get the root naming context
	      // NameService invokes the name service
	     
	      org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
	      // Use NamingContextExt which is part of the Interoperable
	      // Naming Service (INS) specification.
	      
	      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
	      
	      // bind the Object Reference in Naming
	      String name = "Hello1";
	      NameComponent path[] = ncRef.to_name( name );
	      
	      ncRef.rebind(path, href);

	      System.out.println("Front end ready and waiting ...");
	/*      HelloImpl imp = new HelloImpl();
	      imp.intialize_main();*/
	      // wait for invocations from clients
	      orb.run();
		  }
		  catch(Exception e) {
			  System.out.println(e);
			  
		  }

	}

}