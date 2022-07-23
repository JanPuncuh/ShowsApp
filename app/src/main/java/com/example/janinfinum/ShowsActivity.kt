package com.example.janinfinum

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.janinfinum.databinding.ActivityShowsBinding
import com.example.janinfinum.databinding.ManageProfileBottomsheetLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlin.system.exitProcess

class ShowsActivity : Fragment() {

    private lateinit var email: String
    private val shows = listOf(
        Show(
            "The Office",
            "The Office is an American mockumentary sitcom television series that depicts the everyday work lives of office employees in the Scranton, Pennsylvania, branch of the fictional Dunder Mifflin Paper Company. It aired on NBC from March 24, 2005, to May 16, 2013, lasting a total of nine seasons.",
            R.drawable.ic_office
        ),
        Show("Stranger Things", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor", R.drawable.ic_stranger_things),
        Show("Krv Nije Voda", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor", R.drawable.krv_nije_voda_1),
    )

    private var _binding: ActivityShowsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ShowsAdapter

    companion object {
        const val TITLE_ARG = "TITLE_ARG"
        const val DESC_ARG = "DESC_ARG"
        const val IMG_ARG = "IMG_ARG"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ActivityShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initShowsRecycler()

        if (shows.isNotEmpty()) {
            binding.emptyStateText.isVisible = false
            binding.emptyStateImageBackground.isVisible = false
            binding.emptyStateImageForeground.isVisible = false
        }

        binding.imageLogout.setOnClickListener {
            showProfileDialog()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initShowsRecycler() {
        //click on item in recycler view
        adapter = ShowsAdapter(shows) { show ->
            val title = show.title
            val desc = show.description
            val img = show.imageResourceId

            findNavController().navigate(
                R.id.action_showsActivity_to_showDetailsActivity,
                bundleOf(TITLE_ARG to title, DESC_ARG to desc, IMG_ARG to img)
            )
        }

        binding.recycleView.layoutManager = LinearLayoutManager(activity)
        binding.recycleView.adapter = adapter

    }

    private fun showProfileDialog() {
        val dialog = BottomSheetDialog(requireActivity())

        //drug layout binding
        val bottomSheetBinding = ManageProfileBottomsheetLayoutBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        email = arguments?.getString(LoginActivity.EMAIL).toString()
        bottomSheetBinding.userMail.text = email

        //exits application
        bottomSheetBinding.logoutButton.setOnClickListener {
            showAlertDialog()
        }

        dialog.show()
    }

    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
        alertDialogBuilder.setTitle("Are you sure?")
        alertDialogBuilder.setMessage("Are you sure you want to log out?")

        //logs out user and closes the app
        alertDialogBuilder.setPositiveButton(android.R.string.yes) { dialog, which ->
            val preferences = this.requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE)
            preferences.edit {
                putBoolean(LoginActivity.REMEMBER_ME, false).commit()
            }
            exitProcess(-1)
        }

        alertDialogBuilder.setNegativeButton(android.R.string.no) { dialog, which ->
            //cancels the logout
        }

        alertDialogBuilder.show()
    }

}