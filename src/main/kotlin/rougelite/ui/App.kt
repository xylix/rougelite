package rougelite.ui

import javafx.application.Application
import javafx.stage.Stage
import rougelite.utils.FileOperations

class App : Application() {
    override fun start(primaryStage: Stage) {
        val l = FileOperations.loadLevel("1")
        primaryStage.scene = l.scene
        primaryStage.show()
    }

    companion object {
        const val SCALE = 24
        @JvmStatic
        fun main(args: Array<String>) {
            launch(App::class.java)
        }
    }
}
