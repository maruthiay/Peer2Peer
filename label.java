import java.awt.*;
import javax.swing.*;
/* 
<applet code="label" width=200 height=100>
</applet>
*/
public class label extends JApplet
{
	public void init()
	{
		Container con=getContentPane();
		ImageIcon im=new ImageIcon("D:/Dhaya/Picture/nature.jpeg");
		JLabel ji=new JLabel("nature",im,JLabel.CENTER);
		con.add(ji);
		}
		}



