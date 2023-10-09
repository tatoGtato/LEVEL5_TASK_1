package com.example.level5_task_1.ui.theme.screens

import android.content.Context
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.level5_task_1.ProfileViewModel
import com.example.level5_task_1.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProfileScreen(navController: NavController, viewModel: ProfileViewModel) {

    val context = LocalContext.current

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var profileDescription by remember { mutableStateOf("") }

    Scaffold(
        content = { innerPadding ->
            Modifier.padding(innerPadding)
            Column(
                modifier = Modifier
                    .padding(top = 80.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    PickImageFromGallery(context, viewModel)
                    TextField(
                        value = firstName,
                        // below line is used to add placeholder ("hint") for our text field.
                        placeholder = {
                            Text(text = stringResource(id = R.string.first_name_hint))
                        },
                        onValueChange = { firstName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = colorScheme.background, // Same color as background
                            focusedIndicatorColor = colorScheme.secondary, // This is green underlined
                            unfocusedIndicatorColor = Color.Gray, // Grey underlined
                            focusedLabelColor = colorScheme.secondary,
                            unfocusedLabelColor = Color.Gray
                        ),
                        label = {
                            Text(
                                text = stringResource(R.string.first_name),
                            )
                        }
                    )
                    TextField(
                        value = lastName,
                        // below line is used to add placeholder ("hint") for our text field.
                        placeholder = {
                            Text(text = stringResource(id = R.string.last_name_hint))
                        },
                        onValueChange = { lastName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = colorScheme.background, // Same color as background
                            focusedIndicatorColor = colorScheme.secondary, // This is green underlined
                            unfocusedIndicatorColor = Color.Gray, // Grey underlined
                            focusedLabelColor = colorScheme.secondary,
                            unfocusedLabelColor = Color.Gray
                        ),
                        label = {
                            Text(
                                text = stringResource(R.string.last_name),
                            )
                        }
                    )
                    TextField(
                        value = profileDescription,
                        // below line is used to add placeholder ("hint") for our text field.
                        placeholder = {
                            Text(text = stringResource(id = R.string.description_hint))
                        },
                        onValueChange = { profileDescription = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = colorScheme.background, // Same color as background
                            focusedIndicatorColor = colorScheme.secondary, // This is green underlined
                            unfocusedIndicatorColor = Color.Gray, // Grey underlined
                            focusedLabelColor = colorScheme.secondary,
                            unfocusedLabelColor = Color.Gray
                        ),
                        label = {
                            Text(
                                text = stringResource(R.string.description),
                            )
                        }
                    )
                }
                Row(
                    modifier = Modifier
                        .weight(1f, false)
                ) {

                    Button(modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = colorScheme.onPrimary,
                            containerColor = colorScheme.secondary
//                            backgroundColor = colors.onBackground,
                        ),
                        onClick = {
                            // CREATE PROFILE IF INPUT NOT EMPTY
                            if (firstName.isEmpty() || lastName.isEmpty() || profileDescription.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.fields_must_not_be_empty),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val uriString: String
                                if (viewModel.imageUri == null) {
                                    uriString = context.getString(R.string.no_gallery_image)
                                } else {
                                    uriString = viewModel.imageUri.toString()
                                }
                                viewModel.reset()
                                viewModel.createProfile(
                                    firstName = firstName,
                                    lastName = lastName,
                                    description = profileDescription,
                                    imageUri = uriString
                                )
                                firstName = ""
                                lastName = ""
                                profileDescription = ""
                                navController.navigate(ProfileScreens.ProfileScreen.route)
                            }
                        }) {
                        Text(text = context.getString(R.string.confirm).uppercase())
                    }
                }
            }
        })
}

@Composable
fun PickImageFromGallery(context: Context, viewModel: ProfileViewModel) {

    // https://www.howtodoandroid.com/pick-image-from-gallery-jetpack-compose/
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.imageUri = uri
    }

    if (viewModel.imageUri != null) {
        // https://stackoverflow.com/questions/58903911/how-to-fix-deprecated-issue-in-android-bitmap
        viewModel.bitmap.value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val src = ImageDecoder.createSource(context.contentResolver, viewModel.imageUri!!)
            ImageDecoder.decodeBitmap(src)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, viewModel.imageUri)
        }
        viewModel.bitmap.value?.let { btm ->
            Image(
                bitmap = btm.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 0.dp, vertical = 40.dp)
                    .width(128.dp)
                    .height(128.dp)
            )
        }
    } else {
        Image(
            painter = painterResource(id = R.drawable.ic_account_box_black),
            contentDescription = "Logo",
            modifier = Modifier
                .padding(horizontal = 0.dp, vertical = 40.dp)
                .width(128.dp)
                .height(128.dp)
        )
    }
    Button(
        // https://www.howtodoandroid.com/pick-image-from-gallery-jetpack-compose/
        onClick = { launcher.launch("image/*") },
        colors = ButtonDefaults.buttonColors(
            contentColor = colorScheme.onPrimary,
            containerColor = colorScheme.secondary
        )
    )
    {
        Text(text = context.getString(R.string.open_picture_gallery).uppercase())
    }
}