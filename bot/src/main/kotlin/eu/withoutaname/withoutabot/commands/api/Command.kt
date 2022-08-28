package eu.withoutaname.withoutabot.commands.api

import dev.kord.core.Kord
import dev.kord.core.entity.Attachment
import dev.kord.core.entity.Entity
import dev.kord.core.entity.Role
import dev.kord.core.entity.User
import dev.kord.core.entity.channel.ResolvedChannel
import dev.kord.core.entity.interaction.InteractionCommand
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.interaction.*
import eu.withoutaname.withoutabot.ConfigContext
import eu.withoutaname.withoutabot.changedName
import eu.withoutaname.withoutabot.description
import eu.withoutaname.withoutabot.toIntIfPossible
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KType

class Command<T : Any>(
    private val kClass: KClass<T>,
    private val block: suspend ChatInputCommandInteractionCreateEvent.(T) -> Unit
) {

    val name = kClass.changedName
    private val description = kClass.description
    private val params = kClass.constructors.first().parameters.map { Parameter(it) }

    context(ConfigContext) suspend fun Kord.registerCommand() {
        createChatInputCommand(name, description) {
            params.forEach { with(it) { registerParameter() } }
        }
    }

    suspend fun ChatInputCommandInteractionCreateEvent.handleCommand() {
        val args = params.mapNotNull { with(it) { interaction.command.receiveValue() } }.toMap()
        block(kClass.constructors.first().callBy(args))
    }

    class Parameter(private val kParameter: KParameter) {

        val name = kParameter.changedName
        val description = kParameter.description
        val required = kParameter.isOptional.not() && kParameter.type.isMarkedNullable.not()
        private val type = Type.of(kParameter.type)

        fun ChatInputCreateBuilder.registerParameter() = with(this@Parameter.type) { register(this@Parameter) }

        fun InteractionCommand.receiveValue(): Pair<KParameter, Any?>? {
            val value = with(type) { receiveValue(this@Parameter) }
            return if (value != null || kParameter.type.isMarkedNullable) kParameter to value else null
        }

        enum class Type(
            val register: ChatInputCreateBuilder.(Parameter) -> Unit,
            val receiveValue: InteractionCommand.(Parameter) -> Any?
        ) {

            STRING({ string(it.name, it.description) { required = it.required } }, { strings[it.name] }),
            INT({ int(it.name, it.description) { required = it.required } }, { integers[it.name]?.toIntIfPossible() }),
            LONG({ int(it.name, it.description) { required = it.required } }, { integers[it.name] }),
            DOUBLE({ number(it.name, it.description) { required = it.required } }, { numbers[it.name] }),
            BOOLEAN({ boolean(it.name, it.description) { required = it.required } }, { booleans[it.name] }),
            USER({ user(it.name, it.description) { required = it.required } }, { users[it.name] }),
            CHANNEL({ channel(it.name, it.description) { required = it.required } }, { channels[it.name] }),
            ROLE({ role(it.name, it.description) { required = it.required } }, { roles[it.name] }),
            ENTITY({ mentionable(it.name, it.description) { required = it.required } }, { mentionables[it.name] }),
            ATTACHMENT({ attachment(it.name, it.description) { required = it.required } }, { attachments[it.name] });

            companion object {

                fun of(kType: KType): Type {
                    val classifier = kType.classifier
                    if (classifier !is KClass<*>) {
                        throw UnsupportedOperationException("Type is not a supported command parameter type!")
                    }
                    return when (classifier) {
                        String::class -> STRING
                        Int::class -> INT
                        Long::class -> LONG
                        Double::class -> DOUBLE
                        Boolean::class -> BOOLEAN
                        User::class -> USER
                        ResolvedChannel::class -> CHANNEL
                        Role::class -> ROLE
                        Entity::class -> ENTITY
                        Attachment::class -> ATTACHMENT
                        else -> throw UnsupportedOperationException("${classifier.simpleName} is not a supported command parameter type!")
                    }
                }
            }
        }
    }
}