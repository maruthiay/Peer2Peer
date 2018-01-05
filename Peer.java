import javax.swing.*;
import java.net.*;
import java.io.*;
import java.sql.*;
import java.awt.event.*;
import java.util.Date;
public class Peer implements ActionListener,Runnable
{

JLabel jtitle,jname,jpath,jquery,jpeer,state1,state2;
JTextField jtprt,jtxtfile,jtxtquery,jtxtpeer;
JButton jcon,jdis,jbro,jup,jclear,Jdown,Jsearch,jprcon;
JTextArea jta; 
JScrollPane jsp;
JComboBox jbc;

Socket s;
Socket sother;
ServerSocket s1;
String localhost="";
String req="";
String item="";
String ip="";
String remortportno="";
Connection con;
Statement stmt;
ResultSet rst;
String status="";
Date d;
int needport;
int i=0;
File f;

public Peer()
{
JFrame jf=new JFrame("peer");
JTabbedPane jtp=new JTabbedPane();

JPanel jp1=new JPanel();
jp1.setLayout(null);
jtitle=new JLabel("Dynamic ");
jname =new JLabel("name");
jtprt =new JTextField();
jbc=new JComboBox();
jbc.addItem("Select");
jbc.addItem("Super Peer1");
jbc.addItem("Super Peer2");
jcon =new JButton("Connect");
jcon.addActionListener(this);
jdis =new JButton("Disconnect");
jdis.addActionListener(this);
jtitle.setBounds(10,10,375,50);
jname.setBounds(10,75,75,50);
jtprt.setBounds(100,75,75,50);
jbc.setBounds(200,75,75,50);
jcon.setBounds(50,150,100,50);
jdis.setBounds(250,150,100,50);
jp1.add(jtitle);
jp1.add(jname);
jp1.add(jtprt);
jp1.add(jbc);
jp1.add(jcon);
jp1.add(jdis);
jtp.add("Main",jp1);


JPanel jp2=new JPanel();
jp2.setLayout(null);
jpath=new JLabel("File Path");
jtxtfile=new JTextField("");
jbro=new JButton("Browse");
jbro.addActionListener(this);
jup=new JButton("Upload");
jup.addActionListener(this);
jclear=new JButton("Clear");
jclear.addActionListener(this);
jta=new JTextArea();
jsp=new JScrollPane(jta);
jsp.createVerticalScrollBar();
jpath.setBounds(10,5,75,50);
jtxtfile.setBounds(100,10,150,30);
jbro.setBounds(10,50,100,50);
jup.setBounds(140,50,100,50);
jclear.setBounds(275,50,100,50);
jsp.setBounds(50,150,300,150);
jp2.add(jpath);
jp2.add(jtxtfile);
jp2.add(jbro);
jp2.add(jup);
jp2.add(jclear);
jp2.add(jsp);
jtp.add("Upload",jp2);

JPanel jp3=new JPanel();
jp3.setLayout(null);
jquery=new JLabel("Query String");
state1=new JLabel("");
state2=new JLabel("");
jtxtquery=new JTextField();
jpeer=new JLabel("peer name");
jtxtpeer=new JTextField();
Jsearch=new JButton("Search");
Jsearch.addActionListener(this);
Jdown=new JButton("Download");
Jdown.addActionListener(this);
jprcon=new JButton("Peer connect");
jprcon.addActionListener(this);
jquery.setBounds(10,30,100,50);
jtxtquery.setBounds(150,40,120,30);
jpeer.setBounds(10,90,100,50);
jtxtpeer.setBounds(150,100,120,30);
Jsearch.setBounds(50,190,100,50);
jprcon.setBounds(190,190,130,50);
Jdown.setBounds(90,250,200,30);
state1.setBounds(20,290,75,50);
state2.setBounds(150,290,75,50);
jp3.add(jquery);
jp3.add(jtxtquery);
jp3.add(jpeer);
jp3.add(jtxtpeer);
jp3.add(Jsearch);
jp3.add(Jdown);
jp3.add(state1);
jp3.add(state2);
jp3.add(jprcon);
jtp.add("search",jp3);

jf.add(jtp);
jf.setSize(400,400);
jf.setVisible(true);
jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		String portserver="";
                Boolean port_check=true;
                  while (port_check) {  
                	try {                    
                        	portserver = JOptionPane.showInputDialog("Enter Your Portno");  
                        if (portserver.equals(null)) 
			{
                        System.exit(0);
            		}
                        int d=Integer.parseInt(portserver);  
                        port_check=false;     
            		    } 	
			catch (Exception e) 
			{
                		JOptionPane.showMessageDialog(null, "Enter the port number only");
                		port_check=true;
            		}
          			     }
		
			try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			con = DriverManager.getConnection("jdbc:odbc:client","sa","sa");
			localhost = InetAddress.getLocalHost().getHostName();
			JOptionPane.showMessageDialog(null, localhost);

				} catch (Exception e) {
					e.printStackTrace();
				}

jtprt.setText(portserver);
d=new Date();
}

