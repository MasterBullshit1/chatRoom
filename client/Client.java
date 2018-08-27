import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {
	
	public static Queue<Message> messageInput=new LinkedBlockingQueue<Message>();
	public static Queue<Message> messageOutput=new LinkedBlockingQueue<Message>();
	public static Map<String ,LinkedBlockingQueue<Message>> cachedMessage=new HashMap<String ,LinkedBlockingQueue<Message>>();
	static UIFrame frame;
	 ConnectionManage connection;
	 MessageManage messageManage;
	Thread messageManageThread;
	Thread connectionThread;
	
	
	boolean closeMaps=false;
	public Client(){
		
	
		frame=new UIFrame(this);
		
		connection=new ConnectionManage(this);
		connectionThread=new Thread(connection);
		connectionThread.start();
		
		messageManage=new MessageManage(this);
		messageManageThread=new Thread(messageManage);
		messageManageThread.start();
		
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		new Client();
//		try {
//			Thread.sleep(400);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		frame.mainFrame.setVisible(true);
//	try {
//		Thread.sleep(2000);
//	} catch (InterruptedException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	ui.mainFrame.setSize(300, 400);
	}

}
