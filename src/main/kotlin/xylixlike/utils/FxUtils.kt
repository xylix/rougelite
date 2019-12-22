package xylixlike.utils

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button

fun createButton(name: String, action: EventHandler<ActionEvent>): Button {
    val button = Button(name)
    button.onAction = action
    button.id = name
    return button
}
