package com.sundayting.com.mine.integral

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sundayting.com.common.bean.IntegralBean
import com.sundayting.com.mine.databinding.LayoutIntegralItemBinding
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class IntegralAdapter @Inject constructor(
    itemCallback: DiffUtil.ItemCallback<IntegralBean>,
) : ListAdapter<IntegralBean, RecyclerView.ViewHolder>(itemCallback) {

    companion object {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
    }

    class IntegralViewHolder(val binding: LayoutIntegralItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return IntegralViewHolder(
            LayoutIntegralItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            if (holder is IntegralViewHolder) {
                holder.binding.run {
                    tvAddIntegralMode.text = item.reason
                    tvDate.text = simpleDateFormat.format(Date(item.date))
                    tvAddIntegral.text = item.coinCount.toString()
                }
            }
        }
    }


}