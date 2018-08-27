
public class User {
	
	private String name;
	private String ID;
	private String pw;
	private boolean isOnline;
	private String info=null;
	
	public User(String name,String ID,String pw,boolean st,String info){
		this.name=name;
		this.ID=ID;
		this.info=info;
		this.isOnline=st;
		this.pw=pw;
	}
	
	public User(String name,String ID,String pw ,boolean st){
		this.name=name;
		this.ID=ID;
		this.isOnline=st;
		this.pw=pw;
	}
	
	public boolean getState(){
		return isOnline;
	}
	
	public void setState(boolean st){
		this.isOnline=st;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getID(){
		return this.ID;
	}
	
	public String getPw(){
		return this.pw;
	}
	
	public String getInfo(){
		return this.info;
	}
}
