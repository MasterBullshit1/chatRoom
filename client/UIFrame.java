import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

public class UIFrame {
	
	 Map<String, JPanel> chatWindowList=new HashMap<String,JPanel>();
	 Map<String,Integer> index=new HashMap<String,Integer>();
	 Map<String,JTextArea> textAreaList=new HashMap<String,JTextArea>();
	 Map<String ,JTextArea> mesTextAreaList=new HashMap<String, JTextArea>();
	 
	 static Date date=new Date();
	 
	 String userName=null;
	 JPanel currentWindow=null;
	 Login login;
	 int winNum=0;
	 
	private Client client;
	JFrame mainFrame;
	JSplitPane splitPane;
	JPanel leftPanel;
	JPanel rightPanel;
	
	JLabel nameLabel;
	JButton logoutButton;
	JButton addButton;
	
	JTabbedPane rightTab;
	JTabbedPane leftTab;
	
	JPanel friendListPanel;
	JPanel ChatRoomPanel;
	
	JTextArea area1;
	JTextArea area2;
	
	JScrollPane friScrollPane;
	JScrollPane chaScrollPane;
	Map<String, JPanel> friendList;
	Map<String, JPanel> chatRoomList;
	Map<String,JLabel> mesNumList;
	Map<String,JLabel> stateList;
	
	int num_c=0;
	int num=0;
	int offset=5;
	int width=180;
	int height=45;
	
