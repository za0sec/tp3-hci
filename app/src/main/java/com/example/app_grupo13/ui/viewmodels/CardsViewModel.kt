package com.example.app_grupo13.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_grupo13.data.model.Card
import com.example.app_grupo13.data.repository.CardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardsViewModel(
    private val repository: CardRepository
) : ViewModel() {
    
    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadCards() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                repository.getCards()?.let { cardList ->
                    _cards.value = cardList
                } ?: run {
                    _error.value = "Error al cargar las tarjetas"
                }
            } catch (e: Exception) {
                Log.e("CardsViewModel", "Error loading cards", e)
                _error.value = e.message ?: "Error desconocido"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    suspend fun addCard(card: Card): Boolean {
        return try {
            _isLoading.value = true
            _error.value = null
            repository.addCard(card)?.let { newCard ->
                _cards.value = _cards.value + newCard
                true
            } ?: false
        } catch (e: Exception) {
            Log.e("CardsViewModel", "Error adding card", e)
            _error.value = e.message ?: "Error al agregar la tarjeta"
            false
        } finally {
            _isLoading.value = false
        }
    }
    
    suspend fun deleteCard(cardId: Int): Boolean {
        return try {
            _isLoading.value = true
            _error.value = null
            if (repository.deleteCard(cardId)) {
                _cards.value = _cards.value.filter { it.id != cardId }
                true
            } else {
                _error.value = "No se pudo eliminar la tarjeta"
                false
            }
        } catch (e: Exception) {
            Log.e("CardsViewModel", "Error deleting card", e)
            _error.value = e.message ?: "Error al eliminar la tarjeta"
            false
        } finally {
            _isLoading.value = false
        }
    }
} 