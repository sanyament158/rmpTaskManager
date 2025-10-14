package com.example.taskmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.taskmanager.DataClasses.Goal;
import androidx.recyclerview.widget.RecyclerView
// rv = recyclerview
public class GoalAdapter (private val goalList: List<Goal>) : RecyclerView.Adapter<GoalAdapter.GoalViewHolder>()
{


//          override methods
    override fun getItemCount(): Int = goalList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.goal_item, parent, false)
        return GoalViewHolder(view)
    } //complete

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val goal = goalList[position]

        holder.textTitle.text = goal.Title
        holder.textStatus.text = "Status: ${goal.IdStatus}"
        holder.textCategory.text = "Category: ${goal.IdCategory}"
        holder.textImportance.text = "Importance: ${goal.IdImportance}"

        if (goal.IsComplete) {
            holder.textTitle.setTextColor(0xFF4CAF50.toInt()) // Зелёный
        } else {
            holder.textTitle.setTextColor(0xFFFF6B68.toInt()) // Красноватый
        }
    }

    class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val textStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val textCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val textImportance: TextView = itemView.findViewById(R.id.tvImportance)
    }
}


//onBindViewHolder, getItemCount