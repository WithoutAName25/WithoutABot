package eu.withoutaname.withoutabot.interactions

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.Attachment
import dev.kord.core.entity.Entity
import dev.kord.core.entity.Role
import dev.kord.core.entity.User
import dev.kord.core.entity.channel.Channel
import dev.kord.core.entity.interaction.InteractionCommand
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.event.interaction.GlobalChatInputCommandInteractionCreateEvent
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.*
import eu.withoutaname.withoutabot.interactions.CommandParameterConfig.*
import kotlin.reflect.KFunction4
import kotlin.reflect.KProperty1

@DslMarker
annotation class InteractionDSL

@InteractionDSL
class InteractionsConfig {
    private val commands = mutableMapOf<Pair<Snowflake?, String>, CommandConfig>()

    fun commands() = commands.toMap()

    fun command(name: String, guild: Snowflake? = null, block: CommandConfig.() -> Unit) {
        if (commands.containsKey(guild to name)) {
            throw IllegalArgumentException(
                "Command '$name' already exists" + if (guild != null) " on guild with id $guild" else ""
            )
        }
        val config = CommandConfig(name, guild)
        config.block()
        commands[guild to name] = config
    }
}

@InteractionDSL
sealed class CommandParameterConfig<T, B : OptionsBuilder> {
    abstract val name: String
    var description = ""
    var required = true
    abstract val registerFunction: KFunction4<RootInputChatBuilder, String, String, B.() -> Unit, Unit>
    abstract val accessValueMap: KProperty1<InteractionCommand, Map<String, T>>

    class AttachmentParameter(override val name: String) : CommandParameterConfig<Attachment, AttachmentBuilder>() {
        override val registerFunction = RootInputChatBuilder::attachment
        override val accessValueMap = InteractionCommand::attachments
    }

    class BooleanParameter(override val name: String) : CommandParameterConfig<Boolean, BooleanBuilder>() {
        override val registerFunction = RootInputChatBuilder::boolean
        override val accessValueMap = InteractionCommand::booleans
    }

    class ChannelParameter(override val name: String) : CommandParameterConfig<Channel, ChannelBuilder>() {
        override val registerFunction = RootInputChatBuilder::channel
        override val accessValueMap = InteractionCommand::channels
    }

//    class GroupParameter(override val name: String) : CommandParameterConfig<User, UserBuilder>() {
//        override val registerFunction = RootInputChatBuilder::group
//        override val accessValueMap = InteractionCommand::
//    }

    class IntegerParameter(override val name: String) : CommandParameterConfig<Long, IntegerOptionBuilder>() {
        override val registerFunction = RootInputChatBuilder::integer
        override val accessValueMap = InteractionCommand::integers
    }

    class MentionableParameter(override val name: String) : CommandParameterConfig<Entity, MentionableBuilder>() {
        override val registerFunction = RootInputChatBuilder::mentionable
        override val accessValueMap = InteractionCommand::mentionables
    }

    class NumberParameter(override val name: String) : CommandParameterConfig<Double, NumberOptionBuilder>() {
        override val registerFunction = RootInputChatBuilder::number
        override val accessValueMap = InteractionCommand::numbers
    }

    class RoleParameter(override val name: String) : CommandParameterConfig<Role, RoleBuilder>() {
        override val registerFunction = RootInputChatBuilder::role
        override val accessValueMap = InteractionCommand::roles
    }

    class StringParameter(override val name: String) : CommandParameterConfig<String, StringChoiceBuilder>() {
        override val registerFunction = RootInputChatBuilder::string
        override val accessValueMap = InteractionCommand::strings
    }

//    class SubCommandParameter(override val name: String) : CommandParameterConfig<User, UserBuilder>() {
//        override val registerFunction = RootInputChatBuilder::subCommand
//        override val accessValueMap = InteractionCommand::
//    }

    class UserParameter(override val name: String) : CommandParameterConfig<User, UserBuilder>() {
        override val registerFunction = RootInputChatBuilder::user
        override val accessValueMap = InteractionCommand::users
    }
}

