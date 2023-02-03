package com.example.petcare.ui.pet

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.petcare.PetCareApplication
import com.example.petcare.R
import com.example.petcare.database.pet.Pet
import com.example.petcare.databinding.FragmentAddPetBinding
import com.example.petcare.viewmodels.PetViewModel
import com.example.petcare.viewmodels.PetViewModelFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AddPetFragment : Fragment() {
    private var _binding: FragmentAddPetBinding? = null
    private val binding get() = _binding!!

    private val petViewModel: PetViewModel by activityViewModels {
        PetViewModelFactory((activity?.application as PetCareApplication).database.petDao())
    }

    lateinit var pet: Pet

    private val navigationArgs: AddPetFragmentArgs by navArgs()

    private var cal = Calendar.getInstance()

    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }

    // ------------------------------------------------------------------------------------

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddPetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.petId
        if (id > 0) {
            this.petViewModel.retrievePet(id).observe(this.viewLifecycleOwner) { selectedPet ->
                pet = selectedPet
                bind(pet)
            }
        } else {
            binding.apply {
                saveInfoButton.setOnClickListener { addNewPet() }
            }
        }

        binding.petDateOfBirth.setOnClickListener {
            context?.hideKeyboard(it)
            pickDateOfBirth()
        }

        binding.apply {
            dog.setOnClickListener { petViewModel.setImage(R.drawable.dog.toString()) }
            cat.setOnClickListener { petViewModel.setImage(R.drawable.cat.toString()) }
            bird.setOnClickListener { petViewModel.setImage(R.drawable.bird.toString()) }
            fish.setOnClickListener { petViewModel.setImage(R.drawable.fish.toString()) }
            livestock.setOnClickListener { petViewModel.setImage(R.drawable.livestock.toString()) }
            custom.setOnClickListener { selectImageFromGalleryResult.launch("image/*") }
            radioMale.setOnClickListener { petViewModel.setSex(getString(R.string.male)) }
            radioFemale.setOnClickListener { petViewModel.setSex(getString(R.string.female)) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        petViewModel.setSex("")
        petViewModel.setImage(R.drawable.image.toString())

    }

// ----------------------------------------------------------------------------

    private fun isEntryValid(): Boolean {
        return this.petViewModel.isEntryValid(
            binding.petName.text.toString(),
            binding.petSpecies.text.toString(),
            petViewModel.sex.value.toString(),
            binding.petDateOfBirth.text.toString()
        )
    }

    private fun addNewPet() {
        if (isEntryValid()) {
            this.petViewModel.addNewPet(
                petViewModel.image.value!!,
                binding.petName.text.toString(),
                binding.petSpecies.text.toString(),
                binding.petBreed.text.toString(),
                petViewModel.sex.value.toString(),//binding.petSex.text.toString(),
                binding.petDateOfBirth.text.toString(),
                binding.petColour.text.toString(),
                binding.petChip.text.toString()
            )
            findNavController().navigate(R.id.action_addPetFragment_to_petListFragment)
        } else {
            Toast.makeText(context, getString(R.string.pet_fields), Toast.LENGTH_LONG).show()
        }
    }

// ----------------------------------------------------------------------------

    private fun bind(pet: Pet) {
        binding.apply {
            petName.setText(pet.petName, TextView.BufferType.SPANNABLE)
            petSpecies.setText(pet.petSpecies, TextView.BufferType.SPANNABLE)
            petBreed.setText(pet.petBreed, TextView.BufferType.SPANNABLE)
            petDateOfBirth.setText(pet.petDateOfBirth, TextView.BufferType.SPANNABLE)
            petColour.setText(pet.petColour, TextView.BufferType.SPANNABLE)
            petChip.setText(pet.petChip, TextView.BufferType.SPANNABLE)
            saveInfoButton.setOnClickListener { updatePet() }
        }

        if (pet.petSex == getString(R.string.male)) {
            petViewModel.setSex(getString(R.string.male))
            binding.radioMale.isChecked = true
        } else {
            petViewModel.setSex(getString(R.string.female))
            binding.radioFemale.isChecked = true
        }

        if (pet.petImage == R.drawable.dog.toString()) {
            petViewModel.setImage(R.drawable.dog.toString())
            binding.dog.isChecked = true
        } else if (pet.petImage == R.drawable.cat.toString()) {
            petViewModel.setImage(R.drawable.cat.toString())
            binding.cat.isChecked = true
        } else if (pet.petImage == R.drawable.bird.toString()) {
            petViewModel.setImage(R.drawable.bird.toString())
            binding.bird.isChecked = true
        } else if (pet.petImage == R.drawable.fish.toString()) {
            petViewModel.setImage(R.drawable.fish.toString())
            binding.fish.isChecked = true
        } else if (pet.petImage == R.drawable.livestock.toString()) {
            petViewModel.setImage(R.drawable.livestock.toString())
            binding.livestock.isChecked = true
        } else {
            petViewModel.setImage(pet.petImage.toString())
            binding.custom.isChecked = true
        }
    }

    private fun updatePet() {
        if (isEntryValid()) {
            this.petViewModel.updatePet(
                this.navigationArgs.petId,
                petViewModel.image.value!!,
                this.binding.petName.text.toString(),
                this.binding.petSpecies.text.toString(),
                this.binding.petBreed.text.toString(),
                petViewModel.sex.value.toString(),
                this.binding.petDateOfBirth.text.toString(),
                this.binding.petColour.text.toString(),
                this.binding.petChip.text.toString()
            )
            val action = AddPetFragmentDirections.actionAddPetFragmentToPetListFragment()
            findNavController().navigate(action)
        } else {
            Toast.makeText(context, getString(R.string.pet_fields), Toast.LENGTH_LONG).show()
        }
    }

// ----------------------------------------------------------------------------

    private fun pickDateOfBirth() {
        DatePickerDialog(
            requireContext(), dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.petDateOfBirth.setText(sdf.format(cal.time))
    }

// ----------------------------------------------------------------------------

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

// ----------------------------------------------------------------------------

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            try {
                if (uri != null && uri.toString().contains("content://")) {
                    saveImageToInternalStorage(uri)

                // TODO: delete old image and create new one for change in the id icon
                    //val file = File(pet.petImage)
                    //file.delete()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    private fun saveImageToInternalStorage(uri: Uri) {
        val fileName = "${UUID.randomUUID()}" + ".jpg"
        requireContext().openFileOutput(fileName, Context.MODE_PRIVATE)
            .use { stream ->
                val source =
                    ImageDecoder.createSource(requireContext().contentResolver, uri)
                val bitmap = ImageDecoder.decodeBitmap(source)

                val bos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos)
                val bitmapData = bos.toByteArray()

                stream.write(bitmapData)

                val path = requireContext().filesDir.absolutePath
                val file = File(
                    "$path/$fileName"
                )
                petViewModel.setImage(file.path.toString())
            }

    }

    private fun replaceImageToInternalStorage(path: String) {
        val uri = Uri.parse(path)
    }

// ----------------------------------------------------------------------------
}