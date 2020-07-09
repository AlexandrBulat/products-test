package com.test.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.test.R
import com.test.databinding.LoadingFragmentBinding

class LoadingFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val viewBinding = DataBindingUtil.inflate<LoadingFragmentBinding>(
                inflater, R.layout.loading_fragment, container, false)
        isCancelable = false
        return viewBinding.root
    }

    override fun onResume() {
        super.onResume()
        val window = dialog?.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    companion object {
        fun newInstance(): LoadingFragment = LoadingFragment()
    }
}