public void actionPerformed(ActionEvent at) {

		if ((at.getSource()) == jcon) {
		peerConnect();
						}
		if((at.getSource()) == jdis)	
		{
			peerDisconnect();
		}
		if((at.getSource()) == jup)	
		{
			peerLoad();
		}
		if((at.getSource()) == jbro)	
		{
			peerSelect();
		}
		
		if((at.getSource()) == jclear)	
		{
			jta.setText("");
			jtxtfile.setText("");
			jtxtquery.setText("");
			jtxtpeer.setText("");

		}
		if((at.getSource()) == Jsearch)	
		{
			peerSearch();
		}
		if((at.getSource()) ==jprcon)
		{
			analysis();
		}
		if((at.getSource()) == Jdown)	
		{
			downLoad();
		}
		
		
}
public void peerConnect()
{
	status="Connected";
		item=(String)jbc.getSelectedItem();
			if (jtprt.getText().equalsIgnoreCase("")) {
			JOptionPane.showMessageDialog(null, "Enter Port.NO");
		} else if (item.equalsIgnoreCase("Select")) {
			JOptionPane.showMessageDialog(null, "Select Peer");
		} else if (item.equalsIgnoreCase("Super Peer1")) {
					req = "Request"+"&"+jtprt.getText()+"&"+localhost;
					JOptionPane.showMessageDialog(null,"you requested the connection with superpeer1");
			try{
				s = new Socket("localhost", 1201);
				int hh=d.getHours();
				int mm=d.getMinutes();
				int sec=d.getSeconds();
				String intime=hh+":"+mm+":"+sec;
				stmt = con.createStatement();
				stmt.executeUpdate("insert into peerin values('"+jtprt.getText()+"','"+localhost+"','"+status+"')");
				DataOutputStream dout = new DataOutputStream(s.getOutputStream());
				dout.writeUTF(req);
				System.out.print("my port No===>"+jtprt.getText()+"Run on"+localhost);
				jcon.setEnabled(false);
				DataInputStream din = new DataInputStream(s.getInputStream());
				String res = din.readUTF();
				if (res.equalsIgnoreCase("you can join 1201")) {
					JOptionPane.showMessageDialog(null,"you are accepted ");
					System.out.println("my ping request accepted");
					jcon.setEnabled(false);					                                   
					jtprt.setEditable(false);
                   			 jbc.setEnabled(false);	
										}
				 else{
                                        JOptionPane.showMessageDialog(null,"Your Request is not processed By Superpeer1");
										System.out.println("Pong response received not available for communication");
                                        jcon.setEnabled(true);
					}
			} catch (Exception e) {
			e.printStackTrace();
						}	

		
		}
		else if(item.equalsIgnoreCase("Super Peer2")) {
					req = "Request"+"&"+jtprt.getText()+"&"+localhost;
					JOptionPane.showMessageDialog(null,"you request the connection with superpeer2");
			try{
				s = new Socket("localhost", 1202);
				int hh=d.getHours();
				int	mm=d.getMinutes();
				int sec=d.getSeconds();
				String intime=hh+":"+mm+":"+sec;
				stmt = con.createStatement();
				stmt.executeUpdate("insert into peerin values('"+jtprt.getText()+"','"+localhost+"','"+status+"')");
				DataOutputStream dout = new DataOutputStream(s.getOutputStream());
				dout.writeUTF(req);

				jcon.setEnabled(false);
				DataInputStream din = new DataInputStream(s.getInputStream());
				String res = din.readUTF();
				if (res.equalsIgnoreCase("you can join 1202")) {
					JOptionPane.showMessageDialog(null,"you are accepted");
					System.out.println("my ping request accepted");
					jcon.setEnabled(false);					                                   
					jtprt.setEditable(false);
                   			 jbc.setEnabled(false);	
										}
				 else{
                                        JOptionPane.showMessageDialog(null,"Your Request is not processed By Superpeer2");
										System.out.println("Pong response received not available for communication");
                                       jcon.setEnabled(true);
					}
			} catch (Exception e) {
			e.printStackTrace();
						}	

		
		}
		
else{
System.out.println("no super peer node");
}

}

