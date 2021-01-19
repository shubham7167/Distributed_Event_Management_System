package dsd;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Event_Interface extends Remote
{
   public String addEvent(String managerID,String eventID, String eventType, int bookingCapacity)throws RemoteException,IOException;
   public String removeEvent(String eventID,String eventType)throws RemoteException;
   public String listEventAvailability(String eventType)throws RemoteException;
   
   public String bookEvent(String customerID, String eventID, String eventType)throws RemoteException,IOException;
   public String getBookingSchedule(String customerID)throws RemoteException;
   public String cancelEvent(String customerID, String eventID, String eventType)throws RemoteException;
}
