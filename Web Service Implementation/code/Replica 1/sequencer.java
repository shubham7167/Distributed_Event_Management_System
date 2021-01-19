package Helloapl;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class sequencer {
	
	static int unique_id=0;
	DatagramSocket ds=null;
	DatagramPacket dp=null;

	public static void main(String[] args) {
		
		sequencer se=new sequencer();
		
		Runnable task1=()-> {
			DatagramSocket ds1=null;
			while(true){
				System.out.println("Receiving Request from FrontEnd");
				try {
					ds1=new DatagramSocket(7005);
					byte[] buf=new byte[1000];
					String msg="";
					DatagramPacket dp1=new DatagramPacket(buf,buf.length);
					ds1.receive(dp1);
					msg=new String(dp1.getData());
					System.out.println("Message from FrontEnd: "+msg);
					se.unicast(msg);
							
					
				}
				catch(Exception e) {
					System.out.println("Thread exception "+e.getMessage());
				}
				finally {
					if(ds1!=null) {
						ds1.close();
					}
				}
			}
		};
		
		Thread t1=new Thread(task1);
		t1.start();
		
	}

	private void unicast(String msg) {
		
		try {
			System.out.println(unique_id+", "+msg);
			ds=new DatagramSocket();
			InetAddress inet=InetAddress.getByName("localhost");
			msg=unique_id+","+msg;
			dp=new DatagramPacket(msg.getBytes(),msg.length(),inet,1600);
			ds.send(dp);
			unique_id++;
			
			
		} 
		catch (Exception e) {
			System.out.println("Squencer Unicast Exception: "+e.getMessage());
		}
		finally {
			if(ds!=null) {
				ds.close();
			}
		}
	}
}