public void peerDisconnect()
{
status="Disconnected";
item=(String)jbc.getSelectedItem();
if (item.equalsIgnoreCase("Super Peer1")) {
				try {
				s = new Socket("localhost", 1201);
				String bye ="Bye"+"&"+jtprt.getText()+"&"+localhost;
				DataOutputStream dout = new DataOutputStream(s.getOutputStream());
				dout.writeUTF(bye);
				JOptionPane.showMessageDialog(null,"Are you want to leave");
				System.out.println(" I want to disconnect");
				DataInputStream din = new DataInputStream(s.getInputStream());
				System.out.println(din.readUTF());
				JOptionPane.showMessageDialog(null,"Server accepted your request. You released");
				System.out.println("I am not available  in this network");
				/* int hh=d.getHours();
				int mm=d.getMinutes();
				int sec=d.getSeconds();
				String intime=hh+":"+mm+":"+sec;
				stmt = con.createStatement();
				stmt.executeUpdate("insert into peerin values('"+jtprt.getText()+"','"+localhost+"','"+status+"','"+intime+"')");*/
			     System.exit(0);
					} 
					catch (Exception e) {
				JOptionPane.showMessageDialog(null,"You dont have any connection first make a connection");
				}	
	}

if (item.equalsIgnoreCase("Super Peer2")) {
				try {
				s = new Socket("localhost", 1202);
				String bye ="Bye"+"&"+jtprt.getText()+"&"+localhost;
				DataOutputStream dout = new DataOutputStream(s.getOutputStream());
				dout.writeUTF(bye);
				JOptionPane.showMessageDialog(null,"Are you want to leave");
								System.out.println(" I want to disconnect");
				DataInputStream din = new DataInputStream(s.getInputStream());
				System.out.println(din.readUTF());
				JOptionPane.showMessageDialog(null,"Server accepted your request. You released");
								System.out.println("I am not available  in this network");
				/*int hh=d.getHours();
				int mm=d.getMinutes();
				int sec=d.getSeconds();
				String intime=hh+":"+mm+":"+sec;
				stmt = con.createStatement();
				stmt.executeUpdate("insert into peerin values('"+jtprt.getText()+"','"+localhost+"','"+status+"','"+intime+"')");*/
				
                                System.exit(0);
					} catch (Exception e) {
				JOptionPane.showMessageDialog(null,"You dont have any connection first make a connection");
				}	
		}




}

