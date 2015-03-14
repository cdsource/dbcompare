package org.jpf.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.DateTime;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

//import com.asiainfo.openwbass.utils.config.ConfigUtilFilePath;
/**
 *
 * <p>Title: NGBOSS--WBASS</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: asiainfo</p>
 *
 * @author 吴平福
 * @version 4.0
 */
public class ExcelFile
{

   private WritableWorkbook workBook;

   /**
    * @return Returns the workBook.
    */
   public WritableWorkbook getWorkBook()
   {
      return workBook;
   }

   File inputWorkbook;

   File outputWorkbook;

   /**
    */
   public ExcelFile()
   {

   }

   /**
    *
    * @param function
    * @return
    * @throws Exception
    */
   // protected ExcelFile createExcelFileFromTemplate(String function) throws
   // Exception{
   // ExcelFile excelFile = new ExcelFile();
   // String filepath = APSystem.getFileDir("reportfile");
   // excelOutFileName = function + "_" + new Date().getTime() + ".xls";
   // excelFile.createExcelFile(filepath+"template/"+function+".xls",
   // filepath+getExcelOutFileName(function));
   // return excelFile;
   // }
   /**
    * Copy the template excel file to the output excel file for further
    * modification.
    *
    * @param input
    *            the file name of the template excel file
    * @param output
    *            the file name of the output excel file
    * @throws java.lang.Exception
    */
   public void createExcelFile(String input, String output) throws Exception
   {
      this.inputWorkbook = new File(input);
      this.outputWorkbook = new File(output);

      Workbook w1 = Workbook.getWorkbook(inputWorkbook);
      this.workBook = Workbook.createWorkbook(outputWorkbook, w1);
      w1.close();
   }

   /**
    * ?佐??\uFFFDa姚\uFFFDo?Р\uFFFD\uFFFD\uFFFD\uFFFD???\uFFFDodata
    *
    * @param sheetIndex
    * @param startCol
    * @param endCol
    * @param row
    * @param doc
    *            if != null, write date, else only copy format and origin data
    * @param docIndex
    * @param templateRow
    *            \uFFFDf????row
    * @throws Exception
    */
   public void writeDataRowAsTemplate(int sheetIndex, String startCol,
                                      String endCol, int row, String[][] result,
                                      int docIndex,
                                      int templateRow) throws Exception
   {
      if (row < 1)
      {
         throw new Exception();
      }
      if (templateRow < 1)
      {
         throw new Exception();
      }

      WritableSheet sheet = this.workBook.getSheet(sheetIndex);
      WritableCell fromCell, toCell;
      for (int i = 0; i < sheet.getColumns(); i++)
      {
         fromCell = sheet.getWritableCell(i, templateRow - 1);
         if (fromCell.getType() != CellType.EMPTY)
         {
            toCell = fromCell.copyTo(i, row - 1);
            toCell.setCellFormat(fromCell.getCellFormat());
            sheet.addCell(toCell);
         } else if (fromCell.getCellFormat() != null)
         {
            toCell = sheet.getWritableCell(i, row - 1);
            toCell.setCellFormat(fromCell.getCellFormat());
         }
      }
      if (result != null && result[0].length > 0)
      {
         this.writeDataRow(sheetIndex, startCol, row, endCol, result,
                           docIndex);
      }
   }

   /**
    * copyTemplateto
    *
    * @param sheetfrom
    * @param sheetto
    * @throws Exception
    */
   public void copyTemplateto(int firstindex, int count) throws Exception
   {
      int sheets = this.workBook.getNumberOfSheets();
      for (int i = firstindex + 1; i < sheets; i++)
      {
         this.workBook.removeSheet(i);
      }
      for (int i = 1; i <= count; i++)
      {
         this.workBook.copySheet(firstindex,
                                 "Sheet" + String.valueOf(i + 1), i);
      }
   }

   /**
    * Save the output excel file and close it.
    */
   public void close()
   {
      try
      {
         if (this.workBook != null)
         {
            this.workBook.write();
         }
      } catch (Exception e)
      {
         e.printStackTrace();
      } finally
      {
         try
         {
            this.workBook.close();
         } catch (Exception e2)
         {
         }
      }
   }

