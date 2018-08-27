import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.*;


public class MessageManage implements Runnable {

	Client client;
	Message message;
	
	public MessageManage(Client c){
		client=c;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			
			if((message=client.messageInput.poll())==null){
				;//do nothing
			}else{
				System.out.println("log : deal with message !!");
				if(message.type==MessageType.CHAT)
				processChatMessage();
				else if(message.type==MessageType.SERVER)
					processServerMessage();
				else
					processUnknowMessage();
			}
			
			try {
				Thread.sleep(140);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	 void processChatMessage(){
		 System.out.println("log :deal chat message!!");
		 
		if(client.frame.chatWindowList.containsKey(message.getFrom())){
//			Object obj=client.frame.chatWindowList.get(message.getFrom()).getComponent(0);
//			((JTextArea)(((JScrollPane)obj).getComponent(3))).append(message.getContent());;
			client.frame.mesTextAreaList.get(message.getFrom()).append("---------"+String.format("%tr",UIFrame.date)+"-------\n"+message.getContent()+"\n");
			
			System.out.println("log : append chat message to window!!");
		}
		else{
			
			if(!client.cachedMessage.containsKey(message.getFrom())){
				LinkedBlockingQueue<Message> queue=new  LinkedBlockingQueue<Message>();
				queue.add(message);
				client.cachedMessage.put(message.getFrom(), queue);
			}else{
				client.cachedMessage.get(message.getFrom()).add(message);
			}
			client.frame.showMessageNum(message.getFrom(), client.cachedMessage.get(message.getFrom()).size());
			
			System.out.println("log :add chat message to cached!");
		}
	}
	
	 void processServerMessage(){  
		 System.out.println("log :deal server message!!");
		 if(message.getContent().equals("success")){
		 	client.frame.userName=message.getTo();
		 	client.frame.nameLabel.setText(message.getTo());
		 	
		 	client.frame.closeLoginUI();
		 
		 }else{
		
			 if(message.getContent().split(" ").length==2)
				 client.frame.addUser(message);
			 else
				 client.frame.addChatRoom(message);
		
		 }
	}
	
	 void processUnknowMessage(){
		 System.out.println("MessageManage :unknow type message!");
	 }
}
