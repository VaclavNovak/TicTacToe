package net.monarezio.domain

import net.monarezio.domain.common.extensions.diagonalLeft
import net.monarezio.domain.common.extensions.diagonalLeftKeys
import net.monarezio.domain.common.extensions.diagonalRight
import net.monarezio.domain.common.extensions.diagonalRightKeys
import net.monarezio.domain.models.Board
import net.monarezio.domain.models.Coordinate
import net.monarezio.domain.models.Field
import net.monarezio.domain.models.GameBoard

/**
 * Created by monarezio on 04/05/2017.
 */
class Game private constructor(private val board: Board, private val playerOnMove: Field, private val winNumber: Int): TicTacToe {

    override fun makeMove(x: Int, y: Int): TicTacToe {
        if(isMoveAvailable(x, y) && !isGameOver())
            return createGame(board.setField(x, y, playerOnMove), playerOnMove.toggle(), winNumber)
        return this
    }

    override fun getWinner(): Pair<Field, List<Coordinate>> {
        val fields = board.getFields()

        for(i in 0..board.getRows() - winNumber) {
            for(j in 0..board.getColumns() - winNumber) {
                val rowsCoordinates = i.rangeTo(i + winNumber - 1).map{index -> Coordinate(index, j)}
                val rows = i.rangeTo(i + winNumber - 1).map { index -> fields[index][j] }
                if(rows.all { item -> item == Field.CROSS })
                    return Pair(Field.CROSS, rowsCoordinates.toList())
                else if(rows.all { item -> item == Field.CIRCLE })
                    return Pair(Field.CIRCLE, rowsCoordinates.toList())

                val columnsCoordinates = j.rangeTo(j + winNumber - 1).map { index -> Coordinate(i, index) }
                val columns = j.rangeTo(j + winNumber - 1).map { index -> fields[i][index] }
                if(columns.all { item -> item == Field.CROSS })
                    return Pair(Field.CROSS, columnsCoordinates)
                else if(columns.all { item -> item == Field.CIRCLE })
                    return Pair(Field.CIRCLE, columnsCoordinates)

                val diagonalRightCoordinates = fields.diagonalRightKeys(i, j, winNumber)
                val diagonalRight = fields.diagonalRight(i, j, winNumber)
                if(diagonalRight.all { item -> item == Field.CROSS })
                    return Pair(Field.CROSS, diagonalRightCoordinates)
                else if(diagonalRight.all { item -> item == Field.CIRCLE })
                    return Pair(Field.CIRCLE, diagonalRightCoordinates)

                val diagonalLeftCoordinate = fields.diagonalLeftKeys(i, j + winNumber - 1, winNumber)
                val diagonalLeft = fields.diagonalLeft(i, j + winNumber - 1, winNumber)
                if(diagonalLeft.all { item -> item == Field.CROSS })
                    return Pair(Field.CROSS, diagonalLeftCoordinate)
                else if(diagonalLeft.all { item -> item == Field.CIRCLE })
                    return Pair(Field.CIRCLE, diagonalLeftCoordinate)
            }
        }

        return Pair(Field.ANON, listOf())
    }

    override fun isGameOver(): Boolean = getWinner().first != Field.ANON

    override fun isMoveAvailable(x: Int, y: Int): Boolean = board.getField(x, y) == Field.ANON

    override fun getPlayerOnMove(): Field = playerOnMove

    override fun getBoard(): Board = board

    companion object {

        /**
         * creates a new game with an empty gameboard
         */
        fun createNewGame(rows: Int , columns: Int, winNumber: Int = 5): TicTacToe =  Game(GameBoard.createNewBoard(rows, columns), Field.CROSS, winNumber)

        /**
         * create a game with the presets
         */
        fun createGame(board: Board, playerOnMove: Field, winNumber: Int) = Game(board, playerOnMove, winNumber)
    }
}