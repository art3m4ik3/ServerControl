package com.art3m4ik3.servercontrol.classes

import java.io.Serializable


data class Server(
    val ip: String,
    val username: String,
    val password: String,
    val port: Int
) : Serializable