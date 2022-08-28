package eu.withoutaname.withoutabot.commands

import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.rest.builder.message.create.embed
import eu.withoutaname.withoutabot.Description
import eu.withoutaname.withoutabot.Name
import eu.withoutaname.withoutabot.commands.api.Commands

@Name("count")
@Description("Only for test purposes")
data class Test(

    @Description("Any number that is greater than 0")
    val number: Int,

    @Name("custom")
    @Description("Optional test parameter")
    val s: String?,

    @Name("with-default")
    @Description("Optional test parameter with default value")
    val s2: String = "default"
)

fun Commands.countCommand() {
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
            if (it.s != null) {
                embed {
                    description = it.s
                }
            }
            embed {
                description = it.s2
            }
        }
    }
}