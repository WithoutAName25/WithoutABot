package eu.withoutaname.withoutabot.backend.bot.commands

import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.rest.builder.message.create.embed
import eu.withoutaname.withoutabot.backend.bot.Description
import eu.withoutaname.withoutabot.backend.bot.Name
import eu.withoutaname.withoutabot.backend.bot.commands.api.Commands

@Name("test")
@Description("Only for test purposes")
data class Test(

    @Description("Any number that is greater than 0")
    val number: Int,

    @Name("custom")
    @Description("Optional test parameter")
    val s: String = "Test"
)

fun Commands.testCommand() {
    on<Test> {
        interaction.respondEphemeral {
            embed {
                if (it.number <= 0) {
                    description = "Number must be greater than 0"
                } else if (it.number > 10) {
                    description = "${it.number} is to big. Number should be smaller or equal than 10"
                } else {
                    description = ""
                    for (i in 1..it.number) {
                        if (i != 1) description += "; "
                        description += i
                    }
                }
            }
            embed {
                description = it.s
            }
        }
    }
}