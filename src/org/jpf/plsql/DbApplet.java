/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2011-4-26 下午04:56:26 
* 类说明 
*/ 

package org.jpf.plsql;
/**
 * Copyright (c) 1996-2004 Borland Software Corporation. All Rights Reserved.
 * 
 * This SOURCE CODE FILE, which has been provided by Borland Software as part
 * of an Borland Software product for use ONLY by licensed users of the product,
 * includes CONFIDENTIAL and PROPRIETARY information of Borland Software.  
 *
 * USE OF THIS SOFTWARE IS GOVERNED BY THE TERMS AND CONDITIONS 
 * OF THE LICENSE STATEMENT AND LIMITED WARRANTY FURNISHED WITH
 * THE PRODUCT.
 *
 * IN PARTICULAR, YOU WILL INDEMNIFY AND HOLD BORLAND SOFTWARE, ITS
 * RELATED COMPANIES AND ITS SUPPLIERS, HARMLESS FROM AND AGAINST ANY
 * CLAIMS OR LIABILITIES ARISING OUT OF THE USE, REPRODUCTION, OR
 * DISTRIBUTION OF YOUR PROGRAMS, INCLUDING ANY CLAIMS OR LIABILITIES
 * ARISING OUT OF OR RESULTING FROM THE USE, MODIFICATION, OR
 * DISTRIBUTION OF PROGRAMS OR FILES CREATED FROM, BASED ON, AND/OR
 * DERIVED FROM THIS SOURCE CODE FILE.
 */
//------------------------------------------------------------------------------
// Copyright (c) 1996-2004 Borland Software Corporation.  All Rights Reserved.
//------------------------------------------------------------------------------

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.io.IOException;
import java.io.ObjectInputStream;
import com.borland.dx.sql.dataset.Database;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.dataset.DataSetData;
import com.borland.dx.dataset.DataSetException;
import com.borland.dbswing.TableScrollPane;
import com.borland.dbswing.JdbTable;
import com.borland.dbswing.JdbStatusLabel;
import com.borland.dbswing.JdbNavToolBar;


/**
 * ==============================================================================
 * Class: dbApplet.DbApplet
 * Case:  442770
 *
 * Description:
 * ===========
 * Database applet that uses a servlet to connect to a database and
 * stream the data back to the applet.
 * ==============================================================================
 */

public class DbApplet extends JApplet
{

   void jButton1_actionPerformed(ActionEvent e)
   //===========================================================================
   //  This button event handler attempts to connect to our servlet and get the
   //  data.
   //===========================================================================
   {
      DataSetData data = null;                 // Temporary storage for the data.
      URL servletURL = null;                   // The URL to the servlet.
      URLConnection servletConnection = null;  // The connection to the servlet.
      ObjectInputStream dbStream = null;       // The stream from the servlet.

      try
      {
      // The first URL object (commented) can be used to connect to the servlet
      // from a web server.  The second is for use by the IDE.  The port number
      // may need to be modified in order to successfully connect to the servlet
      // in the IDE.  The default port in the IDE is 8080 but may change if that
      // port is occupied.  When you run the servlet, observe the port number
      // used to launch it in the Location bar above the page.

//         servletURL = new URL( "http://server/servletPathOrAlias" );
         servletURL = new URL( "http://aiqcs.asiainfo-linkage.com/DbServlet" );

         servletConnection = servletURL.openConnection();

         dbStream = new ObjectInputStream( servletURL.openStream() );

         // Read an object from the servlet stream and cast it to a DataSetData
         // object.
         data = (DataSetData) dbStream.readObject();
      }
      catch( MalformedURLException ee )
      {
         System.err.println( "MalformedURLException caught!" );
         System.err.println( ee.getMessage() );
         ee.printStackTrace( System.err );
      }
      catch( IOException eee )
      {
         System.err.println( "IOException caught!" );
         System.err.println( eee.getMessage() );
         eee.printStackTrace( System.err );
      }
      catch( ClassNotFoundException eeee )
      {
         System.err.println( "ClassNotFoundException caught!" );
         System.err.println( eeee.getMessage() );
         eeee.printStackTrace( System.err );
      }
      finally
      {
         try
         {
            dbStream.close();
         }
         catch( IOException eeeee )
         {
            System.err.println( "IOException caught!" );
            System.err.println( eeeee.getMessage() );
            eeeee.printStackTrace( System.err );
         }
      }

      if( data != null )
         this.hookupData(data);
      else
         jTextField1.setText( "DataSetData is null, no object read." );
   }

