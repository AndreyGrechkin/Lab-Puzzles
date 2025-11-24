package com.defey.labpuzzles.extensions

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AnyText.asString(): String {
    return when (this) {
        is AnyText.Plain -> text
        is AnyText.Resource -> stringResource(resId)
        is AnyText.ResourceWithArgs -> {
            formatStringResource(resId, args)
        }
    }
}

@Composable
private fun formatStringResource(resId: StringResource, args: List<String>): String {
    val baseString = stringResource(resId)
    return if (args.isNotEmpty()) {
        baseString.replacePlaceholders(args)
    } else {
        baseString
    }
}

private fun String.replacePlaceholders(args: List<String>): String {
    var result = this
    args.forEachIndexed { index, arg ->
        result = result.replace("%${index + 1}\$s", arg)
        result = result.replace("%s", arg)
    }
    return result
}