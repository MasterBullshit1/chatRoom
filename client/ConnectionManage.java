import java.io.*;
import java.net.*;
public class ConnectionManage implements Runnable{

	Client client;
	public boolean isLogout=false;
	
	String ip="127.0.0.1";
	int port=8998;
	Socket socket;
	
	Send send;
	Thread writerThread;
	BufferedReader reader;
	PrintWriter writer;
	
	public ConnectionManage(Client c){
		this.client=c;
		
	}
	
	@Override
	public void run() {
		
		try {
			System.out.println("log : begin connect...");
			socket=new Socket(ip,port);
			System.out.println("log : connected to server!!");
			reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer=new PrintWriter(socket.getOutputStream());
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		send=new Send(this,writer);
		writerThread=new Thread(send);
		writerThread.start();
		
		String str;
		String[] strArray;
		
		while(true){
		try {
			System.out.println("log : wait for message!");
			if((str=reader.readLine())!=null){
				strArray=str.split("\\|");
			
				MessageType type=MessageType.UNKNOW;
				switch(strArray[0]){
				case "CHAT":
					type=MessageType.CHAT;
					break;
				case "LOGIN":
					type=MessageType.LOGIN;
					break;
				case "SERVER":
					type=MessageType.SERVER;
					break;
				case "LOGOUT":
					type=MessageType.LOGOUT;
					break;
				case "SIGNIN":
					type=MessageType.SIGNIN;
					break;
				default:
						type=MessageType.UNKNOW;
				}
				
				Message mes=new Message(type,strArray[1],strArray[2],strArray[3]);
				
				System.out.println("log :receive message:"+strArray[0]+"|"+strArray[1]+"|"+strArray[2]+"|"+strArray[3]+"|");
				client.messageInput.add(mes);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(140);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(isLogout)
			break;
		
		}
		 
		
	}

}

class Send implements Runnable{

	public	boolean isLogout=false;
	ConnectionManage connection;
	PrintWriter writer;
	Message message;
	
	public Send(ConnectionManage c,PrintWriter w){
		this.connection=c;
		this.writer=w;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			if((message=connection.client.messageOutput.poll())==null){
				;//do nothing
			}else{
			
			String type;
			if(message.type==MessageType.CHAT)
				type="CHAT";
			else if(message.type==MessageType.SERVER)
				type="SERVER";
			else if(message.type==MessageType.LOGOUT)
				type="LOGOUT";
			else if(message.type==MessageType.LOGIN)
				type="LOGIN";
			else if(message.type==MessageType.SIGNIN)
				type="SIGNIN";
			else 
				type="UNKNOW";
				
			writer.write(type+"|"+message.getFrom()+"|"+message.getTo()+"|"+message.getContent()+"\n");
			writer.flush();
			System.out.println("log :send message :"+type+"|"+message.getFrom()+"|"+message.getTo()+"|"+message.getContent()+"\n");
			
			
			if(type.equals("LOGOUT"))
				connection.client.closeMaps=true;
			}
			
			try {
				Thread.sleep(140);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(isLogout)
				break;
			
		}
		
	}
	
}