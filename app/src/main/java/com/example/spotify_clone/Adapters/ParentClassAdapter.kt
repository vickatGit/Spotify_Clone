package com.example.spotify_clone.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotify_clone.Models.ApiRelatedModels.Thumbnail
import com.example.spotify_clone.R

class ParentClassAdapter(val allLists: ArrayList<List<Thumbnail>>, val requireActivity: FragmentActivity) : RecyclerView.Adapter<ParentClassAdapter.ListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.home_list_layout,parent,false)
        return ListHolder(view)
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        holder.list.layoutManager=LinearLayoutManager(requireActivity,LinearLayoutManager.HORIZONTAL,false)
        val childList=allLists.get(position)
        val childAdapter=ChildListAdapter(childList,requireActivity)
        holder.list.setAdapter(childAdapter)
        allLists.get(position).get(0).observer.observe(requireActivity, Observer {
            childAdapter.updateAdapter(it!!,childAdapter)
            Log.d("TAG", "onBindViewHolder: $it")
        })
    }

    override fun getItemCount(): Int {
        return allLists.size
    }
    inner class ListHolder(view: View) : RecyclerView.ViewHolder(view){
        val about:TextView=view.findViewById(R.id.which)
        val list:RecyclerView=view.findViewById(R.id.list)

    }

}