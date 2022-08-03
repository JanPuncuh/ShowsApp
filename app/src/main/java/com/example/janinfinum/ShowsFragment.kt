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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import retrofit2.Call
import retrofit2.Response
import kotlin.system.exitProcess


class ShowsFragment : Fragment() {

    lateinit var app: MyApplication
    private lateinit var bottomSheetBinding: ManageProfileBottomsheetLayoutBinding
    private val REQUEST_ID = 7
    private lateinit var getContent: ActivityResultLauncher<Intent>
    private lateinit var email: String

    private var _binding: ActivityShowsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ShowsAdapter

    private val viewModel: ShowsViewModel by viewModels {
        ShowsViewModelFactory(app.database)
    }

    companion object {
        const val ID = "ID"
        private const val UID = "UID"
        private const val TOKEN = "TOKEN"
        private const val CLIENT = "CLIENT"
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

        app = activity?.application as MyApplication

        val preferences = this.requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE)

        //set either here or in registrationFragment after successful register
        if (app.token == null) app.token = preferences.getString(TOKEN, "default")
        if (app.client == null) app.client = preferences.getString(CLIENT, "default")
        if (app.uid == null) app.uid = preferences.getString(UID, "testtest@testi.si")

        ApiModule.initRetrofit(requireActivity())

        showLoadingState()

        //if online, api
        if (app.isOnline(requireContext())) {
            ApiModule.retrofit.getShows("Bearer", app.token!!, app.client!!, app.uid!!)
                .enqueue(object : retrofit2.Callback<ShowResponse> {
                    override fun onResponse(call: Call<ShowResponse>, response: Response<ShowResponse>) {
                        if (response.isSuccessful) {
                            viewModel.onResponseAPI(response.body()?.shows!!)
                            viewModel.shows.observe(viewLifecycleOwner) {
                                initShowsRecycler(it)

                                app.database.showsDao().insertAllShows(response.body()?.shows!!)
                            }

                            setEmptyOrNormalState(viewModel.shows.value!!)
                        }
                        //todo  handle unsuccessful
                    }

                    override fun onFailure(call: Call<ShowResponse>, t: Throwable) {
                        Toast.makeText(requireActivity(), "failed to load data", Toast.LENGTH_SHORT).show()
                        Log.d("TEST", "${t.message.toString()}\n${t.stackTraceToString()}")
                    }
                })
        }
        //if no internet, get from database
        else {
            Log.d("TEST", "AAAAAA")
            viewModel.getShowsFromDatabase().observe(viewLifecycleOwner) { shows ->
                if (shows.isNullOrEmpty()) {
                    showEmptyState()
                    if (shows == null) Log.d("TEST", "shows == null")
                }
                else {
                    initShowsRecycler(shows)
                    setEmptyOrNormalState(shows)
                }
            }
        }

        //sets profile picture
        val bitmap = ImageSaver(requireContext()).setFileName("myImage.png").setDirectoryName("images").load()
        if (bitmap != null) {
            binding.imageLogout.setImageBitmap(bitmap)
        }

        binding.imageLogout.setOnClickListener {
            showProfileDialog()
        }
    }

    private fun showEmptyState() {
        binding.emptyStateImageForeground.isVisible = true
        binding.emptyStateImageBackground.isVisible = true
        binding.recycleView.isVisible = false
        binding.showsText.isVisible = false
        binding.loadingStateText.isVisible = false
        binding.emptyStateText.isVisible = true
        binding.imageLogout.isVisible = false
    }

    private fun showNormalState() {
        binding.emptyStateImageForeground.isVisible = false
        binding.emptyStateImageBackground.isVisible = false
        binding.recycleView.isVisible = true
        binding.showsText.isVisible = true
        binding.imageLogout.isVisible = true
        binding.emptyStateText.isVisible = false
        binding.loadingStateText.isVisible = false
    }

    private fun setEmptyOrNormalState(list: List<Show2>) {
        if (list.isNotEmpty()) {
            showNormalState()
        }
        else if (list.isEmpty()) {
            showEmptyState()
        }
    }

    private fun showLoadingState() {
        binding.emptyStateImageForeground.isVisible = true
        binding.emptyStateImageBackground.isVisible = true
        binding.recycleView.isVisible = false
        binding.showsText.isVisible = false
        binding.emptyStateText.isVisible = false
        binding.imageLogout.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initShowsRecycler(list: List<Show2>) {
        //click on item in recycler view
        adapter = ShowsAdapter(list) { show ->
            val id = show.id

            findNavController().navigate(R.id.action_showsActivity_to_showDetailsActivity, bundleOf(ID to id))
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

        email = app.uid!!
        bottomSheetBinding.userMail.text = email

        bottomSheetBinding.changeProfilePictureButton.setOnClickListener {
            if (checkAndRequestPermissions()) {
                openCamera()
            }
        }

        bottomSheetBinding.logoutButton.setOnClickListener {
            showExitAppAlertDialog()
        }

        dialog.show()
    }

    private fun showExitAppAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
        alertDialogBuilder.setTitle("Are you sure?")
        alertDialogBuilder.setMessage("Are you sure you want to log out?")

        //logs out user and closes the app
        alertDialogBuilder.setPositiveButton(android.R.string.yes) { dialog, which ->
            val preferences = this.requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE)
            preferences.edit {
                putBoolean(LoginFragment.REMEMBER_ME, false).commit()
                remove("UID").commit()
                remove("TOKEN").commit()
                remove("CLIENT").commit()
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