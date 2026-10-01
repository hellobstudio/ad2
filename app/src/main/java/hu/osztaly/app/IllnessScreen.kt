package hu.osztaly.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun IllnessScreen(repo: Repo, modifier: Modifier = Modifier) {
    if (repo.students.isEmpty()) {
        Box(modifier.fillMaxSize().padding(16.dp)) {
            Text("Nincs diák. Importáld őket a Beállítások fülön.")
        }
    } else {
        LazyColumn(
            modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(repo.students, key = { it.name }) { s ->
                var text by remember(s.name) { mutableStateOf(s.note) }
                SectionCard(s.name) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = {
                            text = it
                            repo.setNote(s.name, it)
                        },
                        label = { Text("Allergia, betegség, egyéb tudnivaló") },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
