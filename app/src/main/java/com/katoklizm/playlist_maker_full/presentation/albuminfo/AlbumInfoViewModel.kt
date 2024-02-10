package com.katoklizm.playlist_maker_full.presentation.albuminfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.katoklizm.playlist_maker_full.domain.album.AlbumPlaylistInteractor
import com.katoklizm.playlist_maker_full.domain.album.AlbumPlaylistRepository
import com.katoklizm.playlist_maker_full.domain.album.SelectPlaylistRepository
import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.domain.sharing.SharingInteractor
import com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist.PlaylistState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.lang.reflect.Type

class AlbumInfoViewModel(
    private val albumPlaylistInteractor: AlbumPlaylistInteractor,
    private val selectedRepository: SelectPlaylistRepository,
    private var sharingInteractor: SharingInteractor
) : ViewModel() {

    private val _stateAlbumTrack = MutableLiveData<List<Track>?>()
    val stateAlbumTrack: LiveData<List<Track>?> = _stateAlbumTrack

    private val _stateAlbum = MutableLiveData<AlbumPlaylist?>()
    val stateAlbum: LiveData<AlbumPlaylist?> = _stateAlbum

    init {
        fillData()
    }

    fun fillData() {
//        viewModelScope.launch {
//            albumPlaylistInteractor.getAllAlbumPlaylist()
//                .collect { album ->
//                    album.map {
//                        _stateAlbum.postValue(it)
//                    }
//                }
//        }
        viewModelScope.launch {
            selectedRepository.getPlaylist()
                .filterNotNull()
                .collect {
                    _stateAlbum.postValue(it)
                }
        }
    }

    fun loadTrackList(selectedAlbum: AlbumPlaylist?) {
        selectedAlbum?.track?.let { trackString ->
            val gson = Gson()
            val trackListType = object : TypeToken<List<Track>>() {}.type
            val tracks: List<Track> = gson.fromJson(trackString, trackListType) ?: emptyList()
            _stateAlbumTrack.postValue(tracks)
        }
    }

    fun loadAlbum(album: AlbumPlaylist?) {
        _stateAlbum.postValue(album)
    }

    fun deleteTrack(playlist: AlbumPlaylist, selectedTrack: Track) {
        val listType: Type = object : TypeToken<ArrayList<Track?>?>() {}.type
        val tracks: ArrayList<Track>? = Gson().fromJson(playlist.track, listType)
        if (tracks != null) {
            tracks.remove(selectedTrack)
            val newString = Gson().toJson(tracks)
            val newPlaylist = AlbumPlaylist(
                playlist.id, playlist.name, playlist.description, playlist.image,
                playlist.quantity.minus(1), newString
            )
            viewModelScope.launch(Dispatchers.IO) {
                albumPlaylistInteractor.updateAlbumPlaylist(newPlaylist)
            }
            _stateAlbumTrack.postValue(tracks)
        }
    }

    fun deleteAlbum(albumId: Int) {
        viewModelScope.launch {
            albumPlaylistInteractor.deleteAlbumPlaylist(albumId)
        }
    }

    fun shareAlbum(album: AlbumPlaylist) {
        sharingInteractor.shareAlbum(album)
    }
}