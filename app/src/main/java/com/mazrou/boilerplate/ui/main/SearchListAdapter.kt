package com.mazrou.boilerplate.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mazrou.boilerplate.R

import com.mazrou.boilerplate.model.ui.Racine
import kotlinx.android.synthetic.main.ayat_layout.view.*


class SearchListAdapter(
  private var interaction: Interaction? = null,
)   : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Racine>() {

        override fun areItemsTheSame(
            oldItem: Racine,
            newItem: Racine
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Racine,
            newItem: Racine
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RacineViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.ayat_layout,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RacineViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(list: List<Racine>) {
        differ.submitList(list)
    }

    inner class RacineViewHolder constructor(
        itemView: View,
        private val interaction: Interaction?,
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Racine) = with(itemView) {
            racine_txt_view.text = item.racine
            number_of_world_txt_view.text = item.worldNumber
            number_letter_txt_view.text = item.letterNumber
            setOnClickListener {
                interaction?.onItemClicked(item , adapterPosition)
            }
        }
    }
    interface Interaction{
        fun onItemClicked (item : Any , index : Int)
    }

}