package com.katoklizm.playlist_maker_full.util

sealed interface Resource<T>{
    class Success<T>(val data: T?): Resource<T>
    class Error<T>(val message: String?, val data: T? = null): Resource<T>
}