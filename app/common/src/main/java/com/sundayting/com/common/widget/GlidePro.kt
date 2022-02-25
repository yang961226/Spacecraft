package com.sundayting.com.common.widget

import android.util.ArrayMap
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.manager.LifecycleListener
import com.bumptech.glide.manager.RequestManagerTreeNode
import com.bumptech.glide.util.Util
import java.util.*


/**
 * 让Glide适配navigation，源码来自https://juejin.cn/post/7002798538484613127
 */
class GlidePro {

    companion object {

        private val lifecycleMap = ArrayMap<LifecycleOwner, RequestManager>()

        @MainThread
        fun withViewLifecycleOwner(fragment: Fragment): RequestManager {
            Util.assertMainThread()

            fragment.viewLifecycleOwner.let { lifecycleOwner ->
                if (lifecycleOwner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
                    throw IllegalStateException("View 已被销毁")
                }
                if (lifecycleMap[lifecycleOwner] == null) {
                    fragment.requireContext().applicationContext.let { application ->
                        lifecycleMap[lifecycleOwner] = RequestManager(
                            Glide.get(application),
                            GlideNavigationLifecycle(lifecycleOwner.lifecycle),
                            KEmptyRequestManagerTreeNode(),
                            application
                        )
                    }

                }
                return lifecycleMap[lifecycleOwner]!!
            }
        }

    }

    class KEmptyRequestManagerTreeNode : RequestManagerTreeNode {
        override fun getDescendants(): Set<RequestManager> {
            return emptySet()
        }
    }

    class GlideNavigationLifecycle(
        private val lifecycle: Lifecycle
    ) : com.bumptech.glide.manager.Lifecycle {
        private val lifecycleListeners =
            Collections.newSetFromMap(WeakHashMap<LifecycleListener, Boolean>())

        private val lifecycleObserver = object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                val listeners = Util.getSnapshot(lifecycleListeners)
                for (listener in listeners) {
                    listener.onStart()
                }
            }

            override fun onStop(owner: LifecycleOwner) {
                val listeners = Util.getSnapshot(lifecycleListeners)
                for (listener in listeners) {
                    listener.onStop()
                }
            }

            override fun onDestroy(owner: LifecycleOwner) {
                val listeners = Util.getSnapshot(lifecycleListeners)
                for (listener in listeners) {
                    listener.onDestroy()
                }

                lifecycleMap.remove(owner)
                lifecycleListeners.clear()
                lifecycle.removeObserver(this)
            }
        }

        init {
            lifecycle.addObserver(lifecycleObserver)
        }

        override fun addListener(listener: LifecycleListener) {
            lifecycleListeners.add(listener)
            when (lifecycle.currentState) {
                Lifecycle.State.STARTED, Lifecycle.State.RESUMED -> listener.onStart()
                Lifecycle.State.DESTROYED -> listener.onDestroy()
                else -> listener.onStop()
            }
        }

        override fun removeListener(listener: LifecycleListener) {
            lifecycleListeners.remove(listener)
        }
    }

}