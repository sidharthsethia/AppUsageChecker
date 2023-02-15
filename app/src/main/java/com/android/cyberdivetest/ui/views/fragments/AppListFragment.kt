package com.android.cyberdivetest.ui.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.cyberdivetest.R
import com.android.cyberdivetest.adapters.AppListAdapter
import com.android.cyberdivetest.data.AppListItem
import com.android.cyberdivetest.databinding.FragmentAppListBinding
import com.android.cyberdivetest.ui.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Created by Sidharth Sethia on 14/02/23.
 */

@AndroidEntryPoint
class AppListFragment: Fragment() {

    private lateinit var  binding: FragmentAppListBinding
    private val viewModel: MainActivityViewModel by activityViewModels()
    private val adapter: AppListAdapter by lazy { AppListAdapter() }

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
        binding.recyclerView.adapter = adapter
        adapter.setOnItemClickListener {
            viewModel.onItemSelected(it)
        }
        lifecycleScope.launch {
            viewModel.appListStateFlow
                .flowWithLifecycle(lifecycle)
                .onEach { adapter.setData(it) }
                .launchIn(this)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.update()
    }
}