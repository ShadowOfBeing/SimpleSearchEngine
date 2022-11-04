package search
import kotlin.system.exitProcess
import java.io.File

object MyBeautifulSearchEngine {
    lateinit var command : String
    private val stringsIndexes = mutableMapOf<String, MutableList<Int>>()
    private var strings = mutableListOf<String>()
    private var originalStrings = listOf<String>()
    private val stringsMap = mutableListOf<Int>()

    init {
        val filename = "src/main/kotlin/" + readln()
        originalStrings = File(filename).readLines()
        strings = originalStrings.toSet().toMutableList()
        for (string in strings) {
            stringsMap.add(string.split(" ").size)
        }
        for ((index, string) in strings.withIndex()) {
            for (word in string.split(" ")) {
                if (word.uppercase() in stringsIndexes) {
                    stringsIndexes[word.uppercase()]!!.add(index)
                } else {
                    stringsIndexes[word.uppercase()] = mutableListOf(index)
                }
            }
        }
        this.getMenu()
    }

    private fun getMenu() {
        println("\n=== Menu ===\n1. Find a person\n2. Print all persons\n0. Exit")
        command = readln()
        when (this.command) {
            "0" -> this.bye()
            "1" -> {
                println("\nSelect a matching strategy: ALL, ANY, NONE")
                this.runSearch(readln())
            }
            "2" -> this.printAllStrings()
            else -> {
                println("Incorrect option! Try again.")
                getMenu()
            }
        }
    }

    fun runSearch(strategy: String) {
        println("\nEnter a name or email to search all matching people.")
        val request = readln().uppercase().split(" ").filter { it != " " }.toSet().toList()
        val returnList = mutableListOf<String>()
        val stringsMapTmp = stringsMap.toList().toMutableList()
        for (word in stringsIndexes) {
            if (word.key in request) {
                for (index in word.value) {
                    stringsMapTmp[index] = stringsMapTmp[index] - 1
                }
            }
        }
        for ((index, item) in stringsMapTmp.withIndex()) {
            when (strategy) {
                "ALL" -> {
                    if (item == stringsMap[index] - request.size) {
                        returnList.add(strings[index])
                    }
                }
                "ANY" -> {
                    if (item != stringsMap[index]) {
                        returnList.add(strings[index])
                    }
                }
                "NONE" -> {
                    if (item == stringsMap[index]) {
                        returnList.add(strings[index])
                    }
                }
            }
        }
        returnList.toSet().toList()
        println()
        if (returnList.size != 0) {
            if (returnList.size == 1) {
                println("1 person found:")
            } else {
                println("${returnList.size} persons found:")
            }
            returnList.forEach { println(it) }
        } else {
            println("No matching people found.")
        }
        getMenu()
    }

    fun bye() {
        println()
        println("Bye!")
        exitProcess(1)
    }

    fun printAllStrings() {
        println()
        originalStrings.forEach { println(it) }
        this.getMenu()
    }
}

fun main() {
    MyBeautifulSearchEngine
}