public class Message {

	public MessageType type;
	private String from;
	private String to;
	private String content;
	
	public Message(MessageType mestype,String f,String t,String con){
		type=mestype;
		from=f;
		to=t;
		content=con;
	}
	public String getFrom(){
		return from;
	}
	public String getTo(){
		return to;
	}
	public String getContent(){
		return content;
	}
}