   /**
    * Write a single cell in the output excel file.
    *
    * @param sheetIndex
    *            the index of the excel sheet to be write
    * @param sColumn
    *            the column('A' ~ 'Z') of the cell
    * @param row
    *            the row (1 or 2 or 3 ...) of the cell
    * @param content
    *            the content of the cell
    * @throws java.lang.Exception
    */
   public void writeCell(int sheetIndex, String sColumn, int row,
                         String content) throws Exception
   {
      int column = this.convertColumn(sColumn);
      if (row < 1)
      {
         throw new Exception();
      }
      row--;
      WritableSheet sheet = this.workBook.getSheet(sheetIndex);
      WritableCell cell = sheet.getWritableCell(column, row);
      if (cell.getType() == CellType.NUMBER)
      {
         Number number = (Number) cell;
         try
         {
            number.setValue(Double.parseDouble(content));
         } catch (Exception e)
         {
            number.setValue(0);
            // sheet.addCell(null);
         }
      } else if (cell.getType() == CellType.NUMBER_FORMULA
                 || cell.getType() == CellType.STRING_FORMULA
                 || cell.getType() == CellType.ERROR)
      {
         Formula form = new Formula(column, row, content);
         form.setCellFormat(cell.getCellFormat());
         sheet.addCell(form);
      } else if (cell.getType() == CellType.LABEL)
      {
         Label label = (Label) cell;
         label.setString(content);
      } else
      {
         Label label = new Label(column, row, content);
         sheet.addCell(label);
      }
   }

   /**
    * write a cell
    *
    * @param sheetIndex
    * @param column
    * @param row
    * @param content
    * @throws Exception
    */
   public void writeCell(int sheetIndex, int column, int row, String content) throws
      Exception
   {
      if (column < 0)
      {
         throw new Exception();
      }
      if (row < 1)
      {
         throw new Exception();
      }
      row--;
      WritableSheet sheet = this.workBook.getSheet(sheetIndex);
      WritableCell cell = sheet.getWritableCell(column, row);
      if (cell.getType() == CellType.NUMBER)
      {
         Number number = (Number) cell;
         try
         {
            number.setValue(Double.parseDouble(content));
         } catch (Exception e)
         {
            number.setValue(0);
            // sheet.addCell(null);
         }
      } else if (cell.getType() == CellType.DATE)
      {
         Calendar cal = Calendar.getInstance();
         cal.set(Integer.parseInt(content.substring(0, 4)), Integer
                 .parseInt(content.substring(5, 7)), Integer
                 .parseInt(content.substring(8, 10)), 0, 0, 0);
         Date d = cal.getTime();
         DateTime dateTime = (DateTime) cell;
         dateTime.setDate(d);
      } else if (cell.getType() == CellType.EMPTY)
      {
         Label label = new Label(column, row, content);
         sheet.addCell(label);
      } else if (cell.getType() == CellType.NUMBER_FORMULA
                 || cell.getType() == CellType.STRING_FORMULA
                 || cell.getType() == CellType.ERROR)
      {
         Formula form = new Formula(column, row, content);
         form.setCellFormat(cell.getCellFormat());
         sheet.addCell(form);
      } else
      {
         Label label = (Label) cell;
         label.setString(content);
      }
   }

   /**
    * write a cell as template(format)
    *
    * @param sheetIndex
    * @param column
    * @param row
    * @param tempColumn
    * @param tempRow
    * @param content
    * @throws Exception
    */
   public void writeCellAsTemplate(int sheetIndex, int column, int row,
                                   int tempColumn, int tempRow, String content) throws
      Exception
   {
      if (column != tempColumn || row != tempRow)
      {
         WritableSheet sheet = this.workBook.getSheet(sheetIndex);
         WritableCell cell = sheet.getWritableCell(tempColumn, tempRow - 1);
         sheet.addCell(cell.copyTo(column, row - 1));
      }
      this.writeCell(sheetIndex, column, row, content);
   }

   public void writeCellAsTemplate(int sheetIndex, String column, int row,
                                   String tempColumn, int tempRow,
                                   String content) throws Exception
   {
      int col = this.convertColumn(column);
      int tempCol = this.convertColumn(tempColumn);
      if (col != tempCol || row != tempRow)
      {
         WritableSheet sheet = this.workBook.getSheet(sheetIndex);
         WritableCell cell = sheet.getWritableCell(tempCol, tempRow - 1);
         sheet.addCell(cell.copyTo(col, row - 1));
      }
      this.writeCell(sheetIndex, column, row, content);
   }

