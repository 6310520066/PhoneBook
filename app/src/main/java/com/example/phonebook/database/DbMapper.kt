package com.example.phonebook.database

import com.example.phonebook.domain.model.ContactModel
import com.example.phonebook.domain.model.ColorModel
import com.example.phonebook.domain.model.NEW_CONTACT_ID

class DbMapper {
    // Create list of NoteModels by pairing each note with a color
    fun mapContact(
        noteDbModels: List<ContactDbModel>,
        colorDbModels: Map<Long, ColorDbModel>
    ): List<ContactModel> = noteDbModels.map {
        val colorDbModel = colorDbModels[it.colorId]
            ?: throw RuntimeException("Color for colorId: ${it.colorId} was not found. Make sure that all colors are passed to this method")

        mapContact(it, colorDbModel)
    }

    // convert ContactDbModel to ContactModel
    fun mapContact(contactDbModel: ContactDbModel, colorDbModel: ColorDbModel): ContactModel {
        val color = mapColor(colorDbModel)
        return with(contactDbModel) { ContactModel(id, name, phoneNumber,tag, color) }
    }

    // convert list of ColorDdModels to list of ColorModels
    fun mapColors(colorDbModels: List<ColorDbModel>): List<ColorModel> =
        colorDbModels.map { mapColor(it) }

    // convert ColorDbModel to ColorModel
    fun mapColor(colorDbModel: ColorDbModel): ColorModel =
        with(colorDbModel) { ColorModel(id, name, hex) }

    // convert NoteModel back to NoteDbModel
    fun mapDbContact(note: ContactModel): ContactDbModel =
        with(note) {
            if (id == NEW_CONTACT_ID)
                ContactDbModel(
                    name = name,
                    phoneNumber = phoneNumber,
                    tag = tag,
                    colorId = color.id,
                    isInTrash = false
                )
            else
                ContactDbModel(id, name, phoneNumber,tag, color.id, false)
        }
}