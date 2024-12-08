package com.codewithrish.pdfreader.navigation.serializer

import com.codewithrish.pdfreader.core.model.home.Document
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type


import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.BundleCompat
import androidx.navigation.NavType
import com.google.gson.GsonBuilder

/**
 * Generic function for creating a Custom Nav Type
 */
inline fun <reified T : Parcelable> createCustomNavType(
    isNullableAllowed: Boolean = false,
) = object : NavType<T>(isNullableAllowed) {

    val gson = GsonBuilder()
        .registerTypeAdapter(Document::class.java, DocumentTypeAdapter())
        .create()

    override fun get(bundle: Bundle, key: String): T? {
        return BundleCompat.getParcelable(bundle, key, T::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putParcelable(key, value)
    }

    /**
     * Note that this method also needs to be overridden because the parent class has the following description:
     * This method can be override for custom serialization implementation on types such custom NavType classes.
     */
    override fun serializeAsValue(value: T): String {
        //return Json.encodeToString(T::class.serializer(), value)
        return Uri.encode(gson.toJson(value))
    }

    override fun parseValue(value: String): T {
        //return Json.decodeFromString(T::class.serializer(), value)
        return gson.fromJson(value, T::class.java)
    }
}

//inline fun <reified T> genericNavType(): NavType<T> {
//    return GenericNavType(object : TypeToken<T>() {})
//}
//
//class GenericNavType<T>(private val typeToken: TypeToken<T>) : NavType<T>(isNullableAllowed = true) {
//
//    val gson = GsonBuilder()
//        .registerTypeAdapter(Document::class.java, DocumentTypeAdapter())
//        .create()
//
//    override val name: String
//        get() = "GenericNavType<${typeToken.type}>"
//
//    override fun put(bundle: Bundle, key: String, value: T) {
//        bundle.putString(key, gson.toJson(value))
//    }
//
//    override fun get(bundle: Bundle, key: String): T? {
//        return bundle.getString(key)?.let { json ->
//            gson.fromJson(json, typeToken.type)
//        }
//    }
//
//    override fun parseValue(value: String): T {
//        return gson.fromJson(value, typeToken.type)
//    }
//}

class DocumentTypeAdapter : JsonSerializer<Document>, JsonDeserializer<Document> {

    override fun serialize(
        src: Document?,
        typeOfSrc: Type?,
        context: JsonSerializationContext
    ): JsonElement {
        if (src == null) {
            return JsonNull.INSTANCE
        }

        return JsonObject().apply {
            addProperty("id", src.id)
            addProperty("path", src.path)
            addProperty("uri", src.uri)
            addProperty("name", src.name)
            addProperty("dateTime", src.dateTime)
            addProperty("mimeType", src.mimeType)
            addProperty("size", src.size)
            addProperty("bookmarked", src.bookmarked)
        }
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext
    ): Document {
        val jsonObject = json?.asJsonObject ?: throw JsonParseException("Invalid JSON for Document")

        return Document(
            id = jsonObject.get("id").asLong,
            path = jsonObject.get("path").asString,
            uri = jsonObject.get("uri").asString,
            name = jsonObject.get("name").asString,
            dateTime = jsonObject.get("dateTime").asLong,
            mimeType = jsonObject.get("mimeType").asString,
            size = jsonObject.get("size").asLong,
            bookmarked = jsonObject.get("bookmarked").asBoolean
        )
    }
}