public void peerLoad()
{
	String index=(String)jbc.getSelectedItem();
String upload = "Upload" + "&" +jtprt.getText()+"&"+localhost;
if (jtxtfile.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Selected the file");
            }
if(index.equalsIgnoreCase("Super Peer1")&&!jtxtfile.getText().equals("")) 
	{

try
{
s=new Socket("localhost",1201);
stmt=con.createStatement();
ResultSet rt=stmt.executeQuery("select Filename from peerdata where Portno='"+jtprt.getText()+"'");
while(rt.next())
{
upload+="&"+rt.getString("Filename");
}
System.out.println("To be sent\t" + upload);
DataOutputStream dataOutputStream = new DataOutputStream(s.getOutputStream());
dataOutputStream.writeUTF(upload);
DataInputStream dataInputStream = new DataInputStream(s.getInputStream());
System.out.println(dataInputStream.readUTF());
JOptionPane.showMessageDialog(null,"Your file is uploaded to server");
}
catch (Exception e) 
{
e.printStackTrace();
}
}

if(index.equalsIgnoreCase("Super Peer2")&&!jtxtfile.getText().equals("")) {

try
{
s=new Socket("localhost",1202);
stmt=con.createStatement();
ResultSet rt=stmt.executeQuery("select Filename from peerdata where Portno='"+jtprt.getText()+"'");
while(rt.next())
{
upload+="&"+rt.getString("Filename");
}
System.out.println("To be sent\t" +upload);
DataOutputStream dataOutputStream = new DataOutputStream(s.getOutputStream());
dataOutputStream.writeUTF(upload);
DataInputStream dataInputStream = new DataInputStream(s.getInputStream());
System.out.println(dataInputStream.readUTF());
JOptionPane.showMessageDialog(null,"Your file is uploaded to server");
}
catch (Exception e) 
{
e.printStackTrace();
}
}

}

public void peerSelect()
{ 

JFileChooser jfc=new JFileChooser();
try
{
jfc.showOpenDialog(null);
f=jfc.getSelectedFile();
String spath=f.getAbsolutePath();
jtxtfile.setText(spath);
int flen=(int)f.length();
String fname=f.getName();
System.out.println(spath);
i++;
stmt = con.createStatement();		
jta.append("" +i+"."+fname+"\t"+flen+"Bytes\n");
jta.setEditable(false);
stmt.executeUpdate("INSERT INTO peerdata values('"+fname+"','"+spath+"',"+flen+",'"+jtprt.getText()+"','"+localhost+"')");
}

catch (Exception e) 
{
e.printStackTrace();
}

}


