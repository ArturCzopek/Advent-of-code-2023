package pl.simplecoding.aoc

object AoCUtils {

    fun getFile(fileName: String): String {
        // Load the file as a resource using the class loader
        val classLoader = object {}.javaClass.classLoader
        val fileUrl = classLoader.getResource(fileName)

        // Check if the file exists
        if (fileUrl != null) {
            // Read the content of the file
            val fileContent = classLoader.getResourceAsStream(fileName)?.bufferedReader().use { it?.readText() }

            // Print the content
            return fileContent!!
        } else {
            println("File not found: $fileName")
            return ""
        }
    }
}