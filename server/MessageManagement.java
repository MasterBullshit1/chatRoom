import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;


public class MessageManagement implements  Runnable {
	Message message;
	Server server;
	
	public MessageManagement(Server s){
		server=s;
	}
	
	public void handleMessage(){
		if((message=Server.receiveMessage.poll())==null)
		{
//			System.out.println("-------------------------------------------------------------------");
//			System.out.println("log :there is no message need to deal with (handleMessage() method)");
//			System.out.println("-------------------------------------------------------------------");
			return;
		}
		System.out.println("-------------------------------------------------------------------");
		
		System.out.println("log :begin deal with message (messageManagement) :"+message.type+"|"+message.getFrom()+"|"+message.getTo()+"|"+message.getContent());
		if(message.type==MessageType.CHAT){
		dealChat();
		
		}else if(message.type==MessageType.LOGOUT){
		dealLogOut();
		
		}else if(message.type==MessageType.LOGIN){
		dealLogIn();
		
		}else if(message.type==MessageType.SIGNIN){
		dealSignIn();	
		
		}else if(message.type==MessageType.UNKNOW){
		dealUnknow();	
		}
		
		System.out.println("-------------------------------------------------------------------");
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(true){
		handleMessage();
		}
	}
	
	public void dealChat(){
		server.addMessageNeedtoSend(message);
	}
	
	public void dealLogIn(){
		System.out.println("log : deal Login message!");
		
		if(!server.checkUser(message.getFrom(),message.getContent()))
			return;
		
		server.changeState(message.getFrom(), true);
		Message mes=new Message(MessageType.SERVER,"server",message.getFrom(),"success");
		
		if(!server.message.containsKey(message.getFrom()))
				server.message.put(message.getFrom(), new LinkedBlockingQueue<Message>());
		server.message.get(message.getFrom()).add(mes);
		
		System.out.println("log :------"+server.message.get(message.getFrom()).peek());
		System.out.println("log : add \"success\" to queue");
		
		
		server.sendFriendInfo(message.getFrom());
		
		System.out.println("log : deal Login message end!");
	}
	
	public void dealLogOut(){
		server.changeState(message.getFrom(), false);
		server.sendStateToClients(message.getFrom());
		
		server.connectionManagement.isOnline.remove(message.getFrom());
	}
	
	public void dealSignIn(){
		
		String ID=String.format("%tQ",new Date());
		server.addUserToFile(message.getFrom(), ID, message.getContent());
		
		server.addMessageNeedtoSend(new Message(MessageType.SERVER,"server",message.getFrom(),"you have signed in! welcome to use XXXXX"));
	}
	
	public void dealUnknow(){
		System.out.println("log : cant deal with this type of message!");
	}
}
