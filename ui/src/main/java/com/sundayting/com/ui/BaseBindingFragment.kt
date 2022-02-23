package com.sundayting.com.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**
 * 自动完成ViewBinding的基类
 */
abstract class BaseBindingFragment<T : ViewBinding> : BaseFragment() {

    private var _binding: T? = null
    protected val binding get() = checkNotNull(_binding) { "初始化binding失败" }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            (((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>).getMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            ).invoke(null, inflater, container, false) as T)

        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                _binding = null
            }
        })
        return binding.root
    }

}