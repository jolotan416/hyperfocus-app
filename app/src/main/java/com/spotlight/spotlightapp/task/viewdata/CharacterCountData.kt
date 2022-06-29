package com.spotlight.spotlightapp.task.viewdata

data class CharacterCountData(
    val maxCharacterCount: Int,
    var charactersRemaining: Int,
    var willShowCharactersRemaining: Boolean
)