package withoutabot

import react.FC
import react.Props
import react.createElement
import react.router.Route
import react.router.Routes
import react.router.dom.BrowserRouter
import withoutabot.pages.Index
import withoutabot.pages.InfoCommands

val Application = FC<Props> {
    BrowserRouter {
        Routes {
            Route {
                index = true
                element = createElement(Index)
            }

            Route {
                path = "/info-commands"
                element = createElement(InfoCommands)
            }
        }
    }
}