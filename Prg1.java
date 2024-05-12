//Draw an applet and update database
import java.sql.*;
import java.awt.*;
import java.applet.Applet;

public class Applt extends Applet
{
  Connection con;
  public void init()
  {
    try
    {
      // get parameter values from the html page
      String server = getParameter("server");
      String port = getParameter("port");
      String url = "jdbc:db2://"+server+":"+port+"/sample";
      String userid = getParameter("userid");
      String password = getParameter("password");
        Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
      con = DriverManager.getConnection(url, userid, password);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
  public void paint(Graphics g)
  {
    try
    {
      g.drawString(
        "First, let's retrieve some data from the database...", 10, 10);
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM employee");
      g.drawString("Received results:", 10, 25);

      // display the result set
      // rs.next() returns false when there are no more rows
      int y = 50;
      int i = 0;
      while (rs.next() && (i < 2))
      {
        i++;
        String a= rs.getString(1);
        String str = rs.getString(2);
        String oneLine = " empno= " + a + " firstname= " + str;
        g.drawString(oneLine, 20, y);
        y = y + 15;
      }
      stmt.close();
      g.drawString("Now, update the database...", 10, 100);
      stmt = con.createStatement();
      int rowsUpdated = stmt.executeUpdate(
        "UPDATE employee SET firstnme = 'SHILI' WHERE empno = '000010'");
      String msg = "Updated " + rowsUpdated;
      if (1 == rowsUpdated)
      {
        msg = msg +" row.";
      }
      else
      {
        msg = msg +" rows.";
      }
      y = y + 40;
      g.drawString(msg, 20, y);
      stmt.close();
      y = y + 40;
      g.drawString("Now, rollback the update...", 10, y);
      con.rollback();
      y = y + 15;
      g.drawString("Rollback done.", 10, y);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
} 
