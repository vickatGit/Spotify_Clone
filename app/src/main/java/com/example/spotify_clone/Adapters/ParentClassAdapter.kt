package com.example.spotify_clone.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.SimplePlaylist
import com.example.spotify_clone.Models.ApiRelatedModels.Thumbnail
import com.example.spotify_clone.R
import com.example.spotify_clone.ViewModels.HomeFragmentViewModel

class ParentClassAdapter(
    val allLists: ArrayList<List<Thumbnail>>,
    val requireActivity: FragmentActivity,
    val viewModel: HomeFragmentViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var homeTopCategoryAdaptor:HomeTopAdapter
    private lateinit var allCategories:ArrayList<SimplePlaylist>


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view:View
        if(viewType==1){
            view=LayoutInflater.from(parent.context).inflate(R.layout.home_top_and_grid_layout,parent,false)
            return HomeTopHolder(view)
        }else {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.home_list_layout, parent, false)
            return ListHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(position==0){
            return  1
        }
        else
            return 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(position==0){
            allCategories= ArrayList(5)
            val view=holder as HomeTopHolder
            view.topCategories.layoutManager=GridLayoutManager(requireActivity,2)
            homeTopCategoryAdaptor= HomeTopAdapter(allCategories)
            view.topCategories.adapter=homeTopCategoryAdaptor
            viewModel.getFetchedCategories().observe(requireActivity, Observer {
                allCategories.clear()
                allCategories.addAll(it)
                homeTopCategoryAdaptor.notifyDataSetChanged()
            })
        }
        else {
            val holder=holder as ListHolder

            holder.list.layoutManager = LinearLayoutManager(requireActivity, LinearLayoutManager.HORIZONTAL, false)
            holder.about.text = allLists.get(position).get(0).title
            val childList = allLists.get(position)
            val childAdapter = ChildListAdapter(childList, requireActivity)
            holder.list.setAdapter(childAdapter)
            allLists.get(position).get(0).observer.observe(requireActivity, Observer {
                childAdapter.updateAdapter(it!!, childAdapter)
                Log.d("TAG", "onBindViewHolder: $it")
            })
        }
    }


    override fun getItemCount(): Int {
        return allLists.size
    }
    inner class ListHolder(view: View) : RecyclerView.ViewHolder(view){
        val about:TextView=view.findViewById(R.id.which)
        val list:RecyclerView=view.findViewById(R.id.list)

    }
    inner class HomeTopHolder(view:View): RecyclerView.ViewHolder(view){
        var topCategories:RecyclerView=view.findViewById(R.id.layout_grid)
    }



}