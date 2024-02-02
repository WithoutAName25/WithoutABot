package eu.withoutaname.withoutabot

import com.kotlindiscord.kord.extensions.events.EventHandler
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.behavior.interaction.updatePublicMessage
import dev.kord.core.entity.User
import dev.kord.core.event.interaction.ButtonInteractionCreateEvent
import dev.kord.rest.builder.message.actionRow
import dev.kord.rest.builder.message.create.UpdateMessageInteractionResponseCreateBuilder
import io.ktor.util.collections.*

class TicTacToe : Extension() {
    companion object {
        private val customIdPrefix = "tictactoe"
        private fun customId(type: String, vararg args: String) = "$customIdPrefix:$type:${args.joinToString(":")}"

        context(EventHandler<ButtonInteractionCreateEvent>)
        private inline fun ticTacToeAction(crossinline block: suspend ButtonInteractionCreateEvent.(String, List<String>) -> Unit) {
            action {
                val customId = event.interaction.component.customId ?: return@action
                val parts = customId.split(":")
                if (parts[0] != customIdPrefix || parts.size < 2) return@action

                val type = parts[1]
                val args = parts.subList(2, parts.size)
                event.block(type, args)
            }
        }
    }

    override val name = "tictactoe"

    private val games = ConcurrentMap<Snowflake, Game>()

    override suspend fun setup() {
        publicSlashCommand {
            name = this@TicTacToe.name
            description = "Play a round of Tic Tac Toe"

            action {
                respond {
                    content = "Who want's to play tic tac toe against ${this@action.user.mention}?"
                    actionRow {
                        interactionButton(ButtonStyle.Success, customId("join")) {
                            label = "I want to play"
                        }
                    }
                }
            }
        }
        event<ButtonInteractionCreateEvent> {
            ticTacToeAction { type, args ->
                when (type) {
                    "join" -> {
                        val inviterUser = interaction.message.interaction?.user?.asUser() ?: return@ticTacToeAction
                        val joinedUser = interaction.user
                        if (games.containsKey(inviterUser.id)) return@ticTacToeAction
                        if (games.containsKey(joinedUser.id)) {
                            interaction.respondEphemeral {
                                content = "You are already in a game"
                            }
                            return@ticTacToeAction
                        }
                        val game = Game(inviterUser, joinedUser)
                        games[inviterUser.id] = game
                        games[joinedUser.id] = game
                        try {

                            interaction.updatePublicMessage {
                                game.toMessage()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    "field" -> {
                        val game = games[interaction.user.id] ?: return@ticTacToeAction
                        game.handleFieldClick(interaction.user, args[0].toInt(), args[1].toInt())
                        interaction.updatePublicMessage {
                            game.toMessage()
                        }
                        if (game.finished) {
                            games.remove(game.playerX.id)
                            games.remove(game.playerO.id)
                        }
                    }
                }
            }
        }
    }

    class Game(val playerX: User, val playerO: User) {
        private var currentPlayer: User = playerX
        private val board = Array(3) { Array(3) { State.EMPTY } }
        var tie = false
            private set
        var winner: User? = null
            private set
        val finished: Boolean
            get() = tie || winner != null

        fun handleFieldClick(player: User, row: Int, col: Int) {
            if (player.id != currentPlayer.id) return
            if (board[row][col] != State.EMPTY) return
            val currentPlayerSymbol = if (currentPlayer === playerX) State.X else State.O
            board[row][col] = currentPlayerSymbol
            var hasWon = true
            for (i in 0..2) {
                if (board[i][col] != currentPlayerSymbol) {
                    hasWon = false
                    break
                }
            }
            if (!hasWon) {
                hasWon = true
                for (i in 0..2) {
                    if (board[row][i] != currentPlayerSymbol) {
                        hasWon = false
                        break
                    }
                }
                if (!hasWon && row == col) {
                    hasWon = true
                    for (i in 0..2) {
                        if (board[i][i] != currentPlayerSymbol) {
                            hasWon = false
                            break
                        }
                    }
                }
                if (!hasWon && row + col == 2) {
                    hasWon = true
                    for (i in 0..2) {
                        if (board[i][2 - i] != currentPlayerSymbol) {
                            hasWon = false
                            break
                        }
                    }
                }
            }
            if (hasWon) {
                winner = currentPlayer
            } else if (board.all { it.all { it != State.EMPTY } }) {
                tie = true
            } else {
                currentPlayer = if (currentPlayer === playerX) playerO else playerX
            }
        }

        context(UpdateMessageInteractionResponseCreateBuilder)
        fun toMessage() {
            val title = "Tic Tac Toe: ${playerX.mention}(\"X\") vs ${playerO.mention}(\"O\")"
            val winner = winner
            content = if (tie) {
                "$title\nTie"
            } else if (winner != null) {
                "$title\nWinner: ${winner.mention}"
            } else {
                "$title\nCurrent Player: ${currentPlayer.mention}"
            }
            for (i in 0..2) {
                actionRow {
                    for (j in 0..2) {
                        interactionButton(ButtonStyle.Primary, customId("field", i.toString(), j.toString())) {
                            label = board[i][j].toString()
                            disabled = board[i][j] != State.EMPTY || finished
                        }
                    }
                }
            }
        }

        enum class State(private val string: String) {
            EMPTY("-"), X("X"), O("O");

            override fun toString(): String {
                return string
            }
        }
    }
}
