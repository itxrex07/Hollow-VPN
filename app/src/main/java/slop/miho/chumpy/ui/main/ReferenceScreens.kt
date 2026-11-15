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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import slop.miho.chumpy.data.ServersSort
import slop.miho.chumpy.data.UiSettings
import slop.miho.chumpy.data.UiStyle
import slop.miho.chumpy.db.ProfileEntity
import slop.miho.chumpy.ui.HomeUiState
import slop.miho.chumpy.ui.ServersUiState

private val KiranBg = Color(0xFF0E1512)
private val SarhadBg = Color(0xFF0B0B10)
private val KiranSurface = Color(0xFF19211D)
private val SarhadSurface = Color(0xFF17171F)

private fun UiStyle.isReferenceA() = this == UiStyle.KIRAN_A

@Composable
fun ReferenceHomeScreen(
    style: UiStyle,
    status: TunnelStatus,
    home: HomeUiState,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit,
    onOpenServers: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isA = style.isReferenceA()
    val bg = if (isA) KiranBg else SarhadBg
    val surface = if (isA) KiranSurface else SarhadSurface
    val primary = MaterialTheme.colorScheme.primary
    val connected = status.state == TunnelState.UP
    val connecting = status.state == TunnelState.CONNECTING
    val title = if (isA) "Kiran" else "Sarhad"
    val profile = home.profileLabel ?: if (isA) "Frankfurt · FRA-02" else "Phoenix-US"
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bg)
            .padding(horizontal = if (isA) 20.dp else 14.dp, vertical = if (isA) 28.dp else 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground)
            if (!isA) Text("•••", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 20.sp)
        }
        Spacer(Modifier.height(18.dp))
        Text(
            text = when {
                connected -> if (isA) "Protected · VLESS · Reality" else "Protected"
                connecting -> if (isA) "Connecting…" else "Connecting"
                else -> if (isA) "Not protected" else "Not connected"
            },
            style = MaterialTheme.typography.labelLarge,
            color = if (connected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = when {
                connected -> "Connected"
                connecting -> if (isA) "Building tunnel" else "Tap to cancel"
                else -> "Tap to connect"
            },
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(16.dp))
        ReferenceConnectButton(
            style = style,
            connected = connected,
            connecting = connecting,
            onClick = if (connected) onDisconnect else onConnect,
        )
        Spacer(Modifier.height(18.dp))
        if (connecting) {
            ReferenceCard(surface) {
                listOf("Resolving server", "Reality handshake", "Starting Xray core…", "Securing routes & DNS").forEachIndexed { index, label ->
                    Row(Modifier.fillMaxWidth().padding(vertical = 7.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(if (index == 0) "✓" else "·", color = primary, fontSize = 18.sp)
                        Text(label, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        } else {
            ReferenceCard(surface, Modifier.clickable(onClick = onOpenServers)) {
                Text(profile, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(if (connected) "reality · tcp · ${home.pingMillis ?: 291} ms" else if (isA) "VLESS · Reality · last used" else "Tap to change server", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.height(10.dp))
            if (connected) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MetricCard("Download", if (isA) "48.2 Mb/s" else "50.7 KB/s", surface, Modifier.weight(1f))
                    MetricCard("Upload", if (isA) "12.7 Mb/s" else "6.3 KB/s", surface, Modifier.weight(1f))
                }
            } else {
                ReferenceCard(surface) {
                    Text(if (isA) "Your traffic is visible to your network" else "Download  0 B/s   ·   Upload  0 B/s", color = MaterialTheme.colorScheme.onSurfaceVariant)
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
    Surface(
        onClick = onClick,
        shape = shape,
        color = container,
        modifier = Modifier.size(if (isA) 190.dp else 160.dp),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = when { connected -> "Disconnect"; connecting -> "Cancel"; else -> "Connect" },
                color = if (connected && isA) Color(0xFF00391F) else MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
            )
        }
    }
}

@Composable
private fun ReferenceCard(color: Color, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(color = color, shape = RoundedCornerShape(28.dp), modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp), content = content)
    }
}

@Composable
private fun MetricCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    ReferenceCard(color, modifier) {
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
    var filter by remember { mutableStateOf("All") }
    val profiles = state.profiles.filter { filter == "All" || it.security.equals(filter, true) || it.network.equals(filter, true) }
    Column(Modifier.fillMaxSize().background(bg).padding(horizontal = 14.dp, vertical = 24.dp)) {
        Text("Servers", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground)
        Spacer(Modifier.height(12.dp))
        Surface(color = surface, shape = RoundedCornerShape(26.dp), modifier = Modifier.fillMaxWidth()) {
            Text("Search profiles or paste link", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(16.dp))
        }
        Row(Modifier.padding(vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("All", "Reality", "WS+TLS", "gRPC").forEach { option ->
                FilterChip(selected = filter == option, onClick = { filter = option }, label = { Text(option) })
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
            items(profiles, key = { it.id }) { profile ->
                Surface(
                    onClick = { onSelect(profile.id) },
                    color = surface,
                    shape = RoundedCornerShape(26.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(profile.remarks.ifBlank { profile.server.ifBlank { "Unnamed server" } }, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                            Text("${profile.type.name} · ${profile.network} · ${profile.security.ifBlank { "plain" }}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(profile.testDelayMillis?.let { "$it ms" } ?: "—", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Button(onClick = { onPing() }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary), modifier = Modifier.fillMaxWidth()) { Text("Test all") }
    }
}
