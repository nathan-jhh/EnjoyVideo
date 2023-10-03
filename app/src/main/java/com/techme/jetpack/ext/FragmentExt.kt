package com.techme.jetpack.ext

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <reified VB : ViewBinding> invokeViewBinding() =
    FragmentInflateBindingProperty(VB::class.java)

class FragmentInflateBindingProperty<VB : ViewBinding>(private val clazz: Class<VB>) :
    ReadOnlyProperty<Any, VB> {
    private var binding: VB? = null
    override fun getValue(thisRef: Any, property: KProperty<*>): VB {
        val layoutInflater: LayoutInflater?
        val viewLifecycleOwner: LifecycleOwner?
        when (thisRef) {
            is AppCompatActivity -> {
                layoutInflater = thisRef.layoutInflater
                viewLifecycleOwner = thisRef
            }
            is Fragment -> {
                layoutInflater = thisRef.layoutInflater
                viewLifecycleOwner = thisRef.viewLifecycleOwner
            }

            else -> {
                throw java.lang.IllegalStateException("invokeViewBinding can only be used in AppCompatActivity or Fragment")
            }
        }
        if (binding == null) {
            try {
                binding = (clazz.getMethod("inflate", LayoutInflater::class.java)
                    .invoke(null, layoutInflater) as VB)
            } catch (e: java.lang.IllegalStateException) {
                e.printStackTrace()
                throw e
            }
            viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    super.onDestroy(owner)
                    binding = null
                }
            })
        }
        return binding!!
    }
}

inline fun <reified VM : ViewModel> invokeViewModel() = FragmentViewModelProperty(VM::class.java)

class FragmentViewModelProperty<VM : ViewModel>(private val clazz: Class<VM>) :
    ReadOnlyProperty<Any, VM> {
    private var vm: VM? = null
    override fun getValue(thisRef: Any, property: KProperty<*>): VM {
        if (thisRef !is ViewModelStoreOwner) {
            throw java.lang.IllegalStateException("invokeViewModel can only be used in ViewModelStoreOwner instance")
        }
        if (vm == null) {
            vm = ViewModelProvider(thisRef, ViewModelProvider.NewInstanceFactory())[clazz]
        }
        return vm!!
    }
}




