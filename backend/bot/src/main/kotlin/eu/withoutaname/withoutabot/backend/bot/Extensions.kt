package eu.withoutaname.withoutabot.backend.bot

import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

inline fun <reified T : Any> List<Any>.find(): T? {
    return find { it is T } as T?
}

val KClass<*>.changedName: String
    get() = annotations.find<Name>()?.value ?: simpleName
    ?: throw IllegalArgumentException("Class has no name!")

val KParameter.changedName: String
    get() = annotations.find<Name>()?.value ?: name ?: "No description provided"

val KAnnotatedElement.description: String
    get() = annotations.find<Description>()?.value ?: "No description provided"

fun Long.toIntIfPossible(): Int {
    if (Int.MIN_VALUE <= this && this <= Int.MAX_VALUE) return this.toInt()
    else throw IllegalStateException("Could not convert long to Int")
}