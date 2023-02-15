package com.android.cyberdivetest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.cyberdivetest.R
import com.android.cyberdivetest.data.AppListItem
import com.android.cyberdivetest.databinding.ItemAppListBinding

/**
 * Created by Sidharth Sethia on 14/02/23.
 */
class AppListAdapter : RecyclerView.Adapter<AppListAdapter.AppListItemViewHolder>() {

    inner class AppListItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<AppListItem>() {
        override fun areItemsTheSame(oldItem: AppListItem, newItem: AppListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AppListItem, newItem: AppListItem): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppListItemViewHolder {
        return AppListItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_app_list,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((AppListItem) -> Unit)? = null

    override fun onBindViewHolder(holder: AppListItemViewHolder, position: Int) {
        val appInfo = differ.currentList[position]
        holder.itemView.apply {
            val binding = ItemAppListBinding.bind(this)
            binding.ivIcon.setImageDrawable(context.packageManager.getApplicationIcon(appInfo.id))
            binding.tvName.text = appInfo.name
            binding.tvDuration.text = context.getString(R.string.app_time_in_mins, appInfo.timeSpentInMin)
            setOnClickListener { onItemClickListener?.invoke(appInfo) }
        }
    }

    fun setOnItemClickListener(listener: (AppListItem) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(list: List<AppListItem>) {
        differ.submitList(list)
    }
}