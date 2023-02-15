package com.android.cyberdivetest.ui.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.cyberdivetest.R
import com.android.cyberdivetest.databinding.FragmentAppTimeLimitBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Sidharth Sethia on 14/02/23.
 */

@AndroidEntryPoint
class AppTimeLimitFragment: Fragment() {
    private lateinit var  binding: FragmentAppTimeLimitBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppTimeLimitBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSave.setOnClickListener {
            findNavController().navigate(R.id.action_appTimeLimitFragment_to_appListFragment)
        }
    }
}