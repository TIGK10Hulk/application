package com.example.dorisapp

class Coord {
    var xCoord: Int
    var yCoord: Int
    var isCollision: Boolean
    var session: Int

    constructor(xCoord: Int, yCoord: Int, isCollision: Boolean, session: Int){
        this.xCoord = xCoord
        this.yCoord = yCoord
        this.isCollision = isCollision
        this.session = session
    }
}