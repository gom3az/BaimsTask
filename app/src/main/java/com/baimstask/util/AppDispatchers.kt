package com.baimstask.util

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val niaDispatcher: AppDispatchers)

enum class AppDispatchers {
    Default,
    IO,
}