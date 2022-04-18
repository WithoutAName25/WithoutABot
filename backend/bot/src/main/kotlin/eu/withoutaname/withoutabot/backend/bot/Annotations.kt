package eu.withoutaname.withoutabot.backend.bot

@Target(AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER)
annotation class Name(val value: String)

@Target(AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER)
annotation class Description(val value: String)
