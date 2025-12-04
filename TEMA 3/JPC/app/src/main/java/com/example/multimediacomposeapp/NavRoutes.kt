package com.example.multimediacomposeapp

sealed class NavRoutes (val route: String) {
    object Menu: NavRoutes("menu")
    object Audio: NavRoutes("audio")
    object Video: NavRoutes("video")
    object Camera: NavRoutes("camera")
    object Recorder : NavRoutes("recorder")
}