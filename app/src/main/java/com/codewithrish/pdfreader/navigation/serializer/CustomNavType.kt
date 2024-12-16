package com.codewithrish.pdfreader.navigation.serializer


import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.BundleCompat
import androidx.navigation.NavType
import com.google.gson.Gson
import com.google.gson.GsonBuilder

inline fun <reified T : Parcelable> genericNavType(
    isNullableAllowed: Boolean = false,
    gson: Gson = GsonBuilder().create()
) = object : NavType<T>(isNullableAllowed) {

    override fun get(bundle: Bundle, key: String): T? = BundleCompat.getParcelable(bundle, key, T::class.java)

    override fun put(bundle: Bundle, key: String, value: T) = bundle.putParcelable(key, value)

    override fun serializeAsValue(value: T): String = Uri.encode(gson.toJson(value))

    override fun parseValue(value: String): T = gson.fromJson(value, T::class.java)
}

inline fun <reified T : Enum<T>> enumNavType(): NavType<T> = object : NavType<T>(false) {
    override fun get(bundle: Bundle, key: String): T? {
        val value = bundle.getString(key)
        return value?.let { enumValueOf<T>(it) }
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, value.name)
    }

    override fun parseValue(value: String): T {
        return enumValueOf(value)
    }

    override fun serializeAsValue(value: T): String {
        return value.name
    }
}