   private int writeDataCell(int sheetIndex, int icolumn, int row,
                             String content) throws Exception
   {
      if ("&nbsp;".equalsIgnoreCase(content))
      {
         content = "";
      }
      if (icolumn < 0)
      {
         throw new Exception();
      }
      if (row < 0)
      {
         throw new Exception();
      }
      WritableSheet sheet = this.workBook.getSheet(sheetIndex);
      int skipColumn = 0;
      while (true)
      {
         Cell cell = sheet.getCell(icolumn, row);

         if (cell.getType() == CellType.NUMBER)
         {
            Number number = (Number) cell;
            double v = 0.0;
            try
            {
               v = Double.parseDouble(content);
            } catch (Exception e)
            {
            }
            number.setValue(v);
            break;
         } else if (cell.getType() == CellType.DATE)
         {
            if (content != null && content.trim().length() >= 10)
            {
               Calendar cal = Calendar.getInstance();
               cal.set(Integer.parseInt(content.substring(0, 4)), Integer
                       .parseInt(content.substring(5, 7)) - 1, Integer
                       .parseInt(content.substring(8, 10)), 0, 0, 0);
               Date d = cal.getTime();
               DateTime dateTime = (DateTime) cell;
               dateTime.setDate(d);
            }
            break;
         } else if (cell.getType() == CellType.NUMBER_FORMULA
                    || cell.getType() == CellType.STRING_FORMULA
                    || cell.getType() == CellType.ERROR)
         {
            Formula form = new Formula(icolumn, row, content);
            form.setCellFormat(cell.getCellFormat());
            sheet.addCell(form);
            break;
         } else if (cell.getType() == CellType.EMPTY)
         {
             icolumn++;
            skipColumn++;
            break;
         } else
         {
            Label label = (Label) cell;
            label.setString(content);
            break;
         }
      }
      return skipColumn;
   }

   private void writeDataRow(int sheetIndex, String sStartColumn, int row,
                             String sEndColumn, String[][] result,
                             int documentRow) throws Exception
   {
      if (row < 1)
      {
         throw new Exception();
      }
      row--;
      // NodeList list = document.getElementsByTagName("ROW");
      int startColumn = this.convertColumn(sStartColumn);
      int endColumn = this.convertColumn(sEndColumn);
      int listColumn = 1;
      // Node node = list.item(documentRow);
      int sheetColumn = startColumn;
      while (sheetColumn <= endColumn)
      {
         // String data = DOM.getNodeStr(node, "C" + listColumn);
         //	Debug.println(" write C" + listColumn);
         //	Debug.println(" row " + row);
         //	Debug.println(" sheetColumn " + sheetColumn);
         String data = result[listColumn - 1][documentRow];
         //	Debug.println("data:" + data);
         listColumn++;
         int skipColumn = this.writeDataCell(sheetIndex, sheetColumn, row,
                                             data);
         sheetColumn = sheetColumn + 1 + skipColumn;
      }
   }

   private void writeDataRow(int sheetIndex, int startColumn, int row,
                             int endColumn, String[][] result, int documentRow) throws
      Exception
   {
      if (row < 1)
      {
         throw new Exception();
      }
      row--;
      // NodeList list = document.getElementsByTagName("ROW");
      int listColumn = 1;
      // Node node = list.item(documentRow);
      int sheetColumn = startColumn;
      while (sheetColumn <= endColumn)
      {
         // String data = DOM.getNodeStr(node, "C" + listColumn);
         String data = result[listColumn - 1][row - 1];
         listColumn++;
         int skipColumn = this.writeDataCell(sheetIndex, sheetColumn, row,
                                             data);
         sheetColumn = sheetColumn + 1 + skipColumn;
      }
   }

   /**
    * empty a row (always sample data row)
    *
    * @param sheetIndex
    * @param sStartColumn
    * @param row
    * @param sEndColumn
    * @throws Exception
    */
   public void emptyDataRow(int sheetIndex, String sStartColumn, int row,
                            String sEndColumn) throws Exception
   {
      // int startColumn = this.convertColumn(sStartColumn);
      //int endColumn = this.convertColumn(sEndColumn);
      WritableSheet sheet = workBook.getSheet(sheetIndex);
      sheet.insertRow(row - 1);
      sheet.removeRow(row);
      // int sheetColumn = startColumn;
      // for (; sheetColumn <= endColumn; sheetColumn++) {
      // this.writeCell(sheetIndex, sheetColumn, row, "");
      // }
   }

