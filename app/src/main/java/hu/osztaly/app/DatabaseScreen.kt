package hu.osztaly.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DatabaseScreen(repo: Repo, modifier: Modifier = Modifier) {
    var delIdx by remember { mutableStateOf(-1) }

    if (delIdx >= 0) {
        ConfirmDialog(
            "Törlöd ezt a rögzítést?",
            onYes = { repo.removeRecord(delIdx); delIdx = -1 },
            onNo = { delIdx = -1 }
        )
    }

    if (repo.records.isEmpty()) {
        Box(modifier.fillMaxSize().padding(16.dp)) {
            Text("Még nincs rögzített adat.")
        }
    } else {
        LazyColumn(
            modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(repo.records.indices.reversed().toList()) { idx ->
                val rec = repo.records.getOrNull(idx) ?: return@items
                var open by remember(rec) { mutableStateOf(false) }
                val plus = rec.entries.count { it.value }
                val minus = rec.entries.size - plus

                Card(
                    Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Column(Modifier.fillMaxWidth().clickable { open = !open }) {
                            Text(rec.topic, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text("${rec.date}   ·   + $plus / − $minus", fontSize = 14.sp)
                        }
                        if (open) {
                            Column(Modifier.padding(top = 8.dp)) {
                                rec.entries.forEach { e ->
                                    Row(
                                        Modifier.fillMaxWidth().padding(vertical = 3.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(e.name, fontSize = 16.sp)
                                        Text(
                                            if (e.value) "+" else "−",
                                            color = if (e.value) GreenCell else RedCell,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        )
                                    }
                                }
                                Button(
                                    onClick = { delIdx = idx },
                                    colors = ButtonDefaults.buttonColors(containerColor = RedCell),
                                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                                ) { Text("Rögzítés törlése", color = androidx.compose.ui.graphics.Color.White) }
                            }
                        }
                    }
                }
            }
        }
    }
}
