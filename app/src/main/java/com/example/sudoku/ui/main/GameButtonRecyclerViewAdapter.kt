package com.example.sudoku.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.sudoku.databinding.ItemGameButtonBinding

class GameButtonRecyclerViewAdapter(
    private val values: List<String>,
    val startGame: (Int) -> Unit
) : RecyclerView.Adapter<GameButtonRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemGameButtonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.gameButton.text = item
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: ItemGameButtonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val gameButton: Button = binding.gameButton

        init {
            gameButton.setOnClickListener {
                Log.d("position", absoluteAdapterPosition.toString())
                startGame(absoluteAdapterPosition)
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + gameButton.text + "'"
        }
    }
}