package com.example.subfundatiga.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.subfundatiga.R
import com.example.subfundatiga.assets.setViewInvisible
import com.example.subfundatiga.assets.setViewVisible
import com.example.subfundatiga.data.database.ContractHelper.CONTENT_URI
import com.example.subfundatiga.data.model.User
import com.example.subfundatiga.databinding.FragmentUserDetailBinding
import com.example.subfundatiga.viewmodels.UserDetailViewModel

class UserDetailFragment : Fragment() {

    private lateinit var userDetailViewModel: UserDetailViewModel
    private var _binding: FragmentUserDetailBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var currentUser: User
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLayoutViewsVisible(false)
        userDetailViewModel = ViewModelProvider(this).get(UserDetailViewModel::class.java)

        username = arguments?.getString("username").toString()
        userDetailViewModel.setUser(username)

        userDetailViewModel.user.observe(viewLifecycleOwner, {
            with(binding) {
                Glide.with(view.context)
                    .load(it.imgProfile)
                    .apply(RequestOptions().override(150, 150))
                    .into(imgUserAvatar)

                tvDetailUsername.text = it.username
                tvDetailName.text = it.name
                tvDetailCompany.text = it.company
                tvDetailLocation.text = it.location
                tvDetailRepository.text = it.repoCount.toString()
                tvDetailFollowing.text = it.followingCount.toString()
                tvDetailFollowers.text = it.followersCount.toString()

                currentUser = it

                setLayoutViewsVisible(true)
            }
        })

        userDetailViewModel.checkUserAlreadyFavInDb(username)

        userDetailViewModel.currentUserExists.observe(viewLifecycleOwner, {
            handleUserFavButton(it)
        })
    }

    fun newFragmentInstance(qUsername: String): UserDetailFragment {
        val newFrag = UserDetailFragment()
        val arg: Bundle = Bundle().apply {
            putString("username", qUsername)
        }
        newFrag.arguments = arg

        return newFrag
    }

    private fun handleUserFavButton(isUserAlreadyFav: Boolean) {
        with(binding.fabUserFav) {
            if (isUserAlreadyFav) {
                setImageResource(R.drawable.ic_favorite_fill_white_24)
                setOnClickListener {
                    userDetailViewModel.deleteUser(currentUser)
                    Toast.makeText(this@UserDetailFragment.context, "Successfully Deleted", Toast.LENGTH_SHORT).show()
                    context.contentResolver.notifyChange(CONTENT_URI, null)
                }
            } else {
                setImageResource(R.drawable.ic_favorite_border_white_24)
                setOnClickListener {
                    userDetailViewModel.insertUser(currentUser)
                    Toast.makeText(this@UserDetailFragment.context, "Successfully Added", Toast.LENGTH_SHORT).show()
                    context.contentResolver.notifyChange(CONTENT_URI, null)
                }
            }
        }
    }

    private fun setLayoutViewsVisible(isTrue: Boolean) {
        with(binding) {
            if (isTrue) {
                cardView.setViewVisible(true)
                imgUserAvatar.setViewVisible(true)
                tvDetailUsername.setViewVisible(true)
                tvDetailName.setViewVisible(true)
                tvDetailCompany.setViewVisible(true)
                tvDetailLocation.setViewVisible(true)
                tvLabelFollowers.setViewVisible(true)
                tvLabelFollowing.setViewVisible(true)
                tvLabelRepository.setViewVisible(true)
                tvDetailFollowers.setViewVisible(true)
                tvDetailFollowers.setViewVisible(true)
                tvDetailRepository.setViewVisible(true)
                fabUserFav.setViewVisible(true)
                progressBar.setViewVisible(false)
            } else {
                cardView.setViewInvisible()
                imgUserAvatar.setViewInvisible()
                tvDetailUsername.setViewInvisible()
                tvDetailName.setViewInvisible()
                tvDetailCompany.setViewInvisible()
                tvDetailLocation.setViewInvisible()
                tvLabelFollowers.setViewInvisible()
                tvLabelFollowing.setViewInvisible()
                tvLabelRepository.setViewInvisible()
                tvDetailFollowers.setViewInvisible()
                tvDetailFollowers.setViewInvisible()
                tvDetailRepository.setViewInvisible()
                fabUserFav.setViewInvisible()
                progressBar.setViewVisible(true)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}