   /**
    * empty a row (always sample data row)
    *
    * @param sheetIndex
    * @param sStartColumn
    * @param row
    * @param sEndColumn
    * @throws Exception
    */
   public void emptyDataRow(int sheetIndex, int startColumn, int row,
                            int endColumn) throws Exception
   {
      WritableSheet sheet = workBook.getSheet(sheetIndex);
      sheet.insertRow(row - 1);
      sheet.removeRow(row);
      // int sheetColumn = startColumn;
      // for (; sheetColumn <= endColumn; sheetColumn++) {
      // this.writeCell(sheetIndex, sheetColumn, row, "");
      // }
   }

   /**
    * Write rows of data from the document
    *
    * @param sheetIndex
    *            the index of the excel sheet to be write
    * @param sStartColumn
    *            the start column of the cell to be written
    * @param row
    *            the start row of the cell to be written
    * @param sEndColumn
    *            the end column of the cell to be written
    * @param document
    *            the xml document (data)
    * @param documentRow
    *            the start row of the document to be written
    * @param documentRowCount
    *            the count of the row of the document to be written
    * @throws java.lang.Exception
    */
   public void writeDataRows(int sheetIndex, String sStartColumn, int row,
                             String sEndColumn, String[][] result,
                             int documentRow,
                             int documentRowCount) throws Exception
   {
      if (row < 1)
      {
         throw new Exception();
      }
      this.writeDataRow(sheetIndex, sStartColumn, row, sEndColumn, result,
                        documentRow);
      row++;
      documentRow++;
      this.insertDataRows(sheetIndex, sStartColumn, row, sEndColumn, result,
                          documentRow, documentRowCount - 1);
   }

   /**
    * Write rows of data from the document
    *
    * @param sheetIndex
    *            the index of the excel sheet to be write
    * @param sStartColumn
    *            the start column of the cell to be written
    * @param row
    *            the start row of the cell to be written
    * @param sEndColumn
    *            the end column of the cell to be written
    * @param document
    *            the xml document (data)
    * @param documentRow
    *            the start row of the document to be written
    * @param documentRowCount
    *            the count of the row of the document to be written
    * @throws java.lang.Exception
    */
   public void writeDataRows(int sheetIndex, int iStartColumn, int row,
                             int iEndColumn, String[][] result, int documentRow,
                             int documentRowCount) throws Exception
   {
      if (row < 1)
      {
         throw new Exception();
      }
      this.writeDataRow(sheetIndex, iStartColumn, row, iEndColumn, result,
                        documentRow);
      row++;
      documentRow++;
      this.insertDataRows(sheetIndex, iStartColumn, row, iEndColumn, result,
                          documentRow, documentRowCount - 1);
   }

   private void insertDataRow(int sheetIndex, String sStartColumn, int row,
                              String sEndColumn, String[][] result,
                              int documentRow) throws Exception
   {
      if (row < 1)
      {
         throw new Exception();
      }
      WritableSheet sheet = this.workBook.getSheet(sheetIndex);
      sheet.insertRow(row - 1);
      WritableCell fromCell, toCell;
      for (int i = 0; i < sheet.getColumns(); i++)
      {
         fromCell = sheet.getWritableCell(i, row - 2);
         if (fromCell.getType() != CellType.EMPTY)
         {
            toCell = fromCell.copyTo(i, row - 1);
            sheet.addCell(toCell);
         }
      }
      this.writeDataRow(sheetIndex, sStartColumn, row, sEndColumn, result,
                        documentRow);
   }

   private void insertDataRow(int sheetIndex, int startColumn, int row,
                              int endColumn, String[][] result, int documentRow) throws
      Exception
   {
      if (row < 1)
      {
         throw new Exception();
      }
      WritableSheet sheet = this.workBook.getSheet(sheetIndex);
      sheet.insertRow(row - 1);
      WritableCell fromCell, toCell;
      for (int i = 0; i < sheet.getColumns(); i++)
      {
         fromCell = sheet.getWritableCell(i, row - 2);
         if (fromCell.getType() != CellType.EMPTY)
         {
            toCell = fromCell.copyTo(i, row - 1);
            sheet.addCell(toCell);
         }
      }
      this.writeDataRow(sheetIndex, startColumn, row, endColumn, result,
                        documentRow);
   }

