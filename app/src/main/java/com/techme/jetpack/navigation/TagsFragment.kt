package com.techme.jetpack.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.techme.jetpack.plugin.runtime.NavDestination

import com.techme.jetpack.databinding.LayoutFragmentTagsBinding
import com.techme.jetpack.ext.navigateBack

@NavDestination(type = NavDestination.NavType.Fragment, route = "tags_fragment")
class TagsFragment : BaseFragment() {
    lateinit var tagsBinding: LayoutFragmentTagsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("fragmentlife", "TagsFragment onCreate:$savedInstanceState")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tagsBinding = LayoutFragmentTagsBinding.inflate(inflater, container, false);
        return tagsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //homeFragment--categoryFragment---tagsFragment------userFragment
        //---------------------------------------------NavOptions
        tagsBinding.navigateToUserFragment.setOnClickListener {
            findNavController().navigateBack("home_fragment", inclusive = false, saveState = true)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("outState", "我是TagsFragment")
    }
}