	public UIFrame(Client c){
		client=c;
		login=new Login(client,this);
		
		mainFrame=new JFrame();
		mainFrame.setBounds(700, 200, 540, 500);
		mainFrame.setLayout(null);
		mainFrame.setResizable(false);
		Container con=mainFrame.getContentPane();
		
		splitPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerSize(4);
		splitPane.setDividerLocation(325);
		splitPane.setBounds(0, 0, 540, 500);
//		splitPane.setLeftComponent(null);
//		splitPane.setRightComponent(new JLabel("right"));
		con.add(splitPane);
		//---------------------
		leftPanel=new JPanel();
		rightPanel=new JPanel();
		
		//----------------------
		rightPanel.setLayout(null);
		nameLabel=new JLabel("user name");
		nameLabel.setBounds(10, 10, 100, 30);
		rightPanel.add(nameLabel);
		
		logoutButton=new JButton(" 注销 ");
		logoutButton.setBounds(120, 10, 70, 17);
		rightPanel.add(logoutButton);
		
		addButton=new JButton("add");
		addButton.setBounds(120, 30, 70, 20);
		rightPanel.add(addButton);
		
		rightTab=new JTabbedPane();
		rightTab.setBounds(5, 50, 190, 410);
		rightPanel.add(rightTab);
		friendListPanel=new JPanel();
		ChatRoomPanel=new JPanel();
		friendListPanel.setSize(190, 410);
		friendListPanel.setLayout(null);
		ChatRoomPanel.setSize(190, 410);
		ChatRoomPanel.setLayout(null);
		rightTab.addTab("好友", friendListPanel);
		rightTab.addTab("群", ChatRoomPanel);
		//----------------------------------
		leftTab=new JTabbedPane();
		leftPanel.setLayout(null);
		leftPanel.add(leftTab);
		leftTab.setBounds(10, 10, 300, 450);
		//----------------------------
		JPanel chatWindow=new JPanel();
		chatWindow.setLayout(null);
		area1=new JTextArea();
		area2=new JTextArea();
		JScrollPane scroll1=new JScrollPane(area1);
		JScrollPane scroll2=new JScrollPane(area2);
		scroll1.setBounds(5, 22, 285,290);
		scroll2.setBounds(5, 340,285,75);
		area1.setBackground(Color.gray);
		area2.setBackground(Color.gray);
		chatWindow.add(scroll1);
		chatWindow.add(scroll2);
		
		JButton b2=new JButton("发送");
		JButton b1=new JButton("关闭");
		b1.setBounds(215, 1, 70, 19);
		b2.setBounds(215, 315, 70, 20);
		chatWindow.add(b1);
		chatWindow.add(b2);
		leftTab.addTab("test",chatWindow);
		
		//------------------------------------
		
		friScrollPane=new JScrollPane();
		chaScrollPane=new JScrollPane();
		friScrollPane.setSize(190, 410);
		chaScrollPane.setSize(190, 410);
		friScrollPane.setLayout(null);
		chaScrollPane.setLayout(null);

		friendList=new HashMap<String,JPanel>();
		chatRoomList=new HashMap<String,JPanel>();
		mesNumList=new HashMap<String,JLabel>();
		stateList=new HashMap<String,JLabel>();
		
		friendListPanel.add(friScrollPane);
		friendListPanel.add(chaScrollPane);
		//------------------test user------------------------------
//		
//		JPanel panel=new JPanel();
//		panel.setBackground(Color.gray);
//		panel.setLayout(null);
//		JLabel lname=new JLabel("test user");
//		JLabel lstate=new JLabel("在线");
//		lname.setBounds(2, 2, 60, 20);
//		lstate.setBounds(2, 23,60, 20);
//		JLabel num1=new JLabel("未读消息 :"+0);
//		num1.setBounds(100, 2, 90, 20);
//		panel.add(lname);
//		panel.add(lstate);
//		panel.add(num1);
//		
//		panel.setBounds(3,3+ num*(offset+height), width, height);
//		num++;
//		
//		panel.addMouseListener(new OpenListener(this));
//		friendList.put("test user", panel);
//		friScrollPane.add(panel);
		//--------------------------------------------------------------
		//--------------------------
		
		
		
		logoutButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Message mes=new Message(MessageType.LOGOUT,userName,"server","logout");
				client.messageOutput.add(mes);
				System.out.println("log : add logout message to queue!!");
				
				boolean bo=true;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				while(bo){
					if(client.closeMaps==true){
						
						mainFrame.setVisible(false);
						login.jf.setVisible(true);
				
						chatWindowList.clear();
						index.clear();
						textAreaList.clear();
						mesTextAreaList.clear();
				
						friendList.clear();;
						chatRoomList.clear();;
						mesNumList.clear();;
						stateList.clear();;
				
						client.messageInput.clear();
						client.messageOutput.clear();
						client.cachedMessage.clear();
				
						//close Thread
				
						client.connection.isLogout=true;
						client.connection.send.isLogout=true;
						
						bo=false;
						System.out.println("log :maps closed!!!");
						client.connection=new ConnectionManage(client);
						client.connectionThread=new Thread(client.connection);
						client.connectionThread.start();
					}
				}
			}
			
		});
		
		
		addButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				final JFrame jf=new JFrame();
				jf.setLayout(null);
				jf.setBounds(900, 300, 340, 160);
				JLabel namel=new JLabel("user name");
				namel.setBounds(20, 45, 70, 50);
				final JTextField tx=new JTextField();
				tx.setBounds(100, 45, 110, 40);
				JButton b=new JButton("OK");
				b.setBounds(230, 45, 80, 40);
				
				jf.add(namel);
				jf.add(tx);
				jf.add(b);
				
				b.addActionListener(new ActionListener(){
					
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						Message mes=new Message(MessageType.SERVER,userName,"server",tx.getText());
						client.messageOutput.add(mes);
						jf.setVisible(false);
					}
					
				});
				jf.setVisible(true);
			}
			
		});
		//-----------------------------
		splitPane.setLeftComponent(leftPanel);
		splitPane.setRightComponent(rightPanel);
		//------------------------
		mainFrame.setVisible(false);
		
		
	}
	

	public void addUser(Message mes){
	
		String[] str=mes.getContent().split(" ");
		
		if(friendList.containsKey(str[0]))
		{
			String state=null;
			if(str[1].equals("true"))
				state="在线";
			else
				state="离线";
			System.out.println("log :set friend state!!");
			stateList.get(str[0]).setText(state);;
		}else{
			
		JPanel panel=new JPanel();
		panel.setBackground(Color.gray);
		panel.setLayout(null);
		JLabel lname=new JLabel(str[0]);
		JLabel lstate;
		System.out.println("log :state --"+str[1]);
		if(str[1].equals("true"))
			lstate=new JLabel("在线");
		else 
			lstate=new JLabel("离线");
		
		lname.setBounds(2, 2, 60, 20);
		lstate.setBounds(2, 23,60, 20);
		JLabel num1=new JLabel("未读消息 :"+0);
		num1.setBounds(100, 2, 90, 20);
		panel.add(lname);
		panel.add(lstate);
		panel.add(num1);
		
		panel.setBounds(3, num*(offset+height), width, height);
		num++;
		
		panel.addMouseListener(new OpenListener(this));
		
		friendList.put(str[0], panel);
		mesNumList.put(str[0], num1);
		stateList.put(str[0], lstate);
		
		friScrollPane.add(panel);
		}
	}
	
