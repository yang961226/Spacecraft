package com.sundayting.com.home.article

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sundayting.com.common.bean.ArticleBean
import com.sundayting.com.home.R
import com.sundayting.com.home.databinding.LayoutArticleBinding
import javax.inject.Inject

class ArticleAdapter @Inject constructor(
    itemCallback: DiffUtil.ItemCallback<ArticleBean>,
) : ListAdapter<ArticleBean, RecyclerView.ViewHolder>(itemCallback) {

    val onClickArticleItem: ((articleBean: ArticleBean) -> Unit)? = null

    class ArticleViewHolder(val binding: LayoutArticleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = LayoutArticleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position).let { articleBean ->
            if (holder is ArticleViewHolder) {
                holder.binding.run {
                    rlRoot.setOnClickListener {
                        onClickArticleItem?.invoke(articleBean)
                    }
                    tvTag.text = articleBean.topTitle.orEmpty()
                    tvAuthor.text = articleBean.author.orEmpty()
                    tvDate.text = articleBean.niceDate.orEmpty()
                    tvTitle.text = articleBean.title.orEmpty()
                    tvChapterName.text = articleBean.superChapterName.orEmpty()
                    if (articleBean.collect) {
                        ivCollect.setImageResource(R.drawable._collect)
                    } else {
                        ivCollect.setImageResource(R.drawable.un_collect)
                    }
                }
            }
        }
    }

}