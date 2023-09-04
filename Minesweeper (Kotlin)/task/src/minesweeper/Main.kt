package minesweeper

import java.util.Scanner
import kotlin.random.Random
import kotlin.system.exitProcess

val minesField = mutableListOf<MutableList<Cell>>()
val scanner = Scanner(System.`in`)
var nbMines: Int = 0

data class Cell(val content: Char = '.', val nbMinesAround: Char = '.', val hiddenContent: Char = '.')

fun main() {
    println("How many mines do you want on the field?")
    val cell = Cell()
    repeat(9) {
        val row = mutableListOf<Cell>()
        repeat(9) {
            row.add(cell.copy())
        }
        minesField.add(
            row
        )
    }
    nbMines = scanner.nextInt()
    fillField(nbMines)
    //showField()
    //showFieldWithMines()

    for (k in 0..8) {
        for (l in 0..8) {
            if (minesField[k][l].content == '.') {
                minesField[k][l] = minesField[k][l].copy(
                    nbMinesAround = countMinesAroundCell(k, l)
                )
            }
        }
    }
    showField()
    //showFieldWithNbOfMinesAroundCell()
    while (
        minesField.any {
            it.any { cell ->
                cell.hiddenContent == 'X' && cell.content != '*'
            }
        } && minesField.any {
            it.any { cell ->
                cell.hiddenContent == '.' && cell.content == '.'
            }
        }
    ) {
        setUnsetExploreMines()
    }
    println("Congratulations! You found all the mines!4")
    //showField()
    //printFieldAsEmpty()
    //setUnsetExploreMines()
    //printFieldAndHideMines()

}

fun showFieldWithNbOfMinesAroundCell() {
    println(" |123456789|")
    println("-|---------|")
    minesField.forEachIndexed { index, row ->
        print("${index + 1}|")
        row.forEach {
            print(if (it.hiddenContent == 'X') 'X' else it.nbMinesAround)
        }
        println("|")
    }
    println("-|---------|")
}

fun showField() {
    println(" |123456789|")
    println("-|---------|")
    minesField.forEachIndexed { index, row ->
        print("${index + 1}|")
        row.forEach {
            print(it.content)
        }
        println("|")
    }
    println("-|---------|")
    //showFieldWithMines()
}

fun fillField(nbMines: Int) {
    var i = 1
    while (i <= nbMines) {
        val random = Random.Default
        val randomRaw = random.nextInt(0, 9)
        val randomColumn = random.nextInt(0, 9)
        if (minesField[randomRaw][randomColumn].hiddenContent != 'X') {
            minesField[randomRaw][randomColumn] = minesField[randomRaw][randomColumn].copy(hiddenContent = 'X')
            i++
        }
    }
}

fun countMinesAroundCell(i: Int, j: Int): Char {
    var nbMines = 0
    val indexes = setOf(
        i to j - 1,
        i to j + 1,
        i - 1 to j - 1,
        i - 1 to j,
        i - 1 to j + 1,
        i + 1 to j - 1,
        i + 1 to j,
        i + 1 to j + 1
    ).filter {
        it.first in 0..8 && it.second in 0..8
    }
    indexes.forEach { (key, value) ->
        if (minesField[key][value].hiddenContent == 'X') nbMines++
    }
    return if (nbMines == 0) '.' else nbMines.digitToChar()
}

fun setUnsetExploreMines() {
    println("Set/unset mines marks or claim a cell as free:")
    val x = scanner.nextInt()
    val y = scanner.nextInt()
    val command = scanner.next()
    when (command) {
        "free" -> {
            exploreFirstCell(x, y)
        }

        "mine" -> {
            markCell(x, y)
            showField()
        }
    }
}

fun markCell(x: Int, y: Int) {
    if (minesField[y - 1][x - 1].content == '*'){
        minesField[y - 1][x - 1] = minesField[y - 1][x - 1].copy(content = '.')
    } else if (minesField[y - 1][x - 1].content == '.') {
        minesField[y - 1][x - 1] = minesField[y - 1][x - 1].copy(content = '*')
    }
}

fun exploreFirstCell(x: Int, y: Int) {
    if (minesField[y - 1][x - 1].hiddenContent == 'X') {
        println("You stepped on a mine and failed!")
        showField()
        exitProcess(0)
    } else if (minesField[y - 1][x - 1].nbMinesAround != '.') {
        minesField[y - 1][x - 1] =
            minesField[y - 1][x - 1].copy(
                content = minesField[y - 1][x - 1].nbMinesAround
            )
        showField()
    } else {
        minesField[y - 1][x - 1] = minesField[y - 1][x - 1].copy(content = '/')
        exploreNextCells(y - 1, x - 1)
        showField()
    }
}

fun exploreCell(i: Int, j: Int) {
    if (minesField[i][j].content == '.' || minesField[i][j].content =='*') {
        if (minesField[i][j].nbMinesAround != '.') {
            minesField[i][j] =
                minesField[i][j].copy(
                    content = minesField[i][j].nbMinesAround
                )
        } else {
            minesField[i][j] = minesField[i][j].copy(content = '/')
            exploreNextCells(i, j)
        }
    }
}

fun exploreNextCells(i: Int, j: Int) {
    val indexes = setOf(
        i to j - 1,
        i to j + 1,
        i - 1 to j - 1,
        i - 1 to j,
        i - 1 to j + 1,
        i + 1 to j - 1,
        i + 1 to j,
        i + 1 to j + 1
    ).filter {
        it.first in 0..8 && it.second in 0..8
    }
    indexes.forEach {
        exploreCell(it.first, it.second)
    }

}

fun getNumberOfMarkedCells(): Int {
    var nbCells = 0
    for (row in minesField) {
        for (c in row) {
            if (c.content == '*') {
                nbCells++
            }
        }
    }
    return nbCells
}


fun showFieldWithMines() {
    println(" |123456789|")
    println("-|---------|")
    minesField.forEachIndexed { index, row ->
        print("${index + 1}|")
        row.forEach {
            print(it.hiddenContent)
        }
        println("|")
    }
    println("-|---------|")
}

fun printFieldAsEmpty() {
    println(" |123456789|")
    println("-|---------|")
    minesField.forEachIndexed { index, row ->
        print("${index + 1}|")
        row.forEach { _ ->
            print('.')
        }
        println("|")
    }
    println("-|---------|")
}

