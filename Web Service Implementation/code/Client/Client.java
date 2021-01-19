package Helloapl;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import Helloapll.Hello;
import Helloapll.HelloHelper;

import org.omg.CORBA.*;
import org.omg.CORBA.ORBPackage.InvalidName;

public class Client 
{
	  
	static Hello helloImpl;
	public static void main(String args[]) throws NotBoundException, IOException, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName{
		ORB orb = ORB.init(args, null);
		int status=0;
    	do {	
    	    int port = 0,choice;
            Scanner s = new Scanner(System.in);
            Scanner s1 = new Scanner(System.in);
            Scanner s2 = new Scanner(System.in);
            Scanner s3 = new Scanner(System.in);
            Scanner s4 = new Scanner(System.in);
            System.out.println("Enter ID:");
          String clientID=s.nextLine();
           
            //String name="";
            String server = clientID.substring(0, 3);
            char person = clientID.charAt(3);
            System.out.println(server+" "+person);
            org.omg.CORBA.Object obj;
	    	try {
				obj = orb.resolve_initial_references("NameService");
			
			
			// Use NamingContextExt instead of NamingContext. This is 
	        // part of the Interoperable naming Service.  
	        NamingContextExt ncRef = NamingContextExtHelper.narrow(obj);
	 
	        // resolve the Object Reference in Naming
	        String name = "Hello1";
	        helloImpl = HelloHelper.narrow(ncRef.resolve_str(name));
	        
			} catch (InvalidName e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotFound e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CannotProceed e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          /*
            
            Thread t1 = new Thread(()->{
		    	
		    	org.omg.CORBA.Object obj;
		    	try {
					obj = orb.resolve_initial_references("NameService");
				
				
				// Use NamingContextExt instead of NamingContext. This is 
		        // part of the Interoperable naming Service.  
		        NamingContextExt ncRef = NamingContextExtHelper.narrow(obj);
		 
		        // resolve the Object Reference in Naming
		        String name = "Hello1";
		        helloImpl = HelloHelper.narrow(ncRef.resolve_str(name));
		        
				} catch (InvalidName e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFound e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CannotProceed e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	System.out.println("t1 -> "+helloImpl.bookEvent("MTLC1234", "MTLA010119", "SEM"));
		    	
		
		    });
			
			
		  Thread t2 = new Thread(()->{
		    	
		    	
		    	org.omg.CORBA.Object obj;
				try {
					obj = orb.resolve_initial_references("NameService");
				
				
				// Use NamingContextExt instead of NamingContext. This is 
		        // part of the Interoperable naming Service.  
		        NamingContextExt ncRef = NamingContextExtHelper.narrow(obj);
		 
		        // resolve the Object Reference in Naming
		        String name = "Hello1";
		        helloImpl = HelloHelper.narrow(ncRef.resolve_str(name));
		        
				} catch (InvalidName e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFound e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CannotProceed e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	System.out.println("t2 -> "+helloImpl.bookEvent("MTLC2323", "MTLM020119", "CON"));
		
		    });
		     Thread t3 = new Thread(()->{
		    	org.omg.CORBA.Object obj;
				try {
					obj = orb.resolve_initial_references("NameService");
				// Use NamingContextExt instead of NamingContext. This is 
		        // part of the Interoperable naming Service.  
		        NamingContextExt ncRef = NamingContextExtHelper.narrow(obj);
		 
		        // resolve the Object Reference in Naming
		        String name = "Hello1";
		        helloImpl = HelloHelper.narrow(ncRef.resolve_str(name));
		        
				} catch (InvalidName e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFound e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CannotProceed e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	System.out.println("t3 -> "+helloImpl.bookEvent("MTLC2423", "MTLM030119", "TS"));
		
		    });
		    
		    Thread t4 = new Thread(()->{
		    	org.omg.CORBA.Object obj;
				try {
					obj = orb.resolve_initial_references("NameService");
				// Use NamingContextExt instead of NamingContext. This is 
		        // part of the Interoperable naming Service.  
		        NamingContextExt ncRef = NamingContextExtHelper.narrow(obj);
		        // resolve the Object Reference in Naming
		        String name = "Hello1";
		        helloImpl = HelloHelper.narrow(ncRef.resolve_str(name));
				} catch (InvalidName e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFound e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CannotProceed e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	System.out.println("t4"+helloImpl.cancelEvent("MTLM1234", "MTLA010119", "SEM"));
		    });
		    org.omg.CORBA.Object obj;
		    try {
				obj = orb.resolve_initial_references("NameService");  
				NamingContextExt ncRef = NamingContextExtHelper.narrow(obj);        
				String name = "Hello1";
				helloImpl = HelloHelper.narrow(ncRef.resolve_str(name));
			} catch (InvalidName e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		    t1.start();
		   	t2.start();
		    t3.start();
			t4.start();
       */     
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
                             //     s1.close();
                            	//	s2.close();
                                //  log.log(Level.INFO, "Customer ID: {0}{1}", new Object[]{clientID, msg});
                                  break;
                              
                          case 2: 
                        	  msg=helloImpl.getBookingSchedule(clientID);
                        	  System.out.println(msg);                             
                                   //RMI method
                                   break;
                          case 3: System.out.println("Enter Event ID to drop:");
                          		eventID=s1.nextLine();
                          		System.out.print("Enter Event Type:");
                          		eventType=s2.nextLine(); 
                          		msg=helloImpl.cancelEvent(clientID, eventID, eventType);
                          		System.out.println(msg);
                          	//	s1.close();
                          	//	s2.close();
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
                    
                    System.out.println("1.Add Event");
                    System.out.println("2.Remove Event");
                    System.out.println("3.List Event Availability");
                    System.out.println("4.Book Event");
                    System.out.println("5.Get Booking Schedule");
                    System.out.println("6.Cancel Event");
                    System.out.println("7.Swap Event");
                    System.out.println("8.MultiThreading task");
                    System.out.println("9.Logout");
                    System.out.println("Enter Choice: ");
                    
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
                                msg = helloImpl.removeEvent(ManagerID,eventID, eventType);
                                System.out.println(msg); 
                               
                               // log.log(Level.INFO, "Manager ID: {0}{1}", new Object[]{ManagerID, msg});
                                
                                break;
                            
                        case 3: System.out.println("Enter Event Type:");
                                eventType= s1.nextLine();
                                msg=helloImpl.listEventAvailability(ManagerID,eventType);
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
                        case 8: Thread th1=new Thread(()->{
                        		String msg1=null;
                        		try {
									msg1=helloImpl.addEvent("MTLM1234", "MTLM020220", "SEM", 1);
								} catch (Exception e) {
									
									e.printStackTrace();
								}
                        		System.out.println("TH1:  "+msg1);
                        		});
                        		
                        		Thread th2=new Thread(()->{
                            		String msg1=null;
                            		try {
                               			msg1=helloImpl.bookEvent("MTLC3456", "MTLM020220", "SEM");
    								} catch (Exception e) {
    									
    									e.printStackTrace();
    								}
                            		System.out.println("TH2:  "+msg1);
                            		});
                            		
                            	Thread th3=new Thread(()->{
                                		String msg1=null;
                                		try {
                                			msg1=helloImpl.bookEvent("MTLC2345", "MTLM020220", "SEM");
        								}  catch (Exception e) {
        									
        									e.printStackTrace();
        								}
                                		System.out.println("TH3:  "+msg1);
                                		});
                                		
                                Thread th4=new Thread(()->{
                                    	String msg1=null;
                                    	try {
                                    		msg1=helloImpl.cancelEvent("MTLC2345", "MTLM020220", "SEM");
            							} catch (Exception e) {
        									
        									e.printStackTrace();
        								}
                                    	System.out.println("TH4:  "+msg1);
                                   		});
                                		
                               	Thread th5=new Thread(()->{
                                   		String msg1=null;
                                   		try {
                                   			msg1=helloImpl.removeEvent("MTLC1234", "MTLM020220", "SEM");
            							}  
                                   		catch (Exception e) {
        									
        									e.printStackTrace();
        								}
                                    		System.out.println("TH5:  "+msg1);
                                    	});
		                		th1.start();
		                		th2.start();
		                		th3.start();
		                		th4.start();
		                		th5.start();
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

