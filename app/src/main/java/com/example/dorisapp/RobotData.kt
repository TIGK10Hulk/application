package com.example.dorisapp

class RobotData {
    companion object {
        var manualControl: Boolean = false
        var xPosition: Int = 0
        var yPosition: Int = 0
        var speed: Int = 0
        var unpushedCoords: ArrayList<Coord>? = ArrayList()
        var session: Int = 0
    }
}