package com.example.tetris

open class Rectangulo(var color: Int, var ancho: Int, var alto: Int) {
    // Coordenadas iniciales.Propiedades
    var x = 0
    var y = 0

    // construimos (comportamientos)
    fun movArriba(){
        y-=50
    }
    fun movAbajo(){
        y+=50
    }
    fun movIzqda(){
        x-=50
    }
    fun movDcha(){
        x+=50
    }

    fun cambiarTamano(ancho: Int, alto:Int){
        this.ancho = ancho
        this.alto = alto
    }


}