package com.example.subfundatiga.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.subfundatiga.R
import com.example.subfundatiga.databinding.ItemRowUserBinding
import com.example.subfundatiga.data.model.User

class UsersAdapter: RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {

    private val mData = ArrayList<User>()
    private var onItemClickCallback: OnItemCallback? = null

    inner class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowUserBinding.bind(itemView)

        fun bind(user: User) {
            Glide.with(itemView.context)
                .load(user.imgProfile)
                .apply(RequestOptions().override(100, 100))
                .into(binding.imgUser)

            binding.tvUsername.text = user.username

            itemView.setOnClickListener { onItemClickCallback?.onItemClicked(user) }
        }
    }

    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    fun getData() = mData

    fun setOnItemClickCallback(onItemClickCallback: OnItemCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    interface OnItemCallback {
        fun onItemClicked(data: User)
    }
}