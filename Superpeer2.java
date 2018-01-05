import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

class Superpeer2 {
	public static void main(String sd[]) {
		try {
			Thread t = new Client2();
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class Client2 extends Thread {
        
        
	ServerSocket s1;
	Socket socket;
	int count = 0;
	int portno = 0;
	String port = "";
	Statement stmt;
	Connection conn,conn1;
	String forresult = "";
	long longvalue;
	String Superpeer2_Systemname="localhost";
	Boolean check_true=false;
	String mps2;
	boolean my=true;
	String getval[]=new String[3];
	String getnam[]=new String[3];
	String prname;
	String perval;

	
		

	public void run() {
		try {
                        String address= InetAddress.getLocalHost().getHostName();
                        JOptionPane.showMessageDialog(null, address);
                    	s1 = new ServerSocket(1202);
			System.out.println("Server..........................");
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			conn = DriverManager.getConnection("jdbc:odbc:client","sa","sa");
			try{			
			String man="localhost";
			String sname="superpeer2";
			String val="1.9899820";
			Statement stmt= conn.createStatement();
			stmt.executeUpdate("insert into serindtab1 values('"+sname+"','"+man+"','"+val+"')");
			}
			catch(Exception e)
			{
			e.printStackTrace();
			}
			DataInputStream dataInputStream;
			while (true) {
				System.out.println("while is running");
				socket = s1.accept();
				System.out.println("socket is accepted");
				dataInputStream = new DataInputStream(socket.getInputStream());
				System.out.println("datainput stream is got");
				//System.out.println("responseee===========> before");
				String resposnse = dataInputStream.readUTF();
				System.out.println("Request ===========>" + resposnse);
				System.out.println("readed");
				System.out.println("this is for server....");
				String[] foranalysis = resposnse.split("&");
				

				if (foranalysis[0].equalsIgnoreCase("Bye")) {
					Bye(foranalysis[1],foranalysis[2]);
					System.out.println(""+foranalysis[1]+foranalysis[2]);
				} else if (foranalysis[0].equalsIgnoreCase("Request")) {
					Login(foranalysis[1],foranalysis[2]);
				} else if (foranalysis[0].equalsIgnoreCase("Upload")) {
					Upload(foranalysis);
				} else if (foranalysis[0].equalsIgnoreCase("Query")) {
					Query(foranalysis);
				} else if (foranalysis[0].equalsIgnoreCase("query from superpeer1")) {
					Query1(foranalysis);
				}else if (foranalysis[0].equalsIgnoreCase("download"))
				{					
					System.out.println(foranalysis[1]);
					try {
						String portno=foranalysis[2];
						String remoteadd=foranalysis[1];
						stmt.executeUpdate("INSERT INTO superpeer2 values('" + portno+ "','" + foranalysis[3] + "','" +remoteadd + "')");
						System.out.println("i am listening");
						DataOutputStream dataOutputStream=new DataOutputStream(socket.getOutputStream());
						dataOutputStream.writeUTF("Your file is updated");	
						Statement st=conn.createStatement();
						ResultSet rst=st.executeQuery("select *from serinttab2");
						while(rst.next())
						{
						
						}
					} catch (Exception e) {
						
						e.printStackTrace();
					}
				}
				

				else {
					DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
					dataOutputStream.writeUTF("choose better from last else");
				}
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	
	
	public void Bye(String foranalysis,String localhost) {
		try {
			String na=foranalysis;
			
			Statement stm = conn.createStatement();
			stm.executeUpdate("delete from superpeer2 where Portno='"+na+ "'");
			System.out.println(foranalysis);

			Statement stmt=conn.createStatement();
			stmt.executeUpdate("delete from serindtab2 where Name='"+na+"'");
			count--;
			DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
			dataOutputStream.writeUTF("Successfully you are released from connection");			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	public void Login(String foranalysis,String remoteport) {
		try {
			if (count < 3) {
				port = foranalysis;
				count++;
				System.out.println("Request split");
				DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
				dataOutputStream.writeUTF("you can join 1202");
				Random rand=new Random();
               			float f=rand.nextFloat()+1;
                		System.out.println(f);
				String value1=String.valueOf(f);
				Statement  stmt=conn.createStatement();
				stmt.executeUpdate("insert into serindtab2 values('"+foranalysis+"','"+remoteport+"','"+value1+"')");
				} 
				else {
				DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
				dataOutputStream.writeUTF("you can leave");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void Upload(String[] foranalysis) {
		try {
			System.out.println("upload check");
			String portno=foranalysis[1];
			String remote=foranalysis[2];
			System.out.println("Name and Host"+portno+remote);
                          /*stmt = conn.createStatement();
                          String checking="select *from superpeer2 where Remoteserver='"+foranalysis[1]+"' and Portno='"+portno+"'";
                                ResultSet rs=stmt.executeQuery(checking);
                                if(rs.next()){
                                    stmt.execute("delete superpeer2 where Remoteserver='"+foranalysis[1]+"' and Portno='"+portno+"'");
                                }*/
			for (int i = 3; i < foranalysis.length; i++) {
				String string = foranalysis[i];
				stmt = conn.createStatement();
				System.out.println("Upload split");
				stmt.executeUpdate("insert into superpeer2 values('" + portno+ "','" + foranalysis[i] + "','" + remote + "')");
			}
			DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
			dataOutputStream.writeUTF("Your file is uploaded to the Superpeer");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	

	public void Query(String[] foranalysis) throws IOException 
		{
		
			mps2=foranalysis[1];
			String mrem=foranalysis[2];
			String mquer=foranalysis[3];			
		    try 
		     {
			System.out.println("query  split"+mquer);
			conn1 = DriverManager.getConnection("jdbc:odbc:client","sa","sa");
			stmt = conn1.createStatement();
                       
			ResultSet resultSet = stmt.executeQuery("select Portno,Remoteserver from superpeer2 where Message='"+mquer+"'");
			if(resultSet.next()) 
				{
				forresult = resultSet.getString("Portno")+"&"+resultSet.getString("Remoteserver");
				System.out.println("the result from server");
				DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
				dataOutputStream.writeUTF(forresult);
                                check_true=false;
				my=true;
                                
				}	
			
			else  {
				System.out.println("File not found on this server");
				Socket socketfor1 = new Socket("localhost", 1201);
				DataOutputStream dataOutputStream1 = new DataOutputStream(socketfor1.getOutputStream());
				System.out.println("servie name==\t" + foranalysis);
				dataOutputStream1.writeUTF("query from superpeer2" + "&"+mquer);
				
				DataInputStream dataInputStream2 = new DataInputStream(socketfor1.getInputStream());
				forresult = dataInputStream2.readUTF();
				System.out.println("File Found ==\t" + forresult);

				String yescheck[] = forresult.split("&");
				if (yescheck[0].equalsIgnoreCase("yes")) 
					{
					String prname=yescheck[1];
					String remotip=yescheck[2];
					DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
					dataOutputStream.writeUTF(prname+"&"+remotip);
					//JOptionPane.showMessageDialog(null, forresult);
                                        check_true=false;
					}

				   else {
					DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
					dataOutputStream.writeUTF(forresult);
					//JOptionPane.showMessageDialog(null, forresult);
                                        check_true=false;
					}                                
			       }
                        				if (check_true)
							{
                               				 DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                                			 dataOutputStream.writeUTF("The file is not found");
							}

			if(my)
			{
				System.out.println("from =="+mps2);
				String my[]=forresult.split("&");
				System.out.println("\nDestination=="+my[0]);
				String getv[]=new String[3];
				int ii=0;
				try
				{
					
					Statement st =conn.createStatement();
					ResultSet rs=st.executeQuery("select Value,Name from serindtab2 where not Name='"+mps2+"'");
				
					while(rs.next())
					{
					getval[ii]=rs.getString(1);
					getnam[ii]=rs.getString(2);
					ii=ii+1;
					}
					for(int jz=0;jz<ii;jz++)
					{
					System.out.print("\n Value nn="+getval[jz]);
					System.out.println("Name =="+getnam[jz]);
						if(my[0].equalsIgnoreCase(getnam[jz]))
						{
							float fget=Float.parseFloat(getval[jz]);
							fget=fget+0.1156F;
							getv[jz]=String.valueOf(fget);
							System.out.println("UPDATED Value=="+getv[jz]);
							try
							{
								Statement myst=conn.createStatement();
								myst.executeUpdate("update serindtab2 set Value='"+getv[jz]+"' where Name='"+getnam[jz]+"'");
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}

						}
						else
						{
							float fget=Float.parseFloat(getval[jz]);
							fget=fget-0.0111F;
							getv[jz]=String.valueOf(fget);
							System.out.println("UPDATED Value=="+getv[jz]);
						try
							{
								Statement myst=conn.createStatement();
								myst.executeUpdate("update serindtab2 set Value='"+getv[jz]+"' where Name='"+getnam[jz]+"'");
							}
						catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					   }
				
				
				}

					catch (Exception e) 
						{
						e.printStackTrace();
						}
			}
		else
					{
						String pr=prname;
						try
						{
							Statement stt=conn.createStatement();
							ResultSet rs=stt.executeQuery("select Value,Name from serindtab1 where Name='"+pr+"'");
							while(rs.next())
							{
							perval=rs.getString(1);
							System.out.println("value="+perval+"\t"+"Name="+pr);
							}
							float perflt=Float.parseFloat(perval);
							perflt=perflt+0.1156F;
							String upstr=String.valueOf(perflt);
							System.out.println("updatedvalue="+upstr+"\t"+"Name="+pr);
							Statement ust=conn.createStatement();
							ust.executeUpdate("update serindtab1 set Value='"+upstr+"' where Name='"+pr+"'");

						}
						catch (Exception e)
						{ e.printStackTrace();
						}
				
			}
		} 
		catch (Exception e) 
		{
			System.out.println(""+e);								
		}
	}
       
		public void Query1(String[] foranalysis) throws IOException {
		try {
			System.out.println("query  split"+foranalysis[1]);
			conn1 = DriverManager.getConnection("jdbc:odbc:client","sa","sa");
			stmt = conn1.createStatement();
                        
			ResultSet resultSet = stmt.executeQuery("select Portno,Remoteserver from superpeer2 where Message='"+ foranalysis[1] + "'");
			if(resultSet.next()) {
				forresult = "yes"+"&"+resultSet.getString("Portno")+"&"+resultSet.getString("Remoteserver");
				System.out.println("the result from server");
				DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
				dataOutputStream.writeUTF(forresult);				
			}	
			else  {
                            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
				dataOutputStream.writeUTF("The File is Not found in the Network");				
			}
		} catch (Exception e) {
			System.out.println("I transfered to neighbour"+e);								
		}
        }
   }