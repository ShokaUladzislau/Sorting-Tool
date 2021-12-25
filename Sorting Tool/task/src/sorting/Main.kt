package sorting

import java.io.File
import java.util.*
import kotlin.math.roundToLong

val scanner = Scanner(System.`in`)
var inputFilePath = ""
var outputFilePath = ""


fun main(args: Array<String>) {
    var sortType = "natural"
    var dataType = "word"

    args.map {
        when (it) {
            "-inputFile" -> {
                try {
                    inputFilePath = args[args.indexOf(it) + 1]
                } catch (e: ArrayIndexOutOfBoundsException) {
                    println("No input file defined!")
                }
            }
            "-outputFile" -> {
                try {
                    outputFilePath = args[args.indexOf(it) + 1]
                } catch (e: ArrayIndexOutOfBoundsException) {
                    println("No output file defined!")
                }
            }
            "-dataType" -> {
                try {
                    dataType = args[args.indexOf(it) + 1]
                } catch (e: ArrayIndexOutOfBoundsException) {
                    exitProgram("No data type defined!")
                }
            }
            "-sortingType" -> {
                try {
                    sortType = args[args.indexOf(it) + 1]
                } catch (e: ArrayIndexOutOfBoundsException) {
                    exitProgram("No sorting type defined!")
                }
            }
            else -> {
                val arg = it
                if (it.matches(Regex("-\\w+"))) {
                    println("\"$arg\" is not a valid parameter. It will be skipped.")
                }
            }
        }
    }
    chooseDataType(sortType, dataType)
}

fun chooseDataType(sortType: String, dataType: String) {
    var lines: MutableList<String> = arrayListOf()
    var stringItems = listOf<String>()
    var longItems = listOf<Long>()

    if (inputFilePath.isNotEmpty()) {
        lines = File(inputFilePath).readLines().toMutableList()
    } else {
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine())
        }
    }

    when (dataType) {
        "long" -> longItems = lines.map { it.split(Regex("\\s+")) }.flatten().map { it.toLong() }.toList()
        "word" -> stringItems = lines.map { it.split(Regex("\\s+")) }.flatten()
        "line" -> stringItems = lines
    }

    when (dataType) {
        "long" -> println("Total numbers: ${longItems.size}.")
        "word" -> println("Total words: ${stringItems.size}.")
        "line" -> println("Total lines: ${stringItems.size}.")
    }

    if (stringItems.isNotEmpty()) {
        sortItems(sortType, stringItems)
    } else {
        sortItems(sortType, longItems)
    }
}

fun <T : Comparable<T>> sortItems(sortType: String, arrayList: List<T>) {
    when (sortType) {
        "natural" -> {
            val output = arrayList.sorted().joinToString().replace(",", "")
            if (outputFilePath.isNotEmpty()) {
                File(outputFilePath).appendText(output)
            } else {
                println("Sorted data: $output")
            }
        }
        "byCount" -> {
            arrayList
                .groupingBy { it }
                .eachCount()
                .toList()
                .sortedBy { (_, value) -> value }
                .groupBy { it.second }
                .values
                .map { it.sortedBy { (key, _) -> key } }
                .flatten()
                .toMap()
                .map {
                    val output =
                        "${it.key}: ${it.value} time(s), ${(it.value.toDouble() * 100 / arrayList.size.toDouble()).roundToLong()}%"
                    if (outputFilePath.isNotEmpty()) {
                        File(outputFilePath).appendText(output)
                    } else {
                        println(output)
                    }
                }
        }
    }
}

fun exitProgram(message: String) {
    println(message)
}