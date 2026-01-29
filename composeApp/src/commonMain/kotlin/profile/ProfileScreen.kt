package profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptions
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.cat_plural
import barksandmeows.composeapp.generated.resources.cat_singular
import barksandmeows.composeapp.generated.resources.delete_account
import barksandmeows.composeapp.generated.resources.delete_account_confirmation_message
import barksandmeows.composeapp.generated.resources.delete_account_confirmation_title
import barksandmeows.composeapp.generated.resources.dog_plural
import barksandmeows.composeapp.generated.resources.dog_singular
import barksandmeows.composeapp.generated.resources.email_label
import barksandmeows.composeapp.generated.resources.logout
import barksandmeows.composeapp.generated.resources.profile_icon_description
import com.app.auth.useCases.LogoutUseCase
import org.jetbrains.compose.resources.stringResource
import com.app.auth.viewModels.ProfileUiState
import com.app.extensions.getPetIcon
import com.app.auth.viewModels.ProfileViewModel
import com.app.dataModels.Pet
import com.app.uicomponents.composables.dialogs.ConfirmationDialog
import com.app.uicomponents.composables.loading.LoadingComposable
import com.app.viewModels.GetPetsUiState
import com.app.viewModels.PetViewModel
import kotlinx.coroutines.launch
import navigation.AppRouteActions
import navigation.NavRouter
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = koinViewModel(),
    petViewModel: PetViewModel = koinViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val profileUiState = profileViewModel.profileUiState.collectAsState()
    val petsUiState = petViewModel.getPetsUiState.collectAsState()

    LaunchedEffect(Unit) {
        petViewModel.getPets()
    }

    when (val state = profileUiState.value) {
        is ProfileUiState.Loading -> {
            LoadingComposable()
        }

        is ProfileUiState.Loaded -> {
            ProfileContent(
                email = state.email,
                displayName = state.displayName,
                petsUiState = petsUiState.value,
                onLogout = {
                    coroutineScope.launch {
                        profileViewModel.logout()
                    }
                },
                onDeleteAccount = {
                    coroutineScope.launch {
                        profileViewModel.deleteAccount()
                    }
                }
            )
        }

        is ProfileUiState.Error -> {
            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect(state.message) {
                snackbarHostState.showSnackbar(state.message)
            }

            ProfileContent(
                email = state.email ?: "Unknown",
                displayName = state.displayName,
                petsUiState = petsUiState.value,
                errorMessage = state.message,
                snackbarHostState = snackbarHostState,
                onLogout = {
                    coroutineScope.launch {
                        profileViewModel.logout()
                    }
                },
                onDeleteAccount = {
                    coroutineScope.launch {
                        profileViewModel.deleteAccount()
                    }
                }
            )
        }

        is ProfileUiState.NotLoggedIn, is ProfileUiState.LoggedOut -> {
            // Clear entire backstack when logging out to remove all screens with active Firestore listeners
            val navOptions = NavOptions.Builder()
                .setPopUpTo(AppRouteActions.HomeScreen.route, inclusive = true)
                .build()
            NavRouter.navigate(AppRouteActions.LoginScreen.route, navOptions)
        }
    }
}

@Composable
fun ProfileContent(
    email: String,
    displayName: String?,
    petsUiState: GetPetsUiState,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
    errorMessage: String? = null,
    snackbarHostState: SnackbarHostState? = null
) {
    val scrollState = rememberScrollState()
    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    val localSnackbarHostState = snackbarHostState ?: remember { SnackbarHostState() }

    if (showDeleteAccountDialog) {
        ConfirmationDialog(
            title = stringResource(Res.string.delete_account_confirmation_title),
            message = stringResource(Res.string.delete_account_confirmation_message),
            onConfirm = {
                showDeleteAccountDialog = false
                onDeleteAccount()
            },
            onDismiss = {
                showDeleteAccountDialog = false
            }
        )
    }

    androidx.compose.material3.Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = localSnackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Scrollable content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 140.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Profile Icon
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = stringResource(Res.string.profile_icon_description),
                    modifier = Modifier.size(120.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Username heading - show displayName or extract from email
                val userName = displayName?.takeIf { it.isNotBlank() }
                    ?: email.substringBefore("@").replaceFirstChar { it.uppercase() }
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Email Card
                EmailCard(email = email)

                Spacer(modifier = Modifier.height(16.dp))

                // Pet Statistics
                when (petsUiState) {
                    is GetPetsUiState.Success -> {
                        if (petsUiState.pets.isNotEmpty()) {
                            PetStatisticsSection(pets = petsUiState.pets)
                        }
                    }
                    is GetPetsUiState.Loading -> {
                        // Optional: Show loading indicator for pets
                    }
                    is GetPetsUiState.Error -> {
                        // Optional: Show error message
                    }
                }
            }

            // Bottom buttons
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {
                // Logout Button
                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(stringResource(Res.string.logout))
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Delete Account Button
                OutlinedButton(
                    onClick = { showDeleteAccountDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(stringResource(Res.string.delete_account))
                }
            }
        }
    }
}

@Composable
private fun EmailCard(email: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = stringResource(Res.string.email_label),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.size(12.dp))
            Column {
                Text(
                    text = stringResource(Res.string.email_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun PetStatisticsSection(pets: List<Pet>) {
    val categoryCount = pets.groupBy { it.petCategory.category.lowercase() }
        .mapValues { it.value.size }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Dogs
        val dogCount = categoryCount["dog"] ?: 0
        if (dogCount > 0) {
            PetStatCard(
                icon = getPetIcon("dog"),
                count = dogCount,
                label = if (dogCount == 1) stringResource(Res.string.dog_singular) else stringResource(Res.string.dog_plural)
            )
        }

        // Cats
        val catCount = categoryCount["cat"] ?: 0
        if (catCount > 0) {
            PetStatCard(
                icon = getPetIcon("cat"),
                count = catCount,
                label = if (catCount == 1) stringResource(Res.string.cat_singular) else stringResource(Res.string.cat_plural)
            )
        }
    }
}

@Composable
private fun PetStatCard(
    icon: org.jetbrains.compose.resources.DrawableResource,
    count: Int,
    label: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = label,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "$count",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}