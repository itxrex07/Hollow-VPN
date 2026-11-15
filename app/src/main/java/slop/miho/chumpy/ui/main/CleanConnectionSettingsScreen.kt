package slop.miho.chumpy.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import slop.miho.chumpy.data.ConnectionSettings
import slop.miho.chumpy.data.UiSettings
import slop.miho.chumpy.ui.wallpapers.ReportBarCoverage
import slop.miho.chumpy.xray.RoutingMode

@Composable
fun CleanConnectionSettingsScreen(
    connection: ConnectionSettings,
    settings: UiSettings,
    onChangeConnection: ((ConnectionSettings) -> ConnectionSettings) -> Unit,
    onChangeSettings: ((UiSettings) -> UiSettings) -> Unit,
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
            Text("Routing", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground)
            Text("Choose what the tunnel carries.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        ConnectionCard("Rules", "Applied by the Xray core.") {
            Text("Routing mode", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                RoutingChip("Global", RoutingMode.GLOBAL, connection.routingMode, Modifier.weight(1f)) { onChangeConnection { it.copy(routingMode = RoutingMode.GLOBAL) } }
                RoutingChip("Bypass RU", RoutingMode.BYPASS_RU, connection.routingMode, Modifier.weight(1f)) { onChangeConnection { it.copy(routingMode = RoutingMode.BYPASS_RU) } }
                RoutingChip("Bypass CN", RoutingMode.BYPASS_CN, connection.routingMode, Modifier.weight(1f)) { onChangeConnection { it.copy(routingMode = RoutingMode.BYPASS_CN) } }
            }
            CleanConnectionToggle("Bypass local network", "geoip:private → direct", connection.bypassLan) { value -> onChangeConnection { it.copy(bypassLan = value) } }
            CleanConnectionToggle("Auto-reconnect", "Restore the tunnel after a service restart", connection.autoReconnect) { value -> onChangeConnection { it.copy(autoReconnect = value) } }
        }
        ConnectionCard("Privacy", "Keep the important controls close at hand.") {
            CleanConnectionToggle("Hide addresses", "Mask server addresses in cards and lists", settings.maskAddresses) { value -> onChangeSettings { it.copy(maskAddresses = value) } }
            CleanConnectionToggle("Per-app proxy", "Use the selected application bypass list", connection.perAppEnabled) { value -> onChangeConnection { it.copy(perAppEnabled = value) } }
        }
        ConnectionCard("Advanced", "These changes are applied the next time the tunnel starts.") {
            ConnectionInfoRow("DNS", "Secure DNS is managed by the active profile")
            ConnectionInfoRow("Split tunneling", "Manage excluded apps from the full connection tools")
            ConnectionInfoRow("Test URL", connection.delayTestUrl)
        }
        Spacer(Modifier.height(88.dp))
    }
}

@Composable
private fun ConnectionCard(title: String, subtitle: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(28.dp)).padding(18.dp),
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
private fun RoutingChip(label: String, value: RoutingMode, selected: RoutingMode, modifier: Modifier, onClick: () -> Unit) {
    FilterChip(selected = selected == value, onClick = onClick, label = { Text(label) }, modifier = modifier)
}

@Composable
private fun CleanConnectionToggle(title: String, supporting: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
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
private fun ConnectionInfoRow(title: String, supporting: String) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
        Text(supporting, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
