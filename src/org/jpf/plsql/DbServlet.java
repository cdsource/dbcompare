/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2011-4-26 下午04:56:51 
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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;


import com.borland.dx.sql.dataset.Database;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.dataset.DataSetData;
import com.borland.dx.dataset.DataSetException;
import com.aiqcs.utils.db.DBManager;

/**
 *==============================================================================
 * Class: dbServlet.DbServlet
 * Case:  442770
 *
 * Description:
 * ===========
 *
 * Servlet that connects to a database and streams the data back to the
 * caller in the form of a DataSetData component.
 *==============================================================================
 */

public class DbServlet extends HttpServlet
{
   private Database database1 = new Database();
   private QueryDataSet queryDataSet1 = new QueryDataSet();

   // Database connection information.  Modify the path to the database if necessary.
   // If you are not using the JDataStore driver and the sample employee database,
   // comment out the settings here and modify the empty strings below to match your
   // database setup.
   private String query = "select * from sys_code_value";
//   private String url = "";
//   private String userName = "";
//   private String password = "";
//   private String driver = "";
//   private String query = "";

  /**
   * Process the HTTP Get request
   *
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @throws ServletException exception
   * @throws IOException exception
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response)
   throws ServletException, IOException
   //===========================================================================
   //  This method processes the HTTP Get request to this servlet.  It streams
   //  the result of the query using a DataSetData component.
   //===========================================================================
   {
      ObjectOutputStream dbStream = null;
      DataSetData data = null;
      Connection conn=null;
      System.out.println("doGet");
      try
      {
    	  conn = DBManager.getConnection();
         database1.setJdbcConnection(conn);
         queryDataSet1.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(database1, query, null, true, Load.ALL));
         queryDataSet1.open();

         data = DataSetData.extractDataSet( queryDataSet1 );

         // Get the response stream so we can write the object to it.
         dbStream = new ObjectOutputStream( response.getOutputStream() );
         // Write the object...
         dbStream.writeObject( data );
      }
      catch( DataSetException e )
      {
         System.err.println( "DataSetException caught in DbServlet.doGet()" );
         System.err.println( e.getMessage() );
         e.printStackTrace( System.err );
      }catch(Exception ex)
      {
    	  ex.printStackTrace();
      }
      finally
      {
         if( dbStream != null )
         {
            dbStream.flush();
            dbStream.close();
         }

         try
         {
            queryDataSet1.close();
            database1.closeConnection();
         }
         catch( DataSetException ee )
         {
            System.err.println( "DataSetException caught in DbServlet.doGet()" );
            System.err.println( ee.getMessage() );
            ee.printStackTrace( System.err );
         }
      }
   }

   /**
    * ==============================================================================
    * JBuilder generated code follows, not modified.
    * ==============================================================================
    */

  /**
   * Initialize global variables
   *
   * @param config ServletConfig
   * @throws ServletException exception
   */
  public void init(ServletConfig config) throws ServletException
   {
      super.init(config);
   }


   public DbServlet()
   {
   }
}

