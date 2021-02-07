package com.android.hootor.academy.fundamentals.homework.ui.pagination

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.hootor.academy.fundamentals.homework.uifeature.GridAutofitLayoutManager

abstract class PaginationScrollListener(private val layoutManager: GridLayoutManager) :
    RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                && firstVisibleItemPosition >= 0
                && totalItemCount >= getTotalPageCount()) {
                loadMoreItems()
            }
        }
    }

    abstract fun loadMoreItems()
    abstract fun getTotalPageCount(): Int
    abstract fun isLastPage(): Boolean
    abstract fun isLoading(): Boolean

}