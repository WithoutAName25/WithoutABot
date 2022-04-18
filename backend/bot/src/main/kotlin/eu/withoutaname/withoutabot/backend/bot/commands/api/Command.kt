package eu.withoutaname.withoutabot.backend.bot.commands.api

import dev.kord.core.Kord
import dev.kord.core.entity.interaction.InteractionCommand
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder
import dev.kord.rest.builder.interaction.int
import eu.withoutaname.withoutabot.backend.bot.ConfigContext
import eu.withoutaname.withoutabot.backend.bot.changedName
import eu.withoutaname.withoutabot.backend.bot.description
import eu.withoutaname.withoutabot.backend.bot.toIntIfPossible
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
        val args = params.map { with(it) { interaction.command.receiveValue() } }.toTypedArray()
        block(kClass.constructors.first().call(*args))
    }

    class Parameter(kParameter: KParameter) {

        val name = kParameter.changedName
        val description = kParameter.description
        val required = kParameter.isOptional.not()
        private val type = Type.of(kParameter.type)

        fun ChatInputCreateBuilder.registerParameter() = with(this@Parameter.type) { register(this@Parameter) }

        fun InteractionCommand.receiveValue() = with(this@Parameter.type) { receiveValue(this@Parameter) }

        enum class Type(
            val register: ChatInputCreateBuilder.(Parameter) -> Unit,
            val receiveValue: InteractionCommand.(Parameter) -> Any
        ) {

            INT({ int(it.name, it.description) { required = it.required } }, { integers[it.name]!!.toIntIfPossible() });

            companion object {

                fun of(kType: KType): Type {
                    val classifier = kType.classifier
                    if (classifier !is KClass<*>) {
                        throw UnsupportedOperationException("Type is not a supported command parameter type!")
                    }
                    return when (classifier) {
                        Int::class -> INT
                        else -> throw UnsupportedOperationException("${classifier.simpleName} is not a supported command parameter type!")
                    }
                }
            }
        }
    }
}