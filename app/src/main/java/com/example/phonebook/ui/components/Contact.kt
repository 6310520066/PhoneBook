package com.example.phonebook.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.phonebook.domain.model.ContactModel
import com.example.phonebook.utils.fromHex


@ExperimentalMaterialApi
@Composable
fun Contact(
    modifier: Modifier = Modifier,
    contact : ContactModel,
    onContactClick: (ContactModel) -> Unit = {},
    isSelected: Boolean,
    context: Context
) {
    val background = if (isSelected)
        Color.LightGray
    else
        MaterialTheme.colors.surface

    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        backgroundColor = background
    ) {
        ListItem(
            text = { Text(text = contact.name + " ("+contact.tag+")", maxLines = 1) },
            secondaryText = {
                Text(text = contact.phoneNumber, maxLines = 1)
            },
            icon = {
                ContactIcon(
                    color = Color.fromHex(contact.color.hex),
                    size = 40.dp,
                    border = 1.dp
                )
            },
            trailing = {
                IconButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:${contact.phoneNumber}")
                        context.startActivity(intent)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call",
                    )
                }

            },
            modifier = Modifier.clickable {
                onContactClick.invoke(contact)
            }
        )
    }
}
