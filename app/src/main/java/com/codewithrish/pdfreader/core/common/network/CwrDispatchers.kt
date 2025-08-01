package com.codewithrish.pdfreader.core.common.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val cwrDispatchers: CwrDispatchers)

enum class CwrDispatchers {
    Default,
    IO,
}
