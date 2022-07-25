package com.example.janinfinum

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.janinfinum.databinding.ActivityShowsBinding
import com.example.janinfinum.databinding.ManageProfileBottomsheetLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlin.system.exitProcess


class ShowsActivity : Fragment() {

    private lateinit var bottomSheetBinding: ManageProfileBottomsheetLayoutBinding
    private val REQUEST_ID = 7
    private lateinit var getContent: ActivityResultLauncher<Intent>
    private lateinit var email: String

    /*private val shows = listOf(
        Show(
            "The Office",
            "The Office is an American mockumentary sitcom television series that depicts the everyday work lives of office employees in the Scranton, Pennsylvania, branch of the fictional Dunder Mifflin Paper Company. It aired on NBC from March 24, 2005, to May 16, 2013, lasting a total of nine seasons.",
            R.drawable.ic_office
        ),
        Show("Stranger Things", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor", R.drawable.ic_stranger_things),
        Show("Krv Nije Voda", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor", R.drawable.krv_nije_voda_1),
    )*/

    private var _binding: ActivityShowsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ShowsAdapter

    private val viewModel by viewModels<ShowsViewModel>()

    companion object {
        const val TITLE_ARG = "TITLE_ARG"
        const val DESC_ARG = "DESC_ARG"
        const val IMG_ARG = "IMG_ARG"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ActivityShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        //when photo is taken
        getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val takenPhoto = result.data?.extras?.get("data")

                //sets both images and saves it on phone's storage
                if (takenPhoto != null) {
                    bottomSheetBinding.profilePicture.setImageBitmap(takenPhoto as Bitmap?)
                    binding.imageLogout.setImageBitmap(takenPhoto as Bitmap)

                    ImageSaver(context).setFileName("myImage.png").setDirectoryName("images").save(takenPhoto)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.shows.observe(viewLifecycleOwner) { show ->
            //Toast.makeText(requireContext(), show.size.toString(), Toast.LENGTH_SHORT).show()
        }

        initShowsRecycler()

        //sets profile picture
        val bitmap = ImageSaver(requireContext()).setFileName("myImage.png").setDirectoryName("images").load()
        if (bitmap != null) {
            binding.imageLogout.setImageBitmap(bitmap)
        }

        if (viewModel.shows.value?.isNotEmpty()!!) {
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
        adapter = ShowsAdapter(viewModel.shows) { show ->
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

        bottomSheetBinding = ManageProfileBottomsheetLayoutBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        val bitmap = ImageSaver(requireContext()).setFileName("myImage.png").setDirectoryName("images").load()
        if (bitmap != null) {
            bottomSheetBinding.profilePicture.setImageBitmap(bitmap)
        }

        email = arguments?.getString(LoginActivity.EMAIL).toString()
        bottomSheetBinding.userMail.text = email

        bottomSheetBinding.changeProfilePictureButton.setOnClickListener {
            if (checkAndRequestPermissions()) {
                openCamera()
            }
        }

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
            //does nothing (cancels the logout)
        }

        alertDialogBuilder.show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        getContent.launch(intent)
    }

    private fun checkAndRequestPermissions(): Boolean {
        val camera = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(requireActivity(), listPermissionsNeeded.toTypedArray(), REQUEST_ID)
            return false
        }
        return true
    }

}