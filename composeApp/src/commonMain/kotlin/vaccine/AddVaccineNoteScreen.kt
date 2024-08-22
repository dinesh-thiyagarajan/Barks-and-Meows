package vaccine

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.add_vaccine_note
import com.dineshworkspace.uicomponents.composables.appBar.BarksAndMeowsAppBar
import com.dineshworkspace.uicomponents.composables.loading.LoadingComposable
import com.dineshworkspace.uicomponents.composables.textFields.GenericInputTextFieldComposable
import com.dineshworkspace.vaccine.dataModels.VaccineNote
import com.dineshworkspace.vaccine.viewModels.AddVaccineNoteUiState
import com.dineshworkspace.vaccine.viewModels.VaccineNoteViewModel
import common.utils.generateUUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import navigation.NavRouter
import navigation.NavRouter.getNavController
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun AddVaccineNoteScreen(
    petId: String,
    vaccineNoteViewModel: VaccineNoteViewModel = koinViewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = {
        BarksAndMeowsAppBar(
            canNavigateBack = getNavController()?.previousBackStackEntry != null,
            navigateUp = { getNavController()?.navigateUp() }
        )
    }) { innerPadding ->

        val addVaccineNoteUiState = vaccineNoteViewModel.addVaccineNoteUiState.collectAsState()

        when (addVaccineNoteUiState.value) {
            is AddVaccineNoteUiState.Loading -> {
                LoadingComposable()
            }

            is AddVaccineNoteUiState.NotStarted -> {
                AddNewVaccineNoteComposable(
                    petId = petId,
                    vaccineNoteViewModel = vaccineNoteViewModel,
                    innerPadding = innerPadding,
                    coroutineScope = coroutineScope
                )
            }

            is AddVaccineNoteUiState.Success -> {
                NavRouter.popBackStack()
            }

            is AddVaccineNoteUiState.Error -> {}
        }


    }
}

@Composable
internal fun AddNewVaccineNoteComposable(
    petId: String,
    vaccineNoteViewModel: VaccineNoteViewModel,
    innerPadding: PaddingValues,
    coroutineScope: CoroutineScope
) {

    var vaccineName by remember { mutableStateOf("") }
    var dateTimeStamp by remember { mutableStateOf("") }
    var injectedByDoctor by remember { mutableStateOf("") }
    lateinit var selectedVaccine: com.dineshworkspace.vaccine.dataModels.Vaccine

    Column(modifier = Modifier.padding(paddingValues = innerPadding)) {
        GenericInputTextFieldComposable(
            textFieldValue = vaccineName,
            onValueChange = { vaccineName = it },
            label = { Text("") },
            modifier = Modifier,
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                coroutineScope.launch {
                    vaccineNoteViewModel.addVaccineNote(
                        vaccineNote = VaccineNote(
                            id = generateUUID(),
                            petId = petId,
                            vaccine = selectedVaccine,
                            note = "",
                            timestamp = ""
                        )
                    )
                }
            },
            enabled = vaccineName.isNotEmpty(),
            modifier = Modifier.wrapContentSize()
        ) {
            Text(stringResource(Res.string.add_vaccine_note))
        }
    }
}