package com.kys.linkedinclone.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.kys.linkedinclone.AppSharedPreferences
import com.kys.linkedinclone.R

class JobsFragment : Fragment() {
    private lateinit var profileImg: ImageView
    private lateinit var appSharedPreferences: AppSharedPreferences



    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_jobs, container, false)
        profileImg = view.findViewById(R.id.user_img)
        appSharedPreferences = AppSharedPreferences(requireContext())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(requireContext()).load(appSharedPreferences.getImgUrl()).into(profileImg)
    }
}