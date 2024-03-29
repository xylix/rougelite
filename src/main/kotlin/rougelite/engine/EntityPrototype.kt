package rougelite.engine

data class EntityPrototype (
    var movable: Boolean = false,
    var passable: Boolean = true,
    var graphics: String? = null,
    var width: Int,
    var height: Int,
    var actionMap: HashMap<String, String> = HashMap()
)
