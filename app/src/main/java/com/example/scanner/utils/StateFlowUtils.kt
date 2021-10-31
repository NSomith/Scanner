package com.example.scanner.utils

import kotlinx.coroutines.flow.MutableStateFlow
import java.lang.StringBuilder

inline fun <T> MutableStateFlow<T>.setState(action:T.()->T){
   this.value = this.value.action()
}


//fun build(action: StringBuilder.() -> Unit){
//    val s = StringBuilder().action()
//}

