package rougelite.engine

data class Prototype (
    var movable: Boolean = false,
    var passable: Boolean = true,
    var graphics: String? = null,
    var width: Int,
    var height: Int,
    var actionMap: HashMap<String, String> = HashMap()
)
