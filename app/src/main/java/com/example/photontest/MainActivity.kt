package com.example.photontest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.photontest.ui.theme.PhotonTestTheme

class MainActivity : ComponentActivity() {

    private val viewModelNYC: NYCHighSchoolsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelNYC.fetch()

        setContent {
            PhotonTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StartScreen(vm = viewModelNYC)
                }
            }
        }
    }
}

@Composable
fun StartScreen(vm: NYCHighSchoolsViewModel) {

    var listOfHighSchools = vm.highSchoolLiveData.observeAsState()

    var shouldDisplayDescription by rememberSaveable {
        mutableStateOf(false)
    }
    var schoolClicked by rememberSaveable {
        mutableStateOf("")
    }

    if (!shouldDisplayDescription) {
        LazyColumn() {
            items(listOfHighSchools.value ?: emptyList()) {
                HighSchool(
                    schoolName = it.schoolName ?: "",
                    dbnName = it.dbn ?: "",
                    onClick = { schoolCode ->
                        schoolClicked = schoolCode
                        shouldDisplayDescription = true
                    }
                )
            }
        }
    } else {
            if (!schoolClicked.isNullOrBlank()) {
                var schoolDescription = listOfHighSchools.value?.find {
                    it.schoolName == schoolClicked
                }
                Card(
                    Modifier
                        .fillMaxSize()
                        .padding(8.dp)) {
                    Button(
                        onClick = {
                            shouldDisplayDescription = false
                            schoolClicked = ""
                        }
                    ) {
                        Text(text = "Go back")
                    }
                    if (schoolDescription == null) {
                        Text("ERROR RETRIEVING HIGH SCHOOL CLICKED")
                    } else {
                        Text(text = schoolDescription.overviewParagraph ?: "")
                }
            }
        }
    }
}

@Composable
fun HighSchool(schoolName: String, dbnName: String, onClick : (String) -> Unit) {
    Card(modifier = Modifier
        .padding(4.dp)
        .clickable {
            onClick(schoolName)
        }) {
        Text(text = schoolName, modifier = Modifier.padding(4.dp))
        Text(text = dbnName, modifier = Modifier.padding(4.dp))
    }
}

