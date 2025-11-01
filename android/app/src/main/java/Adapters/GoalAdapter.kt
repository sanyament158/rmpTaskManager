package com.example.taskmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.taskmanager.DataClasses.Goal;
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.databinding.GoalItemBinding

class GoalAdapter () : RecyclerView.Adapter<GoalAdapter.GoalViewHolder>()
{
    var data: List<Goal> = emptyList()
        set(newValue){
            field = newValue
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = GoalItemBinding.inflate(inflater, parent, false)

        return GoalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val goal = data[position]

        holder.textTitle.text = goal.Title
        holder.textStatus.text = goal.Status
        holder.textCategory.text = goal.Category
        holder.textImportance.text = goal.Importance

//        if (goal.IsComplete) {
//            holder.textTitle.setTextColor(0xFF4CAF50.toInt()) // Зелёный
//        } else {
//            holder.textTitle.setTextColor(0xFFFF6B68.toInt()) // Красноватый
//        }
    }

    class GoalViewHolder(val binding: GoalItemBinding) : RecyclerView.ViewHolder(binding.root) { // todo: from View to Binding
        val textTitle = binding.tvTitle
        val textStatus = binding.tvStatus
        val textCategory = binding.tvCategory
        val textImportance = binding.tvImportance
    }
}