//	public void changeState(String name,String state){
//		
//		stateList.get(name).setText(state);
//		
//	}
	
	
	public void addChatRoom(Message mes){
		JPanel panel=new JPanel();
		panel.setLayout(null);
		String[] str=mes.getContent().split("\\|");
		
		JLabel lname=new JLabel(str[0]);
		lname.setBounds(2, 2, 70, 30);
		panel.add(lname);

		panel.setBounds(3, num_c*(offset+height), width, height);
		num_c++;
		
		chatRoomList.put(str[0], panel);
		chaScrollPane.add(panel);
	}
	
	public void openChatWindow(String name){
		if(chatWindowList.containsKey(name)){
			System.out.println("log :UIFrame :chat window is allready opened! ");
		}
		else{
			JPanel chatWindow=new JPanel();
			chatWindow.setLayout(null);
			
			JTextArea areames=new JTextArea();
			JTextArea areainput=new JTextArea();
			
			JScrollPane scroll1=new JScrollPane(areames);
			JScrollPane scroll2=new JScrollPane(areainput);
			
			scroll1.setBounds(5, 22, 285,290);
			scroll2.setBounds(5, 340,285,75);
			areames.setBackground(Color.gray);
			areainput.setBackground(Color.gray);
			chatWindow.add(scroll1);
			chatWindow.add(scroll2);
			
			JButton b2=new JButton("发送");
			JButton b1=new JButton("关闭");
			b1.setBounds(215, 1, 70, 19);
			b2.setBounds(215, 315, 70, 20);
			chatWindow.add(b1);
			chatWindow.add(b2);
			
			b1.setName(name);
			b2.setName(name);
			
			textAreaList.put(name, areainput);
			mesTextAreaList.put(name, areames);
			
			leftTab.addTab(name,chatWindow);
			
			index.put(name, winNum);
			winNum++;
			
			Message s;
			if(!client.cachedMessage.containsKey(name)){
				//do nothing
			}else{
				System.out.println("log :cashed message size--"+client.cachedMessage.get(name).size());
				while((s=client.cachedMessage.get(name).poll())!=null){
					areames.append("---------"+String.format("%tr",UIFrame.date)+"-------\n"+s.getContent()+"\n");
				}
			}
			
			
			currentWindow=chatWindow;
			
			b1.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					chatWindowList.remove(((JButton)e.getSource()).getName());
					textAreaList.remove(((JButton)e.getSource()).getName());
					mesTextAreaList.remove(((JButton)e.getSource()).getName());
					
					int index=leftTab.getSelectedIndex();
					leftTab.removeTabAt(index);
					
					System.out.println("log :remove chat wondow!!");
				}
			});
			
			
			b2.addActionListener(new ActionListener(){
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
					JTextArea text=textAreaList.get(((JButton)e.getSource()).getName());
					Message mes=new Message(MessageType.CHAT,userName,((JButton)e.getSource()).getName(),text.getText()+"\n");
					Client.messageOutput.add(mes);
					mesTextAreaList.get(((JButton)e.getSource()).getName()).append("---------"+String.format("%tr",UIFrame.date)+"-------\n"+text.getText());
					text.setText("");
					System.out.println("log : send message to queue");
					
				}
				
			});
			
			
			
			chatWindowList.put(name, chatWindow);
			System.out.println("log :put panel to windowlist");

		}
	
	}
	
