package eu.withoutaname.discordbots.withoutabot.bot

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.entity.interaction.int
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.int
import dev.kord.rest.builder.message.create.embed
import eu.withoutaname.discordbots.withoutabot.config.Config
import kotlinx.coroutines.coroutineScope
import java.util.*

object Bot {

    private lateinit var kord: Kord
    private lateinit var activityTimer: Timer

    suspend fun start(config: Config.BotConfig) {
        coroutineScope {
            kord = Kord(config.token.get())

            kord.createGuildApplicationCommands(Snowflake(824744192202768394)) {
                input("test", "Hi") {
                    int("value", "Some number") {
                        required = true
                    }
                }
            }

            kord.on<ChatInputCommandInteractionCreateEvent> {
                println(interaction.command.rootName)
                interaction.respondEphemeral {
                    embed {
                        description = interaction.command.options["value"]?.int()?.toString() ?: "Test"
                    }
                }
            }

//            kord.editPresence {
//                this.playing("Test")
//            }

//            jda =
//                JDABuilder
//                    .createDefault(config.token.get())
//                    .addEventListeners(*listeners)
//                    .build()
//            setupActivities(config)
//            jda.awaitReady()
//            jda.updateCommands()
//                .addCommands(
//                    CommandData("info", "Shows all commands and a link to this page"),
//                    CommandData("mods", "Shows all mods from me (WithoutAName)"),
//                    CommandData("ping", "Try it out!"),
//                    CommandData(
//                        "roleselection",
//                        "[Admin] Adds an emote to a message where everyone gets a specific role by clicking on the emote"
//                    )
//                        .addOption(OptionType.INTEGER, "messageid", "MessageId to add role selection", true)
//                ).queue()



            println("Bot online")
            kord.login()
        }
    }

    suspend fun shutdown() {
        if (this::kord.isInitialized) kord.shutdown()
        if (this::activityTimer.isInitialized) activityTimer.cancel()
        println("Bot offline")
    }

//    private fun setupActivities(config: Config.BotConfig) {
//        fun setActivity(activity: ActivityConfig) {
//            jda.presence.activity = Activity.of(activity.type, activity.name)
//        }
//
//        val activities = config.statusMessages.get()
//        if (activities.size == 1) {
//            setActivity(activities[0])
//        } else if (activities.size > 1) {
//            activityTimer = Timer()
//            activityTimer.scheduleAtFixedRate(object : TimerTask() {
//                private var i = 0
//                override fun run() {
//                    if (i >= activities.size) {
//                        i = 0
//                    }
//                    setActivity(activities[i++])
//                }
//            }, 0, config.statusMessagesDelay.get())
//        }
//    }

}