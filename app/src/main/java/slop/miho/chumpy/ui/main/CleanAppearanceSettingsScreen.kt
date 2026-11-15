package slop.miho.chumpy.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import slop.miho.chumpy.data.Accent
import slop.miho.chumpy.data.ThemeMode
import slop.miho.chumpy.data.UiSettings
import slop.miho.chumpy.data.UiStyle
import slop.miho.chumpy.ui.wallpapers.ReportBarCoverage

/** Compact reference-aligned appearance page shared by both Kiran visual styles. */
@Composable
fun CleanAppearanceSettingsScreen(
    settings: UiSettings,
    onChange: ((UiSettings) -> UiSettings) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scroll = rememberScrollState()
    ReportBarCoverage(scroll.canScrollForward || scroll.canScrollBackward, underlaid = true)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "Make Kiran feel like yours.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        CleanSettingsCard(title = "Appearance", subtitle = "The visual language used across the app.") {
            Text("Style", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                StyleChip("A · Kiran", UiStyle.KIRAN_A, settings.uiStyle, Modifier.weight(1f)) {
                    onChange { it.copy(uiStyle = UiStyle.KIRAN_A) }
                }
                StyleChip("B · Sarhad", UiStyle.KIRAN_B, settings.uiStyle, Modifier.weight(1f)) {
                    onChange { it.copy(uiStyle = UiStyle.KIRAN_B) }
                }
            }
            Text("Theme", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                ThemeChip("System", ThemeMode.SYSTEM, settings.themeMode, Modifier.weight(1f)) { onChange { it.copy(themeMode = ThemeMode.SYSTEM) } }
                ThemeChip("Light", ThemeMode.LIGHT, settings.themeMode, Modifier.weight(1f)) { onChange { it.copy(themeMode = ThemeMode.LIGHT) } }
                ThemeChip("Dark", ThemeMode.DARK, settings.themeMode, Modifier.weight(1f)) { onChange { it.copy(themeMode = ThemeMode.DARK) } }
            }
            Text("Accent", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                AccentDot(Accent.GREEN, settings.accent) { onChange { it.copy(accent = Accent.GREEN) } }
                AccentDot(Accent.VIOLET, settings.accent) { onChange { it.copy(accent = Accent.VIOLET) } }
                AccentDot(Accent.SKY, settings.accent) { onChange { it.copy(accent = Accent.SKY) } }
                AccentDot(Accent.MONO, settings.accent) { onChange { it.copy(accent = Accent.MONO) } }
            }
        }

        CleanSettingsCard(title = "Behavior", subtitle = "Small choices that affect how the app feels.") {
            CleanToggleRow(
                title = "Haptic feedback",
                supporting = "Vibrate for important connection actions",
                checked = settings.hapticFeedback,
                onCheckedChange = { value -> onChange { it.copy(hapticFeedback = value) } },
            )
            CleanToggleRow(
                title = "Simple transitions",
                supporting = "Use quieter page changes",
                checked = settings.simpleTransitions,
                onCheckedChange = { value -> onChange { it.copy(simpleTransitions = value) } },
            )
            CleanToggleRow(
                title = "Hide addresses",
                supporting = "Mask server addresses in lists and cards",
                checked = settings.maskAddresses,
                onCheckedChange = { value -> onChange { it.copy(maskAddresses = value) } },
            )
        }

        CleanSettingsCard(title = "Next", subtitle = "Connection and app information live in the other Settings pages.") {
            CleanInfoRow("Connection", "Routing, DNS and per-app controls")
            CleanInfoRow("About", "Version, updates and diagnostics")
        }
        Spacer(Modifier.height(88.dp))
    }
}

@Composable
private fun CleanSettingsCard(title: String, subtitle: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(28.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        content()
    }
}

@Composable
private fun StyleChip(label: String, value: UiStyle, selected: UiStyle, modifier: Modifier, onClick: () -> Unit) {
    FilterChip(selected = selected == value, onClick = onClick, label = { Text(label) }, modifier = modifier)
}

@Composable
private fun ThemeChip(label: String, value: ThemeMode, selected: ThemeMode, modifier: Modifier, onClick: () -> Unit) {
    FilterChip(selected = selected == value, onClick = onClick, label = { Text(label) }, modifier = modifier)
}

@Composable
private fun AccentDot(accent: Accent, selected: Accent, onClick: () -> Unit) {
    val color = when (accent) {
        Accent.GREEN -> Color(0xFF79E2AA)
        Accent.VIOLET -> Color(0xFF9B8CFF)
        Accent.SKY -> Color(0xFF77C8F4)
        else -> MaterialTheme.colorScheme.onSurface
    }
    Row(
        modifier = Modifier
            .size(42.dp)
            .background(if (selected == accent) MaterialTheme.colorScheme.surfaceContainerHighest else Color.Transparent, CircleShape)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Spacer(Modifier.size(20.dp).background(color, CircleShape))
    }
}

@Composable
private fun CleanToggleRow(title: String, supporting: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
            Text(supporting, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                uncheckedBorderColor = Color.Transparent,
            ),
        )
    }
}

@Composable
private fun CleanInfoRow(title: String, supporting: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
            Text(supporting, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text("›", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
