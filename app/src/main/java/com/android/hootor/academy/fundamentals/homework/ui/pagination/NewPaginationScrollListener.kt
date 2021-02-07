package com.android.hootor.academy.fundamentals.homework.ui.pagination

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class NewPaginationScrollListener(
    var currentPage: Int = 0,
    var previousTotalItemCount: Int = 0,
    var startingPageIndex: Int = 0,
    private val layoutManager: LinearLayoutManager
) : RecyclerView.OnScrollListener() {

    var visibleThreshold = 5


    init {
        when (layoutManager) {
            is GridLayoutManager -> {
                visibleThreshold *= layoutManager.spanCount
            }
            is StaggeredGridLayoutManager -> {
                visibleThreshold *= layoutManager.spanCount
            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val totalItemCount = layoutManager.itemCount

        val lastVisibleItemPosition = when (layoutManager) {
            is GridLayoutManager -> {
                layoutManager.findLastVisibleItemPosition()
            }
            is StaggeredGridLayoutManager -> {
                val lastVisibleItemPositions = layoutManager.findLastVisibleItemPositions(null)
                getLastVisibleItem(lastVisibleItemPositions)
            }
            else -> {
                layoutManager.findLastVisibleItemPosition()
            }
        }

        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
        }
        visibleThreshold = layoutManager.childCount
        Log.i("happy", "isLoading: ${isLoading()}")
        Log.i("happy", "lastVisibleItemPosition: ${lastVisibleItemPosition}")
        Log.i("happy", "visibleThreshold: ${visibleThreshold}")
        Log.i("happy", "totalItemCount: ${totalItemCount}")

        if (!isLoading() && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            currentPage++;
            loadMoreItems(currentPage);
        }
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        val maxSize = lastVisibleItemPositions.maxOrNull() ?: 0
        return maxSize
    }

    abstract fun isLoading(): Boolean
    abstract fun loadMoreItems(currentPage: Int)
}