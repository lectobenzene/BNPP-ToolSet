package groovycode.multilangexcelparser.corecode

import groovy.xml.MarkupBuilder

import org.apache.poi.hssf.usermodel.HSSFAnchor
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.DateUtil
import org.apache.poi.ss.usermodel.FormulaEvaluator
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory

class ExcelReader {

	def sourceFile
	def idColumn
	def langColumns
	def underlinedList
	def ommitedList
	def sheetNo

	ExcelReader(){
	}

	ExcelReader(sourceFile, sheetNo, idColumn, langColumns, underlinedList, ommitedList){
		this.sourceFile  = sourceFile
		this.idColumn = idColumn
		this.langColumns = langColumns
		this.underlinedList = underlinedList
		this.ommitedList = ommitedList
		this.sheetNo = sheetNo
	}

	public runExcelReader(){
		def workbook = WorkbookFactory.create(new File(sourceFile))
		def sheet = workbook.getSheetAt(sheetNo)

		langColumns.each {
			ExcelReader.metaClass."writer$it" = new StringWriter()
			def temp= new ExcelReader()
			ExcelReader.metaClass."builder$it" = new MarkupBuilder(temp."writer$it")
			temp."builder$it".setDoubleQuotes(true)
		}

		return processSheet(sheet, idColumn, langColumns, underlinedList)
	}

	/**
	 * 
	 * @param sheet The sheet in the Excel document to process
	 */
	private processSheet(sheet, idColumn, langColumns, underlinedList) {

		def columnMap = [:]
		def multiLangID = null
		def value
		def idColumnNo
		def access = new ExcelReader()

		for(row in sheet){

			/* Get a Map of ColumnName : ColumnId
			 * This is for the first row only.
			 */
			if(row.getRowNum() == 0){
				for(cell in row){
					cell.setCellType(Cell.CELL_TYPE_STRING)
					[idColumn, langColumns].flatten().each {
						if(it == getCellValue(cell).trim()){
							columnMap[it] = cell.getColumnIndex()
						}
					}
				}
				// Get the column id for the ID COLUMN
				idColumnNo = columnMap.find {idColumn.contains(it.key)}.value
				// Since the 1st row is handled, continue with 2nd row
				continue
			}

			for(cell in row){
				cell.setCellType(Cell.CELL_TYPE_STRING)
				value = getCellValue(cell)
				def counter = 0;
				columnMap.find {mKey, mValue ->
					counter++
					if(idColumnNo == cell.getColumnIndex()){
						if(ommitedList.contains(value)){
							multiLangID = null
						}else{
							multiLangID = value
						}
						return true
					}else if(mValue == cell.getColumnIndex() && multiLangID != null){
						appendBuilder(access["builder$mKey"], multiLangID, value, underlinedList)
						return true
					}
				}
			}
		}

		def maps = [:]
		langColumns.each{
			maps["$it"] = replaceChar(access["writer$it"])
		}
		return maps
	}


	/**
	 * 
	 * @param writer
	 * @return
	 */
	private replaceChar(writer) {
		return writer.toString().replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&")
	}

	/**
	 * 
	 * @param builder
	 * @param multiLangID
	 * @param value
	 * @param underlinedList
	 * @return
	 */
	private appendBuilder(builder, multiLangID, value, underlinedList) {
		if(value.find(/[&<>]/)){
			builder.string(name:"_${multiLangID}" , "<![CDATA[${value.replaceAll(/"/,'').replaceAll(/'/,/\\'/)}]]>")
		}else{
			if(underlinedList.contains(multiLangID)){
				builder.string(name:"_${multiLangID}"){ u "${value.replaceAll(/"/,'').replaceAll(/'/,/\\'/)}" }
			}else{
				builder.string(name:"_${multiLangID}" , "${value.replaceAll(/"/,'').replaceAll(/'/,/\\'/)}")
			}
		}
	}

	/**
	 * Returns the content of an Excel Cell.
	 * 
	 * @param cell The cell from which data has to be taken
	 * @return The String value of the content in the cell
	 */
	private String getCellValue(cell) {
		return cell.getRichStringCellValue().getString()
	}
}
