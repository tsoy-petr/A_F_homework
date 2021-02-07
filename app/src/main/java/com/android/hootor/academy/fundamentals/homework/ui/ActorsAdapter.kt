package com.android.hootor.academy.fundamentals.homework.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.hootor.academy.fundamentals.homework.R
import com.android.hootor.academy.fundamentals.homework.domain.models.Actor
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.imageview.ShapeableImageView

class ActorsAdapter() : RecyclerView.Adapter<ActorsAdapter.ActorViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Actor>() {
        override fun areItemsTheSame(oldItem: Actor, newItem: Actor): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Actor, newItem: Actor): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
    private val differ = AsyncListDiffer(this, diffCallback)
    fun submitList(list: List<Actor>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        return ActorViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_holder_actor,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        val actor = differ.currentList[position]
        holder.bind(actor)
    }

    override fun getItemCount() = differ.currentList.size

    class ActorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val siv: ShapeableImageView = itemView.findViewById(R.id.iv_actor)
        private val nameActor: TextView = itemView.findViewById(R.id.tv_name_actor)

        fun bind(actor: Actor) {
            actor.picture?.also {
                val pictureUrl = "https://image.tmdb.org/t/p/w342$it"
                Glide.with(itemView)
                    .load(pictureUrl)
                    .placeholder(R.drawable.ic_base_image)
                    .fallback(R.drawable.ic_base_image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(siv)
            }
            nameActor.text = actor.name
        }
    }
}

