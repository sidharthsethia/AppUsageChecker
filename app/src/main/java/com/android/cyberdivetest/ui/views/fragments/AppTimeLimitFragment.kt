package com.android.cyberdivetest.ui.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.cyberdivetest.R
import com.android.cyberdivetest.data.ActionState
import com.android.cyberdivetest.data.AppTimeLimitItem
import com.android.cyberdivetest.databinding.FragmentAppTimeLimitBinding
import com.android.cyberdivetest.ui.viewmodels.AppTimeLimitViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Created by Sidharth Sethia on 14/02/23.
 */

@AndroidEntryPoint
class AppTimeLimitFragment: Fragment() {
    private lateinit var  binding: FragmentAppTimeLimitBinding
    private val viewModel: AppTimeLimitViewModel by viewModels()

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
        setupSpinner()
        lifecycleScope.launch {
            viewModel.appTimeLimitItemFlow
                .flowWithLifecycle(lifecycle).onEach { updateUI(it) }
                .launchIn(this)
            viewModel.deleteFlow
                .flowWithLifecycle(lifecycle).onEach { updateDeleteButtonVisibility(it) }
                .launchIn(this)
            viewModel.actionStateFlow
                .flowWithLifecycle(lifecycle).onEach { updateState(it) }
                .launchIn(this)
        }
        binding.btnSave.setOnClickListener { viewModel.saveTimeLimit() }
        binding.btnDelete.setOnClickListener { viewModel.deleteTimeLimit() }
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            viewModel.timeDurations
        )
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.selectedPosition = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        binding.spinner.adapter = adapter
    }

    private fun updateState(state: ActionState) {
        when(state) {
            ActionState.AppTimeLimitDeleteSuccess -> navigateToAppListView()
            ActionState.AppTimeLimitInsertSuccess -> navigateToAppListView()
            is ActionState.Failure -> showToast(state.error)
            ActionState.Idle, ActionState.Loading -> Unit
        }
        updateProgressVisibility(state == ActionState.Loading)
    }

    private fun navigateToAppListView() {
        findNavController().navigate(AppTimeLimitFragmentDirections.actionAppTimeLimitFragmentToAppListFragment())
    }

    private fun updateProgressVisibility(show: Boolean) {
        binding.progressCircular.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateDeleteButtonVisibility(show: Boolean) {
        binding.btnDelete.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun updateUI(item: AppTimeLimitItem) {
        binding.apply {
            tvPageTitle.text = item.appName
            spinner.setSelection(viewModel.selectedPosition)
            tvTimeUsedValue.text = getString(R.string.app_time_in_mins, item.timeUsedInMin)
            ivIcon.setImageDrawable(requireContext().packageManager.getApplicationIcon(item.packageName))
        }
    }
}