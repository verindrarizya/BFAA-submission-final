package com.example.subfundatiga.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subfundatiga.adapters.UsersAdapter
import com.example.subfundatiga.assets.setViewVisible
import com.example.subfundatiga.databinding.FragmentUserFollowersBinding
import com.example.subfundatiga.viewmodels.UserFollowersViewModel

class UserFollowersFragment : Fragment() {

    private lateinit var userFollowersViewModel: UserFollowersViewModel
    private lateinit var adapter: UsersAdapter
    private var _binding: FragmentUserFollowersBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userFollowersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserFollowersViewModel::class.java)
        userFollowersViewModel.setUsers(arguments?.getString("username")!!)

        binding.progressBar.setViewVisible(true)

        adapter = UsersAdapter()
        adapter.notifyDataSetChanged()

        binding.rvFollowers.layoutManager = LinearLayoutManager(view.context)
        binding.rvFollowers.adapter = adapter

        userFollowersViewModel.getUsers().observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                adapter.setData(it)
                binding.progressBar.setViewVisible(false)
            } else {
                binding.progressBar.setViewVisible(false)
                binding.tvNoneStatement.setViewVisible(false)
            }
        })
    }

    fun newFragmentInstance(qUsername: String): UserFollowersFragment {
        val newFrag = UserFollowersFragment()
        val arg: Bundle = Bundle().apply {
            putString("username", qUsername)
        }
        newFrag.arguments = arg

        return newFrag
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}