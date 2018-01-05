import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

class Superpeer1 {
	public static void main(String sd[]) {
		try {
			Thread t = new Client1();
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class Client1 extends Thread {
        
        
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
	Boolean my=false;
	String mp;
	String getval[]=new String[5];
	String getnam[]=new String[5];
	String prname="";
	String pervalup2;
	
	
		

	public void run() {
		try {
                        String address= InetAddress.getLocalHost().getHostName();
                        JOptionPane.showMessageDialog(null, address);
                    	s1 = new ServerSocket(1201);
			System.out.println("Server..........................");
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			conn = DriverManager.getConnection("jdbc:odbc:client","sa","sa");
			try{			
			String man="localhost";
			String sname="superpeer1";
			String val="1.9899820";
			Statement stmt= conn.createStatement();
			stmt.executeUpdate("insert into serindtab2 values('"+sname+"','"+man+"','"+val+"')");
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
				} else if (foranalysis[0].equalsIgnoreCase("query from superpeer2")) {
					Query1(foranalysis);
				}else if (foranalysis[0].equalsIgnoreCase("download"))
				{					
					System.out.println(foranalysis[1]);
					try {
						String portno=foranalysis[2];
						String remoteadd=foranalysis[1];
						stmt.executeUpdate("INSERT INTO superpeer1 values('" + portno+ "','" + foranalysis[3] + "','" +remoteadd + "')");
						System.out.println("i am listening");
						DataOutputStream dataOutputStream=new DataOutputStream(socket.getOutputStream());
						dataOutputStream.writeUTF("Your file is updated");	
						Statement st=conn.createStatement();
						ResultSet rst=st.executeQuery("select *from serinttab1");
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
			stm.executeUpdate("delete from superpeer1 where Portno='"+na+ "'");
			System.out.println(foranalysis);
			
			Statement stmt=conn.createStatement();
			stmt.executeUpdate("delete from serindtab1 where (Name='"+na+"')");
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
				dataOutputStream.writeUTF("you can join 1201");
				Random rand=new Random();
               			float f=rand.nextFloat()+1;
                		System.out.println(f);
				String value1=String.valueOf(f);
				Statement  stmt=conn.createStatement();
				stmt.executeUpdate("insert into serindtab1 values('"+foranalysis+"','"+remoteport+"','"+value1+"')");
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
			String  portno=foranalysis[1];
			String remote=foranalysis[2];
			System.out.println("Name and Host"+portno+remote);
                         /* stmt = conn.createStatement();
                          String checking="select *from superpeer1 where Remoteserver='"+foranalysis[1]+"' and Portno='"+portno+"'";
                                ResultSet rs=stmt.executeQuery(checking);
                                if(rs.next()){
                                    stmt.execute("delete superpeer1 where Remoteserver='"+foranalysis[1]+"' and Portno='"+portno+"'");
                                }*/
			for (int i = 3; i < foranalysis.length; i++) {
				String string = foranalysis[i];
				stmt = conn.createStatement();
				System.out.println("Upload split");
				stmt.executeUpdate("insert into superpeer1 values('" + portno+ "','" + foranalysis[i] + "','" + remote + "')");
			}
			DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
			dataOutputStream.writeUTF("Your file is uploaded to the Superpeer");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void Query(String[] foranalysis) throws IOException {
			 mp=foranalysis[1];
			String mrem=foranalysis[2];
			String mquer=foranalysis[3];
		try {
			//System.out.println("query  String is "+foranalysis);
			
			System.out.println("Splitted Message"+mp+"\n"+mrem+"\n"+mquer+"\n");

			conn1 = DriverManager.getConnection("jdbc:odbc:client","sa","sa");
			stmt = conn1.createStatement();
             Boolean check_true=true;
			ResultSet resultSet = stmt.executeQuery("select Portno,Remoteserver from superpeer1 where Message='"+ mquer + "'");
			if(resultSet.next()) 
				{
				forresult = resultSet.getString("Portno")+"&"+resultSet.getString("Remoteserver");
				System.out.println("the result from server");
				DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
				dataOutputStream.writeUTF(forresult);
				my=true;
                               // check_true=false;
                           
				}	
			else  {
				System.out.println("File not found on this Server");
				Socket socketfor1 = new Socket("localhost", 1202);
				DataOutputStream dataOutputStream1 = new DataOutputStream(socketfor1.getOutputStream());
				dataOutputStream1.writeUTF("query from superpeer1" + "&"+ mquer);
				System.out.println("Broadcasting\t" + mquer);

				DataInputStream dataInputStream2 = new DataInputStream(socketfor1.getInputStream());
				forresult = dataInputStream2.readUTF();
				System.out.println("File Found \t" + forresult);

				String yescheck[] = forresult.split("&");
				if (yescheck[0].equalsIgnoreCase("yes")) 
					{
					prname=yescheck[1];
					String remotip=yescheck[2];
					System.out.println("name=="+prname+"\t"+"host=="+remotip);
					DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
					dataOutputStream.writeUTF(prname+"&"+remotip);
					//check_true=false;
					}

				else {
					DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
					dataOutputStream.writeUTF("The file is not found");
					//check_true=false;
					 }                                
			       }
                        
			
			if(my)
			{
				System.out.println("from =="+mp);
				String my[]=forresult.split("&");
				System.out.println("\nDestination=="+my[0]);
				String getv[]=new String[3];
				int ii=0;
				try
				{
					
					Statement st =conn.createStatement();
					ResultSet rs=st.executeQuery("select Value,Name from serindtab1 where not Name='"+mp+"'");
				
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
								myst.executeUpdate("update serindtab1 set Value='"+getv[jz]+"' where Name='"+getnam[jz]+"'");
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
								myst.executeUpdate("update serindtab1 set Value='"+getv[jz]+"' where Name='"+getnam[jz]+"'");
							}
						catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					}
				}
				catch (Exception e)
				{e.printStackTrace();
				}

			}

			else
			{
				String pr=prname;
				try
				{
							System.out.println("req=="+pr);
							Statement stt=conn.createStatement();
							ResultSet rs=stt.executeQuery("select Value,Name from serindtab2 where Name='"+pr+"'");
							while(rs.next())
							{
							pervalup2=rs.getString(1);
							System.out.println("value="+pervalup2+"\t"+"Name="+pr);
							}
							float perflt=Float.parseFloat(pervalup2);
							perflt=perflt+0.1156F;
							String upstr=String.valueOf(perflt);
							System.out.println("updatedvalue="+upstr+"\t"+"Name="+pr);
							Statement ust=conn.createStatement();
							ust.executeUpdate("update serindtab1 set Value='"+upstr+"' where Name='"+pr+"'");
				}
				catch (Exception e)
				{e.printStackTrace();
				}
			}
		} 
		catch (Exception e) 
		{
			System.out.println(""+e);								
		}
	}

        public void Query1(String[] foranalysis) throws IOException {
		String qstr=foranalysis[1];
		try {
			System.out.println("query  String from neighbour peer "+qstr);
			conn1 = DriverManager.getConnection("jdbc:odbc:client","sa","sa");
			stmt = conn1.createStatement();
                        
			ResultSet resultSet = stmt.executeQuery("select Portno,Remoteserver from superpeer1 where Message='"+qstr+ "'");
			if(resultSet.next()) {
				forresult = "yes"+"&"+resultSet.getString("Portno")+"&"+resultSet.getString("Remoteserver");
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