package profile

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
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
import com.app.ads.composables.AdBanner
import com.app.auth.viewModels.ProfileUiState
import com.app.auth.viewModels.ProfileViewModel
import common.utils.getAppVersion
import com.app.dataModels.Pet
import com.app.extensions.getPetIcon
import com.app.uicomponents.composables.dialogs.ConfirmationDialog
import com.app.uicomponents.composables.loading.LoadingComposable
import com.app.viewModels.GetPetsUiState
import com.app.viewModels.PetViewModel
import kotlinx.coroutines.launch
import navigation.AppRouteActions
import navigation.NavRouter
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
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
            val navOptions = NavOptions.Builder()
                .setPopUpTo(AppRouteActions.HomeScreen.route, inclusive = true)
                .build()
            NavRouter.navigate(AppRouteActions.LoginScreen.route, navOptions)
        }
    }
}

@Suppress("UnusedParameter")
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

    Scaffold(
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            // --- Profile Hero Section ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    tonalElevation = 4.dp,
                    shadowElevation = 8.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = stringResource(Res.string.profile_icon_description),
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display name
                val userName = displayName?.takeIf { it.isNotBlank() }
                    ?: email.substringBefore("@").replaceFirstChar { it.uppercase() }
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Email subtitle
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 24.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // --- Info Section ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Email card
                InfoCard(
                    icon = Icons.Outlined.Email,
                    label = stringResource(Res.string.email_label),
                    value = email
                )

                // Pet Statistics
                when (petsUiState) {
                    is GetPetsUiState.Success -> {
                        if (petsUiState.pets.isNotEmpty()) {
                            PetStatisticsSection(pets = petsUiState.pets)
                        }
                    }
                    is GetPetsUiState.Loading -> {}
                    is GetPetsUiState.Error -> {}
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Ad Banner
            AdBanner(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp))

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 24.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // --- Actions Section ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Logout Button
                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(Res.string.logout),
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                // Delete Account Button
                OutlinedButton(
                    onClick = { showDeleteAccountDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(Res.string.delete_account),
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // App version
            Text(
                text = "v${getAppVersion()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun InfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun PetStatisticsSection(pets: List<Pet>) {
    val categoryCount = pets.groupBy { it.petCategory.category.lowercase() }
        .mapValues { it.value.size }

    // Section header
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Pets,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "My Pets",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
    }

    // Pet stat cards in a row
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val dogCount = categoryCount["dog"] ?: 0
        if (dogCount > 0) {
            PetStatCard(
                icon = getPetIcon("dog"),
                count = dogCount,
                label = if (dogCount == 1) stringResource(Res.string.dog_singular)
                else stringResource(Res.string.dog_plural),
                modifier = Modifier.weight(1f)
            )
        }

        val catCount = categoryCount["cat"] ?: 0
        if (catCount > 0) {
            PetStatCard(
                icon = getPetIcon("cat"),
                count = catCount,
                label = if (catCount == 1) stringResource(Res.string.cat_singular)
                else stringResource(Res.string.cat_plural),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PetStatCard(
    icon: org.jetbrains.compose.resources.DrawableResource,
    count: Int,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = label,
                modifier = Modifier.size(44.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "$count",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
