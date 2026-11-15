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
import slop.miho.chumpy.data.UiStyle
import slop.miho.chumpy.ui.wallpapers.ReportBarCoverage

/** Style A: the green/teal reference treatment with routing-first settings. */
@Composable
fun KiranStyleASettingsScreen(
    onChange: ((UiSettings) -> UiSettings) -> Unit,
    modifier: Modifier = Modifier,
) {
    var killSwitch by rememberSaveable { mutableStateOf(true) }
    var autoConnect by rememberSaveable { mutableStateOf(false) }
    var bypassLocal by rememberSaveable { mutableStateOf(true) }
    var bypassDomestic by rememberSaveable { mutableStateOf(true) }
    var blockAds by rememberSaveable { mutableStateOf(true) }
    var splitTunneling by rememberSaveable { mutableStateOf(false) }
    val scroll = rememberScrollState()
    ReportBarCoverage(scroll.canScrollForward || scroll.canScrollBackward, underlaid = true)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(horizontal = 20.dp, vertical = 28.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
        )
        ASection("Connection") {
            AToggle("Kill switch", "Block traffic if the tunnel drops", killSwitch) { killSwitch = !killSwitch }
            AToggle("Auto-connect", "Start on untrusted Wi-Fi", autoConnect) { autoConnect = !autoConnect }
        }
        ASection("Routing") {
            AToggle("Bypass local network", "geoip:private → direct", bypassLocal) { bypassLocal = !bypassLocal }
            AToggle("Bypass domestic sites", "geosite:pk → direct", bypassDomestic) { bypassDomestic = !bypassDomestic }
            AToggle("Block ads & trackers", "geosite:category-ads → block", blockAds) { blockAds = !blockAds }
            AValue("Split tunneling", "6 apps bypass VPN", "Manage apps") { splitTunneling = !splitTunneling }
        }
        ASection("Advanced") {
            AValue("DNS", "DoH · 1.1.1.1", "DoH · 1.1.1.1")
            AValue("Fragment & Mux", "Xray core options", "Advanced")
            AValue("Theme", "Colors and mode", "A") {
                onChange { it.copy(uiStyle = UiStyle.KIRAN_B) }
            }
        }
        Spacer(Modifier.height(88.dp))
    }
}

@Composable
private fun ASection(title: String, content: @Composable () -> Unit) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 2.dp),
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(30.dp))
            .padding(horizontal = 16.dp, vertical = 5.dp),
    ) { content() }
}

@Composable
private fun AToggle(title: String, supporting: String, checked: Boolean, onClick: () -> Unit) {
    ARow(title, supporting, onClick) {
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
private fun AValue(title: String, supporting: String, value: String, onClick: (() -> Unit)? = null) {
    ARow(title, supporting, onClick) {
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
private fun ARow(title: String, supporting: String, onClick: (() -> Unit)?, trailing: @Composable () -> Unit) {
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
