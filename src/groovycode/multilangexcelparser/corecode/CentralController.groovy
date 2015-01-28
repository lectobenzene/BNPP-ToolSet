package groovycode.multilangexcelparser.corecode

class CentralController {

	public static String SOURCE_FILE_LOCATION = '/Users/Saravana/Documents/BNPP/Documentation/Multi-Lang/MR1-2014/Mar05/Mobile_merged_20140304.xls'

	public static String DESTINATION_FOLDER_LOCATION = '/Users/Saravana/Documents/Workspace/MR1_MainBranch_FOR_BUILD'
	public static langColumns = [
		'FR',
		'NL',
		'NLB',
		'EN',
		'DE'
	]
	public static idColumn = ['TEXT ID']
	public static def underlinedList = ['42631', '42632', '47823']

	public static String encodingWrite = 'UTF-8'


	static main(args) {
		runIntegration()
	}

	public static runIntegration(){

        println 'Running Integration...'
		MultiLangWriter writer = new MultiLangWriter()

		def ommitedList = []
		ExcelReader reader = new ExcelReader(SOURCE_FILE_LOCATION, 1, idColumn, langColumns, underlinedList, ommitedList)
		def writerMap = reader.runExcelReader()

		writer.writeFileContents("$DESTINATION_FOLDER_LOCATION/res/values/strings.xml", writerMap['EN'])
		writer.writeFileContents("$DESTINATION_FOLDER_LOCATION/res/values-en/strings.xml", writerMap['EN'])
		writer.writeFileContents("$DESTINATION_FOLDER_LOCATION/res/values-de/strings.xml", writerMap['DE'])

		if(writerMap['NL']){
			writer.writeFileContents("$DESTINATION_FOLDER_LOCATION/res/values-nl/strings.xml", writerMap['NL'])
		}else if(writerMap['NLB']){
			writer.writeFileContents("$DESTINATION_FOLDER_LOCATION/res/values-nl/strings.xml", writerMap['NLB'])
		}
		writer.writeFileContents("$DESTINATION_FOLDER_LOCATION/res/values-fr/strings.xml", writerMap['FR'])



		ommitedList = ['48021']
		reader = new ExcelReader(SOURCE_FILE_LOCATION, 2, idColumn, langColumns, underlinedList, ommitedList)
		writerMap = reader.runExcelReader()

		writer.writeFileContents("$DESTINATION_FOLDER_LOCATION/res_hellobank/values/strings_en.xml", writerMap['EN'])
		writer.writeFileContents("$DESTINATION_FOLDER_LOCATION/res_hellobank/values-en/strings.xml", writerMap['EN'])

		if(writerMap['NL']){
			writer.writeFileContents("$DESTINATION_FOLDER_LOCATION/res_hellobank/values-nl/strings.xml", writerMap['NL'])
		}else if(writerMap['NLB']){
			writer.writeFileContents("$DESTINATION_FOLDER_LOCATION/res_hellobank/values-nl/strings.xml", writerMap['NLB'])
		}

		writer.writeFileContents("$DESTINATION_FOLDER_LOCATION/res_hellobank/values-de/strings.xml", writerMap['DE'])
		writer.writeFileContents("$DESTINATION_FOLDER_LOCATION/res_hellobank/values-fr/strings.xml", writerMap['FR'])

        println 'Integration Completed'
	}
}
