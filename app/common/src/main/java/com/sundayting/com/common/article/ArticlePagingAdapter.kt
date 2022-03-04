package com.sundayting.com.common.article

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sundayting.com.common.R
import com.sundayting.com.common.bean.ArticleBean
import com.sundayting.com.common.databinding.LayoutArticleBinding
import javax.inject.Inject

class ArticlePagingAdapter @Inject constructor(
    itemCallback: DiffUtil.ItemCallback<ArticleBean>,
) : PagingDataAdapter<ArticleBean, ArticlePagingAdapter.ArticleViewHolder>(itemCallback) {

    enum class ClickAction {
        NORMAL_CLICK,
        COLLECT_CLICK,
    }

    var onClickArticleItem: ((articleBean: ArticleBean, clickAction: ClickAction) -> Unit)? = null

    class ArticleViewHolder(val binding: LayoutArticleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        getItem(position)?.let { articleBean ->
            holder.binding.run {
                rlRoot.setOnClickListener {
                    onClickArticleItem?.invoke(articleBean, ClickAction.NORMAL_CLICK)
                }
                ivCollect.setOnClickListener {
                    onClickArticleItem?.invoke(articleBean, ClickAction.COLLECT_CLICK)
                }
                tvTop.visibility = if (articleBean.isTop) View.VISIBLE else View.GONE
                //如果作者为空，说明文章是被分享出来的，因此作者的位置用分享者代替
                if (articleBean.author.isEmpty()) {
                    tvAuthor.text = articleBean.shareUser
                } else {
                    tvAuthor.text = articleBean.author
                }
                tvDate.text = articleBean.niceDate
                tvTitle.text = articleBean.title
                if (articleBean.chapterName.isEmpty()) {
                    tvChapterName.text = articleBean.superChapterName
                } else {
                    tvChapterName.text = articleBean.chapterName
                }
                if (articleBean.collectVisible) {
                    if (articleBean.collect) {
                        ivCollect.setImageResource(R.drawable._collect)
                    } else {
                        ivCollect.setImageResource(R.drawable.un_collect)
                    }
                } else {
                    ivCollect.visibility = View.GONE
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = LayoutArticleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArticleViewHolder(binding)
    }


}