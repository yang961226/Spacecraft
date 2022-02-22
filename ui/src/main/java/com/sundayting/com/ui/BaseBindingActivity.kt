package com.sundayting.com.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.lang.reflect.ParameterizedType

/**
 * 自动完成Binding的基类
 */
abstract class BaseBindingActivity<T : ViewDataBinding> : BaseActivity() {

    private var _binding: T? = null
    protected val binding: T get() = checkNotNull(_binding) { "初始化binding失败" }

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding =
            ((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>).getMethod(
                "inflate",
                LayoutInflater::class.java
            ).invoke(null, layoutInflater) as T
        setContentView(binding.root)
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                _binding = null
            }
        })
    }

}