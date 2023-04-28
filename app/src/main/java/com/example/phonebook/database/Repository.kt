package com.example.phonebook.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.phonebook.domain.model.ContactModel
import com.example.phonebook.domain.model.ColorModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Repository(
    private val contactDao: ContactDao,
    private val colorDao: ColorDao,
    private val dbMapper: DbMapper
) {

    // Working Notes
    private val contactNotInTrashLiveData: MutableLiveData<List<ContactModel>> by lazy {
        MutableLiveData<List<ContactModel>>()
    }

    fun getAllContactNotInTrash(): LiveData<List<ContactModel>> = contactNotInTrashLiveData

    // Deleted Notes
    private val contactInTrashLiveData: MutableLiveData<List<ContactModel>> by lazy {
        MutableLiveData<List<ContactModel>>()
    }

    fun getAllContactInTrash(): LiveData<List<ContactModel>> = contactInTrashLiveData

    init {
        initDatabase(this::updateContactLiveData)
    }

    /**
     * Populates database with colors if it is empty.
     */
    private fun initDatabase(postInitAction: () -> Unit) {
        GlobalScope.launch {
            // Prepopulate colors
            val colors = ColorDbModel.DEFAULT_COLORS.toTypedArray()
            val dbColors = colorDao.getAllSync()
            if (dbColors.isNullOrEmpty()) {
                colorDao.insertAll(*colors)
            }

            // Prepopulate notes
            val contact = ContactDbModel.DEFAULT_CONTACT.toTypedArray()
            val dbContact = contactDao.getAllSync()
            if (dbContact.isNullOrEmpty()) {
                contactDao.insertAll(*contact)
            }

            postInitAction.invoke()
        }
    }

    // get list of working Contact or deleted Contact
    private fun getAllcontactDependingOnTrashStateSync(inTrash: Boolean): List<ContactModel> {
        val colorDbModels: Map<Long, ColorDbModel> = colorDao.getAllSync().map { it.id to it }.toMap()
        val dbContact: List<ContactDbModel> =
            contactDao.getAllSync().filter { it.isInTrash == inTrash }.sortedBy { it.name }
        return dbMapper.mapContact(dbContact, colorDbModels)
    }

    fun insertContact(contact: ContactModel) {
        contactDao.insert(dbMapper.mapDbContact(contact))
        updateContactLiveData()
    }

    fun deleteContact(contactIds: List<Long>) {
        contactDao.delete(contactIds)
        updateContactLiveData()
    }

    fun moveContactToTrash(contactId: Long) {
        val dbContact = contactDao.findByIdSync(contactId)
        val newDbContact = dbContact.copy(isInTrash = true)
        contactDao.insert(newDbContact)
        updateContactLiveData()
    }

    fun restoreContactFromTrash(contactIds: List<Long>) {
        val dbContactInTrash = contactDao.getContactByIdsSync(contactIds)
        dbContactInTrash.forEach {
            val newDbContact = it.copy(isInTrash = false)
            contactDao.insert(newDbContact)
        }
        updateContactLiveData()
    }

    fun getAllColors(): LiveData<List<ColorModel>> =
        Transformations.map(colorDao.getAll()) { dbMapper.mapColors(it) }

    private fun updateContactLiveData() {
        contactNotInTrashLiveData.postValue(getAllcontactDependingOnTrashStateSync(false))
        contactInTrashLiveData.postValue(getAllcontactDependingOnTrashStateSync(true))
    }
}