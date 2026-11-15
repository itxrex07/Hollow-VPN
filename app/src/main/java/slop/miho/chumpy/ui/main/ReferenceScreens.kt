package slop.miho.chumpy.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import slop.miho.chumpy.core.TunnelState
import slop.miho.chumpy.core.TunnelStatus
import slop.miho.chumpy.data.UiStyle
import slop.miho.chumpy.db.ProfileEntity
import slop.miho.chumpy.ui.HomeUiState
import slop.miho.chumpy.ui.ServersUiState

private val KiranBg = Color(0xFF0E1512)
private val SarhadBg = Color(0xFF0B0B10)
private val KiranSurface = Color(0xFF19211D)
private val SarhadSurface = Color(0xFF17171F)
private val KiranMuted = Color(0xFFA5B5AB)
private val SarhadMuted = Color(0xFFA29DC4)

private fun UiStyle.isReferenceA() = this == UiStyle.KIRAN_A

@Composable
fun ReferenceHomeScreen(
    style: UiStyle,
    status: TunnelStatus,
    home: HomeUiState,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit,
    onOpenServers: () -> Unit,
    onOpenStats: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isA = style.isReferenceA()
    val bg = if (isA) KiranBg else SarhadBg
    val surface = if (isA) KiranSurface else SarhadSurface
    val muted = if (isA) KiranMuted else SarhadMuted
    val accent = MaterialTheme.colorScheme.primary
    val connected = status.state == TunnelState.UP
    val connecting = status.state == TunnelState.CONNECTING
    val profile = home.profileLabel ?: if (isA) "Frankfurt · FRA-02" else "Phoenix-US"
    val protocol = if (isA) "VLESS · Reality · last used" else "VLESS · TCP · REALITY"
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bg)
            .padding(horizontal = if (isA) 20.dp else 14.dp, vertical = if (isA) 28.dp else 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(34.dp).background(accent.copy(alpha = .18f), CircleShape), contentAlignment = Alignment.Center) {
                    Text("✦", color = accent, fontSize = 18.sp)
                }
                Text(if (isA) "Kiran" else "Sarhad", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground)
            }
            Text(
                text = when {
                    connected -> if (isA) "Protected · VLESS · Reality" else "Protected"
                    connecting -> "Connecting…"
                    else -> if (isA) "Not protected" else "Unprotected"
                },
                style = MaterialTheme.typography.labelLarge,
                color = if (connected) accent else muted,
            )
        }
        Spacer(Modifier.height(22.dp))
        Text(
            text = when { connected -> "Connected"; connecting -> "Building tunnel"; else -> "Tap to connect" },
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(16.dp))
        ReferenceConnectButton(style, connected, connecting, if (connected) onDisconnect else onConnect)
        Spacer(Modifier.height(18.dp))
        if (connecting) {
            ReferenceCard(surface) {
                listOf("Resolving server", "Reality handshake", "Starting Xray core…").forEachIndexed { index, label ->
                    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(if (index == 0) "✓" else "·", color = accent, fontSize = 18.sp)
                        Text(label, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
                Text("Securing routes & DNS", color = muted, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))
            }
        } else {
            ReferenceCard(surface, Modifier.clickable(onClick = onOpenServers)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(profile, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Text(if (connected) "reality · tcp · ${home.pingMillis ?: 291} ms" else protocol, color = muted)
                    }
                    Text("Change", color = accent, fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(Modifier.height(10.dp))
            if (connected) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MetricCard("Download", if (isA) "48.2 Mb/s" else "50.7 KB/s", surface, Modifier.weight(1f), onOpenStats)
                    MetricCard("Upload", if (isA) "12.7 Mb/s" else "6.3 KB/s", surface, Modifier.weight(1f), onOpenStats)
                }
            } else {
                ReferenceCard(surface) {
                    Text("Tip", color = accent, fontWeight = FontWeight.Bold)
                    Text("Run the speed test in Servers to pick the fastest profile.", color = muted)
                }
            }
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun ReferenceConnectButton(style: UiStyle, connected: Boolean, connecting: Boolean, onClick: () -> Unit) {
    val isA = style.isReferenceA()
    val shape: Shape = if (isA) RoundedCornerShape(46) else RoundedCornerShape(30.dp)
    val container = when {
        connected -> if (isA) Color(0xFF86E3B0) else Color(0xFFFF8FB6)
        connecting -> if (isA) Color(0xFF5ABF8A) else Color(0xFF8E86FF)
        else -> if (isA) Color(0xFF26352D) else Color(0xFF2A2660)
    }
    Surface(onClick = onClick, shape = shape, color = container, modifier = Modifier.size(if (isA) 190.dp else 160.dp)) {
        Box(contentAlignment = Alignment.Center) {
            Text(when { connected -> "Disconnect"; connecting -> "Cancel"; else -> "Connect" }, color = if (connected && isA) Color(0xFF00391F) else MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
        }
    }
}

@Composable
private fun ReferenceCard(color: Color, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(color = color, shape = RoundedCornerShape(28.dp), modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) { content() }
    }
}

@Composable
private fun MetricCard(label: String, value: String, color: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    ReferenceCard(color, modifier.clickable(onClick = onClick)) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelMedium)
        Text(value, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ReferenceServersScreen(
    style: UiStyle,
    state: ServersUiState,
    onPing: () -> Unit,
    onSelect: (String) -> Unit,
    onAddSubscription: (String, String?, (String?) -> Unit) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isA = style.isReferenceA()
    val bg = if (isA) KiranBg else SarhadBg
    val surface = if (isA) KiranSurface else SarhadSurface
    val muted = if (isA) KiranMuted else SarhadMuted
    var filter by remember { mutableStateOf("All") }
    var addUrl by remember { mutableStateOf("") }
    val profiles = state.profiles.filter { filter == "All" || it.security.contains(filter, true) || it.network.contains(filter, true) }
    Column(modifier.fillMaxSize().background(bg).padding(horizontal = if (isA) 20.dp else 14.dp, vertical = 24.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text("Servers", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground)
                Text("${state.profiles.size} servers", color = muted)
            }
            TextButton(onClick = onPing) { Text("Test all") }
        }
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(value = addUrl, onValueChange = { addUrl = it }, modifier = Modifier.fillMaxWidth(), singleLine = true, placeholder = { Text("Search profiles or paste link vless://") })
        Row(Modifier.padding(vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("All", "Reality", "WS+TLS", "gRPC").forEach { option -> FilterChip(selected = filter == option, onClick = { filter = option }, label = { Text(option) }) }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
            items(profiles, key = { it.id }) { profile ->
                val selected = state.selectedProfileId == profile.id
                Surface(onClick = { onSelect(profile.id) }, color = if (selected) surface.copy(alpha = .92f) else surface, shape = RoundedCornerShape(26.dp), modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(profile.remarks.ifBlank { profile.server.ifBlank { "Unnamed server" } }, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                            Text("${profile.type.name} · ${profile.network} · ${profile.security.ifBlank { "plain" }}", color = muted)
                        }
                        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(profile.testDelayMillis?.let { "$it ms" } ?: "—", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            if (selected) Text("✓ Selected", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
        Button(onClick = { if (addUrl.isNotBlank()) onAddSubscription(addUrl, null) { addUrl = "" } }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary), modifier = Modifier.fillMaxWidth()) { Text("+ Add profile") }
    }
}