   void hookupData(DataSetData data)
   //===========================================================================
   //  This method takes the data returned from the servlet and puts it into a
   //  Data Express component that can be wired to the UI.  A QueryDataSet is
   //  used, but any DataSet decendant should work.
   //===========================================================================
   {
      try
      {
         // Be sure to empty the old data...
         queryDataSet1.empty();

         // Load the new data into the QueryDataSet...
         data.loadDataSet(queryDataSet1);
      }
      catch( DataSetException e )
      {
         System.err.println( "DataSetException caught!" );
         System.err.println( e.getMessage() );
         e.printStackTrace( System.err );
      }

      // Connect the data to the UI...
      jdbNavToolBar1.setDataSet( queryDataSet1 );
      jdbTable1.setDataSet( queryDataSet1 );
      jdbStatusLabel1.setDataSet( queryDataSet1 );
   }

   /**
    * ==============================================================================
    * JBuilder generated code follows, not modified.
    * ==============================================================================
    */


   boolean isStandalone = false;
   Database database1 = new Database();
   QueryDataSet queryDataSet1 = new QueryDataSet();
   JPanel jPanel1 = new JPanel();
   GridBagLayout gridBagLayout1 = new GridBagLayout();
   TableScrollPane tableScrollPane1 = new TableScrollPane();
   JdbTable jdbTable1 = new JdbTable();
   JdbStatusLabel jdbStatusLabel1 = new JdbStatusLabel();
   JdbNavToolBar jdbNavToolBar1 = new JdbNavToolBar();
   JTextField jTextField1 = new JTextField();
   JButton jButton1 = new JButton();

  /**
   * Get a parameter value
   *
   * @param key String
   * @param def String
   * @return String
   */
  public String getParameter(String key, String def)
   {
      return isStandalone ? System.getProperty(key, def) :
         (getParameter(key) != null ? getParameter(key) : def);
   }

   /**
    * Construct the applet
    */
   public DbApplet()
   {
   }

   /**
    * Initialize the applet
    */

   public void init()
   {
      try
      {
         jbInit();
      }
      catch(Exception e)
      {
         e.printStackTrace();
      }
   }

  /**
   * Component initialization
   *
   * @throws Exception exception
   */
  private void jbInit() throws Exception
   {
      this.setSize(new Dimension(500, 450));

      jButton1.setText("Invoke Servlet");
      jButton1.addActionListener(new java.awt.event.ActionListener()
      {

         public void actionPerformed(ActionEvent e)
         {
            jButton1_actionPerformed(e);
         }
      });

      jPanel1.setLayout(gridBagLayout1);
      this.getContentPane().add(jPanel1, BorderLayout.CENTER);
      jPanel1.add(tableScrollPane1, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 375, 300));
      jPanel1.add(jdbStatusLabel1, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 150, 0));
      jPanel1.add(jdbNavToolBar1, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
      jPanel1.add(jTextField1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 100, 0));
      jPanel1.add(jButton1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

      tableScrollPane1.setPreferredSize(new Dimension(375, 300));
      tableScrollPane1.getViewport().add(jdbTable1, null);
   }

  /**
   * Get Applet information
   *
   * @return String
   */
  public String getAppletInfo()
   {
      return "Applet Information";
   }

  /**
   * Get parameter info
   *
   * @return String[][]
   */
  public String[][] getParameterInfo()
   {
      return null;
   }

   //static initializer for setting look & feel
   static
   {
      try
      {
         //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
      }
      catch(Exception e)
      {
      }
   }

}

