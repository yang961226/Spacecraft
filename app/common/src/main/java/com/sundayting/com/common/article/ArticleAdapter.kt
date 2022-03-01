package com.sundayting.com.common.article

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sundayting.com.common.R
import com.sundayting.com.common.bean.ArticleBean
import com.sundayting.com.common.databinding.LayoutArticleBinding
import javax.inject.Inject

class ArticleAdapter @Inject constructor(
    itemCallback: DiffUtil.ItemCallback<ArticleBean>,
) : ListAdapter<ArticleBean, RecyclerView.ViewHolder>(itemCallback) {

    enum class ClickAction {
        NORMAL_CLICK,
        COLLECT_CLICK,
    }

    var onClickArticleItem: ((articleBean: ArticleBean, clickAction: ClickAction) -> Unit)? = null

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
        Resources.getSystem().assets
        getItem(position).let { articleBean ->
            if (holder is ArticleViewHolder) {
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
                    if (articleBean.superChapterName.isEmpty()) {
                        tvChapterName.text = articleBean.chapterName
                    } else {
                        tvChapterName.text = articleBean.superChapterName
                    }
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