package com.example.level5_task_1.ui.theme.screens

import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
fun ProfileScreen(viewModel: ProfileViewModel) {
    val context = LocalContext.current

    viewModel.getProfile()

    val errorMsg by viewModel.errorText.observeAsState()
    val successfullyCreatedQuiz by viewModel.createSuccess.observeAsState()
    val profile by viewModel.profile.observeAsState()

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
                    DisplayImageFromGallery(context, viewModel, profile?.imageUri)
                    Text(
                        text = profile?.firstName.toString() + " " + profile?.lastName.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    )
                    Text(
                        text = profile?.description.toString(),
                        style = MaterialTheme.typography.bodyLarge,
//                    fontStyle = FontStyle.Italic,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    )
                }
            }
            if (!errorMsg.isNullOrEmpty()) {
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                viewModel.reset()
            }
            if (successfullyCreatedQuiz != null) {
                if (successfullyCreatedQuiz!!) {
                    Toast.makeText(
                        context,
                        stringResource(id = R.string.successfully_created_profile),
                        Toast.LENGTH_LONG
                    ).show()
                    viewModel.reset()
                }
            }
        })
}

@Composable
fun DisplayImageFromGallery(
    context: Context,
    viewModel: ProfileViewModel,
    imageUriString: String?
) {

    if (!(imageUriString.isNullOrEmpty() || imageUriString.equals(context.getString(R.string.no_gallery_image)))) {
        val imageUri = Uri.parse(imageUriString) // Conversion String to Uri datatype.
        // https://stackoverflow.com/questions/58903911/how-to-fix-deprecated-issue-in-android-bitmap
        viewModel.bitmap.value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val src = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(src)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
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
}