	package dsd;

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

public class Client 
{
	  
  public static void main(String args[]) throws NotBoundException, IOException
   {
    	 System.setProperty("java.rmi.server.hostname","127.0.0.1");
    	 int status=0;
    	 Logger log=null;
    	 FileHandler file;
     do {	 
    	    int port = 0,choice,capacity,index=0;
            String regi="";
            Registry reg=LocateRegistry.getRegistry("127.0.0.1",port);
            Scanner s = new Scanner(System.in);
            Scanner s1 = new Scanner(System.in);
            Scanner s2 = new Scanner(System.in);
            Scanner s3 = new Scanner(System.in);
            System.out.println("Enter ID:");
            String clientID=s.nextLine();
            
            try {
            	log=Logger.getLogger("Client1");
            	file=new FileHandler("C:/Users/patel/eclipse-workspace/dsd/dsd/Logs/"+clientID+".log",true);
            	log.addHandler(file);
            	log.setUseParentHandlers(false);
            	SimpleFormatter sf=new SimpleFormatter();
            	file.setFormatter(sf);
            	
            }
            catch(IOException e){
            	e.printStackTrace();
            	
            }
            
            
            String server = clientID.substring(0, 3);
            char person = clientID.charAt(3);
            System.out.println(server+" "+person);
            
            DatagramSocket socket= new DatagramSocket();
            byte[] buff = new byte[1000];
            
            if(server.equalsIgnoreCase("MTL"))
            {   
                port=4444;                   
                reg = LocateRegistry.getRegistry("127.0.0.1",port);
                regi="ks_mtl";
            }   
            if(server.equalsIgnoreCase("TOR"))
            {   
                port=5555;                   
                reg = LocateRegistry.getRegistry("127.0.0.1",port);
                regi="ks_tor";
            }    
            if(server.equalsIgnoreCase("OTW"))
            {
                port=6666;
                regi="ks_otw";
                reg = LocateRegistry.getRegistry("127.0.0.1",port);
                
            }   
            System.out.println("Port number:"+port);
            
            System.out.println(regi);
            Event_Interface add = (Event_Interface) reg.lookup(regi);
            
            String customerID="",eventID="",eventType="",msg="";
            int bookingCapacity;
            
            if(person=='C'){
                
                //  connect();
                  choice=0;
                  do{
                      
                      System.out.println("1.Book Event");
                      System.out.println("2.Get Booking Schedule");
                      System.out.println("3.Cancel Event");
                      System.out.println("4.Logout");
                      System.out.println("Enter your Choice=");
                      
                      choice=s.nextInt();
                      
                      switch(choice){
                          
                          case 1: System.out.print("Enter Event ID:");
                                  eventID=s1.nextLine();
                                  System.out.print("Enter Event Type:");
                                  eventType=s2.nextLine();                                   
                                  msg= add.bookEvent(clientID, eventID, eventType);
                                  System.out.println(msg);   
                                  log.log(Level.INFO, "Customer ID: {0}{1}", new Object[]{clientID, msg});
                                  break;
                              
                          case 2: System.out.println(add.getBookingSchedule(clientID));                             
                                   //RMI method
                                   break;
                          
                          case 3: System.out.println("Enter Event ID to drop:");
                                  eventID=s1.nextLine();
                                  System.out.print("Enter Event Type:");
                                  eventType=s2.nextLine(); 
                                  msg=add.cancelEvent(clientID, eventID, eventType);
                                  System.out.println(msg);
                                  log.log(Level.INFO, "Customer ID: {0}{1}", new Object[]{clientID, msg});
                                  break;
                                                        
                          case 4:break;    
                      }                   
                  }while(choice!=4);
              }
            
            else if(person=='M') {
                
                choice=0;
                String ManagerID = clientID;
                do{
                    
                    System.out.println("1.Add Event");
                    System.out.println("2.Remove Event");
                    System.out.println("3.List Event Availability");
                    System.out.println("4.Book Event");
                    System.out.println("5.Get Booking Schedule");
                    System.out.println("6.Cancel Event");
                    System.out.println("7.MultiThreading task");
                    System.out.println("8.Logout");
                    System.out.println("Enter Choice");
                    
                    choice=s.nextInt();
                    
                    switch(choice){
                        
                        case 1: System.out.println("Enter Event ID:");
                                eventID=s1.nextLine();
                                System.out.println("Enter Event Type(Con,Ts,Sem):");
                                eventType=s2.nextLine();
                                System.out.println("Enter Booking Capacity:");
                                bookingCapacity=s3.nextInt();
                                msg = add.addEvent(ManagerID,eventID, eventType,bookingCapacity);
                                System.out.println(msg); 
                                log.log(Level.INFO, "Manager ID: {0}{1}", new Object[]{ManagerID, msg});
                                break;
                        
                        case 2: System.out.println("Enter Event ID:"); 
                                eventID=s1.nextLine();
                                System.out.println("Enter Event Type:");
                                eventType=s2.nextLine();
                                msg = add.removeEvent(eventID, eventType);
                                System.out.println(msg); 
                                log.log(Level.INFO, "Manager ID: {0}{1}", new Object[]{ManagerID, msg});
                                
                                break;
                            
                        case 3: System.out.println("Enter Event Type:");
                                eventType= s1.nextLine();
                                msg=add.listEventAvailability(eventType);
                                System.out.println(msg);
                          
                                //RMI method1
                                break;
                        
                        case 4:System.out.print("Enter Customer id: ");
                               customerID=s1.nextLine();
                        	   System.out.println("Enter Event ID:");
                               eventID=s2.nextLine();
                               System.out.println("Enter Event Type:");
                               eventType=s3.nextLine();
                               msg= add.bookEvent(customerID, eventID, eventType);
                               System.out.println(msg); 
                               log.log(Level.INFO, "Manager ID: {0} , Customer ID: {1}{2}", new Object[]{ManagerID,customerID, msg});
                        	   break;
                        	   
                        case 5:System.out.print("Enter Customer id: ");
                               customerID=s1.nextLine();
                               System.out.println(add.getBookingSchedule(customerID));
                               break;
                               
                        case 6:System.out.println("Enter Customer Id:");                                 
                               customerID=s1.nextLine();
                               System.out.println("Enter Event ID:"); 
                               eventID=s2.nextLine();
                               System.out.println("Enter Event Type:");
                               eventType=s3.nextLine();
                               msg=add.cancelEvent(customerID, eventID, eventType);
                               System.out.println(msg);
                               log.log(Level.INFO, "Manager ID: {0} , Customer ID: {1}{2}", new Object[]{ManagerID,customerID, msg});
                               break;
                        case 7: Thread t1=new Thread(()->{
                        		String msg1=null;
                        		try {
									msg1=add.addEvent("MTLM1234", "MTLM020220", "SEM", 1);
								} catch (RemoteException e) {
									
									e.printStackTrace();
								} catch (IOException e) {
									
									e.printStackTrace();
								}
                        		System.out.println("T1:  "+msg1);
                        		});
                        		
                        		Thread t2=new Thread(()->{
                            		String msg1=null;
                            		try {
                               			msg1=add.bookEvent("MTLC3456", "MTLM020220", "SEM");
    								} catch (RemoteException e) {
    									e.printStackTrace();
    								} catch (IOException e) {
    									e.printStackTrace();
    								}
                            		System.out.println("T2:  "+msg1);
                            		});
                            		
                            	Thread t3=new Thread(()->{
                                		String msg1=null;
                                		try {
                                			msg1=add.bookEvent("MTLC2345", "MTLM020220", "SEM");
        								} catch (RemoteException e) {
        									e.printStackTrace();
        								} catch (IOException e) {
        								}
                                		System.out.println("T3:  "+msg1);
                                		});
                                		
                                Thread t4=new Thread(()->{
                                    	String msg1=null;
                                    	try {
                                    		msg1=add.cancelEvent("MTLC2345", "MTLM020220", "SEM");
            							} catch (RemoteException e) {
            								e.printStackTrace();
            							} catch (IOException e) {
            								e.printStackTrace();
            							}
                                    	System.out.println("T4:  "+msg1);
                                   		});
                                		
                               	Thread t5=new Thread(()->{
                                   		String msg1=null;
                                   		try {
                                   			msg1=add.removeEvent("MTLM020220", "SEM");
            							} catch (RemoteException e) {
            									e.printStackTrace();
            								} catch (IOException e) {
            									e.printStackTrace();
            								}
                                    		System.out.println("T5:  "+msg1);
                                    	});
                                    		                               
                                    		t1.start();
                                    		t2.start();
                                    		t3.start();
                                    		t4.start();
                                    		t5.start();
                               
                        		
                               
                        case 8: break;
                        	
                    }
                }while(choice!=8);
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

