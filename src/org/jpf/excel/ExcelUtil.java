package org.jpf.excel;



import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author 吴平福
 * @version 1.0
 */


public class ExcelUtil
{
   public ExcelUtil()
   {
   }

   /**
    * <p>Description:判断EXECL表格是否为空</p>
    * @throws Exception
    */

   public static boolean isRowNull(Sheet sheet, int iRow) throws Exception
   {
      if (sheet.getRows() == iRow)
      {
         return true;
      }
      boolean bIsNull = true;
      try
      {
         Cell[] cell = sheet.getRow(iRow);
         for (int i = 0; i < cell.length; i++)
         {
            if (!cell[i].getContents().equalsIgnoreCase(""))
            {
               bIsNull = false;
               break;
            }
         }
      } catch (Exception ex)
      {
         ex.printStackTrace();
         throw ex;
      }
      return bIsNull;
   }

   /**
    *
    * @param in_cell Cell
    * @param in_row String
    * @return String
    */
   public static String getCellLabel(Cell in_cell, String in_row,
                                     String[] m_array)
   {
      //System.out.println(in_cell.getType());
      for (int i = 0; i < m_array.length; i++)
      {
         if (m_array[i].equalsIgnoreCase(in_row))
         {
            NumberCell m_cell = (NumberCell) in_cell;
            return String.valueOf(m_cell.getValue());
            //return in_cell.getContents().replaceAll(",","");
         }
      }
      return in_cell.getContents().trim();

   }

   public static void addCell(WritableSheet ws, int iRow, int iCol,
                              double dValue,
                              jxl.write.WritableCellFormat wcfN) throws
      Exception
   {
      jxl.write.Number nc = new jxl.write.Number(iCol - 1, iRow - 1, dValue, wcfN);
      ws.addCell(nc);

   }

   public static void addCell(WritableSheet ws, int iRow, int iCol,
                              double dValue) throws
      Exception
   {
      WritableCell cell = ws.getWritableCell(iCol - 1, iRow - 1);
      if (cell.getType() == CellType.NUMBER)
      {
         jxl.write.Number lbl = (jxl.write.Number) cell;
         lbl.setValue(dValue);
      } else
      {
         jxl.write.Number nc = new jxl.write.Number(iCol - 1, iRow - 1, dValue);
         if(cell.getCellFormat()!=null)
         {
            nc.setCellFormat(cell.getCellFormat());
         }
         ws.addCell(nc);
      }

   }

   /**
    *
    * @param ws WritableSheet
    * @param iRow int
    * @param iCol int
    * @param s String
    * @throws Exception
    */
   public static void addCell(WritableSheet ws, int iRow, int iCol, String s) throws
      Exception
   {
      WritableCell cell = ws.getWritableCell(iCol - 1, iRow - 1);
      //System.out.println("cell.getType()=" + cell.getType());

      if (cell.getType() == CellType.LABEL)
      {
         jxl.write.Label lbl = (jxl.write.Label) cell;
         lbl.setString(s);
      } else
      {
         jxl.write.Label label = new jxl.write.Label(iCol - 1, iRow - 1, s);
         if(cell.getCellFormat()!=null)
         {
         label.setCellFormat(cell.getCellFormat());
         }
         ws.addCell(label);
      }

   }
   public static String FormatStr1740(String in_Str)
   {
	   if(in_Str.equalsIgnoreCase("#"))
	   {
		   return "'"+in_Str+"'";
	   }else
	   if(in_Str.equalsIgnoreCase("$"))
	   {
		   return "'"+in_Str+"'";
	   }else
	   if(in_Str.equalsIgnoreCase("_"))
	   {
		   return "'"+in_Str+"'";
	   }
	   return in_Str;
   }
}
