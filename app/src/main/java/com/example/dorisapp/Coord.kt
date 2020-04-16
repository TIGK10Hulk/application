package com.example.dorisapp

class Coord {
    var xCoord: Int?
    var yCoord: Int?
    var isCollision: Boolean?


    constructor(xCoord: Int, yCoord: Int, isCollision: Boolean){
        this.xCoord = xCoord
        this.yCoord = yCoord
        this.isCollision = isCollision
    }
}