package com.example.phonebook.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import com.example.phonebook.database.ContactDbModel
import com.example.phonebook.domain.model.ContactModel
import com.example.phonebook.routing.Screen
import com.example.phonebook.ui.components.AppDrawer
import com.example.phonebook.ui.components.SearchBar
import com.example.phonebook.ui.components.Contact
import com.example.phonebook.viewmodel.MainViewModel
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalMaterialApi
@Composable
fun ContactScreen(viewModel: MainViewModel, context: Context) {
    val contact by viewModel.contactNotInTrash.observeAsState(listOf())
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val searchBy = remember { mutableStateOf("") }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Contacts",
                        color = MaterialTheme.colors.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch { scaffoldState.drawerState.open() }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = "Drawer Button"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onCreateNewContactClick() }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add Contact Button"
                        )
                    }
                }
            )
        },
        drawerContent = {
            AppDrawer(
                currentScreen = Screen.Contact,
                closeDrawerAction = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onCreateNewContactClick() },
                contentColor = MaterialTheme.colors.background,
                content = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Contact Button"
                    )
                }
            )
        }
    )
    {
        if (contact.isNotEmpty()) {
            Column() {
                SearchBar(searchBy = searchBy)
                ContactList(
                    contact = searchContact(searchBy.value, contact),
                    onContactClick = { viewModel.onContactClick(it) },
                    context = context
                )
            }
        }
    }


}
fun searchContact(searchBy:String,contactList : List<ContactModel>):List<ContactModel>{
    val searched : ArrayList<ContactModel> = ArrayList()
    for(aContact in contactList){
        val searchKey = aContact.name + aContact.tag +aContact.phoneNumber
        if(searchKey.contains(searchBy,ignoreCase = true)){
            searched.add(aContact)
        }
    }
    return searched.toList()
}

@ExperimentalMaterialApi
@Composable
private fun ContactList(
    contact: List<ContactModel>,
    onContactClick: (ContactModel) -> Unit,
    context: Context
) {
    LazyColumn {
        items(count = contact.size) { contactIndex ->
            val aContact = contact[contactIndex]
            Contact(
                contact = aContact,
                onContactClick = onContactClick,
                isSelected = false,
                context = context
            )
        }
    }
}
