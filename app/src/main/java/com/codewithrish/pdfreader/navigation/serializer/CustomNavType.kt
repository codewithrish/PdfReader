package com.codewithrish.pdfreader.navigation.serializer

import android.os.Bundle
import androidx.navigation.NavType
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

inline fun <reified T> genericNavType(): NavType<T> {
    return GenericNavType(object : TypeToken<T>() {})
}

class GenericNavType<T>(private val typeToken: TypeToken<T>) : NavType<T>(isNullableAllowed = true) {

    override val name: String
        get() = "GenericNavType<${typeToken.type}>"

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, Gson().toJson(value))
    }

    override fun get(bundle: Bundle, key: String): T? {
        return bundle.getString(key)?.let { json ->
            Gson().fromJson(json, typeToken.type)
        }
    }

    override fun parseValue(value: String): T {
        return Gson().fromJson(value, typeToken.type)
    }
}