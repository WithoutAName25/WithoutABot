package withoutabot.styles

import csstype.Cursor
import emotion.css.css
import kotlinx.js.jso

object Style {

    val pointer = css(jso {
        cursor = Cursor.pointer
    })
}