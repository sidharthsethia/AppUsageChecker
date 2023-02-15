package com.android.cyberdivetest.ui.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.cyberdivetest.R
import com.android.cyberdivetest.databinding.FragmentAppListBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Sidharth Sethia on 14/02/23.
 */

@AndroidEntryPoint
class AppListFragment: Fragment() {
    private lateinit var  binding: FragmentAppListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSave.setOnClickListener {
            findNavController().navigate(R.id.action_appListFragment_to_appTimeLimitFragment)
        }
    }
}