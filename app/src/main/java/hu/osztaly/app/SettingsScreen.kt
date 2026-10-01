package hu.osztaly.app

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(repo: Repo, modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    var text by remember { mutableStateOf("") }
    var confirmClear by remember { mutableStateOf(false) }
    var delIdx by remember { mutableStateOf(-1) }

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            try {
                val t = ctx.contentResolver.openInputStream(uri)
                    ?.bufferedReader()?.use { it.readText() } ?: ""
                val n = repo.addStudents(t)
                Toast.makeText(ctx, "$n diák hozzáadva", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(ctx, "A fájl beolvasása nem sikerült", Toast.LENGTH_SHORT).show()
            }
        }
    }

    if (confirmClear) {
        ConfirmDialog(
            "Biztosan törlöd az ÖSSZES rögzítést? Ez nem vonható vissza.",
            onYes = {
                repo.clearRecords()
                confirmClear = false
                Toast.makeText(ctx, "Minden rögzítés törölve.", Toast.LENGTH_SHORT).show()
            },
            onNo = { confirmClear = false }
        )
    }
    if (delIdx >= 0 && delIdx < repo.students.size) {
        ConfirmDialog(
            "Törlöd: ${repo.students[delIdx].name}?",
            onYes = { repo.removeStudent(delIdx); delIdx = -1 },
            onNo = { delIdx = -1 }
        )
    }

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionCard("Megjelenés") {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Sötét téma", Modifier.weight(1f))
                Switch(checked = repo.darkTheme, onCheckedChange = { repo.setDark(it) })
            }
        }

        SectionCard("Diákok importálása") {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Egy sorba egy név") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    val n = repo.addStudents(text)
                    text = ""
                    Toast.makeText(ctx, "$n diák hozzáadva", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) { Text("Hozzáadás") }
            OutlinedButton(
                onClick = { picker.launch(arrayOf("*/*")) },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) { Text("Importálás fájlból (.txt / .csv)") }
        }

        SectionCard("Diákok (${repo.students.size})") {
            if (repo.students.isEmpty()) Text("Még nincs diák.")
            repo.students.forEachIndexed { i, s ->
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(s.name, Modifier.weight(1f))
                    IconButton(onClick = { delIdx = i }) {
                        Icon(Icons.Filled.Close, contentDescription = "Törlés", tint = RedCell)
                    }
                }
            }
        }

        SectionCard("Adatok") {
            Button(
                onClick = { confirmClear = true },
                colors = ButtonDefaults.buttonColors(containerColor = RedCell),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Minden rögzítés törlése", color = Color.White) }
        }
    }
}
