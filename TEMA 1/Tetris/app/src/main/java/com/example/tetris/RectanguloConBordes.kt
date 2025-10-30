package com.example.tetris

import android.graphics.Color

class RectanguloConBordes(color: Int, ancho:Int, alto:Int, var bordeColor:Int= Color.BLACK):
        Rectangulo(color, ancho, alto){

        fun cambiarColorBorde(colorBorde: Int){
            bordeColor = colorBorde;
        }
}