package eu.withoutaname.withoutabot.backend.bot.commands

import eu.withoutaname.withoutabot.backend.bot.Description
import eu.withoutaname.withoutabot.backend.bot.Name

@Name("test")
@Description("Only for test purposes")
data class Test(
    @Description("Any number that is greater than 0")
    val number: Int
)