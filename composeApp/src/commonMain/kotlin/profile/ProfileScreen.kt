package profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptions
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.cancel
import barksandmeows.composeapp.generated.resources.cat_plural
import barksandmeows.composeapp.generated.resources.cat_singular
import barksandmeows.composeapp.generated.resources.dog_plural
import barksandmeows.composeapp.generated.resources.dog_singular
import barksandmeows.composeapp.generated.resources.email_label
import barksandmeows.composeapp.generated.resources.logout
import barksandmeows.composeapp.generated.resources.profile
import barksandmeows.composeapp.generated.resources.profile_icon_description
import org.jetbrains.compose.resources.stringResource
import com.dineshworkspace.auth.viewModels.ProfileUiState
import com.dineshworkspace.extensions.getPetIcon
import com.dineshworkspace.auth.viewModels.ProfileViewModel
import com.dineshworkspace.dataModels.Pet
import com.dineshworkspace.uicomponents.composables.loading.LoadingComposable
import com.dineshworkspace.viewModels.GetPetsUiState
import com.dineshworkspace.viewModels.PetViewModel
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
                petsUiState = petsUiState.value,
                onLogout = {
                    coroutineScope.launch {
                        profileViewModel.logout()
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
    petsUiState: GetPetsUiState,
    onLogout: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
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

        Text(
            text = stringResource(Res.string.profile),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Email Card
        EmailCard(email = email)

        Spacer(modifier = Modifier.height(16.dp))

        // Pet Statistics - Just the category cards
        when (petsUiState) {
            is GetPetsUiState.Success -> {
                if (petsUiState.pets.isNotEmpty()) {
                    PetCategoryCards(pets = petsUiState.pets)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            is GetPetsUiState.Loading -> {
                // Optional: Show loading indicator for pets
            }
            is GetPetsUiState.Error -> {
                // Optional: Show error message
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(32.dp))
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
private fun PetCategoryCards(pets: List<Pet>) {
    val categoryCount = pets.groupBy { it.petCategory.category.lowercase() }
        .mapValues { it.value.size }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Dogs
        val dogCount = categoryCount["dog"] ?: 0
        if (dogCount > 0) {
            PetCategoryItem(
                icon = getPetIcon("dog"),
                count = dogCount,
                label = if (dogCount == 1) stringResource(Res.string.dog_singular) else stringResource(Res.string.dog_plural)
            )
        }

        // Cats
        val catCount = categoryCount["cat"] ?: 0
        if (catCount > 0) {
            PetCategoryItem(
                icon = getPetIcon("cat"),
                count = catCount,
                label = if (catCount == 1) stringResource(Res.string.cat_singular) else stringResource(Res.string.cat_plural)
            )
        }
    }
}

@Composable
private fun PetCategoryItem(
    icon: org.jetbrains.compose.resources.DrawableResource,
    count: Int,
    label: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = label,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$count",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}