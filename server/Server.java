import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.*;
public class Server {
	
	public static Queue<Message> receiveMessage=new LinkedBlockingQueue<Message>();
	public static Map<String, LinkedBlockingQueue<Message>> message=new HashMap<String,LinkedBlockingQueue<Message>>();
	public static Map<String,User> users=new HashMap<String,User>();
	
	File file;
	BufferedReader fileReader;
	BufferedWriter fileWriter;
	FileReader reader;
	FileWriter writer;
	
	ConnectionManagement connectionManagement;
	MessageManagement messageManagement;
	
	Thread messageThread;
	Thread connectThread;
	
	public Server(){
		init();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Server server=new Server();
		server.messageThread=new Thread(server.messageManagement);
		server.connectThread=new Thread(server.connectionManagement);
		server.messageThread.start();
		server.connectThread.start();
		
		
		System.out.println("..........................");
		
		
	}
	
	public  void init(){
		
		connectionManagement=new ConnectionManagement(this);
		messageManagement=new MessageManagement(this);
		file=new File("userlist.txt");
		try {
			if(!file.exists())
				file.createNewFile();
			System.out.println("log :file path :"+file.getAbsolutePath());
			reader=new FileReader(file);
			fileReader=new BufferedReader(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		loadAllUsers();
	}
	
	public void sendFriendInfo(String name){
		String[] str=Server.users.get(name).getInfo().split(" ");
		int i=0;
		while(i<str.length){
			String state=null;
			if(Server.users.get(str[i]).getState())
				state="true";
			else
				state="false";
			
			Message mes=new Message(MessageType.SERVER,"server",name,str[i]+" "+state);
			Server.message.get(name).add(mes);
			
			i++;
			System.out.println("Log : add friendInfo to queue");
			
		}
	}
	
	public void sendStateToClients(String name){
		String[] str=Server.users.get(name).getInfo().split(" ");
		int i=0;
		while(i<str.length){
			Message mes=new Message(MessageType.SERVER,"server",str[i],name+" false");
			
			if(!Server.message.containsKey(str[i]))
				Server.message.put(str[i], new LinkedBlockingQueue<Message>());
			//System.out.println("log :-------name:"+str[i]);
			Server.message.get(str[i]).add(mes);
			i++;
		}
	}
	
	public boolean checkUser(String name,String pw){
		System.out.println("log :|"+name+"|"+pw+"|");
		if(!users.containsKey(name)){
			System.out.println("log : user doesn't exist!");
			return false;
			// send message to client
		}else if(!users.get(name).getPw().equals(pw)){
			System.out.println("log : password is wrong! --pw:|"+users.get(name).getPw()+"|");
			return false;
			//send message to client
		}
		return true;
	}
	
	public void loadAllUsers(){
		System.out.println("log : load all user!");
		String str=null;
		User user;
		try {
			while((str=fileReader.readLine())!=null){
				String[] values=str.split("\\|");
				String name=values[0];
				String ID=values[1];
				String pw=values[2];
				String info=values[3];
				user=new User(name,ID,pw,false,info);
				Server.users.put(name, user);
				
				System.out.println("log : load user:"+name+"|"+ID+"|"+pw+"|false|"+info);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
		try {
			reader.close();
			fileReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	
	
	public void addUserToFile(String name,String ID,String pw){
		try {
			writer=new FileWriter(file);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		fileWriter=new BufferedWriter(writer);
		
		try {
			fileWriter.write(name+"|"+ID+"|"+pw+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Server.users.put(name, new User(name,ID,pw,false));
		
		try {
			fileWriter.flush();
			writer.close();
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void changeState(String name,boolean st){
		
		if(Server.users.containsKey(name)){
		Server.users.get(name).setState(st);
		System.out.println("log :"+name+" state:"+Server.users.get(name).getState());
		}else{
			System.out.println("log :cant change the "+name+"'s state!!! cant find the user in list");
		}
	}
	
	public boolean getUserState(String name){
		
		return Server.users.get(name).getState(); 
	}
	
	public void addMessageNeedtoSend(Message mes){
		
		if(!message.containsKey(mes.getTo()))
			message.put(mes.getTo(), new LinkedBlockingQueue<Message>());
		
		message.get(mes.getTo()).add(mes);
		System.out.println("log :add chat message to queue!!");
		
	}
	
	
}
