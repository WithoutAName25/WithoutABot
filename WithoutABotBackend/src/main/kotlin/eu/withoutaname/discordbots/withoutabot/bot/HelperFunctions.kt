package eu.withoutaname.discordbots.withoutabot.bot

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.interactions.components.Button
import java.awt.Color

val DEFAULT_COLOR = Color(0, 100, 255)

fun getDeleteButton(): Button = Button.success("delete", "Ok")
fun getDeleteButton(user: User): Button = Button.success("delete_" + user.id, "Ok")

fun getMessageEmbed(description: String, color: Color = DEFAULT_COLOR): MessageEmbed =
	EmbedBuilder().setColor(color).setDescription(description).build()