public void peerSearch()
{
String Query = "Query";
item=(String)jbc.getSelectedItem();
                if (jtxtquery.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Just Validate");                
            } 
		if (item.equals("Super Peer1") && (!jtxtquery.getText().equals("")) ) {
			try {
				s = new Socket("localhost", 1201);
				DataOutputStream dout = new DataOutputStream(s.getOutputStream());
				Query += "&" +jtprt.getText()+ "&" + localhost+"&"+jtxtquery.getText();
				System.out.println(Query);
				dout.writeUTF(Query);
				
				DataInputStream din = new DataInputStream(s.getInputStream());
				String neighbour = din.readUTF();
                                if(neighbour.equalsIgnoreCase("The File is Not found in the Network"))
				{
                                    JOptionPane.showMessageDialog(null, neighbour);
                                }
                                else
				{
                                String[] remote = neighbour.split("&");
				remortportno=remote[0];
				ip=remote[1];
				
				System.out.println("read the file from the server\t");
				jtxtpeer.setText(remote[0]+remote[1] );
                                }
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		if (item.equals("Super Peer2") && (!jtxtquery.getText().equals(""))) {
			try {
				s = new Socket("localhost", 1202);
				DataOutputStream dataOutputStream = new DataOutputStream(s.getOutputStream());
				Query += "&" +jtprt.getText()+ "&" + localhost+"&"+jtxtquery.getText();
				System.out.println(Query);
				dataOutputStream.writeUTF(Query);

				
				DataInputStream dataInputStream = new DataInputStream(s.getInputStream());
				String neighbour = dataInputStream.readUTF();
				if(neighbour.equalsIgnoreCase("The File is Not found in the Network"))
				{
                                    JOptionPane.showMessageDialog(null, neighbour);
                                }
                                else
				{
                                String[] remote = neighbour.split("&");
				remortportno=remote[0];
				ip=remote[1];

				System.out.println("read the file from the server\t");
				JOptionPane.showMessageDialog(null, remote);
				jtxtpeer.setText(remote[0]+remote[1] );
                                }
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}

}

public void analysis()
{
item=(String)jbc.getSelectedItem();
try
{
			if (item.equals("Super Peer1")) {
				String need = jtxtquery.getText();
				needport = Integer.parseInt(remortportno);
				sother = new Socket(ip, needport);
				DataOutputStream dataOutputStream = new DataOutputStream(sother.getOutputStream());
				dataOutputStream.writeUTF("toget" + "&" + need);
				//JOptionPane.showMessageDialog(null,need);

				DataInputStream dataInputStream = new DataInputStream(sother.getInputStream());
				String Reply = dataInputStream.readUTF();
				//state2.setText(Reply);
				//JOptionPane.showMessageDialog(null, "Message got");
			}
			if (item.equals("Super Peer2")) {
				String need = jtxtquery.getText();
				needport = Integer.parseInt(remortportno);
				sother = new Socket(ip, needport);
				DataOutputStream dataOutputStream = new DataOutputStream(sother.getOutputStream());
				dataOutputStream.writeUTF("toget" + "&" + need);
				JOptionPane.showMessageDialog(null, need);

				DataInputStream dataInputStream = new DataInputStream(sother.getInputStream());
				String Reply = dataInputStream.readUTF();
				//state2.setText(Reply);
				JOptionPane.showMessageDialog(null, "Message got");
			}



} 
catch (Exception e) 
{
e.printStackTrace();
}
}
public void downLoad()
	{
	try
	{
		
	if(jtxtquery.getText().equals("")||jtxtpeer.getText().equals(""))
		{
		JOptionPane.showMessageDialog(null, "No File and Peer "); 
		}
		else
		{
			int filesize = 6022386;
			int bytesRead = 0;
			int current = 0;
				System.out.println("Connecting...");
				byte[] mybytearray = new byte[filesize];
			//System.out.println("byte is length\t" + mybytearray.length);
			InputStream is = sother.getInputStream();
			System.out.println("before fos" + jtxtquery.getText());
			FileOutputStream fos = new FileOutputStream(jtxtquery.getText());
			//System.out.println("fos is created");
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bytesRead = is.read(mybytearray, 0, mybytearray.length);
			current = bytesRead;
			System.out.println("byte is read\t" + bytesRead);
			bos.write(mybytearray, 0, current);
			System.out.println("finished");
			JOptionPane.showMessageDialog(null, "You downloaded the file");
			bos.close();
                        }
		} 
		catch (Exception e) 
			{
			e.printStackTrace();
		}
					
	}

public void run() {
		try {
			s1 = new ServerSocket(Integer.parseInt(jtprt.getText()));
			int count = 1;
			while (true) {
				Socket ss = s1.accept();
				DataInputStream d2 = new DataInputStream(ss.getInputStream());
				String str2 = d2.readUTF();
				String string[] = str2.split("&");
				if (string[0].equalsIgnoreCase("toget")) 
				{
					DataOutputStream dataOutputStream = new DataOutputStream(ss.getOutputStream());
					dataOutputStream.writeUTF("you got");
					JOptionPane.showMessageDialog(null, string[1]);
					//state1.setText(string[1]);

				} else 
				{
					DataOutputStream dataOutputStream = new DataOutputStream(ss.getOutputStream());
					dataOutputStream.writeUTF("you didnt got");
					JOptionPane.showMessageDialog(null, "You dont have");
				}
				//System.out.println("Client 2:" + "hai i got the message");
				String filepathfromdb = string[1];
				String resultfromdb ="" ; 
				System.out.println(filepathfromdb);
				ResultSet rt = stmt.executeQuery("select Location from peerdata where Filename='"+filepathfromdb+"'");
				if (rt.next()) {
					resultfromdb = rt.getString("Location");
				}
				System.out.println("file from db==\t" + resultfromdb);
				File myFile = new File(resultfromdb);
				//JOptionPane.showMessageDialog(null, resultfromdb);
				byte[] mybytearray = new byte[(int) myFile.length()];
				FileInputStream fis = new FileInputStream(myFile);
				BufferedInputStream bis = new BufferedInputStream(fis);
				bis.read(mybytearray, 0, mybytearray.length);
				OutputStream os = ss.getOutputStream();
				System.out.println("Sending...");
				os.write(mybytearray, 0, mybytearray.length);
				System.out.println("Sended...........");
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
public static void main(String[] args) 
	{

Thread thread = new Thread(new Peer());
		thread.start();

}
}