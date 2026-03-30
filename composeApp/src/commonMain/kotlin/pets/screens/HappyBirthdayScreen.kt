package pets.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.happy_birthday_message
import barksandmeows.composeapp.generated.resources.happy_birthday_title
import navigation.NavRouter
import org.jetbrains.compose.resources.stringResource
import kotlin.random.Random

@Composable
fun HappyBirthdayScreen(petName: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "birthday")

    val popperRotation by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "popperRotation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF9C4),
                        Color(0xFFFFE0B2),
                        Color(0xFFF8BBD0)
                    )
                )
            )
    ) {
        ConfettiCanvas(infiniteTransition = infiniteTransition)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.2f))

            Text(
                text = "🎉 🎊 🎉",
                fontSize = 48.sp,
                modifier = Modifier.rotate(popperRotation)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(Res.string.happy_birthday_title),
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 42.sp
                ),
                color = Color(0xFFE91E63),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.happy_birthday_message, petName),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF6A1B9A),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "🐾 Wishing you the best day ever! 🐾",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF795548),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(0.4f))

            Button(
                onClick = { NavRouter.popBackStack() },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE91E63)
                ),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(52.dp)
            ) {
                Text(
                    text = "Close",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.weight(0.1f))
        }
    }
}

@Composable
private fun ConfettiCanvas(infiniteTransition: InfiniteTransition) {
    val confettiColors = listOf(
        Color(0xFFE91E63), Color(0xFF9C27B0), Color(0xFF2196F3),
        Color(0xFF4CAF50), Color(0xFFFF9800), Color(0xFFFFEB3B)
    )

    val particles = remember {
        List(60) {
            ConfettiParticle(
                xFraction = Random.nextFloat(),
                size = Random.nextFloat() * 10f + 4f,
                color = confettiColors[Random.nextInt(confettiColors.size)],
                speedOffset = Random.nextFloat(),
                rotationOffset = Random.nextFloat() * 360f,
                isRect = Random.nextBoolean()
            )
        }
    }

    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "confettiProgress"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            val adjustedProgress = (progress + particle.speedOffset) % 1f
            val x = particle.xFraction * size.width
            val y = adjustedProgress * (size.height + 80f) - 40f
            val rotation = adjustedProgress * 360f + particle.rotationOffset

            withTransform({
                translate(x, y)
                rotate(rotation, Offset.Zero)
            }) {
                if (particle.isRect) {
                    drawRect(
                        color = particle.color,
                        size = Size(particle.size, particle.size * 0.5f),
                        topLeft = Offset(-particle.size / 2f, -particle.size / 4f)
                    )
                } else {
                    drawCircle(
                        color = particle.color,
                        radius = particle.size / 2f,
                        center = Offset.Zero
                    )
                }
            }
        }
    }
}

private data class ConfettiParticle(
    val xFraction: Float,
    val size: Float,
    val color: Color,
    val speedOffset: Float,
    val rotationOffset: Float,
    val isRect: Boolean
)
