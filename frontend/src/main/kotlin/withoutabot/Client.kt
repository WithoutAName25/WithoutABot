package withoutabot

import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import react.create
import react.dom.client.createRoot

val client = HttpClient(Js) {
    install(Resources)
    install(ContentNegotiation) {
        json()
    }
    defaultRequest {
        url(js("API_URL").unsafeCast<String>())
    }
}

val mainScope = MainScope()

fun start() {
    val container = document.getElementById("app")!!
    val root = createRoot(container)
    root.render(Application.create())
}