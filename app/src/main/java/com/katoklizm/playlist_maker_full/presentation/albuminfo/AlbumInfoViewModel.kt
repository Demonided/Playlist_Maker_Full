package com.katoklizm.playlist_maker_full.presentation.albuminfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.katoklizm.playlist_maker_full.domain.album.AlbumPlaylistInteractor
import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist
import com.katoklizm.playlist_maker_full.domain.albuminfo.AlbumInfoInteractor
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.domain.sharing.SharingInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.lang.reflect.Type

class AlbumInfoViewModel(
    private val albumPlaylistInteractor: AlbumPlaylistInteractor,
    private val albumInfoInteractor: AlbumInfoInteractor,
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
        viewModelScope.launch {
            albumInfoInteractor.getPlaylist()
                .filterNotNull()
                .collect {
                    val gson = Gson()
                    val trackListType = object : TypeToken<List<Track>>() {}.type
                    val track: List<Track> = gson.fromJson(it.track, trackListType) ?: emptyList()
                    _stateAlbum.postValue(it)
                    _stateAlbumTrack.postValue(track)
                }
        }
    }

    fun getAlbumPlaylist() {
        albumInfoInteractor.getPlaylist()
    }

    fun loadTrackList(selectedAlbum: AlbumPlaylist?) {
        selectedAlbum?.track?.let { trackString ->
            val gson = Gson()
            val trackListType = object : TypeToken<List<Track>>() {}.type
            val tracks: List<Track> = gson.fromJson(trackString, trackListType) ?: emptyList()
            _stateAlbumTrack.postValue(tracks)
        }
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
                albumInfoInteractor.setPlaylist(newPlaylist)
                albumInfoInteractor.getPlaylist()
                    .collect() {
                        _stateAlbum.postValue(it)
                    }
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