package com.defey.labpuzzles.extensions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

@Serializable
sealed class AnyText {

    @Serializable
    @SerialName("PlainText")
    data class Plain(val text: String) : AnyText()

    @Serializable
    @SerialName("ResourceText")
    data class Resource(val resId: StringResource) : AnyText()

    @Serializable
    @SerialName("ResourceWithArgs")
    data class ResourceWithArgs(val resId: StringResource, val args: List<String>) : AnyText()

    companion object {
        fun from(string: String): AnyText = Plain(string)
        fun from(resId: StringResource): AnyText = Resource(resId)
        fun from(resId: StringResource, vararg args: String): AnyText =
            ResourceWithArgs(resId, args.toList())
    }
}

fun String.toAnyText(): AnyText = AnyText.Plain(this)
fun StringResource.toAnyText(): AnyText = AnyText.Resource(this)
fun StringResource.toAnyText(vararg args: String): AnyText = AnyText.ResourceWithArgs(this, args.toList())