   /**
    * insert data rows
    *
    * @param sheetIndex
    * @param sStartColumn
    * @param row
    * @param sEndColumn
    * @param document
    * @param documentRow
    * @param documentRowCount
    * @throws Exception
    */
   public void insertDataRows(int sheetIndex, String sStartColumn, int row,
                              String sEndColumn, String[][] result,
                              int documentRow,
                              int documentRowCount) throws Exception
   {
      if (row < 1)
      {
         throw new Exception();
      }
      //WritableSheet sheet = this.workBook.getSheet(sheetIndex);
      for (int i = 0; i < documentRowCount; i++)
      {
         this.insertDataRow(sheetIndex, sStartColumn, row, sEndColumn,
                            result, documentRow);
         row++;
         documentRow++;
      }
   }

   private void insertDataRows(int sheetIndex, int startColumn, int row,
                               int endColumn, String[][] result, int documentRow,
                               int documentRowCount) throws Exception
   {
      if (row < 1)
      {
         throw new Exception();
      }
      //WritableSheet sheet = this.workBook.getSheet(sheetIndex);
      for (int i = 0; i < documentRowCount; i++)
      {
         this.insertDataRow(sheetIndex, startColumn, row, endColumn, result,
                            documentRow);
         row++;
         documentRow++;
      }
   }

   public String getCellContents(int sheetIndex, int column, int row) throws
      Exception
   {
      if (column < 0)
      {
         throw new Exception();
      }
      if (row < 1)
      {
         throw new Exception();
      }
      row--;
      WritableSheet sheet = this.workBook.getSheet(sheetIndex);
      WritableCell cell = sheet.getWritableCell(column, row);

      return cell.getContents();
   }

   public void insertEmptyDataRowAsTemplate(int sheetIndex, int startColumn,
                                            int row, int endColumn,
                                            int templateRow) throws Exception
   {
      if (row < 1)
      {
         throw new Exception();
      }
      if (templateRow < 1)
      {
         throw new Exception();
      }
      WritableSheet sheet = this.workBook.getSheet(sheetIndex);
      sheet.insertRow(row - 1);
      WritableCell fromCell, toCell;
      for (int i = startColumn; i <= endColumn; i++)
      {
         fromCell = sheet.getWritableCell(i, templateRow - 1);
         toCell = fromCell.copyTo(i, row - 1);
         toCell.setCellFormat(fromCell.getCellFormat());
         sheet.addCell(toCell);
      }
   }

   /**
    * Merges the specified cells
    *
    * @param sheetIndex
    *            the index of the excel sheet to be write
    * @param col1 -
    *            the column number of the top left cell
    * @param row1 -
    *            the row number of the top left cell
    * @param col2 -
    *            the column number of the bottom right cell
    * @param row2 -
    *            the row number of the bottom right cell
    * @throws java.lang.Exception
    */
   public void mergeColumnCell(int sheetIndex, String col1, int row1,
                               String col2, int row2) throws Exception
   {
      row1--;
      row2--;
      int c1 = this.convertColumn(col1);
      int c2 = this.convertColumn(col2);
      WritableSheet sheet = this.workBook.getSheet(sheetIndex);
      sheet.mergeCells(c1, row1, c2, row2);
   }

   /**
    * @param sColumn
    * @return int
    * @throws java.lang.Exception
    * @roseuid 41C28DBE00AB
    */
   private int convertColumn(String sColumn) throws Exception
   {
      if (sColumn == null || sColumn.length() < 1 || sColumn.length() > 2)
      {
         throw new Exception();
      }
      //String s2;
      char ch1, ch2;
      int column;
      sColumn = sColumn.toUpperCase();
      if (sColumn.length() == 2)
      {
         ch1 = sColumn.charAt(0);
         ch2 = sColumn.charAt(1);
         if (ch1 < 'A' || ch1 > 'Z')
         {
            throw new Exception();
         }
         if (ch2 < 'A' || ch2 > 'Z')
         {
            throw new Exception();
         }
      } else if (sColumn.length() == 1)
      {
         ch1 = 'A' - 1;
         ch2 = sColumn.charAt(0);
         if (ch2 < 'A' || ch2 > 'Z')
         {
            throw new Exception();
         }
      } else
      {
         throw new Exception();
      }
      column = (ch1 - 'A' + 1) * 26 + ch2 - 'A';
      return column;
   }

