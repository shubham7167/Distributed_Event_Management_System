package Helloapl;

import java.io.IOException;
import java.net.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.CORBA.*;
import org.omg.CORBA.ORBPackage.InvalidName;

public class Client 
{
	  
	static hello helloImpl;
  public static void main(String args[]) throws NotBoundException, IOException, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName
   {
    	 int status=0;
    	 Logger log=null;
    	 FileHandler file;
     do {	 
    	 
    	 
    	 	
    	 
    	 	
    	    int port = 0,choice,capacity,index=0;
            Scanner s = new Scanner(System.in);
            Scanner s1 = new Scanner(System.in);
            Scanner s2 = new Scanner(System.in);
            Scanner s3 = new Scanner(System.in);
            Scanner s4 = new Scanner(System.in);
            System.out.println("Enter ID:");
            String clientID=s.nextLine();
            
            ORB orb = ORB.init(args,null);
            
       /*     try {
            	
            	
            	log=Logger.getLogger("Client1");
            	file=new FileHandler("D:/Shubham/Concordia/Distributed System/assignment/dsd/dsd/Logs/"+clientID+".log",true);
            	log.helloImplHandler(file);
            	log.setUseParentHandlers(false);
            	SimpleFormatter sf=new SimpleFormatter();
            	file.setFormatter(sf);
            	
            }
            catch(IOException e){
            	e.printStackTrace();
            	
            }*/
  
            
          
            String name="";

            
            String server = clientID.substring(0, 3);
            char person = clientID.charAt(3);
            System.out.println(server+" "+person);
            
            DatagramSocket socket= new DatagramSocket();
            byte[] buff = new byte[1000];
            
            if(server.equalsIgnoreCase("MTL"))
            {   
            	  org.omg.CORBA.Object obj;
				try {
					obj = orb.resolve_initial_references("NameService");
					NamingContextExt ncRef = NamingContextExtHelper.narrow(obj);
					  name="Hello1";
	                  helloImpl = helloHelper.narrow(ncRef.resolve_str(name));
	                port=4444;                   
				} catch (InvalidName e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}             
                
            }   
            if(server.equalsIgnoreCase("TOR"))
            {   
            	  org.omg.CORBA.Object obj;
				try {
					obj = orb.resolve_initial_references("NameService");
					 NamingContextExt ncRef = NamingContextExtHelper.narrow(obj);	                  
	                  name="Hello3";
	                  helloImpl = helloHelper.narrow(ncRef.resolve_str(name));
	                port=5555;     
				} catch (InvalidName e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                  
                               
            }    
            if(server.equalsIgnoreCase("OTW"))
            {
            	  org.omg.CORBA.Object obj;
				try 
				{
					obj = orb.resolve_initial_references("NameService");
					  NamingContextExt ncRef = NamingContextExtHelper.narrow(obj);                  
	                  name="Hello2";
	                  helloImpl = helloHelper.narrow(ncRef.resolve_str(name));
	                port=6666;
				} 
				catch (InvalidName e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                                 
            }   
            System.out.println("Port number:"+port);
            
            String customerID="",eventID="",eventType="",msg="",newEventID="",newEventType="";
            int bookingCapacity;
            
            if(person=='C'){
                
                //  connect();
                  choice=0;
                  do{
                      
                      System.out.println("1.Book Event");
                      System.out.println("2.Get Booking Schedule");
                      System.out.println("3.Cancel Event");
                      System.out.println("4.Swap Event");
                      System.out.println("5.Logout");
                      System.out.println("Enter your Choice=");
                      
                      choice=s.nextInt();
                      
                      switch(choice){
                          
                          case 1: System.out.print("Enter Event ID:");
                                  eventID=s1.nextLine();
                                  System.out.print("Enter Event Type:");
                                  eventType=s2.nextLine();                                   
                                  msg= helloImpl.bookEvent(clientID, eventID, eventType);
                                  System.out.println(msg);   
                                //  log.log(Level.INFO, "Customer ID: {0}{1}", new Object[]{clientID, msg});
                                  break;
                              
                          case 2: System.out.println(helloImpl.getBookingSchedule(clientID));                             
                                   //RMI method
                                   break;
                          case 3: System.out.println("Enter Event ID to drop:");
                          		eventID=s1.nextLine();
                          		System.out.print("Enter Event Type:");
                          		eventType=s2.nextLine(); 
                          		msg=helloImpl.cancelEvent(clientID, eventID, eventType);
                          		System.out.println(msg);
                          		// log.log(Level.INFO, "Customer ID: {0}{1}", new Object[]{clientID, msg});
                          		break;
                          
                          case 4: 	System.out.println("Enter new Event ID: ");
                			        newEventID=s1.nextLine();
                			        System.out.print("Enter new Event Type:");
                			        newEventType=s2.nextLine(); 
                			        System.out.println("Enter old Event ID: ");
                			        eventID=s3.nextLine();
                			        System.out.print("Enter old Event Type:");
                			        eventType=s4.nextLine(); 
                			        msg=helloImpl.swapEvent(clientID, newEventID, newEventType, eventID, eventType);
                			        System.out.println(msg);
                			        break;        
                          case 5:  break;    
                      }                   
                  }while(choice!=5);
              }
            
            else if(person=='M') {
                
                choice=0;
                String ManagerID = clientID;
                do{
                    
                    System.out.println("1.helloImpl Event");
                    System.out.println("2.Remove Event");
                    System.out.println("3.List Event Availability");
                    System.out.println("4.Book Event");
                    System.out.println("5.Get Booking Schedule");
                    System.out.println("6.Cancel Event");
                    System.out.println("7.Swap Event");
                    System.out.println("8.MultiThreading task");
                    System.out.println("9.Logout");
                    System.out.println("Enter Choice");
                    
                    choice=s.nextInt();
                    
                    switch(choice){
                        
                        case 1: System.out.println("Enter Event ID:");
                                eventID=s1.nextLine();
                                System.out.println("Enter Event Type(Con,Ts,Sem):");
                                eventType=s2.nextLine();
                                System.out.println("Enter Booking Capacity:");
                                bookingCapacity=s3.nextInt();
                                msg = helloImpl.addEvent(ManagerID,eventID, eventType,bookingCapacity);
                                System.out.println(msg); 
                               // log.log(Level.INFO, "Manager ID: {0}{1}", new Object[]{ManagerID, msg});
                                break;
                        
                        case 2: System.out.println("Enter Event ID:"); 
                                eventID=s1.nextLine();
                                System.out.println("Enter Event Type:");
                                eventType=s2.nextLine();
                                msg = helloImpl.removeEvent(eventID, eventType);
                                System.out.println(msg); 
                               // log.log(Level.INFO, "Manager ID: {0}{1}", new Object[]{ManagerID, msg});
                                
                                break;
                            
                        case 3: System.out.println("Enter Event Type:");
                                eventType= s1.nextLine();
                                msg=helloImpl.listEventAvailability(eventType);
                                System.out.println(msg);
                          
                                //RMI method1
                                break;
                        
                        case 4:System.out.print("Enter Customer id: ");
                               customerID=s1.nextLine();
                        	   System.out.println("Enter Event ID:");
                               eventID=s2.nextLine();
                               System.out.println("Enter Event Type:");
                               eventType=s3.nextLine();
                               msg= helloImpl.bookEvent(customerID, eventID, eventType);
                               System.out.println(msg); 
                              // log.log(Level.INFO, "Manager ID: {0} , Customer ID: {1}{2}", new Object[]{ManagerID,customerID, msg});
                        	   break;
                        	   
                        case 5:System.out.print("Enter Customer id: ");
                               customerID=s1.nextLine();
                               System.out.println(helloImpl.getBookingSchedule(customerID));
                               break;
                               
                        case 6:System.out.println("Enter Customer Id:");                                 
                               customerID=s1.nextLine();
                               System.out.println("Enter Event ID:"); 
                               eventID=s2.nextLine();
                               System.out.println("Enter Event Type:");
                               eventType=s3.nextLine();
                               msg=helloImpl.cancelEvent(customerID, eventID, eventType);
                               System.out.println(msg);
                             //  log.log(Level.INFO, "Manager ID: {0} , Customer ID: {1}{2}", new Object[]{ManagerID,customerID, msg});
                               break;
                        case 7:System.out.println("Enter new Event ID: ");
                        	   newEventID=s1.nextLine();
                        	   System.out.print("Enter new Event Type:");
                        	   newEventType=s2.nextLine(); 
                        	   System.out.println("Enter old Event ID: ");
                        	   eventID=s3.nextLine();
                        	   System.out.print("Enter old Event Type:");
                        	   eventType=s4.nextLine(); 
                        	   msg=helloImpl.swapEvent(clientID, newEventID, newEventType, eventID, eventType);
                        	   System.out.println(msg);
                        	   break;        
                        case 8: Thread t1=new Thread(()->{
                        		String msg1=null;
                        		try {
									msg1=helloImpl.addEvent("MTLM1234", "MTLM020220", "SEM", 1);
								} catch (Exception e) {
									
									e.printStackTrace();
								}
                        		System.out.println("T1:  "+msg1);
                        		});
                        		
                        		Thread t2=new Thread(()->{
                            		String msg1=null;
                            		try {
                               			msg1=helloImpl.bookEvent("MTLC3456", "MTLM020220", "SEM");
    								} catch (Exception e) {
    									
    									e.printStackTrace();
    								}
                            		System.out.println("T2:  "+msg1);
                            		});
                            		
                            	Thread t3=new Thread(()->{
                                		String msg1=null;
                                		try {
                                			msg1=helloImpl.bookEvent("MTLC2345", "MTLM020220", "SEM");
        								}  catch (Exception e) {
        									
        									e.printStackTrace();
        								}
                                		System.out.println("T3:  "+msg1);
                                		});
                                		
                                Thread t4=new Thread(()->{
                                    	String msg1=null;
                                    	try {
                                    		msg1=helloImpl.cancelEvent("MTLC2345", "MTLM020220", "SEM");
            							} catch (Exception e) {
        									
        									e.printStackTrace();
        								}
                                    	System.out.println("T4:  "+msg1);
                                   		});
                                		
                               	Thread t5=new Thread(()->{
                                   		String msg1=null;
                                   		try {
                                   			msg1=helloImpl.removeEvent("MTLM020220", "SEM");
            							}  
                                   		catch (Exception e) {
        									
        									e.printStackTrace();
        								}
                                    		System.out.println("T5:  "+msg1);
                                    	});
                                    		                               
                                    		t1.start();
                                    		t2.start();
                                    		t3.start();
                                    		t4.start();
                                    		t5.start();
                               
                        		
                               
                        case 9: break;
                        	
                    }
                }while(choice!=9);
            }
            
            else {
            	System.out.println("Enter valid clientID....");
            System.out.println("Press 1 to Log in: ");
            System.out.println("Press 2 to Close the Application: ");
            status = s.nextInt();
            }

       }while(status!=2);
            	
    }
}

