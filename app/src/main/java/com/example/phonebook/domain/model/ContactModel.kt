package com.example.phonebook.domain.model


const val NEW_CONTACT_ID = -1L

data class ContactModel(
    val id: Long = NEW_CONTACT_ID,
    val name: String = "",
    val phoneNumber: String = "",
    val tag: String = "",
    val color: ColorModel = ColorModel.DEFAULT
)