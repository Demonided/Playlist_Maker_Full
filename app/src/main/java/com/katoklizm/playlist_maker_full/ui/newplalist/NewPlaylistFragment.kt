package com.katoklizm.playlist_maker_full.ui.newplalist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.databinding.FragmentNewPlaylistBinding
import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist
import com.katoklizm.playlist_maker_full.presentation.newplaylist.NewPlaylistViewModel
import com.katoklizm.playlist_maker_full.ui.albuminfo.AlbumInfoFragment
import com.markodevcic.peko.PermissionRequester
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class NewPlaylistFragment : Fragment() {

    private var _binding: FragmentNewPlaylistBinding? = null
    val binding get() = _binding!!

    private val viewModel: NewPlaylistViewModel by viewModel()

    val requester = PermissionRequester.instance()

    val album = AlbumPlaylist()

    var imageUri: Uri? = null

    private var stateButton = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageView = binding.newPlaylistImage
        val editTextTitle = binding.newPlaylistTitle
        val editTextDescription = binding.newPlaylistDescription
        val buttonCreated = binding.newPlayerCreate

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        buttonCreated.isEnabled = stateButton

        val pickImage =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                //обрабатываем событие выбора пользователем фотографии
                if (uri != null) {
                    imageView.setImageURI(uri)
                    saveImageToPrivateStorage(uri)
                    imageUri = uri
                } else {
                    imageUri = uri
                }
            }

        val selectedAlbum: AlbumPlaylist? = arguments?.getParcelable(ALBUM)
        viewModel.updateStateAlbum(selectedAlbum)

        binding.newPlayerBack.setOnClickListener {
            onBackPressed()
        }

        binding.newPlaylistTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.newPlayerCreate.isEnabled = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                // TODO("Not yet implemented")
            }
        })

        viewModel.fillData()

        viewModel.stateAlbumPlaylist.observe(viewLifecycleOwner) { albumPlaylist ->
            if (albumPlaylist != null) {
                binding.newPlayerPlaylist.text = getString(R.string.new_playlist_edit)
                binding.newPlayerCreate.text = getString(R.string.new_playlist_save)
                val title = albumPlaylist.name
                val description = albumPlaylist.description

                binding.newPlaylistTitle.setText(title)
                binding.newPlaylistDescription.setText(description)

                Glide.with(requireContext())
                    .load(albumPlaylist.image)
                    .into(binding.newPlaylistImage)
            }

            binding.newPlayerCreate.setOnClickListener {
                if (albumPlaylist != null) {
                    val title = editTextTitle.text.toString()
                    val description = editTextDescription.text.toString()

                    val album = AlbumPlaylist(
                        id = albumPlaylist.id,
                        name = title,
                        description = description,
                        image = imageUri.toString(),
                        quantity = albumPlaylist.quantity,
                        track = albumPlaylist.track
                    )

                    lifecycleScope.launch {
                        viewModel.updateAlbum(album)
                        Snackbar.make(
                            binding.root,
                            "${getString(R.string.new_playlist_playlist)} $title отредактирован",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    }
                } else {
                    val title = editTextTitle.text.toString()
                    val description = editTextDescription.text.toString()

                    val album = AlbumPlaylist(
                        name = title,
                        description = description,
                        image = imageUri.toString()
                    )

                    lifecycleScope.launch {
                        viewModel.addAlbumPlaylist(album)
                        Snackbar.make(
                            binding.root,
                            "${getString(R.string.new_playlist_playlist)} $title ${getString(R.string.new_playlist_created)}",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    }
                }
            }
        }



        binding.newPlaylistImage.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    fun onBackPressed() {
        if (isFieldsEmpty() && !isImageSelected()) {
            findNavController().popBackStack()
        } else {
            lifecycleScope.launch {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.new_playlist_finish_creating_playlist)
                    .setMessage(R.string.new_playlist_data_loss)
                    .setNegativeButton(R.string.new_playlist_cancellation) { dialog, which -> // Добавляет кнопку «Отмена»
                        // Действия, выполняемые при нажатии на кнопку «Отмена»

                    }
                    .setPositiveButton(R.string.new_playlist_complete) { dialog, which -> // Добавляет кнопку «Да»
                        // Действия, выполняемые при нажатии на кнопку «Да»
//                            requireActivity().onBackPressed()
                        findNavController().popBackStack()
                    }
                    .show()
            }
        }
    }

    private fun isFieldsEmpty(): Boolean {
        val title = binding.newPlaylistTitle.text.toString()
        val description = binding.newPlaylistDescription.text.toString()
        return title.isEmpty() && description.isEmpty()
    }

    private fun isImageSelected(): Boolean {
        // Проверить наличие изображения в ImageView
        return binding.newPlaylistImage.drawable != null
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePatch =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        //создаем каталог, если он не создан
        if (!filePatch.exists()) {
            filePatch.mkdir()
        }
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePatch, "first_cover.jpg")
        // создаем входящий поток байтов из выбранной картинки
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        // создаем исходящий поток данных байтов в созданный выше файл
        val outputStream = FileOutputStream(file)
        // записываем картинку с помощью BitmapFactory
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    companion object {

        private const val ALBUM = "album_playlist"

        fun createArgs(album: AlbumPlaylist?): Bundle =
            bundleOf(ALBUM to album)
    }
}