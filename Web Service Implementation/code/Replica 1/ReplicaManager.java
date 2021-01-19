package Helloapl;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.StringTokenizer;

public class ReplicaManager {
	
	static int count=0;
	DatagramSocket ds=null;
	DatagramSocket ds1=null;
	DatagramPacket dp=null;
	
	
	public static void main(String[] args) {
		
		ReplicaManager rm=new ReplicaManager();
		Runnable task1=()->{
			
			DatagramSocket ds=null;
			DatagramSocket ds1=null;
			
			
			while(true) {
				System.out.println("Receiveing Request from Sequencer");
				
				try {
					ds=new DatagramSocket(1600);
					byte[] buf=new byte[1000];
					String msg="";
					DatagramPacket dp=new DatagramPacket(buf,buf.length);
					ds.receive(dp);
					msg=new String(dp.getData());
					System.out.println("Message from Sequencer: "+msg);
					StringTokenizer st=new StringTokenizer(msg,",");
					int id=Integer.parseInt(st.nextToken());
					System.out.println("Id: "+id);
					String s=st.nextToken();
					System.out.println(s);
					char choice=s.charAt(0);
					switch(choice) {
					case 'M':  	InetAddress inet=InetAddress.getByName("230.0.0.1");
								rm.multicast(msg, inet);
								break;
						
					case 'T':	inet=InetAddress.getByName("230.0.0.2");
								rm.multicast(msg, inet);
								break;
						
					case 'O':	inet=InetAddress.getByName("230.0.0.3");
								rm.multicast(msg, inet);
								break;
						default:
							break;   
					}
					
					
				}
				catch(Exception e) {
					System.out.println("ReplicaManager-Sequencer Exception "+e.getMessage());
				}
				finally {
					if(ds!=null)
					{
						ds.close();
					}
					
				}
				
			}
		};
		Thread t1=new Thread(task1);
		t1.start();
		
		
		Runnable task2=()->{	
			
			DatagramSocket ds=null;
			while(true){
				try {
				System.out.println("Receiving FrontEnd Software Bug Message");
				ds=new DatagramSocket(3030);
				byte []buf=new byte[1000];
				String msg="";
				DatagramPacket dp=new DatagramPacket(buf,buf.length);
				ds.receive(dp);
				//System.out.println("count="+count);
				msg=new String(dp.getData());
				rm.count++;
				System.out.println("count+"+rm.count);
					if(rm.count==3){
						InetAddress ip=InetAddress.getByName("localhost");
						ds.send(new DatagramPacket("Bug".getBytes(),"Bug".length(),ip,2000));
					}
				}
				catch(Exception e) {
					System.out.println("Main: "+e.getMessage());
				}
				finally {
					if(ds!=null){
						ds.close();
					}
				}
				}
		};
			Thread t2=new Thread(task2);
			t2.start(); 
	}

	private void multicast(String msg, InetAddress inet) {
		try
		{	
			System.out.print(msg);
			ds=new DatagramSocket();
			//sending msg to replicas on 1700 port number
			dp=new DatagramPacket(msg.getBytes(),msg.length(),inet,1700);
			ds.send(dp);
		}
		catch(Exception e)
		{
			System.out.println("Sequencer Multicast Exception: "+e.getMessage());
		}
		finally
		{
			if(ds!=null)
			{
				ds.close();
			}
		}	
		
	}
	
}
