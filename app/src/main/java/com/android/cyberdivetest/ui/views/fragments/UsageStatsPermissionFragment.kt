package com.android.cyberdivetest.ui.views.fragments

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.android.cyberdivetest.R
import com.android.cyberdivetest.databinding.FragmentUsageStatsPermissionBinding
import com.android.cyberdivetest.helpers.UsageStatsPermissionChecker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by Sidharth Sethia on 15/02/23.
 */

@AndroidEntryPoint
class UsageStatsPermissionFragment: Fragment() {

    @Inject
    lateinit var permissionChecker: UsageStatsPermissionChecker
    private lateinit var  binding: FragmentUsageStatsPermissionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsageStatsPermissionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnContinue.setOnClickListener {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (permissionChecker.hasPermission()) {
            findNavController().navigate(R.id.action_usageStatsPermissionFragment_to_appListFragment)
        }
    }
}