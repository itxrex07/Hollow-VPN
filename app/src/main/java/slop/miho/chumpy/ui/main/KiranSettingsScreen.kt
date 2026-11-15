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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import slop.miho.chumpy.data.UiSettings
import slop.miho.chumpy.ui.wallpapers.ReportBarCoverage

/**
 * The compact Kiran settings treatment from the supplied reference: large title,
 * tonal grouped lists, expressive violet chips, and switch rows with generous touch targets.
 */
@Composable
fun KiranSettingsScreen(
    onChange: ((UiSettings) -> UiSettings) -> Unit,
    modifier: Modifier = Modifier,
) {
    var killSwitch by rememberSaveable { mutableStateOf(true) }
    var autoConnect by rememberSaveable { mutableStateOf(false) }
    var startOnBoot by rememberSaveable { mutableStateOf(false) }
    var bypassLocal by rememberSaveable { mutableStateOf(true) }
    var bypassDomestic by rememberSaveable { mutableStateOf(true) }
    var splitTunneling by rememberSaveable { mutableStateOf(false) }
    val scroll = rememberScrollState()
    ReportBarCoverage(scroll.canScrollForward || scroll.canScrollBackward, underlaid = true)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(horizontal = 14.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
        )
        KiranSection("Connection") {
            KiranToggle("Kill switch", "Block traffic if tunnel drops", killSwitch) { killSwitch = !killSwitch }
            KiranToggle("Auto-connect", "Start on untrusted Wi-Fi", autoConnect) { autoConnect = !autoConnect }
            KiranToggle("Start on boot", "Open when device starts", startOnBoot) { startOnBoot = !startOnBoot }
        }
        KiranSection("Routing") {
            KiranToggle("Bypass local network", "geoip:private", bypassLocal) { bypassLocal = !bypassLocal }
            KiranToggle("Bypass domestic sites", "geosite:pk", bypassDomestic) { bypassDomestic = !bypassDomestic }
            KiranValueRow("Split tunneling", "Choose apps", "6 apps") { splitTunneling = !splitTunneling }
        }
        KiranSection("Advanced") {
            KiranValueRow("DNS", "Secure DNS", "DoH · 1.1.1.1")
            KiranValueRow("Fragment & Mux", "Xray core options", "Edit")
            KiranValueRow("Style", "Choose appearance", "B") {
                onChange { it.copy(uiStyle = slop.miho.chumpy.data.UiStyle.KIRAN_A) }
            }
        }
        Spacer(Modifier.height(88.dp))
    }
}

@Composable
private fun KiranSection(title: String, content: @Composable () -> Unit) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 2.dp),
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(28.dp))
            .padding(horizontal = 16.dp, vertical = 4.dp),
    ) { content() }
}

@Composable
private fun KiranToggle(title: String, supporting: String, checked: Boolean, onClick: () -> Unit) {
    KiranRow(title, supporting, onClick) {
        Switch(
            checked = checked,
            onCheckedChange = { onClick() },
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
private fun KiranValueRow(title: String, supporting: String, value: String, onClick: (() -> Unit)? = null) {
    KiranRow(title, supporting, onClick) {
        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary, CircleShape)
                .padding(horizontal = 12.dp, vertical = 6.dp),
        )
    }
}

@Composable
private fun KiranRow(title: String, supporting: String, onClick: (() -> Unit)?, trailing: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
            Text(supporting, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        trailing()
    }
}