//	public void closeChatWindow(String name){
//		if(!chatRoomList.containsKey(name)){
//			System.out.println("UIFrame :chat window is allready closed!");
//		}else{
//			leftTab.removeTabAt(index.get(name));
//			index.remove(name);
//			chatWindowList.remove(name);
//		}
//	}
	
	
	public void showMessageNum(String name,int num){
		JPanel panel=null;
		if((panel=friendList.get(name))==null)
		{
			System.out.println("receive unknow target message");
		}else{
			mesNumList.get(name).setText("未读消息 :"+num);
		}
	}
	
	public void removeUser(Message mes){

		
	}
	
	
	public void closeLoginUI(){
		this.login.namef.setText("");
		this.login.pwf.setText("");
		
		this.login.jf.setVisible(false);
		this.mainFrame.setVisible(true);
	}
}


class Login{
	JFrame jf;
	Client client;
	UIFrame frame;
	JButton login;
	JButton signin;
	JTextField namef;
	JTextField pwf;
	public Login(Client c,UIFrame ui){
		client=c;
		frame=ui;
		jf=new JFrame();
		jf.setBounds(300, 400, 400, 300);
		jf.setLayout(null);
		jf.setResizable(false);
		Container con=jf.getContentPane();
		JLabel label=new JLabel("LOG IN");
		label.setFont(new Font("黑体",Font.BOLD,43));
		label.setBounds(120, 40, 150, 30);
		con.add(label);
		
		//ImageIcon turing=new ImageIcon("C:\\Users\\Administrator\\Desktop\\turin.jpg");
		//JLabel bg=new JLabel(turing);
		//bg.setBounds(0,0,400,300);
		
		//con.add(bg);
		JLabel name=new JLabel("name");
		JLabel pw=new JLabel("password");
		name.setBounds(80,140,70,20);
		pw.setBounds(80,180,70,20);
		con.add(name);
		con.add(pw);
		namef=new JTextField();
		pwf=new JTextField();
		namef.setBounds(160, 140, 140, 30);
		pwf.setBounds(160, 180, 140, 30);
		login=new JButton("登陆");
		login.setBounds(100,220, 80, 30);
		signin=new JButton("注册");
		signin.setBounds(200,220, 80, 30);
		con.add(namef);
		con.add(pwf);
		con.add(login);
		con.add(signin);
		
		login.addActionListener(new LoginUIListener(client,frame));
		jf.setVisible(true);
	}
	
	
}



class LoginUIListener implements ActionListener{

	Client client;
	UIFrame uiFrame;
	
	public LoginUIListener(Client c,UIFrame ui){
		client=c;
		uiFrame=ui;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object obj=e.getSource();
		if(obj==uiFrame.login.login){
			Message mes=new Message(MessageType.LOGIN,uiFrame.login.namef.getText(),"server",uiFrame.login.pwf.getText());
			client.messageOutput.add(mes);
			System.out.println("log : add login message to queue! name :"+uiFrame.login.namef.getText()+" password :"+uiFrame.login.pwf.getText());
			//uiFrame.login.jf.setVisible(false);
			System.out.println("login :login............");
			
		}else if(obj==uiFrame.login.signin){
			
		}
	}
	
}


class OpenListener implements MouseListener{
	UIFrame uiframe;
	public OpenListener(UIFrame ui){
		uiframe=ui;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getButton()==MouseEvent.BUTTON1&&e.getClickCount()>=2){
			System.out.println("click !!!");
		String name=((JLabel)((JPanel)e.getSource()).getComponent(0)).getText();
		uiframe.openChatWindow(name);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
