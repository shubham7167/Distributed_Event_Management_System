package Helloapl;




public interface Event_Interface 
{
   public String addEvent(String managerID,String eventID, String eventType, int bookingCapacity);
   public String removeEvent(String eventID,String eventType);
   public String listEventAvailability(String eventType);
   
   public String bookEvent(String customerID, String eventID, String eventType);
   public String getBookingSchedule(String customerID);
   public String cancelEvent(String customerID, String eventID, String eventType);
}
