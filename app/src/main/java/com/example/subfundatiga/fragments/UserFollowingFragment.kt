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
import com.example.subfundatiga.databinding.FragmentUserFollowingBinding
import com.example.subfundatiga.viewmodels.UserFollowingViewModel

class UserFollowingFragment : Fragment() {

    private lateinit var userFollowingViewModel: UserFollowingViewModel
    private lateinit var adapter: UsersAdapter
    private var _binding: FragmentUserFollowingBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userFollowingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserFollowingViewModel::class.java)
        userFollowingViewModel.setUsers(arguments?.getString("username")!!)

        adapter = UsersAdapter()
        adapter.notifyDataSetChanged()

        binding.progressBar.setViewVisible(true)

        binding.rvFollowings.layoutManager = LinearLayoutManager(view.context)
        binding.rvFollowings.adapter = adapter

        userFollowingViewModel.getUsers().observe(viewLifecycleOwner, {
            if(it.isNotEmpty()) {
                adapter.setData(it)
                binding.progressBar.setViewVisible(false)
            } else {
                binding.progressBar.setViewVisible(false)
                binding.tvNoneStatement.setViewVisible(true)
            }
        })
    }

    fun newFragmentInstance(qUsername: String): UserFollowingFragment {
        val newFrag = UserFollowingFragment()
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