package site.whiteoffice.todoist.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class AppLifeCycleListener (
    val onForeground: ()->Unit,
    val onBackground:()->Unit
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() { // app moved to foreground
        println("onMoveToForeground")
        onForeground()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onMoveToBackground() { // app moved to background
        println("onMoveToBackground")
        onBackground()

    }


}