   /**
    * Terry Tung 指定某Sheet的名\uFFFDQ
    *
    * @param sheetIndex
    * @param setName
    */
   public void writeSheetName(int sheetIndex, String setName)
   {
      WritableSheet wSheet = this.workBook.getSheet(sheetIndex);
      wSheet.setName(setName);
   }

   public String getSheetName(int sheetIndex)
   {
      WritableSheet wSheet = this.workBook.getSheet(sheetIndex);
      return wSheet.getName();
   }

   public static String[][] importExcel(String fullPath) throws Exception
   {
      String[][] strTemp = null;
      try
      {
         // 构建Workbook对象, 只读Workbook对象
         // 直接从本地文件创建Workbook
         // 从输入流创建Workbook
         InputStream is = new FileInputStream(fullPath);
         jxl.Workbook rwb = Workbook.getWorkbook(is);
         Sheet rs = rwb.getSheet(0);
         int rows = rs.getRows();
         int cols = rs.getColumns();
         Date strc11;
         //		System.out.println(rows+" "+cols);
         strTemp = new String[rows - 4][cols];
         SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         for (int i = 4; i < rows; i++)
         {
            for (int j = 0; j < cols; j++)
            {
               Cell ctemp = rs.getCell(j, i);
               if (ctemp.getType() == CellType.DATE)
               {
                  //DateTime dateTime = (DateTime) ctemp;
                  DateCell datec11 = (DateCell) ctemp;
                  strc11 = datec11.getDate();
                  System.out.println(strc11 + " ");
                  strTemp[i - 4][j] = sfd.format(strc11); //ctemp.getContents();
                  System.out.println(strTemp[i - 4][j] + " ");
               } else
               {
                  strTemp[i - 4][j] = ctemp.getContents();
               }
            }
         }
         rwb.close();
         for (int i = 0; i < rows - 4; i++)
         {
            for (int j = 0; j < cols; j++)
            {
               // System.out.print(strTemp[i][j].length());
               if (strTemp[i][j].equalsIgnoreCase(""))
               {
                  System.out.print("====");
               }
               System.out.print(strTemp[i][j] + " ");
               if (j == cols - 1)
               {
                  System.out.print("\n");
               }
            }
         }
      } catch (Exception ex)
      {
         ex.printStackTrace();
      }
      return strTemp;
   }

   public static void main(String[] args) throws Exception
   {

//	//	System.out.println(Integer.valueOf("FF",16).intValue());
//		String[] s0 = {"2005-09-01 10:12:12","2005-09-01 10:12:12","2005-09-01 10:12:12","2005-09-01","2005-09-01","2005-09-01","2005-09-01","2005-09-01"};
//		String[] s1 = {"杭州", "宁波","杭州","萧山","西湖","上城","下城","滨江"};
//		String[] s2 = {"全球通", "神州行","神州行","神州行","动感地带","动感地带","动感地带","全球通"};
//		String[] s3 =  {"650","150","200","300","400","460","100","450"};
//		String[][] result = { s0,s1,s2,s3 };

//	//	String[][] result = { {"杭州","全球通","4110"},{"宁波","全球通","4110"} };
//	//	for(int i =0; i< 4;i++){
//			for(int k=0;k<8;++k) {
//				Debug.println(result[0][k]);
//				Debug.println(result[1][k]);
//				Debug.println(result[2][k]);
//				Debug.println(result[3][k]);
//			}
//	//	}
      ExcelFile excelFile = null;
      try
      {
         /*
         ConfigUtilFilePath.setWebserverRoot("d:\\openwbass\\openwbass1.0\\openwbass\\");
         System.out.println(ConfigUtilFilePath.GetConfigPath());
         String fileName = ConfigUtilFilePath.GetConfigPath() + "2008年X月新通信网间结算.xls";

         excelFile = new ExcelFile();

         excelFile.createExcelFile(
            ConfigUtilFilePath.GetConfigPath() + "2008年X月新通信网间结算.xls",
            ConfigUtilFilePath.GetConfigPath() + "2008年12月新通信网间结算.xls");
         //excelFile.writeSheetName(0, "拨测号码分析报表");

         excelFile.writeCell(0, 4, 2, "aaa");
         //excelFile.writeCell(0, "b", 3,"bbb" );
         //excelFile.writeCell(0, "b", 4,"ccc");
         */

      } catch (Exception e)
      {
         e.printStackTrace();
      } finally
      {
         if (excelFile != null)
         {
            excelFile.close();
         }
      }
      //	importExcel("C:\\rs.xls");


   }

}
