package groovycode.multilangexcelparser.corecode

/**
 * Handles the logic of writing to the Destination file
 * 
 * @author Saravana T / 587418
 */
class MultiLangWriter {

	/**
	 * The String to lookup in the Destination file pointing the <b>start</b> of replacing
	 */
	static String KEY_START = "START.START.START"
	/**
	 * The String to lookup in the Destination file pointing the <b>end</b> of replacing
	 */
	static String KEY_END = "END.END.END"

	/**
	 * Replaces the content between the Start pointer and End pointer in the file with the contents
	 * provided
	 * 
	 * @param filePath The String that denotes the location of the file
	 * @param fileContents The contents that replaces the existing content in the file
	 */
	def writeFileContents(filePath, fileContents) {
		def stringsFile = new File(filePath)
		def fullContent = new StringWriter()
		boolean isPrintable = false

		// if fileContents is null, then do not perform any operation
		if(fileContents){
			// Logic to construct the text of the entire destination document
			// Try to replace this logic, so that conditions are not checked always
			stringsFile.eachLine {
				if(it.contains(KEY_END)){
					isPrintable = false
				}
				if(!isPrintable){
					fullContent << it + System.getProperty("line.separator")
				}
				if(it.contains(KEY_START)){
					isPrintable = true
					fullContent << fileContents << System.getProperty("line.separator")
				}
			}

			// Write the new content to the destination file
			stringsFile.write(fullContent.toString(), CentralController.encodingWrite)
		}
	}
}
