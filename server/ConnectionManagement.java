import java.util.*;
import java.io.IOException;
import java.net.*;
import java.io.*;

public class ConnectionManagement implements Runnable {
	
	Server server;
	int port=8998;
	
	Map<String ,String> isOnline=new HashMap<String,String>();
	
	Collection<User> userList=new ArrayList<User>();
	Collection<Thread> connections=new ArrayList<Thread>();
	int maxListenNum=100;
	
	private ServerSocket serverSocket;
	
	public ConnectionManagement(Server s){
		server=s;
	}

	
	public void addUserToList(User u){
		if(!(userList.contains(u))){
			userList.add(u);
		}
		else{
			
		}
		 
	}
	public void listen(){
		
		try {
			serverSocket=new ServerSocket(port);
			while(true){
				System.out.println("log : wait client to connect");
				Socket client=serverSocket.accept(); 
				
				System.out.println("log : a client connect! ip:"+client.getInetAddress());
				
				
				Thread conThread=new Thread(new ClientConnection(client,this));
				conThread.start();
				
				connections.add(conThread);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("-------------------------------------------");
		System.out.println("log :enter ConnectionManagement() method");
		while(true){
		listen();
		}
	}
}


class ClientConnection implements Runnable{
	
	String userName=null;
	boolean hasLogin=false;
	boolean fristTimeGetReaderContent=true;
	Socket client;
	ConnectionManagement manager;
	
	BufferedReader reader;
	PrintWriter writer;
	
	Thread sendThread;
	
	public ClientConnection(Socket sock,ConnectionManagement manag){
		client=sock;
		manager=manag;
		
	}
	
	
	public void run() {
		
		System.out.println("log :new connection thread");
		
		try {
			writer=new PrintWriter(client.getOutputStream());
			reader=new BufferedReader(new InputStreamReader(client.getInputStream()));
			//writer=new PrintWriter(client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		sendThread=new Thread(new Send(writer,this));
		sendThread.start();
		System.out.println("log : thread name :"+userName);
		
		while(true){
			String mes;
				
				System.out.println("log :wait for message");
			try {
				if((mes=reader.readLine())!=null){

					System.out.println("log : receive from IO:"+mes);
					String[] str=mes.split("\\|");
					
					String type=str[0];
					String from=str[1];
					String to=str[2];
					String content=str[3];
					
					
					if(fristTimeGetReaderContent&&type.equals("LOGIN")){
						userName=from;
						fristTimeGetReaderContent=false;
						System.out.println("log : thread userName :"+userName);
					}
					
					MessageType t=MessageType.UNKNOW;
					switch(type){
					case "LOGIN":
						t=MessageType.LOGIN;
						break;
					case "LOGOUT":
						t=MessageType.LOGOUT;
						break;
					case "SIGNIN":
						t=MessageType.SIGNIN;
						break;
					case "CHAT":
						t=MessageType.CHAT;
						break;
					case "SERVER":
						System.out.println("log :server never receive message from server!!!!");
						break;
					default :
						System.out.println("log :unknow message type :"+type);
					}
					
					
					Message message=new Message(t,from,to,content);
					Server.receiveMessage.add(message);
					System.out.println("log :add message to queue : "+mes);
				}
				else{
					System.out.println("log : receive null");
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("error: connection (read) closed!  userName:"+userName);
				
				break;
			}
			
			
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//break the thread!!!
			if((!manager.isOnline.containsKey(userName))&&hasLogin){
				System.out.println("log : break write thread!!");
				break;
			}
			
		}
	}
	
}

class Send implements Runnable {
	PrintWriter writer;
	ClientConnection clientConnection;
	Message message;
	boolean hasLogin=false;
	int num=0;
	public Send(PrintWriter w,ClientConnection c){
		writer=w;
		clientConnection=c;
	}
	
	@Override
	public void run() {
		System.out.println("log : send thread --");
		// TODO Auto-generated method stub
		while(true){
			if(((message=getMessageFromQueue(clientConnection.userName))!=null)){
				System.out.println("log : get message from queue!!");
				String type;
				if(message.type==MessageType.CHAT)
					type="CHAT";
				else if(message.type==MessageType.SERVER)
					type="SERVER";
				else 
					type="UNKNOW";
				
				
				if((message.type==MessageType.SERVER)&&(message.getContent().equals("success"))){
					hasLogin=true;
					clientConnection.hasLogin=true;
					System.out.println("log : send thread--userName-"+clientConnection.userName);
					clientConnection.manager.isOnline.put(clientConnection.userName, "true");
					num++;
				}
					
				if(hasLogin&&(num>=1))
					clientConnection.userName=message.getTo();
				else
					clientConnection.userName=null;
				
					
				writer.write(type+"|"+message.getFrom()+"|"+message.getTo()+"|"+message.getContent()+"\n");
				writer.flush();
				System.out.println("log :send message thread userName:"+clientConnection.userName+"--"+type+"|"+message.getFrom()+"|"+message.getTo()+"|"+message.getContent());
			}
			
			
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// break the thread!!!!
			if((!clientConnection.manager.isOnline.containsKey(clientConnection.userName))&&hasLogin){
				System.out.println("log : break send thread!!");
				break;
			}
		}
	}
	
	public Message getMessageFromQueue(String name){
		if(!clientConnection.manager.server.message.containsKey(name)){
			return null;
		}else{
			return clientConnection.manager.server.message.get(name).poll();
		}
	}
}