class CommandParameter<T, B : OptionsBuilder>(private val config: CommandParameterConfig<T, B>) {
    context(ChatInputCommandInteractionCreateEvent)
    fun getValue() =
        config.accessValueMap.get(this@ChatInputCommandInteractionCreateEvent.interaction.command)[config.name]
}

@InteractionDSL
class CommandConfig(val name: String, val guild: Snowflake?) {
    var description = ""
    private val parameters = mutableListOf<CommandParameterConfig<out Any, out OptionsBuilder>>()
    fun parameters() = parameters.toList()
    var action: suspend ChatInputCommandInteractionCreateEvent.() -> Unit = {}
        private set

    fun action(block: suspend ChatInputCommandInteractionCreateEvent.() -> Unit) {
        action = block
    }

    private inline fun <reified T : Any, B : OptionsBuilder> parameter(
        parameter: CommandParameterConfig<T, B>,
        block: CommandParameterConfig<T, B>.() -> Unit
    ): CommandParameter<T, B> {
        parameter.block()
        parameters.add(parameter)
        return CommandParameter(parameter)
    }

    fun attachment(name: String, block: CommandParameterConfig<Attachment, AttachmentBuilder>.() -> Unit) =
        parameter(AttachmentParameter(name), block)

    fun boolean(name: String, block: CommandParameterConfig<Boolean, BooleanBuilder>.() -> Unit) =
        parameter(BooleanParameter(name), block)

    fun channel(name: String, block: CommandParameterConfig<Channel, ChannelBuilder>.() -> Unit) =
        parameter(ChannelParameter(name), block)

    //    fun group(name: String, block: CommandParameterConfig<Attachment, AttachmentBuilder>.() -> Unit) =
//        parameter(AttachmentParameter(name), block)
    fun integer(name: String, block: CommandParameterConfig<Long, IntegerOptionBuilder>.() -> Unit) =
        parameter(IntegerParameter(name), block)

    fun mentionable(name: String, block: CommandParameterConfig<Entity, MentionableBuilder>.() -> Unit) =
        parameter(MentionableParameter(name), block)

    fun number(name: String, block: CommandParameterConfig<Double, NumberOptionBuilder>.() -> Unit) =
        parameter(NumberParameter(name), block)

    fun role(name: String, block: CommandParameterConfig<Role, RoleBuilder>.() -> Unit) =
        parameter(RoleParameter(name), block)

    fun string(name: String, block: CommandParameterConfig<String, StringChoiceBuilder>.() -> Unit) =
        parameter(StringParameter(name), block)

    //    fun subcommand(name: String, block: CommandParameterConfig<Attachment, AttachmentBuilder>.() -> Unit) =
//        parameter(AttachmentParameter(name), block)
    fun user(name: String, block: CommandParameterConfig<User, UserBuilder>.() -> Unit) =
        parameter(UserParameter(name), block)
}

suspend fun Kord.interactions(block: InteractionsConfig.() -> Unit) {
    InteractionsConfig().run {
        block()
        val commands = commands()
        commands.deleteNonExisting()
        commands.forEach { (_, command) ->
            command.configure()
        }

        on<ChatInputCommandInteractionCreateEvent> {
            val guildId = interaction.invokedCommandGuildId
            val name = interaction.command.rootName
            val command = commands[guildId to name] ?: commands[null to name]
            command?.action?.let { it() }
        }
    }
}

context(Kord)
private suspend fun Map<Pair<Snowflake?, String>, CommandConfig>.deleteNonExisting() {
    getGlobalApplicationCommands().collect {
        if (this[null to it.name] == null) it.delete()
    }

    this.keys.mapNotNull { it.first }.toSet().forEach { guild ->
        getGuildApplicationCommands(guild).collect {
            if (this[guild to it.name] == null) it.delete()
        }
    }
}

context(Kord)
private suspend fun CommandConfig.configure() {
    fun RootInputChatBuilder.configureCommand() {
        parameters().forEach {
            it.registerFunction(this, it.name, it.description) {
                required = it.required
            }
        }
    }
    if (guild == null) createGlobalChatInputCommand(name, description) {
        configureCommand()
    } else createGuildChatInputCommand(guild, name, description) {
        configureCommand()
    }
}
